package animationEnums;

public enum DIRECTION {
	//this enum is for ease of reading the direction in which the orc is travelling
	NORTHWEST(0), NORTH(1), NORTHEAST(2), EAST(3), SOUTHEAST(4), SOUTH(5), SOUTHWEST(6), WEST(7);

	private int direction;

	DIRECTION(int dir) {
		direction = dir;
	}


}