package me.markeh.factionswarps.migration.factionsuuid;

import java.util.Map.Entry;

import org.bukkit.Location;

import com.massivecraft.factions.P;
import com.massivecraft.factions.util.LazyLocation;

import me.markeh.factionsframework.entities.Faction;
import me.markeh.factionsframework.entities.Factions;
import me.markeh.factionswarps.Config;
import me.markeh.factionswarps.FactionsWarps;
import me.markeh.factionswarps.migration.Migrator;
import me.markeh.factionswarps.store.WarpData;

public class MigrateFactionsUUID extends Migrator<MigrateFactionsUUID> {

	private static MigrateFactionsUUID i;
	public static MigrateFactionsUUID get() {
		if (i == null) i = new MigrateFactionsUUID();
		return i;
	}
	
	@Override
	public void migrate() {
		// Migrate data if wanted
		if (Config.get().migrateFactionsUUIDWarps) {
			FactionsWarps.get().log("[Migrator] Starting FactionsUUID Warps Migrator, this will take a moment");
			
			for (com.massivecraft.factions.Faction fuuidFaction: com.massivecraft.factions.Factions.getInstance().getAllFactions()) {
				Faction faction = Factions.getById(fuuidFaction.getId());
				WarpData warpData = WarpData.get(faction);
				
				int count = 0;
				
				for(Entry<String, LazyLocation> fuuidWarp : fuuidFaction.getWarps().entrySet()) {
					String name = fuuidWarp.getKey().toLowerCase();
					Location location = fuuidWarp.getValue().getLocation();
					
					warpData.addWarp(name, location);
					count++;
				}
				
				FactionsWarps.get().log("[Migrator] Migrated " + count + " warps for " + faction.getName() );
			}
			
			FactionsWarps.get().log("[Migrator] Warps migrated! The configuration option has been reversed, and config.json saved.");
			
			Config.get().migrateFactionsUUIDWarps = false;
			Config.get().save();
		}
		
		// Remove FactionsUUID commands if wanted 
		if (Config.get().removeFactionsUUIDWarps) {
			P.p.cmdBase.subCommands.remove(P.p.cmdBase.cmdFWarp);
			P.p.cmdBase.subCommands.remove(P.p.cmdBase.cmdDelFWarp);
			P.p.cmdBase.subCommands.remove(P.p.cmdBase.cmdSetFWarp);			
		}
	}

}
