package jp.mc.ancientred.starminer.basics.block.gravitized;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WorldGenTaiga2G
  extends WorldGenAbstractTreeEx
{
  public int minTreeHeight;
  
  public WorldGenTaiga2G(boolean par1) {
    super(par1);
  }
  
  public WorldGenTaiga2G(boolean par1, int minTreeHeight, int argDir) {
    super(par1);
    this.minTreeHeight = minTreeHeight;
    this.dir = argDir;
  }

  public boolean generate(World par1World, Random par2Random, int posX, int posY, int posZ) {
    this.saved.x = posX;
    this.saved.y = posY;
    this.saved.z = posZ;
    
    int treeHight = par2Random.nextInt(4) + this.minTreeHeight;
    int i1 = 1 + par2Random.nextInt(2);
    int j1 = treeHight - i1;
    int k1 = 2 + par2Random.nextInt(2);
    boolean cangrow = true;

    if (par1World != null)
    {
      for (int yCnt = posY; yCnt <= posY + 1 + treeHight && cangrow; yCnt++) {
        int j;
        boolean flag1 = true;
        
        if (yCnt - posY < i1) {
          
          j = 0;
        }
        else {
          
          j = k1;
        } 
        
        for (int i = posX - j; i <= posX + j && cangrow; i++) {
          
          for (int zCnt = posZ - j; zCnt <= posZ + j && cangrow; zCnt++) {

            translateXYZ(i, yCnt, zCnt);
            
            Block block = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
            
            if (!isReplaceable(par1World, this.trans.x, this.trans.y, this.trans.z))
            {

              cangrow = false;
            }
          } 
        } 
      } 
    }

    if (!cangrow)
    {
      return false;
    }

    int k2 = par2Random.nextInt(2);
    int xCnt = 1;
    byte b0 = 0;
    
    int j2;
    
    for (j2 = 0; j2 <= j1; j2++) {
      
      int i = posY + treeHight - j2;
      
      for (int xCnt2 = posX - k2; xCnt2 <= posX + k2; xCnt2++) {
        
        int k3 = xCnt2 - posX;
        
        for (int zCnt2 = posZ - k2; zCnt2 <= posZ + k2; zCnt2++) {
          
          int i4 = zCnt2 - posZ;
          
          translateXYZ(xCnt2, i, zCnt2);
          if (par1World != null) {
            Block block = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
            
            if ((Math.abs(k3) != k2 || Math.abs(i4) != k2 || k2 <= 0) && (block == null || block.isAir((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z) || block.canBeReplacedByLeaves((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z)))
            {

              setBlockAndNotifyAdequately(par1World, this.trans.x, this.trans.y, this.trans.z, (Block)Blocks.leaves, 1);
            }
          } else {
            setBlockForChunkProvide(this.trans.x, this.trans.y, this.trans.z, (Block)Blocks.leaves, 1);
          } 
        } 
      } 
      
      if (k2 >= xCnt) {
        
        k2 = b0;
        b0 = 1;
        xCnt++;
        
        if (xCnt > k1)
        {
          xCnt = k1;
        }
      }
      else {
        
        k2++;
      } 
    } 
    
    j2 = par2Random.nextInt(3);
    
    for (int j3 = 0; j3 < treeHight - j2; j3++) {
      
      translateXYZ(posX, posY + j3, posZ);
      if (par1World != null) {
        Block block = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
        
        if (block == null || block.isAir((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z) || block.isLeaves((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z))
        {

          setBlockAndNotifyAdequately(par1World, this.trans.x, this.trans.y, this.trans.z, Blocks.log, convertWoodMeta(1));
        }
      } else {
        setBlockForChunkProvide(this.trans.x, this.trans.y, this.trans.z, Blocks.log, convertWoodMeta(1));
      } 
    } 
    
    return true;
  }
}
