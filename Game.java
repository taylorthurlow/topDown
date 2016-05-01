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

import tthurlow.topdown.UnitPosition.Direction;

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
	private Canvas canvas;
	private BufferStrategy strategy; 
	private BufferedImage background;
	private Graphics2D backgroundGraphics;
	private Graphics2D graphics;
	private JFrame frame;
	public static int width     = 640;
	public static int height    = 480;
	public static int scale     = 1; // this doesnt work quite right
	private int framesPerSecond = 0;
	private int fpsMeter        = 0;
	private GraphicsConfiguration config =
				GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
	
	// Create a hardware accelerated image
	public final BufferedImage create(final int width, final int height,
			final boolean alpha) {
		return config.createCompatibleImage(width, height, alpha
    			? Transparency.TRANSLUCENT : Transparency.OPAQUE);
	}

	public static SpriteSheet tileSprites, thingSprites, charSprites;
	private BufferedImage tileSheet, thingSheet, charSheet;
	private Player player;
	private Map testmap;
	
	public Game() {
		// Asset and map loading
		ImageLoader loader = new ImageLoader();
		tileSheet    = loader.load("./sprites/tiles.png");
		thingSheet   = loader.load("./sprites/things.png");
		charSheet    = loader.load("./sprites/chars.png");
		tileSprites  = new SpriteSheet(tileSheet);
		thingSprites = new SpriteSheet(thingSheet);
		charSprites  = new SpriteSheet(charSheet);
		testmap      = new Map("export", new UnitPosition(32, 32, Direction.SOUTH));
		player       = new Player(testmap.getSpawnPosition(), charSprites, testmap);
		
		// JFrame
		frame = new JFrame();
		frame.addWindowListener(new FrameClose());
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setSize(width * scale, height * scale);
		frame.addKeyListener(player);
		frame.setVisible(true);
		
		// Canvas
		canvas = new Canvas(config);
		canvas.setSize(width * scale, height * scale);
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
		long fpsWait = (long) (1.0 / 60 * 1000);
		main: while (isRunning) {
			long renderStart = System.nanoTime();
			updateGame();
			
			// Update Graphics
			do {
				Graphics2D bg = getBuffer();
				if (!isRunning) {
					break main;
				}
				renderGame(backgroundGraphics); // this calls your draw method
				if (scale != 1) {
					bg.drawImage(background, 0, 0, width * scale, height * scale, 
							0, 0, width, height, null);
				} else {
					bg.drawImage(background, 0, 0, null);
				}
				bg.dispose();
			} while (!updateScreen());
			
			
			// FPS limiting here
			long renderTime = (System.nanoTime() - renderStart) / 1000000;
			
			try {
				Thread.sleep(Math.max(0, fpsWait - renderTime));
			} catch (InterruptedException e) {
				Thread.interrupted();
				break;
			}
			renderTime = (System.nanoTime() - renderStart) / 1000000;
			framesPerSecond = (int) (1000 / renderTime);
		}
		frame.dispose();
	}
	
	private void setFpsMeter(int fps) {
		this.fpsMeter = fps;
	}
	
	public void updateGame() {
		player.tick();
		setFpsMeter(framesPerSecond);
	}
	
	public void renderGame(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, width, height);
		
		// Render map
		testmap.render(g, 0, 0);
		
		// Render debug
		if (isDebugging) {
			int selX = player.getPosition().getX() - player.getPosition().getX() % 16;
			int selY = player.getPosition().getY() - player.getPosition().getY() % 16;
			g.setColor(Color.DARK_GRAY);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
			g.fillRect(selX, selY, 16, 16);
		}
		
		player.render(g);
		
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.drawString("FPS: " + fpsMeter, 5, 15);
	}
	
	public static void main(String[] args) {
		new Game();
	}
	
	public static String getVersion() {
		return "v0.1";
	}
}
