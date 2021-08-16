package dev.bluecom.starminer.api.util;

class GravityConst {
	public static final int[] MATRIX_ROT_UP_TO_DOWN_I = new int[] { 1, 0, 0, 0, 1, 0, 0, 0, 1 };
	public static final int[] MATRIX_ROT_DOWN_TO_UP_I = new int[] { 1, 0, 0, 0, -1, 0, 0, 0, -1 };
	public static final int[] MATRIX_ROT_EAST_TO_WEST_I = new int[] { 0, -1, 0, 1, 0, 0, 0, 0, 1 };
	public static final int[] MATRIX_ROT_WEST_TO_EAST_I = new int[] { 0, 1, 0, -1, 0, 0, 0, 0, 1 };
	public static final int[] MATRIX_ROT_NORTH_TO_SOUTH_I = new int[] { 1, 0, 0, 0, 0, -1, 0, 1, 0 };
	public static final int[] MATRIX_ROT_SOUTH_TO_NORTH_I = new int[] { 1, 0, 0, 0, 0, 1, 0, -1, 0 };
	
	public static final double[] MATRIX_ROT_UP_TO_DOWN_D = new double[] { 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D };
	public static final double[] MATRIX_ROT_DOWN_TO_UP_D = new double[] { 1.0D, 0.0D, 0.0D, 0.0D, -1.0D, 0.0D, 0.0D, 0.0D, -1.0D };
	public static final double[] MATRIX_ROT_EAST_TO_WEST_D = new double[] { 0.0D, -1.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D };
	public static final double[] MATRIX_ROT_WEST_TO_EAST_D = new double[] { 0.0D, 1.0D, 0.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D };
	public static final double[] MATRIX_ROT_NORTH_TO_SOUTH_D = new double[] { 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, -1.0D, 0.0D, 1.0D, 0.0D };
	public static final double[] MATRIX_ROT_SOUTH_TO_NORTH_D = new double[] { 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
	
	public static final int[] FORGE_SIDE_ROT_UP_TO_DOWN = new int[] { 0, 1, 2, 3, 4, 5 };
	public static final int[] FORGE_SIDE_ROT_DOWN_TO_UP = new int[] { 1, 0, 3, 2, 4, 5 };
	public static final int[] FORGE_SIDE_ROT_EAST_TO_WEST = new int[] { 5, 4, 2, 3, 0, 1 };
	public static final int[] FORGE_SIDE_ROT_WEST_TO_EAST = new int[] { 4, 5, 2, 3, 1, 0 };
	public static final int[] FORGE_SIDE_ROT_NORTH_TO_SOUTH = new int[] { 2, 3, 1, 0, 4, 5 };
	public static final int[] FORGE_SIDE_ROT_SOUTH_TO_NORTH = new int[] { 3, 2, 0, 1, 4, 5 };
}
