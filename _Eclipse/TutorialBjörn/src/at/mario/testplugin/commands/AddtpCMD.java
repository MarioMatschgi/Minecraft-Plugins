package at.mario.testplugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.testplugin.Main;
import at.mario.testplugin.manager.DataManager;

public class AddtpCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				p.sendMessage(Main.PREFIX + "§cPlease enter a name after §f\"/§6addtp§f\"§f!");
			} else if (args.length == 1) {
				Location loc = p.getLocation();
				
				Boolean contains = DataManager.getData().contains("Data.teleports." + args[0]);

				DataManager.getData().set("Data.teleports." + args[0] + ".world", loc.getWorld().getName());
				DataManager.getData().set("Data.teleports." + args[0] + ".x", loc.getX());
				DataManager.getData().set("Data.teleports." + args[0] + ".y", loc.getY());
				DataManager.getData().set("Data.teleports." + args[0] + ".z", loc.getZ());
				DataManager.getData().set("Data.teleports." + args[0] + ".yaw", loc.getYaw());
				DataManager.getData().set("Data.teleports." + args[0] + ".pitch", loc.getPitch());
				
				DataManager.saveData();
				
				if (contains == true) {
					p.sendMessage(Main.PREFIX + "§aThe teleport §f\"" + args[0] + "§f\" §ahas been §bchanged§f!");
				} else {
					p.sendMessage(Main.PREFIX + "§aThe teleport §f\"" + args[0] + "§f\" §ahas been §6set§f!");
				}
			} else {
				p.sendMessage(Main.PREFIX + "§cUsage: /addtp {tpName}");
			}
		} else {
			sender.sendMessage(Main.PREFIX + "§cYou have to be a player§f!");
		}
		
		return true;
	}
	
}
/*

Data:
  teleports:
    args[0]:
      x:
      y:
      z:
      yaw:
      pitch:

*/
