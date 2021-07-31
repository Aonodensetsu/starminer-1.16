package dev.bluecom.starminer.api;

import javax.annotation.Nullable;

public interface IGravityCapability {
  public GravityDirection getGravityDir();
  public boolean getGravityZero();
  public void setGravity(@Nullable GravityDirection gravity, Boolean zr);
}
