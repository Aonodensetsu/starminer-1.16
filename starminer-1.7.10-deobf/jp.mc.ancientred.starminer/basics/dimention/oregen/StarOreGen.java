package jp.mc.ancientred.starminer.basics.dimention.oregen;

import java.util.Random;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class StarOreGen
{
  private WorldGenMinable genOuterCore = new WorldGenMinable(SMModContainer.OuterCoreBlock, 8);
  private WorldGenMinable genInnerCore = new WorldGenMinable(SMModContainer.InnerCoreBlock, 4);
  
  private Random randPrivate = new Random();

  public void generateOreInSurface(World world, Random neverUseRand, int chunkX, int chunkZ) {
    this.randPrivate.setSeed(world.getSeed());
    long i1 = this.randPrivate.nextLong() / 2L * 2L + 1L;
    long j1 = this.randPrivate.nextLong() / 2L * 2L + 1L;
    this.randPrivate.setSeed(chunkX * i1 + chunkZ * j1 ^ world.getSeed());

    int minY = 0;
    int maxY = 32; int k;
    for (k = 0; k < 6; k++) {
      int xCoord = chunkX + this.randPrivate.nextInt(16);
      int yCoord = this.randPrivate.nextInt(maxY - minY) + minY;
      int zCoord = chunkZ + this.randPrivate.nextInt(16);
      
      this.genOuterCore.generate(world, this.randPrivate, xCoord, yCoord, zCoord);
    } 

    minY = 0;
    maxY = 16;
    for (k = 0; k < 2; k++) {
      int xCoord = chunkX + this.randPrivate.nextInt(16);
      int yCoord = this.randPrivate.nextInt(maxY - minY) + minY;
      int zCoord = chunkZ + this.randPrivate.nextInt(16);
      
      this.genInnerCore.generate(world, this.randPrivate, xCoord, yCoord, zCoord);
    } 
  }
}
