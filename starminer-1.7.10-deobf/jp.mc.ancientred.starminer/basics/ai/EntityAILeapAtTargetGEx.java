package jp.mc.ancientred.starminer.basics.ai;

import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAILeapAtTargetGEx extends EntityAIBase {
  EntityLiving leaper;
  
  public EntityAILeapAtTargetGEx(EntityLiving p_i1630_1_, float p_i1630_2_) {
    this.leaper = p_i1630_1_;
    this.leapMotionVertical = p_i1630_2_;
    setMutexBits(5);
  }
  
  EntityLivingBase leapTarget;
  float leapMotionVertical;
  
  public boolean shouldExecute() {
    this.leapTarget = this.leaper.getAttackTarget();
    
    if (this.leapTarget == null) {
      return false;
    }
    double d0 = this.leaper.getDistanceSqToEntity((Entity)this.leapTarget);
    return (d0 >= 4.0D && d0 <= 16.0D) ? (!this.leaper.onGround ? false : ((this.leaper.getRNG().nextInt(5) == 0))) : false;
  }

  public boolean continueExecuting() {
    return !this.leaper.onGround;
  }

  public void startExecuting() {
    double distY;
    switch (Gravity.getGravityDirection((Entity)this.leaper)) {
      case northTOsouth_ZP:
        distX = this.leapTarget.posX - this.leaper.posX;
        distY = this.leapTarget.posY - this.leaper.posY;
        dist = Math.sqrt(distX * distX + distY * distY);
        this.leaper.motionX += distX / dist * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
        this.leaper.motionY += distY / dist * 0.5D * 0.800000011920929D + this.leaper.motionY * 0.20000000298023224D;
        this.leaper.motionZ = -(this.leapMotionVertical);
        return;
      
      case southTOnorth_ZN:
        distX = this.leapTarget.posX - this.leaper.posX;
        distY = this.leapTarget.posY - this.leaper.posY;
        dist = Math.sqrt(distX * distX + distY * distY);
        this.leaper.motionX += distX / dist * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
        this.leaper.motionY += distY / dist * 0.5D * 0.800000011920929D + this.leaper.motionY * 0.20000000298023224D;
        this.leaper.motionZ = this.leapMotionVertical;
        return;
      
      case westTOeast_XP:
        distY = this.leapTarget.posY - this.leaper.posY;
        distZ = this.leapTarget.posZ - this.leaper.posZ;
        dist = Math.sqrt(distY * distY + distZ * distZ);
        this.leaper.motionY += distY / dist * 0.5D * 0.800000011920929D + this.leaper.motionY * 0.20000000298023224D;
        this.leaper.motionZ += distZ / dist * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
        this.leaper.motionX = -(this.leapMotionVertical);
        return;
      
      case eastTOwest_XN:
        distY = this.leapTarget.posY - this.leaper.posY;
        distZ = this.leapTarget.posZ - this.leaper.posZ;
        dist = Math.sqrt(distY * distY + distZ * distZ);
        this.leaper.motionY += distY / dist * 0.5D * 0.800000011920929D + this.leaper.motionY * 0.20000000298023224D;
        this.leaper.motionZ += distZ / dist * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
        this.leaper.motionX = this.leapMotionVertical;
        return;
      
      case downTOup_YP:
        distX = this.leapTarget.posX - this.leaper.posX;
        distZ = this.leapTarget.posZ - this.leaper.posZ;
        dist = Math.sqrt(distX * distX + distZ * distZ);
        this.leaper.motionX += distX / dist * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
        this.leaper.motionZ += distZ / dist * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
        this.leaper.motionY = -(this.leapMotionVertical);
        return;
    } 
    
    double distX = this.leapTarget.posX - this.leaper.posX;
    double distZ = this.leapTarget.posZ - this.leaper.posZ;
    double dist = Math.sqrt(distX * distX + distZ * distZ);
    this.leaper.motionX += distX / dist * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
    this.leaper.motionZ += distZ / dist * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
    this.leaper.motionY = this.leapMotionVertical;
  }
}
