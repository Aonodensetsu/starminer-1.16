package jp.mc.ancientred.starminer.basics.block.gravitized;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockTallGrassGravitized
  extends BlockBushGravitized
  implements IShearable
{
  public BlockTallGrassGravitized() {
    super(Material.vine, 0.4D, 0.8D);
  }

  public int quantityDroppedWithBonus(int par1, Random par2Random) {
    return 1 + par2Random.nextInt(par1 * 2 + 1);
  }

  public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
    super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
  }
  
  public boolean allowPlantOn(Block block, int meta) {
    if (meta == 1) return (block == Blocks.snow || block == Blocks.clay || super.allowPlantOn(block, meta)); 
    if (meta == 2) return (block != Blocks.air); 
    return false;
  }

  public int getDamageValue(World par1World, int par2, int par3, int par4) {
    return par1World.getBlockMetadata(par2, par3, par4);
  }

  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
    ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    if (world.rand.nextInt(8) != 0)
    {
      return ret;
    }
    
    if (world.rand.nextInt(5) == 0)
    {
      ret.add(new ItemStack(SMModContainer.SeedGravizedItem));
    }
    return ret;
  }

  public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
    return true;
  }

  public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
    ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    ret.add(new ItemStack((Block)this, 1, world.getBlockMetadata(x, y, z)));
    return ret;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    return Blocks.tallgrass.getIcon(par1, par2);
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {}

  public int idDropped(int par1, Random par2Random, int par3) {
    return -1;
  }

  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    par3List.add(new ItemStack(par1, 1, 1));
    par3List.add(new ItemStack(par1, 1, 2));
  }

  @SideOnly(Side.CLIENT)
  public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
    int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
    return (l == 0) ? 16777215 : par1IBlockAccess.getBiomeGenForCoords(par2, par4).getBiomeGrassColor(par2, par3, par4);
  }

  @SideOnly(Side.CLIENT)
  public int getBlockColor() {
    double d0 = 0.5D;
    double d1 = 1.0D;
    return ColorizerGrass.getGrassColor(d0, d1);
  }

  @SideOnly(Side.CLIENT)
  public int getRenderColor(int par1) {
    return (par1 == 0) ? 16777215 : ColorizerFoliage.getFoliageColorBasic();
  }
}
