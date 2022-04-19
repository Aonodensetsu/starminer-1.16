package jp.mc.ancientred.starminer.basics.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import jp.mc.ancientred.starminer.api.GravityDirection;
import jp.mc.ancientred.starminer.basics.block.BlockChestEx;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityChestEx
  extends TileEntity implements IInventory {
  private ItemStack[] chestContents = new ItemStack[36];
  
  public float lidAngle;
  
  public float prevLidAngle;
  
  public int numPlayersUsing;
  
  private int ticksSinceSync;
  
  private int cachedChestType;
  
  private String customName;
  private static final String __OBFID = "CL_00000346";
  public static int IS_adjacentChestZNeg = 0;
  public static int IS_adjacentChestXPos = 1;
  public static int IS_adjacentChestXNeg = 2;
  public static int IS_adjacentChestZPos = 3;
  public static int IS_adjacentChestNone = 4; public boolean isSubBlock; public int relatedBlockX; public int relatedBlockY; public int relatedBlockZ; private GravityDirection gravityDirection; public int[] conv;
  
  public int getAdjacentChestTo() {
    GravityDirection direOpp = GravityDirection.turnWayForNormal(this.gravityDirection);
    direOpp.rotateXYZAt(this.conv, this.relatedBlockX, this.relatedBlockY, this.relatedBlockZ, this.xCoord, this.yCoord, this.zCoord);
    int xDiff = this.conv[0] - this.xCoord;
    int yDiff = this.conv[1] - this.yCoord;
    int zDiff = this.conv[2] - this.zCoord;
    
    if (xDiff < 0)
      return IS_adjacentChestXNeg; 
    if (xDiff > 0)
      return IS_adjacentChestXPos; 
    if (zDiff < 0)
      return IS_adjacentChestZNeg; 
    if (zDiff > 0) {
      return IS_adjacentChestZPos;
    }
    return IS_adjacentChestNone;
  }

  public int getSizeInventory() {
    return 27;
  }

  public ItemStack getStackInSlot(int p_70301_1_) {
    return this.chestContents[p_70301_1_];
  }

  public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
    if (this.chestContents[p_70298_1_] != null) {

      if ((this.chestContents[p_70298_1_]).stackSize <= p_70298_2_) {
        
        ItemStack itemStack = this.chestContents[p_70298_1_];
        this.chestContents[p_70298_1_] = null;
        markDirty();
        return itemStack;
      } 

      ItemStack itemstack = this.chestContents[p_70298_1_].splitStack(p_70298_2_);
      
      if ((this.chestContents[p_70298_1_]).stackSize == 0)
      {
        this.chestContents[p_70298_1_] = null;
      }
      
      markDirty();
      return itemstack;
    } 

    return null;
  }

  public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
    if (this.chestContents[p_70304_1_] != null) {
      
      ItemStack itemstack = this.chestContents[p_70304_1_];
      this.chestContents[p_70304_1_] = null;
      return itemstack;
    } 

    return null;
  }

  public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
    this.chestContents[p_70299_1_] = p_70299_2_;
    
    if (p_70299_2_ != null && p_70299_2_.stackSize > getInventoryStackLimit())
    {
      p_70299_2_.stackSize = getInventoryStackLimit();
    }
    
    markDirty();
  }

  public String getInventoryName() {
    return isCustomInventoryName() ? this.customName : "container.chest";
  }

  public boolean isCustomInventoryName() {
    return (this.customName != null && this.customName.length() > 0);
  }

  public void setCustomName(String p_145976_1_) {
    this.customName = p_145976_1_;
  }

  public void readFromNBT(NBTTagCompound p_145839_1_) {
    super.readFromNBT(p_145839_1_);
    NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
    this.chestContents = new ItemStack[getSizeInventory()];
    
    if (p_145839_1_.hasKey("CustomName", 8))
    {
      this.customName = p_145839_1_.getString("CustomName");
    }
    
    for (int i = 0; i < nbttaglist.tagCount(); i++) {
      
      NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
      int j = nbttagcompound1.getByte("Slot") & 0xFF;
      
      if (j >= 0 && j < this.chestContents.length)
      {
        this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
      }
    } 
    readOriginalNBTData(p_145839_1_);
  }
  public void readOriginalNBTData(NBTTagCompound p_145839_1_) {
    this.gravityDirection = GravityDirection.values()[p_145839_1_.getInteger("gDir")];
    this.relatedBlockX = p_145839_1_.getInteger("relatedBlockX");
    this.relatedBlockY = p_145839_1_.getInteger("relatedBlockY");
    this.relatedBlockZ = p_145839_1_.getInteger("relatedBlockZ");
    this.isSubBlock = p_145839_1_.getBoolean("isSubBlock");
  }

  public void writeToNBT(NBTTagCompound p_145841_1_) {
    super.writeToNBT(p_145841_1_);
    NBTTagList nbttaglist = new NBTTagList();
    
    for (int i = 0; i < this.chestContents.length; i++) {
      
      if (this.chestContents[i] != null) {
        
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setByte("Slot", (byte)i);
        this.chestContents[i].writeToNBT(nbttagcompound1);
        nbttaglist.appendTag((NBTBase)nbttagcompound1);
      } 
    } 
    
    p_145841_1_.setTag("Items", (NBTBase)nbttaglist);
    
    if (isCustomInventoryName())
    {
      p_145841_1_.setString("CustomName", this.customName);
    }
    
    writeOriginalNBTData(p_145841_1_);
  }
  public void writeOriginalNBTData(NBTTagCompound p_145841_1_) {
    p_145841_1_.setInteger("gDir", this.gravityDirection.ordinal());
    p_145841_1_.setInteger("relatedBlockX", this.relatedBlockX);
    p_145841_1_.setInteger("relatedBlockY", this.relatedBlockY);
    p_145841_1_.setInteger("relatedBlockZ", this.relatedBlockZ);
    p_145841_1_.setBoolean("isSubBlock", this.isSubBlock);
  }

  public int getInventoryStackLimit() {
    return 64;
  }

  public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
    return (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) ? false : ((p_70300_1_.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D));
  }

  public void updateEntity() {
    super.updateEntity();
    
    int adjacentChestInt = getAdjacentChestTo();
    boolean dontPlaySound = (adjacentChestInt == IS_adjacentChestZNeg || adjacentChestInt == IS_adjacentChestXNeg);
    
    this.ticksSinceSync++;

    if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0) {
      
      this.numPlayersUsing = 0;
      float f1 = 5.0F;
      List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((this.xCoord - f1), (this.yCoord - f1), (this.zCoord - f1), ((this.xCoord + 1) + f1), ((this.yCoord + 1) + f1), ((this.zCoord + 1) + f1)));
      Iterator<EntityPlayer> iterator = list.iterator();
      
      while (iterator.hasNext()) {
        
        EntityPlayer entityplayer = iterator.next();
        
        if (entityplayer.openContainer instanceof ContainerChest) {
          
          IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();
          
          if (iinventory == this || (iinventory instanceof InventoryLargeChest && ((InventoryLargeChest)iinventory).isPartOfLargeChest(this)))
          {
            this.numPlayersUsing++;
          }
        } 
      } 
    } 
    
    this.prevLidAngle = this.lidAngle;
    float f = 0.1F;

    if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && !dontPlaySound)
    {
      this.worldObj.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
    }
    
    if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0F) || (this.numPlayersUsing > 0 && this.lidAngle < 1.0F)) {
      
      float f1 = this.lidAngle;
      
      if (this.numPlayersUsing > 0) {
        
        this.lidAngle += f;
      }
      else {
        
        this.lidAngle -= f;
      } 
      
      if (this.lidAngle > 1.0F)
      {
        this.lidAngle = 1.0F;
      }
      
      float f2 = 0.5F;
      
      if (this.lidAngle < f2 && f1 >= f2 && !dontPlaySound)
      {
        this.worldObj.playSoundEffect(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
      }
      
      if (this.lidAngle < 0.0F)
      {
        this.lidAngle = 0.0F;
      }
    } 
  }

  public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
    if (p_145842_1_ == 1) {
      
      this.numPlayersUsing = p_145842_2_;
      return true;
    } 

    return super.receiveClientEvent(p_145842_1_, p_145842_2_);
  }

  public void openChest() {
    if (this.numPlayersUsing < 0)
    {
      this.numPlayersUsing = 0;
    }
    
    this.numPlayersUsing++;
    this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, getBlockType(), 1, this.numPlayersUsing);
    this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, getBlockType());
    this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, getBlockType());
  }

  public void closeChest() {
    if (getBlockType() instanceof BlockChestEx) {
      
      this.numPlayersUsing--;
      this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, getBlockType(), 1, this.numPlayersUsing);
      this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, getBlockType());
      this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, getBlockType());
    } 
  }

  public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
    return true;
  }

  public int getChestType() {
    if (this.cachedChestType == -1) {
      
      if (this.worldObj == null || !(getBlockType() instanceof BlockChestEx))
      {
        return 0;
      }
      
      this.cachedChestType = ((BlockChestEx)getBlockType()).chestType;
    } 
    
    return this.cachedChestType;
  }
  
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    NBTTagCompound tag = pkt.getNbtCompound();
    readOriginalNBTData(tag);
  }
  
  public Packet getDescriptionPacket() {
    NBTTagCompound tag = new NBTTagCompound();
    writeOriginalNBTData(tag);
    return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
  }
  
  public TileEntityChestEx() {
    this.isSubBlock = false;

    this.gravityDirection = GravityDirection.upTOdown_YN;
    this.conv = new int[3]; this.cachedChestType = -1; } @SideOnly(Side.CLIENT) public TileEntityChestEx(int p_i2350_1_) { this.isSubBlock = false; this.gravityDirection = GravityDirection.upTOdown_YN; this.conv = new int[3];
    this.cachedChestType = p_i2350_1_; }
   public GravityDirection getGravityDirection() {
    return this.gravityDirection;
  }
  
  public void setGravityDirection(GravityDirection gDir) {
    this.gravityDirection = gDir;
  }
  
  public boolean hasRelated() {
    return (this.relatedBlockX != this.xCoord || this.relatedBlockY != this.yCoord || this.relatedBlockZ != this.zCoord);
  }
}
