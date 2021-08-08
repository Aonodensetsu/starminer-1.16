package dev.bluecom.starminer.basics.common;

import dev.bluecom.starminer.api.GravityCapability;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.api.IGravityCapability;
import dev.bluecom.starminer.basics.ModContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
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
			IGravityCapability cap = event.getTarget().getCapability(GravityProvider.GRAVITY).orElse(null);
			if (cap != null) {
				CommonNetworkHandler.sendToClient(cap.getPacket(), (ServerPlayerEntity) event.getPlayer());
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
		if (!event.getEntityLiving().level.isClientSide) {
			LazyOptional<IGravityCapability> gravity = event.getEntityLiving().getCapability(GravityProvider.GRAVITY);
			IGravityCapability cap = gravity.orElseThrow(() -> new IllegalArgumentException("This should always exist."));
			cap.tick();
		}
	}
	
	@SubscribeEvent
	public void renderLivingPre(final RenderLivingEvent.Pre<?, ?> event) {
		if (event.getEntity() instanceof PlayerEntity) { //temporarily only overwrite player
			IGravityCapability grav = GravityCapability.getGravityProp((PlayerEntity) event.getEntity());
			System.out.println(grav.getAttracted());
		//	if (cap.getAttracted()) {
		//		System.out.println("overwriting");
		//		event.setCanceled(true);
		//		Entity entity = event.getEntity().getEntity();
		//		Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).render(entity, MathHelper.lerp(event.getPartialRenderTick(), entity.yRotO, entity.yRot), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight());
		//	}
		}
	}
}
