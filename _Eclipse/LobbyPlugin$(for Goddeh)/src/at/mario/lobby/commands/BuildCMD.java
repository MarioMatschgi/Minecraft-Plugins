package at.mario.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class BuildCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.builder") || p.isOp()) {
				if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".build") == true) {
					dm.getData().set("Data." + p.getName().toLowerCase() + ".build", false);
					mm.sendMessage("Messages.build.disabled", p);
				} else {
					dm.getData().set("Data." + p.getName().toLowerCase() + ".build", true);
					mm.sendMessage("Messages.build.enabled", p);
				}
				dm.saveData();
			} else {
				mm.sendMessage("Messages.noPermission", p);
			}
		} else {
			sender.sendMessage(ChatColor.RED+"You have to be a player!");
		}
		
		return false;
	}
}
