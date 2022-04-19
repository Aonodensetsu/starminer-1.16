package jp.mc.ancientred.starminer.basics.entity;

import jp.mc.ancientred.starminer.basics.packet.SMPacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityReRideAttacher extends Entity {
  private int entityLimit = 150;
  private Entity toRideEntity;
  private Entity toBeRiddenEntity;
  
  public EntityReRideAttacher(World world) {
    super(world);
    this.isImmuneToFire = true;
    this.noClip = true;
  }
  
  public void setToRideEntityAtServer(Entity entity) {
    this.toRideEntity = entity;
    setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
  }
  
  public void setToBeRiddenEntityAtServer(Entity entity) {
    this.toBeRiddenEntity = entity;
  }

  protected void entityInit() {
    setInvisible(true);
  }
  
  public boolean shouldRenderInPass(int pass) {
    return false;
  }
  public boolean canBeCollidedWith() {
    return false;
  } public float getShadowSize() {
    return 0.0F;
  } public boolean isEntityInvulnerable() {
    return true;
  } public boolean canAttackWithItem() {
    return false;
  } public boolean isPushedByWater() {
    return false;
  }
  public void setPositionAndRotation2(double posX, double posY, double posZ, float rotationYaw, float rotationPitch, int parInt) {
    setPosition(posX, posY, posZ);
  }

  public void onUpdate() {
    super.onUpdate();
    
    if (--this.entityLimit > 0) {
      if (!this.worldObj.isRemote)
      {
        if (this.toRideEntity != null && this.toBeRiddenEntity != null) {
          if (this.toRideEntity.ridingEntity == this.toBeRiddenEntity && this.toBeRiddenEntity.riddenByEntity == this.toRideEntity) {

            
            if (this.entityLimit % 5 == 0) {
              SMPacketSender.sendReRidePacket(this.toRideEntity, this.toBeRiddenEntity);
            }
          } else {
            
            setDead();
          } 
        } else {
          
          setDead();
        } 
      }
    } else {
      
      setDead();
    } 
  }
  
  protected void readEntityFromNBT(NBTTagCompound tag) {}
  
  protected void writeEntityToNBT(NBTTagCompound tag) {}
}
