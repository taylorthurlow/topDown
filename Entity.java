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
		if (up && canEnterNextSquare(Direction.NORTH)) position.decrementY(speed);
		if (down && canEnterNextSquare(Direction.SOUTH)) position.incrementY(speed);
		if (left && canEnterNextSquare(Direction.WEST)) position.decrementX(speed);
		if (right && canEnterNextSquare(Direction.EAST)) position.incrementX(speed);
	}

	public void render(Graphics g, float interpolation) {
		setViewPosition(interpolation);
		g.drawImage(sprites.crop(0, 0, 16, 16),
			(viewPosition.getTileX() * 16 + viewPosition.getInnerPositionX()) - 8,
			(viewPosition.getTileY() * 16 + viewPosition.getInnerPositionY()) - 8,
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
