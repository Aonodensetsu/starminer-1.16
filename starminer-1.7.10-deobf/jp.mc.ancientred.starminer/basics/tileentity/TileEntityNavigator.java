package jp.mc.ancientred.starminer.basics.tileentity;

import java.util.Iterator;
import java.util.List;
import jp.mc.ancientred.starminer.basics.block.BlockNavigator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileEntityNavigator
  extends TileEntity
{
  private static final String LX_NBT_KEY = "stmn.lx";
  private static final String LY_NBT_KEY = "stmn.ly";
  private static final String LZ_NBT_KEY = "stmn.lz";
  private static final String ACT_NBT_KEY = "stmn.actvc";
  public int activeTickCount = 0;
  
  public float lookX = 0.0F;
  public float lookY = 0.0F;
  public float lookZ = 0.0F;

  public void activate() {
    this.activeTickCount = 100;
  }
  
  public boolean isActive() {
    return (this.activeTickCount > 0);
  }

  public void updateEntity() {
    if (!this.worldObj.isRemote) {
      if (this.activeTickCount > 0)
      {
        updateAngleByNearPlayer();

        this.activeTickCount--;
        if (this.activeTickCount <= 0 || this.worldObj.getTotalWorldTime() % 10L == 0L)
        {
          this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
      }
    
    } else if (this.activeTickCount > 0) {
      
      this.activeTickCount--;

      
      this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
    } 
  }

  private void updateAngleByNearPlayer() {
    AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)).expand(2.0D, 2.0D, 2.0D);

    axisalignedbb.maxY = this.worldObj.getHeight();
    List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
    Iterator<EntityPlayer> iterator = list.iterator();
    
    while (iterator.hasNext()) {
      EntityPlayer entityPlayer = iterator.next();

      if (entityPlayer.isPlayerSleeping()) {
        continue;
      }
      if (!BlockNavigator.doesPlayerHasTorchOnHand(entityPlayer))
        continue; 
      Vec3 vec3 = entityPlayer.getLook(1.0F);
      this.lookX = (float)vec3.xCoord;
      this.lookY = (float)vec3.yCoord;
      this.lookZ = (float)vec3.zCoord;
    } 
  }

  public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    super.readFromNBT(par1NBTTagCompound);
    this.lookX = par1NBTTagCompound.getFloat("stmn.lx");
    this.lookY = par1NBTTagCompound.getFloat("stmn.ly");
    this.lookZ = par1NBTTagCompound.getFloat("stmn.lz");
    this.activeTickCount = par1NBTTagCompound.getInteger("stmn.actvc");
  }

  public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    super.writeToNBT(par1NBTTagCompound);
    par1NBTTagCompound.setFloat("stmn.lx", this.lookX);
    par1NBTTagCompound.setFloat("stmn.ly", this.lookY);
    par1NBTTagCompound.setFloat("stmn.lz", this.lookZ);
    par1NBTTagCompound.setInteger("stmn.actvc", this.activeTickCount);
  }
  
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    NBTTagCompound tag = pkt.getNbtCompound();
    readFromNBT(tag);
  }
  
  public Packet getDescriptionPacket() {
    NBTTagCompound tag = new NBTTagCompound();
    writeToNBT(tag);
    return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
  }
}
