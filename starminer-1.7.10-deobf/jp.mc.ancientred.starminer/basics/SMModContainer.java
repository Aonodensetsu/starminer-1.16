package jp.mc.ancientred.starminer.basics;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.io.File;
import java.lang.reflect.Field;
import jp.mc.ancientred.starminer.basics.ai.mobai.MobAnimalsAIHelpers;
import jp.mc.ancientred.starminer.basics.ai.mobai.MobMonstersAIHelpers;
import jp.mc.ancientred.starminer.basics.block.BlockAirEx;
import jp.mc.ancientred.starminer.basics.block.BlockChestEx;
import jp.mc.ancientred.starminer.basics.block.BlockDirtGrassEx;
import jp.mc.ancientred.starminer.basics.block.BlockGravityCore;
import jp.mc.ancientred.starminer.basics.block.BlockGravityWall;
import jp.mc.ancientred.starminer.basics.block.BlockInnerCore;
import jp.mc.ancientred.starminer.basics.block.BlockManBazooka;
import jp.mc.ancientred.starminer.basics.block.BlockNavigator;
import jp.mc.ancientred.starminer.basics.block.BlockOuterCore;
import jp.mc.ancientred.starminer.basics.block.BlockRotator;
import jp.mc.ancientred.starminer.basics.block.bed.BlockStarBedBody;
import jp.mc.ancientred.starminer.basics.block.bed.BlockStarBedHead;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockCarrotGravitized;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockCropsGravitized;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockFlowerGravitized;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockPotatoGravitized;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockSaplingGravitized;
import jp.mc.ancientred.starminer.basics.block.gravitized.BlockTallGrassGravitized;
import jp.mc.ancientred.starminer.basics.common.CommonForgeEventHandler;
import jp.mc.ancientred.starminer.basics.common.CommonProxy;
import jp.mc.ancientred.starminer.basics.common.CommonProxyClient;
import jp.mc.ancientred.starminer.basics.common.CommonProxyServer;
import jp.mc.ancientred.starminer.basics.dimention.WorldProviderSpace;
import jp.mc.ancientred.starminer.basics.dimention.oregen.StarOreGenEventHandler;
import jp.mc.ancientred.starminer.basics.entity.EntityComet;
import jp.mc.ancientred.starminer.basics.entity.EntityFallingBlockEx;
import jp.mc.ancientred.starminer.basics.entity.EntityGProjectile;
import jp.mc.ancientred.starminer.basics.entity.EntityReRideAttacher;
import jp.mc.ancientred.starminer.basics.entity.EntityStarSquid;
import jp.mc.ancientred.starminer.basics.item.ItemBlockGun;
import jp.mc.ancientred.starminer.basics.item.ItemEarthStone;
import jp.mc.ancientred.starminer.basics.item.ItemGArrow;
import jp.mc.ancientred.starminer.basics.item.ItemGHook;
import jp.mc.ancientred.starminer.basics.item.ItemGrappleGun;
import jp.mc.ancientred.starminer.basics.item.ItemGravityContoler;
import jp.mc.ancientred.starminer.basics.item.ItemLifeSoup;
import jp.mc.ancientred.starminer.basics.item.ItemPortableGravigy;
import jp.mc.ancientred.starminer.basics.item.ItemSolarWindFan;
import jp.mc.ancientred.starminer.basics.item.ItemStarBed;
import jp.mc.ancientred.starminer.basics.item.ItemStarContoler;
import jp.mc.ancientred.starminer.basics.item.ItemStardust;
import jp.mc.ancientred.starminer.basics.item.block.ItemBlockForGWall;
import jp.mc.ancientred.starminer.basics.item.block.ItemBlockForRotator;
import jp.mc.ancientred.starminer.basics.item.block.ItemBlockWithStarMark;
import jp.mc.ancientred.starminer.basics.item.block.ItemColoredForTallGrass;
import jp.mc.ancientred.starminer.basics.item.block.ItemMultiTextureTileForG;
import jp.mc.ancientred.starminer.basics.item.crop.ItemSeedFoodGravitized;
import jp.mc.ancientred.starminer.basics.item.crop.ItemSeedGravitized;
import jp.mc.ancientred.starminer.basics.structure.ComponentStar;
import jp.mc.ancientred.starminer.basics.structure.MapGenStar;
import jp.mc.ancientred.starminer.basics.structure.StructureStarStart;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityBlockRotator;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityChestEx;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityGravityGenerator;
import jp.mc.ancientred.starminer.basics.tileentity.TileEntityNavigator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSapling;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = "modJ_StarMiner", name = "StarMiner", version = "0.9.9", useMetadata = true)
public class SMModContainer
{
  @Instance("modJ_StarMiner")
  public static SMModContainer instance;
  public static CommonProxy proxy;
  public static FMLEventChannel channel;
  public static final String networkChannelName = "Starminer";
  public static final int PACKET_TYPE_GCORE_GUIACT = 10;
  public static final int PACKET_TYPE_SKYMAP = 12;
  public static final int PACKET_TYPE_TEUPD_GCORE = 14;
  public static final int PACKET_TYPE_TEUPD_NAVIG = 16;
  public static final int PACKET_TYPE_DIMENTION_RESPAWN = 18;
  public static final int PACKET_TYPE_RERIDE_MOB = 20;
  public static MapGenStar starGenerator = new MapGenStar();

