package jp.mc.ancientred.starminer.basics.dimention.renderer;

import java.util.Random;
import jp.mc.ancientred.starminer.basics.SMReflectionHelperClient;
import jp.mc.ancientred.starminer.basics.dimention.MapFromSky;
import jp.mc.ancientred.starminer.basics.dimention.WorldProviderSpace;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

public class SpaceSkyRenderHanlder
  extends IRenderHandler
{
  private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
  private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
  private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
  private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
  
  private DynamicTexture bufferedImage;
  private ResourceLocation skyMap;
  private int[] intArray = new int[16384];
  
  private int RenderGlobal_starGLCallList = -1;
  private int RenderGlobal_glSkyList = -1;
  private int RenderGlobal_glSkyList2 = -1;

  
  private int modOriginal_starGLCallList = -1;
  private int modOriginal_glSkyList = -1;
  private int modOriginal_glSkyList2 = -1;

  public void render(float partialTicks, WorldClient world, Minecraft mc) {
    if (this.skyMap == null) {
      this.bufferedImage = new DynamicTexture(128, 128);
      this.skyMap = mc.getTextureManager().getDynamicTextureLocation("skymap", this.bufferedImage);
      this.intArray = this.bufferedImage.getTextureData();
      for (int i = 0; i < this.intArray.length; i++)
      {
        this.intArray[i] = 0;
      }
    } 
    if (this.RenderGlobal_starGLCallList == -1) {
      this.RenderGlobal_starGLCallList = SMReflectionHelperClient.getStarGLCallList(mc.renderGlobal);
      this.RenderGlobal_glSkyList = this.RenderGlobal_starGLCallList + 1;
      this.RenderGlobal_glSkyList2 = this.RenderGlobal_starGLCallList + 2;
    } 

    Tessellator tessellator1 = Tessellator.instance;
    
    GL11.glDisable(3553);

    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;

    if (mc.gameSettings.anaglyph) {
      
      float f5 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
      float f6 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
      float f11 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
      f1 = f5;
      f2 = f6;
      f3 = f11;
    } 

    GL11.glDepthMask(false);
    GL11.glEnable(2912);
    GL11.glColor3f(f1, f2, f3);
    GL11.glPushMatrix();
    GL11.glTranslatef(0.0F, -10.0F, 0.0F);
    GL11.glCallList(this.RenderGlobal_glSkyList);
    GL11.glPopMatrix();
    GL11.glDisable(2912);
    GL11.glDisable(3008);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    RenderHelper.disableStandardItemLighting();

    float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

    if (afloat != null) {
      
      GL11.glDisable(3553);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef((MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F) ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      float f5 = afloat[0];
      float f6 = afloat[1];
      float f11 = afloat[2];
      
      if (mc.gameSettings.anaglyph) {
        
        float f12 = (f5 * 30.0F + f6 * 59.0F + f11 * 11.0F) / 100.0F;
        float f13 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
        float f19 = (f5 * 30.0F + f11 * 70.0F) / 100.0F;
        f5 = f12;
        f6 = f13;
        f11 = f19;
      } 
      
      tessellator1.startDrawing(6);
      tessellator1.setColorRGBA_F(f5, f6, f11, afloat[3]);
      tessellator1.addVertex(0.0D, 100.0D, 0.0D);
      byte b0 = 16;
      tessellator1.setColorRGBA_F(afloat[0], afloat[1], afloat[2], 0.0F);
      
      for (int j = 0; j <= b0; j++) {
        
        float f19 = j * 3.1415927F * 2.0F / b0;
        float f12 = MathHelper.sin(f19);
        float f13 = MathHelper.cos(f19);
        tessellator1.addVertex((f12 * 120.0F), (f13 * 120.0F), (-f13 * 40.0F * afloat[3]));
      } 
      
      tessellator1.draw();
      GL11.glPopMatrix();
      GL11.glShadeModel(7424);
    } 

    GL11.glEnable(3553);
    
    GL11.glBlendFunc(770, 1);
    GL11.glPushMatrix();
    
    float f4 = 1.0F - world.getRainStrength(partialTicks);
    float f7 = 0.0F;
    float f8 = 0.0F;
    float f9 = 0.0F;
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    
    GL11.glTranslatef(f7, f8, f9);
    
    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
    
    GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
    
    float f10 = 30.0F;
    mc.renderEngine.bindTexture(locationSunPng);
    tessellator1.startDrawingQuads();
    tessellator1.addVertexWithUV(-f10, 100.0D, -f10, 0.0D, 0.0D);
    tessellator1.addVertexWithUV(f10, 100.0D, -f10, 1.0D, 0.0D);
    tessellator1.addVertexWithUV(f10, 100.0D, f10, 1.0D, 1.0D);
    tessellator1.addVertexWithUV(-f10, 100.0D, f10, 0.0D, 1.0D);
    tessellator1.draw();
    
    f10 = 20.0F;
    mc.renderEngine.bindTexture(locationMoonPhasesPng);
    int k = world.getMoonPhase();
    int l = k % 4;
    int i1 = k / 4 % 2;
    float f14 = (l + 0) / 4.0F;
    float f15 = (i1 + 0) / 2.0F;
    float f16 = (l + 1) / 4.0F;
    float f17 = (i1 + 1) / 2.0F;
    tessellator1.startDrawingQuads();
    tessellator1.addVertexWithUV(-f10, -100.0D, f10, f16, f17);
    tessellator1.addVertexWithUV(f10, -100.0D, f10, f14, f17);
    tessellator1.addVertexWithUV(f10, -100.0D, -f10, f14, f15);
    tessellator1.addVertexWithUV(-f10, -100.0D, -f10, f16, f15);
    tessellator1.draw();

    GL11.glDisable(3553);
    
    float f18 = 1.0F;
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glCallList(this.RenderGlobal_starGLCallList);
    
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDisable(3042);
    GL11.glEnable(3008);
    GL11.glEnable(2912);
    GL11.glPopMatrix();
    GL11.glDisable(3553);
    float ca = mc.theWorld.getCelestialAngle(partialTicks);
    float ca2 = MathHelper.cos(ca * 3.1415927F * 2.0F) * 2.0F + 0.5F;
    if (ca2 < 0.0F) ca2 = 0.0F; 
    if (ca2 > 1.0F) ca2 = 1.0F;
    
    GL11.glColor3f(0.3F * ca2, 0.3F * ca2, 1.0F * ca2);

    GL11.glPushMatrix();
    GL11.glTranslatef(0.0F, 8.0F, 0.0F);
    GL11.glCallList(this.RenderGlobal_glSkyList2);
    GL11.glPopMatrix();

    if (world.provider.isSkyColored()) {
      
      GL11.glColor3f(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
    }
    else {
      
      GL11.glColor3f(f1, f2, f3);
    } 

    GL11.glEnable(3553);
    
    GL11.glPushMatrix();
    renderGround(partialTicks, world, mc);
    GL11.glPopMatrix();
    
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDisable(3042);
    GL11.glDepthMask(true);
  }
  
  private void bindMapFromSky() {
    byte[] colors = MapFromSky.skyMapclientData;
    for (int i = 0; i < 16384; i++) {
      
      byte b0 = colors[i];
      
      if (b0 / 4 == 0) {
        
        this.intArray[i] = (i + i / 128 & 0x1) * 8 + 16 << 24;
      }
      else {
        
        int j = (MapColor.mapColorArray[b0 / 4]).colorValue;
        int k = b0 & 0x3;
        short short1 = 220;
        
        if (k == 2)
        {
          short1 = 255;
        }
        
        if (k == 0)
        {
          short1 = 180;
        }
        
        int l = (j >> 16 & 0xFF) * short1 / 255;
        int i1 = (j >> 8 & 0xFF) * short1 / 255;
        int j1 = (j & 0xFF) * short1 / 255;
        this.intArray[i] = 0xFF000000 | l << 16 | i1 << 8 | j1;
      } 
    } 
    this.bufferedImage.updateDynamicTexture();
  }

  private int renderGroundCallList = -1;
  private void renderGround(float partialTicks, WorldClient world, Minecraft mc) {
    GL11.glDisable(2884);
    float f1 = (float)(mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * partialTicks);
    byte b0 = 32;
    int i = 256 / b0;
    Tessellator tessellator = Tessellator.instance;
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    Vec3 vec3 = world.getCloudColour(partialTicks);
    float f2 = (float)vec3.xCoord;
    float f3 = (float)vec3.yCoord;
    float f4 = (float)vec3.zCoord;

    if (mc.gameSettings.anaglyph) {
      
      float f11 = (f2 * 30.0F + f3 * 59.0F + f4 * 11.0F) / 100.0F;
      float f6 = (f2 * 30.0F + f3 * 70.0F) / 100.0F;
      float f7 = (f2 * 30.0F + f4 * 70.0F) / 100.0F;
      f2 = f11;
      f3 = f6;
      f4 = f7;
    } 
    GL11.glDepthMask(false);
    float f5 = 0.001953125F;
    
    double d0 = (WorldProviderSpace.cloudTickCounter + partialTicks);
    double d1 = mc.renderViewEntity.prevPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX) * partialTicks + d0 * 0.029999999329447746D;
    double d2 = mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ) * partialTicks;
    int j = MathHelper.floor_double(d1 / 2048.0D);
    int k = MathHelper.floor_double(d2 / 2048.0D);
    d1 -= (j * 2048);
    d2 -= (k * 2048);
    float f8 = -22.0F;
    float f9 = (float)(d1 * f5);
    float f10 = (float)(d2 * f5);

    GL11.glColor4f(f2, f3, f4, 0.8F);
    if (MapFromSky.hasSkyMapImageData) {
      
      GL11.glColor4f(f2, f3, f4, 0.8F);
      if (this.renderGroundCallList == -1 || MapFromSky.doRecompileSkyMapList) {
        LogWrapper.log(Level.INFO, "[Starminer]Compiling gllist for ground texture..", new Object[0]);
        
        bindMapFromSky();
        
        if (MapFromSky.doRecompileSkyMapList && this.renderGroundCallList != -1 && GL11.glIsList(this.renderGroundCallList)) {
          
          GL11.glDeleteLists(this.renderGroundCallList, 1);
          
          LogWrapper.log(Level.INFO, "[Starminer]Deleted compiled gllist for ground texture..", new Object[0]);
          
          MapFromSky.doRecompileSkyMapList = false;
        } 
        this.renderGroundCallList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(this.renderGroundCallList, 4864);
        
        int ii = i *= 8;
        tessellator.startDrawingQuads();
        mc.renderEngine.bindTexture(this.skyMap);
        
        double ff5 = (f5 * 4.0F); int m;
        for (m = -b0 * ii; m < b0 * ii; m += b0) {
          int i1;
          for (i1 = -b0 * ii; i1 < b0 * ii; i1 += b0) {
            
            tessellator.addVertexWithUV((m + 0), f8, (i1 + b0), (m + 0) * ff5 + 0.5D, (i1 + b0) * ff5 + 0.5D);
            tessellator.addVertexWithUV((m + b0), f8, (i1 + b0), (m + b0) * ff5 + 0.5D, (i1 + b0) * ff5 + 0.5D);
            tessellator.addVertexWithUV((m + b0), f8, (i1 + 0), (m + b0) * ff5 + 0.5D, (i1 + 0) * ff5 + 0.5D);
            tessellator.addVertexWithUV((m + 0), f8, (i1 + 0), (m + 0) * ff5 + 0.5D, (i1 + 0) * ff5 + 0.5D);
          } 
        } 
        
        tessellator.draw();
        GL11.glEndList();
        LogWrapper.log(Level.INFO, "[Starminer]Compiled gllist for ground texture, done.", new Object[0]);
      } else {
        GL11.glCallList(this.renderGroundCallList);
      } 
    } 

    f9 = (float)(d0 * 3.0E-5D);
    f10 = 0.0F;
    mc.renderEngine.bindTexture(locationCloudsPng);
    tessellator.startDrawingQuads(); int l;
    for (l = -b0 * i; l < b0 * i; l += b0) {
      int i1;
      for (i1 = -b0 * i; i1 < b0 * i; i1 += b0) {
        
        tessellator.addVertexWithUV((l + 0), f8, (i1 + b0), ((l + 0) * f5 + f9), ((i1 + b0) * f5 + f10));
        tessellator.addVertexWithUV((l + b0), f8, (i1 + b0), ((l + b0) * f5 + f9), ((i1 + b0) * f5 + f10));
        tessellator.addVertexWithUV((l + b0), f8, (i1 + 0), ((l + b0) * f5 + f9), ((i1 + 0) * f5 + f10));
        tessellator.addVertexWithUV((l + 0), f8, (i1 + 0), ((l + 0) * f5 + f9), ((i1 + 0) * f5 + f10));
      } 
    } 
    tessellator.draw();
    
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDisable(3042);
    GL11.glEnable(2884);
    GL11.glDepthMask(true);
  }
  
  private void renderOriginalStars() {
    Random random = new Random(10842L);
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    
    for (int i = 0; i < 3000; i++) {
      
      double d0 = (random.nextFloat() * 2.0F - 1.0F);
      double d1 = (random.nextFloat() * 2.0F - 1.0F);
      double d2 = (random.nextFloat() * 2.0F - 1.0F);
      double d3 = (0.15F + random.nextFloat() * 0.1F);
      double d4 = d0 * d0 + d1 * d1 + d2 * d2;
      
      if (d4 < 1.0D && d4 > 0.01D) {
        
        d4 = 1.0D / Math.sqrt(d4);
        d0 *= d4;
        d1 *= d4;
        d2 *= d4;
        double d5 = d0 * 100.0D;
        double d6 = d1 * 100.0D;
        double d7 = d2 * 100.0D;
        double d8 = Math.atan2(d0, d2);
        double d9 = Math.sin(d8);
        double d10 = Math.cos(d8);
        double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
        double d12 = Math.sin(d11);
        double d13 = Math.cos(d11);
        double d14 = random.nextDouble() * Math.PI * 2.0D;
        double d15 = Math.sin(d14);
        double d16 = Math.cos(d14);
        
        for (int j = 0; j < 4; j++) {
          
          double d17 = 0.0D;
          double d18 = ((j & 0x2) - 1) * d3;
          double d19 = ((j + 1 & 0x2) - 1) * d3;
          double d20 = d18 * d16 - d19 * d15;
          double d21 = d19 * d16 + d18 * d15;
          double d22 = d20 * d12 + d17 * d13;
          double d23 = d17 * d12 - d20 * d13;
          double d24 = d23 * d9 - d21 * d10;
          double d25 = d21 * d9 + d23 * d10;
          tessellator.addVertex(d5 + d24, d6 + d22, d7 + d25);
        } 
      } 
    } 
    
    tessellator.draw();
  }
}
