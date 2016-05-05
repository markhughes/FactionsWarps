package me.markeh.factionswarps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
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
import me.markeh.factionswarps.integration.Integration;
import me.markeh.factionswarps.integration.vault.IntegrationVault;
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
	
	private Metrics metrics;
	
	// -------------------------------------------------- //
	// METHODS  
	// -------------------------------------------------- //
	
	@Override
	public void onEnable() {
		
		if ( ! this.getServer().getPluginManager().isPluginEnabled("FactionsFramework")) {
			log(ChatColor.RED + "This plugin requires Factions Framework!");
			log(ChatColor.AQUA + "- Bukkit Dev: " + ChatColor.UNDERLINE + "http://dev.bukkit.org/bukkit-plugins/factionsframework/");
			log(ChatColor.AQUA + "- Spigot Resources: " + ChatColor.UNDERLINE + "https://www.spigotmc.org/resources/factions-framework.22278/");
			return;
		}
		
		FactionsCommandManager.get().add(CmdWarp.get());
		FactionsCommandManager.get().add(CmdWarpAdd.get());
		FactionsCommandManager.get().add(CmdWarpList.get());
		FactionsCommandManager.get().add(CmdWarpRemove.get());
		
		this.getServer().getPluginManager().registerEvents(this, this);
		
		// Integrations
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
			Integration.add(IntegrationVault.get());
		}
		
		if (Config.get().metrics) {
			try {
				metrics = new Metrics(this);
				metrics.enable();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		
		if (metrics != null) {
			try {
				metrics.disable();
				metrics = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			metrics = null;
		}
	}
	
	public void log(String msg) {
		this.getServer().getConsoleSender().sendMessage(msg);
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
