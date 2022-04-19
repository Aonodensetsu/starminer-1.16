package jp.mc.ancientred.starminer.basics.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.entity.fx.EntityCometTailFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityComet
  extends EntityAmbientCreature
{
  private static final int DW_REDONE = 16;
  private Vec3 vecA = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
  private Vec3 vecB = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
  
  private boolean isResting = false;
  
  private int scaleWaveAngle;

  public EntityComet(World par1World) {
    super(par1World);
    setSize(0.95F, 0.95F);
    this.isImmuneToFire = true;
    this.ignoreFrustumCheck = true;
    this.renderDistanceWeight = 2.0D;
  }

  protected void entityInit() {
    super.entityInit();
    if (this.worldObj.rand.nextInt(12) == 0) {
      this.dataWatcher.addObject(16, Byte.valueOf((byte)1));
    } else {
      this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    } 
  }
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
  }
  
  public boolean shouldRenderInPass(int pass) {
    return (pass == 0 || pass == 1);
  }
  
  public boolean isRedOne() {
    return (this.dataWatcher.getWatchableObjectByte(16) != 0);
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
    if (isRedOne()) {
      entityDropItem(new ItemStack(SMModContainer.LifeSoupItem, 32, 0), 0.0F);
    } else {
      int j = this.rand.nextInt(3 + par2) + 1;
      for (int k = 0; k < j; k++) {
        entityDropItem(new ItemStack(SMModContainer.LifeSoupItem, 2 + this.rand.nextInt(2), 0), 0.0F);
      }
    } 
  }
  
  public boolean canBePushed() {
    return false;
  }
  public void onLivingUpdate() {
    super.onLivingUpdate();
    
    double movedX = this.posX - this.prevPosX;
    double movedY = this.posY - this.prevPosY;
    double movedZ = this.posZ - this.prevPosZ;
    
    double movedXZ = movedX * movedX + movedZ * movedZ;
    float xz = MathHelper.sqrt_double(movedXZ);
    float xyz = MathHelper.sqrt_double(movedXZ + movedY * movedY);
    
    this.rotationYaw = (float)(-90.0D + Math.atan2(movedZ, movedX) * 180.0D / Math.PI);
    this.rotationPitch = -((float)(Math.atan2(movedY, xz) * 180.0D / Math.PI));
    
    if (this.worldObj.isRemote)
    {
      if (xyz >= 0.2F) {
        this.scaleWaveAngle = 0;
        spawnCometTail();
      } else {
        this.scaleWaveAngle++;
      } 
    }
  }

  @SideOnly(Side.CLIENT)
  public void spawnCometTail() {
    float scale = 2.0F;
    AxisAlignedBB bb = this.boundingBox;
    double fxPosX = (bb.minX + bb.maxX) / 2.0D;
    double fxPosY = (bb.minY + bb.maxY) / 2.0D;
    double fxPosZ = (bb.minZ + bb.maxZ) / 2.0D;

    float yaw = this.rotationYaw;
    float pitch = this.rotationPitch;
    float f1 = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
    float f2 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
    float f3 = -MathHelper.cos(-pitch * 0.017453292F);
    float f4 = MathHelper.sin(-pitch * 0.017453292F);
    
    this.vecA.xCoord = (f2 * f3);
    this.vecA.yCoord = f4;
    this.vecA.zCoord = (f1 * f3);

    EntityCometTailFX entityCometTailFX = new EntityCometTailFX(this.worldObj, fxPosX, fxPosY, fxPosZ, scale);
    ((EntityFX)entityCometTailFX).motionX = -this.vecA.xCoord;
    ((EntityFX)entityCometTailFX).motionY = -this.vecA.yCoord;
    ((EntityFX)entityCometTailFX).motionZ = -this.vecA.zCoord;
    (Minecraft.getMinecraft()).effectRenderer.addEffect((EntityFX)entityCometTailFX);

    double fixX = -this.vecA.xCoord / 2.0D;
    double fixY = -this.vecA.yCoord / 2.0D;
    double fixZ = -this.vecA.zCoord / 2.0D;

    pitch = (this.rotationPitch + 90.0F + 180.0F) % 180.0F;
    f1 = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
    f2 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
    f3 = -MathHelper.cos(-pitch * 0.017453292F);
    f4 = MathHelper.sin(-pitch * 0.017453292F);
    this.vecB.xCoord = (f2 * f3);
    this.vecB.yCoord = f4;
    this.vecB.zCoord = (f1 * f3);

    entityCometTailFX = new EntityCometTailFX(this.worldObj, fxPosX, fxPosY, fxPosZ, scale);
    ((EntityFX)entityCometTailFX).motionX = this.vecB.xCoord + fixX;
    ((EntityFX)entityCometTailFX).motionY = this.vecB.yCoord + fixY;
    ((EntityFX)entityCometTailFX).motionZ = this.vecB.zCoord + fixZ;
    (Minecraft.getMinecraft()).effectRenderer.addEffect((EntityFX)entityCometTailFX);

    entityCometTailFX = new EntityCometTailFX(this.worldObj, fxPosX, fxPosY, fxPosZ, scale);
    ((EntityFX)entityCometTailFX).motionX = -this.vecB.xCoord + fixX;
    ((EntityFX)entityCometTailFX).motionY = -this.vecB.yCoord + fixX;
    ((EntityFX)entityCometTailFX).motionZ = -this.vecB.zCoord + fixX;
    (Minecraft.getMinecraft()).effectRenderer.addEffect((EntityFX)entityCometTailFX);

    double motX = this.vecA.yCoord * this.vecB.zCoord - this.vecA.zCoord * this.vecB.yCoord;
    double motY = this.vecA.zCoord * this.vecB.xCoord - this.vecA.xCoord * this.vecB.zCoord;
    double motZ = this.vecA.xCoord * this.vecB.yCoord - this.vecA.yCoord * this.vecB.xCoord;
    entityCometTailFX = new EntityCometTailFX(this.worldObj, fxPosX, fxPosY, fxPosZ, scale);
    ((EntityFX)entityCometTailFX).motionX = motX + fixX;
    ((EntityFX)entityCometTailFX).motionY = motY + fixY;
    ((EntityFX)entityCometTailFX).motionZ = motZ + fixZ;
    (Minecraft.getMinecraft()).effectRenderer.addEffect((EntityFX)entityCometTailFX);
    
    motX = this.vecB.yCoord * this.vecA.zCoord - this.vecB.zCoord * this.vecA.yCoord;
    motY = this.vecB.zCoord * this.vecA.xCoord - this.vecB.xCoord * this.vecA.zCoord;
    motZ = this.vecB.xCoord * this.vecA.yCoord - this.vecB.yCoord * this.vecA.xCoord;
    entityCometTailFX = new EntityCometTailFX(this.worldObj, fxPosX, fxPosY, fxPosZ, scale);
    ((EntityFX)entityCometTailFX).motionX = motX + fixX;
    ((EntityFX)entityCometTailFX).motionY = motY + fixY;
    ((EntityFX)entityCometTailFX).motionZ = motZ + fixZ;
    (Minecraft.getMinecraft()).effectRenderer.addEffect((EntityFX)entityCometTailFX);
  }
  
  public void moveEntityWithHeading(float par1, float par2) {
    moveEntity(this.motionX, this.motionY, this.motionZ);
  }
  
  public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
    if (!this.worldObj.isRemote) {
      this.isResting = false;
    }
    return super.attackEntityFrom(p_70097_1_, p_70097_2_);
  }
  protected void updateEntityActionState() {
    this.entityAge++;
    if (this.entityAge > 2000) {
      setDead();
    }
    if (this.isResting) {
      this.motionX *= 0.9D;
      this.motionY *= 0.9D;
      this.motionZ *= 0.9D;
      
      int restChange = 180;
      if (isRedOne()) {
        restChange = 60;
      }
      if (this.rand.nextInt(restChange) == 0) {
        this.isResting = false;
      }
    } else {
      int wayChange = 60;
      double speed = 0.2D;
      if (isRedOne()) {
        wayChange = 20;
        speed = 0.6D;
      } 
      if (this.rand.nextInt(wayChange) == 0 || this.isCollided) {
        double angleRad = this.rand.nextDouble() * Math.PI * 2.0D;
        double angleRad2 = this.rand.nextDouble() * Math.PI * 2.0D;
        double cos = Math.cos(angleRad2);
        speed += this.rand.nextDouble() * 0.8D;
        this.motionX = Math.sin(angleRad2) * speed;
        this.motionY = Math.cos(angleRad) * cos * speed;
        this.motionZ = Math.sin(angleRad) * cos * speed;
      } 
      if (this.rand.nextInt(180) == 0) {
        this.isResting = true;
      }
    } 

    if (this.posY <= 5.0D) {
      this.motionY = Math.abs(this.motionY);
    } else if (this.posY >= 250.0D) {
      this.motionY = -Math.abs(this.motionY);
    } 
    
    despawnEntity();
  }
  
  public float getScaleNow() {
    return MathHelper.cos((float)((this.scaleWaveAngle * 2) / 180.0D * Math.PI)) * 0.25F + 1.0F;
  }

  protected void fall(float par1) {}

  public boolean getCanSpawnHere() {
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.boundingBox.minY);
    int k = MathHelper.floor_double(this.posZ);
    if (this.worldObj.canBlockSeeTheSky(i, j, k) && 
      this.worldObj.getEntitiesWithinAABB(EntityComet.class, this.boundingBox.expand(64.0D, 64.0D, 64.0D)).isEmpty()) {
      return true;
    }
    
    return false;
  }
  
  public void writeEntityToNBT(NBTTagCompound tag) {
    super.writeEntityToNBT(tag);
    tag.setBoolean("isResting", this.isResting);
    tag.setByte("isRedOne", this.dataWatcher.getWatchableObjectByte(16));
  }
  
  public void readEntityFromNBT(NBTTagCompound tag) {
    super.readEntityFromNBT(tag);
    this.isResting = tag.getBoolean("isResting");
    this.dataWatcher.updateObject(16, Byte.valueOf(tag.getByte("isRedOne")));
  }
}