  public static int GeostationaryOrbitDimentionId = 88;

  public static Item GravityControllerItem;

  public static Item StarControlerItem;

  public static Item SolarWindFanItem;
  
  public static Item StarBedItem;
  
  public static Item StardustItem;
  
  public static Item GrappleGunItem;
  
  public static Item BlockGunItem;
  
  public static Item GArrowItem;
  
  public static Item GHookItem;
  
  public static Item LifeSoupItem;
  
  public static Item PortableGravityItem;
  
  public static Item EarthStone;
  
  public static Item SeedGravizedItem;
  
  public static Item CarrotGravizedItem;
  
  public static Item PotatoGravizedItem;
  
  public static Block GravityCoreBlock;
  
  public static Block InnerCoreBlock;
  
  public static Block OuterCoreBlock;
  
  public static Block ManBazookaBlock;
  
  public static Block NavigatorBlock;
  
  public static Block GravityWallBlock;
  
  public static Block AirExBlock;
  
  public static Block DirtGrassExBlock;
  
  public static Block StarBedBodyBlock;
  
  public static Block StarBedHeadBlock;
  
  public static Block BlockRotatorBlock;
  
  public static Block BlockChestEx;
  
  public static Block CropGravitizedBlock;
  
  public static Block PlantYelGravitizedBlock;
  
  public static Block PlantRedGravitizedBlock;
  
  public static Block SaplingGravitizedBlock;
  
  public static Block CarrotGravitizedBlock;
  
  public static Block PotatoGravitizedBlock;
  
  public static Block TallGrassGravitizedBlock;
  
  public static int guiStarCoreId = 1243;
  
  public static CreativeTabs starMinerTab = new CreativeTabs(CreativeTabs.getNextID(), "StarMiner")
    {
      public Item getTabIconItem() {
        return Item.getItemFromBlock(SMModContainer.GravityCoreBlock);
      }
    };

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    proxy = FMLCommonHandler.instance().getSide().isClient() ? (CommonProxy)new CommonProxyClient() : (CommonProxy)new CommonProxyServer();
    
