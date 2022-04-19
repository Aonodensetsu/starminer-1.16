package jp.mc.ancientred.starminer.basics.block.gravitized;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class WorldGenSavannaTreeG
  extends WorldGenAbstractTreeEx
{
  public int minTreeHeight;
  
  public WorldGenSavannaTreeG(boolean par1) {
    super(par1);
  }

  public WorldGenSavannaTreeG(boolean par1, int minTreeHeight, int argDir) {
    super(par1);
    this.minTreeHeight = minTreeHeight;
    this.dir = argDir;
  }

  public boolean generate(World par1World, Random par2Random, int posX, int posY, int posZ) {
    this.saved.x = posX;
    this.saved.y = posY;
    this.saved.z = posZ;
    
    int l = par2Random.nextInt(3) + par2Random.nextInt(3) + 5;
    boolean flag = true;

    if (par1World != null)
    {
      for (int yCnt = posY; yCnt <= posY + 1 + l; yCnt++) {
        
        byte b0 = 1;
        
        if (yCnt == posY)
        {
          b0 = 0;
        }
        
        if (yCnt >= posY + 1 + l - 2)
        {
          b0 = 2;
        }
        
        for (int i = posX - b0; i <= posX + b0 && flag; i++) {
          
          for (int j = posZ - b0; j <= posZ + b0 && flag; j++) {

            translateXYZ(i, yCnt, j);
            
            Block block = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
            
            if (!isReplaceable(par1World, this.trans.x, this.trans.y, this.trans.z))
            {
              flag = false;
            }
          } 
        } 
      } 
    }

    if (!flag)
    {
      return false;
    }

    int j3 = par2Random.nextInt(4);
    int xCnt = l - par2Random.nextInt(4) - 1;
    int k1 = 3 - par2Random.nextInt(3);
    int k3 = posX;
    int l1 = posZ;
    int i2 = 0;
    
    int j2;
    
    for (j2 = 0; j2 < l; j2++) {
      
      int k2 = posY + j2;
      
      if (j2 >= xCnt && k1 > 0) {
        
        k3 += Direction.offsetX[j3];
        l1 += Direction.offsetZ[j3];
        k1--;
      } 
      
      translateXYZ(k3, k2, l1);
      
      if (par1World != null) {
        Block block1 = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
        
        if (block1.isAir((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z) || block1.isLeaves((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z)) {
          
          setBlockAndNotifyAdequately(par1World, this.trans.x, this.trans.y, this.trans.z, Blocks.log2, convertWoodMeta(0));
          i2 = k2;
        } 
      } else {
        setBlockForChunkProvide(this.trans.x, this.trans.y, this.trans.z, Blocks.log2, convertWoodMeta(0));
        i2 = k2;
      } 
    } 
    
    for (j2 = -1; j2 <= 1; j2++) {
      
      for (int k2 = -1; k2 <= 1; k2++) {
        
        translateXYZ(k3 + j2, i2 + 1, l1 + k2);
        setLeaf(par1World, this.trans.x, this.trans.y, this.trans.z);
      } 
    } 
    
    translateXYZ(k3 + 2, i2 + 1, l1);
    setLeaf(par1World, this.trans.x, this.trans.y, this.trans.z);
    translateXYZ(k3 - 2, i2 + 1, l1);
    setLeaf(par1World, this.trans.x, this.trans.y, this.trans.z);
    translateXYZ(k3, i2 + 1, l1 + 2);
    setLeaf(par1World, this.trans.x, this.trans.y, this.trans.z);
    translateXYZ(k3, i2 + 1, l1 - 2);
    setLeaf(par1World, this.trans.x, this.trans.y, this.trans.z);
    
    for (j2 = -3; j2 <= 3; j2++) {
      
      for (int k2 = -3; k2 <= 3; k2++) {
        
        if (Math.abs(j2) != 3 || Math.abs(k2) != 3) {
          
          translateXYZ(k3 + j2, i2, l1 + k2);
          setLeaf(par1World, this.trans.x, this.trans.y, this.trans.z);
        } 
      } 
    } 
    
    k3 = posX;
    l1 = posZ;
    j2 = par2Random.nextInt(4);
    
    if (j2 != j3) {
      
      int k2 = xCnt - par2Random.nextInt(2) - 1;
      int l3 = 1 + par2Random.nextInt(3);
      i2 = 0;
      
      int l2;
      
      for (l2 = k2; l2 < l && l3 > 0; l3--) {
        
        if (l2 >= 1) {
          
          int i3 = posY + l2;
          k3 += Direction.offsetX[j2];
          l1 += Direction.offsetZ[j2];
          
          translateXYZ(k3, i3, l1);
          if (par1World != null) {
            Block block2 = par1World.getBlock(this.trans.x, this.trans.y, this.trans.z);
            
            if (block2.isAir((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z) || block2.isLeaves((IBlockAccess)par1World, this.trans.x, this.trans.y, this.trans.z)) {
              
              setBlockAndNotifyAdequately(par1World, this.trans.x, this.trans.y, this.trans.z, Blocks.log2, convertWoodMeta(0));
              i2 = i3;
            } 
          } else {
            setBlockForChunkProvide(this.trans.x, this.trans.y, this.trans.z, Blocks.log2, convertWoodMeta(0));
            i2 = i3;
          } 
        } 
        
        l2++;
      } 
      
      if (i2 > 0) {
        
        for (l2 = -1; l2 <= 1; l2++) {
          
          for (int i3 = -1; i3 <= 1; i3++) {
            
            translateXYZ(k3 + l2, i2 + 1, l1 + i3);
            setLeaf(par1World, this.trans.x, this.trans.y, this.trans.z);
          } 
        } 
        
        for (l2 = -2; l2 <= 2; l2++) {
          
          for (int i3 = -2; i3 <= 2; i3++) {
            
            if (Math.abs(l2) != 2 || Math.abs(i3) != 2) {
              
              translateXYZ(k3 + l2, i2, l1 + i3);
              setLeaf(par1World, this.trans.x, this.trans.y, this.trans.z);
            } 
          } 
        } 
      } 
    } 
    
    return true;
  }

  private void setLeaf(World parWorld, int p_150525_2_, int p_150525_3_, int p_150525_4_) {
    if (parWorld != null) {
      Block block = parWorld.getBlock(p_150525_2_, p_150525_3_, p_150525_4_);
      
      if (block.isAir((IBlockAccess)parWorld, p_150525_2_, p_150525_3_, p_150525_4_) || block.isLeaves((IBlockAccess)parWorld, p_150525_2_, p_150525_3_, p_150525_4_))
      {
        setBlockAndNotifyAdequately(parWorld, p_150525_2_, p_150525_3_, p_150525_4_, (Block)Blocks.leaves2, 0);
      }
    } else {
      setBlockForChunkProvide(p_150525_2_, p_150525_3_, p_150525_4_, (Block)Blocks.leaves2, 0);
    } 
  }
}
