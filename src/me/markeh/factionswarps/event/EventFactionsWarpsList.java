package me.markeh.factionswarps.event;

import org.bukkit.event.HandlerList;

import me.markeh.factionsframework.entities.FPlayer;
import me.markeh.factionsframework.entities.Faction;

public class EventFactionsWarpsList extends AbstractFactionsWarpsEvent<EventFactionsWarpsList> {

	public EventFactionsWarpsList(Faction faction, FPlayer fplayer) {
		super(faction, fplayer);
	}
	
    private static final HandlerList handlers = new HandlerList();
	
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
