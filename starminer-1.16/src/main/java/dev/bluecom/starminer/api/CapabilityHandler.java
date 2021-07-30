package dev.bluecom.starminer.api;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityHandler {
  public static final ResourceLocation GRAVITY = new ResourceLocation("starminer", "gravity");
  
  @SubscribeEvent
  public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
    event.addCapability(GRAVITY, new GravityProvider());
  };
}
