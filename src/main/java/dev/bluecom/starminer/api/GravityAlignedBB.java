package dev.bluecom.starminer.api;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class GravityAlignedBB extends AxisAlignedBB {
    private final GravityDirection gravityDirection;

    public GravityAlignedBB(GravityDirection gravityDirection, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.gravityDirection = gravityDirection;
    }

    public GravityAlignedBB(GravityDirection gravityDirection, AxisAlignedBB bb) {
        super(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
        this.gravityDirection = gravityDirection;
    }

    public GravityAlignedBB(GravityAlignedBB grav, AxisAlignedBB bb) {
        this(grav.gravityDirection, bb);
    }

    public GravityAlignedBB(GravityAlignedBB grav, GravityAlignedBB bb) {
        this(grav.gravityDirection, bb);
    }

    public GravityAlignedBB(GravityDirection gravityDirection, BlockPos pos) {
        super(pos);
        this.gravityDirection = gravityDirection;
    }

    public GravityAlignedBB(GravityDirection gravityDirection, BlockPos pos1, BlockPos pos2) {
        super(pos1, pos2);
        this.gravityDirection = gravityDirection;
    }

    public GravityAlignedBB(GravityDirection gravityDirection, Vector3d vec1, Vector3d vec2) {
        super(vec1, vec2);
        this.gravityDirection = gravityDirection;
    }

    public GravityAlignedBB offset(BlockPos pos) {
        return new GravityAlignedBB(this, this.offset(pos.getX(), pos.getY(), pos.getZ()));
    }

    public GravityAlignedBB offset(double x, double y, double z) {
        double[] d = this.gravityDirection.adjustXYZValues(x, y, z);
        return new GravityAlignedBB(this, super.move(d[0], d[1], d[2]));
    }

    public static double getRelativeBottom(AxisAlignedBB bb) {
        if (bb instanceof GravityAlignedBB) {
            return ((GravityAlignedBB) bb).getRelativeBottom();
        }
        return bb.minY;
    }

    private double getRelativeBottom() {
        switch (this.gravityDirection) {
            case DOWN_TO_UP_YP:
                return -this.maxY;
            case NORTH_TO_SOUTH_ZP:
                return -this.maxZ;
            case EAST_TO_WEST_XN:
                return this.minX;
            case SOUTH_TO_NORTH_ZN:
                return this.minZ;
            case WEST_TO_EAST_XP:
                return -this.maxX;
            default:
                return this.minY;
        }
    }
}
