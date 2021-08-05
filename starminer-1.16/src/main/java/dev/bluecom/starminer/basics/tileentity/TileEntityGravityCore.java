package dev.bluecom.starminer.basics.tileentity;

import javax.annotation.Nonnull;
import dev.bluecom.starminer.basics.common.CommonRegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGravityCore extends TileEntity implements ITickableTileEntity {
	private ItemStackHandler itemHandler = new ItemStackHandler(27) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};
	private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

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
	public void tick() {
		//if (this.level.isClientSide) { return; }
		return;
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
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}
}
