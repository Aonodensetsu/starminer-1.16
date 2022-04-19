package jp.mc.ancientred.starminer.basics.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockOuterCore
  extends Block
{
  public BlockOuterCore() {
    super(Material.rock);
    setHardness(4.0F);
    setResistance(2000.0F);
    setLightLevel(1.0F);
  }
}
