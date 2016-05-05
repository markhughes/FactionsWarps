package me.markeh.factionswarps.event;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import me.markeh.factionsframework.entities.FPlayer;
import me.markeh.factionsframework.entities.Faction;

public class EventFactionsWarpsCreate extends AbstractFactionsWarpsEvent<EventFactionsWarpsCreate> {

	public EventFactionsWarpsCreate(Faction faction, FPlayer fplayer, Location location, String warpName, String password) {
		super(faction, fplayer);
		
		this.location = location;
		this.warpName = warpName;
		this.password = password;
	}

    private static final HandlerList handlers = new HandlerList();
    private Location location;
    private String warpName;
    private String password;
    
    public Location getLocation() {
    	return this.location;
    }
    
    public String getName() {
    	return this.warpName;
    }
    
    public Boolean hasPassword() {
    	return this.password != null;
    }
    
    public String getPassword() {
    	return this.password;
    }
    
    public void setLocation(Location location) {
    	this.location = location;
    }
    
    public void setName(String newName) {
    	this.warpName = newName;
    }
    
    public void setPassword(String newPassword) {
    	this.password = newPassword;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
