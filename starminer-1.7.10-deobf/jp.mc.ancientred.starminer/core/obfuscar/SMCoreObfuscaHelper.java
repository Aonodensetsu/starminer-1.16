package jp.mc.ancientred.starminer.core.obfuscar;
import java.util.Map;
import jp.mc.ancientred.starminer.core.SMPlugin;

public class SMCoreObfuscaHelper {
  static class Elem {
    public String name;
    public String rawName;
    
    public Elem(String name, String rawName, String seargeName, String desc, String rawDesc) {
      this.name = name;
      this.rawName = rawName;
      this.seargeName = seargeName;
      this.desc = desc;
      this.rawDesc = rawDesc;
    } public String seargeName; public String desc; public String rawDesc;
    public Elem(String name, String rawName, String seargeName, String desc) {
      this.name = name;
      this.rawName = rawName;
      this.seargeName = seargeName;
      this.desc = desc;
      this.rawDesc = desc;
    }
    public Elem(String name, String rawName) {
      this.name = name;
      this.rawName = rawName;
    }
  }

  public static Map<String, Elem> descMap1710 = new HashMap<String, Elem>();

  public static boolean isVersion164;

  public static boolean isVersion17210;

  public static void putMapping1710(Elem elem) {
    descMap1710.put(elem.name, elem);
  }
  public static String getProperName(String name) {
    Map<String, Elem> descMap = descMap1710;
    if (!SMPlugin.RUNTIME_DEOBF) {
      return ((Elem)descMap.get(name)).name;
    }
    return ((Elem)descMap.get(name)).rawName;
  }
  
  public static String getProperDesc(String name) {
    Map<String, Elem> descMap = descMap1710;
    if (!SMPlugin.RUNTIME_DEOBF) {
      return ((Elem)descMap.get(name)).desc;
    }
    return ((Elem)descMap.get(name)).rawDesc;
  }
  
  public static String getSrgName(String name) {
    Map<String, Elem> descMap = descMap1710;
    if (!SMPlugin.RUNTIME_DEOBF) {
      return ((Elem)descMap.get(name)).name;
    }
    return ((Elem)descMap.get(name)).seargeName;
  }

  static {
    putMapping1710(new Elem("getIconIndex", "j", "func_77650_f", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/util/IIcon;", "(Ladd;)Lrf;"));
    
    putMapping1710(new Elem("getMovingObjectPositionFromPlayer", "a", "func_77621_a", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Z)Lnet/minecraft/util/MovingObjectPosition;", "(Lahb;Lyz;Z)Lazu;"));

    putMapping1710(new Elem("net/minecraft/entity/Entity", "sa"));
    
    putMapping1710(new Elem("isCollidedHorizontally", "E", "field_70123_F", "Z"));
    
    putMapping1710(new Elem("motionY", "w", "field_70181_x", "D"));
    
    putMapping1710(new Elem("onEntityUpdate", "C", "func_70030_z", "()V"));

    putMapping1710(new Elem("onUpdate", "h", "func_70071_h_", "()V"));

    putMapping1710(new Elem("net/minecraft/entity/EntityLivingBase", "sv"));

    putMapping1710(new Elem("net/minecraft/entity/player/EntityPlayer", "yz"));
    
    putMapping1710(new Elem("moveEntityWithHeading", "e", "func_70612_e", "(FF)V"));
    
    putMapping1710(new Elem("isPlayerSleeping", "bm", "func_70608_bn", "()Z"));
    
    putMapping1710(new Elem("getPosition", "l", "func_70666_h", "(F)Lnet/minecraft/util/Vec3;", "(F)Lazw;"));

    putMapping1710(new Elem("net/minecraft/client/entity/EntityPlayerSP", "blk"));
    
    putMapping1710(new Elem("onLivingUpdate", "e", "func_70636_d", "()V"));
    
    putMapping1710(new Elem("isRidingHorse", "u", "func_110317_t", "Z"));

    putMapping1710(new Elem("updateAITasks", "bn", "func_70619_bc", "()V"));

    putMapping1710(new Elem("orientCamera", "h", "func_78467_g", "(F)V"));
    
    putMapping1710(new Elem("updateFogColor", "j", "func_78466_h", "(F)V"));
    
    putMapping1710(new Elem("mc", "t", "field_78531_r", null));

    putMapping1710(new Elem("gameSettings", "u", "field_71474_y", null));

    putMapping1710(new Elem("net/minecraft/client/settings/GameSettings", "bbj"));
    
    putMapping1710(new Elem("debugCamEnable", "aC", "field_74325_U", "Z"));
    
    putMapping1710(new Elem("thirdPersonView", "aw", "field_74320_O", null));

    putMapping1710(new Elem("net/minecraft/client/renderer/entity/RendererLivingEntity", "boh"));
    
    putMapping1710(new Elem("rotateCorpse", "a", "func_77043_a", "(Lnet/minecraft/entity/EntityLivingBase;FFF)V", "(Lsv;FFF)V"));
    
    putMapping1710(new Elem("doRender", "a", "func_76986_a", "(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", "(Lsv;DDDFF)V"));

    putMapping1710(new Elem("func_151509_a", "a", "func_151509_a", "(Lnet/minecraft/network/PacketBuffer;)V", "(Let;)V"));
    
    putMapping1710(new Elem("readWatchedListFromPacketBuffer", "b", "func_151508_b", "(Lnet/minecraft/network/PacketBuffer;)Ljava/util/List;", "(Let;)Ljava/util/List;"));

    putMapping1710(new Elem("processPlayer", "a", "func_147347_a", "(Lnet/minecraft/network/play/client/C03PacketPlayer;)V", "(Ljd;)V"));
    
    putMapping1710(new Elem("kickPlayerFromServer", "c", "func_147360_c", "(Ljava/lang/String;)V"));

    putMapping1710(new Elem("readPacketData", "a", "func_148837_a", "(Lnet/minecraft/network/PacketBuffer;)V", "(Let;)V"));
    
    putMapping1710(new Elem("writePacketData", "b", "func_148840_b", "(Lnet/minecraft/network/PacketBuffer;)V", "(Let;)V"));
    
    putMapping1710(new Elem("readInt", "readInt", "readInt", "()I", "()I"));
    
    putMapping1710(new Elem("readByte", "readByte", "readByte", "()B", "()B"));
    
    putMapping1710(new Elem("writeByte", "writeByte", "writeByte", "(I)Lio/netty/buffer/ByteBuf;", "(I)Lio/netty/buffer/ByteBuf;"));
    
    putMapping1710(new Elem("writeInt", "writeInt", "writeInt", "(I)Lio/netty/buffer/ByteBuf;", "(I)Lio/netty/buffer/ByteBuf;"));

    putMapping1710(new Elem("onBlockAdded", "b", "func_149726_b", "(Lnet/minecraft/world/World;III)V", "(Lahb;III)V"));

    putMapping1710(new Elem("instance", "a", "field_78398_a", null));

    putMapping1710(new Elem("blockBlocksFlow", "n", "func_72208_o", "(Lnet/minecraft/world/World;III)Z", "(Labw;III)Z"));
    putMapping1710(new Elem("liquidCanDisplaceBlock", "o", "func_72207_p", "(Lnet/minecraft/world/World;III)Z", "(Labw;III)Z"));
    putMapping1710(new Elem("getRenderType", "d", "func_71857_b", "()I"));
  }
}
