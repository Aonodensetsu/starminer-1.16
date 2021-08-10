package dev.bluecom.starminer.api;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.system.APIUtil;

public enum GravityDirection {
	UP_TO_DOWN_YN(1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, GravityConst.MATRIX_ROT_UP_TO_DOWN_I, GravityConst.MATRIX_ROT_UP_TO_DOWN_D, GravityConst.FORGE_SIDE_ROT_UP_TO_DOWN),
	DOWN_TO_UP_YP(1.0F, 0.0F, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, GravityConst.MATRIX_ROT_DOWN_TO_UP_I, GravityConst.MATRIX_ROT_DOWN_TO_UP_D, GravityConst.FORGE_SIDE_ROT_DOWN_TO_UP),
	EAST_TO_WEST_XN(0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5F, -1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, GravityConst.MATRIX_ROT_EAST_TO_WEST_I, GravityConst.MATRIX_ROT_EAST_TO_WEST_D, GravityConst.FORGE_SIDE_ROT_EAST_TO_WEST),
	WEST_TO_EAST_XP(0.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.5F, 1.0F, 1.0F, 0.0F, -1.0F, 0.0F, 0.0F, GravityConst.MATRIX_ROT_WEST_TO_EAST_I, GravityConst.MATRIX_ROT_WEST_TO_EAST_D, GravityConst.FORGE_SIDE_ROT_WEST_TO_EAST),
	NORTH_TO_SOUTH_ZP(1.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, -1.0F, GravityConst.MATRIX_ROT_NORTH_TO_SOUTH_I, GravityConst.MATRIX_ROT_NORTH_TO_SOUTH_D, GravityConst.FORGE_SIDE_ROT_NORTH_TO_SOUTH),
	SOUTH_TO_NORTH_ZN(1.0F, 0.0F, 0.0F, 0.0F, 1.0F, -0.5F, 0.0F, 0.0F, 1.0F, -1.0F, 0.0F, 0.0F, 1.0F, GravityConst.MATRIX_ROT_SOUTH_TO_NORTH_I, GravityConst.MATRIX_ROT_SOUTH_TO_NORTH_D, GravityConst.FORGE_SIDE_ROT_SOUTH_TO_NORTH);
		
	public float pitchRotDirX;
	public float pitchRotDirY;
	public float yawRotDirX;
	public float yawRotDirY;
	public float yawRotDirZ;
	public float rotX;
	public float rotZ;
	public float shiftEyeX;
	public float shiftEyeY;
	public float shiftEyeZ;
	public float shiftSneakX;
	public float shiftSneakY;
	public float shiftSneakZ;
	public int[] matrixRotationI;
	public double[] matrixRotationD;
	public int[] forgeSideRot;
	public int collideCheckExpandX;
	public int collideCheckExpandY;
	public int collideCheckExpandZ;
		
	GravityDirection(float argPitchRotDirX, float argPitchRotDirY, float argYawRotDirX, float argYawRotDirY, float argYawRotDirZ, float argRotX, float argRotZ, float argShiftEyeX, float argShiftEyeY, float argShiftEyeZ, float argShiftSneakX, float argShiftSneakY, float argShiftSneakZ, int[] argMatrixRotationI, double[] argMatrixRotationD, int[] argForgeSideRot) {
		this.pitchRotDirX = argPitchRotDirX;
		this.pitchRotDirY = argPitchRotDirY;
		this.yawRotDirX = argYawRotDirX;
		this.yawRotDirY = argYawRotDirY;
		this.yawRotDirZ = argYawRotDirZ;
		this.rotX = argRotX;
		this.rotZ = argRotZ;
		this.shiftEyeX = argShiftEyeX;
		this.shiftEyeY = argShiftEyeY;
		this.shiftEyeZ = argShiftEyeZ;
		this.shiftSneakX = argShiftSneakX;
		this.shiftSneakY = argShiftSneakY;
		this.shiftSneakZ = argShiftSneakZ;
		this.matrixRotationI = argMatrixRotationI;
		this.matrixRotationD = argMatrixRotationD;
		this.forgeSideRot = argForgeSideRot;
		this.collideCheckExpandX = -this.matrixRotationI[3];
		this.collideCheckExpandY = -this.matrixRotationI[4];
		this.collideCheckExpandZ = -this.matrixRotationI[5];
	}

