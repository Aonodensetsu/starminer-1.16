package dev.bluecom.starminer.api;

public class GravityCapability implements IGravityCapability {
	private GravityDirection gravity = GravityDirection.upTOdown_YN;
	private boolean zero = false;
	private boolean inverted = false;
	
	@Override
	public GravityDirection getGravityDir() {
		return this.gravity;
	}
	
	@Override
	public boolean getGravityZero() {
		return this.zero;
	}
	
	@Override
	public boolean getGravityInverted() {
		return this.inverted;
	}
	
	@Override
	public void setGravity(GravityDirection grav, Boolean zer, Boolean inv) {
		if (grav != null) {
			this.gravity = grav;
		}
		if (zer != null) {
			this.zero = zer;
		}
		if (inv != null) {
			this.inverted = inv;
		}
	}
}
