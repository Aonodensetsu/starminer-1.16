package jp.mc.ancientred.starminer.basics.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemLifeSoup
  extends Item
{
  public static final int LIFESOUPTYPE_ANIMAL = 0;
  public static final int LIFESOUPTYPE_DIRTY = 1;
  public static final int LIFESOUPTYPE_VILLAGER = 2;
  private String[] SUB_NAMES = new String[] { "animal", "dirty", "villager" };
  private IIcon[] itemIconPrivate = new IIcon[2];

  public ItemLifeSoup() {
    setMaxStackSize(64);
    setMaxDurability(0);
    setHasSubtypes(true);
    setTextureName("starminer:lifesoup_a");
  }

  public String getUnlocalizedName(ItemStack itemStack) {
    int i = itemStack.getMetadata();
    
    if (i < 0 || i >= this.SUB_NAMES.length) {
      i = 0;
    }
    
    return getUnlocalizedName() + "." + this.SUB_NAMES[i];
  }

  public int getItemEnchantability() {
    return 0;
  }

  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
    for (int i = 0; i < this.SUB_NAMES.length; i++) {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int damage) {
    if (damage == 2) {
      return this.itemIconPrivate[1];
    }
    return this.itemIconPrivate[0];
  }
  
  public int getColorFromItemStack(ItemStack itemStack, int pass) {
    if (itemStack.getMetadata() == 1) {
      return 16733525;
    }
    return super.getColorFromItemStack(itemStack, pass);
  }
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {
    this.itemIconPrivate[0] = par1IconRegister.registerIcon("starminer:lifesoup_a");
    this.itemIconPrivate[1] = par1IconRegister.registerIcon("starminer:lifesoup_v");
  }
  
  public static boolean isStackLifeSeedAnimal(ItemStack itemStack) {
    return (itemStack != null && itemStack.getItem() == SMModContainer.LifeSoupItem && itemStack.getMetadata() == 0 && itemStack.stackSize > 0);
  }

  public static boolean isStackLifeSeedAnimalDirty(ItemStack itemStack) {
    return (itemStack != null && itemStack.getItem() == SMModContainer.LifeSoupItem && itemStack.getMetadata() == 1 && itemStack.stackSize > 0);
  }

  public static boolean isStackLifeSeedVillager(ItemStack itemStack) {
    return (itemStack != null && itemStack.getItem() == SMModContainer.LifeSoupItem && itemStack.getMetadata() == 2 && itemStack.stackSize > 0);
  }
}
