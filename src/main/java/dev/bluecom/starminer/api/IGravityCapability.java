package dev.bluecom.starminer.api;

import javax.annotation.Nonnull;

import dev.bluecom.starminer.basics.common.PacketGravityCapability;
import net.minecraft.util.math.BlockPos;

public interface IGravityCapability {
	public void tick();
	public int getTicks();
	public void setTicks(int ticks);
	public void changeTicks(int ticks);
	public GravityDirection getGravityDir();
	public void setGravityDir(@Nonnull GravityDirection gravity);
	public boolean getGravityZero();
	public void setGravityZero(boolean zero);
	public boolean getGravityInverted();
	public void setGravityInverted(boolean inverted);
	public boolean getAttracted();
	public void setAttracted(boolean attracted);
	public BlockPos getAttractedPos();
	public void setAttractedPos(BlockPos position);
	public boolean getAttractedBy(IAttractableTileEntity entity);
	public void setAttractedBy(IAttractableTileEntity entity);
	public void loseAttractedBy();
	public void updateClients();
	public PacketGravityCapability getPacket();
}
