package me.markeh.factionswarps.integration.vault;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.markeh.factionswarps.FactionsWarps;
import me.markeh.factionswarps.integration.Integration;
import net.milkbowl.vault.economy.Economy;

public class IntegrationVault extends Integration {

	private static IntegrationVault i;
	public static IntegrationVault get() {
		if (i == null) {
			i = new IntegrationVault();
		}
		return i;
	}
	
	private Economy econ = null;
	
	public Economy getEcon() {
		if (this.econ == null) {
			RegisteredServiceProvider<Economy> rsp = FactionsWarps.get().getServer().getServicesManager().getRegistration(Economy.class);
			if (rsp == null) {
				FactionsWarps.get().log("<red>Vault enabled, but couldn't find an economy provider. ");
				return null;
			}
			
			this.econ = rsp.getProvider();
		}
		
		return this.econ;
	}
	
	@Override
	public void enable() {
		FactionsWarps.get().getServer().getPluginManager().registerEvents(VaultListener.get(), FactionsWarps.get());
	}

	@Override
	public void disable() {
		HandlerList.unregisterAll(VaultListener.get());
	}

}
