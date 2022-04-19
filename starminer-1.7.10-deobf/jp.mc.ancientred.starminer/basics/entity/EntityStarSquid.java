package jp.mc.ancientred.starminer.basics.entity;

import jp.mc.ancientred.starminer.api.Gravity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityStarSquid
  extends EntityAmbientCreature
{
  public float squidPitch;
  public float prevSquidPitch;
  public float squidYaw;
  public float prevSquidYaw;
  public float squidRotation;
  public float prevSquidRotation;
  public float tentacleAngle;
  public float prevTentacleAngle;
  private float randomMotionSpeed;
  private float rotationVelocity;
  private float field_70871_bB;
  private float randomMotionVecX;
  private float randomMotionVecY;
  private float randomMotionVecZ;
  private boolean isBazookaSquid = false;
  
  public EntityStarSquid(World par1World) {
    super(par1World);
    setSize(0.95F, 0.95F);
    this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
    this.isImmuneToFire = true;
  }
  
  public EntityStarSquid(World par1World, boolean isBazookaSquid) {
    this(par1World);
    this.isBazookaSquid = isBazookaSquid;
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, Integer.valueOf(0));
  }
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
  }

  protected String getLivingSound() {
    return null;
  }

  protected String getHurtSound() {
    return null;
  }

  protected String getDeathSound() {
    return null;
  }

  protected float getSoundVolume() {
    return 0.4F;
  }

  protected int getDropItemId() {
    return 0;
  }

  protected boolean canTriggerWalking() {
    return false;
  }

  protected void dropFewItems(boolean par1, int par2) {
    int j = this.rand.nextInt(3 + par2) + 1;
    
    for (int k = 0; k < j; k++) {
      entityDropItem(new ItemStack(Items.gunpowder, 1, 0), 0.0F);
    }
  }

  public void onLivingUpdate() {
    if (this.worldObj.isRemote) {
      int toBeRidByEntityId = this.dataWatcher.getWatchableObjectInt(16);
      if (toBeRidByEntityId != 0) {
        Entity toBeRidByEntity = this.worldObj.getEntityByID(toBeRidByEntityId);
        if (toBeRidByEntity != null) {
          if (toBeRidByEntity.ridingEntity == this) {
            this.dataWatcher.updateObject(16, Integer.valueOf(0));
          } else if (Math.abs(toBeRidByEntity.posX - this.posX) < 16.0D && Math.abs(toBeRidByEntity.posZ - this.posZ) < 16.0D) {
            
            toBeRidByEntity.mountEntity((Entity)this);
          } 
        }
        return;
      } 
    } 
    if (this.isBazookaSquid) {
      if (this.riddenByEntity != null) {
        this.riddenByEntity.fallDistance = 0.0F;
        if (!this.worldObj.isRemote) {
          
          Gravity gravity = Gravity.getGravityProp(this.riddenByEntity);
          if (gravity != null) gravity.setResistInOpaqueBlockDamegeTick(20); 
        } 
      } 
      super.onLivingUpdate();
      
      if (!this.worldObj.isRemote) {
        if (this.isCollided || this.riddenByEntity == null) {
          setDead();
          
          return;
        } 
        if (!(this.worldObj.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider)) {
          this.motionX *= 0.9870000190734863D;
          this.motionY *= 0.9870000190734863D;
          this.motionZ *= 0.9870000190734863D;
          double totalSpeed = Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
          if (totalSpeed < 0.6D) {
            this.motionY -= 0.4D;
          }
        } 
      } 
      
      this.prevSquidPitch = this.squidPitch;
      this.prevSquidYaw = this.squidYaw;
      this.prevSquidRotation = this.squidRotation;
      this.prevTentacleAngle = this.tentacleAngle;
      this.squidRotation += this.rotationVelocity;
      
      if (this.squidRotation > 6.2831855F) {
        this.squidRotation -= 6.2831855F;
        
        if (this.rand.nextInt(10) == 0) {
          this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
        }
      } 
      
      float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.renderYawOffset += (-((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / 3.1415927F - this.renderYawOffset) * 0.1F;
      this.rotationYaw = this.renderYawOffset;
      this.squidPitch += (-((float)Math.atan2(f, this.motionY)) * 180.0F / 3.1415927F - this.squidPitch) * 0.5F;
    } else {
      super.onLivingUpdate();
      this.prevSquidPitch = this.squidPitch;
      this.prevSquidYaw = this.squidYaw;
      this.prevSquidRotation = this.squidRotation;
      this.prevTentacleAngle = this.tentacleAngle;
      this.squidRotation += this.rotationVelocity;
      
      if (this.squidRotation > 6.2831855F) {
        
        this.squidRotation -= 6.2831855F;
        
        if (this.rand.nextInt(10) == 0)
        {
          this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
        }
      } 

      if (this.squidRotation < 3.1415927F) {
        
        float f1 = this.squidRotation / 3.1415927F;
        this.tentacleAngle = MathHelper.sin(f1 * f1 * 3.1415927F) * 3.1415927F * 0.25F;
        
        if (f1 > 0.75D)
        {
          this.randomMotionSpeed = 1.0F;
          this.field_70871_bB = 1.0F;
        }
        else
        {
          this.field_70871_bB *= 0.8F;
        }
      
      } else {
        
        this.tentacleAngle = 0.0F;
        this.randomMotionSpeed *= 0.9F;
        this.field_70871_bB *= 0.99F;
      } 
      
      if (!this.worldObj.isRemote) {
        
        this.motionX = (this.randomMotionVecX * this.randomMotionSpeed);
        this.motionY = (this.randomMotionVecY * this.randomMotionSpeed);
        this.motionZ = (this.randomMotionVecZ * this.randomMotionSpeed);
      } 
      
      float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.renderYawOffset += (-((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / 3.1415927F - this.renderYawOffset) * 0.1F;
      this.rotationYaw = this.renderYawOffset;
      this.squidYaw += 3.1415927F * this.field_70871_bB * 1.5F;
      this.squidPitch += (-((float)Math.atan2(f, this.motionY)) * 180.0F / 3.1415927F - this.squidPitch) * 0.1F;
    } 
  }
  
  public void onDeath(DamageSource par1DamageSource) {
    if (this.riddenByEntity != null) {
      this.riddenByEntity.fallDistance = 0.0F;
    }
    super.onDeath(par1DamageSource);
  }

  protected void fall(float par1) {}

  public void moveEntityWithHeading(float par1, float par2) {
    moveEntity(this.motionX, this.motionY, this.motionZ);
  }
  
  protected void updateEntityActionState() {
    if (this.isBazookaSquid) {
      super.updateEntityActionState();
    } else {
      this.entityAge++;
      
      if (this.entityAge > 100) {
        
        this.randomMotionVecX = this.randomMotionVecY = this.randomMotionVecZ = 0.0F;
      }
      else if (this.rand.nextInt(50) == 0 || !this.inWater || (this.randomMotionVecX == 0.0F && this.randomMotionVecY == 0.0F && this.randomMotionVecZ == 0.0F)) {
        
        float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
        this.randomMotionVecX = MathHelper.cos(f) * 0.2F;
        this.randomMotionVecY = -0.1F + this.rand.nextFloat() * 0.2F;
        this.randomMotionVecZ = MathHelper.sin(f) * 0.2F;
      } 
      
      despawnEntity();
    } 
  }

  public boolean getCanSpawnHere() {
    if (this.isBazookaSquid) return true; 
    return this.worldObj.checkNoEntityCollision(this.boundingBox);
  }
  
  public void writeEntityToNBT(NBTTagCompound tag) {
    super.writeEntityToNBT(tag);
    tag.setBoolean("isBazookaSquid", this.isBazookaSquid);
  }
  
  public void readEntityFromNBT(NBTTagCompound tag) {
    super.readEntityFromNBT(tag);
    this.isBazookaSquid = tag.getBoolean("isBazookaSquid");
  }
}
