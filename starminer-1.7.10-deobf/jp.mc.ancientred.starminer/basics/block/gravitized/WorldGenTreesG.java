package jp.mc.ancientred.starminer.basics.block.gravitized;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WorldGenTreesG
  extends WorldGenAbstractTreeEx
{
  public int minTreeHeight;
  public int metaWood;
  public int metaLeaves;
  private Block logBlock = Blocks.log;
  private Block leafBlock = (Block)Blocks.leaves;
  
  public WorldGenTreesG(boolean par1) {
    super(par1);
  }

  public WorldGenTreesG(boolean par1, int argDir, boolean asNewTreeSet) {
    this(par1, 4, 0, 0, false, argDir, true);
  }
  
  public WorldGenTreesG(boolean par1, int argDir) {
    this(par1, 4, 0, 0, false, argDir, false);
  }
  
  public WorldGenTreesG(boolean par1, int par2, int par3, int par4, boolean par5, int argDir) {
    this(par1, par2, par3, par4, par5, argDir, false);
  }
  
  public WorldGenTreesG(boolean par1, int par2, int par3, int par4, boolean par5, int argDir, boolean asNewTreeSet) {
    super(par1);
    this.dir = argDir;
    this.minTreeHeight = par2;
    this.metaWood = par3;
    this.metaLeaves = par4;

    setAsNewTreeSet(asNewTreeSet);
  }
  public void setAsNewTreeSet(boolean asNewTreeSet) {
    if (asNewTreeSet) {
      this.logBlock = Blocks.log2;
      this.leafBlock = (Block)Blocks.leaves2;
    } else {
      this.logBlock = Blocks.log;
      this.leafBlock = (Block)Blocks.leaves;
    } 
  }

  public boolean generate(World par1World, Random par2Random, int posX, int posY, int posZ) {
    this.saved.x = posX;
    this.saved.y = posY;
    this.saved.z = posZ;
    
    int treeHight = par2Random.nextInt(3) + this.minTreeHeight;
    boolean canGrow = true;

    if (par1World != null)
    {
      for (int i1 = posY; i1 <= posY + 1 + treeHight; i1++) {
        
        byte b = 1;
        
        if (i1 == posY)
        {
          b = 0;
        }
        
        if (i1 >= posY + 1 + treeHight - 2)
        {
          b = 2;
        }
        
        for (int xCnt = posX - b; xCnt <= posX + b && canGrow; xCnt++) {
          
          for (int i = posZ - b; i <= posZ + b && canGrow; i++) {


            
            translateXYZ(xCnt, i1, i);
            
            Block block = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
            
            if (!isReplaceable(par1World, this.trans.x, this.trans.y, this.trans.z))
            {
              canGrow = false;
            }
          } 
        } 
      } 
    }

    if (!canGrow)
    {
      return false;
    }

    byte b0 = 3;
    byte b1 = 0;

    int yCnt;
    
    for (yCnt = posY - b0 + treeHight; yCnt <= posY + treeHight; yCnt++) {
      
      int k1 = yCnt - posY + treeHight;
      int i2 = b1 + 1 - k1 / 2;
      
      for (int xCnt = posX - i2; xCnt <= posX + i2; xCnt++) {
        
        int k2 = xCnt - posX;
        
        for (int zCnt = posZ - i2; zCnt <= posZ + i2; zCnt++) {
          
          int i3 = zCnt - posZ;
          
          if (Math.abs(k2) != i2 || Math.abs(i3) != i2 || (par2Random.nextInt(2) != 0 && k1 != 0)) {
            
            translateXYZ(xCnt, yCnt, zCnt);
            
            if (par1World != null) {
              
              Block block = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
              
              if (block == null || block.canBeReplacedByLeaves((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z))
              {
                setBlockAndNotifyAdequately(par1World, this.trans.x, this.trans.y, this.trans.z, this.leafBlock, this.metaLeaves);
              }
            } else {
              setBlockForChunkProvide(this.trans.x, this.trans.y, this.trans.z, this.leafBlock, this.metaLeaves);
            } 
          } 
        } 
      } 
    } 
    
    for (yCnt = 0; yCnt < treeHight; yCnt++) {
      
      translateXYZ(posX, posY + yCnt, posZ);
      if (par1World != null) {
        Block block = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
        
        if (block == null || block.isAir((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z) || block.isLeaves((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z))
        {
          setBlockAndNotifyAdequately(par1World, this.trans.x, this.trans.y, this.trans.z, this.logBlock, convertWoodMeta(this.metaWood));

        }

      }
      else {

        setBlockForChunkProvide(this.trans.x, this.trans.y, this.trans.z, this.logBlock, convertWoodMeta(this.metaWood));
      } 
    } 

    return true;
  }
  
  private void growVines(World par1World, int par2, int par3, int par4, int par5) {}
}
