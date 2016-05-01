package tthurlow.topdown;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Scanner;

public class Map {
	
	private Tile mapTiles[][];
	private Thing mapObjects[][];
	private int width = 0, height = 0;
	private UnitPosition spawnPosition;
	
	public Map(String fileName, UnitPosition spawnPosition) {
			this.spawnPosition = spawnPosition;	
			
			String path = URLDecoder.decode(getClass().getResource("maps/" + fileName + ".csv").getPath());
			File file = new File(path);
			System.out.println(file.getPath());

			Scanner scanner;
			String theString = "";
			try {
				scanner = new Scanner(file);
				theString = scanner.nextLine();
				while (scanner.hasNextLine()) {
					theString = theString + "|" + scanner.nextLine();
				}
				theString = theString.replace(",", "");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			char[] serializedMap = theString.toCharArray();
			ArrayList<Character> delimitersRemoved = new ArrayList<>();
			
			for (int i = 0; i < serializedMap.length; i++) {
				if (serializedMap[i] == '|') {
					if (this.width == 0) {
						this.width = i;
					}
				} else {
					delimitersRemoved.add(serializedMap[i]);
				}
			}
			
			int length = delimitersRemoved.toArray().length;
			height = length / width;
			
			mapTiles = new Tile[height - 1][width - 1];
			Tile[] tileArray = new Tile[(width * height)];
			for (int i = 0; i < tileArray.length; i++) {
				int got = Character.getNumericValue((char) delimitersRemoved.toArray()[i]);
				tileArray[i] = Tile.getTileById(got);
			}
			mapTiles = monoToBidi(tileArray, height, width);
	}
	
	/**
	 * Code here is from StackOverflow, covers converting a 1D array into a 2D array,
	 * filling each row before moving to a new one (x then y)
	 * http://stackoverflow.com/questions/5134555/how-to-convert-a-1d-array-to-2d-array
	 */
	public Tile[][] monoToBidi(final Tile[] array, final int rows, final int cols) {
	    if (array.length != (rows * cols))
	        throw new IllegalArgumentException("Invalid array length");

	    Tile[][] bidi = new Tile[rows][cols];
	    for (int i = 0; i < rows; i++)
	        System.arraycopy(array, (i*cols), bidi[i], 0, cols);

	    return bidi;
	}
	
	public void render(Graphics g, int x, int y) {		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int id = mapTiles[j][i].getId();
				int spriteX = (id % 16);
				int spriteY = (id / 16);
				g.drawImage(Tile.getSprites().crop(spriteX, spriteY, 16, 16), 16 * i, 16 * j, 16 * Game.scale, 16 * Game.scale, null);
			}
		}
	}
	
	public void setTile(int x, int y, int newTileId) {
		mapTiles[y][x] = Tile.getTileById(newTileId);
	}
	
	public Tile getTileAtPosition(int posX, int posY) {
		if (posX > width || posY > height) {
			return null;
		} else {
			return mapTiles[posY][posX];
		}
	}

	public Tile[][] getTiles() {
		return mapTiles;
	}

	public UnitPosition getSpawnPosition() {
		return spawnPosition;
	}
}
