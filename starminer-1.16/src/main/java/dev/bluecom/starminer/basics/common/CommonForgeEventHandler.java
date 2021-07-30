package dev.bluecom.starminer.basics.common;

import dev.bluecom.starminer.api.GravityCapability;
import dev.bluecom.starminer.api.GravityProvider;
import dev.bluecom.starminer.api.IGravityCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonForgeEventHandler {
  @SubscribeEvent
  public void onPlayerLogsIn(PlayerLoggedInEvent event) {
    PlayerEntity player = event.getPlayer();
    LazyOptional<IGravityCapability> gravity = player.getCapability(GravityProvider.GRAVITY, null);
    IGravityCapability grav = gravity.orElse(new GravityCapability());
    player.sendMessage(new StringTextComponent("DEBUG: Your gravity is " + (grav.getGravity())), null);
  }
}
