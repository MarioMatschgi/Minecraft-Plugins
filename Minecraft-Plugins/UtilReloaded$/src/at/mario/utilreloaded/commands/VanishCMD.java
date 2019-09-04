package at.mario.utilreloaded.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class VanishCMD implements CommandExecutor {

	 List<Player> hiddenFrom = new ArrayList<Player>(); //defined as a class variable
	 List<Player> vanishedPlayers = new ArrayList<Player>(); //defined as a class variable
	 
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			 Player p = (Player) sender;

			 if (args.length > 0 && args[0].equalsIgnoreCase("list")) {
				 String names = "";
				 
				 if (vanishedPlayers != null && !vanishedPlayers.isEmpty()) {
					 for (int i = 0; i < args.length; i++) {
						 if (i > 0) {
							 names += "§f, ";
						 }
						 names += "§6" + vanishedPlayers.get(i).getName();
					 }
				 }
				 
				 p.sendMessage("§aVanished Players§f: " + names);
				 
				 return true;
			 }
			 
			if (!hiddenFrom.contains(p)) {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					player.hidePlayer(p);
					hiddenFrom.add(player);
				}
				vanishedPlayers.add(p);
				p.sendMessage(mm.getMessages().getString("Messages.command.vanish.enabled").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				Bukkit.broadcastMessage(mm.getMessages().getString("Messages.quitMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()));
			 } else {
				 for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					 player.showPlayer(p);
				     hiddenFrom.remove(player);
				 }
				 vanishedPlayers.remove(p);
				 p.sendMessage(mm.getMessages().getString("Messages.command.vanish.disabled").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				 Bukkit.broadcastMessage(mm.getMessages().getString("Messages.joinMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()));
			 }
		} else {
			sender.sendMessage("You have to be a player!");
		}
		
		return false;
	}
}
