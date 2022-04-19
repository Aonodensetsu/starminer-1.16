package jp.mc.ancientred.starminer.basics;

public class Config
{
  public static final String CONFIG_CAT_SERVERSIDE = "basics_server_side_property";
  public static final String CONFIG_CAT_CLIENTSIDE = "basics_client_side_property";
  public static final double TELEPORT_SPACE_POSY = -10.0D;
  public static final double TELEPORT_GROUND_POSY = 288.0D;
  public static double bazookaStartSpeed = 3.3D;
  
  public static boolean generateStars = true;
  
  public static boolean generateOres = false;
  
  public static long attractCheckTick = 8L;
  
  public static long maxStarRad = 48L;
  
  public static long maxGravityRad = 54L;
  
  public static boolean ticketFreeForTeleport = false;
  
  public static int naviLaserLength = 7;
  public static boolean enableFakeRotatorOnlyVannilaBlock = true;
  public static boolean hasShaderModInstalled = false;
}
