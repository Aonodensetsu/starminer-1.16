package jp.mc.ancientred.starminer.basics.ai;

import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.api.GravityDirection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.AxisAlignedBB;

public class EntityAILookIdleGEx
  extends EntityAIBase {
  private EntityLiving idleEntity;
  private double lookA;
  
  public EntityAILookIdleGEx(EntityLiving entityLiving) {
    this.idleEntity = entityLiving;
    setMutexBits(3);
  }
  
  private double lookB;
  private int idleTime;
  
  public boolean shouldExecute() {
    return (this.idleEntity.getRNG().nextFloat() < 0.02F);
  }

  public boolean continueExecuting() {
    return (this.idleTime >= 0);
  }

  public void startExecuting() {
    double d0 = 6.283185307179586D * this.idleEntity.getRNG().nextDouble();
    this.lookA = Math.cos(d0);
    this.lookB = Math.sin(d0);
    this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
  }

  public void updateTask() {
    this.idleTime--;
    AxisAlignedBB bb = this.idleEntity.boundingBox;
    double lookPosX = 0.0D;
    double lookPosY = 0.0D;
    double lookPosZ = 0.0D;
    switch (Gravity.getGravityDirection((Entity)this.idleEntity)) {
      case northTOsouth_ZP:
        lookPosX = bb.minX + (bb.maxX - bb.minX) / 2.0D + this.lookA;
        lookPosY = bb.minY + (bb.maxY - bb.minY) / 2.0D + this.lookB;
        if (this.idleEntity instanceof net.minecraft.entity.EntityLivingBase) {
          lookPosZ = bb.maxZ - this.idleEntity.getEyeHeight(); break;
        } 
        lookPosZ = (bb.minZ + bb.maxZ) / 2.0D;
        break;

      case southTOnorth_ZN:
        lookPosX = bb.minX + (bb.maxX - bb.minX) / 2.0D + this.lookA;
        lookPosY = bb.minY + (bb.maxY - bb.minY) / 2.0D + this.lookB;
        if (this.idleEntity instanceof net.minecraft.entity.EntityLivingBase) {
          lookPosZ = bb.minZ + this.idleEntity.getEyeHeight(); break;
        } 
        lookPosZ = (bb.minZ + bb.maxZ) / 2.0D;
        break;

      case westTOeast_XP:
        if (this.idleEntity instanceof net.minecraft.entity.EntityLivingBase) {
          lookPosX = bb.maxX - this.idleEntity.getEyeHeight();
        } else {
          lookPosX = (bb.minX + bb.maxX) / 2.0D;
        } 
        lookPosY = bb.minY + (bb.maxY - bb.minY) / 2.0D + this.lookA;
        lookPosZ = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D + this.lookB;
        break;
      
      case eastTOwest_XN:
        if (this.idleEntity instanceof net.minecraft.entity.EntityLivingBase) {
          lookPosX = bb.minX + this.idleEntity.getEyeHeight();
        } else {
          lookPosX = (bb.minX + bb.maxX) / 2.0D;
        } 
        lookPosY = bb.minY + (bb.maxY - bb.minY) / 2.0D + this.lookA;
        lookPosZ = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D + this.lookB;
        break;
      
      case downTOup_YP:
        lookPosX = bb.minX + (bb.maxX - bb.minX) / 2.0D + this.lookA;
        if (this.idleEntity instanceof net.minecraft.entity.EntityLivingBase) {
          lookPosY = bb.maxY - this.idleEntity.getEyeHeight();
        } else {
          lookPosY = (bb.minY + bb.maxY) / 2.0D;
        } 
        lookPosZ = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D + this.lookB;
        break;
      
      default:
        lookPosX = bb.minX + (bb.maxX - bb.minX) / 2.0D + this.lookB;
        if (this.idleEntity instanceof net.minecraft.entity.EntityLivingBase) {
          lookPosY = bb.minY + this.idleEntity.getEyeHeight();
        } else {
          lookPosY = (bb.minY + bb.maxY) / 2.0D;
        } 
        lookPosZ = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D + this.lookB;
        break;
    } 
    
    this.idleEntity.getLookHelper().setLookPosition(lookPosX, lookPosY, lookPosZ, 10.0F, this.idleEntity.getVerticalFaceSpeed());
  }
}
