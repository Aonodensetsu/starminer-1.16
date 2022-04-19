package jp.mc.ancientred.starminer.core;

public class CoreConfig
{
  public static final String CONFIG_CAT_SERVERSIDE = "core_server_side_property";
  public static final String CONFIG_CAT_CLIENTSIDE = "core_client_side_property";
  public static boolean skipIllegalStanceCheck = true;
  public static int illegalGStateTickToCheck = 100;

  
  public static boolean showUnsynchronizedWarning;
  
  public static int unsynchronizedWarnToKick;
  
  public static int gravityUpdateFreq = 100;
}
