package me.markeh.factionswarps.migration;

import java.util.ArrayList;
import java.util.List;

public abstract class Migrator<T> {

	private static List<Migrator<?>> migrators = new ArrayList<Migrator<?>>();
	public static void add(Migrator<?> migrator) {
		migrators.add(migrator);
		migrator.migrate();
	}
	
	public abstract void migrate();
	
}
