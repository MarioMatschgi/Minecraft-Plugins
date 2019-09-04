package at.mario.lobby.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class VanishCMD implements CommandExecutor {

	 List<Player> hiddenFrom = new ArrayList<Player>(); //defined as a class variable
	 
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			 Player p = (Player) sender;

				if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.vip") || p.hasPermission("lobby.builder") || p.hasPermission("lobby.vanish") || p.isOp()) {
				if (!hiddenFrom.contains(p)) {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						player.hidePlayer(p);
						hiddenFrom.add(player);
					}
					mm.sendMessage("Messages.vanish.enabled", p);
				 } else {
					 for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						 player.showPlayer(p);
					     hiddenFrom.remove(player);
					 }
					 mm.sendMessage("Messages.vanish.disabled", p);
				 }
			} else {
				mm.sendMessage("Messages.noPermission", p);
			}
		} else {
			sender.sendMessage("You have to be a player!");
		}
		
		return false;
	}
}
