package dev.bluecom.starminer.basics.tileentity;

import javax.annotation.Nonnull;
import dev.bluecom.starminer.basics.common.CommonRegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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

	private int gravRad = 0;
	private int starRad = 0;
	
	public TileEntityGravityCore() {
		super(CommonRegistryHandler.TILE_GRAVITY_CORE.get());
	}
	
	public int getGravityRadius() {
		return gravRad;
	}
	
	public void setGravityRadius(int grad) {
		gravRad = grad;
		if (gravRad < 0) {
			gravRad = 0;
		}
		if (gravRad > 32) {
			gravRad = 32;
		}
	}
	
	public void changeRadius(int g, int s) {
		setGravityRadius(gravRad+g);
		setStarRadius(starRad+s);
	}
	
	public int getStarRadius() {
		return starRad;
	}
	
	public void setStarRadius(int srad) {
		starRad = srad;
		if (starRad < 0) {
			starRad = 0;
		}
		if (starRad > 32) {
			starRad = 32;
		}
	}
	
	public boolean canPlayerAccessInventory(PlayerEntity player) {
		if (level.getBlockEntity(worldPosition) != this) { return false; }
		return player.blockPosition().distSqr(new Vector3i(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5)) < 64;
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		handler.invalidate();
	}
	
	@Override
	public void tick() {
		return;
	}
	
	@Override
	public void load(BlockState block, CompoundNBT nbt) {
		itemHandler.deserializeNBT(nbt.getCompound("inv"));
		gravRad = nbt.getInt("gravRad");
		starRad = nbt.getInt("starRad");
		super.load(block, nbt);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("inv", itemHandler.serializeNBT());
		nbt.putInt("gravRad", gravRad);
		nbt.putInt("starRad", starRad);
		return super.save(nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT nbt = pkt.getTag();
		gravRad = nbt.getInt("gravRad");
		starRad = nbt.getInt("starRad");
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("gravRad", gravRad);
		nbt.putInt("starRad", starRad);
		return new SUpdateTileEntityPacket(this.getBlockPos(), -1, nbt);
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = super.getUpdateTag();
		nbt.putInt("gravRad", gravRad);
		nbt.putInt("starRad", starRad);
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		gravRad = tag.getInt("gravRad");
		starRad = tag.getInt("starRad");
		super.handleUpdateTag(state, tag);
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
