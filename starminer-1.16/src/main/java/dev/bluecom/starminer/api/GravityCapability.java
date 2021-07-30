package dev.bluecom.starminer.api;

import java.util.Optional;

// import java.io.BufferedInputStream;
// import java.io.BufferedOutputStream;
// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.io.ObjectInputStream;
// import java.io.ObjectOutputStream;
// import java.util.Hashtable;
// import net.minecraft.entity.Entity;
// import net.minecraft.nbt.ByteArrayNBT;
// import net.minecraft.nbt.INBT;

public class GravityCapability implements IGravityCapability {
  private GravityDirection gravity = GravityDirection.upTOdown_YN;
  private boolean zero = false;
  @Override
  public GravityDirection getGravity() {
    return this.gravity;
  };
  @Override
  public void setGravity(GravityDirection grav) {
    this.gravity = grav;
  }
  @Override
  public boolean isZeroGravity(Optional<Boolean> set) {
	if (set.isPresent()) {
      zero = set.get();
	}
	return zero;
  }
  
// @Override
// public ByteArrayNBT storeTable() throws IOException {
// 	ByteArrayOutputStream output = new ByteArrayOutputStream();
// 	ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(output));
// 	stream.writeObject(entityList);
// 	stream.close();
// 	ByteArrayNBT out = new ByteArrayNBT(output.toByteArray());
// 	return out;
// }
// @SuppressWarnings("unchecked")
// @Override
// public void restoreTable(INBT nbt) throws IOException, ClassNotFoundException {
//	ByteArrayNBT nbt2 = (ByteArrayNBT) nbt;
//	ByteArrayInputStream input = new ByteArrayInputStream(nbt2.getAsByteArray());
//	ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(input));
//	entityList = (Hashtable<Entity, GravityDirection>) stream.readObject();
// }
}
