package jp.mc.ancientred.starminer.core;

import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.api.IRotateSleepingViewHandler;
import jp.mc.ancientred.starminer.core.common.VecUtils;
import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import jp.mc.ancientred.starminer.core.obfuscar.SMCoreReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class TransformClientHelper
{
  public static void orientCameraByGravity(float par1) {
    Minecraft mc = Minecraft.getMinecraft();
    EntityLivingBase entityLivingBase = mc.renderViewEntity;
    ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)mc.thePlayer);
    
    GravityDirection dir = gravity.gravityDirection;
    
    GL11.glRotatef(180.0F * dir.rotX, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(180.0F * dir.rotZ, 0.0F, 0.0F, 1.0F);
    
    float pitch = entityLivingBase.prevRotationPitch + (entityLivingBase.rotationPitch - entityLivingBase.prevRotationPitch) * par1;
    GL11.glRotatef(pitch * dir.pitchRotDirX, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(pitch * dir.pitchRotDirY, 0.0F, 1.0F, 0.0F);
    
    float yaw = entityLivingBase.prevRotationYaw + (entityLivingBase.rotationYaw - entityLivingBase.prevRotationYaw) * par1 + 180.0F;
    GL11.glRotatef(yaw * dir.yawRotDirX, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(yaw * dir.yawRotDirY, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(yaw * dir.yawRotDirZ, 0.0F, 0.0F, 1.0F);
    
    float fixHeight = entityLivingBase.yOffset - entityLivingBase.width / 2.0F;
    GL11.glTranslatef(fixHeight * dir.shiftEyeX, fixHeight * dir.shiftEyeY, fixHeight * dir.shiftEyeZ);

    GL11.glTranslatef(entityLivingBase.yOffset2 * dir.shiftSneakX, entityLivingBase.yOffset2 * dir.shiftSneakY, entityLivingBase.yOffset2 * dir.shiftSneakZ);

    if (gravity.turnRate < 1.0F) {
      GL11.glRotatef(90.0F * (gravity.prevTurnRate + (gravity.turnRate - gravity.prevTurnRate) * par1), gravity.onChangeRoatDirX, gravity.onChangeRoatDirY, gravity.onChangeRoatDirZ);
    }
  }

  public static void rotateCorpseByGravity(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
    switch (ExtendedPropertyGravity.getGravityDirection((Entity)par1EntityLivingBase)) {
      case southTOnorth_ZN:
        GL11.glTranslatef(0.0F, par1EntityLivingBase.width / 2.0F, -par1EntityLivingBase.width / 2.0F);
        GL11.glRotatef(180.0F - par3, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        break;
      case northTOsouth_ZP:
        GL11.glTranslatef(0.0F, par1EntityLivingBase.width / 2.0F, par1EntityLivingBase.width / 2.0F);
        GL11.glRotatef(180.0F + par3, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        break;
      case westTOeast_XP:
        GL11.glTranslatef(par1EntityLivingBase.width / 2.0F, par1EntityLivingBase.width / 2.0F, 0.0F);
        GL11.glRotatef(180.0F + par3, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        break;
      case eastTOwest_XN:
        GL11.glTranslatef(-par1EntityLivingBase.width / 2.0F, par1EntityLivingBase.width / 2.0F, 0.0F);
        GL11.glRotatef(180.0F - par3, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
        break;
      case downTOup_YP:
        GL11.glTranslatef(0.0F, par1EntityLivingBase.height, 0.0F);
        GL11.glRotatef(180.0F + par3, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        break;
      default:
        GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
        break;
    } 

    if (par1EntityLivingBase.deathTime > 0) {
      
      float f3 = (par1EntityLivingBase.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
      f3 = MathHelper.sqrt_float(f3);
      
      if (f3 > 1.0F)
      {
        f3 = 1.0F;
      }
      if (par1EntityLivingBase instanceof net.minecraft.entity.monster.EntitySpider || par1EntityLivingBase instanceof net.minecraft.entity.monster.EntitySilverfish) {
        GL11.glRotatef(f3 * 180.0F, 0.0F, 0.0F, 1.0F);
      } else {
        GL11.glRotatef(f3 * 90.0F, 0.0F, 0.0F, 1.0F);
      }
    
    } else {
      
      String s = EnumChatFormatting.getTextWithoutFormattingCodes(par1EntityLivingBase.getCommandSenderName());
      
      if ((s.equals("Dinnerbone") || s.equals("Grumm")) && (!(par1EntityLivingBase instanceof EntityPlayer) || !((EntityPlayer)par1EntityLivingBase).getHideCape())) {
        
        GL11.glTranslatef(0.0F, par1EntityLivingBase.height + 0.1F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      } 
    } 
  }
  
  public static void setFlyMovementByGravity(EntityPlayer entityPlayer) {
    SMCoreModContainer.proxy.setFlyMovementByGravity(entityPlayer); } public static boolean roatate3rdPersonViewByGravity(double d3, float par1) {
    double d4, d5, d6;
    float zVal, zFix, yVal, yFix, xVal, xFix;
    Minecraft mc = Minecraft.getMinecraft();
    EntityLivingBase entitylivingbase = mc.renderViewEntity;
    ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)mc.renderViewEntity);
    
    float f1 = entitylivingbase.yOffset - 1.62F;
    double d0 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * par1;
    double d1 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * par1 - f1;
    double d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * par1;
    
    float f3 = entitylivingbase.rotationYaw;
    float f2 = entitylivingbase.rotationPitch;
    
    if (mc.gameSettings.thirdPersonView == 2)
    {
      f2 += 180.0F;
    }

    switch (ExtendedPropertyGravity.getGravityDirection((Entity)entitylivingbase)) {
      case southTOnorth_ZN:
        zVal = MathHelper.sin(-f2 * 0.017453292F);
        zFix = MathHelper.cos(-f2 * 0.017453292F);
        yVal = MathHelper.cos(-f3 * 0.017453292F - 3.1415927F);
        xVal = -MathHelper.sin(-f3 * 0.017453292F - 3.1415927F);
        d4 = (xVal * zFix) * d3;
        d6 = (yVal * zFix) * d3;
        d5 = zVal * d3;
        break;
      case northTOsouth_ZP:
        zVal = -MathHelper.sin(-f2 * 0.017453292F);
        zFix = MathHelper.cos(-f2 * 0.017453292F);
        yVal = -MathHelper.cos(-f3 * 0.017453292F - 3.1415927F);
        xVal = -MathHelper.sin(-f3 * 0.017453292F - 3.1415927F);
        d4 = (xVal * zFix) * d3;
        d6 = (yVal * zFix) * d3;
        d5 = zVal * d3;
        break;
      case westTOeast_XP:
        xVal = -MathHelper.sin(-f2 * 0.017453292F);
        xFix = MathHelper.cos(-f2 * 0.017453292F);
        zVal = -MathHelper.cos(-f3 * 0.017453292F - 3.1415927F);
        yVal = -MathHelper.sin(-f3 * 0.017453292F - 3.1415927F);
        d4 = xVal * d3;
        d6 = (yVal * xFix) * d3;
        d5 = (zVal * xFix) * d3;
        break;
      case eastTOwest_XN:
        xVal = MathHelper.sin(-f2 * 0.017453292F);
        xFix = MathHelper.cos(-f2 * 0.017453292F);
        zVal = -MathHelper.cos(-f3 * 0.017453292F - 3.1415927F);
        yVal = MathHelper.sin(-f3 * 0.017453292F - 3.1415927F);
        d4 = xVal * d3;
        d6 = (yVal * xFix) * d3;
        d5 = (zVal * xFix) * d3;
        break;
      case downTOup_YP:
        yVal = -MathHelper.sin(-f2 * 0.017453292F);
        yFix = MathHelper.cos(-f2 * 0.017453292F);
        zVal = MathHelper.cos(-f3 * 0.017453292F - 3.1415927F);
        xVal = -MathHelper.sin(-f3 * 0.017453292F - 3.1415927F);
        d4 = (xVal * yFix) * d3;
        d6 = yVal * d3;
        d5 = (zVal * yFix) * d3;
        break;
      default:
        yVal = MathHelper.sin(-f2 * 0.017453292F);
        yFix = -MathHelper.cos(-f2 * 0.017453292F);
        zVal = MathHelper.cos(-f3 * 0.017453292F - 3.1415927F);
        xVal = MathHelper.sin(-f3 * 0.017453292F - 3.1415927F);
        d4 = (xVal * yFix) * d3;
        d6 = yVal * d3;
        d5 = (zVal * yFix) * d3;
        break;
    } 
    Vec3 vecEye = TransformUtils.fixEyePositionByGravityClient((Entity)entitylivingbase, VecUtils.createVec3(d0, d1, d2));
    
    for (int l = 0; l < 8; l++) {
      
      float f4 = ((l & 0x1) * 2 - 1);
      float f5 = ((l >> 1 & 0x1) * 2 - 1);
      float f6 = ((l >> 2 & 0x1) * 2 - 1);
      f4 *= 0.1F;
      f5 *= 0.1F;
      f6 *= 0.1F;
      MovingObjectPosition movingobjectposition = mc.theWorld.rayTraceBlocks(VecUtils.createVec3(vecEye.xCoord + f4, vecEye.yCoord + f5, vecEye.zCoord + f6), VecUtils.createVec3(vecEye.xCoord - d4 + f4 + f6, vecEye.yCoord - d6 + f5, vecEye.zCoord - d5 + f6));

      if (movingobjectposition != null) {
        
        double d7 = movingobjectposition.hitVec.distanceTo(vecEye);
        
        if (d7 < d3)
        {
          d3 = d7;
        }
      } 
    } 
    
    if (mc.gameSettings.thirdPersonView == 2)
    {
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
    }
    
    GL11.glRotatef(entitylivingbase.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(entitylivingbase.rotationYaw - f3, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(0.0F, 0.0F, (float)-d3);
    GL11.glRotatef(f3 - entitylivingbase.rotationYaw, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(f2 - entitylivingbase.rotationPitch, 1.0F, 0.0F, 0.0F);
    
    return true;
  }
  
  public static boolean rotateSleepingViewByGravity(float par1) {
    for (int i = 0; i < IRotateSleepingViewHandler.handlerList.size(); i++) {
      IRotateSleepingViewHandler handler = IRotateSleepingViewHandler.handlerList.get(0);
      if (handler.rotateSleepingFPView()) {
        return false;
      }
    } 
    return true;
  }
  
  public static void rotateCorpseProxy(RendererLivingEntity renderer, EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
    if (par1EntityLivingBase instanceof AbstractClientPlayer) {
      AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer)par1EntityLivingBase;
      if (abstractClientPlayer.isEntityAlive() && abstractClientPlayer.isPlayerSleeping())
      {
        if (rotatePlayerSleeping((EntityPlayer)abstractClientPlayer)) {
          return;
        }
      }
    } 
    if (SMCoreReflectionHelper.method_rotateCorpsePublic == null) {
      SMCoreReflectionHelper.initMethodAccessRotateCorpsePublic();
    }

    SMCoreReflectionHelper.method_rotateCorpsePublic_args[0] = par2;
    SMCoreReflectionHelper.method_rotateCorpsePublic_args[1] = par3;
    SMCoreReflectionHelper.method_rotateCorpsePublic_args[2] = par4;
    try {
      SMCoreReflectionHelper.method_rotateCorpsePublic.invoke(renderer, new Object[] { par1EntityLivingBase, SMCoreReflectionHelper.method_rotateCorpsePublic_args });
    
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  private static boolean rotatePlayerSleeping(EntityPlayer player) {
    for (int i = 0; i < IRotateSleepingViewHandler.handlerList.size(); i++) {
      IRotateSleepingViewHandler handler = IRotateSleepingViewHandler.handlerList.get(0);
      if (handler.rotateTPPlayerSleeping(player)) {
        return true;
      }
    } 
    return false;
  }

  public static Vec3 getPositionForgeHook(EntityPlayer pThis, float par1) {
    if (par1 == 1.0F)
    {
      return TransformUtils.fixEyePositionByGravityClient((Entity)pThis, Vec3.createVectorHelper(pThis.posX, pThis.posY + (pThis.getEyeHeight() - pThis.getDefaultEyeHeight()), pThis.posZ));
    }

    double d0 = pThis.prevPosX + (pThis.posX - pThis.prevPosX) * par1;
    double d1 = pThis.prevPosY + (pThis.posY - pThis.prevPosY) * par1 + (pThis.getEyeHeight() - pThis.getDefaultEyeHeight());
    double d2 = pThis.prevPosZ + (pThis.posZ - pThis.prevPosZ) * par1;
    
    return TransformUtils.fixEyePositionByGravityClient((Entity)pThis, VecUtils.createVec3(d0, d1, d2));
  }
}
