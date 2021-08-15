package dev.bluecom.starminer.basics.common;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class Vector3dHelper {
    private static final double PI_OVER_180 = Math.PI / 180d;
    private static final double ONE_HUNDRED_EIGHTY_OVER_PI = 180d / Math.PI;
    private static final float ONE_HUNDRED_EIGHTY_OVER_PI_FLOAT = (float)(180d / Math.PI);

    public static double getAbsolutePitchPrecise(Vector3d lookVec) {
        return -(Math.asin(lookVec.y) * (180D / Math.PI));
    }

    public static double getAbsoluteYawPrecise(Vector3d lookVec) {
        return (Math.atan2(-lookVec.x, lookVec.z) * (180D / Math.PI));
    }

    public static double[] getFastPitchAndYawFromVector(Vector3d xyz) {
        double pitch = -Math.asin(xyz.y) * ONE_HUNDRED_EIGHTY_OVER_PI;
        double yaw = MathHelper.atan2(-xyz.x, xyz.z) * ONE_HUNDRED_EIGHTY_OVER_PI;
        return new double[]{pitch, yaw};
    }

    public static Vector3d getFastUpwardsVector(float pitch, float yaw) {
        return getFastVectorForRotation(pitch + 90f, yaw);
    }

    public static Vector3d getFastVectorForRotation(float pitch, float yaw) {
        return Vector3d.directionFromRotation(pitch, yaw);
    }

    public static float getPitch(Vector3d lookVec) {
        return (float)-(Math.asin(lookVec.y) * (180D / Math.PI));
    }

    public static double[] getPrecisePitchAndYawFromVector(Vector3d xyz) {
        double pitch = -(Math.asin(xyz.y) * ONE_HUNDRED_EIGHTY_OVER_PI);
        double yaw = Math.atan2(-xyz.x, xyz.z) * ONE_HUNDRED_EIGHTY_OVER_PI;
        return new double[]{pitch, yaw};
    }

    public static Vector3d getPreciseVectorForRotation(double pitch, double yaw) {
        double f = Math.cos(-yaw * PI_OVER_180 - Math.PI);
        double f1 = Math.sin(-yaw * PI_OVER_180 - Math.PI);
        double f2 = -Math.cos(-pitch * PI_OVER_180);
        double f3 = Math.sin(-pitch * PI_OVER_180);
        return new Vector3d((f1 * f2), f3, (f * f2));
    }

    public static float getYaw(Vector3d lookVec) {
        return (float)(Math.atan2(-lookVec.x, lookVec.z) * (180D / Math.PI));
    }
}
