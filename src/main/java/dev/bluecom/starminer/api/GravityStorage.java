package dev.bluecom.starminer.api;

import javax.annotation.Nullable;

import dev.bluecom.starminer.api.util.GravityDirection;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class GravityStorage implements Capability.IStorage<IGravityCapability> {
	@Nullable
	@Override
	public INBT writeNBT(Capability<IGravityCapability> capability, IGravityCapability instance, Direction side) {
		CompoundNBT stri = new CompoundNBT();
		stri.putString("gravity", instance.getGravityDir().name());
		stri.putBoolean("zero", instance.getGravityZero());
		stri.putBoolean("inverted", instance.getGravityInverted());
		return stri;
	}
	
	@Override
	public void readNBT(Capability<IGravityCapability> capability, IGravityCapability instance, Direction side, INBT nbt) {
		CompoundNBT nbt2 = (CompoundNBT) nbt;
		instance.setGravityDir(GravityDirection.valueOf(nbt2.getString("gravity")));
		instance.setGravityZero(nbt2.getBoolean("zero"));
		instance.setGravityInverted(nbt2.getBoolean("inverted"));
	}
}
