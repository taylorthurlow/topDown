package tthurlow.topdown;

public class UnitPosition {
	private int x, y;
	private Direction heading;
	public static enum Direction {
		NORTH, EAST, SOUTH, WEST, NONE
	}
	
	public UnitPosition(int posX, int posY, Direction heading) {
		this.x = posX;
		this.y = posY;
		this.heading = heading;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void incrementX(int increment) {
		this.x += increment;
	}
	
	public void decrementX(int decrement) {
		this.x -= decrement;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void incrementY(int increment) {
		this.y += increment;
	}
	
	public void decrementY(int decrement) {
		this.y -= decrement;
	}

	public Direction getHeading() {
		return heading;
	}

	public void setHeading(Direction heading) {
		this.heading = heading;
	}

	public String toString() {
		return "(" + this.x + ", " + this.y + ") heading " + this.heading.toString();
	}
}
