package dev.bluecom.starminer.basics.item;

import dev.bluecom.starminer.basics.SMModContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SMModContainer.MODID);
	
	public static final RegistryObject<Item> GRAVITYCONTROLLER = ITEMS.register("gravitycontroller",
		() -> new ItemGravityController(new Item.Properties().tab(StarminerTab.STARMINER)));
	
	public static void init(IEventBus bus) {
		ITEMS.register(bus);
	}
}

class StarminerTab {
	public static final ItemGroup STARMINER = new ItemGroup("starminer") {
		@Override
		public ItemStack makeIcon() { return new ItemStack(RegistryHandler.GRAVITYCONTROLLER.get()); }
	};
}
