package me.markeh.factionswarps.event;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import me.markeh.factionsframework.entities.FPlayer;
import me.markeh.factionsframework.entities.Faction;

public class EventFactionsWarpsUse extends AbstractFactionsWarpsEvent<EventFactionsWarpsUse> {

	public EventFactionsWarpsUse(Faction faction, FPlayer fplayer, String warpName, String passwordProvided, Location targetLocation) {
		super(faction, fplayer);
		
		this.warpName = warpName;
		this.passwordProvided = passwordProvided;
		this.targetLocation = targetLocation;
	}
	
    private static final HandlerList handlers = new HandlerList();
    private String warpName;
    private String passwordProvided;
    private Location targetLocation;
    
    public String getName() {
    	return this.warpName;
    }
    
    public String getPassword() {
    	return this.passwordProvided;
    }
    
    public Location getTargetLocation() {
    	return this.targetLocation;
    }
    
    public void setTargetLocation(Location location) {
    	this.targetLocation = location;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
