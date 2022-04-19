package jp.mc.ancientred.starminer.core.ai.helper;

import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class GEntityBodyHelper
  extends EntityBodyHelper {
  private EntityLivingBase theLiving;
  private int tawTick;
  private float yawHead;
  
  public GEntityBodyHelper(EntityLivingBase entityLivingBase) {
    super(entityLivingBase);
    this.theLiving = entityLivingBase;
  }

  public void updateRenderAngles() {
    double d0 = this.theLiving.posX - this.theLiving.prevPosX;
    double d1 = this.theLiving.posZ - this.theLiving.prevPosZ;
    
    if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D) {
      
      this.theLiving.renderYawOffset = this.theLiving.rotationYaw;
      this.theLiving.rotationYawHead = computeAngleWithBound(this.theLiving.renderYawOffset, this.theLiving.rotationYawHead, 75.0F);
      this.yawHead = this.theLiving.rotationYawHead;
      this.tawTick = 0;
    }
    else {
      
      float f = 75.0F;
      
      if (Math.abs(this.theLiving.rotationYawHead - this.yawHead) > 15.0F) {
        
        this.tawTick = 0;
        this.yawHead = this.theLiving.rotationYawHead;
      }
      else {
        
        this.tawTick++;
        boolean flag = true;
        
        if (this.tawTick > 10)
        {
          f = Math.max(1.0F - (this.tawTick - 10) / 10.0F, 0.0F) * 75.0F;
        }
      } 
      
      this.theLiving.renderYawOffset = computeAngleWithBound(this.theLiving.rotationYawHead, this.theLiving.renderYawOffset, f);
    } 
  }

  private float computeAngleWithBound(float yawHead, float yawOffset, float diffLimit) {
    float f3 = MathHelper.wrapAngleTo180_float(yawHead - yawOffset);
    
    if (f3 < -diffLimit)
    {
      f3 = -diffLimit;
    }
    
    if (f3 >= diffLimit)
    {
      f3 = diffLimit;
    }
    
    return yawHead - f3;
  }
}
