package dev.bluecom.starminer.basics.common;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.bluecom.starminer.api.GravityCapability;
import dev.bluecom.starminer.api.GravityDirection;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.api.IGravityCapability;
import dev.bluecom.starminer.basics.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonForgeEventHandler {
	@SubscribeEvent
	public void playerLogin(PlayerLoggedInEvent event) {
		if (!event.getPlayer().level.isClientSide) {
			CommonNetworkHandler.sendToClient(event.getPlayer().getCapability(GravityProvider.GRAVITY).orElseThrow(() -> new IllegalAccessError("Player should always have capability")).getPacket(), (ServerPlayerEntity) event.getPlayer());
		}
	}
	
	@SubscribeEvent
	public void playerDimension(PlayerChangedDimensionEvent event) {
		if (!event.getPlayer().level.isClientSide) {
			CommonNetworkHandler.sendToClient(event.getPlayer().getCapability(GravityProvider.GRAVITY).orElseThrow(() -> new IllegalAccessError("Player should always have capability")).getPacket(), (ServerPlayerEntity) event.getPlayer());
		}
	}
	
	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		if (!event.getPlayer().level.isClientSide) {
			CommonNetworkHandler.sendToClient(event.getPlayer().getCapability(GravityProvider.GRAVITY).orElseThrow(() -> new IllegalAccessError("Player should always have capability")).getPacket(), (ServerPlayerEntity) event.getPlayer());
		}
	}
	
	@SubscribeEvent
	public void playerTrack(PlayerEvent.StartTracking event) {
		if (!event.getPlayer().level.isClientSide) {
			if (event.getTarget() instanceof LivingEntity) {
				IGravityCapability cap = event.getTarget().getCapability(GravityProvider.GRAVITY).orElse(null);
				CommonNetworkHandler.sendToClient(cap.getPacket(), (ServerPlayerEntity) event.getPlayer());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void cameraSetup(EntityViewRenderEvent.CameraSetup event) {
		Minecraft minecraft = Minecraft.getInstance();
		Entity renderViewEntity = minecraft.getCameraEntity();
		if (renderViewEntity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) renderViewEntity;
			IGravityCapability capability = GravityCapability.getGravityProp(player);
			GravityDirection gravityDirection = capability.getGravityDir();

			float transitionRollAmount = 0;
			int timeoutTicks = capability.getTicks();
			double effectiveTimeoutTicks = timeoutTicks - event.getRenderPartialTicks();

			double interpolatedPitch = (player.yRotO + (player.yRot - player.yRotO) * event.getRenderPartialTicks());
			double interpolatedYaw = (player.xRotO + (player.xRot - player.xRotO) * event.getRenderPartialTicks());

			interpolatedPitch = interpolatedPitch % 360;
			interpolatedYaw = interpolatedYaw % 360;

			Vector3d interpolatedLookVec = Vector3dHelper.getPreciseVectorForRotation(interpolatedPitch, interpolatedYaw);
			Vector3d relativeInterpolatedLookVec = GravityDirection.turnWayForNormal(gravityDirection).adjustLookVec(interpolatedLookVec);
			double[] precisePitchAndYawFromVector = Vector3dHelper.getPrecisePitchAndYawFromVector(relativeInterpolatedLookVec);

			double relativeInterpolatedPitch = precisePitchAndYawFromVector[Vector3dHelper.PITCH];
			double relativeInterpolatedYaw = precisePitchAndYawFromVector[Vector3dHelper.YAW];

			double xTranslation = 0;
			double yTranslation = 0;
			double zTranslation = 0;

			if (timeoutTicks != 0 && effectiveTimeoutTicks > ConfigHandler.transitionAnimationRotationEnd) {
				double rotationAngle;

				if (!capability.hasTransitionAngle()) {
					double yaw = player.xRot;
					double pitch = player.yRot;

					Vector3d absoluteLookVec = Vector3dHelper.getPreciseVectorForRotation(pitch, yaw);
					Vector3d relativeCurrentLookVector = GravityDirection.turnWayForNormal(gravityDirection).adjustLookVec(absoluteLookVec);
					double[] pitchAndYawRelativeCurrentLook = Vector3dHelper.getPrecisePitchAndYawFromVector(relativeCurrentLookVector);
					double relativeCurrentPitch = pitchAndYawRelativeCurrentLook[Vector3dHelper.PITCH] - 90;
					double relativeCurrentYaw = pitchAndYawRelativeCurrentLook[Vector3dHelper.YAW];
					Vector3d relativeCurrentUpVector = Vector3dHelper.getPreciseVectorForRotation(relativeCurrentPitch, relativeCurrentYaw);
					Vector3d absoluteCurrentUpVector = gravityDirection.adjustLookVec(relativeCurrentUpVector);
					GravityDirection g = capability.getPreviousDir();
					Vector3d relativePrevLookVector = GravityDirection.turnWayForNormal(g).adjustLookVec(absoluteLookVec);
					double[] pitchAndYawRelativePrevLook = Vector3dHelper.getPrecisePitchAndYawFromVector(relativePrevLookVector);
					double relativePrevPitch = pitchAndYawRelativePrevLook[Vector3dHelper.PITCH] - 90;
					double relativePrevYaw = pitchAndYawRelativePrevLook[Vector3dHelper.YAW];
					Vector3d relativePrevUpVector = Vector3dHelper.getPreciseVectorForRotation(relativePrevPitch, relativePrevYaw);
					Vector3d absolutePrevUpVector = capability.getPreviousDir().adjustLookVec(relativePrevUpVector);

					rotationAngle = (180d / Math.PI) * Math.atan2(absoluteCurrentUpVector.cross(absolutePrevUpVector).dot(absoluteLookVec), absoluteCurrentUpVector.dot(absolutePrevUpVector));
					capability.setTransitionAngle(rotationAngle);
				}
				else {
					rotationAngle = capability.getTransitionAngle();
				}

				double numerator = GravityCapability.UPDATE_AFTER_TICKS - effectiveTimeoutTicks;
				double denominator = ConfigHandler.transitionAnimationRotationLength;
				double multiplierZeroToOne = numerator / denominator;
				double multiplierOneToZero = 1 - multiplierZeroToOne;

				transitionRollAmount = (float)(rotationAngle * multiplierOneToZero);
				Vector3d eyePosChangeVector = capability.getEyePos();
				xTranslation = eyePosChangeVector.x * multiplierOneToZero;
				yTranslation = eyePosChangeVector.y * multiplierOneToZero;
				zTranslation = eyePosChangeVector.z * multiplierOneToZero;
				minecraft.levelRenderer.needsUpdate();
			}

			relativeInterpolatedPitch = relativeInterpolatedPitch % 360;
			relativeInterpolatedYaw = relativeInterpolatedYaw % 360;

			//GlStateManager._rotatef((float) relativeInterpolatedPitch, 1, 0, 0);
			//GlStateManager._rotatef((float) relativeInterpolatedYaw, 0, 1, 0);
			gravityDirection.runCameraTransformation();
			//GlStateManager._rotatef((float) -interpolatedYaw, 0, 1, 0);
			//GlStateManager._rotatef((float) -interpolatedPitch, 1, 0, 0);

			//event.setRoll(event.getRoll() + transitionRollAmount);
			//GlStateManager._rotatef(transitionRollAmount, 0, 0, 1);

			//GlStateManager._rotatef(event.getRoll(), 0, 0, 1);

			//GlStateManager._rotatef(event.getPitch(), 1, 0, 0);
			//GlStateManager._rotatef(event.getYaw(), 0, 1, 0);
			//GlStateManager._translated(xTranslation, yTranslation, zTranslation);
			//GlStateManager._rotatef(-event.getYaw(), 0, 1, 0);
			//GlStateManager._rotatef(-event.getPitch(), 1, 0, 0);

			//GlStateManager._rotatef(-event.getRoll(), 0, 0, 1);
		}
	}

	@SubscribeEvent
	public void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(new ResourceLocation(ModContainer.MODID, "gravity"), new GravityProvider((LivingEntity) event.getObject()));
		}
	}

	@SubscribeEvent
	public void tickCapabilityEntity(final LivingUpdateEvent event) {
		if (!event.getEntityLiving().level.isClientSide) {
			LazyOptional<IGravityCapability> gravity = event.getEntityLiving().getCapability(GravityProvider.GRAVITY);
			IGravityCapability cap = gravity.orElseThrow(() -> new IllegalAccessError("This should always exist."));
			cap.tick();
		}
	}
	
	//@SubscribeEvent
	//public void renderLivingPre(final RenderLivingEvent.Pre<?, ?> event) {
	//	if (event.getEntity() instanceof PlayerEntity) { //temporarily only overwrite player
	//		IGravityCapability grav = GravityCapability.getGravityProp(event.getEntity());
	//		if (grav.getAttracted()) {
	//			MatrixStack matrix = event.getMatrixStack();
	//			switch (grav.getGravityDir()) {
	//				case DOWN_TO_UP_YP:
	//					matrix.mulPose(Vector3f.XP.rotationDegrees(180));
	//					break;
	//				case EAST_TO_WEST_XN:
	//					matrix.mulPose(Vector3f.ZP.rotationDegrees(-90));
	//					break;
	//				case WEST_TO_EAST_XP:
	//					matrix.mulPose(Vector3f.ZP.rotationDegrees(90));
	//					break;
	//				case NORTH_TO_SOUTH_ZP:
	//					matrix.mulPose(Vector3f.XP.rotationDegrees(-90));
	//					break;
	//				case SOUTH_TO_NORTH_ZN:
	//					matrix.mulPose(Vector3f.XP.rotationDegrees(90));
	//					break;
	//			}
	//		}
	//	}
	//}
}
