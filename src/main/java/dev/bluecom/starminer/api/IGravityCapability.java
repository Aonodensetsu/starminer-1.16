package dev.bluecom.starminer.api;

import dev.bluecom.starminer.basics.common.PacketGravityCapability;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;

public interface IGravityCapability {
	void tick();
	int getTicks();
	void setTicks(int ticks);
	void changeTicks(int ticks);
	boolean hasTransitionAngle();
	double getTransitionAngle();
	void setTransitionAngle(double angle);
	Vector3d getEyePos();
	void setEyePos(Vector3d vec);
	GravityDirection getGravityDir();
	void setGravityDir(@NotNull GravityDirection gravity);
	GravityDirection getPreviousDir();
	boolean getGravityZero();
	void setGravityZero(boolean zero);
	boolean getGravityInverted();
	void setGravityInverted(boolean inverted);
	boolean getAttracted();
	void setAttracted(boolean attracted);
	BlockPos getAttractedPos();
	void setAttractedPos(BlockPos position);
	boolean getAttractedBy(IAttractableTileEntity entity);
	void setAttractedBy(IAttractableTileEntity entity);
	void loseAttractedBy();
	void updateClients();
	PacketGravityCapability getPacket();
}
