package jp.mc.ancientred.starminer.basics.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenStar
  extends MapGenStructure
{
  public int maxDistanceBetweenStars = 16;
  public int minDistanceBetweenStars = 8;

  public MapGenStar() {}

  public MapGenStar(Map par1Map) {
    this();
    Iterator<Map.Entry> iterator = par1Map.entrySet().iterator();
    
    while (iterator.hasNext()) {
      
      Map.Entry entry = iterator.next();
      
      if (((String)entry.getKey()).equals("distance"))
      {
        this.maxDistanceBetweenStars = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.maxDistanceBetweenStars, this.minDistanceBetweenStars + 1);
      }
    } 
  }
  
  public boolean generateStructuresImmidiateInChunk(World par1World, Random par2Random, int par3, int par4, Block[] blocksData, byte[] blockMetas) {
    int k = par3 << 4;
    int l = par4 << 4;
    boolean flag = false;
    Iterator<StructureStarStart> iterator = this.structureMap.values().iterator();
    
    while (iterator.hasNext()) {
      
      StructureStarStart structurestart = iterator.next();
      
      if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().intersectsWith(k, l, k + 15, l + 15)) {
        
        structurestart.generateStructureImmidiate(par1World, par2Random, new StructureBoundingBox(k, l, k + 15, l + 15), blocksData, blockMetas);
        flag = true;
      } 
    } 
    
    return flag;
  }
  
  public String getStructureName() {
    return "Star";
  }

  protected boolean canSpawnStructureAtCoords(int parX, int parY) {
    int k = parX;
    int l = parY;
    
    if (parX < 0)
    {
      parX -= this.maxDistanceBetweenStars - 1;
    }
    
    if (parY < 0)
    {
      parY -= this.maxDistanceBetweenStars - 1;
    }
    
    int i1 = parX / this.maxDistanceBetweenStars;
    int j1 = parY / this.maxDistanceBetweenStars;
    Random random = this.worldObj.setRandomSeed(i1, j1, 58939324);
    i1 *= this.maxDistanceBetweenStars;
    j1 *= this.maxDistanceBetweenStars;
    i1 += random.nextInt(this.maxDistanceBetweenStars - this.minDistanceBetweenStars);
    j1 += random.nextInt(this.maxDistanceBetweenStars - this.minDistanceBetweenStars);
    
    if (k == i1 && l == j1)
    {
      return true;
    }
    
    return false;
  }

  protected StructureStart getStructureStart(int par1, int par2) {
    return new StructureStarStart(this.worldObj, this.rand, par1, par2);
  }
}
