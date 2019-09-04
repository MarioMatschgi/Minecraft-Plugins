package at.mario.lobby.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class SilenthubCMD implements CommandExecutor {
	
	public static HashMap<Player, Player> hiddenPlayers = new HashMap<Player, Player>();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			MessagesManager mm = new MessagesManager();
			Player p = (Player) sender;
			
			if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.vip") || p.hasPermission("lobby.builder") || p.hasPermission("lobby.silenthub") || p.isOp()) {
				if (hiddenPlayers.containsKey(p)) {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					     p.showPlayer(player);
					     hiddenPlayers.remove(p);
					}
					mm.sendMessage("Messages.silenthub.disabled", p);
				} else {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						p.hidePlayer(player);
						hiddenPlayers.put(p, player);
					}
					mm.sendMessage("Messages.silenthub.enabled", p);
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED+"You have to be a player!");
		}
		
		return false;
	}
}
