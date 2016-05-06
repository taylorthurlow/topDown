package tthurlow.topdown;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Scanner;

public class Map {
	
	private Tile mapTiles[][];
	private Thing mapThings[][];
	private int width = 0, height = 0;
	private EntityPosition spawnPosition;
	
	public Map(String fileName, EntityPosition spawnPosition) {
			this.spawnPosition = spawnPosition;

			/**
			 * Tiles
			 */
			
			String tilePath = URLDecoder.decode(getClass().getResource("maps/" + fileName + "_tiles" + ".csv").getPath());
			File tileFile = new File(tilePath);
			System.out.println("Tiles: " + tileFile.getPath());

			Scanner tileScanner;
			String tileString = "";
			try {
				tileScanner = new Scanner(tileFile);
				tileString = tileScanner.nextLine();
				while (tileScanner.hasNextLine()) {
					tileString = tileString + "|" + tileScanner.nextLine();
				}
				tileString = tileString.replace(",", "");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			char[] serializedTileMap = tileString.toCharArray();
			ArrayList<Character> delimitersRemovedTiles = new ArrayList<>();

			for (int i = 0; i < serializedTileMap.length; i++) {
				if (serializedTileMap[i] == '|') {
					if (this.width == 0) {
						this.width = i;
					}
				} else {
					delimitersRemovedTiles.add(serializedTileMap[i]);
				}
			}
			
			int length = delimitersRemovedTiles.toArray().length;
			height = length / width;
			
			mapTiles = new Tile[height - 1][width - 1];
			Tile[] tileArray = new Tile[(width * height)];
			for (int i = 0; i < tileArray.length; i++) {
				int got = Character.getNumericValue((char) delimitersRemovedTiles.toArray()[i]);
				tileArray[i] = Tile.getTileById(got);
			}
			mapTiles = monoToBidiTile(tileArray, height, width);

			/**
			 * Things
			 */
			
			String thingPath = URLDecoder.decode(getClass().getResource("maps/" + fileName + "_things" + ".csv").getPath());
			File thingFile = new File(thingPath);
			System.out.println("Tiles: " + thingFile.getPath());

			Scanner thingScanner;
			String thingString = "";
			try {
				thingScanner = new Scanner(thingFile);
				thingString = thingScanner.nextLine();
				while (thingScanner.hasNextLine()) {
					thingString = thingString + "|" + thingScanner.nextLine();
				}
				thingString = thingString.replace(",", "");
				thingString = thingString.replace("-1", "0");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			char[] serializedThingMap = thingString.toCharArray();
			ArrayList<Character> delimitersRemovedThings = new ArrayList<>();

			for (int i = 0; i < serializedThingMap.length; i++) {
				if (serializedTileMap[i] != '|') {
					delimitersRemovedThings.add(serializedThingMap[i]);
				}
				
			}
			
			mapThings = new Thing[height - 1][width - 1];
			Thing[] thingArray = new Thing[(width * height)];
			for (int i = 0; i < thingArray.length; i++) {
				int got = Character.getNumericValue((char) delimitersRemovedThings.toArray()[i]);
				thingArray[i] = Thing.getThingById(got);
			}
			mapThings = monoToBidiThing(thingArray, height, width);
	}
			
	
	/**
	 * Code here is from StackOverflow, covers converting a 1D array into a 2D array,
	 * filling each row before moving to a new one (x then y)
	 * http://stackoverflow.com/questions/5134555/how-to-convert-a-1d-array-to-2d-array
	 */
	public Tile[][] monoToBidiTile(final Tile[] array, final int rows, final int cols) {
	    if (array.length != (rows * cols))
	        throw new IllegalArgumentException("Invalid array length");

	    Tile[][] bidi = new Tile[rows][cols];
	    for (int i = 0; i < rows; i++)
	        System.arraycopy(array, (i*cols), bidi[i], 0, cols);

	    return bidi;
	}

	public Thing[][] monoToBidiThing(final Thing[] array, final int rows, final int cols) {
	    if (array.length != (rows * cols))
	        throw new IllegalArgumentException("Invalid array length");

	    Thing[][] bidi = new Thing[rows][cols];
	    for (int i = 0; i < rows; i++)
	        System.arraycopy(array, (i*cols), bidi[i], 0, cols);

	    return bidi;
	}
	
	public void render(Graphics g) {
		// Tiles
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int id = mapTiles[j][i].getId();
				int spriteX = (id % 16);
				int spriteY = (id / 16);
				g.drawImage(Tile.getSprites().crop(spriteX, spriteY, 16, 16), 16 * i, 16 * j, 16, 16, null);
			}
		}

		// Things
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int id = mapThings[j][i].getId();
				int spriteX = (id % 16);
				int spriteY = (id / 16);
				g.drawImage(Thing.getSprites().crop(spriteX, spriteY, 16, 16), 16 * i, 16 * j, 16, 16, null);
			}
		}
	}

	public void tick() {
		// tick map logic here
	}
	
	public void setTile(int x, int y, int newTileId) {
		mapTiles[y][x] = Tile.getTileById(newTileId);
	}

	public void setThing(int x, int y, int newThingId) {
		mapThings[y][x] = Thing.getThingById(newThingId);
	}
	
	public Tile getTileAtPosition(int posX, int posY) {
		if (posX > width || posY > height) {
			return null;
		} else {
			return mapTiles[posY][posX];
		}
	}
	
	public Thing getThingAtPosition(int posX, int posY) {
		if (posX > width || posY > height) {
			return null;
		} else {
			return mapThings[posY][posX];
		}
	}

	public Tile[][] getTiles() {
		return mapTiles;
	}

	public Thing[][] getThings() {
		return mapThings;
	}

	public EntityPosition getSpawnPosition() {
		return spawnPosition;
	}
}
