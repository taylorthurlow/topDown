package tthurlow.topdown;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import tthurlow.topdown.EntityPosition.Direction;

/**
 * Code originally pulled from StackOverflow question:
 * http://stackoverflow.com/questions/26455018/2d-top-down-java-1-8-game-from-scratchnothing
 * 
 * Heavily modified with answers from:
 * http://stackoverflow.com/questions/1963494/java-2d-game-graphics
 * 
 * Loop and interpolation code partly from:
 * http://stackoverflow.com/questions/771206/how-do-i-cap-my-framerate-at-60-fps-in-java
 */

public class Game extends Thread {
	private boolean isRunning   = true;
	private boolean isDebugging = true;
	private BufferStrategy strategy;
	private BufferedImage background;
	private Graphics2D backgroundGraphics;
	private Graphics2D graphics;
	private Graphics2D bg;
	private JFrame frame;
	private static int width  = 640;
	private static int height = 480;

	private static int TICKS_PER_SECOND = 25;
	private static int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
	private static int MAX_FRAMESKIP = 5;
	private static long NEXT_GAME_TICK = System.nanoTime() / 1000000;
	private static int RENDER_FPS = 0;
	private static float interpolation;

	private Font debugFont = new Font("Arial", Font.BOLD, 12);
	private int selX = 0;
	private int selY = 0;

	private GraphicsConfiguration config =
				GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
	
	// Create a hardware accelerated image
	private BufferedImage create(final int width, final int height, final boolean alpha) {
		return config.createCompatibleImage(width, height, alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
	}

	static SpriteSheet tileSprites, thingSprites, charSprites;
	private EntityPlayer player;
	private Map testMap;
	
	private Game() {
		// Asset and map loading
		ImageLoader loader = new ImageLoader();
		BufferedImage tileSheet = loader.load("./sprites/tiles.png");
		BufferedImage thingSheet = loader.load("./sprites/things.png");
		BufferedImage charSheet = loader.load("./sprites/chars.png");
		tileSprites  = new SpriteSheet(tileSheet);
		thingSprites = new SpriteSheet(thingSheet);
		charSprites  = new SpriteSheet(charSheet);
		testMap      = new Map("export", new EntityPosition(2, 2, Direction.SOUTH));
		player       = new EntityPlayer(testMap.getSpawnPosition(), charSprites, testMap);
		
		// JFrame
		frame = new JFrame();
		frame.addWindowListener(new FrameClose());
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setSize(width, height);
		frame.addKeyListener(player);
		frame.setVisible(true);

		// Canvas
		Canvas canvas = new Canvas(config);
		canvas.setSize(width, height);
		canvas.setFocusable(false);
		frame.add(canvas, 0);
		
		// Background & Buffer
		background = create(width, height, false);
		canvas.createBufferStrategy(2);;
		do {
			strategy = canvas.getBufferStrategy();
		} while (strategy == null);
		
		start();
	}
	
	private class FrameClose extends WindowAdapter {
		@Override
		public void windowClosing(final WindowEvent e) {
			isRunning = false;
		}
	}
	
	// Screen and buffer stuff
	private Graphics2D getBuffer() {
		if (graphics == null) {
			try {
				graphics = (Graphics2D) strategy.getDrawGraphics();
			} catch (IllegalStateException e) {
				return null;
			}
		}
		return graphics;
	}
	
	private boolean updateScreen() {
		graphics.dispose();
		graphics = null;
		try {
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
			return (!strategy.contentsLost());
		} catch (NullPointerException e) {
			return true;
		} catch (IllegalStateException e) {
			return true;
		}
	}
	
	public void run() {
		backgroundGraphics = (Graphics2D) background.getGraphics();

		main: while (isRunning) {

			int loops = 0;

			while ((System.nanoTime() / 1000000) > NEXT_GAME_TICK && loops < MAX_FRAMESKIP) {
				tick();
				NEXT_GAME_TICK += SKIP_TICKS;
				loops++;
			}

			interpolation = (float) ((System.nanoTime() / 1000000) + SKIP_TICKS - NEXT_GAME_TICK) / (float) (SKIP_TICKS);
			render(interpolation);

			setFpsMeter((int) ((System.nanoTime() / 1000000) + SKIP_TICKS - NEXT_GAME_TICK));

			/**
			try {
				int sleepTime = (int) (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
				System.out.println("sleepTime: " + sleepTime);
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				Thread.interrupted();
				break;
			}
			 **/
		}
		frame.dispose();
	}

	private void render(float interpolation) {
		do {
			bg = getBuffer();
			if (!isRunning) {
				/** This worked before render() was its own method. Not sure
				 * if this will cause issues in the future with closing the
				 * app or not.
				 */
				//break main;
			}
			renderGame(backgroundGraphics, interpolation); // this calls your draw method
			bg.drawImage(background, 0, 0, width, height, 0, 0, width, height, null);
			bg.dispose();
		} while (!updateScreen());
	}

	private void setFpsMeter(int fps) {
		RENDER_FPS = fps;
	}
	
	private void tick() {
		player.tick();
		testMap.tick();
	}
	
	private void renderGame(Graphics2D g, float interpolation) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, width, height);

		testMap.render(g);
		
		if (isDebugging) {
			// Current player square highlight
			selX = player.getPosition().getTileX() * 16;
			selY = player.getPosition().getTileY() * 16;
			g.setColor(Color.DARK_GRAY);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
			g.fillRect(selX, selY, 16, 16);

			// Debug background
			//g.fillRect(3, 3, 100, 45);

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));

			// FPS meter	
			g.setColor(Color.YELLOW);
			g.setFont(debugFont);
			//g.drawString("FPS: " + RENDER_FPS, 5, 15);

			// Position data
			//g.drawString("X: " + player.getPosition().getTileX() + "(" + player.getPosition().getInnerPositionX() +
			//		") [" +	player.getPosition().getX() + "]", 5, 30);
			//g.drawString("Y: " + player.getPosition().getTileY() + "(" + player.getPosition().getInnerPositionY() +
			//		") [" +	player.getPosition().getY() + "]", 5, 45);
		}
		
		player.render(g, interpolation);

		// Render UI here
	}
	
	public static void main(String[] args) {
		new Game();
	}
	
	public static String getVersion() {
		return "v0.1";
	}
}
