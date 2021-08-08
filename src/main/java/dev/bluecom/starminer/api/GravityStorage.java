package dev.bluecom.starminer.api;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class GravityStorage implements Capability.IStorage<IGravityCapability> {
	@Nullable
	@Override
	public INBT writeNBT(Capability<IGravityCapability> capability, IGravityCapability instance, Direction side) {
		GravityDirection grav = instance.getGravityDir();
		String dir = null;
		switch (grav) {
			case DOWN_TO_UP_YP:
				dir = "YP";
				break;
			case EAST_TO_WEST_XN:
				dir = "XN";
				break;
			case NORTH_TO_SOUTH_ZP:
				dir = "ZP";
				break;
			case SOUTH_TO_NORTH_ZN:
				dir = "ZN";
				break;
			case UP_TO_DOWN_YN:
				dir = "YN";
				break;
			case WEST_TO_EAST_XP:
				dir = "XP";
				break;
			default:
				dir = "YN";
				break;
		};
		CompoundNBT stri = new CompoundNBT();
		stri.putString("gravity", dir);
		stri.putBoolean("zero", instance.getGravityZero());
		stri.putBoolean("inverted", instance.getGravityInverted());
		return stri;
	}
	
	@Override
	public void readNBT(Capability<IGravityCapability> capability, IGravityCapability instance, Direction side, INBT nbt) {
		CompoundNBT nbt2 = (CompoundNBT) nbt;
		GravityDirection grav;
		switch (nbt2.getString("gravity")) {
			case "YP":
				grav = GravityDirection.DOWN_TO_UP_YP;
				break;
			case "XN":
				grav = GravityDirection.EAST_TO_WEST_XN;
				break;
			case "ZP":
				grav = GravityDirection.NORTH_TO_SOUTH_ZP;
				break;
			case "ZN":
				grav = GravityDirection.SOUTH_TO_NORTH_ZN;
				break;
			case "YN":
				grav = GravityDirection.UP_TO_DOWN_YN;
				break;
			case "XP":
				grav = GravityDirection.WEST_TO_EAST_XP;
				break;
			default:
				grav = GravityDirection.UP_TO_DOWN_YN;
				break;
		};
		instance.setGravityDir(grav);
		instance.setGravityZero(nbt2.getBoolean("zero"));
		instance.setGravityInverted(nbt2.getBoolean("inverted"));
	}
}
