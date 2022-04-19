package jp.mc.ancientred.starminer.basics.item;

import jp.mc.ancientred.starminer.api.Gravity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemSolarWindFan extends Item {
  private static final String STMN_INSTANTFLAG = "stmn;instantflag";
  
  public ItemSolarWindFan() {
    setTextureName("starminer:uchiwa");
    setMaxDurability(500);
    setMaxStackSize(1);
  }
  
  public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack itemStack) {
    if (!entityLiving.isSneaking() && Gravity.isEntityZeroGravity((Entity)entityLiving)) {
      Vec3 look = entityLiving.getLookVec();
      
      NBTTagCompound nbttagcompound = itemStack.getTagCompound();
      if (nbttagcompound == null) {
        itemStack.setTagCompound(nbttagcompound = new NBTTagCompound());
      }
      
      if (nbttagcompound.getBoolean("stmn;instantflag")) {
        double div = 3.0D;
        entityLiving.motionX -= entityLiving.motionX / div;
        if (Math.abs(entityLiving.motionX) < 0.03D) {
          entityLiving.motionX = 0.0D;
        }
        entityLiving.motionY -= entityLiving.motionY / div;
        if (Math.abs(entityLiving.motionY) < 0.03D) {
          entityLiving.motionY = 0.0D;
        }
        entityLiving.motionZ -= entityLiving.motionZ / div;
        if (Math.abs(entityLiving.motionZ) < 0.03D) {
          entityLiving.motionZ = 0.0D;
        }
      } else {
        double speed = 0.06D;
        double min = -1.25D;
        double max = 1.25D;
        if ((look.xCoord > 0.0D && max >= entityLiving.motionX) || (look.xCoord < 0.0D && min <= entityLiving.motionX))
        {
          entityLiving.motionX += look.xCoord * speed;
        }
        if ((look.yCoord > 0.0D && max >= entityLiving.motionY) || (look.yCoord < 0.0D && min <= entityLiving.motionY))
        {
          entityLiving.motionY += look.yCoord * speed;
        }
        if ((look.zCoord > 0.0D && max >= entityLiving.motionZ) || (look.zCoord < 0.0D && min <= entityLiving.motionZ))
        {
          entityLiving.motionZ += look.zCoord * speed;
        }
      } 
      
      itemStack.damageItem(1, entityLiving);
    } 
    
    return false;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    NBTTagCompound nbttagcompound = itemStack.getTagCompound();
    if (nbttagcompound == null) {
      itemStack.setTagCompound(nbttagcompound = new NBTTagCompound());
    }
    
    nbttagcompound.setBoolean("stmn;instantflag", true);
    player.swingItem();
    nbttagcompound.setBoolean("stmn;instantflag", false);
    nbttagcompound.removeTag("stmn;instantflag");
    
    return itemStack;
  }
}
