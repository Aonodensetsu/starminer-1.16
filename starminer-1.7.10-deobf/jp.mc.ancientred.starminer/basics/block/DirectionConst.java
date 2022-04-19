package jp.mc.ancientred.starminer.basics.block;

import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.block.gravitized.IGravitizedPlants;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DirectionConst
{
  public static final int SHIFT = 1;
  public static final int PLACEDIR_YBOT = 0;
  public static final int PLACEDIR_YTOP = 1;
  public static final int PLACEDIR_XBOT = 2;
  public static final int PLACEDIR_XTOP = 3;
  public static final int PLACEDIR_ZBOT = 4;
  public static final int PLACEDIR_ZTOP = 5;
  public static final int[][] CHECKNEIGHBOR_LIST = new int[][] { { 0, 1, 0, 0 }, { 0, -1, 0, 1 }, { 1, 0, 0, 2 }, { -1, 0, 0, 3 }, { 0, 0, 1, 4 }, { 0, 0, -1, 5 } };

  public static final int[] OPPOSITE_CNV = new int[] { 1, 0, 3, 2, 5, 4 };
  public static final int getPlantGravityDirection(IBlockAccess par1World, int parX, int parY, int parZ) {
    int len = CHECKNEIGHBOR_LIST.length;

    for (int i = 0; i < len; i++) {
      int[] neighbor = CHECKNEIGHBOR_LIST[i];
      Block block = par1World.getBlock(parX + neighbor[0], parY + neighbor[1], parZ + neighbor[2]);
      if (block == Blocks.air && 
        par1World.getBlockMetadata(parX + neighbor[0], parY + neighbor[1], parZ + neighbor[2]) - 1 == neighbor[3]) {
        return neighbor[3];
      }
    } 
    
    return -1;
  }
  
  public static final int[] getBlockBedHeadNeighborBody(IBlockAccess par1World, int parX, int parY, int parZ) {
    int len = CHECKNEIGHBOR_LIST.length;

    for (int i = 0; i < len; i++) {
      int[] neighbor = CHECKNEIGHBOR_LIST[i];
      Block block = par1World.getBlock(parX + neighbor[0], parY + neighbor[1], parZ + neighbor[2]);
      if (block == SMModContainer.StarBedBodyBlock && 
        par1World.getBlockMetadata(parX + neighbor[0], parY + neighbor[1], parZ + neighbor[2]) == OPPOSITE_CNV[neighbor[3]]) {
        return neighbor;
      }
    } 
    
    return null;
  }
  
  public static boolean isStayableAtOppositeSide(World par1World, int par2, int par3, int par4, int dir, int meta, IGravitizedPlants plant) {
    if (dir < 0 || OPPOSITE_CNV.length <= dir) return false; 
    int[] neighborToCheck = CHECKNEIGHBOR_LIST[OPPOSITE_CNV[dir]];
    Block oppositeSblock = par1World.getBlock(par2 + neighborToCheck[0], par3 + neighborToCheck[1], par4 + neighborToCheck[2]);
    return plant.allowPlantOn(oppositeSblock, meta);
  }
  
  public static final boolean tryStayable(World par1World, int par2, int par3, int par4, int meta, IGravitizedPlants plant) {
    int len = CHECKNEIGHBOR_LIST.length;

    for (int dir = 0; dir < len; dir++) {
      int[] neighborToCheck = CHECKNEIGHBOR_LIST[OPPOSITE_CNV[dir]];
      Block oppositeSblock = par1World.getBlock(par2 + neighborToCheck[0], par3 + neighborToCheck[1], par4 + neighborToCheck[2]);
      if (plant.allowPlantOn(oppositeSblock, meta)) {
        neighborToCheck = CHECKNEIGHBOR_LIST[dir];
        if (par1World.getBlock(par2 + neighborToCheck[0], par3 + neighborToCheck[1], par4 + neighborToCheck[2]) == Blocks.air && par1World.getBlockMetadata(par2 + neighborToCheck[0], par3 + neighborToCheck[1], par4 + neighborToCheck[2]) == 0) {
          
          par1World.setBlockMetadataWithNotify(par2 + neighborToCheck[0], par3 + neighborToCheck[1], par4 + neighborToCheck[2], dir + 1, 2);
          return true;
        } 
      } 
    } 
    return false;
  }

  public static final void setDummyAirBlockWithMeta(World world, int parX, int parY, int parZ, int dir, int flag) {
    world.setBlock(parX, parY, parZ, Blocks.air, dir, flag);
  }
}
