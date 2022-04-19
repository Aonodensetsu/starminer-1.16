package jp.mc.ancientred.starminer.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import java.io.File;
import java.util.Map;

@MCVersion("1.7.10")
public class SMPlugin
  implements IFMLLoadingPlugin
{
  public static boolean RUNTIME_DEOBF = false;
  public static File forgeLocation;
  
  public String getAccessTransformerClass() {
    return "jp.mc.ancientred.starminer.core.SMAccessTransformer";
  }

  public String[] getASMTransformerClass() {
    return new String[] { "jp.mc.ancientred.starminer.core.SMTransformer" };
  }

  public String getModContainerClass() {
    return "jp.mc.ancientred.starminer.core.SMCoreModContainer";
  }

  public String getSetupClass() {
    return null;
  }

  public void injectData(Map<String, Object> data) {
    RUNTIME_DEOBF = ((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
    forgeLocation = (File)data.get("coremodLocation");
  }
}
