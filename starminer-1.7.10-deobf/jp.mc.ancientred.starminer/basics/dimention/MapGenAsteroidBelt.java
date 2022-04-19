package jp.mc.ancientred.starminer.basics.dimention;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class MapGenAsteroidBelt
{
  protected int range = 8;

  protected Random rand = new Random();

  protected World worldObj;

  public void generate(IChunkProvider par1IChunkProvider, World world, int xChunkPos, int zChunkPos, Block[] blocksData) {
    int rangeLoc = this.range;
    this.worldObj = world;
    this.rand.setSeed(world.getSeed());
    long l = this.rand.nextLong();
    long i1 = this.rand.nextLong();
    
    for (int workChunkX = xChunkPos - rangeLoc; workChunkX <= xChunkPos + rangeLoc; workChunkX++) {
      
      for (int workChunkZ = zChunkPos - rangeLoc; workChunkZ <= zChunkPos + rangeLoc; workChunkZ++) {
        
        long l1 = workChunkX * l;
        long i2 = workChunkZ * i1;
        this.rand.setSeed(l1 ^ i2 ^ world.getSeed());
        recursiveGenerate(world, workChunkX, workChunkZ, xChunkPos, zChunkPos, blocksData);
      } 
    } 
  }

  protected void recursiveGenerate(World world, int workChunkX, int workChunkZ, int xChunkPos, int zChunkPos, Block[] blocksData) {
    int i1 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(4) + 1) + 1);
    
    if (this.rand.nextInt(15) != 0)
    {
      i1 = 0;
    }
    
    for (int j1 = 0; j1 < i1; j1++) {
      
      double workPosX = (workChunkX * 16 + this.rand.nextInt(16));
      double workPosY = this.rand.nextInt(this.rand.nextInt(230) + 15);
      double workPosZ = (workChunkZ * 16 + this.rand.nextInt(16));
      int k1 = 1;

      float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
      float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
      float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
      
      generateCaveNode(this.rand.nextLong(), xChunkPos, zChunkPos, blocksData, workPosX, workPosY, workPosZ, f2, f, f1, 0, 0, 1.0D);
    } 
  }

  protected void generateCaveNode(long par1, int xChunkPos, int zChunkPos, Block[] blocksData, double workPosX, double workPosY, double workPosZ, float par12, float hDirectionYawRad, float vDirectionPitchRad, int par15, int par16, double par17) {
    double chunkCenterX = (xChunkPos * 16 + 8);
    double chunkCenterZ = (zChunkPos * 16 + 8);
    float f3 = 0.0F;
    float f4 = 0.0F;
    Random random = new Random(par1);
    
    if (par16 <= 0) {
      
      int j1 = this.range * 16 * 4 - 16;
      par16 = j1 - random.nextInt(j1 / 4);
    } 
    
    boolean flag = false;
    
    if (par15 == -1) {
      
      par15 = par16 / 2;
      flag = true;
    } 
    
    int k1 = random.nextInt(par16 / 2) + par16 / 4;
    
    for (boolean flag1 = (random.nextInt(6) == 0); par15 < par16; par15++) {
      
      double d6 = 16.0D + (MathHelper.sin(par15 * 3.1415927F / par16) * par12 * 1.0F);
      double d7 = d6 * par17;
      float vPercentage = MathHelper.cos(vDirectionPitchRad);
      float vDirection = MathHelper.sin(vDirectionPitchRad);
      workPosX += (MathHelper.cos(hDirectionYawRad) * vPercentage);
      workPosY += vDirection;
      workPosZ += (MathHelper.sin(hDirectionYawRad) * vPercentage);
      
      if (flag1) {
        
        vDirectionPitchRad *= 0.92F;
      }
      else {
        
        vDirectionPitchRad *= 0.7F;
      } 
      
      vDirectionPitchRad += f4 * 0.1F;
      hDirectionYawRad += f3 * 0.1F;
      f4 *= 0.9F;
      f3 *= 0.75F;
      f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
      f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;

      if (flag || random.nextInt(4) != 0) {
        
        double d8 = workPosX - chunkCenterX;
        double d9 = workPosZ - chunkCenterZ;
        double d10 = (par16 - par15);
        double d11 = (par12 + 2.0F + 16.0F);
        
        if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
          return;
        }

        if (workPosX >= chunkCenterX - 16.0D - d6 * 2.0D && workPosZ >= chunkCenterZ - 16.0D - d6 * 2.0D && workPosX <= chunkCenterX + 16.0D + d6 * 2.0D && workPosZ <= chunkCenterZ + 16.0D + d6 * 2.0D) {

          int xMin = MathHelper.floor_double(workPosX - d6) - xChunkPos * 16 - 1;
          int xMax = MathHelper.floor_double(workPosX + d6) - xChunkPos * 16 + 1;
          int yMin = MathHelper.floor_double(workPosY - d7) - 1;
          int yMax = MathHelper.floor_double(workPosY + d7) + 1;
          int zMin = MathHelper.floor_double(workPosZ - d6) - zChunkPos * 16 - 1;
          int zMax = MathHelper.floor_double(workPosZ + d6) - zChunkPos * 16 + 1;
          
          if (xMin < 0)
          {
            xMin = 0;
          }
          
          if (xMax > 16)
          {
            xMax = 16;
          }
          
          if (yMin < 1)
          {
            yMin = 1;
          }
          
          if (yMax > 240)
          {
            yMax = 240;
          }
          
          if (zMin < 0)
          {
            zMin = 0;
          }
          
          if (zMax > 16)
          {
            zMax = 16;
          }
          
          boolean isOceanBlock = false;
          
          int x;
          
          for (x = xMin; !isOceanBlock && x < xMax; x++) {
            
            for (int l3 = zMin; !isOceanBlock && l3 < zMax; l3++) {
              
              for (int i4 = yMax + 1; !isOceanBlock && i4 >= yMin - 1; i4--) {
                
                int z = (x * 16 + l3) * 128 + i4;
                
                if (i4 >= 0 && i4 < 256)
                {
                  if (i4 != yMin - 1 && x != xMin && x != xMax - 1 && l3 != zMin && l3 != zMax - 1)
                  {
                    i4 = yMin;
                  }
                }
              } 
            } 
          } 
          
          if (!isOceanBlock) {
            
            for (x = xMin; x < xMax; x++) {
              
              double d12 = ((x + xChunkPos * 16) + 0.5D - workPosX) / d6;
              
              for (int z = zMin; z < zMax; z++) {
                
                double d13 = ((z + zChunkPos * 16) + 0.5D - workPosZ) / d6;
                boolean flag3 = false;
                
                if (d12 * d12 + d13 * d13 < 1.0D)
                {
                  for (int y = yMax - 1; y >= yMin; y--) {
                    
                    int index = y << 8 | z << 4 | x;
                    double d14 = (y + 0.5D - workPosY) / d7;
                    
                    if (d14 > -0.7D && d12 * d12 + d14 * d14 + d13 * d13 < 1.0D) {
                      
                      int randVal = random.nextInt(32768);
                      if (randVal == 1) {
                        blocksData[index] = Blocks.packed_ice;
                      } else if (randVal <= 4) {
                        blocksData[index] = Blocks.dirt;
                      } else if (randVal <= 8) {
                        blocksData[index] = Blocks.cobblestone;
                      } 
                    } 
                    
                    index--;
                  } 
                }
              } 
            } 
            
            if (flag) {
              break;
            }
          } 
        } 
      } 
    } 
  }

  protected boolean isOceanBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ) {
    return (data[index] == Blocks.water);
  }

  private boolean isExceptionBiome(BiomeGenBase biome) {
    if (biome == BiomeGenBase.mushroomIsland) return true; 
    if (biome == BiomeGenBase.beach) return true; 
    if (biome == BiomeGenBase.desert) return true; 
    return false;
  }

  private boolean isTopBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ) {
    BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16);
    return isExceptionBiome(biome) ? ((data[index] == Blocks.grass)) : ((data[index] == biome.topBlock));
  }

  protected void digBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop) {
    data[index] = Blocks.dirt;
  }
}
