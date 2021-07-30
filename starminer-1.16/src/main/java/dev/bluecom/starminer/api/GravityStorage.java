package dev.bluecom.starminer.api;

// import java.io.IOException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
// import net.minecraftforge.common.capabilities.Capability.IStorage;

public class GravityStorage implements Capability.IStorage<IGravityCapability> {
  @Override
  public INBT writeNBT(Capability<IGravityCapability> capability, IGravityCapability instance, Direction side) {
    String dir;
    switch (instance.getGravity()) {
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
    return stri;
    // try {
      // return instance.storeTable();
      // } catch (IOException e) {
      // e.printStackTrace();
      // return null;
    // }
  }
  @Override
  public void readNBT(Capability<IGravityCapability> capability, IGravityCapability instance, Direction side, INBT nbt) {
    GravityDirection dir;
    CompoundNBT nbt2 = (CompoundNBT) nbt;
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
      default:
        dir = GravityDirection.upTOdown_YN;
    };
    instance.setGravity(dir);
    // try {
      // instance.restoreTable(nbt);
      // } catch (IOException | ClassNotFoundException e) {
      // e.printStackTrace();
    // }
  }
}
