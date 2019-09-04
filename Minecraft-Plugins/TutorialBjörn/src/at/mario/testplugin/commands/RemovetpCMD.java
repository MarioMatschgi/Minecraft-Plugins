package at.mario.testplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.testplugin.Main;
import at.mario.testplugin.manager.DataManager;

public class RemovetpCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				p.sendMessage(Main.PREFIX + "§cPlease enter a name after §f\"/§6removetp§f\"§f!");
			} else if (args.length == 1) {
				if (DataManager.getData().contains("Data.teleports." + args[0])) {
					DataManager.getData().set("Data.teleports." + args[0], null);
					
					DataManager.saveData();
					p.sendMessage(Main.PREFIX + "§aThe teleport §f\"" + args[0] + "§f\" §ahas been successfully §bremoved§f!");
				} else {
					p.sendMessage(Main.PREFIX + "§cCould not find teleport §f\"" + args[0] + "§f\"");
				}
			} else {
				p.sendMessage(Main.PREFIX + "§cUsage: /removetp {tpName}");
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
