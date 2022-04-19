package jp.mc.ancientred.starminer.core;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import java.io.File;
import java.util.Arrays;
import jp.mc.ancientred.starminer.core.common.CoreProxy;
import jp.mc.ancientred.starminer.core.common.CoreProxyClient;
import jp.mc.ancientred.starminer.core.common.CoreProxyServer;
import jp.mc.ancientred.starminer.core.common.SMCommonEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class SMCoreModContainer
  extends DummyModContainer
{
  @Instance("modJ_StarMinerCore")
  public static SMCoreModContainer instance;
  public static CoreProxy proxy;
  public static final String coreNetworkChannelName = "StarminerCore";
  public static FMLEventChannel coreChannel;
  public static final int PACKET_TYPE_ATTRACT = 0;
  public static final int PACKET_TYPE_GCHANGE = 1;
  public static final int PACKET_TYPE_ATTRACT_MOB = 2;
  
  public SMCoreModContainer() {
    super(new ModMetadata());
    ModMetadata meta = getMetadata();
    
    meta.modId = "modJ_StarMinerCore";
    meta.name = "StarMinerCore";
    meta.version = "0.9.9";
    meta.authorList = Arrays.asList(new String[] { "ARUBE(oANCIENTREDo)" });
    meta.description = "It's for StarMinerMOD.";
    meta.url = "";
    meta.credits = "";
    meta.parent = "modJ_StarMiner";
    setEnabledState(true);
  }
  
  public boolean registerBus(EventBus bus, LoadController controller) {
    bus.register(this);
    return true;
  }
  
  @Subscribe
  public void preInit(FMLPreInitializationEvent event) {
    proxy = FMLCommonHandler.instance().getSide().isClient() ? (CoreProxy)new CoreProxyClient() : (CoreProxy)new CoreProxyServer();
    try {
      Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "Starminer.cfg"));
      config.load();

      config.getCategory("core_server_side_property");
      config.addCustomCategoryComment("core_server_side_property", "Affects server side only.");
      CoreConfig.skipIllegalStanceCheck = config.get("core_server_side_property", "skipIllegalStanceCheck", true).getBoolean(true);
      CoreConfig.illegalGStateTickToCheck = config.get("core_server_side_property", "unsynchronizedTickToCheck", 100).getInt(100);
      CoreConfig.showUnsynchronizedWarning = config.get("core_server_side_property", "showUnsynchronizedWarning", true).getBoolean(true);
      CoreConfig.unsynchronizedWarnToKick = config.get("core_server_side_property", "unsynchronizedWarnToKick", 0).getInt(0);
      
      if (config.hasChanged())
      {
        config.save(); } 
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  @Subscribe
  public void init(FMLInitializationEvent event) {
    Object handler = new SMCommonEventHandler();
    MinecraftForge.EVENT_BUS.register(handler);
    FMLCommonHandler.instance().bus().register(handler);

    
    coreChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel("StarminerCore");
    proxy.registerNetworkHandler();
  }
}
