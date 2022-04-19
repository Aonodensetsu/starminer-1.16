package jp.mc.ancientred.starminer.basics.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemStardust
  extends Item
{
  public ItemStardust() {
    setTextureName("glowstone_dust");
    setMaxStackSize(64);
  }

  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses() {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public int getRenderPasses(int metadata) {
    return 2;
  }
  
  public int getColorFromItemStack(ItemStack stack, int pass) {
    if (pass == 0) {
      return 16746751;
    }
    return super.getColorFromItemStack(stack, pass);
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack stack, int pass) {
    if (pass == 1)
    {
      return ((ItemStarContoler)SMModContainer.StarControlerItem).starMarkIcon;
    }
    return super.getIcon(stack, pass);
  }
}
