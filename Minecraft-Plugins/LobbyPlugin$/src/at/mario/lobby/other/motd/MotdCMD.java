package at.mario.lobby.other.motd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.Main;
import at.mario.lobby.commands.LobbyCMD;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class MotdCMD implements CommandExecutor {

	public static String Motd = "";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
	    if ((sender.hasPermission("lobby.admin") || sender.isOp())) {
	    	if (args.length < 1) {
	    		if (sender instanceof Player) {
	    			LobbyCMD.cmdInfo((Player) sender);
	    		} else {
	    			sender.sendMessage("----§3Lobby§bReloaded§f------------------------(page 1/1)----");
	    			sender.sendMessage("§6/lobby §f| §eMain command");
	    			sender.sendMessage("§6Aliases: lb, hub, spawn, l");
	    			sender.sendMessage("  ");
	    			sender.sendMessage("§6/lobby §f| §eTeleports you to the lobby");
	    			sender.sendMessage("§6/lobby help §f| §eShows this info");
	    			sender.sendMessage("  ");
	    			sender.sendMessage("§6/lobby setspawn §f| §eSets the spawn of the lobby");
	    			sender.sendMessage("§6/lobby setloc1 §f| §eSets the first bound of the lobby");
	    			sender.sendMessage("§6/lobby setloc2 §f| §eSets the second bound of the lobby");
	    			sender.sendMessage("§6/lobby wand §f| §eGives you the wand to set the lobby bounds");
	    			sender.sendMessage("  ");
	    			sender.sendMessage("§6/motd §f| §eSets the MOTD of the server");
	    			sender.sendMessage("§6/broadcast help §f| §eFor broadcast help");
	    			sender.sendMessage("  ");
	    			sender.sendMessage("§6/vanish §f| §eMakes you invisible");
	    			sender.sendMessage("§6/build §f| §eYou can build blocks in the lobby");
	    			sender.sendMessage("§6/fly §f| §eYou can fly");
	    			sender.sendMessage("§6/visibility §f| §eOpenes the gui to togggle visibility");
	    			sender.sendMessage("§f------------------------------------------------------");
	    		}
	    	} else if (args.length >= 1) {
	    		if ((args[0].equalsIgnoreCase("set")) && (args.length > 1)) {
	    			Motd.replaceAll("(&([a-r0-9]))", "��$2");
	    			
	    			String s = "";
	    			for (int x = 1; x < args.length; x++) {
	    				s = s + args[x] + " ";
	    				Motd = s;
	    				Main.getInstance().getConfig().set("Config.serverMotd", Motd);
	    				Main.getInstance().saveConfig();
	    			}
	    			sender.sendMessage(mm.getMessages().getString("Messages.prefix") + " §aSuccesfully set the MOTD: §6" + Motd);
	    			
	    			return true;
	    		}
	    		if ((args[0].equalsIgnoreCase("set")) && (args.length == 1)) {
	    			sender.sendMessage("----§3Lobby§bReloaded§f------------------------(page 1/1)----");
	    			sender.sendMessage("§6/lobby §f| §eMain command");
	    			sender.sendMessage("§6Aliases: lb, hub, spawn, l");
	    			sender.sendMessage("  ");
	    			sender.sendMessage("§6/lobby §f| §eTeleports you to the lobby");
	    			sender.sendMessage("§6/lobby help §f| §eShows this info");
	    			sender.sendMessage("  ");
	    			sender.sendMessage("§6/lobby setspawn §f| §eSets the spawn of the lobby");
	    			sender.sendMessage("§6/lobby setloc1 §f| §eSets the first bound of the lobby");
	    			sender.sendMessage("§6/lobby setloc2 §f| §eSets the second bound of the lobby");
	    			sender.sendMessage("§6/lobby wand §f| §eGives you the wand to set the lobby bounds");
	    			sender.sendMessage("  ");
	    			sender.sendMessage("§6/motd §f| §eSets the MOTD of the server");
	    			sender.sendMessage("§6/broadcast help §f| §eFor broadcast help");
	    			sender.sendMessage("  ");
	    			sender.sendMessage("§6/vanish §f| §eMakes you invisible");
	    			sender.sendMessage("§6/build §f| §eYou can build blocks in the lobby");
	    			sender.sendMessage("§6/fly §f| §eYou can fly");
	    			sender.sendMessage("§6/visibility §f| §eOpenes the gui to togggle visibility");
	    			sender.sendMessage("§f------------------------------------------------------");
	    		}
	    	}
	    }
	    return true;
	}
}
