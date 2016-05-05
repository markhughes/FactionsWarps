package me.markeh.factionswarps.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqInFaction;
import me.markeh.factionsframework.command.requirements.ReqRankAtLeast;
import me.markeh.factionswarps.Config;
import me.markeh.factionswarps.event.EventFactionsWarpsRemove;
import me.markeh.factionswarps.store.WarpData;

public class CmdWarpRemove extends FactionsCommand {
	
	// -------------------------------------------------- //
	// COMMAND CONSTRUCT  
	// -------------------------------------------------- //

	private static CmdWarpRemove instance = new CmdWarpRemove();
	public static CmdWarpRemove get() { return instance; }
	
	public CmdWarpRemove() {
		this.addAlias("removewarp", "deletewarp", "delwarp", "remwarp");
		
		this.setDescription("remove a warp");
		
		this.setPermission("factions.warp.manage");
		
		this.addRequiredArgument("name");
		
		this.addRequirement(ReqInFaction.get(this));
		this.addRequirement(ReqRankAtLeast.get(this, Config.get().minimumManage));
	}
	
	// -------------------------------------------------- //
	// METHODS  
	// -------------------------------------------------- //
	
	public void run() throws Exception {
		if ( ! this.getFPlayer().getRole().isAtLeast(Config.get().minimumManage)) {
			msg("<red>You must be at least {required-rank} to manage warps!",
					"required-rank", Config.get().minimumManage.getDescPlayerMany());
			return;
		}
		
		String warp = this.getArg(0);
		
		EventFactionsWarpsRemove event = new EventFactionsWarpsRemove(this.getFPlayer().getFaction(), this.getFPlayer(), warp);
		event.call();
		if (event.isCancelled()) return;
		
		WarpData data = WarpData.get(this.getFPlayer().getFaction());
		
		if ( ! data.warpExists(warp)) {
			msg("<red>The warp {name} does not exist!",
					"name", warp);
			return;
		}
		
		data.removeWarp(warp);
		
		msg("<green>The warp {name} has been removed!",
				"name", warp);
	}

}
