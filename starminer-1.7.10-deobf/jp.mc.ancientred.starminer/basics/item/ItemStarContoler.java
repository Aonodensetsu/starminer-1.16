package jp.mc.ancientred.starminer.basics.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemStarContoler
  extends Item
{
  public IIcon starMarkIcon;
  public static final double DISTANCE_MAX = 64.0D;
  public static final String TARGETX_NBT_TAG = "stmn.tX";
  public static final String TARGETY_NBT_TAG = "stmn.tY";
  public static final String TARGETZ_NBT_TAG = "stmn.tZ";
  
  public ItemStarContoler() {
    setTextureName("starminer:scontroler");
    setMaxStackSize(1);
  }
  
  public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    TileEntity te = world.getTileEntity(x, y, z);
    if (!world.isRemote && 
      te != null && te instanceof jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator) {
      NBTTagCompound nbttagcompound = itemStack.getTagCompound();
      if (nbttagcompound == null) {
        
        nbttagcompound = new NBTTagCompound();
        itemStack.setTagCompound(nbttagcompound);
      } 
      
      if (!nbttagcompound.hasKey("stmn.tX")) {
        nbttagcompound.setInteger("stmn.tX", x);
        nbttagcompound.setInteger("stmn.tY", y);
        nbttagcompound.setInteger("stmn.tZ", z);
      } 
      return true;
    } 
    
    return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
  }

  public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
    NBTTagCompound nbttagcompound = itemStack.getTagCompound();
    if (nbttagcompound != null && nbttagcompound.hasKey("stmn.tX")) {
      
      list.add(StatCollector.translateToLocalFormatted("starInfo.sconInfoON", new Object[] { Integer.valueOf(nbttagcompound.getInteger("stmn.tX")), Integer.valueOf(nbttagcompound.getInteger("stmn.tY")), Integer.valueOf(nbttagcompound.getInteger("stmn.tZ")) }));
    
    }
    else {
      
      list.add(StatCollector.translateToLocal("starInfo.sconInfoOFF"));
    } 
  }

  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    if (!world.isRemote) {
      NBTTagCompound nbttagcompound = itemStack.getTagCompound();
      if (nbttagcompound != null)
      {
        if (nbttagcompound.hasKey("stmn.tX")) {
          int targetX = nbttagcompound.getInteger("stmn.tX");
          int targetY = nbttagcompound.getInteger("stmn.tY");
          int targetZ = nbttagcompound.getInteger("stmn.tZ");
          if (player.getDistance(targetX + 0.5D, targetY + 0.5D, targetZ + 0.5D) <= 64.0D) {
            player.openGui(SMModContainer.instance, SMModContainer.guiStarCoreId, world, targetX, targetY, targetZ);
          }
        } 
      }
    } 
    
    return itemStack;
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {
    this.starMarkIcon = par1IconRegister.registerIcon("starminer:starmark");
    super.registerIcons(par1IconRegister);
  }
}
