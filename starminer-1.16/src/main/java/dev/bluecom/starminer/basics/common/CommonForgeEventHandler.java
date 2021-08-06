package dev.bluecom.starminer.basics.common;

import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.api.IGravityCapability;
import dev.bluecom.starminer.basics.SMModContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonForgeEventHandler {
	@SubscribeEvent
	public void onPlayerLogsIn(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		LazyOptional<IGravityCapability> gravity = player.getCapability(GravityProvider.GRAVITY, null);
		IGravityCapability gravity2 = gravity.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"));
		player.sendMessage(new StringTextComponent("DEBUG: Your gravity is " + (gravity2.getGravityDir())), null);
	}
	
	@SubscribeEvent
	public void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event) {
		event.addCapability(new ResourceLocation(SMModContainer.MODID, "gravity"), new GravityProvider());
	}
}
