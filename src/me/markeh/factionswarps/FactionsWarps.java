package me.markeh.factionswarps;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.markeh.factionsframework.command.FactionsCommandManager;
import me.markeh.factionsframework.event.EventFactionsDisband;
import me.markeh.factionswarps.commands.*;
import me.markeh.factionswarps.store.WarpData;

public class FactionsWarps extends JavaPlugin implements Listener {

	// -------------------------------------------------- //
	// SINGLETON  
	// -------------------------------------------------- //
	
	private static FactionsWarps i;
	public static FactionsWarps get() { return i; }
	public FactionsWarps() { i = this; }

	// -------------------------------------------------- //
	// FIELDS  
	// -------------------------------------------------- //
	
	public Map<String, Long> cooldownMap = new HashMap<String, Long>();
	public Map<String, BukkitTask> warmupMap = new HashMap<String, BukkitTask>();
	
	// -------------------------------------------------- //
	// METHODS  
	// -------------------------------------------------- //
	
	@Override
	public void onEnable() {
		FactionsCommandManager.get().add(CmdWarp.get());
		FactionsCommandManager.get().add(CmdWarpAdd.get());
		FactionsCommandManager.get().add(CmdWarpList.get());
		FactionsCommandManager.get().add(CmdWarpRemove.get());
		
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		FactionsCommandManager.get().remove(CmdWarp.get());
		FactionsCommandManager.get().remove(CmdWarpAdd.get());
		FactionsCommandManager.get().remove(CmdWarpList.get());
		FactionsCommandManager.get().remove(CmdWarpRemove.get());
		
		// Save all our warps 
		for (WarpData warpData : WarpData.getAll()) {
			warpData.save();
		}
	}
	
	public void pingWarmup(Player player) {
		String playerId = player.getUniqueId().toString();
		
		if ( ! FactionsWarps.get().warmupMap.containsKey(playerId)) return;
		
		this.warmupMap.get(playerId).cancel();
		this.warmupMap.remove(playerId);
		
		player.sendMessage(ChatColor.RED + "Warm up for warp cancelled ... ");
	}
	
	@EventHandler
	public void onFactionDisband(EventFactionsDisband event) {
		if (event.isCancelled()) return;
		
		// Remove the warp data
		WarpData.get(event.getFaction()).remove();
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent event) {
		if (event.getPlayer().hasPermission("factionswarps.bypass.warmups")) return;
		
		this.pingWarmup(event.getPlayer());
	}
	
}
