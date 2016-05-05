package me.markeh.factionswarps.integration;

import java.util.ArrayList;
import java.util.List;

public abstract class Integration {
	
	private static List<Integration> enabledIntegrations = new ArrayList<Integration>();
	public static void add(Integration integration) {
		enabledIntegrations.add(integration);
		integration.enable();
	}
	
	public static void remove(Integration integration) {
		integration.disable();
		enabledIntegrations.remove(integration);
	}
	
	public static List<Integration> getIntegrations() {
		return new ArrayList<Integration>(enabledIntegrations);
	}
	
	public abstract void enable();
	
	public abstract void disable();
	
}
