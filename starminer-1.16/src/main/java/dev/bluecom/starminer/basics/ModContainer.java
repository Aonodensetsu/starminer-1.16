package dev.bluecom.starminer.basics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.basics.common.CommonForgeEventHandler;
import dev.bluecom.starminer.basics.common.CommonNetworkHandler;
import dev.bluecom.starminer.basics.common.CommonRegistryHandler;
import dev.bluecom.starminer.basics.item.ItemGravityController;
import dev.bluecom.starminer.basics.tileentity.ScreenGravityCore;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModContainer.MODID)
public class ModContainer {
	public static final String MODID = "starminer";
	public static final int PACKET_TYPE_GCORE_GUIACT = 10;
	public static final int PACKET_TYPE_SKYMAP = 12;
	public static final int PACKET_TYPE_TEUPD_GCORE = 14;
	public static final int PACKET_TYPE_TEUPD_NAVIG = 16;
	public static final int PACKET_TYPE_DIMENTION_RESPAWN = 18;
	public static final int PACKET_TYPE_RERIDE_MOB = 20;
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public ModContainer() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setup);
		bus.addListener(this::postcomms);
		CommonRegistryHandler.init(bus);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Registering the Event Handlers");
		MinecraftForge.EVENT_BUS.register(new CommonForgeEventHandler());
		GravityProvider.init();
		CommonNetworkHandler.registerMessages();
	}

	private void postcomms(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			RenderTypeLookup.setRenderLayer(CommonRegistryHandler.BLOCK_INNER_CORE.get(), RenderType.translucent());
			ItemModelsProperties.register(
				CommonRegistryHandler.ITEM_GRAVITY_CONTROLLER.get(),
				new ResourceLocation(ModContainer.MODID, "gravitystate"), (stack, world, living) -> { 
					ItemGravityController item = (ItemGravityController) stack.getItem();
					return item.gravstate;
				}
			);
			ScreenManager.register(CommonRegistryHandler.CONTAINER_GRAVITY_CORE.get(), ScreenGravityCore::new);
		});
	}
}
