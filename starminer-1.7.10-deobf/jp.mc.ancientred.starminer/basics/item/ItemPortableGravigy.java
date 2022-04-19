package jp.mc.ancientred.starminer.basics.item;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;

public class ItemPortableGravigy
  extends Item
{
  private static final double G_EXPAND = 16.0D;
  public static final String ENABLEPGRV_NBT_TAG = "stmn.p_grav";
  private IIcon[] itemIconPrivate = new IIcon[2];
  
  public ItemPortableGravigy() {
    setTextureName("dummy");
    setMaxStackSize(1);
  }

  public void onUpdate(ItemStack itemStack, World world, Entity entity, int mainInvSlotNum, boolean isCurrentItem) {
    if (entity instanceof EntityPlayer && itemStack.hasTagCompound()) {
      EntityPlayer entityPlayer = (EntityPlayer)entity;
      NBTTagCompound tag = itemStack.getTagCompound();
      if (tag.getBoolean("stmn.p_grav")) {
        ItemStack[] mainInv = entityPlayer.inventory.mainInventory;
        for (int len = mainInv.length, i = 0; i < len && i < mainInvSlotNum; i++) {
          if (isItemEnabledPortableG(mainInv[i])) {
            return;
          }
        } 

        AxisAlignedBB bb = entityPlayer.boundingBox;
        double centerGravityX = (bb.maxX + bb.minX) / 2.0D;
        double centerGravityY = (bb.maxY + bb.minY) / 2.0D;
        double centerGravityZ = (bb.maxZ + bb.minZ) / 2.0D;
        
        pullEntityItemInRange(world, centerGravityX, centerGravityY, centerGravityZ);
        pullExpOrbsInRange(world, centerGravityX, centerGravityY, centerGravityZ);
      } 
    } 
  }
  
  private final boolean isItemEnabledPortableG(ItemStack itemStack) {
    if (itemStack == null || itemStack.getItem() != this) return false; 
    return (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("stmn.p_grav"));
  }
  
  private void pullEntityItemInRange(World world, double centerGravityX, double centerGravityY, double centerGravityZ) {
    List list = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(centerGravityX - 0.5D, centerGravityY - 0.5D, centerGravityZ - 0.5D, centerGravityX + 0.5D, centerGravityY + 0.5D, centerGravityZ + 0.5D).expand(16.0D, 16.0D, 16.0D));
    
    Iterator<EntityItem> iterator = list.iterator();
    
    while (iterator.hasNext()) {
      EntityItem entityItem = iterator.next();
      if (isItemEnabledPortableG(entityItem.getEntityItem()))
        continue; 
      entityItem.motionX = (centerGravityX - entityItem.posX) / 16.0D;
      entityItem.motionY = (centerGravityY - entityItem.posY) / 16.0D;
      entityItem.motionZ = (centerGravityZ - entityItem.posZ) / 16.0D;
    } 
  }
  
  private void pullExpOrbsInRange(World world, double centerGravityX, double centerGravityY, double centerGravityZ) {
    List list = world.getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.getBoundingBox(centerGravityX - 0.5D, centerGravityY - 0.5D, centerGravityZ - 0.5D, centerGravityX + 0.5D, centerGravityY + 0.5D, centerGravityZ + 0.5D).expand(16.0D, 16.0D, 16.0D));
    
    Iterator<EntityXPOrb> iterator = list.iterator();
    
    while (iterator.hasNext()) {
      EntityXPOrb entityXPOrb = iterator.next();
      entityXPOrb.motionX = (centerGravityX - entityXPOrb.posX) / 16.0D;
      entityXPOrb.motionY = (centerGravityY - entityXPOrb.posY) / 16.0D;
      entityXPOrb.motionZ = (centerGravityZ - entityXPOrb.posZ) / 16.0D;
    } 
  }

  public boolean onEntityItemUpdate(EntityItem entityItem) {
    if (isItemEnabledPortableG(entityItem.getEntityItem())) {
      
      entityItem.onEntityUpdate();
      
      if (entityItem.delayBeforeCanPickup > 0)
      {
        entityItem.delayBeforeCanPickup--;
      }
      
      entityItem.prevPosX = entityItem.posX;
      entityItem.prevPosY = entityItem.posY;
      entityItem.prevPosZ = entityItem.posZ;
      
      entityItem.moveEntity(entityItem.motionX, entityItem.motionY, entityItem.motionZ);
      boolean flag = ((int)entityItem.prevPosX != (int)entityItem.posX || (int)entityItem.prevPosY != (int)entityItem.posY || (int)entityItem.prevPosZ != (int)entityItem.posZ);
      
      if (flag || entityItem.ticksExisted % 25 == 0)
      {
        if (entityItem.worldObj.getBlock(MathHelper.floor_double(entityItem.posX), MathHelper.floor_double(entityItem.posY), MathHelper.floor_double(entityItem.posZ)).getMaterial() == Material.lava) {
          
          entityItem.motionY = 0.20000000298023224D;
          entityItem.motionX = ((entityItem.worldObj.rand.nextFloat() - entityItem.worldObj.rand.nextFloat()) * 0.2F);
          entityItem.motionZ = ((entityItem.worldObj.rand.nextFloat() - entityItem.worldObj.rand.nextFloat()) * 0.2F);
          entityItem.playSound("random.fizz", 0.4F, 2.0F + entityItem.worldObj.rand.nextFloat() * 0.4F);
        } 
      }
      
      AxisAlignedBB bb = entityItem.boundingBox;
      double centerGravityX = (bb.maxX + bb.minX) / 2.0D;
      double centerGravityY = (bb.maxY + bb.minY) / 2.0D + 1.0D;
      double centerGravityZ = (bb.maxZ + bb.minZ) / 2.0D;
      pullEntityItemInRange(entityItem.worldObj, centerGravityX, centerGravityY, centerGravityZ);
      pullExpOrbsInRange(entityItem.worldObj, centerGravityX, centerGravityY, centerGravityZ);

      entityItem.motionX *= 0.7D;
      entityItem.motionY *= 0.7D;
      entityItem.motionZ *= 0.7D;
      
      if (entityItem.isCollided) {
        
        entityItem.motionX *= 0.5D;
        entityItem.motionY *= 0.5D;
        entityItem.motionZ *= 0.5D;
      } 
      
      entityItem.age++;
      
      ItemStack item = entityItem.getEntityItem();
      
      if (!entityItem.worldObj.isRemote && entityItem.age >= entityItem.lifespan)
      {
        if (item != null) {
          
          ItemExpireEvent event = new ItemExpireEvent(entityItem, (item.getItem() == null) ? 6000 : item.getItem().getEntityLifespan(item, entityItem.worldObj));
          if (MinecraftForge.EVENT_BUS.post((Event)event))
          {
            entityItem.lifespan += event.extraLife;
          }
          else
          {
            entityItem.setDead();
          }
        
        } else {
          
          entityItem.setDead();
        } 
      }
      
      if (item != null && item.stackSize <= 0)
      {
        entityItem.setDead();
      }
      
      return true;
    } 
    return false;
  }

  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    boolean gcon;
    NBTTagCompound nbttagcompound = itemStack.getTagCompound();
    
    if (nbttagcompound == null) {
      
      nbttagcompound = new NBTTagCompound();
      itemStack.setTagCompound(nbttagcompound);
      gcon = false;
    } else {
      gcon = nbttagcompound.getBoolean("stmn.p_grav");
    } 
    
    nbttagcompound.setBoolean("stmn.p_grav", !gcon);
    
    return itemStack;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(ItemStack itemStack, int pass) {
    if (itemStack.hasTagCompound()) {
      NBTTagCompound tag = itemStack.getTagCompound();
      boolean gcon = tag.getBoolean("stmn.p_grav");
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
      boolean gcon = tag.getBoolean("stmn.p_grav");
      return gcon ? this.itemIconPrivate[1] : this.itemIconPrivate[0];
    } 
    return this.itemIconPrivate[0];
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {
    this.itemIconPrivate[0] = par1IconRegister.registerIcon("starminer:portablegravity_off");
    this.itemIconPrivate[1] = par1IconRegister.registerIcon("starminer:portablegravity_on");
  }
}