package at.mario.pets.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.pets.Inventorys.MainInventory;

public class Pet implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				MainInventory.getInstance().newInventory(p);
			} else {
				cmdInfo(p);
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You need to be a player!");
		}
		
		return true;
	}
	
	private void cmdInfo(Player p) {
		p.sendMessage("-----§ePetsReloaded§f-----(page 1/1)----");
		p.sendMessage("§6/pet help §f| §eShows this info");
		p.sendMessage("§6/pet §f| §eOpens the inventory");
		p.sendMessage("§f--------------------------------------");
	}
}
