package jp.mc.ancientred.starminer.basics.common;

import net.minecraft.util.Vec3;

public class VecUtils {
  public static final Vec3 createVec3(double x, double y, double z) {
    return Vec3.createVectorHelper(x, y, z);
  }
}
