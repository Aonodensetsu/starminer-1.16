package jp.mc.ancientred.starminer.basics.dimention.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class BurningScreenEffectRenderer
{
  public static final void renderBurningScreen(int screenWidth, int screenHeight, float partialTicks) {
    double eyePosY = ((Minecraft.getMinecraft()).thePlayer.getPosition(partialTicks)).yCoord;
    if (eyePosY < -20.0D) {
      float redRate = -((float)(eyePosY + 20.0D)) / 48.0F;
      if (redRate > 0.7F) redRate = 0.7F; 
      drawGradientRect(0, 0, screenWidth, screenHeight, redRate);
    } 
  }
  
  private static final void drawGradientRect(int left, int top, int width, int height, float redRate) {
    GL11.glDisable(2929);
    GL11.glDisable(3008);

    if (left < width) {
      int swap = left;
      left = width;
      width = swap;
    } 
    
    if (top < height) {
      int swap = top;
      top = height;
      height = swap;
    } 
    
    Tessellator tessellator = Tessellator.instance;
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glColor4f(redRate, 0.0F, 0.0F, redRate);
    tessellator.startDrawingQuads();
    tessellator.addVertex(left, height, 0.0D);
    tessellator.addVertex(width, height, 0.0D);
    tessellator.addVertex(width, top, 0.0D);
    tessellator.addVertex(left, top, 0.0D);
    tessellator.draw();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    
    GL11.glEnable(3008);
    GL11.glEnable(2929);
  }
}
