package jp.mc.ancientred.starminer.basics.block.gravitized;

import jp.mc.ancientred.starminer.basics.block.DirectionConst;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class WorldGenAbstractTreeEx extends WorldGenAbstractTree {
  public int dir;
  protected TranslatedVec saved;
  protected TranslatedVec trans;
  private StructureBoundingBox chunkBoundingBox;
  private Block[] blocksData;
  private byte[] blockMetas;
  
  public WorldGenAbstractTreeEx(boolean p_i45448_1_) { super(p_i45448_1_);

    this.saved = new TranslatedVec();
    this.trans = new TranslatedVec(); } public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
    return false;
  } protected int convertWoodMeta(int meta) {
    int typeMetaa = meta & 0x3;
    switch (this.dir) {
      case 2:
      case 3:
        return typeMetaa | 0x4;
      case 4:
      case 5:
        return typeMetaa | 0x8;
    } 
    return meta;
  } protected class TranslatedVec {
    public int x; public int y; public int z; } protected void translateXYZ(int argX, int argY, int argZ) {
    switch (this.dir) {
      case 1:
        this.saved.x += argX - this.saved.x;
        this.saved.y -= argY - this.saved.y;
        this.saved.z += argZ - this.saved.z;
        break;
      case 0:
        this.saved.x += argX - this.saved.x;
        this.saved.y += argY - this.saved.y;
        this.saved.z += argZ - this.saved.z;
        break;
      case 3:
        this.saved.x -= argY - this.saved.y;
        this.saved.y += argZ - this.saved.z;
        this.saved.z += argX - this.saved.x;
        break;
      case 2:
        this.saved.x += argY - this.saved.y;
        this.saved.y += argZ - this.saved.z;
        this.saved.z += argX - this.saved.x;
        break;
      case 5:
        this.saved.x += argZ - this.saved.z;
        this.saved.y += argX - this.saved.x;
        this.saved.z -= argY - this.saved.y;
        break;
      case 4:
        this.saved.x += argZ - this.saved.z;
        this.saved.y += argX - this.saved.x;
        this.saved.z += argY - this.saved.y;
        break;
    } 
  }

  public void setChunkProviderSupportData(StructureBoundingBox parChunkBoundingBox, Block[] parBlocksData, byte[] parBlockMetas) {
    this.chunkBoundingBox = parChunkBoundingBox;
    this.blocksData = parBlocksData;
    this.blockMetas = parBlockMetas;
  }

  protected void setBlockForChunkProvide(int posX, int posY, int posZ, Block parBlock, int meta) {
    if (posY >= 0 && posY <= 255 && posX >= this.chunkBoundingBox.minX && posX <= this.chunkBoundingBox.maxX && posZ >= this.chunkBoundingBox.minZ && posZ <= this.chunkBoundingBox.maxZ) {

      int targetIdIndex = posY << 8 | posZ - this.chunkBoundingBox.minZ << 4 | posX - this.chunkBoundingBox.minX;

      if (this.blocksData[targetIdIndex] == Blocks.air) {
        int airDir = this.blockMetas[targetIdIndex] - 1;
        if (airDir >= 0 && airDir < DirectionConst.OPPOSITE_CNV.length) {
          int plantPosX = posX + DirectionConst.CHECKNEIGHBOR_LIST[DirectionConst.OPPOSITE_CNV[airDir]][0];
          int plantPosY = posY + DirectionConst.CHECKNEIGHBOR_LIST[DirectionConst.OPPOSITE_CNV[airDir]][1];
          int plantPosZ = posZ + DirectionConst.CHECKNEIGHBOR_LIST[DirectionConst.OPPOSITE_CNV[airDir]][2];

          if (plantPosY >= 0 && plantPosY <= 255 && plantPosX >= this.chunkBoundingBox.minX && plantPosX <= this.chunkBoundingBox.maxX && plantPosZ >= this.chunkBoundingBox.minZ && posZ <= this.chunkBoundingBox.maxZ) {

            int plantIdIndex = plantPosY << 8 | plantPosZ - this.chunkBoundingBox.minZ << 4 | plantPosX - this.chunkBoundingBox.minX;
            if (this.blocksData[plantIdIndex] instanceof IGravitizedPlants) {
              this.blocksData[plantIdIndex] = Blocks.air;
            }
          } 
        } 
      } 
      
      this.blocksData[targetIdIndex] = parBlock;
      this.blockMetas[targetIdIndex] = (byte)meta;
    } 
  }
}
