package at.mario.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class FlyCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.builder") || p.hasPermission("lobby.fly") || p.hasPermission("lobby.vip") || p.isOp()) {
				if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".fly")) {
					if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".fly") == true) {
						p.setAllowFlight(false);
						dm.getData().set("Data." + p.getName().toLowerCase() + ".fly", false);
						dm.saveData();
						
						mm.sendMessage("Messages.fly.disabled", p);
					} else {
						p.setAllowFlight(true);
						dm.getData().set("Data." + p.getName().toLowerCase() + ".fly", true);
						dm.saveData();
						
						mm.sendMessage("Messages.fly.enabled", p);
					}
				} else {
					p.setAllowFlight(true);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".fly", true);
					dm.saveData();
					
					mm.sendMessage("Messages.fly.enabled", p);
				}
			} else {
				mm.sendMessage("Messages.noPermission", p);
			}
		} else {
			sender.sendMessage(ChatColor.RED+"You have to be a player!");
		}
		
		return false;
	}

}
