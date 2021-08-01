package dev.bluecom.starminer.api;

public interface IGravityCapability {
	public GravityDirection getGravityDir();
	public boolean getGravityZero();
	boolean getGravityInverted();
	public void setGravity(GravityDirection gravity, Boolean zero, Boolean inverted);
	
}
