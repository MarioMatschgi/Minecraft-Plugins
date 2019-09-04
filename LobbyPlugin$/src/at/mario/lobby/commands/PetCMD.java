package at.mario.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.Main;
import at.mario.lobby.inventories.PetInventory;

public class PetCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (Main.getInstance().getConfig().getBoolean("Config.pet.enabled") == true) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				
				PetInventory.getInstance().newInventory(p);
			} else {
				sender.sendMessage(ChatColor.RED+"You have to be a player!");
			}
		} else {
			sender.sendMessage("§fUnknown command. Type \"/help\" for help.");
		}
		
		return false;
	}

}
