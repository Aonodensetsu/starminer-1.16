package jp.mc.ancientred.starminer.basics.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.mc.ancientred.starminer.basics.Config;
import jp.mc.ancientred.starminer.basics.common.VecUtils;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityNavigator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockNavigatorRenderHelper
  implements ISimpleBlockRenderingHandler
{
  public static final int RENDER_TYPE = 4341807;
  
  public int getRenderId() {
    return 4341807;
  }
  
  public void renderInventoryBlock(Block par1Block, int metadata, int modelID, RenderBlocks renderer) {
    IIcon beaconIcon = Blocks.beacon.getIcon(0, 0);
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    renderer.setRenderBounds(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D);
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, -1.0F, 0.0F);
    renderer.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, beaconIcon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 1.0F, 0.0F);
    renderer.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, beaconIcon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, -1.0F);
    renderer.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, beaconIcon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, 1.0F);
    renderer.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, beaconIcon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
    renderer.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, beaconIcon);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(1.0F, 0.0F, 0.0F);
    renderer.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, beaconIcon);
    tessellator.draw();
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  }
  private static Vec3[] vec = new Vec3[60];
  
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    Tessellator tes = Tessellator.instance;
    boolean isOn = ((world.getBlockMetadata(x, y, z) & 0x1) != 0);
    
    renderer.renderAllFaces = true;
    renderer.setOverrideBlockTexture(renderer.getBlockIcon((Block)Blocks.beacon));
    renderer.setRenderBounds(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D);
    if (isOn) {
      renderer.renderStandardBlock(block, x, y, z);
    } else {
      renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, 0.6F, 0.9F, 0.6F);
    } 
    renderer.renderAllFaces = false;
    renderer.clearOverrideBlockTexture();
    
    if (!isOn) return true;
    
    TileEntity te = world.getTileEntity(x, y, z);
    if (!(te instanceof TileEntityNavigator)) return true;
    
    TileEntityNavigator teNavi = (TileEntityNavigator)te;
    
    Vec3 directionVec = VecUtils.createVec3(teNavi.lookX, teNavi.lookY, teNavi.lookZ);
    Vec3 centerVec = VecUtils.createVec3(x + 0.5D, y + 0.5D, z + 0.5D);

    if (teNavi.isActive()) {
      tes.setColorOpaque_F(1.0F, 1.0F, 0.0F);
      directionVec.xCoord *= 100.0D;
      directionVec.yCoord *= 100.0D;
      directionVec.zCoord *= 100.0D;
    } else {
      directionVec.xCoord *= Config.naviLaserLength;
      directionVec.yCoord *= Config.naviLaserLength;
      directionVec.zCoord *= Config.naviLaserLength;
    } 
    
    directionVec.xCoord += centerVec.xCoord;
    directionVec.yCoord += centerVec.yCoord;
    directionVec.zCoord += centerVec.zCoord;
    
    double w = 0.05D;
    tes.addVertex(centerVec.xCoord + w, centerVec.yCoord, centerVec.zCoord);
    tes.addVertex(directionVec.xCoord + w, directionVec.yCoord, directionVec.zCoord);
    tes.addVertex(directionVec.xCoord - w, directionVec.yCoord, directionVec.zCoord);
    tes.addVertex(centerVec.xCoord - w, centerVec.yCoord, centerVec.zCoord);
    
    tes.addVertex(centerVec.xCoord - w, centerVec.yCoord, centerVec.zCoord);
    tes.addVertex(directionVec.xCoord - w, directionVec.yCoord, directionVec.zCoord);
    tes.addVertex(directionVec.xCoord + w, directionVec.yCoord, directionVec.zCoord);
    tes.addVertex(centerVec.xCoord + w, centerVec.yCoord, centerVec.zCoord);
    
    tes.addVertex(centerVec.xCoord, centerVec.yCoord, centerVec.zCoord + w);
    tes.addVertex(directionVec.xCoord, directionVec.yCoord, directionVec.zCoord + w);
    tes.addVertex(directionVec.xCoord, directionVec.yCoord, directionVec.zCoord - w);
    tes.addVertex(centerVec.xCoord, centerVec.yCoord, centerVec.zCoord - w);
    
    tes.addVertex(centerVec.xCoord, centerVec.yCoord, centerVec.zCoord - w);
    tes.addVertex(directionVec.xCoord, directionVec.yCoord, directionVec.zCoord - w);
    tes.addVertex(directionVec.xCoord, directionVec.yCoord, directionVec.zCoord + w);
    tes.addVertex(centerVec.xCoord, centerVec.yCoord, centerVec.zCoord + w);
    
    tes.addVertex(centerVec.xCoord, centerVec.yCoord + w, centerVec.zCoord);
    tes.addVertex(directionVec.xCoord, directionVec.yCoord + w, directionVec.zCoord + w);
    tes.addVertex(directionVec.xCoord, directionVec.yCoord - w, directionVec.zCoord - w);
    tes.addVertex(centerVec.xCoord, centerVec.yCoord - w, centerVec.zCoord - w);
    
    tes.addVertex(centerVec.xCoord, centerVec.yCoord - w, centerVec.zCoord);
    tes.addVertex(directionVec.xCoord, directionVec.yCoord - w, directionVec.zCoord);
    tes.addVertex(directionVec.xCoord, directionVec.yCoord + w, directionVec.zCoord);
    tes.addVertex(centerVec.xCoord, centerVec.yCoord + w, centerVec.zCoord);
    
    return true;
  }

  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }
}
