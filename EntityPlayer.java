package tthurlow.topdown;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import tthurlow.topdown.EntityPosition.Direction;

public class EntityPlayer extends Entity implements KeyListener {

	public EntityPlayer(EntityPosition playerPosition, SpriteSheet sprites, Map map) {
		super(playerPosition, sprites, map);
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
	}
}
