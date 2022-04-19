package jp.mc.ancientred.starminer.basics.common;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import jp.mc.ancientred.starminer.api.IRotateSleepingViewHandler;
import jp.mc.ancientred.starminer.basics.Config;
import jp.mc.ancientred.starminer.basics.SMModContainer;
import jp.mc.ancientred.starminer.basics.SMReflectionHelper;
import jp.mc.ancientred.starminer.basics.SMReflectionHelperClient;
import jp.mc.ancientred.starminer.basics.block.render.BlockCSGravitizedRenderHelper;
import jp.mc.ancientred.starminer.basics.block.render.BlockCropsGravitizedRenderHelper;
import jp.mc.ancientred.starminer.basics.block.render.BlockDirtExRenderHelper;
import jp.mc.ancientred.starminer.basics.block.render.BlockGravityCoreRenderHelper;
import jp.mc.ancientred.starminer.basics.block.render.BlockGravityWallRenderHelper;
import jp.mc.ancientred.starminer.basics.block.render.BlockManBazookaRenderHelper;
import jp.mc.ancientred.starminer.basics.block.render.BlockNavigatorRenderHelper;
import jp.mc.ancientred.starminer.basics.block.render.BlockRotatorRenderHelper;
import jp.mc.ancientred.starminer.basics.block.render.BlockStarBedRenderHelper;
import jp.mc.ancientred.starminer.basics.dimention.MapFromSky;
import jp.mc.ancientred.starminer.basics.dimention.WorldProviderSpace;
import jp.mc.ancientred.starminer.basics.dimention.renderer.SpaceCloudRenderHandler;
import jp.mc.ancientred.starminer.basics.dimention.renderer.SpaceSkyRenderHanlder;
import jp.mc.ancientred.starminer.basics.entity.EntityComet;
import jp.mc.ancientred.starminer.basics.entity.EntityFallingBlockEx;
import jp.mc.ancientred.starminer.basics.entity.EntityGProjectile;
import jp.mc.ancientred.starminer.basics.entity.EntityStarSquid;
import jp.mc.ancientred.starminer.basics.entity.RenderComet;
import jp.mc.ancientred.starminer.basics.entity.RenderFallingBlockEx;
import jp.mc.ancientred.starminer.basics.entity.RenderGProjectile;
import jp.mc.ancientred.starminer.basics.entity.RenderStarSquid;
import jp.mc.ancientred.starminer.basics.gui.ContainerStarCore;
import jp.mc.ancientred.starminer.basics.gui.GuiStarCore;
import jp.mc.ancientred.starminer.basics.item.render.RenderEquippedItemGrappleGun;
import jp.mc.ancientred.starminer.basics.packet.SMPacketHandlerClient;
import jp.mc.ancientred.starminer.basics.packet.SMPacketHandlerServer;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityBlockRotator;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityChestEx;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator;
import jp.mc.ancientred.starminer.basics.tileentity.render.TileEnityRenderBlockRotator;
import jp.mc.ancientred.starminer.basics.tileentity.render.TileEntityChestRendererEx;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.logging.log4j.Level;

