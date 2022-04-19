package jp.mc.ancientred.starminer.basics.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemGHook
  extends Item
{
  public ItemGHook() {
    setTextureName("arrow");
    setMaxStackSize(64);
  }

  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses() {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public int getRenderPasses(int metadata) {
    return 3;
  }
  
  public int getColorFromItemStack(ItemStack stack, int pass) {
    if (pass == 0) {
      return 6728362;
    }
    return super.getColorFromItemStack(stack, pass);
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack stack, int pass) {
    if (pass == 0)
    {
      return Items.lead.getIcon(stack, pass);
    }
    if (pass == 2)
    {
      return ((ItemStarContoler)SMModContainer.StarControlerItem).starMarkIcon;
    }
    return super.getIcon(stack, pass);
  }
}
