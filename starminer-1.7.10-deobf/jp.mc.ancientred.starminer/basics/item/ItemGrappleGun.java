package jp.mc.ancientred.starminer.basics.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.entity.EntityGProjectile;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemGrappleGun
  extends Item
{
  public static final String G_RAPPLE_NBT_TAG = "stmn.g_rapple";
  private ItemStack itemStackTemp;
  private boolean noMessageFlg;
  
  public ItemGrappleGun() {
    setMaxStackSize(1);
    setMaxDurability(512);
    setTextureName("starminer:g-rapplegun_arrow");
  }

  public ItemStack onItemRightClick(ItemStack parItemStack, World parWorld, EntityPlayer parEntityPlayer) {
    if (parEntityPlayer.inventory.hasItem(SMModContainer.GHookItem)) {
      boolean infinity = (parEntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, parItemStack) > 0);
      
      parWorld.playSoundAtEntity((Entity)parEntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

      EntityGProjectile entityGProjectile = new EntityGProjectile(parWorld, parEntityPlayer, 2.5F, EntityGProjectile.GProjectileType.gRappleHook);
      
      parItemStack.damageItem(1, (EntityLivingBase)parEntityPlayer);
      
      if (!infinity)
      {
        parEntityPlayer.inventory.consumeInventoryItem(SMModContainer.GHookItem);
      }
      
      if (!parWorld.isRemote) {
        parWorld.spawnEntityInWorld((Entity)entityGProjectile);
        
        NBTTagCompound nbttagcompound = parItemStack.getTagCompound();
        if (nbttagcompound == null) {
          nbttagcompound = new NBTTagCompound();
          parItemStack.setTagCompound(nbttagcompound);
        } 

        if (nbttagcompound.hasKey("stmn.g_rapple")) {
          Entity oldGrapple = parWorld.getEntityByID(nbttagcompound.getInteger("stmn.g_rapple"));
          if (oldGrapple != null) {
            oldGrapple.setDead();
          }
        } 

        nbttagcompound.setInteger("stmn.g_rapple", entityGProjectile.getEntityId());
      } else {
        
        this.noMessageFlg = true;
        this.itemStackTemp = parItemStack;

        
        SMModContainer.proxy.showGrappleGunGuideMessage();
      } 
    } 
    
    return parItemStack;
  }
  
  public int getItemEnchantability() {
    return 0;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldRotateAroundWhenRendering() {
    return false;
  }

  public String getUnlocalizedNameInefficiently(ItemStack parItemStack) {
    if (this.itemStackTemp != parItemStack && 
      this.noMessageFlg) {
      this.noMessageFlg = false;
      SMModContainer.proxy.setRemainingHighlightTicksOFF();
    } 
    
    return super.getUnlocalizedNameInefficiently(parItemStack);
  }
}
