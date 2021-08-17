package dev.bluecom.starminer.basics.common;

import dev.bluecom.starminer.api.GravityCapability;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.api.IGravityCapability;
import dev.bluecom.starminer.api.camera.CameraEntity;
import dev.bluecom.starminer.api.util.GravityDirection;
import dev.bluecom.starminer.api.util.Vector3dHelper;
import dev.bluecom.starminer.basics.ModContainer;
import dev.bluecom.starminer.basics.network.CommonNetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class CommonForgeEventHandler {

	@SubscribeEvent
	public void playerLogin(PlayerLoggedInEvent event) {
		if (!event.getPlayer().level.isClientSide) {
			CommonNetworkHandler.sendToClient(event.getPlayer().getCapability(GravityProvider.GRAVITY).orElseThrow(() -> new IllegalAccessError("Player should always have capability")).getPacket(), (ServerPlayerEntity) event.getPlayer());

			//PlayerEntity player = event.getPlayer();
			//CameraEntity cam = new CameraEntity(player);
			//player.level.addFreshEntity(cam);
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

	//@SubscribeEvent
	public void cameraSetup(EntityViewRenderEvent.CameraSetup event) {
		Minecraft instance = Minecraft.getInstance();
		if (instance.getCameraEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) instance.getCameraEntity();
			GravityCapability cap = GravityCapability.getGravityProp(player);
			GravityDirection dir = cap.getGravityDir();

			float transitionRollAmount = 0;
			int timeoutTicks = cap.getTicks();
			double effectiveTimeoutTicks = timeoutTicks - event.getRenderPartialTicks();
			double interpolatedPitch = (player.xRotO + (player.xRot - player.xRotO) * event.getRenderPartialTicks());
			double interpolatedYaw = (player.yRotO + (player.yRot - player.yRotO) * event.getRenderPartialTicks());
			interpolatedPitch %= 360;
			interpolatedYaw %= 360;
			Vector3d interpolatedLookVec = Vector3dHelper.getPreciseVectorForRotation(interpolatedPitch, interpolatedYaw);
			Vector3d relativeInterpolatedLookVec = dir.turnWayForNormal().adjustLookVec(interpolatedLookVec);
			double[] relativeInterpolatedPitchAndYaw = Vector3dHelper.getPrecisePitchAndYawFromVector(relativeInterpolatedLookVec);
			double relativeInterpolatedPitch = relativeInterpolatedPitchAndYaw[0];
			double relativeInterpolatedYaw = relativeInterpolatedPitchAndYaw[1];
			double xTranslation = 0;
			double yTranslation = 0;
			double zTranslation = 0;

			if (timeoutTicks != 0 && effectiveTimeoutTicks > CommonConfigHandler.transitionAnimationRotationEnd) {
				double rotationAngle;
				if (!cap.hasTransitionAngle()) {
					double yaw = player.yRot;
					double pitch = player.xRot;
					Vector3d absoluteLookVec = Vector3dHelper.getPreciseVectorForRotation(pitch, yaw);
					Vector3d relativeCurrentLookVector = dir.turnWayForNormal().adjustLookVec(absoluteLookVec);
					double[] pitchAndYawRelativeCurrentLook = Vector3dHelper.getPrecisePitchAndYawFromVector(relativeCurrentLookVector);
					double relativeCurrentPitch = pitchAndYawRelativeCurrentLook[0] - 90;
					double relativeCurrentYaw = pitchAndYawRelativeCurrentLook[1];
					Vector3d relativeCurrentUpVector = Vector3dHelper.getPreciseVectorForRotation(relativeCurrentPitch, relativeCurrentYaw);
					Vector3d absoluteCurrentUpVector = dir.adjustLookVec(relativeCurrentUpVector);
					Vector3d relativePrevLookVector = cap.getPreviousDir().turnWayForNormal().adjustLookVec(absoluteLookVec);
					double[] pitchAndYawRelativePrevLook = Vector3dHelper.getPrecisePitchAndYawFromVector(relativePrevLookVector);
					double relativePrevPitch = pitchAndYawRelativePrevLook[0] - 90;
					double relativePrevYaw = pitchAndYawRelativePrevLook[1];
					Vector3d relativePrevUpVector = Vector3dHelper.getPreciseVectorForRotation(relativePrevPitch, relativePrevYaw);
					Vector3d absolutePrevUpVector = cap.getPreviousDir().adjustLookVec(relativePrevUpVector);
					rotationAngle = (180d / Math.PI) * Math.atan2(absoluteCurrentUpVector.cross(absolutePrevUpVector).dot(absoluteLookVec), absoluteCurrentUpVector.dot(absolutePrevUpVector));
					cap.setTransitionAngle(rotationAngle);
				} else {
					rotationAngle = cap.getTransitionAngle();
				}
				double numerator = GravityCapability.DEFAULT_TIMEOUT - effectiveTimeoutTicks;
				double denominator = CommonConfigHandler.transitionAnimationRotationLength;
				double multiplierZeroToOne = numerator / denominator;
				double multiplierOneToZero = 1 - multiplierZeroToOne;
				transitionRollAmount = (float)(rotationAngle * multiplierOneToZero);
				Vector3d eyePosChangeVector = cap.getEyePos();
				xTranslation = eyePosChangeVector.x * multiplierOneToZero;
				yTranslation = eyePosChangeVector.y * multiplierOneToZero;
				zTranslation = eyePosChangeVector.z * multiplierOneToZero;
				instance.levelRenderer.needsUpdate();
			}

			GL11.glRotated(relativeInterpolatedPitch, 1, 0, 0);
			GL11.glRotated(relativeInterpolatedYaw, 0, 1, 0);
			dir.runCameraTransformation();
			GL11.glRotated(-interpolatedYaw, 0, 1, 0);
			GL11.glRotated(-interpolatedPitch, 1, 0, 0);
			GL11.glRotatef(transitionRollAmount, 0, 0, 1);
			GL11.glRotatef(event.getPitch(), 1, 0, 0);
			GL11.glRotatef(event.getYaw(), 0, 1, 0);
			GL11.glTranslated(xTranslation, yTranslation, zTranslation);
			GL11.glRotatef(-event.getYaw(), 0, 1, 0);
			GL11.glRotatef(-event.getPitch(), 1, 0, 0);
		}
	}

	//@SubscribeEvent
	public void cameraSetup2(EntityViewRenderEvent.CameraSetup event) {
		Minecraft instance = Minecraft.getInstance();
		if (instance.getCameraEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) instance.getCameraEntity();
			GravityCapability cap = GravityCapability.getGravityProp(player);
			GravityDirection dir = cap.getGravityDir();

			GL11.glRotatef(180.0F * dir.rotX, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(180.0F * dir.rotZ, 0.0F, 0.0F, 1.0F);

			double pitch = player.xRotO + (player.xRot - player.xRotO) * event.getRenderPartialTicks();
			GL11.glRotatef((float) pitch * dir.pitchRotDirX, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef((float) pitch * dir.pitchRotDirY, 0.0F, 1.0F, 0.0F);

			double yaw = player.yRotO + (player.yRot - player.yRotO) * event.getRenderPartialTicks() + 180.0F;
			GL11.glRotatef((float) yaw * dir.yawRotDirX, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef((float) yaw * dir.yawRotDirY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef((float) yaw * dir.yawRotDirZ, 0.0F, 0.0F, 1.0F);

			double fixHeight = player.yo - player.getBbWidth() / 2.0F;
			GL11.glTranslatef((float) fixHeight * dir.shiftEyeX, (float) fixHeight * dir.shiftEyeY, (float) fixHeight * dir.shiftEyeZ);
			GL11.glTranslatef((float) player.yo * dir.shiftSneakX, (float) player.yo * dir.shiftSneakY, (float) player.yo * dir.shiftSneakZ);
		}
	}

	//@SubscribeEvent
	public void cameraSetup3(EntityViewRenderEvent.CameraSetup event) {
		Minecraft instance = Minecraft.getInstance();
		if (instance.getCameraEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) instance.getCameraEntity();
			GravityCapability cap = GravityCapability.getGravityProp(player);
			GravityDirection dir = cap.getGravityDir();

			double pitch = (player.xRotO + (player.xRot - player.xRotO) * event.getRenderPartialTicks()) % 360;
			double yaw = (player.yRotO + (player.yRot - player.yRotO) * event.getRenderPartialTicks()) % 360;
			Vector3d relativeInterpolatedLookVec = dir.turnWayForNormal().adjustLookVec(player.getLookAngle());
			double[] relativeInterpolatedPitchAndYaw = Vector3dHelper.getPrecisePitchAndYawFromVector(relativeInterpolatedLookVec);
			GL11.glRotated(relativeInterpolatedPitchAndYaw[0], 1, 0, 0);
			GL11.glRotated(relativeInterpolatedPitchAndYaw[1], 0, 1, 0);
			dir.runCameraTransformation();
			GL11.glRotated(-yaw, 0, 1, 0);
			GL11.glRotated(-pitch, 1, 0, 0);
		}
	}

	//@SubscribeEvent
	public void cameraSetup4(EntityViewRenderEvent.CameraSetup event) {
		Minecraft instance = Minecraft.getInstance();
		if (instance.getCameraEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) instance.getCameraEntity();
			List<CameraEntity> list = instance.level.getEntitiesOfClass(CameraEntity.class, player.getBoundingBox().inflate(2));
			instance.cameraEntity = list.get(0);
		}
		if (instance.getCameraEntity() instanceof CameraEntity) {
			CameraEntity camera = (CameraEntity) instance.getCameraEntity();
			PlayerEntity player = camera.host;
			//double pitch = (player.xRotO + (player.xRot - player.xRotO) * event.getRenderPartialTicks()) % 360;
			//double yaw = (player.yRotO + (player.yRot - player.yRotO) * event.getRenderPartialTicks()) % 360;
		}
	}

	//@SubscribeEvent
	public void cameraSetup5(EntityViewRenderEvent.CameraSetup event) {
		Minecraft instance = Minecraft.getInstance();
		if (instance.getCameraEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) instance.getCameraEntity();
			GravityCapability cap = GravityCapability.getGravityProp(player);
			GravityDirection dir = cap.getGravityDir();
			switch (dir) {
				case UP_TO_DOWN_YN:
					break;
				case DOWN_TO_UP_YP:
					//event.setPitch(event.getPitch()-180);
					event.setRoll(event.getRoll()-180);
					break;
				case WEST_TO_EAST_XP:
					//event.setPitch(event.getPitch()-90);
					event.setRoll(event.getRoll()-90);
					break;
				case SOUTH_TO_NORTH_ZN:
					//event.setPitch(event.getPitch()-90);
					event.setRoll(event.getRoll()-90);
					break;
				case NORTH_TO_SOUTH_ZP:
					//event.setPitch(event.getPitch()-90);
					event.setRoll(event.getRoll()-90);
					break;
				case EAST_TO_WEST_XN:
					//event.setPitch(event.getPitch()-90);
					event.setRoll(event.getRoll()-90);
					break;
			}
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
		IGravityCapability cap = GravityCapability.getGravityProp(event.getEntity());
		if (!event.getEntityLiving().level.isClientSide) {
			cap.tickServer();
		} else {
			cap.tickClient();
		}
	}

	@SubscribeEvent
	public void renderLivingPre(final RenderLivingEvent.Pre<?, ?> event) {
		if (event.getEntity() instanceof PlayerEntity) { //temporarily only overwrite player
			Minecraft instance = Minecraft.getInstance();
			//if (instance.getCameraEntity() instanceof Camera && instance.options.getCameraType().isFirstPerson()) { event.setCanceled(true); return; }

			//IGravityCapability grav = GravityCapability.getGravityProp(event.getEntity());
			//if (grav.getAttracted()) {
			//	MatrixStack matrix = event.getMatrixStack();
			//	switch (grav.getGravityDir()) {
			//		case DOWN_TO_UP_YP:
			//			matrix.mulPose(Vector3f.XP.rotationDegrees(180));
			//			break;
			//		case EAST_TO_WEST_XN:
			//			matrix.mulPose(Vector3f.ZP.rotationDegrees(-90));
			//			break;
			//		case WEST_TO_EAST_XP:
			//			matrix.mulPose(Vector3f.ZP.rotationDegrees(90));
			//			break;
			//		case NORTH_TO_SOUTH_ZP:
			//			matrix.mulPose(Vector3f.XP.rotationDegrees(-90));
			//			break;
			//		case SOUTH_TO_NORTH_ZN:
			//			matrix.mulPose(Vector3f.XP.rotationDegrees(90));
			//			break;
			//	}
			//}
		}
	}
}
