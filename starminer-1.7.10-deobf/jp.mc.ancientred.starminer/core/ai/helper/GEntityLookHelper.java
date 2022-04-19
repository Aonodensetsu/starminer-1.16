package jp.mc.ancientred.starminer.core.ai.helper;

import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class GEntityLookHelper
  extends EntityLookHelper {
  private EntityLiving entity;
  private float deltaLookYaw;
  private float deltaLookPitch;
  private boolean isLooking;
  private double posX;
  private double posY;
  private double posZ;
  
  public GEntityLookHelper(EntityLiving entityLiving) {
    super(entityLiving);
    this.entity = entityLiving;
  }

  public void setLookPositionWithEntity(Entity lookingAt, float deltaLookYaw, float deltaLookPitch) {
    AxisAlignedBB bb = lookingAt.boundingBox;
    switch (ExtendedPropertyGravity.getGravityDirection(lookingAt)) {
      case northTOsouth_ZP:
        this.posX = bb.minX + (bb.maxX - bb.minX) / 2.0D;
        this.posY = bb.minY + (bb.maxY - bb.minY) / 2.0D;
        if (lookingAt instanceof net.minecraft.entity.EntityLivingBase) {
          this.posZ = bb.maxZ - lookingAt.getEyeHeight(); break;
        } 
        this.posZ = (bb.minZ + bb.maxZ) / 2.0D;
        break;

      case southTOnorth_ZN:
        this.posX = bb.minX + (bb.maxX - bb.minX) / 2.0D;
        this.posY = bb.minY + (bb.maxY - bb.minY) / 2.0D;
        if (lookingAt instanceof net.minecraft.entity.EntityLivingBase) {
          this.posZ = bb.minZ + lookingAt.getEyeHeight(); break;
        } 
        this.posZ = (bb.minZ + bb.maxZ) / 2.0D;
        break;

      case westTOeast_XP:
        if (lookingAt instanceof net.minecraft.entity.EntityLivingBase) {
          this.posX = bb.maxX - lookingAt.getEyeHeight();
        } else {
          this.posX = (bb.minX + bb.maxX) / 2.0D;
        } 
        this.posY = bb.minY + (bb.maxY - bb.minY) / 2.0D;
        this.posZ = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
        break;
      
      case eastTOwest_XN:
        if (lookingAt instanceof net.minecraft.entity.EntityLivingBase) {
          this.posX = bb.minX + lookingAt.getEyeHeight();
        } else {
          this.posX = (bb.minX + bb.maxX) / 2.0D;
        } 
        this.posY = bb.minY + (bb.maxY - bb.minY) / 2.0D;
        this.posZ = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
        break;
      
      case downTOup_YP:
        this.posX = bb.minX + (bb.maxX - bb.minX) / 2.0D;
        if (lookingAt instanceof net.minecraft.entity.EntityLivingBase) {
          this.posY = bb.maxY - lookingAt.getEyeHeight();
        } else {
          this.posY = (bb.minY + bb.maxY) / 2.0D;
        } 
        this.posZ = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
        break;
      
      default:
        this.posX = bb.minX + (bb.maxX - bb.minX) / 2.0D;
        if (lookingAt instanceof net.minecraft.entity.EntityLivingBase) {
          this.posY = bb.minY + lookingAt.getEyeHeight();
        } else {
          this.posY = (bb.minY + bb.maxY) / 2.0D;
        } 
        this.posZ = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
        break;
    } 

    this.deltaLookYaw = deltaLookYaw;
    this.deltaLookPitch = deltaLookPitch;
    this.isLooking = true;
  }

  public void setLookPosition(double posX, double posY, double posZ, float deltaLookYaw, float deltaLookPitch) {
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
    this.deltaLookYaw = deltaLookYaw;
    this.deltaLookPitch = deltaLookPitch;
    this.isLooking = true;
  }

  public void onUpdateLook() {
    EntityLiving entityLiving = this.entity;
    ((Entity)entityLiving).rotationPitch = 0.0F;
    
    if (this.isLooking) {
      double diffX, diffY, diffZ, diffElse;
      this.isLooking = false;
      
      float yaw = 0.0F;
      float pitch = 0.0F;
      AxisAlignedBB bb = ((Entity)entityLiving).boundingBox;
      switch (ExtendedPropertyGravity.getGravityDirection((Entity)entityLiving)) {
        case northTOsouth_ZP:
          diffX = this.posX - bb.minX + (bb.maxX - bb.minX) / 2.0D;
          diffY = this.posY - bb.minY + (bb.maxY - bb.minY) / 2.0D;
          diffZ = this.posZ - bb.maxZ - entityLiving.getEyeHeight();
          
          diffElse = MathHelper.sqrt_double(diffX * diffX + diffY * diffY);
          yaw = (float)(Math.atan2(diffY, diffX) * 180.0D / Math.PI) - 90.0F;
          pitch = (float)-(Math.atan2(-diffZ, diffElse) * 180.0D / Math.PI);
          break;
        case southTOnorth_ZN:
          diffX = this.posX - bb.minX + (bb.maxX - bb.minX) / 2.0D;
          diffY = this.posY - bb.minY + (bb.maxY - bb.minY) / 2.0D;
          diffZ = this.posZ - bb.minZ + entityLiving.getEyeHeight();
          
          diffElse = MathHelper.sqrt_double(diffX * diffX + diffY * diffY);
          yaw = (float)(Math.atan2(-diffY, diffX) * 180.0D / Math.PI) - 90.0F;
          pitch = (float)-(Math.atan2(diffZ, diffElse) * 180.0D / Math.PI);
          break;
        case westTOeast_XP:
          diffX = this.posX - bb.maxX - entityLiving.getEyeHeight();
          diffY = this.posY - bb.minY + (bb.maxY - bb.minY) / 2.0D;
          diffZ = this.posZ - bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
          
          diffElse = MathHelper.sqrt_double(diffY * diffY + diffZ * diffZ);
          yaw = (float)(Math.atan2(diffZ, diffY) * 180.0D / Math.PI) - 90.0F;
          pitch = (float)-(Math.atan2(-diffX, diffElse) * 180.0D / Math.PI);
          break;
        case eastTOwest_XN:
          diffX = this.posX - bb.minX + entityLiving.getEyeHeight();
          diffY = this.posY - bb.minY + (bb.maxY - bb.minY) / 2.0D;
          diffZ = this.posZ - bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
          
          diffElse = MathHelper.sqrt_double(diffY * diffY + diffZ * diffZ);
          yaw = (float)(Math.atan2(diffZ, -diffY) * 180.0D / Math.PI) - 90.0F;
          pitch = (float)-(Math.atan2(diffX, diffElse) * 180.0D / Math.PI);
          break;
        case downTOup_YP:
          diffX = this.posX - bb.minX + (bb.maxX - bb.minX) / 2.0D;
          diffY = this.posY - bb.maxY - entityLiving.getEyeHeight();
          diffZ = this.posZ - bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
          
          diffElse = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
          yaw = (float)(Math.atan2(-diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
          pitch = (float)-(Math.atan2(-diffY, diffElse) * 180.0D / Math.PI);
          break;
        default:
          diffX = this.posX - bb.minX + (bb.maxX - bb.minX) / 2.0D;
          diffY = this.posY - bb.minY + entityLiving.getEyeHeight();
          diffZ = this.posZ - bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
          
          diffElse = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
          yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
          pitch = (float)-(Math.atan2(diffY, diffElse) * 180.0D / Math.PI);
          break;
      } 
      
      this.entity.rotationPitch = updateRotation(this.entity.rotationPitch, pitch, this.deltaLookPitch);
      this.entity.rotationYawHead = updateRotation(this.entity.rotationYawHead, yaw, this.deltaLookYaw);
    }
    else {
      
      this.entity.rotationYawHead = updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
    } 
    
    float f2 = MathHelper.wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);
    
    if (!this.entity.getNavigator().noPath()) {
      
      if (f2 < -75.0F)
      {
        this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0F;
      }
      
      if (f2 > 75.0F)
      {
        this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0F;
      }
    } 
  }

  private float updateRotation(float yawHead, float yawOffset, float diffLimit) {
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
