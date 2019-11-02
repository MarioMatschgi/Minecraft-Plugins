package at.mario.resourcebattlereloaded.commands;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.resourcebattlereloaded.Main;
import at.mario.resourcebattlereloaded.countdowns.MainCountdown;

public class ResourceBattleCMD implements CommandExecutor {
	 
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (args.length == 0) {
				sendCMDInfo(p);
			}
			
			else if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("list")) {
					p.sendMessage("--------§2Resource Battle§f--(/§aItem List§f)--------");
					for (Entry<Material, Integer> entry : Main.GetItemHashMap().entrySet()) {
						Material key = entry.getKey();
						Integer value = entry.getValue();
						
						if (key == null || value == null)
							continue;
						
						p.sendMessage("Item: " + key + " -> Points: " + value);
					}
					p.sendMessage("§f-------- -------- -------- --------");
				}
				else if (args[0].equalsIgnoreCase("start")) {
					if (args.length >= 2) {
						if (!MainCountdown.isRunning) {
							MainCountdown.start(Main.parseInt(args[1]));
						}
					}
				}
				else if (args[0].equalsIgnoreCase("setup")) {
					if (args[1].equalsIgnoreCase("removeitem")) {
						Main.RemoveItem(p.getItemInHand());
					}
					else if (args.length >= 3) {
						if (args[1].equalsIgnoreCase("additem")) {
							Main.AddItem(p.getItemInHand(), Main.parseInt(args[2]));
						}
						else {
							sendSetupInfo(p);
						}
					}
					else {
						sendSetupInfo(p);
					}
				}
				else {
					sendCMDInfo(p);
				}
			}
			
			else {
				sendCMDInfo(p);
			}
		} else {
			sender.sendMessage("You have to be a player!");
		}
		
		return false;
	}
	
	public void sendCMDInfo(Player p) {
		p.sendMessage("--------§2Resource Battle§f--------");
		p.sendMessage("§6/resourceBattle setup §f| §eThe main subcommand");
		p.sendMessage(" ");
		p.sendMessage("§6/resourceBattle setup help §f| §eShows you this info");
		p.sendMessage("§6/resourceBattle setup addItem {points} §f| §eAdds the item hold in the Main-hand with a point-score of {points}");
		p.sendMessage("§6/resourceBattle setup removeItem §f| §eRemoves the item hold in the Main-hand");
		p.sendMessage("§f-------- -------- -------- --------");
	}
	
	public void sendSetupInfo(Player p) {
		p.sendMessage("--------§2Resource Battle§f--(/§aresourceBattle arena§f)--------");
		p.sendMessage("§6/resourceBattle §f| §eThe main command");
		p.sendMessage("§6Aliasses§f: §erb§f, §eresourceBattleReloaded");
		p.sendMessage(" ");
		p.sendMessage("§6/resourceBattle help §f| §eShows you this info");
		p.sendMessage("§6/resourceBattle setup help §f| §eShows you the info of the \"/setup\" subcommand");
		p.sendMessage("§f-------- -------- -------- --------");
	}
}



