package dev.bluecom.starminer.basics.tileentity;

import dev.bluecom.starminer.basics.common.CommonRegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerGravityCore extends Container {
	private TileEntity tileEntity;
	@SuppressWarnings("unused")
	private PlayerEntity playerEntity;
	private IItemHandler playerInventory;
	
	public ContainerGravityCore(int id, World world, BlockPos coord, PlayerInventory playerInventory, PlayerEntity player) {
		super(CommonRegistryHandler.CONTAINER_GRAVITY_CORE.get(), id);
		tileEntity = world.getBlockEntity(coord);
		this.playerEntity = player;
		this.playerInventory = new InvWrapper(playerInventory);
		if (tileEntity != null) { tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> { addSlotBox(h, 36, 8, 72, 9, 18, 3, 18); }); }
		layoutPlayerInventorySlots(8, 140);
	}
		
	@Override
	public boolean stillValid(PlayerEntity player) {
		return player.blockPosition().distSqr(new Vector3i(this.tileEntity.getBlockPos().getX() + 0.5, this.tileEntity.getBlockPos().getY() + 0.5, this.tileEntity.getBlockPos().getZ() + 0.5)) < 64;
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack = slot.getItem();
			itemstack = stack.copy();
			if (index >= 0 && index < 36) {
				if (!this.moveItemStackTo(stack, 36, 63, true)) {
					return ItemStack.EMPTY;
				}
				slot.setChanged();
			} else {
				if (!this.moveItemStackTo(stack, 0, 36, true)) {
					return ItemStack.EMPTY;
				}
				slot.setChanged();
			}
			if (stack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (stack.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, stack);
		}
		return itemstack;
	}
	
	@Override
	public void removed(PlayerEntity player) {
		super.removed(player);
	}
	
	private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
		for (int i = 0 ; i < amount ; i++) {
			addSlot(new SlotItemHandler(handler, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}
	
	private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
		for (int j = 0 ; j < verAmount ; j++) {
			index = addSlotRange(handler, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}
	
	private void layoutPlayerInventorySlots(int leftCol, int topRow) {
		addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
		topRow += 58;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
	}
}
