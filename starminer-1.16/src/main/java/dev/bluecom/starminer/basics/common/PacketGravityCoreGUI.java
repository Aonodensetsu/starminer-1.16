package dev.bluecom.starminer.basics.common;

import java.util.function.Supplier;

import dev.bluecom.starminer.basics.tileentity.TileEntityGravityCore;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketGravityCoreGUI {
	private BlockPos coord;
	private int gravRad;
	private int starRad;
	
	public PacketGravityCoreGUI(PacketBuffer buf) {
		this.coord = buf.readBlockPos();
		this.gravRad = buf.readInt();
		this.starRad = buf.readInt();
	}
	
	public PacketGravityCoreGUI(BlockPos coord, int grav, int star) {
		this.coord = coord;
		this.gravRad = grav;
		this.starRad = star;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeBlockPos(coord);
		buf.writeInt(gravRad);
		buf.writeInt(starRad);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TileEntity tile = ctx.get().getSender().getLevel().getBlockEntity(coord);
			if (tile instanceof TileEntityGravityCore) {
				TileEntityGravityCore tile2 = (TileEntityGravityCore) tile; 
				tile2.changeRadius(gravRad, starRad);
				BlockState block = tile2.getBlockState();
				ctx.get().getSender().getLevel().sendBlockUpdated(coord, block, block, 2);
				tile2.setChanged();
			}
		});
		return true;
	}
}
