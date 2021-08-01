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
			case downTOup_YP:
				dir = "YP";
				break;
			case eastTOwest_XN:
				dir = "XN";
				break;
			case northTOsouth_ZP:
				dir = "ZP";
				break;
			case southTOnorth_ZN:
				dir = "ZN";
				break;
			case upTOdown_YN:
				dir = "YN";
				break;
			case westTOeast_XP:
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
				grav = GravityDirection.downTOup_YP;
				break;
			case "XN":
				grav = GravityDirection.eastTOwest_XN;
				break;
			case "ZP":
				grav = GravityDirection.northTOsouth_ZP;
				break;
			case "ZN":
				grav = GravityDirection.southTOnorth_ZN;
				break;
			case "YN":
				grav = GravityDirection.upTOdown_YN;
				break;
			case "XP":
				grav = GravityDirection.westTOeast_XP;
				break;
			default:
				grav = GravityDirection.upTOdown_YN;
				break;
		};
		instance.setGravity(grav, nbt2.getBoolean("zero"), nbt2.getBoolean("inverted"));
	}
}
