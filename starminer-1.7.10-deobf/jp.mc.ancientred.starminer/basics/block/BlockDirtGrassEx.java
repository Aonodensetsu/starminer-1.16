package jp.mc.ancientred.starminer.basics.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDirtGrassEx
  extends Block
{
  public BlockDirtGrassEx() {
    super(Material.ground);
    setHardness(0.5F);
    setStepSound(Block.soundTypeGravel);
  }

  public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
    return true;
  }
  
  public boolean isOpaqueCube() {
    return true;
  }

  @SideOnly(Side.CLIENT)
  public boolean renderAsNormalBlock() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {}
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    if ((meta & 0x8) == 0) {
      return Blocks.grass.getIcon(1, 0);
    }
    return Blocks.farmland.getIcon(1, 1);
  }

  @SideOnly(Side.CLIENT)
  public int getRenderType() {
    return 4341802;
  }

  @SideOnly(Side.CLIENT)
  public int getBlockColor() {
    double d0 = 0.5D;
    double d1 = 1.0D;
    return ColorizerGrass.getGrassColor(d0, d1);
  }

  @SideOnly(Side.CLIENT)
  public int getRenderColor(int par1) {
    return getBlockColor();
  }
}