    try {
      Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "Starminer.cfg"));
      config.load();

      config.getCategory("basics_server_side_property");
      config.addCustomCategoryComment("basics_server_side_property", "Affects server side only.");
      
      Config.bazookaStartSpeed = config.get("basics_server_side_property", "bazookaStartSpeed", 3.3D).getDouble(3.3D);
      Config.generateStars = config.get("basics_server_side_property", "generateStars", true).getBoolean(true);
      Config.generateOres = config.get("basics_server_side_property", "generateOres", false).getBoolean(false);
      Config.ticketFreeForTeleport = config.get("basics_server_side_property", "ticketFreeForTeleport", false).getBoolean(false);
      Config.attractCheckTick = config.get("basics_server_side_property", "attractCheckTick", 8).getInt(8);
      Config.enableFakeRotatorOnlyVannilaBlock = config.get("basics_server_side_property", "enableFakeRotatorOnlyVannilaBlock", true).getBoolean(true);
      if (Config.attractCheckTick == 0L) Config.attractCheckTick = 1L;

      Config.maxStarRad = config.get("basics_server_side_property", "maxStarRad", 48).getInt(48);
      Config.maxGravityRad = config.get("basics_server_side_property", "maxGravityRad", 54).getInt(54);

      GeostationaryOrbitDimentionId = config.get("basics_server_side_property", "GSODimentionId_byte", 88).getInt(88);

      config.getCategory("basics_client_side_property");
      config.addCustomCategoryComment("basics_client_side_property", "Affects client side only.");
      Config.naviLaserLength = config.get("basics_client_side_property", "naviLaserLength", 7).getInt(7);
      
      if (config.hasChanged())
      {
        config.save(); } 
    } catch (Exception ex) {
      ex.printStackTrace();
    } 

    MapGenStructureIO.registerStructure(StructureStarStart.class, "StarminerStarStart");
    MapGenStructureIO.registerStructureComponent(ComponentStar.class, "StarminerStar");

    GameRegistry.registerBlock(GravityCoreBlock = (new BlockGravityCore()).setTextureName("starminer:g_core").setUnlocalizedName("gravitycore"), "gravitycore");
    GameRegistry.registerBlock(InnerCoreBlock = (new BlockInnerCore()).setTextureName("starminer:inner_core").setUnlocalizedName("innercore"), "innercore");
    GameRegistry.registerBlock(OuterCoreBlock = (new BlockOuterCore()).setTextureName("starminer:outer_core").setUnlocalizedName("outercore"), "outercore");
    GameRegistry.registerBlock(ManBazookaBlock = (new BlockManBazooka()).setTextureName("starminer:dokan").setUnlocalizedName("manbazooka"), "manbazooka");
    GameRegistry.registerBlock(NavigatorBlock = (new BlockNavigator()).setTextureName("starminer:g_core").setUnlocalizedName("navigator"), "navigator");
    GameRegistry.registerBlock(GravityWallBlock = (new BlockGravityWall()).setTextureName("starminer:g_wall").setUnlocalizedName("gravitywall"), ItemBlockForGWall.class, "gravitywall");
    GameRegistry.registerBlock(AirExBlock = (new BlockAirEx()).setUnlocalizedName("airex"), "airex");
    GameRegistry.registerBlock(DirtGrassExBlock = (new BlockDirtGrassEx()).setUnlocalizedName("dirtgrassex"), "dirtgrassex");
    GameRegistry.registerBlock(BlockRotatorBlock = (new BlockRotator()).setUnlocalizedName("blockrotator"), ItemBlockForRotator.class, "blockrotator");
    GameRegistry.registerBlock(BlockChestEx = (new BlockChestEx(0)).setUnlocalizedName("chestgravitized"), ItemBlockWithStarMark.class, "chestgravitized");

    OreDictionary.registerOre("blockGravityCore", GravityCoreBlock);
    OreDictionary.registerOre("blockInnerCore", InnerCoreBlock);
    OreDictionary.registerOre("blockOuterCore", OuterCoreBlock);
    OreDictionary.registerOre("blockManBazooka", ManBazookaBlock);
    OreDictionary.registerOre("blockNavigator", NavigatorBlock);
    OreDictionary.registerOre("blockGravityWall", GravityWallBlock);
    OreDictionary.registerOre("blockStarDirt", DirtGrassExBlock);

    GameRegistry.registerBlock(StarBedBodyBlock = (new BlockStarBedBody()).setUnlocalizedName("starbedbody"), "starbedbody");
    GameRegistry.registerBlock(StarBedHeadBlock = (new BlockStarBedHead()).setUnlocalizedName("starbedhead"), "starbedhead");

    GameRegistry.registerItem(GravityControllerItem = (new ItemGravityContoler()).setUnlocalizedName("gravitycontroller"), "gravitycontroller");
    GameRegistry.registerItem(StarControlerItem = (new ItemStarContoler()).setUnlocalizedName("starcontroller"), "starcontroller");
    GameRegistry.registerItem(SolarWindFanItem = (new ItemSolarWindFan()).setUnlocalizedName("solarwindFan"), "solarwindFan");
    GameRegistry.registerItem(StarBedItem = (new ItemStarBed()).setUnlocalizedName("starbed"), "starbed");
    
    GameRegistry.registerItem(StardustItem = (new ItemStardust()).setUnlocalizedName("starcore_dust"), "starcore_dust");
    GameRegistry.registerItem(GrappleGunItem = (new ItemGrappleGun()).setUnlocalizedName("g_rapplegun"), "g_rapplegun");
    GameRegistry.registerItem(GArrowItem = (new ItemGArrow()).setUnlocalizedName("g_arrow"), "g_arrow");
    GameRegistry.registerItem(GHookItem = (new ItemGHook()).setUnlocalizedName("g_hook"), "g_hook");
    GameRegistry.registerItem(BlockGunItem = (new ItemBlockGun()).setUnlocalizedName("blockgun"), "blockgun");
    
    GameRegistry.registerItem(LifeSoupItem = (new ItemLifeSoup()).setUnlocalizedName("lifesoup"), "lifesoup");
    GameRegistry.registerItem(PortableGravityItem = (new ItemPortableGravigy()).setUnlocalizedName("portablegravity"), "portablegravity");

    GameRegistry.registerItem(EarthStone = (new ItemEarthStone()).setUnlocalizedName("earthstone"), "earthstone");

    OreDictionary.registerOre("gemStardust", StardustItem);

    GameRegistry.registerBlock(CropGravitizedBlock = (new BlockCropsGravitized()).setUnlocalizedName("cropgravitized"), "cropgravitized");
    GameRegistry.registerBlock(CarrotGravitizedBlock = (new BlockCarrotGravitized()).setUnlocalizedName("carrotgravitized"), "carrotgravitized");
    GameRegistry.registerBlock(PotatoGravitizedBlock = (new BlockPotatoGravitized()).setUnlocalizedName("potatogravitized"), "potatogravitized");
    
    GameRegistry.registerBlock(SaplingGravitizedBlock = (new BlockSaplingGravitized()).setUnlocalizedName("saplinggravitized"), ItemMultiTextureTileForG.class, "saplinggravitized", new Object[] { SaplingGravitizedBlock, BlockSapling.field_149882_a });
    GameRegistry.registerBlock(PlantYelGravitizedBlock = (new BlockFlowerGravitized(0)).setUnlocalizedName("yflowergravitized"), ItemMultiTextureTileForG.class, "yflowergravitized", new Object[] { PlantYelGravitizedBlock, BlockFlower.field_149858_b });
    GameRegistry.registerBlock(PlantRedGravitizedBlock = (new BlockFlowerGravitized(1)).setUnlocalizedName("rflowergravitized"), ItemMultiTextureTileForG.class, "rflowergravitized", new Object[] { PlantRedGravitizedBlock, BlockFlower.field_149859_a });
    GameRegistry.registerBlock(TallGrassGravitizedBlock = (new BlockTallGrassGravitized()).setUnlocalizedName("tallgrassgravitized"), ItemColoredForTallGrass.class, "tallgrassgravitized");

    GameRegistry.registerItem(SeedGravizedItem = (new ItemSeedGravitized(CropGravitizedBlock, Blocks.farmland)).setUnlocalizedName("seedsgravitized").setTextureName("seeds_wheat"), "seedsgravitized");
    GameRegistry.registerItem(CarrotGravizedItem = (new ItemSeedFoodGravitized(CarrotGravitizedBlock)).setUnlocalizedName("carrotgravitizeditem").setTextureName("carrot"), "carrotgravitizeditem");
    GameRegistry.registerItem(PotatoGravizedItem = (new ItemSeedFoodGravitized(PotatoGravitizedBlock)).setUnlocalizedName("potatogravitizeditem").setTextureName("potato"), "potatogravitizeditem");

    setMyCreativeTabs();

    NetworkRegistry.INSTANCE.registerGuiHandler(this, (IGuiHandler)proxy);

    DimensionManager.registerProviderType(GeostationaryOrbitDimentionId, WorldProviderSpace.class, true);
    DimensionManager.registerDimension(GeostationaryOrbitDimentionId, GeostationaryOrbitDimentionId);

    this; checkIf1710OldWorld();
  }

  public static boolean isIS1710OldWorld = false;

  private static final void checkIf1710OldWorld() {
    try {
      Field targetFiled = ReflectionHelper.findField(World.class, new String[] { "restoringBlockSnapshots" });
    }
    catch (Exception ex) {
      
      isIS1710OldWorld = true;
    } 
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    Object handler = new CommonForgeEventHandler();
    MinecraftForge.EVENT_BUS.register(handler);
    FMLCommonHandler.instance().bus().register(handler);

    channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("Starminer");
    proxy.registerNetworkHandler();

    GameRegistry.registerTileEntity(TileEntityGravityGenerator.class, "TileEntityGravityCore");
    GameRegistry.registerTileEntity(TileEntityNavigator.class, "TileEntityNavigator");
    GameRegistry.registerTileEntity(TileEntityBlockRotator.class, "TileEntityBlockRotator");
    GameRegistry.registerTileEntity(TileEntityChestEx.class, "TileEntityChestEx");

    EntityRegistry.registerModEntity(EntityFallingBlockEx.class, "FallingBlockEx", 564, this, 30, 1, true);
    EntityRegistry.registerModEntity(EntityStarSquid.class, "StarSquid", 565, this, 80, 1, true);
    EntityRegistry.registerModEntity(EntityGProjectile.class, "GProjectile", 566, this, 80, 1, true);
    EntityRegistry.registerModEntity(EntityReRideAttacher.class, "ReRideAttacher", 567, this, 30, 1, true);
    EntityRegistry.registerModEntity(EntityComet.class, "WanderingComet", 568, this, 128, 1, true);

    addRecipie();

    proxy.registerRenderHelper();

    if (Config.generateOres) {
      MinecraftForge.ORE_GEN_BUS.register(new StarOreGenEventHandler());
    }

    MobAnimalsAIHelpers.register();
    
    MobMonstersAIHelpers.register();
  }

  private void addRecipie() {
    GameRegistry.addRecipe(new ItemStack(OuterCoreBlock), new Object[] { "XZX", "ZYZ", "XZX", Character.valueOf('X'), Blocks.sandstone, Character.valueOf('Y'), Items.lava_bucket, Character.valueOf('Z'), Blocks.dirt });
    
    GameRegistry.addRecipe(new ItemStack(OuterCoreBlock), new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), StardustItem });
    
    GameRegistry.addRecipe(new ItemStack(InnerCoreBlock), new Object[] { "ZXZ", "XYX", "ZXZ", Character.valueOf('X'), Blocks.obsidian, Character.valueOf('Y'), Blocks.iron_block, Character.valueOf('Z'), Blocks.glass });
    
    GameRegistry.addRecipe(new ItemStack(GravityCoreBlock), new Object[] { "XXX", "XYX", "XXX", Character.valueOf('X'), OuterCoreBlock, Character.valueOf('Y'), InnerCoreBlock });
    
    GameRegistry.addRecipe(new ItemStack(GravityWallBlock), new Object[] { "XXX", Character.valueOf('X'), StardustItem });
    
    GameRegistry.addRecipe(new ItemStack(GravityControllerItem), new Object[] { "X  ", "YZZ", "X Z", Character.valueOf('X'), OuterCoreBlock, Character.valueOf('Y'), InnerCoreBlock, Character.valueOf('Z'), Items.stick });
    
    GameRegistry.addRecipe(new ItemStack(StarControlerItem), new Object[] { "XYX", "XXX", " Z ", Character.valueOf('X'), Items.iron_ingot, Character.valueOf('Y'), InnerCoreBlock, Character.valueOf('Z'), GravityControllerItem });
    
    GameRegistry.addRecipe(new ItemStack(SolarWindFanItem), new Object[] { "XXY", "XZY", "YYW", Character.valueOf('X'), OuterCoreBlock, Character.valueOf('Y'), new ItemStack(Items.dye, 1, 4), Character.valueOf('Z'), InnerCoreBlock, Character.valueOf('W'), Items.stick });
    
    GameRegistry.addRecipe(new ItemStack(StarBedItem), new Object[] { " X ", "YYY", Character.valueOf('X'), Items.bed, Character.valueOf('Y'), GravityWallBlock });
    
    GameRegistry.addRecipe(new ItemStack(BlockChestEx), new Object[] { " X ", "YYY", Character.valueOf('X'), Blocks.chest, Character.valueOf('Y'), GravityWallBlock });
    
    GameRegistry.addShapelessRecipe(new ItemStack(StardustItem, 9), new Object[] { new ItemStack(OuterCoreBlock, 1) });

    GameRegistry.addShapelessRecipe(new ItemStack(BlockRotatorBlock, 1), new Object[] { new ItemStack(Blocks.glass, 1), new ItemStack(StardustItem, 1) });

    GameRegistry.addRecipe(new ItemStack(BlockGunItem, 1, 0), new Object[] { " XX", "XXY", "  Z", Character.valueOf('X'), Blocks.cobblestone, Character.valueOf('Y'), StardustItem, Character.valueOf('Z'), GravityControllerItem });
    GameRegistry.addShapelessRecipe(new ItemStack(BlockGunItem, 1, 1), new Object[] { new ItemStack(BlockGunItem, 1, 0) });
    GameRegistry.addShapelessRecipe(new ItemStack(BlockGunItem, 1, 2), new Object[] { new ItemStack(BlockGunItem, 1, 1) });
    GameRegistry.addShapelessRecipe(new ItemStack(BlockGunItem, 1, 3), new Object[] { new ItemStack(BlockGunItem, 1, 2) });
    GameRegistry.addShapelessRecipe(new ItemStack(BlockGunItem, 1, 0), new Object[] { new ItemStack(BlockGunItem, 1, 3) });

    GameRegistry.addRecipe(new ItemStack(GrappleGunItem), new Object[] { " XX", "XXY", "  Z", Character.valueOf('X'), Blocks.obsidian, Character.valueOf('Y'), GravityCoreBlock, Character.valueOf('Z'), GravityControllerItem });
    
    GameRegistry.addShapelessRecipe(new ItemStack(GArrowItem, 1), new Object[] { new ItemStack(Items.arrow, 1), new ItemStack(StardustItem, 1) });
    
    GameRegistry.addShapelessRecipe(new ItemStack(GHookItem, 1), new Object[] { new ItemStack(GArrowItem, 1), new ItemStack(Items.string, 1) });
    
    GameRegistry.addRecipe(new ItemStack(NavigatorBlock, 2), new Object[] { "X", "Y", Character.valueOf('X'), OuterCoreBlock, Character.valueOf('Y'), Blocks.torch });
    GameRegistry.addRecipe(new ItemStack(NavigatorBlock, 2), new Object[] { "Y", "X", Character.valueOf('X'), OuterCoreBlock, Character.valueOf('Y'), Blocks.torch });
    
    GameRegistry.addRecipe(new ItemStack(ManBazookaBlock), new Object[] { "XTX", "XZX", "YWY", Character.valueOf('X'), Blocks.cobblestone_wall, Character.valueOf('T'), Blocks.planks, Character.valueOf('Z'), Items.gunpowder, Character.valueOf('Y'), Blocks.cobblestone, Character.valueOf('W'), Blocks.furnace });
    
    GameRegistry.addRecipe(new ItemStack(ManBazookaBlock), new Object[] { "XTX", "XZX", "YWY", Character.valueOf('X'), Blocks.cobblestone_wall, Character.valueOf('T'), Blocks.planks, Character.valueOf('Z'), InnerCoreBlock, Character.valueOf('Y'), Blocks.cobblestone, Character.valueOf('W'), Blocks.furnace });

    GameRegistry.addRecipe(new ItemStack(PortableGravityItem), new Object[] { " X ", "XYX", " X ", Character.valueOf('X'), Items.iron_ingot, Character.valueOf('Y'), GravityCoreBlock });
    
    GameRegistry.addShapelessRecipe(new ItemStack(LifeSoupItem, 1, 1), new Object[] { new ItemStack(StardustItem, 1), new ItemStack(Items.water_bucket, 1) });
    
    GameRegistry.addShapelessRecipe(new ItemStack(LifeSoupItem, 1, 2), new Object[] { new ItemStack(LifeSoupItem, 1, 0), new ItemStack(Items.book, 1) });

    GameRegistry.addShapelessRecipe(new ItemStack(DirtGrassExBlock, 1), new Object[] { new ItemStack(Blocks.dirt, 1), new ItemStack(StardustItem, 1) });
    
    GameRegistry.addShapelessRecipe(new ItemStack(SeedGravizedItem, 1), new Object[] { new ItemStack(Items.wheat_seeds, 1), new ItemStack(StardustItem, 1) });
    
    GameRegistry.addShapelessRecipe(new ItemStack(CarrotGravizedItem, 1), new Object[] { new ItemStack(Items.carrot, 1), new ItemStack(StardustItem, 1) });
    
    GameRegistry.addShapelessRecipe(new ItemStack(PotatoGravizedItem, 1), new Object[] { new ItemStack(Items.potato, 1), new ItemStack(StardustItem, 1) });
    int i;
    for (i = 0; i < 1; i++) {
      GameRegistry.addShapelessRecipe(new ItemStack(PlantYelGravitizedBlock, 1, i), new Object[] { new ItemStack((Block)Blocks.yellow_flower, 1, i), new ItemStack(StardustItem, 1) });
    } 
    
    for (i = 0; i < 9; i++) {
      GameRegistry.addShapelessRecipe(new ItemStack(PlantRedGravitizedBlock, 1, i), new Object[] { new ItemStack((Block)Blocks.red_flower, 1, i), new ItemStack(StardustItem, 1) });
    } 
    
    for (i = 0; i < 6; i++) {
      GameRegistry.addShapelessRecipe(new ItemStack(SaplingGravitizedBlock, 1, i), new Object[] { new ItemStack(Blocks.sapling, 1, i), new ItemStack(StardustItem, 1) });
    } 
  }

  private void setMyCreativeTabs() {
    GravityControllerItem.setCreativeTab(starMinerTab);
    StarControlerItem.setCreativeTab(starMinerTab);
    SolarWindFanItem.setCreativeTab(starMinerTab);
    StarBedItem.setCreativeTab(starMinerTab);
    StardustItem.setCreativeTab(starMinerTab);
    GrappleGunItem.setCreativeTab(starMinerTab);
    BlockGunItem.setCreativeTab(starMinerTab);
    GArrowItem.setCreativeTab(starMinerTab);
    GHookItem.setCreativeTab(starMinerTab);
    LifeSoupItem.setCreativeTab(starMinerTab);
    PortableGravityItem.setCreativeTab(starMinerTab);
    EarthStone.setCreativeTab(starMinerTab);

    GravityCoreBlock.setCreativeTab(starMinerTab);
    InnerCoreBlock.setCreativeTab(starMinerTab);
    OuterCoreBlock.setCreativeTab(starMinerTab);
    ManBazookaBlock.setCreativeTab(starMinerTab);
    NavigatorBlock.setCreativeTab(starMinerTab);
    GravityWallBlock.setCreativeTab(starMinerTab);
    DirtGrassExBlock.setCreativeTab(starMinerTab);
    BlockRotatorBlock.setCreativeTab(starMinerTab);
    BlockChestEx.setCreativeTab(starMinerTab);

    SeedGravizedItem.setCreativeTab(starMinerTab);
    CarrotGravizedItem.setCreativeTab(starMinerTab);
    PotatoGravizedItem.setCreativeTab(starMinerTab);

    SaplingGravitizedBlock.setCreativeTab(starMinerTab);
    PlantYelGravitizedBlock.setCreativeTab(starMinerTab);
    PlantRedGravitizedBlock.setCreativeTab(starMinerTab);
    TallGrassGravitizedBlock.setCreativeTab(starMinerTab);

    CropGravitizedBlock.setCreativeTab(null);
    CarrotGravitizedBlock.setCreativeTab(null);
    PotatoGravitizedBlock.setCreativeTab(null);
  }
}
