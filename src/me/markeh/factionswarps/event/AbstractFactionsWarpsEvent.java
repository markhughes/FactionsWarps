package me.markeh.factionswarps.event;

import org.bukkit.event.Event;

import me.markeh.factionsframework.entities.FPlayer;
import me.markeh.factionsframework.entities.Faction;

public abstract class AbstractFactionsWarpsEvent<T> extends Event {
	
	public AbstractFactionsWarpsEvent(Faction faction, FPlayer fplayer) {
		this.faction = faction;
		this.fplayer = fplayer;
	}
	
	private Faction faction;
	private FPlayer fplayer;
	private Boolean cancelled = false;
	
	public Faction getFaction() {
		return this.faction;
	}
	
	public FPlayer getFPlayer() {
		return this.fplayer;
	}
	
	public Boolean isCancelled() {
		return this.cancelled;
	}
	
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public void call() {
		// TODO: call
	}

}