	public static GravityDirection turnWayForNormal(GravityDirection gDir) {
		switch (gDir) {
			case DOWN_TO_UP_YP:
				return DOWN_TO_UP_YP;
			case EAST_TO_WEST_XN:
				return WEST_TO_EAST_XP;
			case WEST_TO_EAST_XP:
				return EAST_TO_WEST_XN;
			case NORTH_TO_SOUTH_ZP:
				return SOUTH_TO_NORTH_ZN;
			case SOUTH_TO_NORTH_ZN:
				return NORTH_TO_SOUTH_ZP;
			default:
				return UP_TO_DOWN_YN;
		} 
	}

	public GravityDirection turnWayForNormal() {
		return GravityDirection.turnWayForNormal(this);
	}

	public Vector3d rotateVec3(Vector3d vec3) {
		return rotateVec3(this, vec3);
	}
	public Vector3d rotateVec3At(Vector3d vec3, double centerX, double centerY, double centerZ) {
		return rotateVec3At(this, vec3, centerX, centerY, centerZ);
	}
	public Vector3d rotateVec3At(Vector3d vec3, Vector3d centerVec3) {
		return rotateVec3At(this, vec3, centerVec3);
	}
	
	public static Vector3d rotateVec3(GravityDirection dir, Vector3d vec3) {
		double x = vec3.x;
		double y = vec3.y;
		double z = vec3.z;
	
		return new Vector3d(
			x * dir.matrixRotationD[0] + y * dir.matrixRotationD[3] + z * dir.matrixRotationD[6], 
			x * dir.matrixRotationD[1] + y * dir.matrixRotationD[4] + z * dir.matrixRotationD[7], 
			x * dir.matrixRotationD[2] + y * dir.matrixRotationD[5] + z * dir.matrixRotationD[8]
		);
	}
	
	public static Vector3d rotateVec3At(GravityDirection dir, Vector3d vec3, Vector3d centerVec3) {
		return rotateVec3At(dir, centerVec3, centerVec3.x, centerVec3.y, centerVec3.z);
	}
	
	public static Vector3d rotateVec3At(GravityDirection dir, Vector3d vec3, double centerX, double centerY, double centerZ) {
		double x = vec3.x - centerX;
		double y = vec3.y - centerY;
		double z = vec3.z - centerZ;
	
		return new Vector3d(
			x * dir.matrixRotationD[0] + y * dir.matrixRotationD[3] + z * dir.matrixRotationD[6] + centerX,
			x * dir.matrixRotationD[1] + y * dir.matrixRotationD[4] + z * dir.matrixRotationD[7] + centerY,
			x * dir.matrixRotationD[2] + y * dir.matrixRotationD[5] + z * dir.matrixRotationD[8] + centerZ
		);
	}

	public double[] rotateXYZAt(double[] retVal, double argX, double argY, double argZ, double centerX, double centerY, double centerZ) {
		return rotateXYZAt(this, retVal, argX, argY, argZ, centerX, centerY, centerZ);
	}
	public static double[] rotateXYZAt(GravityDirection dir, double[] retVal, double argX, double argY, double argZ, double centerX, double centerY, double centerZ) {
		double x = argX - centerX;
		double y = argY - centerY;
		double z = argZ - centerZ;
	
		retVal[0] = x * dir.matrixRotationD[0] + y * dir.matrixRotationD[3] + z * dir.matrixRotationD[6] + centerX;
		retVal[1] = x * dir.matrixRotationD[1] + y * dir.matrixRotationD[4] + z * dir.matrixRotationD[7] + centerY;
		retVal[2] = x * dir.matrixRotationD[2] + y * dir.matrixRotationD[5] + z * dir.matrixRotationD[8] + centerZ;

		return retVal;
	}
	public float[] rotateXYZAt(float[] retVal, float argX, float argY, float argZ, float centerX, float centerY, float centerZ) {
		return rotateXYZAt(this, retVal, argX, argY, argZ, centerX, centerY, centerZ);
	}
	public static float[] rotateXYZAt(GravityDirection dir, float[] retVal, float argX, float argY, float argZ, float centerX, float centerY, float centerZ) {
		float x = argX - centerX;
		float y = argY - centerY;
		float z = argZ - centerZ;
	
		retVal[0] = x * (float)dir.matrixRotationD[0] + y * (float)dir.matrixRotationD[3] + z * (float)dir.matrixRotationD[6] + centerX;
		retVal[1] = x * (float)dir.matrixRotationD[1] + y * (float)dir.matrixRotationD[4] + z * (float)dir.matrixRotationD[7] + centerY;
		retVal[2] = x * (float)dir.matrixRotationD[2] + y * (float)dir.matrixRotationD[5] + z * (float)dir.matrixRotationD[8] + centerZ;
	
		return retVal;
	}
	public int[] rotateXYZAt(int[] retVal, int argX, int argY, int argZ, int centerX, int centerY, int centerZ) {
		return rotateXYZAt(this, retVal, argX, argY, argZ, centerX, centerY, centerZ);
	}
	public static int[] rotateXYZAt(GravityDirection dir, int[] retVal, int argX, int argY, int argZ, int centerX, int centerY, int centerZ) {
		int x = argX - centerX;
		int y = argY - centerY;
		int z = argZ - centerZ;
	
		retVal[0] = x * dir.matrixRotationI[0] + y * dir.matrixRotationI[3] + z * dir.matrixRotationI[6] + centerX;
		retVal[1] = x * dir.matrixRotationI[1] + y * dir.matrixRotationI[4] + z * dir.matrixRotationI[7] + centerY;
		retVal[2] = x * dir.matrixRotationI[2] + y * dir.matrixRotationI[5] + z * dir.matrixRotationI[8] + centerZ;

		return retVal;
	}
	
