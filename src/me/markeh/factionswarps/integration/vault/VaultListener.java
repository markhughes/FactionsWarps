package me.markeh.factionswarps.integration.vault;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.markeh.factionswarps.Config;
import me.markeh.factionswarps.event.EventFactionsWarpsCreate;
import me.markeh.factionswarps.event.EventFactionsWarpsList;
import me.markeh.factionswarps.event.EventFactionsWarpsRemove;
import me.markeh.factionswarps.event.EventFactionsWarpsUse;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultListener implements Listener {
	
	private static VaultListener i;
	public static VaultListener get() {
		if (i == null) i = new VaultListener();
		return i;
	}
	
	@EventHandler
	public void chargeOnCreate(EventFactionsWarpsCreate event) {
		if (Config.get().costCreateWarps <= 0) return;
		
		EconomyResponse response = IntegrationVault.get().getEcon().withdrawPlayer(event.getFPlayer().asBukkitPlayer(), Config.get().costCreateWarps);
		if ( ! response.transactionSuccess()) {
			event.setCancelled(true);
			
			if (Config.get().chargeNotificationOnFail) {
				event.getFPlayer().msg("<red>You can't afford to set this warp, it costs " + Config.get().costCreateWarps);
			}
			return;
		} 
		
		if (Config.get().chargeNotificationOnSuccess) {
			event.getFPlayer().msg("<gold>You were charged " + Config.get().costCreateWarps + " to create the warp.");
		}
	}
	
	@EventHandler
	public void chargeOnList(EventFactionsWarpsList event) {
		if (Config.get().costListWarps <= 0) return;
		
		EconomyResponse response = IntegrationVault.get().getEcon().withdrawPlayer(event.getFPlayer().asBukkitPlayer(), Config.get().costListWarps);
		if ( ! response.transactionSuccess()) {
			event.setCancelled(true);
			
			if (Config.get().chargeNotificationOnFail) {
				event.getFPlayer().msg("<red>You can't afford to list warps, it costs " + Config.get().costListWarps);
			}
			return;
		} 
		
		if (Config.get().chargeNotificationOnSuccess) {
			event.getFPlayer().msg("<gold>You were charged " + Config.get().costListWarps + " to list warps.");
		}
	}
	
	@EventHandler
	public void chargeOnUse(EventFactionsWarpsUse event) {
		if (Config.get().costUseWarps <= 0) return;
		
		EconomyResponse response = IntegrationVault.get().getEcon().withdrawPlayer(event.getFPlayer().asBukkitPlayer(), Config.get().costUseWarps);
		if ( ! response.transactionSuccess()) {
			event.setCancelled(true);
			
			if (Config.get().chargeNotificationOnFail) {
				event.getFPlayer().msg("<red>You can't afford to use this warp, it costs " + Config.get().costUseWarps);
			}
			return;
		} 
		
		if (Config.get().chargeNotificationOnSuccess) {
			event.getFPlayer().msg("<gold>You were charged " + Config.get().costUseWarps + " to use this warp.");
		}
	}
	
	@EventHandler
	public void refundOnRemove(EventFactionsWarpsRemove event) {
		if (Config.get().costRemoveWarps <= 0) return;
		
		EconomyResponse response = IntegrationVault.get().getEcon().depositPlayer(event.getFPlayer().asBukkitPlayer(), Config.get().costRemoveWarps);
		if ( ! response.transactionSuccess()) {
			event.setCancelled(true);
			
			if (Config.get().chargeNotificationOnFail) {
				event.getFPlayer().msg("<red>For some reason, we couldn't refund you for removing this warp.");
			}
			return;
		} 
		
		if (Config.get().chargeNotificationOnSuccess) {
			event.getFPlayer().msg("<gold>You were refunded " + Config.get().costRemoveWarps + " for removing this warp.");
		}
	}
	
}
