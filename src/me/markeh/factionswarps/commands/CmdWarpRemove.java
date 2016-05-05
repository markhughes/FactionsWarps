package me.markeh.factionswarps.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqInFaction;
import me.markeh.factionsframework.command.requirements.ReqRankAtLeast;
import me.markeh.factionswarps.Config;

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
			msg("<red>You must be at least " + Config.get().minimumManage.getDescPlayerMany() + " to manage warps!");
			return;
		}
	}

}
