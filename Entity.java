package tthurlow.topdown;

import java.awt.Graphics;

import tthurlow.topdown.EntityPosition.Direction;

public class Entity {
	public EntityPosition position;
	public Map map;
	public SpriteSheet sprites = Game.charSprites;
	public boolean up = false, down = false, left = false, right = false, isMoving = false;
	public int speed = 1;

	public Entity(EntityPosition entityPosition, SpriteSheet sprites, Map map) {
		this.position = entityPosition;
		this.sprites = sprites;
		this.map = map;
	}

	public void tick() {
		if (up && canEnterNextSquare(Direction.NORTH)) position.decrementY(speed);
		if (down && canEnterNextSquare(Direction.SOUTH)) position.incrementY(speed);
		if (left && canEnterNextSquare(Direction.WEST)) position.decrementX(speed);
		if (right && canEnterNextSquare(Direction.EAST)) position.incrementX(speed);
	}

	public void render(Graphics g, float interpolation) {
		g.drawImage(sprites.crop(0, 0, 16, 16),
				(int) (Math.floor(position.getX() / 16) * interpolation),
				(int) (Math.floor(position.getY() / 16) * interpolation),
				16, 16, null);
	}

	public boolean canEnterNextSquare(Direction dir) {
		return true;
		/**

		int posX = position.getX() / 16;
		int posY = position.getY() / 16;
		int innerPosX = getInnerPositionX();
		int innerPosY = getInnerPositionY();

		switch (dir) {
			case NORTH:
				Tile nextTileNorth = map.getTileAtPosition(posX, posY - 1);
				Thing nextThingNorth = map.getThingAtPosition(posX, posY - 1);
				if ((!nextTileNorth.isSolid() && !nextThingNorth.isSolid()) || innerPosY != 0) {
					return true;
				} else {
					return false;
				}
			case EAST:
				Tile nextTileEast = map.getTileAtPosition(posX + 1, posY);
				Thing nextThingEast = map.getThingAtPosition(posX + 1, posY);
				if ((!nextTileEast.isSolid() && !nextThingEast.isSolid()) || innerPosX != 15) {
					return true;
				} else {
					return false;
				}
			case SOUTH:
				Tile nextTileSouth = map.getTileAtPosition(posX, posY + 1);
				Thing nextThingSouth = map.getThingAtPosition(posX, posY + 1);
				if ((!nextTileSouth.isSolid() && !nextThingSouth.isSolid()) || innerPosY != 15) {
					return true;
				} else {
					return false;
				}
			case WEST:
				Tile nextTileWest = map.getTileAtPosition(posX - 1, posY);
				Thing nextThingWest = map.getThingAtPosition(posX - 1, posY);
				if ((!nextTileWest.isSolid() && !nextThingWest.isSolid()) || innerPosX != 0) {
					return true;
				} else {
					return false;
				}
			case NONE:
			default:
				return true;

		    **/
		//}
	}

	public EntityPosition getPosition() {
		return position;
	}

	public void setPosition(EntityPosition newPosition) {
		position.setX(newPosition.getX());
		position.setY(newPosition.getY());
	}

	public int getTilePositionX() {
		long iPart = (long) position.getX();
		return (int) iPart;
	}

	public int getTilePositionY() {
		long iPart = (long) position.getY();
		return (int) iPart;
	}

	public int getInnerPositionX() {
		/**
		 * This next code rounds the decimal part of the position (effectively position within each square) to the
		 * nearest sixteenth (nearest pixel) by multiplying the decimal part by 16, rounding to the nearest integer,
		 * and then dividing by 16. The result should effectively be the sub-square position in pixels.
		 */
		long iPart = (long) position.getX();
		double fPart = position.getX() - iPart;
		return (int) ((Math.round(fPart * 16)) / 16);
	}

	public int getInnerPositionY() {
		long iPart = (long) position.getY();
		double fPart = position.getY() - iPart;
		return (int) ((Math.round(fPart * 16)) / 16);
	}

	public boolean isMoving() {
		return isMoving;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Map getmap() {
		return map;
	}

	public void setmap(Map map) {
		this.map = map;
	}

	public String toString() {
		return "Raw: (" + position.getX() + ", " + position.getY() + ")  Tile: (" + position.getX() / 16 +
				", " + position.getY() / 16 + ")  Sub: (" + getInnerPositionX() + ", " + getInnerPositionY() +
				")  Speed: " + speed + " un/sec, heading " + position.getHeading().toString();
	}
}
