package dev.bluecom.starminer.basics.block;

import dev.bluecom.starminer.basics.tileentity.ContainerGravityCore;
import dev.bluecom.starminer.basics.tileentity.TileEntityGravityCore;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockGravityCore extends Block {
	public BlockGravityCore() {
		super(AbstractBlock.Properties.of(Material.STONE).noOcclusion().strength(1));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityGravityCore();
	}
	
	@Override
	public ActionResultType use(BlockState block, World world, BlockPos coord, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
		if (!world.isClientSide) {
			TileEntity tileEntity = world.getBlockEntity(coord);
			if (tileEntity instanceof TileEntityGravityCore) {
				INamedContainerProvider containerProvider = new INamedContainerProvider() {
					@Override
					public ITextComponent getDisplayName() {
						return new TranslationTextComponent("screen.starminer.gravity_core");
					}
					
					@Override
					public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
						return new ContainerGravityCore(id, world, coord, playerInventory, player);
					}
				};
				NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
			} else {
				throw new IllegalStateException("Named container provider is missing!");
			}
		}
		return ActionResultType.SUCCESS;
	}
}