	public AxisAlignedBB rotateAABBAt(AxisAlignedBB aabb, int x, int y, int z) {
		return rotateAABBAt(this, aabb, x, y, z);
	}
	public static AxisAlignedBB rotateAABBAt(GravityDirection dir, AxisAlignedBB aabb, int x, int y, int z) {
		return rotateAABBAt(dir, aabb, x + 0.5D, y + 0.5D, z + 0.5D);
	}
	public AxisAlignedBB rotateAABBAt(AxisAlignedBB aabb, double roatCenterX, double roatCenterY, double roatCenterZ) {
		return rotateAABBAt(this, aabb, roatCenterX, roatCenterY, roatCenterZ);
	}
	
	public static AxisAlignedBB rotateAABBAt(GravityDirection dir, AxisAlignedBB aabb, double roatCenterX, double roatCenterY, double roatCenterZ) {
		double aabbminX = aabb.minX - roatCenterX;
		double aabbminY = aabb.minY - roatCenterY;
		double aabbminZ = aabb.minZ - roatCenterZ;
		double aabbmaxX = aabb.maxX - roatCenterX;
		double aabbmaxY = aabb.maxY - roatCenterY;
		double aabbmaxZ = aabb.maxZ - roatCenterZ;
		
		double x1 = aabbminX * dir.matrixRotationD[0] + aabbminY * dir.matrixRotationD[3] + aabbminZ * dir.matrixRotationD[6] + roatCenterX;
		double y1 = aabbminX * dir.matrixRotationD[1] + aabbminY * dir.matrixRotationD[4] + aabbminZ * dir.matrixRotationD[7] + roatCenterY;
		double z1 = aabbminX * dir.matrixRotationD[2] + aabbminY * dir.matrixRotationD[5] + aabbminZ * dir.matrixRotationD[8] + roatCenterZ;
		double x2 = aabbmaxX * dir.matrixRotationD[0] + aabbmaxY * dir.matrixRotationD[3] + aabbmaxZ * dir.matrixRotationD[6] + roatCenterX;
		double y2 = aabbmaxX * dir.matrixRotationD[1] + aabbmaxY * dir.matrixRotationD[4] + aabbmaxZ * dir.matrixRotationD[7] + roatCenterY;
		double z2 = aabbmaxX * dir.matrixRotationD[2] + aabbmaxY * dir.matrixRotationD[5] + aabbmaxZ * dir.matrixRotationD[8] + roatCenterZ;

		return new AxisAlignedBB(
			Math.max(x1, x2),
			Math.max(y1, y2),
			Math.max(z1, z2),
			Math.min(x1, x2),
			Math.min(y1, y2),
			Math.min(z1, z2)
		);
	}

	public Vector3d adjustLookVec(Vector3d input) {
		double[] d = this.adjustXYZValues(input.x, input.y, input.z);
		return new Vector3d(d[0], d[1], d[2]);
	}

	public double[] adjustXYZValues(double x, double y, double z) {
		switch (this) {
			case DOWN_TO_UP_YP:
				return new double[]{-x, -y, z};
			case EAST_TO_WEST_XN:
				return new double[]{y, -x, z};
			case WEST_TO_EAST_XP:
				return new double[]{-y, x, z};
			case NORTH_TO_SOUTH_ZP:
				return new double[]{x, z, -y};
			case SOUTH_TO_NORTH_ZN:
				return new double[]{x, -z, y};
			default:
				return new double[]{x, y, z};
		}
	}

