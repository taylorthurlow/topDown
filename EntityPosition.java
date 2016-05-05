package tthurlow.topdown;

public class EntityPosition {
	private double x, y;
	private Direction heading;
	public enum Direction {
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

	public double getY() {
		return y;
	}


	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}


	public int getTileX() {
		long iPart = (long) this.x;
		return (int) iPart;
	}

	public int getTileY() {
		long iPart = (long) this.y;
		return (int) iPart;
	}


	public int getInnerPositionX() {
		/**
		 * This next code rounds the decimal part of the position (effectively position within each square) to the
		 * nearest sixteenth (nearest pixel) by multiplying the decimal part by 16, rounding to the nearest integer,
		 * and then dividing by 16. The result should effectively be the sub-square position in pixels.
		 */
		long iPart = (long) this.x;
		double fPart = this.x - iPart;
		return (int) (Math.round(fPart * 16));
	}

	public int getInnerPositionY() {
		long iPart = (long) this.y;
		double fPart = this.y - iPart;
		return (int) (Math.round(fPart * 16));
	}


	public void incrementX(double increment) {
		this.x += increment;
	}

	public void incrementY(double increment) {
		this.y += increment;
	}

	
	public void decrementX(double decrement) {
		this.x -= decrement;
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
