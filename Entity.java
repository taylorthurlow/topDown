package tthurlow.topdown;

import java.awt.Graphics;

import tthurlow.topdown.EntityPosition.Direction;

public class Entity {
	public EntityPosition position, viewPosition;
	public Map map;
	public SpriteSheet sprites = Game.charSprites;
	public boolean up = false, down = false, left = false, right = false, isMoving = false;
	public double speed = 0.0625;

	public Entity(EntityPosition entityPosition, SpriteSheet sprites, Map map) {
		this.position = entityPosition;
		this.viewPosition = entityPosition;
		this.sprites = sprites;
		this.map = map;
	}

	public void tick() {
		viewPosition = position; // may not be necessary
		if (up && canEnterNextSquare()) position.decrementY(speed);
		if (down && canEnterNextSquare()) position.incrementY(speed);
		if (left && canEnterNextSquare()) position.decrementX(speed);
		if (right && canEnterNextSquare()) position.incrementX(speed);
	}

	public void render(Graphics g, float interpolation) {
		setViewPosition(interpolation);
		int spriteX, spriteY;
		switch (viewPosition.getHeading()) {
			case NORTH:
				spriteX = 1;
				spriteY = 0;
				break;
			case SOUTH:
				spriteX = 0;
				spriteY = 0;
				break;
			case EAST:
				spriteX = 3;
				spriteY = 0;
				break;
			case WEST:
				spriteX = 2;
				spriteY = 0;
				break;
			case NONE:
			default:
				spriteX = 0;
				spriteY = 0;
		}
		g.drawImage(sprites.crop(spriteX, spriteY, 16, 16),
			(viewPosition.getTileX() * 16 + viewPosition.getInnerPositionX()) - 8,
			(viewPosition.getTileY() * 16 + viewPosition.getInnerPositionY()) - 8,
			16, 16, null);
	}

	public Tile getNextTile() {
		switch (position.getHeading()) {
			case NORTH:
				return map.getTileAtPosition(position.getTileX(), position.getTileY() - 1);
			case EAST:
				return map.getTileAtPosition(position.getTileX() + 1, position.getTileY());
			case SOUTH:
				return map.getTileAtPosition(position.getTileX(), position.getTileY() + 1);
			case WEST:
				return map.getTileAtPosition(position.getTileX() - 1, position.getTileY());
			case NONE:
			default:
				return null;
		}
	}

	public Thing getNextThing() {
		switch (position.getHeading()) {
			case NORTH:
				return map.getThingAtPosition(position.getTileX(), position.getTileY() - 1);
			case EAST:
				return map.getThingAtPosition(position.getTileX() + 1, position.getTileY());
			case SOUTH:
				return map.getThingAtPosition(position.getTileX(), position.getTileY() + 1);
			case WEST:
				return map.getThingAtPosition(position.getTileX() - 1, position.getTileY());
			case NONE:
			default:
				return null;
		}
	}

	public boolean canEnterNextSquare() {
		if (getNextTile().isSolid() || getNextThing().isSolid()) {
			return false;
		} else {
			return true;
		}
	}

	public EntityPosition getPosition() {
		return position;
	}

	public void setViewPosition(double interpolation) {
		double adjustment = round16(speed * interpolation);

		if (up && !left && !right) { // Only upwards
			viewPosition.setY(position.getY() - adjustment);
		} else if (left && !down && !up) { // Only leftwards
			viewPosition.setX(position.getX() - adjustment);
		} else if (down && !left && !right) { // Only downwards
			viewPosition.setY(position.getY() + adjustment);
		} else if (right && !up && !down) { // Only rightwards
			viewPosition.setX(position.getX() + adjustment);
		} else if (up && left) {
			viewPosition.setY(position.getY() - adjustment);
			viewPosition.setX(position.getX() - adjustment);
		} else if (left && down) {
			viewPosition.setY(position.getY() + adjustment);
			viewPosition.setX(position.getX() - adjustment);
		} else if (down && right) {
			viewPosition.setY(position.getY() + adjustment);
			viewPosition.setX(position.getX() + adjustment);
		} else if (right && up) {
			viewPosition.setY(position.getY() - adjustment);
			viewPosition.setX(position.getX() + adjustment);
		}
	}

	public static double round16(double toRound) {
		long iPart = (long) toRound;
		double fPart = toRound - iPart;
		return iPart + (double) (Math.round(fPart * 16)) / 16;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String toString() {
		return "Raw: (" + position.getX() + ", " + position.getY() + ")  Tile: (" + position.getX() / 16 +
				", " + position.getY() / 16 + ")  Sub: (" + position.getInnerPositionX() + ", " + position.getInnerPositionY() +
				")  Speed: " + speed + " un/sec, heading " + position.getHeading().toString();
	}
}