	public void runCameraTransformation() {
		int x = 0, y = 0, z = 0;
		switch (this) {
			case DOWN_TO_UP_YP:
				z = 180;
				break;
			case SOUTH_TO_NORTH_ZN:
				x = 90;
				break;
			case WEST_TO_EAST_XP:
				z = 90;
				break;
			case NORTH_TO_SOUTH_ZP:
				x = -90;
				break;
			case EAST_TO_WEST_XN:
				z = -90;
				break;
		}
		if (x != 0) {
			GlStateManager._rotatef(x, 1, 0, 0);
		}
		if (y != 0) {
			GlStateManager._rotatef(y, 0, 1, 0);
		}
		if (z != 0) {
			GlStateManager._rotatef(z, 0, 0, 1);
		}
	}

	public void postModifyPlayerOnGravityChange(PlayerEntity player, GravityDirection gravity, Vector3d eyes) {
		gravity.returnCentreOfGravityToPlayerPos(player);
		this.offsetCentreOfGravityFromPlayerPos(player);
		this.setBoundingBoxAndPositionOnGravityChange(player, gravity, eyes);
	}

	private void setBoundingBoxAndPositionOnGravityChange(PlayerEntity player, GravityDirection oldGravity, Vector3d oldEyePos) {
		AxisAlignedBB axisAlignedBB = this.getGravityAdjustedAABB(player);
		player.resetPos();
		if (!player.level.noCollision(axisAlignedBB)) {
			GravityDirection directionToTry;
			double distanceToMove;
			if (player.getBbHeight() > player.getBbWidth()) {
				distanceToMove = (player.getBbHeight() - player.getBbWidth())/2;
				directionToTry = this;
			} else if (player.getBbHeight() < player.getBbWidth()) {
				distanceToMove = (player.getBbHeight() - player.getBbWidth())/2;
				directionToTry = oldGravity;
			} else {
				player.setBoundingBox(axisAlignedBB);
				return;
			}
			double[] adjustedMovement = directionToTry.adjustXYZValues(0, distanceToMove, 0);
			adjustedMovement = this.turnWayForNormal().adjustXYZValues(adjustedMovement[0], adjustedMovement[1], adjustedMovement[2]);
			AxisAlignedBB secondTry = axisAlignedBB.move(adjustedMovement[0], adjustedMovement[1], adjustedMovement[2]);
			if (!player.level.noCollision(secondTry)) {
				AxisAlignedBB thirdTry = axisAlignedBB.move(-adjustedMovement[0], -adjustedMovement[1], -adjustedMovement[2]);
				if (!player.level.noCollision(thirdTry)) {
					player.setBoundingBox(axisAlignedBB);
					player.resetPos();
					Vector3d newEyePos = player.position().add(0, player.getEyeHeight(), 0);
					Vector3d eyesDifference = oldEyePos.subtract(newEyePos);
					Vector3d adjustedDifference = this.turnWayForNormal().adjustLookVec(eyesDifference);
					AxisAlignedBB givenUp = axisAlignedBB.move(adjustedDifference.x, adjustedDifference.y, adjustedDifference.z);
					double relativeBottomOfBB = GravityAlignedBB.getRelativeBottom(givenUp);
					long rounded = Math.round(relativeBottomOfBB);
					double difference = rounded - relativeBottomOfBB;
					givenUp = givenUp.move(0, difference + 1, 0);
					if (player.level.noCollision(givenUp)) {
						givenUp = givenUp.move(0, -1, 0);
					}
					axisAlignedBB = givenUp;
				} else {
					axisAlignedBB = thirdTry;
				}
			} else {
				axisAlignedBB = secondTry;
			}
		}
		player.setBoundingBox(axisAlignedBB);
		player.resetPos();
	}

