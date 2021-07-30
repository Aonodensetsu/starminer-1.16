package dev.bluecom.starminer.basics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.bluecom.starminer.api.GravityCapability;
import dev.bluecom.starminer.api.GravityStorage;
import dev.bluecom.starminer.api.IGravityCapability;
import dev.bluecom.starminer.basics.common.CommonForgeEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("starminer")
public class SMModContainer {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public SMModContainer instance = this;
  public static final String networkChannelName = "Starminer";
  public static final int PACKET_TYPE_GCORE_GUIACT = 10;
  public static final int PACKET_TYPE_SKYMAP = 12;
  public static final int PACKET_TYPE_TEUPD_GCORE = 14;
  public static final int PACKET_TYPE_TEUPD_NAVIG = 16;
  public static final int PACKET_TYPE_DIMENTION_RESPAWN = 18;
  public static final int PACKET_TYPE_RERIDE_MOB = 20;
  
  public SMModContainer() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
  }
  private void setup(final FMLCommonSetupEvent event) {
	  LOGGER.info("Registering the Event Handlers");
	  MinecraftForge.EVENT_BUS.register(new CommonForgeEventHandler());
	  CapabilityManager.INSTANCE.register(IGravityCapability.class, new GravityStorage(), GravityCapability::new);
  }
}
