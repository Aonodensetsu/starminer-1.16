package dev.bluecom.starminer.api;

import javax.annotation.Nullable;

public class GravityCapability implements IGravityCapability {
	private GravityDirection gravity = GravityDirection.upTOdown_YN;
	private boolean zeroG = false;
	
	@Override
	public GravityDirection getGravityDir() {
		return this.gravity;
	}
	
	@Override
	public boolean getGravityZero() {
		return this.zeroG;
	}
	
	@Override
	public void setGravity(@Nullable GravityDirection grav, Boolean zr) {
		if (grav != null) {
			this.gravity = grav;
		}
		if (zr != null) {
			this.zeroG = zr;
		}
	}
}
