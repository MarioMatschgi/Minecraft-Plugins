package at.mario.testplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.testplugin.Main;
import at.mario.testplugin.inventories.TeleporterInventory;

public class InventoryCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			TeleporterInventory.newInventory(p);
		} else {
			sender.sendMessage(Main.PREFIX + "§cYou have to be a player§f!");
		}
		
		return true;
	}
}
