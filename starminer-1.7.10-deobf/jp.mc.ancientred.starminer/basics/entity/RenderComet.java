package jp.mc.ancientred.starminer.basics.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.common.SMRenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderComet
  extends Render
{
  private static final ResourceLocation resCM = new ResourceLocation("starminer:textures/entity/comet.png");
  private static final ResourceLocation resLS = new ResourceLocation("starminer:textures/items/lifesoup_a.png");

  public void doRender(EntityComet entityComet, double posX, double poxY, double poxZ, float p_76986_8_, float partialTick) {
    Tessellator tes = Tessellator.instance;
    
    SMRenderHelper.ensureValues(partialTick);
    float rotationX = SMRenderHelper.rotationX;
    float rotationZ = SMRenderHelper.rotationZ;
    float rotationYZ = SMRenderHelper.rotationYZ;
    float rotationXY = SMRenderHelper.rotationXY;
    float rotationXZ = SMRenderHelper.rotationXZ;
    float fX = (float)(entityComet.prevPosX + (entityComet.posX - entityComet.prevPosX) * partialTick - SMRenderHelper.interpPosX);
    float fY = (float)(entityComet.prevPosY + (entityComet.posY - entityComet.prevPosY) * partialTick - SMRenderHelper.interpPosY) + 0.5F;
    float fZ = (float)(entityComet.prevPosZ + (entityComet.posZ - entityComet.prevPosZ) * partialTick - SMRenderHelper.interpPosZ);
    
    float texUMN = 0.0F;
    float texUMX = 1.0F;
    float texVMN = 0.0F;
    float texVMX = 1.0F;
    
    int j = 240;
    int k = 240;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    
    RenderHelper.disableStandardItemLighting();
    
    if (MinecraftForgeClient.getRenderPass() == 0) {
      fX += fX / 1000.0F;
      fY += fY / 1000.0F;
      fZ += fZ / 1000.0F;
      this.renderManager.renderEngine.bindTexture(resLS);
      float scale = 1.0F;
      tes.startDrawingQuads();
      tes.setColorRGBA_F(0.3F, 0.3F, 0.3F, 0.8F);
      tes.addVertexWithUV((fX - rotationX * scale - rotationYZ * scale), (fY - rotationXZ * scale), (fZ - rotationZ * scale - rotationXY * scale), texUMX, texVMX);
      tes.addVertexWithUV((fX - rotationX * scale + rotationYZ * scale), (fY + rotationXZ * scale), (fZ - rotationZ * scale + rotationXY * scale), texUMX, texVMN);
      tes.addVertexWithUV((fX + rotationX * scale + rotationYZ * scale), (fY + rotationXZ * scale), (fZ + rotationZ * scale + rotationXY * scale), texUMN, texVMN);
      tes.addVertexWithUV((fX + rotationX * scale - rotationYZ * scale), (fY - rotationXZ * scale), (fZ + rotationZ * scale - rotationXY * scale), texUMN, texVMX);
      tes.draw();
    } else {
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      
      this.renderManager.renderEngine.bindTexture(resCM);
      float scale = entityComet.getScaleNow() * 3.0F;
      tes.startDrawingQuads();
      if (entityComet.isRedOne()) {
        tes.setColorRGBA_F(1.0F, 0.2F, 0.2F, 0.8F);
      } else {
        tes.setColorRGBA_F(0.6F, 0.8F, 1.0F, 0.8F);
      } 
      tes.addVertexWithUV((fX - rotationX * scale - rotationYZ * scale), (fY - rotationXZ * scale), (fZ - rotationZ * scale - rotationXY * scale), texUMX, texVMX);
      tes.addVertexWithUV((fX - rotationX * scale + rotationYZ * scale), (fY + rotationXZ * scale), (fZ - rotationZ * scale + rotationXY * scale), texUMX, texVMN);
      tes.addVertexWithUV((fX + rotationX * scale + rotationYZ * scale), (fY + rotationXZ * scale), (fZ + rotationZ * scale + rotationXY * scale), texUMN, texVMN);
      tes.addVertexWithUV((fX + rotationX * scale - rotationYZ * scale), (fY - rotationXZ * scale), (fZ + rotationZ * scale - rotationXY * scale), texUMN, texVMX);
      tes.draw();
    } 
    
    RenderHelper.enableStandardItemLighting();
  }

  protected ResourceLocation getEntityTexture(Entity entity) {
    return null;
  }

  public void doRender(Entity entity, double posX, double posY, double posZ, float tickPartial, float scale) {
    doRender((EntityComet)entity, posX, posY, posZ, tickPartial, scale);
  }
  
  public void doRenderShadowAndFire(Entity entity, double posX, double posY, double posZ, float tickPartial, float scale) {}
}
