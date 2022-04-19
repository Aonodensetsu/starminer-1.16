package jp.mc.ancientred.starminer.core;

import jp.mc.ancientred.starminer.core.entity.ExtendedPropertyGravity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class TransformUtils {
  public static Vec3 fixEyePositionByGravityClient(Entity entity, Vec3 vec3) {
    ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity(entity);
    float fixHeight = entity.yOffset - entity.width / 2.0F;
    vec3.xCoord -= (fixHeight * gravity.gravityDirection.shiftEyeX);
    vec3.yCoord -= (fixHeight * gravity.gravityDirection.shiftEyeY);
    vec3.zCoord -= (fixHeight * gravity.gravityDirection.shiftEyeZ);
    return vec3;
  }
  public static Vec3 fixEyePositionByGravityServer(EntityPlayer entity, Vec3 vec3) {
    ExtendedPropertyGravity gravity = ExtendedPropertyGravity.getExtendedPropertyGravity((Entity)entity);
    float fixHeight = entity.getEyeHeight() - entity.width / 2.0F;
    vec3.xCoord -= (fixHeight * gravity.gravityDirection.shiftEyeX);
    vec3.yCoord -= (fixHeight * gravity.gravityDirection.shiftEyeY);
    vec3.zCoord -= (fixHeight * gravity.gravityDirection.shiftEyeZ);
    return vec3;
  }
}
