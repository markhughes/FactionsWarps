package me.markeh.factionswarps;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import me.markeh.factionsframework.enums.Rel;
import me.markeh.factionsframework.jsonconf.JSONConf;

public class Config extends JSONConf<Config> {
	
	// -------------------------------------------------- //
	// GETTER  
	// -------------------------------------------------- //
	
	private static Path path = Paths.get(FactionsWarps.get().getDataFolder().toString(), "config.json");
	
	private static Config i;
	public static Config get() {
		if (i == null) {
			i = new Config();
			i.load();
			i.save();
		}
		
		return i;
	}
	
	private Config() { }
	
	// -------------------------------------------------- //
	// FIELDS  
	// -------------------------------------------------- //
	
	// Should metrics be enabled?
	public Boolean metrics = true;
	
	// Minimum ranks for managing, lising, and using warps
	public Rel minimumManage = Rel.OFFICER;
	public Rel minimumList = Rel.RECRUIT;
	public Rel minimumUse = Rel.RECRUIT;
	
	// Should warps only be in the territory?
	public Boolean mustBeInTerritory = true;
	
	// Warmups and cooldowns
	public Integer secondsWarmup = 3;
	public Integer secondsCooldown = 5;
	
	// Set to true to allow being lenient with cooldowns/warmups
	// so if there is less than 850ms remaining, we bypass
	public Boolean secondsLenient = true;
	
	// Distance from eneimes 
	public Double distanceFromEnemiesMinimum = 5.0;
	public Boolean distanceFromEnemiesIgnoreInOwn = true;
	
	// -------------------------------------------------- //
	// METHODS  
	// -------------------------------------------------- //
	
	public void save() {
		try {
			this.saveTo(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		try {
			this.loadFrom(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
