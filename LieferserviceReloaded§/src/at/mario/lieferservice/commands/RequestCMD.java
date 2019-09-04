package at.mario.lieferservice.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lieferservice.inventories.RequestsInventory;

public class RequestCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			RequestsInventory.getInstance().newInventory(p);
		} else {
			sender.sendMessage("§cYou have to be a player");
		}
		
		return true;
	}

	public static void CMDInfo(Player p) {
		p.sendMessage("§f--------§bLieferservice§f-------------------------");
		p.sendMessage("§6/order §f| §eThe main command");
		p.sendMessage(" ");
		p.sendMessage("§6/order %itemtyp% %itemamount% §f| §eOrders %itemamount% of %itemtyp%");
		p.sendMessage("§6/order %itemtyp% %itemamount% stacks §f| §eOrders %itemamount% STACKS of %itemtyp%");
		p.sendMessage("§f-----------------------------------------------");
	}
}
