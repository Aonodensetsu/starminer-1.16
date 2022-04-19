package jp.mc.ancientred.starminer.core.ai.path;

import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

public class GPathPoint
  extends PathPoint
{
  private final int hash;
  int index = -1;
  
  float totalPathDistance;
  
  float distanceToNext;
  
  float distanceToTarget;
  
  GPathPoint previous;
  
  public boolean visited;
  
  public GPathPoint(int x, int y, int z) {
    super(x, y, z);
    this.hash = PathPoint.makeHash(x, y, z);
  }

  public float distanceTo(PathPoint p_75829_1_) {
    float f = (p_75829_1_.xCoord - this.xCoord);
    float f1 = (p_75829_1_.yCoord - this.yCoord);
    float f2 = (p_75829_1_.zCoord - this.zCoord);
    return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
  }

  public float distanceToSquared(PathPoint p_75832_1_) {
    float f = (p_75832_1_.xCoord - this.xCoord);
    float f1 = (p_75832_1_.yCoord - this.yCoord);
    float f2 = (p_75832_1_.zCoord - this.zCoord);
    return f * f + f1 * f1 + f2 * f2;
  }

  public boolean equals(Object p_equals_1_) {
    if (!(p_equals_1_ instanceof GPathPoint)) {
      return false;
    }
    GPathPoint pathpoint = (GPathPoint)p_equals_1_;
    return (this.hash == pathpoint.hash && this.xCoord == pathpoint.xCoord && this.yCoord == pathpoint.yCoord && this.zCoord == pathpoint.zCoord);
  }

  public int hashCode() {
    return this.hash;
  }

  public boolean isAssigned() {
    return (this.index >= 0);
  }

  public String toString() {
    return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
  }
}
