package me.markeh.factionswarps.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqInFaction;
import me.markeh.factionsframework.command.requirements.ReqRankAtLeast;
import me.markeh.factionsframework.entities.FPlayer;
import me.markeh.factionsframework.entities.FPlayers;
import me.markeh.factionsframework.entities.Factions;
import me.markeh.factionsframework.enums.Rel;
import me.markeh.factionswarps.Config;
import me.markeh.factionswarps.FactionsWarps;
import me.markeh.factionswarps.event.EventFactionsWarpsUse;
import me.markeh.factionswarps.store.WarpData;

public class CmdWarp extends FactionsCommand {
		
	// -------------------------------------------------- //
	// COMMAND CONSTRUCT  
	// -------------------------------------------------- //
	
	private static CmdWarp instance = new CmdWarp();
	public static CmdWarp get() { return instance; }
	
	public CmdWarp() {
		this.addAlias("warp");
		this.setPermission("factions.warp.use");
		
		this.setDescription("add a faction warp");
		
		this.addRequiredArgument("name");
		this.addOptionalArgument("password", "none");
		
		this.addRequirement(ReqInFaction.get(this));
		this.addRequirement(ReqRankAtLeast.get(this, Config.get().minimumUse));
	}
	
	// -------------------------------------------------- //
	// METHODS  
	// -------------------------------------------------- //
	
	public void run() throws Exception {
		if (this.getArg(0) == null || this.getFPlayer().getFaction() == null || this.getFPlayer().getFaction().isNone()) {
			Bukkit.dispatchCommand(this.getSender(), "f warp ?");
			return;
		}
				
		String warp = this.getArg(0);
		
		WarpData warpData = WarpData.get(this.getFPlayer().getFaction());
		
		if ( ! warpData.warpExists(warp)) {
			msg("<red>{name} doesn't seem to exist", "name", warp);
			return;
		}
		
		if (Factions.getFactionAt(warpData.getLocation(warp)).getId() != this.getFPlayer().getFaction().getId()) {
			msg("<red>That warp isn't in your faction land and has been removed.");
			warpData.removeWarp(warp);
			return;
		}
		
		// Check our requirements...
		
		if (Config.get().reqMinimumClaims > 0 && Config.get().reqBlocksUseWarp) {
			if (this.getFPlayer().getFaction().getLandCount() < Config.get().reqMinimumClaims) {
				msg("<red>You need to have claimed at least {min} chunks to use warps.",
					"min", Config.get().reqMinimumClaims.toString());
				return;
			}
		}
		
		if (Config.get().reqMinimumMembers > 0 && Config.get().reqBlocksUseWarp) {
			if (this.getFPlayer().getFaction().getMembers().size() < Config.get().reqMinimumMembers) {
				msg("<red>You need to have at least {min} members to use warps.",
					"min", Config.get().reqMinimumMembers.toString());
				return;
			}
		}
		
		if (Config.get().reqMinimumFactionPower > 0 && Config.get().reqBlocksUseWarp) {
			if (this.getFPlayer().getFaction().getPower() < Config.get().reqMinimumFactionPower) {
				msg("<red>Your faction must have at least {min} power to use warps.",
					"min", Config.get().reqMinimumFactionPower.toString());
				return;
			}
		}
		
		final String password = this.getArg(1);
		
		if (warpData.hasPassword(warp)) {			
			if ( ! warpData.isPassword(warp, password)) {
				msg("<red>Invalid password, please specify the correct password.");
				return;
			}
		}
		
		// Check for enemies if required 
		if (Config.get().distanceFromEnemiesMinimum > 0) {
			for (Player player : this.getFPlayer().getWorld().getPlayers()) {
				if (player.getLocation().distance(this.getFPlayer().getLocation()) <= Config.get().distanceFromEnemiesMinimum) {
					FPlayer otherPlayer = FPlayers.getBySender(player);
					
					if (otherPlayer.getRelationTo(this.getFPlayer()) == Rel.ENEMY) {
						msg("<red>There is an enemy nearby, you can't warp!");
						return;
					}
				}
			}
		}
		
		// Check cooldown if required 
		if (Config.get().secondsCooldown > 0 && ! this.getFPlayer().asBukkitPlayer().hasPermission("factionswarps.bypass.cooldowns")) {
			if (FactionsWarps.get().cooldownMap.containsKey(this.getFPlayer().getId())) {
				Long lastWarpAt = FactionsWarps.get().cooldownMap.get(this.getFPlayer().getId());
				
				Long compare = Config.get().secondsCooldown - ((System.currentTimeMillis() - lastWarpAt) / 1000);
				
				if (compare > 0L) {
					if (Config.get().secondsLenient && compare < (long) 0.85) {
						
					} else {
						msg("<red>You must wait <gold>" + Config.get().secondsCooldown + " seconds<red> between warps! You have <gold>" + compare + " seconds<red> left.");
					}
					
					return;
				}
			}
			
			FactionsWarps.get().cooldownMap.put(this.getFPlayer().getId(), System.currentTimeMillis());
		}
		
		if (Config.get().secondsWarmup > 0 && ! this.getFPlayer().asBukkitPlayer().hasPermission("factionswarps.bypass.warmups")) {
			msg("<green>Teleporting you in <gold>" + Config.get().secondsWarmup + " <green>seconds ...");
						
			BukkitTask task = new BukkitRunnable() {
				private FPlayer teleporting;
				private Location to;
				private String name;
				
				public BukkitRunnable set(FPlayer teleporting, Location to, String name) {
					this.teleporting = teleporting;
					this.to = to;
					this.name = name;
					return this;
				}
				
				@Override
				public void run() {
					EventFactionsWarpsUse event = new EventFactionsWarpsUse(this.teleporting.getFaction(), this.teleporting, this.name, password, this.to);
					event.call();
					if (event.isCancelled()) return;
					
					doTeleport(this.teleporting, event.getTargetLocation(), this.name);
					FactionsWarps.get().warmupMap.remove(this.teleporting.getId());
				}
				
			}
			.set(this.getFPlayer(), warpData.getLocation(warp), warp)
			.runTaskLater(FactionsWarps.get(), 20 * Config.get().secondsWarmup);
			
			FactionsWarps.get().warmupMap.put(this.getFPlayer().getId(), task);
		} else {
			EventFactionsWarpsUse event = new EventFactionsWarpsUse(this.getFPlayer().getFaction(), this.getFPlayer(), warp, password, warpData.getLocation(warp));
			event.call();
			if (event.isCancelled()) return;
			
			
			this.doTeleport(this.getFPlayer(), event.getTargetLocation(), warp);
		}
	}
	
	private final void doTeleport(FPlayer fplayer, Location location, String warpName) {
		fplayer.asBukkitPlayer().teleport(location);
		msg("<green>Taking you to {name} ...", "name", warpName);
	}
	
}
