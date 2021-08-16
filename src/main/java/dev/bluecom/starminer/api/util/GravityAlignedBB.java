package dev.bluecom.starminer.api.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class GravityAlignedBB extends AxisAlignedBB {
    private final GravityDirection gravityDirection;

    public GravityAlignedBB(GravityAlignedBB bb, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.gravityDirection = bb.gravityDirection;
    }

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

    @Override
    public GravityAlignedBB move(BlockPos pos) {
        return new GravityAlignedBB(this, this.move(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Override
    public GravityAlignedBB move(double x, double y, double z) {
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

    public Vector3d getOrigin() {
        switch (this.gravityDirection) {
            case DOWN_TO_UP_YP:
                return new Vector3d(this.getCentreX(), this.maxY, this.getCentreZ());
            case NORTH_TO_SOUTH_ZP:
                return new Vector3d(this.getCentreX(), this.getCentreY(), this.maxZ);
            case EAST_TO_WEST_XN:
                return new Vector3d(this.minX, this.getCentreY(), this.getCentreZ());
            case SOUTH_TO_NORTH_ZN:
                return new Vector3d(this.getCentreX(), this.getCentreY(), this.minZ);
            case WEST_TO_EAST_XP:
                return new Vector3d(this.maxX, this.getCentreY(), this.getCentreZ());
            default:
                return new Vector3d(this.getCentreX(), this.minY, this.getCentreZ());
        }
    }

    private double getCentreX() {
        return (this.minX + this.maxX) / 2d;
    }

    private double getCentreZ() {
        return (this.minZ + this.maxZ) / 2d;
    }

    private double getCentreY() {
        return (this.minY + this.maxY) / 2d;
    }

    public GravityAlignedBB moveSuper(double x, double y, double z) {
        return new GravityAlignedBB(this, super.move(x, y, z));
    }

    public GravityAlignedBB expandUp(double y) {
        switch (this.gravityDirection) {
            case DOWN_TO_UP_YP:
                return new GravityAlignedBB(this, this.minX, this.minY - y, this.minZ, this.maxX, this.maxY, this.maxZ);
            case NORTH_TO_SOUTH_ZP:
                return new GravityAlignedBB(this, this.minX, this.minY, this.minZ - y, this.maxX, this.maxY, this.maxZ);
            case EAST_TO_WEST_XN:
                return new GravityAlignedBB(this, this.minX, this.minY, this.minZ, this.maxX + y, this.maxY, this.maxZ);
            case SOUTH_TO_NORTH_ZN:
                return new GravityAlignedBB(this, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ + y);
            case WEST_TO_EAST_XP:
                return new GravityAlignedBB(this, this.minX - y, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
            default:
                return new GravityAlignedBB(this, this.minX, this.minY, this.minZ, this.maxX, this.maxY + y, this.maxZ);
        }
    }
    public AxisAlignedBB toVanilla() {
        return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
}
