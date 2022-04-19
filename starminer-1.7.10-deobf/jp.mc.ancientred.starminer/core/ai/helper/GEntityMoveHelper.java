package jp.mc.ancientred.starminer.core.ai.helper;

import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class GEntityMoveHelper
  extends EntityMoveHelper
{
  private EntityLiving entity;
  private double posX;
  private double posY;
  private double[] dWork = new double[3]; private double posZ; private double speed;
  private boolean update;
  
  public GEntityMoveHelper(EntityLiving entityLiving) {
    super(entityLiving);
    this.entity = entityLiving;
    this.posX = entityLiving.posX;
    this.posY = entityLiving.posY;
    this.posZ = entityLiving.posZ;
  }

  public boolean isUpdating() {
    return this.update;
  }

  public double getSpeed() {
    return this.speed;
  }

  public void setMoveTo(double p_75642_1_, double p_75642_3_, double p_75642_5_, double p_75642_7_) {
    this.posX = p_75642_1_;
    this.posY = p_75642_3_;
    this.posZ = p_75642_5_;
    this.speed = p_75642_7_;
    this.update = true;
  }

  public void onUpdateMoveHelper() {
    this.entity.setMoveForward(0.0F);
    
    if (this.update) {
      
      this.update = false;
      switch (ExtendedPropertyGravity.getGravityDirection((Entity)this.entity)) {
        case northTOsouth_ZP:
          updateMoveZP();
          return;
        case southTOnorth_ZN:
          updateMoveZN();
          return;
        case westTOeast_XP:
          updateMoveXP();
          return;
        case eastTOwest_XN:
          updateMoveXN();
          return;
        case downTOup_YP:
          updateMoveYP();
          return;
      } 
      updateMoveYN();
    } 
  }

  private void getEntityPos(double[] ret, GravityDirection gdir) {
    AxisAlignedBB bb = this.entity.boundingBox;
    switch (gdir) {
      case southTOnorth_ZN:
        ret[0] = bb.minX + (bb.maxX - bb.minX) / 2.0D;
        ret[1] = bb.minY + (bb.maxY - bb.minY) / 2.0D;
        ret[2] = bb.minZ + 0.5D;
        return;
      case northTOsouth_ZP:
        ret[0] = bb.minX + (bb.maxX - bb.minX) / 2.0D;
        ret[1] = bb.minY + (bb.maxY - bb.minY) / 2.0D;
        ret[2] = bb.maxZ - 0.5D;
        return;
      case westTOeast_XP:
        ret[0] = bb.maxX - 0.5D;
        ret[1] = bb.minY + (bb.maxY - bb.minY) / 2.0D;
        ret[2] = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
        return;
      case eastTOwest_XN:
        ret[0] = bb.minX + 0.5D;
        ret[1] = bb.minY + (bb.maxY - bb.minY) / 2.0D;
        ret[2] = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
        return;
      case downTOup_YP:
        ret[0] = bb.minX + (bb.maxX - bb.minX) / 2.0D;
        ret[1] = bb.maxY - 0.5D;
        ret[2] = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
        return;
    } 
    ret[0] = bb.minX + (bb.maxX - bb.minX) / 2.0D;
    ret[1] = bb.minY + 0.5D;
    ret[2] = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
  }

  private void updateMoveZP() {
    getEntityPos(this.dWork, GravityDirection.northTOsouth_ZP);
    double diffX = this.posX - this.dWork[0];
    double diffY = this.posY - this.dWork[1];
    double diffZ = this.posZ - this.dWork[2];
    double dist = diffX * diffX + diffY * diffY + diffZ * diffZ;
    
    if (dist >= 2.500000277905201E-7D) {
      
      float f = (float)(Math.atan2(diffY, diffX) * 180.0D / Math.PI) - 90.0F;
      this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, f, 30.0F);
      this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
      
      if (diffZ < 0.0D && diffX * diffX + diffY * diffY < 1.0D)
      {
        this.entity.getJumpHelper().setJumping();
      }
    } 
  }
  
  private void updateMoveZN() {
    getEntityPos(this.dWork, GravityDirection.southTOnorth_ZN);
    double diffX = this.posX - this.dWork[0];
    double diffY = this.posY - this.dWork[1];
    double diffZ = this.posZ - this.dWork[2];
    double dist = diffX * diffX + diffY * diffY + diffZ * diffZ;
    
    if (dist >= 2.500000277905201E-7D) {
      
      float f = (float)(Math.atan2(-diffY, diffX) * 180.0D / Math.PI) - 90.0F;
      this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, f, 30.0F);
      this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
      
      if (diffZ > 0.0D && diffX * diffX + diffY * diffY < 1.0D)
      {
        this.entity.getJumpHelper().setJumping();
      }
    } 
  }
  
  private void updateMoveXP() {
    getEntityPos(this.dWork, GravityDirection.westTOeast_XP);
    double diffX = this.posX - this.dWork[0];
    double diffY = this.posY - this.dWork[1];
    double diffZ = this.posZ - this.dWork[2];
    double dist = diffX * diffX + diffY * diffY + diffZ * diffZ;
    
    if (dist >= 2.500000277905201E-7D) {
      
      float f = (float)(Math.atan2(diffZ, diffY) * 180.0D / Math.PI) - 90.0F;
      this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, f, 30.0F);
      this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
      
      if (diffX < 0.0D && diffY * diffY + diffZ * diffZ < 1.0D)
      {
        this.entity.getJumpHelper().setJumping();
      }
    } 
  }
  
  private void updateMoveXN() {
    getEntityPos(this.dWork, GravityDirection.eastTOwest_XN);
    double diffX = this.posX - this.dWork[0];
    double diffY = this.posY - this.dWork[1];
    double diffZ = this.posZ - this.dWork[2];
    double dist = diffX * diffX + diffY * diffY + diffZ * diffZ;
    
    if (dist >= 2.500000277905201E-7D) {
      
      float f = (float)(Math.atan2(diffZ, -diffY) * 180.0D / Math.PI) - 90.0F;
      this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, f, 30.0F);
      this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
      
      if (diffX > 0.0D && diffY * diffY + diffZ * diffZ < 1.0D)
      {
        this.entity.getJumpHelper().setJumping();
      }
    } 
  }
  
  private void updateMoveYP() {
    getEntityPos(this.dWork, GravityDirection.downTOup_YP);
    double diffX = this.posX - this.dWork[0];
    double diffY = this.posY - this.dWork[1];
    double diffZ = this.posZ - this.dWork[2];
    double dist = diffX * diffX + diffY * diffY + diffZ * diffZ;
    
    if (dist >= 2.500000277905201E-7D) {
      
      float f = (float)(Math.atan2(-diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
      this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, f, 30.0F);
      this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
      
      if (diffY < 0.0D && diffX * diffX + diffZ * diffZ < 1.0D)
      {
        this.entity.getJumpHelper().setJumping();
      }
    } 
  }
  
  private void updateMoveYN() {
    getEntityPos(this.dWork, GravityDirection.upTOdown_YN);
    double diffX = this.posX - this.dWork[0];
    double diffY = this.posY - this.dWork[1];
    double diffZ = this.posZ - this.dWork[2];
    double dist = diffX * diffX + diffY * diffY + diffZ * diffZ;
    
    if (dist >= 2.500000277905201E-7D) {
      
      float f = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
      this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, f, 30.0F);
      this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
      
      if (diffY > 0.0D && diffX * diffX + diffZ * diffZ < 1.0D)
      {
        this.entity.getJumpHelper().setJumping();
      }
    } 
  }

  private float limitAngle(float yawHead, float yawOffset, float diffLimit) {
    float f3 = MathHelper.wrapAngleTo180_float(yawOffset - yawHead);
    
    if (f3 > diffLimit)
    {
      f3 = diffLimit;
    }
    
    if (f3 < -diffLimit)
    {
      f3 = -diffLimit;
    }
    
    return yawHead + f3;
  }
}
