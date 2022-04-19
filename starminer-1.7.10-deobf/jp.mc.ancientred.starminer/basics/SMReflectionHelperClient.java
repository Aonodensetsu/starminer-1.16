package jp.mc.ancientred.starminer.basics;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;

public class SMReflectionHelperClient
{
  public static final String[] FIELD_NAME_chunkListing = new String[] { "chunkListing", "field_73237_c" };
  public static final String[] FIELD_NAME_isGapLightingUpdated = new String[] { "isGapLightingUpdated", "field_76650_s" };
  public static final String[] FIELD_NAME_queuedLightChecks = new String[] { "queuedLightChecks", "field_76649_t" };
  public static Field field_starGLCallList;
  public static Field field_remainingHighlightTicks;
  public static Field field_tesselator_instance;
  
  public static int getStarGLCallList(RenderGlobal renderGlobal) {
    try {
      if (field_starGLCallList == null) {
        field_starGLCallList = ReflectionHelper.findField(RenderGlobal.class, new String[] { "starGLCallList", "field_72772_v" });
      }
      return field_starGLCallList.getInt(renderGlobal);
    } catch (Exception ex) {
      ex.printStackTrace();
      
      return -1;
    } 
  }

  public static void setRemainingHighlightTicks(GuiIngame ingameGUI, int value) {
    try {
      if (field_remainingHighlightTicks == null) {
        field_remainingHighlightTicks = ReflectionHelper.findField(GuiIngame.class, new String[] { "remainingHighlightTicks", "field_92017_k" });
      }
      field_remainingHighlightTicks.setInt(ingameGUI, value);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }

  public static void setWrappedTesselator(Tessellator instance) {
    try {
      if (field_tesselator_instance == null) {
        field_tesselator_instance = ReflectionHelper.findField(Tessellator.class, new String[] { "instance", "field_78398_a" });
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field_tesselator_instance, field_tesselator_instance.getModifiers() & 0xFFFFFFEF);
      } 
      
      field_tesselator_instance.set(null, instance);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
}
