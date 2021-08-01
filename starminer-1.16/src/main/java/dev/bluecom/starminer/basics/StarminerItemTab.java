package dev.bluecom.starminer.basics;

import dev.bluecom.starminer.basics.item.ItemRegistryHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class StarminerItemTab {
	public static final ItemGroup STARMINER = new ItemGroup("starminer") {
		@Override
		public ItemStack makeIcon() { return new ItemStack(ItemRegistryHandler.GRAVITY_CONTROLLER.get()); }
	};
}
