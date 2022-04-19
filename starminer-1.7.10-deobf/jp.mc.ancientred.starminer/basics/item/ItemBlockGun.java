package jp.mc.ancientred.starminer.basics.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import jp.mc.ancientred.starminer.api.Gravity;
import jp.mc.ancientred.starminer.basics.entity.EntityFallingBlockEx;
import jp.mc.ancientred.starminer.basics.tileentity.AllowedBlockDictionary;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemBlockGun
  extends Item
{
  private String[] SUB_NAMES = new String[] { "short", "normal", "long", "spread" };
  private double[] SUB_EXCEED_RANGES = new double[] { 2.0D, 4.0D, 6.0D, 0.0D };
  private IIcon[] itemIconPrivate = new IIcon[4];

  public ItemBlockGun() {
    setMaxStackSize(1);
    setMaxDurability(0);
    setHasSubtypes(true);
    setTextureName("starminer:blockgun_long");
  }

  public ItemStack onItemRightClick(ItemStack parItemStack, World parWorld, EntityPlayer parEntityPlayer) {
    Gravity gravity = Gravity.getGravityProp((Entity)parEntityPlayer);
    if (gravity == null) return parItemStack;
    
    boolean infinity = parEntityPlayer.capabilities.isCreativeMode;
    
    int damageOfItem = parItemStack.getMetadata();
    if (damageOfItem < 0 || damageOfItem >= this.SUB_NAMES.length)
    {
      damageOfItem = 0;
    }
    
    boolean doSpread = (damageOfItem == 3);
    int spreadBlockCount = doSpread ? 5 : 1;
    
    ItemStack[] mainInv = parEntityPlayer.inventory.mainInventory;
    
    for (int i = 0, count = 0; i < mainInv.length; i++) {
      Item item; if (mainInv[i] != null && (item = mainInv[i].getItem()) != null) {
        Block block = Block.getBlockFromItem(item);
        if (block != null && block != Blocks.air && AllowedBlockDictionary.isAllowed(block))
        {

          while (count++ < spreadBlockCount) {
            if (count == 1) {
              parWorld.playSoundAtEntity((Entity)parEntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            }

            
            EntityFallingBlockEx blockEntity = new EntityFallingBlockEx(parWorld, parEntityPlayer.posX, parEntityPlayer.posY, parEntityPlayer.posZ, block, mainInv[i].getMetadata());
            gravity.setGravityFixedPlayerShootVec(parEntityPlayer, (Entity)blockEntity, 1.0F);
            
            blockEntity.setExceedRange(this.SUB_EXCEED_RANGES[damageOfItem]);
            
            if (doSpread) {
              this; this; blockEntity.motionX += itemRand.nextDouble() * 0.8D - itemRand.nextDouble() * 0.8D;
              this; this; blockEntity.motionY += itemRand.nextDouble() * 0.8D - itemRand.nextDouble() * 0.8D;
              this; this; blockEntity.motionZ += itemRand.nextDouble() * 0.8D - itemRand.nextDouble() * 0.8D;
            } 
            
            blockEntity.prevPosX = blockEntity.spawnPosX = blockEntity.posX;
            blockEntity.prevPosY = blockEntity.spawnPosY = blockEntity.posY;
            blockEntity.prevPosZ = blockEntity.spawnPosZ = blockEntity.posZ;
            
            if (!infinity)
            {
              parEntityPlayer.inventory.consumeInventoryItem(item);
            }
            
            if (!parWorld.isRemote) {
              parWorld.spawnEntityInWorld((Entity)blockEntity);
            }
            
            if (mainInv[i] == null || (mainInv[i]).stackSize == 0) return parItemStack;
          
          } 
        }
      } 
    } 

    return parItemStack;
  }
  
  public String getUnlocalizedName(ItemStack itemStack) {
    int i = itemStack.getMetadata();
    
    if (i < 0 || i >= this.SUB_NAMES.length)
    {
      i = 0;
    }
    
    return getUnlocalizedName() + "." + this.SUB_NAMES[i];
  }
  
  public int getItemEnchantability() {
    return 0;
  }

  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
    for (int i = 0; i < this.SUB_NAMES.length; i++)
    {
      list.add(new ItemStack(item, 1, i));
    }
  }
  
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldRotateAroundWhenRendering() {
    return false;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int damage) {
    return this.itemIconPrivate[damage];
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister) {
    this.itemIconPrivate[0] = par1IconRegister.registerIcon("starminer:blockgun_short");
    this.itemIconPrivate[1] = par1IconRegister.registerIcon("starminer:blockgun_normal");
    this.itemIconPrivate[2] = par1IconRegister.registerIcon("starminer:blockgun_long");
    this.itemIconPrivate[3] = par1IconRegister.registerIcon("starminer:blockgun_spread");
  }
}
