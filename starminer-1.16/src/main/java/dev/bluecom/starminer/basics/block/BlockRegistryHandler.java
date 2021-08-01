package dev.bluecom.starminer.basics.block;

import java.util.function.Supplier;

import dev.bluecom.starminer.basics.SMModContainer;
import dev.bluecom.starminer.basics.item.ItemRegistryHandler;
import dev.bluecom.starminer.basics.StarminerItemTab;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistryHandler {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SMModContainer.MODID);
	
	public static final RegistryObject<Block> INNER_CORE = registerBlock("inner_core",
		() -> new GlassBlock(AbstractBlock.Properties
			.of(Material.STONE)
			.noOcclusion()
			.sound(SoundType.GLASS)
			.harvestLevel(1)
			.strength(45, 2000)
			.harvestTool(ToolType.PICKAXE)
			.requiresCorrectToolForDrops()
	));
	
	public static final RegistryObject<Block> OUTER_CORE = registerBlock("outer_core",
			() -> new Block(AbstractBlock.Properties
				.of(Material.STONE)
				.harvestLevel(1)
				.strength(4, 2000)
				.harvestTool(ToolType.PICKAXE)
				.requiresCorrectToolForDrops()
		));
	
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ItemRegistryHandler.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(StarminerItemTab.STARMINER)));
    }
    
	public static void init(IEventBus bus) {
		BLOCKS.register(bus);
	}
}
