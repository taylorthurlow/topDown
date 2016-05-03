package tthurlow.topdown;

public class Tile {
	private final int id;
	private final String name;
	private final boolean isSolid, isTrigger;
	private static SpriteSheet sprites = Game.tileSprites;
	
	public Tile(int id, String name, boolean isSolid) {
		this.id = id;
		this.name = name;
		this.isSolid = isSolid;
		this.isTrigger = false;
	}

	public boolean isSolid() {
		return isSolid;
	}
	
	public boolean isTrigger() {
		return isTrigger;
	}

	public int getId() {
		return id;
	}
	
	public static Tile getTileById(int id) {
		if (id == 0) return tileBlackness;
		if (id == 1) return tileGrass;
		if (id == 2) return tileStone;
		if (id == 3) return tileWater;
		else return tileBlackness;
	}
	
	public static SpriteSheet getSprites() {
		return sprites;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name + "(" + id + ")";
	}

	private static final Tile tileBlackness = new Tile(0, "Blackness", true);
	private static final Tile tileGrass = new Tile(1, "Grass", false);
	private static final Tile tileStone = new Tile(2, "Stone", false);
	private static final Tile tileWater = new Tile(3, "Water", true);
}
