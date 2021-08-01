package dev.bluecom.starminer.basics.item;

import dev.bluecom.starminer.basics.SMModContainer;
import dev.bluecom.starminer.basics.StarminerItemTab;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistryHandler {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SMModContainer.MODID);

	public static final RegistryObject<Item> GRAVITY_CONTROLLER = ITEMS.register("gravity_controller",
		() -> new ItemGravityController(new Item.Properties().tab(StarminerItemTab.STARMINER)));
	
	public static void init(IEventBus bus) {
		ITEMS.register(bus);
	}
}