public class CommonProxyClient
  extends CommonProxy {
  private static Field field_chunkListing;
  
  public World getClientWorld() {
    return (World)(FMLClientHandler.instance().getClient()).theWorld;
  }
  private static Field field_isGapLightingUpdated; private static Field field_queuedLightChecks;
  public void registerNetworkHandler() {
    SMModContainer.channel.register(new SMPacketHandlerClient());
    SMModContainer.channel.register(new SMPacketHandlerServer());
  }
  public void registerRenderHelper() {
    RenderingRegistry.registerEntityRenderingHandler(EntityStarSquid.class, (Render)new RenderStarSquid((ModelBase)new ModelSquid(), 0.7F));
    RenderingRegistry.registerEntityRenderingHandler(EntityGProjectile.class, (Render)new RenderGProjectile());
    RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockEx.class, (Render)new RenderFallingBlockEx());
    RenderingRegistry.registerEntityRenderingHandler(EntityComet.class, (Render)new RenderComet());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BlockGravityCoreRenderHelper());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BlockGravityWallRenderHelper());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BlockManBazookaRenderHelper());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BlockNavigatorRenderHelper());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BlockCropsGravitizedRenderHelper());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BlockCSGravitizedRenderHelper());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BlockDirtExRenderHelper());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BlockStarBedRenderHelper());
    
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BlockRotatorRenderHelper());
    
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlockRotator.class, (TileEntitySpecialRenderer)new TileEnityRenderBlockRotator());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChestEx.class, (TileEntitySpecialRenderer)new TileEntityChestRendererEx());
    
    MinecraftForgeClient.registerItemRenderer(SMModContainer.GrappleGunItem, (IItemRenderer)new RenderEquippedItemGrappleGun());
    MinecraftForgeClient.registerItemRenderer(SMModContainer.BlockGunItem, (IItemRenderer)new RenderEquippedItemGrappleGun());

    IRotateSleepingViewHandler.handlerList.add(new SleepingViewHandler());

    try {
      Tessellator tes = Tessellator.instance;
      
      ReflectionHelper.findField(tes.getClass(), new String[] { "shadersTess" });
      
      Config.hasShaderModInstalled = true;
    } catch (Exception ex) {
      
      Config.hasShaderModInstalled = false;
    } 
  }
  
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == SMModContainer.guiStarCoreId) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te != null && te instanceof TileEntityGravityGenerator) {
        return new ContainerStarCore(player, (TileEntityGravityGenerator)te);
      }
    } 
    return null;
  }
  
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == SMModContainer.guiStarCoreId) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te != null && te instanceof TileEntityGravityGenerator) {
        return new GuiStarCore(player, (TileEntityGravityGenerator)te);
      }
    } 
    return null;
  }
  
  public void handleWorldLoadEvent(WorldEvent.Load event) {
    if (event.world.provider instanceof WorldProviderSpace) {
      ((WorldProviderSpace)event.world.provider).setSkyRenderer((IRenderHandler)new SpaceSkyRenderHanlder());
      ((WorldProviderSpace)event.world.provider).setCloudRenderer((IRenderHandler)new SpaceCloudRenderHandler());
    } 

    
    if (!event.world.isRemote && 
      event.world.provider.dimensionId == 0 && event.world.provider instanceof net.minecraft.world.WorldProviderSurface) {
      LogWrapper.log(Level.INFO, "[Starminer]Creating dimention 0(surface) ground texture at server....", new Object[0]);
      MapFromSky.createMapDataFromSky(event.world);
    } 
  }

  public void doWakeupAll(WorldServer worldServer) {
    if (!worldServer.isRemote && 
      worldServer.provider instanceof jp.mc.ancientred.starminer.api.IZeroGravityWorldProvider && 
      worldServer.areAllPlayersAsleep()) {
      
      if (worldServer.getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
        
        WorldServer serverOverWorld = DimensionManager.getWorld(0);
        long i = serverOverWorld.getWorldTime() + 24000L;
        serverOverWorld.setWorldTime(i - i % 24000L);
      } 
      
      SMReflectionHelper.setallPlayersSleeping(worldServer, false);
      Iterator<EntityPlayer> iterator = worldServer.playerEntities.iterator();
      
      while (iterator.hasNext()) {
        
        EntityPlayer entityplayer = iterator.next();
        
        if (entityplayer.isPlayerSleeping())
        {
          entityplayer.wakeUpPlayer(false, false, true);
        }
      } 
      worldServer.provider.resetRainAndThunder();
    } 
  }

  public void canselLightGapUpdate(IChunkProvider chunkProvider_Client) {
    try {
      if (field_chunkListing == null) {
        field_chunkListing = ReflectionHelper.findField(ChunkProviderClient.class, new String[] { SMReflectionHelperClient.FIELD_NAME_chunkListing[0], SMReflectionHelperClient.FIELD_NAME_chunkListing[1] });
      }
      if (field_isGapLightingUpdated == null) {
        field_isGapLightingUpdated = ReflectionHelper.findField(Chunk.class, new String[] { SMReflectionHelperClient.FIELD_NAME_isGapLightingUpdated[0], SMReflectionHelperClient.FIELD_NAME_isGapLightingUpdated[1] });
      }
      if (field_queuedLightChecks == null) {
        field_queuedLightChecks = ReflectionHelper.findField(Chunk.class, new String[] { SMReflectionHelperClient.FIELD_NAME_queuedLightChecks[0], SMReflectionHelperClient.FIELD_NAME_queuedLightChecks[1] });
      }
      Iterator<Chunk> iterator = ((List)field_chunkListing.get(chunkProvider_Client)).iterator();
      while (iterator.hasNext()) {
        
        Chunk chunk = iterator.next();
        field_isGapLightingUpdated.setBoolean(chunk, false);
        field_queuedLightChecks.setInt(chunk, 4096);
      } 
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  public void showGrappleGunGuideMessage() {
    GameSettings gamesettings = (Minecraft.getMinecraft()).gameSettings;
    (Minecraft.getMinecraft()).ingameGUI.setRecordPlaying(I18n.format("item.g_rapplegun.keyguide", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode()) }), false);
  }
  
  public void setRemainingHighlightTicksOFF() {
    SMReflectionHelperClient.setRemainingHighlightTicks((Minecraft.getMinecraft()).ingameGUI, 0);
  }
}
