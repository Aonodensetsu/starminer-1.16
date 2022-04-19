package jp.mc.ancientred.starminer.basics.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderFallingBlockEx
  extends Render
{
  private final RenderBlocks renderBlocks = new RenderBlocks();

  public RenderFallingBlockEx() {
    this.shadowSize = 0.5F;
  }

  public void doRender(EntityFallingBlockEx entityFallingBlockEx, double posX, double poxY, double poxZ, float p_76986_8_, float partialTick) {
    World world = entityFallingBlockEx.getWorldObj();
    Block block = entityFallingBlockEx.getBlock();
    int i = MathHelper.floor_double(entityFallingBlockEx.posX);
    int j = MathHelper.floor_double(entityFallingBlockEx.posY);
    int k = MathHelper.floor_double(entityFallingBlockEx.posZ);
    
    if (block != null && block != world.getBlock(i, j, k)) {
      
      GL11.glPushMatrix();
      GL11.glTranslatef((float)posX, (float)poxY, (float)poxZ);
      bindEntityTexture((Entity)entityFallingBlockEx);
      GL11.glDisable(2896);

      if (block instanceof BlockAnvil) {
        
        this.renderBlocks.blockAccess = (IBlockAccess)world;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setTranslation((-i - 0.5F), (-j - 0.5F), (-k - 0.5F));
        this.renderBlocks.renderBlockAnvilMetadata((BlockAnvil)block, i, j, k, entityFallingBlockEx.blockMeta);
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
      }
      else if (block instanceof BlockDragonEgg) {
        
        this.renderBlocks.blockAccess = (IBlockAccess)world;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setTranslation((-i - 0.5F), (-j - 0.5F), (-k - 0.5F));
        this.renderBlocks.renderBlockDragonEgg((BlockDragonEgg)block, i, j, k);
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
      }
      else if (block instanceof jp.mc.ancientred.starminer.basics.block.BlockDirtGrassEx) {
        
        this.renderBlocks.setRenderBoundsFromBlock(block);
        renderBlockDirtEx(block, world, i, j, k, entityFallingBlockEx.blockMeta);
      }
      else {
        
        this.renderBlocks.setRenderBoundsFromBlock(block);
        this.renderBlocks.renderBlockSandFalling(block, world, i, j, k, entityFallingBlockEx.blockMeta);
      } 
      
      GL11.glEnable(2896);
      GL11.glPopMatrix();
    } 
  }
  
  public void renderBlockDirtEx(Block block, World world, int x, int y, int z, int meta) {
    int l;
    if ((meta & 0x8) == 0) {
      l = Blocks.grass.colorMultiplier((IBlockAccess)world, x, y, z);
    } else {
      l = block.colorMultiplier((IBlockAccess)world, x, y, z);
    } 
    float cf = (l >> 16 & 0xFF) / 255.0F;
    float cf1 = (l >> 8 & 0xFF) / 255.0F;
    float cf2 = (l & 0xFF) / 255.0F;
    
    float f = 0.5F;
    float f1 = 1.0F;
    float f2 = 0.8F;
    float f3 = 0.6F;
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.setBrightness(block.getMixedBrightnessForBlock((IBlockAccess)world, x, y, z));
    tessellator.setColorOpaque_F(cf, cf1, cf2);
    this.renderBlocks.renderFaceYNeg(block, -0.5D, -0.5D, -0.5D, this.renderBlocks.getBlockIconFromSideAndMetadata(block, 0, meta));
    tessellator.setColorOpaque_F(cf, cf1, cf2);
    this.renderBlocks.renderFaceYPos(block, -0.5D, -0.5D, -0.5D, this.renderBlocks.getBlockIconFromSideAndMetadata(block, 1, meta));
    tessellator.setColorOpaque_F(cf, cf1, cf2);
    this.renderBlocks.renderFaceZNeg(block, -0.5D, -0.5D, -0.5D, this.renderBlocks.getBlockIconFromSideAndMetadata(block, 2, meta));
    tessellator.setColorOpaque_F(cf, cf1, cf2);
    this.renderBlocks.renderFaceZPos(block, -0.5D, -0.5D, -0.5D, this.renderBlocks.getBlockIconFromSideAndMetadata(block, 3, meta));
    tessellator.setColorOpaque_F(cf, cf1, cf2);
    this.renderBlocks.renderFaceXNeg(block, -0.5D, -0.5D, -0.5D, this.renderBlocks.getBlockIconFromSideAndMetadata(block, 4, meta));
    tessellator.setColorOpaque_F(cf, cf1, cf2);
    this.renderBlocks.renderFaceXPos(block, -0.5D, -0.5D, -0.5D, this.renderBlocks.getBlockIconFromSideAndMetadata(block, 5, meta));
    tessellator.draw();
  }

  protected ResourceLocation getEntityTexture(EntityFallingBlockEx p_110775_1_) {
    return TextureMap.locationBlocksTexture;
  }

  protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
    return getEntityTexture((EntityFallingBlockEx)p_110775_1_);
  }

  public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
    doRender((EntityFallingBlockEx)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
  }
}
