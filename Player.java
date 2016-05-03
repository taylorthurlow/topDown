package tthurlow.topdown;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import tthurlow.topdown.UnitPosition.Direction;

public class Player implements KeyListener {
	private UnitPosition position;
	private Map currentMap;
	private SpriteSheet sprites = Game.charSprites;
	private boolean up = false, down = false, left = false, right = false, isMoving = false;
	private int speed = 1; // changing this is bad haha

	public Player(UnitPosition playerPosition, SpriteSheet sprites, Map map) {
		this.position = playerPosition;
		this.sprites = sprites;
		this.currentMap = map;
	}

	public void tick() {
		for (int i = 0; i < speed; i++) { // For loop prevents speeds higher than 1 from skipping over
										  // collision boundaries
			if (up && canEnterNextSquare(Direction.NORTH)) {
				position.decrementY(speed);
			}
			if (down && canEnterNextSquare(Direction.SOUTH)) {
				position.incrementY(speed);
			}
			if (left && canEnterNextSquare(Direction.WEST)) {
				position.decrementX(speed);
			}
			if (right && canEnterNextSquare(Direction.EAST)) {
				position.incrementX(speed);
			}
		}
	}

	public void render(Graphics g) {
		g.drawImage(sprites.crop(0, 0, 16, 16), position.getX() - ((16 * Game.scale) / 2),
				position.getY() - ((16 * Game.scale) / 2), 16 * Game.scale, 16 * Game.scale, null);
	}
	
	public boolean canEnterNextSquare(Direction dir) {
		int posX = position.getX() / 16;
		int posY = position.getY() / 16;
		int innerPosX = getInnerPositionX();
		int innerPosY = getInnerPositionY();
		
		switch (dir) {
			case NORTH:
				Tile nextTileNorth = currentMap.getTileAtPosition(posX, posY - 1);
				Thing nextThingNorth = currentMap.getThingAtPosition(posX, posY - 1);
				if ((!nextTileNorth.isSolid() && !nextThingNorth.isSolid()) || innerPosY != 0) {
					return true;
				} else {
					return false;
				}
			case EAST:
				Tile nextTileEast = currentMap.getTileAtPosition(posX + 1, posY);
				Thing nextThingEast = currentMap.getThingAtPosition(posX + 1, posY);
				if ((!nextTileEast.isSolid() && !nextThingEast.isSolid()) || innerPosX != 15) {
					return true;
				} else {
					return false;
				}
			case SOUTH:
				Tile nextTileSouth = currentMap.getTileAtPosition(posX, posY + 1);
				Thing nextThingSouth = currentMap.getThingAtPosition(posX, posY + 1);
				if ((!nextTileSouth.isSolid() && !nextThingSouth.isSolid()) || innerPosY != 15) {
					return true;
				} else {
					return false;
				}
			case WEST:
				Tile nextTileWest = currentMap.getTileAtPosition(posX - 1, posY);
				Thing nextThingWest = currentMap.getThingAtPosition(posX - 1, posY);
				if ((!nextTileWest.isSolid() && !nextThingWest.isSolid()) || innerPosX != 0) {
					return true;
				} else {
					return false;
				}
			case NONE:
			default:
				return true;
		}
	}

	public UnitPosition getPosition() {
		return position;
	}
	
	public void setPosition(UnitPosition newPosition) {
		position.setX(newPosition.getX());
		position.setY(newPosition.getY());
	}
	
	public int getInnerPositionX() {
		return position.getX() % 16;
	}
	
	public int getInnerPositionY() {
		return position.getY() % 16;
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
	
	public Map getCurrentMap() {
		return currentMap;
	}
	
	public void setCurrentMap(Map map) {
		this.currentMap = map;
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
			// W or UP_ARR pressed
			isMoving = true;
			up = true;
			down = false;
			position.setHeading(Direction.NORTH);
		}
		if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {
			// S or DOWN_ARR pressed
			isMoving = true;
			down = true;
			up = false;
			position.setHeading(Direction.SOUTH);
		}
		if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
			// A or LEFT_ARR pressed
			isMoving = true;
			left = true;
			right = false;
			position.setHeading(Direction.WEST);
		}
		if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
			// D or RIGHT_ARR pressed
			isMoving = true;
			right = true;
			left = false;
			position.setHeading(Direction.EAST);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
			// W or UP_ARR released
			up = false;
		}
		if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {
			// S or DOWN_ARR released
			down = false;
		}
		if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
			// A or LEFT_ARR released
			left = false;
		}
		if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
			// D or RIGHT_ARR released
			right = false;
		}
		
		if (!up && !down && !left && !right) {
			isMoving = false;
		}
		
		if (e.getKeyCode() == 32) {
			Tile interactedThing = null;
			int posX = position.getX() / 16;
			int posY = position.getY() / 16;
			switch(position.getHeading()) {
				case NONE:
					System.out.println("Interact with:" + "Nothing" + ". heading NONE.");
					break;
				case NORTH:
					System.out.println("Interact with:" + currentMap.getThingAtPosition(posX, posY - 1).getName() + ". Heading NORTH.");
					interact(posX, posY - 1, currentMap.getThingAtPosition(posX, posY - 1).getId());
					break;
				case EAST:
					System.out.println("Interact with:" + currentMap.getThingAtPosition(posX + 1, posY).getName() + ". Heading EAST.");
					interact(posX + 1, posY, currentMap.getThingAtPosition(posX + 1, posY).getId());
					break;
				case SOUTH:
					System.out.println("Interact with:" + currentMap.getThingAtPosition(posX, posY + 1).getName() + ". Heading SOUTH.");
					interact(posX, posY + 1, currentMap.getThingAtPosition(posX, posY + 1).getId());
					break;
				case WEST:
					System.out.println("Interact with:" + currentMap.getThingAtPosition(posX - 1, posY).getName() + ". Heading WEST.");
					interact(posX - 1, posY, currentMap.getThingAtPosition(posX - 1, posY).getId());
					break;
				default:
					break;
			}
				
		}
	}
	
	public void interact(int interactX, int interactY, int interactedID) {
		if (interactedID == 3) {
			currentMap.setThing(interactX, interactY, 4);
		} else if (interactedID == 4) {
			currentMap.setThing(interactX, interactY, 3);
		}

	}
	
	public String toString() {
		return "Raw: (" + position.getX() + ", " + position.getY() + ")  Tile: (" + position.getX() / 16 +
				", " + position.getY() / 16 + ")  Sub: (" + getInnerPositionX() + ", " + getInnerPositionY() +
				")  Speed: " + speed + " un/sec, heading " + position.getHeading().toString();
	}
}
