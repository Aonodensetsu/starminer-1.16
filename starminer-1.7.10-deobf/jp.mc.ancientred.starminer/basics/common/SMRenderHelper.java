package jp.mc.ancientred.starminer.basics.common;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class SMRenderHelper
{
  public static float rotationX;
  public static float rotationZ;
  public static float rotationYZ;
  public static float rotationXY;
  public static float rotationXZ;
  public static double interpPosX;
  public static double interpPosY;
  public static double interpPosZ;
  public static boolean isValueCalculatedOnThisRender;
  
  public static void ensureValues(float partialTick) {
    if (!isValueCalculatedOnThisRender) {
      setRenderValues(partialTick);
      isValueCalculatedOnThisRender = true;
    } 
  }
  public static void setRenderValues(float partialTick) {
    int i = ((Minecraft.getMinecraft()).gameSettings.thirdPersonView == 2) ? 1 : 0;
    EntityLivingBase living = (Minecraft.getMinecraft()).renderViewEntity;
    Vec3 lookVec = living.getLook(partialTick);
    float xz = MathHelper.sqrt_double(lookVec.xCoord * lookVec.xCoord + lookVec.zCoord * lookVec.zCoord);
    float ry = (float)(-90.0D + Math.atan2(lookVec.zCoord, lookVec.xCoord) * 180.0D / Math.PI);
    float rp = -((float)(Math.atan2(lookVec.yCoord, xz) * 180.0D / Math.PI));
    rotationX = MathHelper.cos(ry * 3.1415927F / 180.0F) * (1 - i * 2);
    rotationZ = MathHelper.sin(ry * 3.1415927F / 180.0F) * (1 - i * 2);
    rotationYZ = -rotationZ * MathHelper.sin(rp * 3.1415927F / 180.0F) * (1 - i * 2);
    rotationXY = rotationX * MathHelper.sin(rp * 3.1415927F / 180.0F) * (1 - i * 2);
    rotationXZ = MathHelper.cos(rp * 3.1415927F / 180.0F);
    interpPosX = living.lastTickPosX + (living.posX - living.lastTickPosX) * partialTick;
    interpPosY = living.lastTickPosY + (living.posY - living.lastTickPosY) * partialTick;
    interpPosZ = living.lastTickPosZ + (living.posZ - living.lastTickPosZ) * partialTick;
  }
}
