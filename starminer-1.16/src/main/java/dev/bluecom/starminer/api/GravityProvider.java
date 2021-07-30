package dev.bluecom.starminer.api;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class GravityProvider implements ICapabilitySerializable<INBT> {
  @CapabilityInject(IGravityCapability.class)
  public final static Capability<IGravityCapability> GRAVITY = null;
  
  private static final LazyOptional<IGravityCapability> holder = LazyOptional.of(GravityCapability::new);
  private IGravityCapability instance = GRAVITY.getDefaultInstance();
  
  public <T> boolean hasCapability(Capability<T> capability, Direction side) {
    return capability == GRAVITY;
  }
  
  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
    return capability == GRAVITY ? holder.cast() : LazyOptional.empty();
  }

  @Override
  public INBT serializeNBT() {
    return GRAVITY.getStorage().writeNBT(GRAVITY, this.instance, null);
  }

  @Override
  public void deserializeNBT(INBT nbt) {
    GRAVITY.getStorage().readNBT(GRAVITY, this.instance, null, nbt);
  }
}
