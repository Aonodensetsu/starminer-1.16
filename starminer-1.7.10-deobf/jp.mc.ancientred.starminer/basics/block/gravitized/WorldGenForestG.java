package jp.mc.ancientred.starminer.basics.block.gravitized;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WorldGenForestG
  extends WorldGenAbstractTreeEx
{
  public int minTreeHeight;
  
  public WorldGenForestG(boolean par1) {
    super(par1);
  }
  
  public WorldGenForestG(boolean par1, int minTreeHeight, int argDir) {
    super(par1);
    this.minTreeHeight = minTreeHeight;
    this.dir = argDir;
  }

  public boolean generate(World par1World, Random par2Random, int posX, int posY, int posZ) {
    this.saved.x = posX;
    this.saved.y = posY;
    this.saved.z = posZ;
    
    int treeHight = par2Random.nextInt(3) + this.minTreeHeight;
    boolean canGrow = true;

    if (par1World != null)
    {
      for (int yCnt = posY; yCnt <= posY + 1 + treeHight; yCnt++) {
        
        byte b0 = 1;
        
        if (yCnt == posY)
        {
          b0 = 0;
        }
        
        if (yCnt >= posY + 1 + treeHight - 2)
        {
          b0 = 2;
        }
        
        for (int xCnt = posX - b0; xCnt <= posX + b0 && canGrow; xCnt++) {
          
          for (int zCnt = posZ - b0; zCnt <= posZ + b0 && canGrow; zCnt++) {


            translateXYZ(xCnt, yCnt, zCnt);
            
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

    int yCnt2;

    for (yCnt2 = posY - 3 + treeHight; yCnt2 <= posY + treeHight; yCnt2++) {
      
      int xCnt = yCnt2 - posY + treeHight;
      int zCnt = 1 - xCnt / 2;
      
      for (int xCnt2 = posX - zCnt; xCnt2 <= posX + zCnt; xCnt2++) {
        
        int j2 = xCnt2 - posX;
        
        for (int zCnt2 = posZ - zCnt; zCnt2 <= posZ + zCnt; zCnt2++) {
          
          int l2 = zCnt2 - posZ;
          
          if (Math.abs(j2) != zCnt || Math.abs(l2) != zCnt || (par2Random.nextInt(2) != 0 && xCnt != 0)) {
            
            translateXYZ(xCnt2, yCnt2, zCnt2);
            
            if (par1World != null) {
              Block block = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
              
              if (block == null || block.canBeReplacedByLeaves((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z))
              {
                setBlockAndNotifyAdequately(par1World, this.trans.x, this.trans.y, this.trans.z, (Block)Blocks.leaves, 2);
              }
            } else {
              setBlockForChunkProvide(this.trans.x, this.trans.y, this.trans.z, (Block)Blocks.leaves, 2);
            } 
          } 
        } 
      } 
    } 
    
    for (yCnt2 = 0; yCnt2 < treeHight; yCnt2++) {
      
      translateXYZ(posX, posY + yCnt2, posZ);
      
      if (par1World != null) {
        Block block = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
        
        if (block == null || block.isAir((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z) || block.isLeaves((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z))
        {
          
          setBlockAndNotifyAdequately(par1World, this.trans.x, this.trans.y, this.trans.z, Blocks.log, convertWoodMeta(2));
        }
      } else {
        setBlockForChunkProvide(this.trans.x, this.trans.y, this.trans.z, Blocks.log, convertWoodMeta(2));
      } 
    } 
    
    return true;
  }
}
