package jp.mc.ancientred.starminer.basics.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.api.Gravity;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGProjectile
  extends Render
{
  private static final ResourceLocation particlesTexture = new ResourceLocation("textures/particle/particles.png");
  private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");

  private double calcTickDiv(double p_110828_1_, double p_110828_3_, double p_110828_5_) {
    return p_110828_1_ + (p_110828_3_ - p_110828_1_) * p_110828_5_;
  }
  
  public void renderGProjectile(EntityGProjectile parEntityGrappleHook, double parPosX, double parPosY, double parPosZ, float p_110827_8_, float parPartialTick) {
    doRenderArrowPart(parEntityGrappleHook, parPosX, parPosY, parPosZ, p_110827_8_, parPartialTick);
    if (parEntityGrappleHook.getGProjectileType() == EntityGProjectile.GProjectileType.gRappleHook) {
      doRenderLeashPart(parEntityGrappleHook, parPosX, parPosY, parPosZ, p_110827_8_, parPartialTick);
    }
  }
  
  public void doRenderLeashPart(EntityGProjectile parEntityGrappleHook, double parRenderBasePosX, double parRenderBasePosY, double parRenderBasePosZ, float p_110827_8_, float parPartialTick) {
    if (parEntityGrappleHook.shootingEntity == null || !(parEntityGrappleHook.shootingEntity instanceof EntityPlayer)) {
      return;
    }
    
    EntityPlayer entityPlayer = (EntityPlayer)parEntityGrappleHook.shootingEntity;
    Tessellator tessellator = Tessellator.instance;
    
    Gravity gravity = Gravity.getGravityProp((Entity)entityPlayer);
    if (gravity == null)
      return;  Vec3 vecEyePoz = gravity.getGravityFixedPlayerEyePoz(entityPlayer, parPartialTick);
    vecEyePoz.yCoord -= 0.12D;
    float playerPitch = entityPlayer.prevRotationPitch + (entityPlayer.rotationPitch - entityPlayer.prevRotationPitch) * parPartialTick;
    float playerYaw = entityPlayer.prevRotationYaw + (entityPlayer.rotationYaw - entityPlayer.prevRotationYaw) * parPartialTick;
    Vec3 vecLook = gravity.getGravityFixedLook(playerPitch + 10.0F, playerYaw + 25.0F);
    
    double padding = 0.4D;
    double gunTopX = vecEyePoz.xCoord + vecLook.xCoord * padding;
    double gunTopY = vecEyePoz.yCoord + vecLook.yCoord * padding;
    double gunTopZ = vecEyePoz.zCoord + vecLook.zCoord * padding;

    double yaw = calcTickDiv(parEntityGrappleHook.prevRotationYaw, parEntityGrappleHook.rotationYaw, (parPartialTick * 0.5F)) * 0.01745329238474369D;
    double ptc = calcTickDiv(parEntityGrappleHook.prevRotationPitch, parEntityGrappleHook.rotationPitch, (parPartialTick * 0.5F)) * 0.01745329238474369D;
    double yVal = Math.sin(-ptc);
    double yFix = -Math.cos(-ptc);
    double zVal = -Math.cos(-yaw - Math.PI);
    double xVal = Math.sin(-yaw - Math.PI);
    padding = 0.65D;
    double addX = xVal * yFix * padding;
    double addY = yVal * padding - 1.5D;
    double addZ = zVal * yFix * padding;
    parRenderBasePosX += addX;
    parRenderBasePosY += addY;
    parRenderBasePosZ += addZ;
    
    double arrowX = parEntityGrappleHook.prevPosX + (parEntityGrappleHook.posX - parEntityGrappleHook.prevPosX) * parPartialTick;
    double arrowY = parEntityGrappleHook.prevPosY + (parEntityGrappleHook.posY - parEntityGrappleHook.prevPosY) * parPartialTick;
    double arrowZ = parEntityGrappleHook.prevPosZ + (parEntityGrappleHook.posZ - parEntityGrappleHook.prevPosZ) * parPartialTick;
    arrowX += addX;
    arrowY += addY;
    arrowZ += addZ;
    
    double distanceX = (float)(gunTopX - arrowX);
    double distanceY = (float)(gunTopY - arrowY);
    double distanceZ = (float)(gunTopZ - arrowZ);
    
    GL11.glDisable(3553);
    GL11.glDisable(2896);
    GL11.glDisable(2884);
    
    tessellator.startDrawing(5); int i;
    for (i = 0; i <= 24; i++) {
      
      if (i % 2 == 0) {
        
        tessellator.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
      }
      else {
        
        tessellator.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
      } 
      float f2 = i / 24.0F;
      tessellator.addVertex(parRenderBasePosX + distanceX * f2 + 0.0D, parRenderBasePosY + distanceY * (f2 * f2 + f2) * 0.5D + ((24.0F - i) / 18.0F + 0.125F), parRenderBasePosZ + distanceZ * f2);
      tessellator.addVertex(parRenderBasePosX + distanceX * f2 + 0.025D, parRenderBasePosY + distanceY * (f2 * f2 + f2) * 0.5D + ((24.0F - i) / 18.0F + 0.125F) + 0.025D, parRenderBasePosZ + distanceZ * f2);
    } 
    tessellator.draw();
    
    tessellator.startDrawing(5);
    for (i = 0; i <= 24; i++) {
      
      if (i % 2 == 0) {
        
        tessellator.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
      }
      else {
        
        tessellator.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
      } 
      float f2 = i / 24.0F;
      tessellator.addVertex(parRenderBasePosX + distanceX * f2 + 0.0D, parRenderBasePosY + distanceY * (f2 * f2 + f2) * 0.5D + ((24.0F - i) / 18.0F + 0.125F) + 0.025D, parRenderBasePosZ + distanceZ * f2);
      tessellator.addVertex(parRenderBasePosX + distanceX * f2 + 0.025D, parRenderBasePosY + distanceY * (f2 * f2 + f2) * 0.5D + ((24.0F - i) / 18.0F + 0.125F), parRenderBasePosZ + distanceZ * f2 + 0.025D);
    } 
    tessellator.draw();
    
    GL11.glEnable(2896);
    GL11.glEnable(3553);
    GL11.glEnable(2884);
  }
  
  public void doRenderArrowPart(EntityGProjectile parEntityGrappleHook, double parPosX, double parPosY, double parPosZ, float p_76986_8_, float parPartialTick) {
    bindTexture(arrowTextures);
    GL11.glPushMatrix();
    GL11.glTranslatef((float)parPosX, (float)parPosY, (float)parPosZ);
    GL11.glRotatef(parEntityGrappleHook.prevRotationYaw + (parEntityGrappleHook.rotationYaw - parEntityGrappleHook.prevRotationYaw) * parPartialTick - 90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(parEntityGrappleHook.prevRotationPitch + (parEntityGrappleHook.rotationPitch - parEntityGrappleHook.prevRotationPitch) * parPartialTick, 0.0F, 0.0F, 1.0F);
    Tessellator tessellator = Tessellator.instance;
    byte b0 = 0;
    float f2 = 0.0F;
    float f3 = 0.5F;
    float f4 = (0 + b0 * 10) / 32.0F;
    float f5 = (5 + b0 * 10) / 32.0F;
    float f6 = 0.0F;
    float f7 = 0.15625F;
    float f8 = (5 + b0 * 10) / 32.0F;
    float f9 = (10 + b0 * 10) / 32.0F;
    float f10 = 0.05625F;
    GL11.glEnable(32826);
    float f11 = parEntityGrappleHook.arrowShake - parPartialTick;
    
    if (f11 > 0.0F) {
      
      float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
      GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
    } 
    
    GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
    GL11.glScalef(f10, f10, f10);
    GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
    GL11.glNormal3f(f10, 0.0F, 0.0F);
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV(-7.0D, -2.0D, -2.0D, f6, f8);
    tessellator.addVertexWithUV(-7.0D, -2.0D, 2.0D, f7, f8);
    tessellator.addVertexWithUV(-7.0D, 2.0D, 2.0D, f7, f9);
    tessellator.addVertexWithUV(-7.0D, 2.0D, -2.0D, f6, f9);
    tessellator.draw();
    GL11.glNormal3f(-f10, 0.0F, 0.0F);
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV(-7.0D, 2.0D, -2.0D, f6, f8);
    tessellator.addVertexWithUV(-7.0D, 2.0D, 2.0D, f7, f8);
    tessellator.addVertexWithUV(-7.0D, -2.0D, 2.0D, f7, f9);
    tessellator.addVertexWithUV(-7.0D, -2.0D, -2.0D, f6, f9);
    tessellator.draw();
    
    for (int i = 0; i < 4; i++) {
      
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glNormal3f(0.0F, 0.0F, f10);
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV(-8.0D, -2.0D, 0.0D, f2, f4);
      tessellator.addVertexWithUV(8.0D, -2.0D, 0.0D, f3, f4);
      tessellator.addVertexWithUV(8.0D, 2.0D, 0.0D, f3, f5);
      tessellator.addVertexWithUV(-8.0D, 2.0D, 0.0D, f2, f5);
      tessellator.draw();
    } 
    
    GL11.glDisable(32826);
    GL11.glPopMatrix();
  }

  protected ResourceLocation getEntityTexture(EntityGProjectile p_110775_1_) {
    return particlesTexture;
  }

  protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
    return getEntityTexture((EntityGProjectile)p_110775_1_);
  }

  public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float parPartialTick) {
    renderGProjectile((EntityGProjectile)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, parPartialTick);
  }
}
