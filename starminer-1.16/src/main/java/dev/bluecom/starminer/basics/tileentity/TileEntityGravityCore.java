package dev.bluecom.starminer.basics.tileentity;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import dev.bluecom.starminer.basics.common.CommonRegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGravityCore extends TileEntity {
	private ItemStackHandler itemHandler = new ItemStackHandler(27) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};
	private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

	public static Supplier<TileEntityType<TileEntityGravityCore>> builder() {
		return () -> TileEntityType.Builder.of(TileEntityGravityCore::new, CommonRegistryHandler.BLOCK_GRAVITY_CORE.get()).build(null);
	}

	public TileEntityGravityCore() {
		super(CommonRegistryHandler.TILE_GRAVITY_CORE.get());
	}

	public boolean canPlayerAccessInventory(PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) { return false; }
		return player.blockPosition().distSqr(new Vector3i(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5)) < 64;
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		handler.invalidate();
	}
	
	
	@Override
	public void load(BlockState block, CompoundNBT nbt) {
		itemHandler.deserializeNBT(nbt.getCompound("inv"));
		super.load(block, nbt);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("inv", itemHandler.serializeNBT());
		return super.save(nbt);
	}
	
	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return new SUpdateTileEntityPacket(this.worldPosition, 42, nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		BlockState block = level.getBlockState(this.worldPosition);
		load(block, pkt.getTag());
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
		save(nbt);
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(BlockState block, CompoundNBT tag) {
		load(block, tag);
	}
}
