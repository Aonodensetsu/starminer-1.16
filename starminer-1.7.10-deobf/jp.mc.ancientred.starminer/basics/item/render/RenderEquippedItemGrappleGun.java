package jp.mc.ancientred.starminer.basics.item.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderEquippedItemGrappleGun
  implements IItemRenderer
{
  private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
  
  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
  }

  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    return true;
  }
  
  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack itemStack, Object... data) {
    int renderPass = 0;
    if (data == null || data[1] == null || !(data[1] instanceof EntityLivingBase))
      return;  EntityLivingBase entity = (EntityLivingBase)data[1];
    
    TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
    IIcon iicon = entity.getItemIcon(itemStack, renderPass);
    if (iicon == null)
      return; 
    texturemanager.bindTexture(texturemanager.getResourceLocation(itemStack.getItemSpriteNumber()));
    
    TextureUtil.func_152777_a(false, false, 1.0F);
    Tessellator tessellator = Tessellator.instance;
    float f = iicon.getMinU();
    float f1 = iicon.getMaxU();
    float f2 = iicon.getMinV();
    float f3 = iicon.getMaxV();
    float f4 = 0.0F;
    float f5 = -0.5F;
    GL11.glEnable(32826);
    GL11.glTranslatef(-f4, -f5, 0.0F);
    float f6 = 1.5F;
    GL11.glScalef(f6, f6, f6);
    GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
    GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
    ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
    
    if (itemStack.hasEffect(renderPass)) {
      
      GL11.glDepthFunc(514);
      GL11.glDisable(2896);
      texturemanager.bindTexture(RES_ITEM_GLINT);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(768, 1, 1, 0);
      float f7 = 0.76F;
      GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
      GL11.glMatrixMode(5890);
      GL11.glPushMatrix();
      float f8 = 0.125F;
      GL11.glScalef(f8, f8, f8);
      float f9 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
      GL11.glTranslatef(f9, 0.0F, 0.0F);
      GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
      ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glScalef(f8, f8, f8);
      f9 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
      GL11.glTranslatef(-f9, 0.0F, 0.0F);
      GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
      ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glDisable(3042);
      GL11.glEnable(2896);
      GL11.glDepthFunc(515);
    } 
    
    GL11.glDisable(32826);
    texturemanager.bindTexture(texturemanager.getResourceLocation(itemStack.getItemSpriteNumber()));
    TextureUtil.func_147945_b();
  }
}
