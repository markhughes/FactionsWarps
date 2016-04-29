package me.markeh.factionswarps.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqInFaction;
import me.markeh.factionsframework.command.requirements.ReqRankAtLeast;
import me.markeh.factionswarps.Config;
import me.markeh.factionswarps.store.WarpData;

public class CmdWarpList extends FactionsCommand {

	// -------------------------------------------------- //
	// COMMAND CONSTRUCT  
	// -------------------------------------------------- //
	
	private static CmdWarpList instance = new CmdWarpList();
	public static CmdWarpList get() { return instance; }
	
	public CmdWarpList() {
		this.addAlias("listwarps", "warps");
		
		this.setDescription("show a list of available warps");
		
		this.addRequirement(ReqInFaction.get(this));
		this.addRequirement(ReqRankAtLeast.get(this, Config.get().minimumList));
	}
	
	// -------------------------------------------------- //
	// METHODS  
	// -------------------------------------------------- //
	
	public void run() throws Exception {
		if ( ! this.getFPlayer().getRole().isAtLeast(Config.get().minimumList)) {
			msg("<red>You must be at least {required-rank} to list warps.",
					"required-rank", Config.get().minimumList.getDescPlayerOne());
			
			return;
		}
		
		WarpData warpData = WarpData.get(this.getFPlayer().getFaction());
		
		// \uD83D\uDD12
		
		msg(ChatColor.GOLD + "__________.[ " + ChatColor.DARK_GREEN + "Warps" + ChatColor.GOLD + " ].__________");
		
		for (String warp : warpData.warpLocations.keySet()) {
			String act = "run_command";
			String add = "";
			if (warpData.warpPasswords.containsKey(warp)) {
				act = "suggest_command";
				add = ChatColor.GRAY + " (has password)";
			}
			
			String command = "tellraw " + this.getFPlayer().asBukkitPlayer().getName() +" [\"» \",{\"text\":\""+ warp + add + "\",\"bold\":false,\"underlined\":false,\"clickEvent\":{\"action\":\"" + act + "\",\"value\":\"/f warp use "+ warp + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click to use ...\"}]}}}]";
			
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);

		}
		
		msg("");
	}

}
