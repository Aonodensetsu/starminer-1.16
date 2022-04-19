package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.SMReflectionHelperClient;
import jp.mc.ancientred.starminer.basics.block.BlockRotator;
import jp.mc.ancientred.starminer.basics.dummy.DummyRotatedBlockAccess;
import jp.mc.ancientred.starminer.basics.dummy.TesselatorWrapper;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityBlockRotator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class BlockRotatorRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 398378466;
  private DummyRotatedBlockAccess wrappedBlockAccess;
  private TesselatorWrapper wrappedTesselator;
  
  public int getRenderId() {
    return 398378466;
  }
  
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
    Tessellator tessellator = Tessellator.instance;
    block.setBlockBoundsForItemRender();
    renderer.setRenderBoundsFromBlock(block);
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, -1.0F, 0.0F);
    renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 1.0F, 0.0F);
    renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, -1.0F);
    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, 1.0F);
    renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
    renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(1.0F, 0.0F, 0.0F);
    renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
    tessellator.draw();
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  }

  public boolean renderWorldBlock(IBlockAccess world, int parX, int parY, int parZ, Block block, int modelId, RenderBlocks renderer) {
    Tessellator tessellatorOrg = Tessellator.instance;
    IBlockAccess blockAccessOrg = renderer.blockAccess;
    
    (BlockRotator)block; TileEntityBlockRotator tileEntity = BlockRotator.getTileEntityBlockRotator(world, parX, parY, parZ);
    
    Block storedBlock = tileEntity.getStoredBlock();
    GravityDirection gDir = tileEntity.getGravityDirection();

    
    if (storedBlock == null || storedBlock == Blocks.air) {
      return false;
    }

    if (storedBlock.getRenderBlockPass() != ForgeHooksClient.getWorldRenderPass()) {
      return false;
    }

    try {
      if (this.wrappedBlockAccess == null) this.wrappedBlockAccess = DummyRotatedBlockAccess.get(); 
      if (this.wrappedTesselator == null) this.wrappedTesselator = new TesselatorWrapper();

      
      if (renderer.blockAccess != this.wrappedBlockAccess) {
        renderer.blockAccess = this.wrappedBlockAccess.wrapp(blockAccessOrg, gDir, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
      }
      
      if (Tessellator.instance != this.wrappedTesselator) {
        SMReflectionHelperClient.setWrappedTesselator(this.wrappedTesselator.wrap(tessellatorOrg, gDir, parX + 0.5D, parY + 0.5D, parZ + 0.5D));
      }

      if (storedBlock instanceof BlockDoublePlant) {
        
        renderBlockDoublePlant((BlockDoublePlant)storedBlock, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, renderer);
      } else {
        renderer.renderBlockAllFaces(storedBlock, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
      }
    
    } finally {
      
      renderer.blockAccess = blockAccessOrg;
      SMReflectionHelperClient.setWrappedTesselator(tessellatorOrg);
    } 
    
    return true;
  }

  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }

  public boolean renderBlockDoublePlant(BlockDoublePlant blockDoublePlant, int parX, int parY, int parZ, RenderBlocks renderer) {
    int k1;
    Tessellator tessellator = Tessellator.instance;
    tessellator.setBrightness(blockDoublePlant.getMixedBrightnessForBlock(renderer.blockAccess, parX, parY, parZ));
    int l = blockDoublePlant.colorMultiplier(renderer.blockAccess, parX, parY, parZ);
    float f = (l >> 16 & 0xFF) / 255.0F;
    float f1 = (l >> 8 & 0xFF) / 255.0F;
    float f2 = (l & 0xFF) / 255.0F;
    
    if (EntityRenderer.anaglyphEnable) {
      
      float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
      float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
      float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
      f = f3;
      f1 = f4;
      f2 = f5;
    } 
    
    tessellator.setColorOpaque_F(f, f1, f2);
    
    double d19 = parX;
    double d0 = parY;
    double d1 = parZ;
    
    int i1 = renderer.blockAccess.getBlockMetadata(parX, parY, parZ);
    boolean flag = false;
    boolean flag1 = BlockDoublePlant.func_149887_c(i1);

    if (flag1) {
      
      if (renderer.blockAccess.getBlock(parX, parY - 1, parZ) != blockDoublePlant)
      {
        return false;
      }
      
      k1 = BlockDoublePlant.func_149890_d(renderer.blockAccess.getBlockMetadata(parX, parY - 1, parZ));
    }
    else {
      
      k1 = BlockDoublePlant.func_149890_d(i1);
    } 
    
    IIcon iicon = blockDoublePlant.func_149888_a(flag1, k1);
    renderer.drawCrossedSquares(iicon, d19, d0, d1, 1.0F);
    
    if (flag1 && k1 == 0) {
      
      IIcon iicon1 = blockDoublePlant.sunflowerIcons[0];
      double d2 = Math.cos(0.8D) * Math.PI * 0.1D;
      double d3 = Math.cos(d2);
      double d4 = Math.sin(d2);
      double d5 = iicon1.getMinU();
      double d6 = iicon1.getMinV();
      double d7 = iicon1.getMaxU();
      double d8 = iicon1.getMaxV();
      double d9 = 0.3D;
      double d10 = -0.05D;
      double d11 = 0.5D + 0.3D * d3 - 0.5D * d4;
      double d12 = 0.5D + 0.5D * d3 + 0.3D * d4;
      double d13 = 0.5D + 0.3D * d3 + 0.5D * d4;
      double d14 = 0.5D + -0.5D * d3 + 0.3D * d4;
      double d15 = 0.5D + -0.05D * d3 + 0.5D * d4;
      double d16 = 0.5D + -0.5D * d3 + -0.05D * d4;
      double d17 = 0.5D + -0.05D * d3 - 0.5D * d4;
      double d18 = 0.5D + 0.5D * d3 + -0.05D * d4;
      tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d5, d8);
      tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d7, d8);
      tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d7, d6);
      tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d5, d6);
      IIcon iicon2 = blockDoublePlant.sunflowerIcons[1];
      d5 = iicon2.getMinU();
      d6 = iicon2.getMinV();
      d7 = iicon2.getMaxU();
      d8 = iicon2.getMaxV();
      tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d5, d8);
      tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d7, d8);
      tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d7, d6);
      tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d5, d6);
    } 
    
    return true;
  }
}
