package jp.mc.ancientred.starminer.basics.dimention;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import jp.mc.ancientred.starminer.basics.entity.EntityComet;
import jp.mc.ancientred.starminer.basics.structure.MapGenStar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderSpace
  implements IChunkProvider
{
  private Random spaceRNG;
  private World spaceWorld;
  private double[] densities;
  private MapGenAsteroidBelt asteroidBeltGenerator = new MapGenAsteroidBelt();
  private MapGenStar starGenerator = new MapGenStar();
  
  protected List spawnableListSpace;
  
  private BiomeGenBase[] biomesForGeneration;

  public ChunkProviderSpace(World par1World, long par2) {
    this.spaceWorld = par1World;
    this.spaceRNG = new Random(par2);
    this.starGenerator.maxDistanceBetweenStars = 15;
    this.starGenerator.minDistanceBetweenStars = 4;
    this.spawnableListSpace = new ArrayList();
    this.spawnableListSpace.add(new BiomeGenBase.SpawnListEntry(EntityComet.class, 10, 1, 1));
  }

  public void generateTerrain(int xChunkPos, int zChunkPos, byte[] par3ArrayOfByte) {}

  public void replaceBlocksForBiome(int xChunkPos, int zChunkPos, byte[] par3ArrayOfByte, BiomeGenBase[] par4ArrayOfBiomeGenBase) {}

  public Chunk loadChunk(int par1, int par2) {
    return provideChunk(par1, par2);
  }

  public Chunk provideChunk(int xChunkPos, int zChunkPos) {
    this.spaceRNG.setSeed(xChunkPos * 341873128712L + zChunkPos * 132897987541L);
    Block[] blocksData = new Block[65536];
    byte[] blockMetas = new byte[65536];
    this.biomesForGeneration = this.spaceWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, xChunkPos * 16, zChunkPos * 16, 16, 16);
    
    this.asteroidBeltGenerator.generate(this, this.spaceWorld, xChunkPos, zChunkPos, blocksData);
    this.starGenerator.generate(this, this.spaceWorld, xChunkPos, zChunkPos, null);
    this.starGenerator.generateStructuresImmidiateInChunk(this.spaceWorld, this.spaceRNG, xChunkPos, zChunkPos, blocksData, blockMetas);

    Chunk chunk = new Chunk(this.spaceWorld, xChunkPos, zChunkPos);
    ChunkHelper.fillinChunk(chunk, this.spaceWorld, blocksData, blockMetas, xChunkPos, zChunkPos);
    byte[] abyte1 = chunk.getBiomeArray();
    for (int k = 0; k < abyte1.length; k++)
    {
      abyte1[k] = (byte)(this.biomesForGeneration[k]).biomeID;
    }
    
    chunk.generateSkylightMap();
    return chunk;
  }

  public boolean chunkExists(int par1, int par2) {
    return true;
  }

  public void populate(IChunkProvider par1IChunkProvider, int xChunkPos, int zChunkPos) {
    BlockSand.fallInstantly = true;
    int k = xChunkPos * 16;
    int l = zChunkPos * 16;

    this.starGenerator.generateStructuresInChunk(this.spaceWorld, this.spaceRNG, xChunkPos, zChunkPos);
    
    BlockSand.fallInstantly = false;
  }

  public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
    return true;
  }

  public void saveExtraData() {}

  public boolean unloadQueuedChunks() {
    return false;
  }

  public boolean canSave() {
    return true;
  }

  public String makeString() {
    return "RandomLevelSource";
  }

  public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
    return (par1EnumCreatureType == EnumCreatureType.creature) ? this.spawnableListSpace : null;
  }

  public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5) {
    return null;
  }

  public int getLoadedChunkCount() {
    return 0;
  }
  
  public void recreateStructures(int par1, int par2) {}
}
