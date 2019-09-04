package at.mario.utilreloaded.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.mario.utilreloaded.Main;
import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class DupeItemCMD implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				ItemStack itemToDupe = p.getItemInHand();
				
				if (itemToDupe != null && !itemToDupe.getType().equals(Material.AIR) && !(itemToDupe.getType() == Material.AIR)) {
					p.getInventory().addItem(itemToDupe);
					p.sendMessage(mm.getMessages().getString("Messages.command.dupeitem.success").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				} else {
					p.sendMessage(mm.getMessages().getString("Messages.command.dupeitem.didntHoldInHand").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				}
			} else if (args.length > 0) {
				if (Main.isInteger(args[0]) && Main.parseInt(args[0]) > 0) {
					ItemStack itemToDupe = p.getItemInHand();
					
					if (itemToDupe != null && !itemToDupe.getType().equals(Material.AIR) && !(itemToDupe.getType() == Material.AIR)) {
						for (int i = 0; i < Main.parseInt(args[0]); i++) {
							int prevCt = itemToDupe.getAmount();
							p.getInventory().addItem(itemToDupe);
							itemToDupe.setAmount(prevCt);
						}
						p.sendMessage(mm.getMessages().getString("Messages.command.dupeitem.success").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					} else {
						p.sendMessage(mm.getMessages().getString("Messages.command.dupeitem.didntHoldInHand").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					}
				} else {
					p.sendMessage(mm.getMessages().getString("Messages.command.dupeitem.didntEnterNumber").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				}
			}
		} else {
			sender.sendMessage("§cYou have to be a player");
		}
		
		return true;
	}

	public static void CMDInfo(Player p) {
		p.sendMessage("§f--------§bXp-§3Exchange§f-------------------------");
		p.sendMessage("§6/xpexchange §f| §eThe main command");
		p.sendMessage("§6Aliasses§f: §eexchangexpf, §eexchange");
		p.sendMessage(" ");
		p.sendMessage("§6/xpexchange {%playerToTransfere%} {%amountToTransfere%} §f| §eTransfere %amountToTransfere% to %playerToTransfere%");
		p.sendMessage("§f-----------------------------------------------");
	}
}
