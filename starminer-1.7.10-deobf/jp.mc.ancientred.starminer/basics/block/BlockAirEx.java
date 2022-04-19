package jp.mc.ancientred.starminer.basics.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class BlockAirEx
  extends Block
{
  public BlockAirEx() {
    super(Material.air);
    setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
  }
  public boolean isOpaqueCube() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    return Blocks.glass.getIcon(par1, par2);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {}
}
