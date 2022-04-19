package jp.mc.ancientred.starminer.core.ai.path;

public class GPath {
  private GPathPoint[] GPathPoints = new GPathPoint[1024];
  
  private int count;
  
  public GPathPoint addPoint(GPathPoint gPathPoint) {
    if (gPathPoint.index >= 0)
    {
      throw new IllegalStateException("OW KNOWS!");
    }

    if (this.count == this.GPathPoints.length) {
      
      GPathPoint[] aGPathPoint = new GPathPoint[this.count << 1];
      System.arraycopy(this.GPathPoints, 0, aGPathPoint, 0, this.count);
      this.GPathPoints = aGPathPoint;
    } 
    
    this.GPathPoints[this.count] = gPathPoint;
    gPathPoint.index = this.count;
    sortBack(this.count++);
    return gPathPoint;
  }

  public void clearPath() {
    this.count = 0;
  }

  public GPathPoint dequeue() {
    GPathPoint pathPoint = this.GPathPoints[0];
    this.GPathPoints[0] = this.GPathPoints[--this.count];
    this.GPathPoints[this.count] = null;
    
    if (this.count > 0)
    {
      sortForward(0);
    }
    
    pathPoint.index = -1;
    return pathPoint;
  }

  public void changeDistance(GPathPoint p_75850_1_, float p_75850_2_) {
    float f1 = p_75850_1_.distanceToTarget;
    p_75850_1_.distanceToTarget = p_75850_2_;
    
    if (p_75850_2_ < f1) {
      
      sortBack(p_75850_1_.index);
    }
    else {
      
      sortForward(p_75850_1_.index);
    } 
  }

  private void sortBack(int p_75847_1_) {
    GPathPoint gPathPoint = this.GPathPoints[p_75847_1_];

    for (float f = gPathPoint.distanceToTarget; p_75847_1_ > 0; p_75847_1_ = j) {
      
      int j = p_75847_1_ - 1 >> 1;
      GPathPoint GPathPoint1 = this.GPathPoints[j];
      
      if (f >= GPathPoint1.distanceToTarget) {
        break;
      }

      this.GPathPoints[p_75847_1_] = GPathPoint1;
      GPathPoint1.index = p_75847_1_;
    } 
    
    this.GPathPoints[p_75847_1_] = gPathPoint;
    gPathPoint.index = p_75847_1_;
  }

  private void sortForward(int p_75846_1_) {
    GPathPoint gPathPoint = this.GPathPoints[p_75846_1_];
    float f = gPathPoint.distanceToTarget;
    while (true) {
      GPathPoint GPathPoint2;
      float f2;
      int j = 1 + (p_75846_1_ << 1);
      int k = j + 1;
      
      if (j >= this.count) {
        break;
      }

      GPathPoint GPathPoint1 = this.GPathPoints[j];
      float f1 = GPathPoint1.distanceToTarget;

      if (k >= this.count) {
        
        GPathPoint2 = null;
        f2 = Float.POSITIVE_INFINITY;
      }
      else {
        
        GPathPoint2 = this.GPathPoints[k];
        f2 = GPathPoint2.distanceToTarget;
      } 
      
      if (f1 < f2) {
        
        if (f1 >= f) {
          break;
        }

        this.GPathPoints[p_75846_1_] = GPathPoint1;
        GPathPoint1.index = p_75846_1_;
        p_75846_1_ = j;
        
        continue;
      } 
      if (f2 >= f) {
        break;
      }

      this.GPathPoints[p_75846_1_] = GPathPoint2;
      GPathPoint2.index = p_75846_1_;
      p_75846_1_ = k;
    } 

    this.GPathPoints[p_75846_1_] = gPathPoint;
    gPathPoint.index = p_75846_1_;
  }

  public boolean isPathEmpty() {
    return (this.count == 0);
  }
}
