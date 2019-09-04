package at.mario.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.inventories.ArmorInventory;

public class ArmorCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			ArmorInventory.getInstance().newInventory(p);
		} else {
			sender.sendMessage(ChatColor.RED+"You have to be a player!");
		}
		
		return false;
	}

}
