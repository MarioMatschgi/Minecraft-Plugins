package at.mario.economy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.economy.Main;
import at.mario.economy.Manager.MoneyManager;
import at.mario.economy.Manager.ConfigManagers.MessagesManager;

public class Money implements CommandExecutor {
	
	MessagesManager mm;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player)sender;
			Player p2;
			
			if (args.length == 0) {
				p.sendMessage(mm.replaceIn(p, "Messages.balance.info.you"));
			} else {
				if (args[0].equalsIgnoreCase("add")) {
					if (args.length == 1) {
						cmdInfo(p);
					} else if (args.length == 2) {
						if (Main.isInteger(args[1])) {
							Double amt = (double)(Integer.parseInt(args[1]));
							MoneyManager.getInstance().addBalance(p.getName(), amt);
							p.sendMessage(mm.replaceIn(p, "Messages.balance.info.you"));
						} else {
							p.sendMessage("Please enter a number!");
						}
					} else if (args.length == 3) {
						if (Main.isInteger(args[1])) {
							p2 = Bukkit.getPlayerExact(args[2]);
							
							if (p2 == null) {
								sender.sendMessage(mm.replaceIn(p, "Messages.other.playerNotFound").replace("%player2%", args[2] + "") + "");
							    return true;
							} else {
								Double amt = (double)(Integer.parseInt(args[1]));
								MoneyManager.getInstance().addBalance(p2.getName(), amt);
								p.sendMessage(mm.replaceIn(p, "Messages.balance.info.other"));
							}
						} else {
							p.sendMessage(mm.replaceIn(p, "Messages.balance.enterANumber"));
						}
					} else {
						cmdInfo(p);
					}
				} else {
					cmdInfo(p);
				}
			}
		} else {
			sender.sendMessage("You need to be a player!");
		}
		
		return false;
	}

	private void cmdInfo(Player p) {
		p.sendMessage("--------§eEconomy§f-----(page 1/1)-----");
		p.sendMessage("§6/money help§f| §eShows this info");
		p.sendMessage("§6/money [player]§f| §eShows the balance of [player]");
		p.sendMessage("§6/money add {money} [player]§f| §eAdds {money} to [Player]");
		p.sendMessage("§f--------------------------------------");
	}
}
