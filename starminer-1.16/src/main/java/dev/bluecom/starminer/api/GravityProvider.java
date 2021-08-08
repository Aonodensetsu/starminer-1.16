package dev.bluecom.starminer.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class GravityProvider implements ICapabilitySerializable<CompoundNBT> {
	@CapabilityInject(IGravityCapability.class)
	public static final Capability<IGravityCapability> GRAVITY = null;
	
	private final LazyOptional<IGravityCapability> instance;

	public GravityProvider(LivingEntity entity) {
		instance = LazyOptional.of(() -> new GravityCapability(entity));
	}
	
	public static void init() {
		CapabilityManager.INSTANCE.register(IGravityCapability.class, new GravityStorage(), GravityCapability::new);
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
		return GRAVITY.orEmpty(capability, instance);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) GRAVITY.getStorage().writeNBT(GRAVITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		GRAVITY.getStorage().readNBT(GRAVITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
