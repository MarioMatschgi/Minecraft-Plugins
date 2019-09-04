package at.mario.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.inventories.VisibilityInventory;

public class PlayerVisibilityCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			 Player p = (Player) sender;
			 
			 VisibilityInventory.getInstance().newInventory(p);
		}
		
		return false;
	}
	
}
