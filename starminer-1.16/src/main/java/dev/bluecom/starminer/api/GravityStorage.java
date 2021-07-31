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
    String dir = null;
    GravityDirection gv = instance.getGravityDir();
    boolean zr = instance.getGravityZero();
    switch (gv) {
      case downTOup_YP:
        dir = "YP";
      case eastTOwest_XN:
        dir = "XN";
      case northTOsouth_ZP:
        dir = "ZP";
      case southTOnorth_ZN:
        dir = "ZN";
      case upTOdown_YN:
        dir = "YN";
      case westTOeast_XP:
        dir = "XP";
      default:
        dir = "YN";
    };
    CompoundNBT stri = new CompoundNBT();
    stri.putString("dir", dir);
    stri.putBoolean("zero", zr);
    return stri;
  }
  @Override
  public void readNBT(Capability<IGravityCapability> capability, IGravityCapability instance, Direction side, INBT nbt) {
    CompoundNBT nbt2 = (CompoundNBT) nbt;
    GravityDirection dir = null;
    switch (nbt2.getString("dir")) {
      case "YP":
        dir = GravityDirection.downTOup_YP;
      case "XN":
        dir = GravityDirection.eastTOwest_XN;
      case "ZP":
        dir = GravityDirection.northTOsouth_ZP;
      case "ZN":
        dir = GravityDirection.southTOnorth_ZN;
      case "YN":
        dir = GravityDirection.upTOdown_YN;
      case "XP":
        dir = GravityDirection.westTOeast_XP;
    };
   boolean zr = nbt2.getBoolean("zero");
    instance.setGravity(dir, zr);
  }
}
