package dev.bluecom.starminer.basics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.basics.common.CommonForgeEventHandler;
import dev.bluecom.starminer.basics.item.RegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SMModContainer.MODID)
public class SMModContainer {
	public static final String MODID = "starminer";
	public static final int PACKET_TYPE_GCORE_GUIACT = 10;
	public static final int PACKET_TYPE_SKYMAP = 12;
	public static final int PACKET_TYPE_TEUPD_GCORE = 14;
	public static final int PACKET_TYPE_TEUPD_NAVIG = 16;
	public static final int PACKET_TYPE_DIMENTION_RESPAWN = 18;
	public static final int PACKET_TYPE_RERIDE_MOB = 20;
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public SMModContainer() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setup);
		RegistryHandler.init(bus);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Registering the Event Handlers");
		MinecraftForge.EVENT_BUS.register(new CommonForgeEventHandler());
		GravityProvider.init();
	}
}
