package dev.bluecom.starminer.basics.common;

import java.util.Optional;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.api.IGravityCapability;
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
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
    PlayerEntity player = event.getPlayer();
    LazyOptional<IGravityCapability> gravity = player.getCapability(GravityProvider.GRAVITY, null);
    Optional<IGravityCapability> gravity2 = (Optional<IGravityCapability>) gravity.resolve();
    if (gravity2.isPresent()) {
      IGravityCapability grav = gravity2.get();
      player.sendMessage(new StringTextComponent("DEBUG: Your gravity is " + (grav.getGravityDir())), null);
    }
  }
  @SubscribeEvent
  public void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof PlayerEntity) {
      event.addCapability(new ResourceLocation("starminer", "gravity"), new GravityProvider());
    }
  }
}
