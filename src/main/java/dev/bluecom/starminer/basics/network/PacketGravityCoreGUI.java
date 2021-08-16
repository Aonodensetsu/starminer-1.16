package dev.bluecom.starminer.basics.network;

import java.util.function.Supplier;

import dev.bluecom.starminer.basics.tileentities.TileEntityGravityCore;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketGravityCoreGUI {
	private final BlockPos coord;
	private final int gravRad;
	private final int starRad;
	private final boolean message;
	private final boolean inversion;
	
	public PacketGravityCoreGUI(BlockPos coord, int grav, int star, boolean msg, boolean inv) {
		this.coord = coord;
		this.gravRad = grav;
		this.starRad = star;
		this.message = msg;
		this.inversion = inv;
	}
	
	public PacketGravityCoreGUI(PacketBuffer buf) {
		this.coord = buf.readBlockPos();
		this.gravRad = buf.readInt();
		this.starRad = buf.readInt();
		this.message = buf.readBoolean();
		this.inversion = buf.readBoolean();
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeBlockPos(coord);
		buf.writeInt(gravRad);
		buf.writeInt(starRad);
		buf.writeBoolean(message);
		buf.writeBoolean(inversion);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TileEntity tile = ctx.get().getSender().getLevel().getBlockEntity(coord);
			if (tile instanceof TileEntityGravityCore) {
				TileEntityGravityCore tile2 = (TileEntityGravityCore) tile; 
				tile2.changeRadius(gravRad, starRad);
				if (message) {
					tile2.nextGravityType();
				}
				if (inversion) {
					tile2.nextInvType();
				}
				BlockState block = tile2.getBlockState();
				ctx.get().getSender().getLevel().sendBlockUpdated(coord, block, block, 2);
				tile2.setChanged();
			}
		});
		return true;
	}
}
