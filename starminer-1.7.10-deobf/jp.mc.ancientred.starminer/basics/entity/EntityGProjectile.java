package jp.mc.ancientred.starminer.basics.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityGProjectile
  extends EntityArrow
  implements IProjectile
{
  private static final int DW_TYPE = 17;
  private static final int DW_RELATED_ENTITY = 18;
  private int xTile = -1;
  private int yTile = -1;
  private int zTile = -1;
  private Block inTile;
  private int inData;
  private boolean inGround;
  public int canBePickedUp;
  public int arrowShake;
  public Entity shootingEntity;
  private int ticksInGround;
  private int ticksInAir;
  private double damage = 2.0D;
  
  private int knockbackStrength;
  
  private int hitBlockSide;
  
  public enum GProjectileType
  {
    gArrow,
    gRappleHook;
  }

  public EntityGProjectile(World parWorld) {
    super(parWorld);
    this.renderDistanceWeight = 10.0D;
    setSize(0.5F, 0.5F);
  }

  public EntityGProjectile(World parWorld, double parX, double parY, double parZ) {
    super(parWorld);
    this.renderDistanceWeight = 10.0D;
    setSize(0.5F, 0.5F);
    setPosition(parX, parY, parZ);
    this.yOffset = 0.0F;
  }

  public EntityGProjectile(World parWorld, EntityPlayer parShooterEntity, float shootSpeed, GProjectileType type) {
    super(parWorld);
    setGProjectileType(type);
    this.renderDistanceWeight = 10.0D;
    this.shootingEntity = (Entity)parShooterEntity;
    setShooterEntityToDataWatcher(this.shootingEntity);
    
    if (type == GProjectileType.gArrow && parShooterEntity instanceof EntityPlayer)
    {
      this.canBePickedUp = 1;
    }
    setSize(0.5F, 0.5F);
    setLocationAndAngles(parShooterEntity.posX, parShooterEntity.posY + parShooterEntity.getEyeHeight(), parShooterEntity.posZ, parShooterEntity.rotationYaw, parShooterEntity.rotationPitch);

    Gravity gravity = Gravity.getGravityProp((Entity)parShooterEntity);
    if (gravity != null) gravity.setGravityFixedPlayerShootVec(parShooterEntity, (Entity)this, 1.0F);
    
    setThrowableHeading(this.motionX, this.motionY, this.motionZ, shootSpeed * 1.5F, 1.0F);
  }

  public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float shootSpeed, float p_70186_8_) {
    float f2 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
    p_70186_1_ /= f2;
    p_70186_3_ /= f2;
    p_70186_5_ /= f2;
    p_70186_1_ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : true) * 0.007499999832361937D * p_70186_8_;
    p_70186_3_ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : true) * 0.007499999832361937D * p_70186_8_;
    p_70186_5_ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : true) * 0.007499999832361937D * p_70186_8_;
    p_70186_1_ *= shootSpeed;
    p_70186_3_ *= shootSpeed;
    p_70186_5_ *= shootSpeed;
    this.motionX = p_70186_1_;
    this.motionY = p_70186_3_;
    this.motionZ = p_70186_5_;
    float f3 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
    this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / Math.PI);
    this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_70186_3_, f3) * 180.0D / Math.PI);
    this.ticksInGround = 0;
  }

  protected void entityInit() {
    this.ignoreFrustumCheck = true;
    this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(17, Integer.valueOf(0));
    this.dataWatcher.addObject(18, Integer.valueOf(0));
  }
  
  public void setShooterEntityToDataWatcher(Entity entity) {
    if (!this.worldObj.isRemote) {
      this.dataWatcher.updateObject(18, Integer.valueOf(entity.getEntityId()));
    }
  }
  
  public void setGProjectileType(GProjectileType type) {
    this.dataWatcher.updateObject(17, Integer.valueOf(type.ordinal()));
  }
  
  public GProjectileType getGProjectileType() {
    return GProjectileType.values()[this.dataWatcher.getWatchableObjectInt(17)];
  }

  
  public void onUpdate() {
    if (getGProjectileType() == GProjectileType.gRappleHook) {
      if (!this.worldObj.isRemote) {
        
        if (this.shootingEntity != null && this.shootingEntity instanceof EntityPlayerMP) {
          
          this.shootingEntity.fallDistance = 0.0F;

          Gravity gravity = Gravity.getGravityProp(this.shootingEntity);
          if (gravity != null) gravity.acceptExceptionalGravityTick = 45;
          
          ItemStack itemstack = ((EntityPlayerMP)this.shootingEntity).getCurrentEquippedItem();
          
          if (this.shootingEntity.isDead || !this.shootingEntity.isEntityAlive() || this.shootingEntity.isSneaking() || getDistanceSqToEntity(this.shootingEntity) > 3000.0D) {

            setDead();
            return;
          } 
        } else {
          setDead();
          
          return;
        } 
      } else {
        int EntityId = this.dataWatcher.getWatchableObjectInt(18);
        if (EntityId != 0) {
          this.shootingEntity = this.worldObj.getEntityByID(EntityId);
        }
        
        if (this.inGround && this.shootingEntity != null) {
          Entity entityToPull = this.shootingEntity;

          entityToPull.fallDistance = 0.0F;
          
          Gravity gravity = Gravity.getGravityProp(entityToPull);
          
          double distance = entityToPull.getDistance(this.posX, this.posY + 1.0D, this.posZ);
          
          double distX = this.posX - entityToPull.posX;
          double motionForX = distX / distance * 0.05D;

          double distY = this.posY + 1.0D - entityToPull.posY;
          double motionForY = distY / distance * 0.05D;

          double distZ = this.posZ - entityToPull.posZ;
          double motionForZ = distZ / distance * 0.05D;
          
          double acl = 4.0D;
          boolean ignore = false;
          boolean xNearCheck = false;
          boolean yNearCheck = false;
          boolean zNearCheck = false;
          GravityDirection againstWallGravityDir = GravityDirection.upTOdown_YN;
          switch (this.hitBlockSide) {
            case 0:
              againstWallGravityDir = GravityDirection.downTOup_YP;
              ignore = true;
            case 1:
              if (!ignore) againstWallGravityDir = GravityDirection.upTOdown_YN;
              
              motionForX *= acl;
              motionForZ *= acl;
              
              yNearCheck = true;
              break;
            case 2:
              againstWallGravityDir = GravityDirection.northTOsouth_ZP;
              ignore = true;
            case 3:
              if (!ignore) againstWallGravityDir = GravityDirection.southTOnorth_ZN;
              
              motionForX *= acl;
              motionForY *= acl;
              
              zNearCheck = true;
              break;
            case 4:
              againstWallGravityDir = GravityDirection.westTOeast_XP;
              ignore = true;
            case 5:
              if (!ignore) againstWallGravityDir = GravityDirection.eastTOwest_XN;
              
              motionForY *= acl;
              motionForZ *= acl;
              
              xNearCheck = true;
              break;
          } 
          entityToPull.motionX += motionForX;
          entityToPull.motionY += motionForY;
          entityToPull.motionZ += motionForZ;
          
          if (Math.abs(distX) < 1.0D) {
            entityToPull.motionX *= Math.abs(distX) / 50.0D;
            if (distX > 0.2D) {
              entityToPull.motionX += distX / Math.abs(distX) * 0.05D;
            }
            xNearCheck = true;
          } 
          if (Math.abs(distY) < 1.0D) {
            entityToPull.motionY *= Math.abs(distY) / 50.0D;
            if (distY > 0.2D) {
              entityToPull.motionY += distY / Math.abs(distY) * 0.05D;
            }
            yNearCheck = true;
          } 
          if (Math.abs(distZ) < 1.0D) {
            entityToPull.motionZ *= Math.abs(distZ) / 50.0D;
            if (distZ > 0.2D) {
              entityToPull.motionZ += distZ / Math.abs(distZ) * 0.05D;
            }
            zNearCheck = true;
          } 
          
          if ((xNearCheck && yNearCheck && zNearCheck) || distance < 4.0D) {
            gravity.setTemporaryGravityDirection(againstWallGravityDir, 15);
          } else {
            gravity.setTemporaryZeroGravity(2);
          } 
        } 
      } 
    }
    
    onUpdateOriginal();
  }

  @SideOnly(Side.CLIENT)
  public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_) {
    setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
    setRotation(p_70056_7_, p_70056_8_);
  }

  @SideOnly(Side.CLIENT)
  public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
    this.motionX = p_70016_1_;
    this.motionY = p_70016_3_;
    this.motionZ = p_70016_5_;
    
    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      
      float f = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
      this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
      this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_70016_3_, f) * 180.0D / Math.PI);
      this.prevRotationPitch = this.rotationPitch;
      this.prevRotationYaw = this.rotationYaw;
      setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      this.ticksInGround = 0;
    } 
  }

  public void onUpdateOriginal() {
    onEntityUpdate();
    
    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      
      float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
      this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, f) * 180.0D / Math.PI);
    } 
    
    Block block = this.worldObj.getBlock(this.xTile, this.xTile, this.zTile);
    
    if (block.getMaterial() != Material.air) {
      
      block.setBlockBoundsBasedOnState((IBlockAccess)this.worldObj, this.xTile, this.xTile, this.zTile);
      AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.xTile, this.zTile);
      
      if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ)))
      {
        this.inGround = true;
      }
    } 
    
    if (this.arrowShake > 0)
    {
      this.arrowShake--;
    }
    
    if (this.inGround) {
      
      int j = this.worldObj.getBlockMetadata(this.xTile, this.xTile, this.zTile);
      
      if (block == this.inTile && j == this.inData)
      {
        this.ticksInGround++;
        
        if (this.ticksInGround == 1200)
        {
          setDead();
        }
      }
      else
      {
        this.inGround = false;
        this.motionX *= (this.rand.nextFloat() * 0.2F);
        this.motionY *= (this.rand.nextFloat() * 0.2F);
        this.motionZ *= (this.rand.nextFloat() * 0.2F);
        this.ticksInGround = 0;
        this.ticksInAir = 0;
      }
    
    } else {
      
      this.ticksInAir++;
      Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
      Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
      vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
      vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      
      if (movingobjectposition != null)
      {
        vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
      }
      
      Entity entity = null;
      List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
      double d0 = 0.0D;
      
      int i;
      
      for (i = 0; i < list.size(); i++) {
        
        Entity entity1 = list.get(i);
        
        if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
          
          float f = 0.3F;
          AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f, f, f);
          MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);
          
          if (movingobjectposition1 != null) {
            
            double d1 = vec31.distanceTo(movingobjectposition1.hitVec);
            
            if (d1 < d0 || d0 == 0.0D) {
              
              entity = entity1;
              d0 = d1;
            } 
          } 
        } 
      } 
      
      if (entity != null)
      {
        movingobjectposition = new MovingObjectPosition(entity);
      }
      
      if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
        
        EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;
        
        if (entityplayer.capabilities.disableDamage || (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer)))
        {
          movingobjectposition = null;
        }
      } 

      if (movingobjectposition != null)
      {
        if (movingobjectposition.entityHit != null) {
          
          float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
          int k = MathHelper.ceiling_double_int(f * this.damage);
          
          if (getIsCritical())
          {
            k += this.rand.nextInt(k / 2 + 2);
          }
          
          DamageSource damagesource = null;
          
          if (this.shootingEntity == null) {
            
            damagesource = DamageSource.causeArrowDamage(this, (Entity)this);
          }
          else {
            
            damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
          } 
          
          if (isBurning() && !(movingobjectposition.entityHit instanceof net.minecraft.entity.monster.EntityEnderman))
          {
            movingobjectposition.entityHit.setFire(5);
          }
          
          if (movingobjectposition.entityHit.attackEntityFrom(damagesource, k)) {
            
            if (movingobjectposition.entityHit instanceof EntityLivingBase) {
              
              EntityLivingBase entitylivingbase = (EntityLivingBase)movingobjectposition.entityHit;
              
              if (!this.worldObj.isRemote)
              {
                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
              }
              
              if (this.knockbackStrength > 0) {
                
                float f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                
                if (f4 > 0.0F)
                {
                  movingobjectposition.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f4, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f4);
                }
              } 
              
              if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
                
                EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
                EnchantmentHelper.func_151385_b((EntityLivingBase)this.shootingEntity, (Entity)entitylivingbase);
              } 
              
              if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
              {
                ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(6, 0.0F));
              }
            } 
            
            playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            
            if (!(movingobjectposition.entityHit instanceof net.minecraft.entity.monster.EntityEnderman))
            {
              setDead();
            
            }
          
          }
          else if (getGProjectileType() == GProjectileType.gRappleHook) {
            setDead();
          } else {
            this.motionX *= -0.10000000149011612D;
            this.motionY *= -0.10000000149011612D;
            this.motionZ *= -0.10000000149011612D;
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            this.ticksInAir = 0;
          }
        
        }
        else {
          
          this.hitBlockSide = movingobjectposition.sideHit;
          this.xTile = movingobjectposition.blockX;
          this.xTile = movingobjectposition.blockY;
          this.zTile = movingobjectposition.blockZ;
          this.inTile = this.worldObj.getBlock(this.xTile, this.xTile, this.zTile);
          this.inData = this.worldObj.getBlockMetadata(this.xTile, this.xTile, this.zTile);
          this.motionX = (float)(movingobjectposition.hitVec.xCoord - this.posX);
          this.motionY = (float)(movingobjectposition.hitVec.yCoord - this.posY);
          this.motionZ = (float)(movingobjectposition.hitVec.zCoord - this.posZ);
          float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
          this.posX -= this.motionX / f * 0.05000000074505806D;
          this.posY -= this.motionY / f * 0.05000000074505806D;
          this.posZ -= this.motionZ / f * 0.05000000074505806D;
          playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
          this.inGround = true;
          this.arrowShake = 7;
          setIsCritical(false);
          
          if (this.inTile.getMaterial() != Material.air)
          {
            this.inTile.onEntityCollidedWithBlock(this.worldObj, this.xTile, this.xTile, this.zTile, (Entity)this);
          }
        } 
      }
      
      if (getIsCritical())
      {
        for (i = 0; i < 4; i++)
        {
          this.worldObj.spawnParticle("crit", this.posX + this.motionX * i / 4.0D, this.posY + this.motionY * i / 4.0D, this.posZ + this.motionZ * i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
        }
      }
      
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
      
      for (this.rotationPitch = (float)(Math.atan2(this.motionY, f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);

      while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
      {
        this.prevRotationPitch += 360.0F;
      }
      
      while (this.rotationYaw - this.prevRotationYaw < -180.0F)
      {
        this.prevRotationYaw -= 360.0F;
      }
      
      while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
      {
        this.prevRotationYaw += 360.0F;
      }
      
      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
      float f3 = 0.99F;
      float f1 = 0.05F;
      
      if (isInWater()) {
        
        for (int l = 0; l < 4; l++) {
          
          float f4 = 0.25F;
          this.worldObj.spawnParticle("bubble", this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ);
        } 
        
        f3 = 0.8F;
      } 
      
      if (isWet())
      {
        extinguish();
      }
      
      if (getGProjectileType() == GProjectileType.gArrow && !(this.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider)) {
        this.motionX *= f3;
        this.motionY *= f3;
        this.motionZ *= f3;
        this.motionY -= f1;
      } 
      setPosition(this.posX, this.posY, this.posZ);
      doBlockCollisions();
    } 
  }

  public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    p_70014_1_.setShort("gpType", (short)getGProjectileType().ordinal());
    p_70014_1_.setShort("xTile", (short)this.xTile);
    p_70014_1_.setShort("yTile", (short)this.xTile);
    p_70014_1_.setShort("zTile", (short)this.zTile);
    p_70014_1_.setShort("life", (short)this.ticksInGround);
    p_70014_1_.setByte("inTile", (byte)Block.getIdFromBlock(this.inTile));
    p_70014_1_.setByte("inData", (byte)this.inData);
    p_70014_1_.setByte("shake", (byte)this.arrowShake);
    p_70014_1_.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    p_70014_1_.setByte("pickup", (byte)this.canBePickedUp);
    p_70014_1_.setDouble("damage", this.damage);
  }

  public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    setGProjectileType(GProjectileType.values()[p_70037_1_.getShort("gpType")]);
    this.xTile = p_70037_1_.getShort("xTile");
    this.xTile = p_70037_1_.getShort("yTile");
    this.zTile = p_70037_1_.getShort("zTile");
    this.ticksInGround = p_70037_1_.getShort("life");
    this.inTile = Block.getBlockById(p_70037_1_.getByte("inTile") & 0xFF);
    this.inData = p_70037_1_.getByte("inData") & 0xFF;
    this.arrowShake = p_70037_1_.getByte("shake") & 0xFF;
    this.inGround = (p_70037_1_.getByte("inGround") == 1);
    
    if (p_70037_1_.hasKey("damage", 99))
    {
      this.damage = p_70037_1_.getDouble("damage");
    }
    
    if (p_70037_1_.hasKey("pickup", 99)) {
      
      this.canBePickedUp = p_70037_1_.getByte("pickup");
    }
    else if (p_70037_1_.hasKey("player", 99)) {
      
      this.canBePickedUp = p_70037_1_.getBoolean("player") ? 1 : 0;
    } 
  }

  public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
    if (getGProjectileType() == GProjectileType.gArrow && 
      !this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
      
      boolean flag = (this.canBePickedUp == 1 || (this.canBePickedUp == 2 && p_70100_1_.capabilities.isCreativeMode));
      
      if (this.canBePickedUp == 1 && !p_70100_1_.inventory.addItemStackToInventory(new ItemStack(SMModContainer.GArrowItem, 1)))
      {
        flag = false;
      }
      
      if (flag) {
        
        playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        p_70100_1_.onItemPickup((Entity)this, 1);
        setDead();
      } 
    } 
  }

  protected boolean canTriggerWalking() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public float getShadowSize() {
    return 0.0F;
  }

  public void setDamage(double p_70239_1_) {
    this.damage = p_70239_1_;
  }

  public double getDamage() {
    return this.damage;
  }

  public void setKnockbackStrength(int p_70240_1_) {
    this.knockbackStrength = p_70240_1_;
  }

  public boolean canAttackWithItem() {
    return false;
  }

  public void setIsCritical(boolean p_70243_1_) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(16);
    
    if (p_70243_1_) {
      
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
    }
    else {
      
      this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
    } 
  }

  public boolean getIsCritical() {
    byte b0 = this.dataWatcher.getWatchableObjectByte(16);
    return ((b0 & 0x1) != 0);
  }
}
