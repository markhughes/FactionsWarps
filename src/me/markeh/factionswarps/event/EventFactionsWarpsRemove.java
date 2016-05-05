package me.markeh.factionswarps.event;

import org.bukkit.event.HandlerList;

import me.markeh.factionsframework.entities.FPlayer;
import me.markeh.factionsframework.entities.Faction;

public class EventFactionsWarpsRemove extends AbstractFactionsWarpsEvent<EventFactionsWarpsRemove> {

	public EventFactionsWarpsRemove(Faction faction, FPlayer fplayer, String warpName) {
		super(faction, fplayer);
		
		this.warpName = warpName;
	}

    private static final HandlerList handlers = new HandlerList();
    private String warpName;
    
    public String getWarpName() {
    	return this.warpName;
    }
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
