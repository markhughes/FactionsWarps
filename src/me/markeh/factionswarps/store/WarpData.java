package me.markeh.factionswarps.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.markeh.factionsframework.entities.Faction;
import me.markeh.factionsframework.jsonconf.JSONConf;
import me.markeh.factionswarps.Config;
import me.markeh.factionswarps.FactionsWarps;

public class WarpData extends JSONConf<WarpData> {
	
	// -------------------------------------------------- //
	// FIELDS  
	// -------------------------------------------------- //
	
	// Warp locations 
	public Map<String, String> warpLocations = new HashMap<String, String>();
	
	// Warp passwords
	public Map<String, String> warpPasswords = new HashMap<String, String>();

	// This is not how I usually organise classes, however
	// it makes it easier having fields up the top.

	// -------------------------------------------------- //
	// METHODS  
	// -------------------------------------------------- //
	
	public final Boolean warpExists(String name) {
		return (this.warpLocations.containsKey(this.nameify(name)));
	}
	
	public final Boolean hasPassword(String name) {
		return (this.warpPasswords.containsKey(this.nameify(name)));
	}
	
	public final Boolean isPassword(String name, String password) {
		if (Config.get().passwordsCaseSensitive) {
			return (this.warpPasswords.get(this.nameify(name)).equals(password));
		}
		
		return (this.warpPasswords.get(this.nameify(name)).equalsIgnoreCase(password));
	}
	
	public final Location getLocation(String name) {
		return this.stringToLocation(this.warpLocations.get(this.nameify(name)));
	}
	
	public final void addWarp(String name, Location location) {
		this.warpLocations.put(this.nameify(name), this.locationToString(location));
	}
	
	public final void addWarp(String name, String password, Location location) {
		this.warpPasswords.put(this.nameify(name), password);
		this.addWarp(name, location);
	}
	
	public final void removeWarp(String name) {
		name = this.nameify(name);
		
		if (this.warpLocations.containsKey(name)) this.warpLocations.remove(name);
		if (this.warpPasswords.containsKey(name)) this.warpPasswords.remove(name);
	}
	
	public final void remove() {
		this.warpLocations.clear();
		this.warpPasswords.clear();
		
		try {
			Files.delete(this.dataPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Setting the dataPath to null will prevent #save() from working
		this.dataPath = null;
		
		// Clear from the static map
		warpDataMap.remove(this.factionId);
	}
	
	// -------------------------------------------------- //
	// UTILS  
	// -------------------------------------------------- //
	
	private String nameify(String name) {
		return name.trim().toLowerCase();
	}
	
	private String locationToString(Location location) {
		String raw = location.getWorld().getUID().toString() + " :: " +
					 location.getX() + " :: " + 
					 location.getY() + " :: " + 
					 location.getZ() + " :: " + 
					 location.getPitch() + " :: " + 
					 location.getYaw();
		
		return raw;
	}
	
	private Location stringToLocation(String raw) {
		String[] rawArray = raw.split(" :: ");
		
		if (rawArray.length != 6) return null;
		
        Location location = new Location(
        		Bukkit.getWorld(UUID.fromString(rawArray[0])),
        		Double.parseDouble(rawArray[1]),
        		Double.parseDouble(rawArray[2]),
        		Double.parseDouble(rawArray[3]),
        		Float.parseFloat(rawArray[5]),
        		Float.parseFloat(rawArray[4]));

		return location;
	}
	
	// -------------------------------------------------- //
	// CONSTRUCT  
	// -------------------------------------------------- //
	
	private transient static Map<String, WarpData> warpDataMap = new HashMap<String, WarpData>();
	public static WarpData get(Faction faction) {
		String id = faction.getId();
		
		if ( ! warpDataMap.containsKey(id)) {
			warpDataMap.put(id, new WarpData(id));
		}
		
		return warpDataMap.get(id);
	}
	
	public static List<WarpData> getAll() {
		return new ArrayList<WarpData>(warpDataMap.values());
	}
	
	private transient Path dataPath;
	private transient String factionId;
	private WarpData(String factionId) {
		this.factionId = factionId;
		
		Path warpsFolder = Paths.get(FactionsWarps.get().getDataFolder().toString(), "warps");
		
		dataPath = Paths.get(warpsFolder.toString(), factionId + ".json");
		
		try { 
			this.loadFrom(dataPath);
			this.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  final void save() {
		if (this.dataPath == null) return;
		
		try {
			this.saveTo(dataPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
}
