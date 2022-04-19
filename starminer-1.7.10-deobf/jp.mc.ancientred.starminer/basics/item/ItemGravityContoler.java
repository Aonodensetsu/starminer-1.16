package jp.mc.ancientred.starminer.basics.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemGravityContoler extends Item {
  public static final String G_REVERSE_NBT_TAG = "stmn.g_reverse";
  private IIcon[] itemIconPrivate = new IIcon[2];
  
  public ItemGravityContoler() {
    setTextureName("dummy");
    setMaxStackSize(1);
  }

  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    boolean gcon;
    NBTTagCompound nbttagcompound = itemStack.getTagCompound();
    
    if (nbttagcompound == null) {
      
      nbttagcompound = new NBTTagCompound();
      itemStack.setTagCompound(nbttagcompound);
      gcon = false;
    } else {
      gcon = nbttagcompound.getBoolean("stmn.g_reverse");
    } 
    
    nbttagcompound.setBoolean("stmn.g_reverse", !gcon);
    
    return itemStack;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack itemStack, int pass) {
    if (itemStack.hasTagCompound()) {
      NBTTagCompound tag = itemStack.getTagCompound();
      boolean gcon = tag.getBoolean("stmn.g_reverse");
      return gcon ? this.itemIconPrivate[1] : this.itemIconPrivate[0];
    } 
    return this.itemIconPrivate[0];
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int par1) {
    return this.itemIconPrivate[1];
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIconIndex(ItemStack itemStack) {
    if (itemStack.hasTagCompound()) {
      NBTTagCompound tag = itemStack.getTagCompound();
      boolean gcon = tag.getBoolean("stmn.g_reverse");
      return gcon ? this.itemIconPrivate[1] : this.itemIconPrivate[0];
    } 
    return this.itemIconPrivate[0];
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {
    this.itemIconPrivate[0] = par1IconRegister.registerIcon("starminer:gcontroler_a");
    this.itemIconPrivate[1] = par1IconRegister.registerIcon("starminer:gcontroler_b");
  }
}