	private AxisAlignedBB getGravityAdjustedAABB(PlayerEntity player) {
		double widthOver2;
		float eyeHeight;
		switch (this) {
			case DOWN_TO_UP_YP:
				widthOver2 = player.getBbWidth() / 2f;
				return new GravityAlignedBB(GravityCapability.getGravityProp(player).getGravityDir(), player.position().x - widthOver2, player.position().y - player.getBbHeight(), player.position().z - widthOver2, player.position().x + widthOver2, player.position().y, player.position().z + widthOver2);
			case SOUTH_TO_NORTH_ZN:
				widthOver2 = player.getBbWidth() / 2f;
				eyeHeight = player.getEyeHeight(Pose.STANDING);
				return new GravityAlignedBB(GravityCapability.getGravityProp(player).getGravityDir(), player.position().x - widthOver2, player.position().y - widthOver2, player.position().z - eyeHeight, player.position().x + widthOver2, player.position().y + widthOver2, player.position().z + (player.getBbHeight() - eyeHeight));
			case WEST_TO_EAST_XP:
				widthOver2 = player.getBbWidth() / 2f;
				eyeHeight = player.getEyeHeight(Pose.STANDING);
				return new GravityAlignedBB(GravityCapability.getGravityProp(player).getGravityDir(), player.position().x - (player.getBbHeight() - eyeHeight), player.position().y - widthOver2, player.position().z - widthOver2, player.position().x + eyeHeight, player.position().y + widthOver2, player.position().z + widthOver2);
			case NORTH_TO_SOUTH_ZP:
				widthOver2 = player.getBbWidth() / 2f;
				eyeHeight = player.getEyeHeight(Pose.STANDING);
				return new GravityAlignedBB(GravityCapability.getGravityProp(player).getGravityDir(), player.position().x - widthOver2, player.position().y - widthOver2, player.position().z - (player.getBbHeight() - eyeHeight), player.position().x + widthOver2, player.position().y + widthOver2, player.position().z + eyeHeight);
			case EAST_TO_WEST_XN:
				widthOver2 = player.getBbWidth() / 2f;
				eyeHeight = player.getEyeHeight(Pose.STANDING);
				return new GravityAlignedBB(GravityCapability.getGravityProp(player).getGravityDir(), player.position().x - eyeHeight, player.position().y - widthOver2, player.position().z - widthOver2, player.position().x + (player.getBbHeight() - eyeHeight), player.position().y + widthOver2, player.position().z + widthOver2);
			default:
				widthOver2 = player.getBbWidth() / 2f;
				return new GravityAlignedBB(GravityCapability.getGravityProp(player).getGravityDir(), player.position().x - widthOver2, player.position().y, player.position().z - widthOver2, player.position().x + widthOver2, player.position().y + player.getBbHeight(), player.position().z + widthOver2);
		}
	}

	private void offsetCentreOfGravityFromPlayerPos(PlayerEntity player) {
		switch (this) {
			case DOWN_TO_UP_YP:
				player.setPos(player.position().x, player.position().y+player.getBbHeight()/2, player.position().z);
			case UP_TO_DOWN_YN:
				player.setPos(player.position().x, player.position().y-player.getBbHeight()/2, player.position().z);
			case SOUTH_TO_NORTH_ZN:
				player.setPos(player.position().x, player.position().y, player.position().z+(player.getEyeHeight(Pose.STANDING)-player.getBbHeight()/2));
			case WEST_TO_EAST_XP:
				player.setPos(player.position().x-(player.getEyeHeight(Pose.STANDING)-player.getBbHeight()/2), player.position().y, player.position().z);
			case EAST_TO_WEST_XN:
				player.setPos(player.position().x+(player.getEyeHeight(Pose.STANDING)-player.getBbHeight()/2), player.position().y, player.position().z);
			case NORTH_TO_SOUTH_ZP:
				player.setPos(player.position().x, player.position().y, player.position().z-(player.getEyeHeight(Pose.STANDING)-player.getBbHeight()/2));
		}
	}

	private void returnCentreOfGravityToPlayerPos(PlayerEntity player) {
		switch (this) {
			case DOWN_TO_UP_YP:
				player.setPos(player.position().x, player.position().y-player.getBbHeight()/2, player.position().z);
			case UP_TO_DOWN_YN:
				player.setPos(player.position().x, player.position().y+player.getBbHeight()/2, player.position().z);
			case SOUTH_TO_NORTH_ZN:
				player.setPos(player.position().x, player.position().y, player.position().z-(player.getEyeHeight(Pose.STANDING)-player.getBbHeight()/2));
			case WEST_TO_EAST_XP:
				player.setPos(player.position().x+(player.getEyeHeight(Pose.STANDING)-player.getBbHeight()/2), player.position().y, player.position().z);
			case EAST_TO_WEST_XN:
				player.setPos(player.position().x-(player.getEyeHeight(Pose.STANDING)-player.getBbHeight()/2), player.position().y, player.position().z);
			case NORTH_TO_SOUTH_ZP:
				player.setPos(player.position().x, player.position().y, player.position().z+(player.getEyeHeight(Pose.STANDING)-player.getBbHeight()/2));
		}
	}
}
