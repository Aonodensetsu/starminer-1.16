package jp.mc.ancientred.starminer.basics.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.mc.ancientred.starminer.api.GravityDirection;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityBlockRotator
  extends TileEntity
{
  private Block storedBlock = Blocks.air;
  private Item storedItem = Items.apple;
  private GravityDirection gravityDirection = GravityDirection.upTOdown_YN;
  
  public boolean isSubBlock = false;
  private int itemMetadata;
  
  public void setStoredBlock(Block block) {
    this.storedBlock = block;
  }
  public int relatedBlockX; public int relatedBlockY; public int relatedBlockZ;
  
  public void setStoredItem(Item item) {
    this.storedItem = item;
  }

  public void setItemMetadata(int meta) {
    this.itemMetadata = meta;
  }

  public Block getStoredBlock() {
    return this.storedBlock;
  }

  public Item getStoredItem() {
    return this.storedItem;
  }

  public int getItemMetadata() {
    return this.itemMetadata;
  }
  
  public void setGravityDirection(GravityDirection gDir) {
    this.gravityDirection = gDir;
  }
  
  public GravityDirection getGravityDirection() {
    return this.gravityDirection;
  }
  
  public boolean hasRelated() {
    return (this.relatedBlockX != this.xCoord || this.relatedBlockY != this.yCoord || this.relatedBlockZ != this.zCoord);
  }
  
  public boolean canUpdate() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return AxisAlignedBB.getBoundingBox((this.xCoord - 1), (this.yCoord - 1), (this.zCoord - 1), (this.xCoord + 2), (this.yCoord + 2), (this.zCoord + 2));
  }
  
  public void readFromNBT(NBTTagCompound p_145839_1_) {
    super.readFromNBT(p_145839_1_);
    this.storedBlock = Block.getBlockById(p_145839_1_.getInteger("blockId"));
    this.storedItem = Item.getItemById(p_145839_1_.getInteger("itemId"));
    this.itemMetadata = p_145839_1_.getInteger("itemMetadata");
    this.gravityDirection = GravityDirection.values()[p_145839_1_.getInteger("gDir")];
    
    this.relatedBlockX = p_145839_1_.getInteger("relatedBlockX");
    this.relatedBlockY = p_145839_1_.getInteger("relatedBlockY");
    this.relatedBlockZ = p_145839_1_.getInteger("relatedBlockZ");
    this.isSubBlock = p_145839_1_.getBoolean("isSubBlock");
  }
  
  public void writeToNBT(NBTTagCompound p_145841_1_) {
    super.writeToNBT(p_145841_1_);
    p_145841_1_.setInteger("blockId", Block.getIdFromBlock(this.storedBlock));
    p_145841_1_.setInteger("itemId", Item.getIdFromItem(this.storedItem));
    p_145841_1_.setInteger("itemMetadata", this.itemMetadata);
    p_145841_1_.setInteger("gDir", this.gravityDirection.ordinal());
    
    p_145841_1_.setInteger("relatedBlockX", this.relatedBlockX);
    p_145841_1_.setInteger("relatedBlockY", this.relatedBlockY);
    p_145841_1_.setInteger("relatedBlockZ", this.relatedBlockZ);
    p_145841_1_.setBoolean("isSubBlock", this.isSubBlock);
  }
  
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    NBTTagCompound tag = pkt.getNbtCompound();
    
    NBTTagCompound tagOld = new NBTTagCompound();
    writeToNBT(tagOld);
    
    readFromNBT(tag);
    
    if (this.storedBlock != null && this.storedBlock.getLightValue() > 1) {
      this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
    }
    if (!tagOld.equals(tag)) {
      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
    }
  }
  
  public Packet getDescriptionPacket() {
    NBTTagCompound tag = new NBTTagCompound();
    writeToNBT(tag);
    return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
  }
}
