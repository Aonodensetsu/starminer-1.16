package jp.mc.ancientred.starminer.core.ai.path;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Vec3;

public class GPathEntity extends PathEntity {
  private static final PathPoint[] DUMMY = new PathPoint[0];
  
  private final GPathPoint[] points;
  private int currentPathIndex;
  private int pathLength;
  
  public GPathEntity(GPathPoint[] p_i2136_1_) {
    super(DUMMY);
    this.points = p_i2136_1_;
    this.pathLength = p_i2136_1_.length;
  }

  public void incrementPathIndex() {
    this.currentPathIndex++;
  }

  public boolean isFinished() {
    return (this.currentPathIndex >= this.pathLength);
  }

  public PathPoint getFinalPathPoint() {
    return (this.pathLength > 0) ? this.points[this.pathLength - 1] : null;
  }

  public PathPoint getPathPointFromIndex(int p_75877_1_) {
    return this.points[p_75877_1_];
  }

  public int getCurrentPathLength() {
    return this.pathLength;
  }

  public void setCurrentPathLength(int p_75871_1_) {
    this.pathLength = p_75871_1_;
  }

  public int getCurrentPathIndex() {
    return this.currentPathIndex;
  }

  public void setCurrentPathIndex(int p_75872_1_) {
    this.currentPathIndex = p_75872_1_;
  }

  public Vec3 getVectorFromIndex(Entity entity, int pathIndex) {
    double d0 = (this.points[pathIndex]).xCoord + 0.5D;
    double d1 = (this.points[pathIndex]).yCoord + 0.5D;
    double d2 = (this.points[pathIndex]).zCoord + 0.5D;
    return Vec3.createVectorHelper(d0, d1, d2);
  }

  public Vec3 getPosition(Entity entity) {
    return getVectorFromIndex(entity, this.currentPathIndex);
  }

  public boolean isSamePath(PathEntity targetPathEntity) {
    if (targetPathEntity instanceof GPathEntity) {
      GPathEntity gpathEntity = (GPathEntity)targetPathEntity;
      if (gpathEntity.points.length != this.points.length)
      {
        return false;
      }

      for (int i = 0; i < this.points.length; i++) {
        
        if ((this.points[i]).xCoord != (gpathEntity.points[i]).xCoord || (this.points[i]).yCoord != (gpathEntity.points[i]).yCoord || (this.points[i]).zCoord != (gpathEntity.points[i]).zCoord)
        {
          return false;
        }
      } 
      
      return true;
    } 
    
    return false;
  }

  public boolean isDestinationSame(Vec3 vec) {
    PathPoint pathPoint = getFinalPathPoint();
    return (pathPoint == null) ? false : ((pathPoint.xCoord == (int)vec.xCoord && pathPoint.zCoord == (int)vec.zCoord));
  }
}
