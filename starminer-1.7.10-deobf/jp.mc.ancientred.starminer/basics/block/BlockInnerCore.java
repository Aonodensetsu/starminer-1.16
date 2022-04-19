package jp.mc.ancientred.starminer.basics.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInnerCore
  extends Block
{
  public BlockInnerCore() {
    super(Material.rock);
    setHardness(45.0F);
    setResistance(2000.0F);
    setLightLevel(1.0F);
  }
  public boolean isOpaqueCube() {
    return false;
  }
}
