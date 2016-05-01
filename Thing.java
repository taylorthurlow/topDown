package tthurlow.topdown;

public class Thing {
	private final int id;
	private final String name;
	private final boolean isSolid, isInteractable;
	private static SpriteSheet sprites = Game.thingSprites;
	
	public Thing(int id, String name, boolean isSolid, boolean isInteractable) {
		this.id = id;
		this.name = name;
		this.isSolid = isSolid;
		this.isInteractable = false;
	}

	public boolean isSolid() {
		return isSolid;
	}
	
	public boolean isInteractable() {
		return isInteractable;
	}

	public int getId() {
		return id;
	}
	
	public static Thing getThingById(int id) {
		if (id == 0) return thingEmpty;
		if (id == 1) return thingRock;
		if (id == 2) return thingWeeds;
		else return thingEmpty;
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

	private static final Thing thingEmpty = new Thing(0, "empty", false, false);
	private static final Thing thingRock = new Thing(1, "Rock", true, false);
	private static final Thing thingWeeds = new Thing(2, "Weeds", false, false);
	
}
