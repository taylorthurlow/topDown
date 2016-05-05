package tthurlow.topdown;

public class EntityPosition {
	private double x, y;
	private Direction heading;
	public static enum Direction {
		NORTH, EAST, SOUTH, WEST, NONE
	}
	
	public EntityPosition(double posX, double posY, Direction heading) {
		this.x = posX;
		this.y = posY;
		this.heading = heading;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public void incrementX(double increment) {
		this.x += increment;
	}
	
	public void decrementX(double decrement) {
		this.x -= decrement;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void incrementY(double increment) {
		this.y += increment;
	}
	
	public void decrementY(double decrement) {
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
