package jp.mc.ancientred.starminer.basics.block.gravitized;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockFlowerGravitized
  extends BlockBushGravitized
{
  private int flowerType;
  
  public BlockFlowerGravitized(int flowerType) {
    this.flowerType = flowerType;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if (this.flowerType == 0) {
      return Blocks.yellow_flower.getIcon(par1, par2);
    }
    return Blocks.red_flower.getIcon(par1, par2);
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister p_149651_1_) {}

  public int damageDropped(int par1) {
    return par1;
  }

  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item parItem, CreativeTabs parTabs, List<ItemStack> parList) {
    int subItemCount = (this.flowerType == 0) ? 1 : 9;
    for (int i = 0; i < subItemCount; i++)
      parList.add(new ItemStack(parItem, 1, i)); 
  }
}
