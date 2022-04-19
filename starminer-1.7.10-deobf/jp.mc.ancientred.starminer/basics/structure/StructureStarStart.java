package jp.mc.ancientred.starminer.basics.structure;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureStarStart
  extends StructureStart
{
  public StructureStarStart() {}
  
  public StructureStarStart(World par1World, Random par2Random, int par3, int par4) {
    super(par3, par4);
    
    ComponentStar componentStar = new ComponentStar(0, par2Random, (par3 << 4) + 2, (par4 << 4) + 2, par1World);
    this.components.add(componentStar);
    componentStar.buildComponent(componentStar, this.components, par2Random);
    updateBoundingBox();
    markAvailableHeight(par1World, par2Random, 68);
  }
  
  public void generateStructureImmidiate(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox, Block[] blocksData, byte[] blockMetas) {
    Iterator<ComponentStar> iterator = this.components.iterator();
    
    while (iterator.hasNext()) {
      
      ComponentStar structurecomponent = iterator.next();
      
      if (structurecomponent.getBoundingBox().intersectsWith(par3StructureBoundingBox) && !structurecomponent.addComponentParts(par1World, par2Random, par3StructureBoundingBox, blocksData, blockMetas))
      {
        
        iterator.remove();
      }
    } 
  }
  
  public void func_143022_a(NBTTagCompound par1NBTTagCompound) {}
  
  public void func_143017_b(NBTTagCompound par1NBTTagCompound) {}
}
