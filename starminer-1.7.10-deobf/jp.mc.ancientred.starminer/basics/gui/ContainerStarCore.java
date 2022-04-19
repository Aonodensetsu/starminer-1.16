package jp.mc.ancientred.starminer.basics.gui;

import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ContainerStarCore
  extends Container
{
  public TileEntityGravityGenerator tileEntityGrav;
  public EntityPlayer entityplayer;
  private World world;
  private int posX;
  private int posY;
  private int posZ;
  
  public ContainerStarCore(EntityPlayer par1Player, TileEntityGravityGenerator par2IInventoryStarCore) {
    this.entityplayer = par1Player;
    InventoryPlayer par1InventoryPlayer = par1Player.inventory;
    this.tileEntityGrav = par2IInventoryStarCore;
    this.world = par2IInventoryStarCore.getWorld();
    this.posX = par2IInventoryStarCore.xCoord;
    this.posY = par2IInventoryStarCore.yCoord;
    this.posZ = par2IInventoryStarCore.zCoord;
    
    int i = 37;
    
    int j;
    
    for (j = 0; j < 3; j++) {
      
      for (int k = 0; k < 9; k++)
      {
        addSlotToContainer(new Slot((IInventory)par2IInventoryStarCore, k + j * 9, 8 + k * 18, 72 + j * 18));
      }
    } 
    
    addSlotToContainer(new Slot((IInventory)par2IInventoryStarCore, 27, 142, 36));

    
    for (j = 0; j < 3; j++) {
      
      for (int k = 0; k < 9; k++)
      {
        addSlotToContainer(new Slot((IInventory)par1InventoryPlayer, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
      }
    } 
    
    for (j = 0; j < 9; j++)
    {
      addSlotToContainer(new Slot((IInventory)par1InventoryPlayer, j, 8 + j * 18, 161 + i));
    }
  }
  
  public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
    if (this.world.getBlock(this.posX, this.posY, this.posZ) != SMModContainer.GravityCoreBlock)
    {
      return false;
    }
    return (par1EntityPlayer.getDistance(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) < 64.0D);
  }

  public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
    return super.slotClick(par1, par2, par3, par4EntityPlayer);
  }

  public void onContainerClosed(EntityPlayer par1EntityPlayer) {
    super.onContainerClosed(par1EntityPlayer);
  }

  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    ItemStack itemstack = null;
    Slot slot = this.inventorySlots.get(par2);
    
    if (slot != null && slot.getHasStack()) {
      
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (par2 < 28) {
        
        if (!mergeItemStack(itemstack1, 54, this.inventorySlots.size(), true))
        {
          return null;
        }
      }
      else if (!mergeItemStack(itemstack1, 0, 27, true)) {
        
        return null;
      } 
      
      if (itemstack1.stackSize == 0) {
        
        slot.putStack((ItemStack)null);
      }
      else {
        
        slot.onSlotChanged();
      } 
    } 
    
    return itemstack;
  }
  public void receiveButtonAction(int ID) {
    if (!this.world.isRemote) {
      
      TileEntity te = this.world.getTileEntity(this.posX, this.posY, this.posZ);
      if (te == null)
        return;  if (this.tileEntityGrav == null)
        return;  if (te != this.tileEntityGrav)
        return;  if (!(te instanceof TileEntityGravityGenerator))
        return; 
      boolean resetWorkState = false;
      switch (ID) {
        case 10:
          this.tileEntityGrav.useBufferArea = !this.tileEntityGrav.useBufferArea;
          break;
        case 1:
          this.tileEntityGrav.gravityRange++;
          break;
        case 2:
          this.tileEntityGrav.gravityRange--;
          break;
        case 3:
          this.tileEntityGrav.gravityRange += 5.0D;
          break;
        case 4:
          this.tileEntityGrav.gravityRange -= 5.0D;
          break;
        case 5:
          this.tileEntityGrav.starRad++;
          resetWorkState = true;
          break;
        case 6:
          this.tileEntityGrav.starRad--;
          resetWorkState = true;
          break;
        case 7:
          this.tileEntityGrav.starRad += 5.0D;
          resetWorkState = true;
          break;
        case 8:
          this.tileEntityGrav.starRad -= 5.0D;
          resetWorkState = true;
          break;
        case 9:
          this.tileEntityGrav.type++;
          this.tileEntityGrav.type %= 5;
          resetWorkState = true;
          break;
      } 
      
      this.tileEntityGrav.fixInValidRange();
      
      if (resetWorkState)
      {
        this.tileEntityGrav.resetWorkState();
      }
      
      this.world.markBlockForUpdate(this.posX, this.posY, this.posZ);
    } 
  }
}
