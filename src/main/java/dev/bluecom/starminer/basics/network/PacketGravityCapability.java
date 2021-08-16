package dev.bluecom.starminer.basics.network;

import java.util.function.Supplier;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.api.IGravityCapability;
import dev.bluecom.starminer.api.util.GravityDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketGravityCapability {
	private final GravityDirection gravity;
	private final Entity host;
	private final boolean zero;
	private final boolean inverted;
	private final boolean isAttracted;
	private final BlockPos attractedPos;
	private final int ticksLeft;
	
	public PacketGravityCapability(GravityDirection g, Entity h, boolean z, boolean i, boolean a, BlockPos at, int t) {
		this.gravity = g;
		this.host = h;
		this.zero = z;
		this.inverted = i;
		this.isAttracted = a;
		this.attractedPos = at;
		this.ticksLeft = t;
	}
	
	public PacketGravityCapability(PacketBuffer buf) {
		this.gravity = buf.readEnum(GravityDirection.class);
		Minecraft instance = Minecraft.getInstance();
		this.host = instance.level.getEntity(buf.readInt());
		this.zero = buf.readBoolean();
		this.inverted = buf.readBoolean();
		this.isAttracted = buf.readBoolean();
		this.attractedPos = buf.readBlockPos();
		this.ticksLeft = buf.readInt();
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeEnum(gravity);
		buf.writeInt(host.getId());
		buf.writeBoolean(zero);
		buf.writeBoolean(inverted);
		buf.writeBoolean(isAttracted);
		buf.writeBlockPos(attractedPos);
		buf.writeInt(ticksLeft);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			IGravityCapability cap = this.host.getCapability(GravityProvider.GRAVITY).orElse(null);
			if (cap != null) {
				cap.setGravityDir(gravity);
				cap.setGravityZero(zero);
				cap.setGravityInverted(inverted);
				cap.setAttractedPos(attractedPos);
				cap.setAttracted(isAttracted);
				cap.setTicks(ticksLeft);
			}
		});
		return true;
	}
}
