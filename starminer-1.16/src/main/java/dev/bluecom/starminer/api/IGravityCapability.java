package dev.bluecom.starminer.api;

// import java.io.IOException;
// import net.minecraft.nbt.ByteArrayNBT;
// import net.minecraft.nbt.INBT;

public interface IGravityCapability {
  public GravityDirection getGravity();
  public void setGravity(GravityDirection gravity);
  // public ByteArrayNBT storeTable() throws IOException;
  // public void restoreTable(INBT nbt) throws IOException, ClassNotFoundException;
}
