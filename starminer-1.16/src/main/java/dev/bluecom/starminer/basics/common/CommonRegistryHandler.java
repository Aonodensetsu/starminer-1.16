package dev.bluecom.starminer.basics.common;

import java.util.function.Supplier;
import dev.bluecom.starminer.basics.SMModContainer;
import dev.bluecom.starminer.basics.block.BlockGravityCore;
import dev.bluecom.starminer.basics.item.ItemGravityController;
import dev.bluecom.starminer.basics.tileentity.ContainerGravityCore;
import dev.bluecom.starminer.basics.tileentity.TileEntityGravityCore;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonRegistryHandler {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SMModContainer.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SMModContainer.MODID);
	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SMModContainer.MODID);
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SMModContainer.MODID);

	public static void init(IEventBus bus) {
		BLOCKS.register(bus);
		ITEMS.register(bus);
		TILES.register(bus);
		CONTAINERS.register(bus);
	}
	
	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties().tab(STARMINER)));
		return toReturn;
	}
	
	// Item Tab
	public static final ItemGroup STARMINER = new ItemGroup("starminer") {
		@Override
		public ItemStack makeIcon() { return new ItemStack(CommonRegistryHandler.BLOCK_GRAVITY_CORE.get()); }
	};
	
	// Blocks
	public static final RegistryObject<Block> BLOCK_GRAVITY_CORE = registerBlock("gravity_core", () -> new BlockGravityCore());
		
	public static final RegistryObject<Block> BLOCK_INNER_CORE = registerBlock("inner_core",
		() -> new GlassBlock(AbstractBlock.Properties
			.of(Material.STONE)
			.noOcclusion()
			.sound(SoundType.GLASS)
			.harvestLevel(1)
			.strength(45, 2000)
			.harvestTool(ToolType.PICKAXE)
			.requiresCorrectToolForDrops()
	));
	
	public static final RegistryObject<Block> BLOCK_OUTER_CORE = registerBlock("outer_core",
		() -> new Block(AbstractBlock.Properties
			.of(Material.STONE)
			.harvestLevel(1)
			.strength(4, 2000)
			.harvestTool(ToolType.PICKAXE)
			.requiresCorrectToolForDrops()
	));

	// Items
	public static final RegistryObject<Item> ITEM_GRAVITY_CONTROLLER = ITEMS.register("gravity_controller", () -> new ItemGravityController());
	
	// Tile Entities
	public static final RegistryObject<TileEntityType<TileEntityGravityCore>> TILE_GRAVITY_CORE = TILES.register("tile_gravity_core", TileEntityGravityCore.builder());

	// Containers
	public static final RegistryObject<ContainerType<ContainerGravityCore>> CONTAINER_GRAVITY_CORE = CONTAINERS.register("container_gravity_core", 
		() -> IForgeContainerType.create((windowId, inv, data) -> {
			BlockPos pos = data.readBlockPos();
			World world = inv.player.level;
			return new ContainerGravityCore(windowId, world, pos, inv, inv.player);
		})
	);
}
