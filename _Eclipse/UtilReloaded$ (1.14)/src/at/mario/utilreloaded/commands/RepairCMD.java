package at.mario.utilreloaded.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class RepairCMD implements CommandExecutor {
	
	int durabilityFactor = 1;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;

				ItemStack itemToRepair = p.getItemInHand();
				int durabilityLeft = itemToRepair.getType().getMaxDurability() - itemToRepair.getDurability();
				int durabilityToRepair = itemToRepair.getDurability();
				int xpCost = durabilityToRepair / 2 * durabilityFactor;
				
				if (args.length == 0) {
					/*
					if (p.getTotalExperience() < xpCost) {
						xpCost = p.getTotalExperience();
						durabilityToRepair = xpCost * 2 / durabilityFactor;
					}
					p.setTotalExperience(p.getTotalExperience() - xpCost);
					*/
					p.getItemInHand().setDurability((short) (itemToRepair.getType().getMaxDurability() - (durabilityLeft + durabilityToRepair)));
					
					p.sendMessage(mm.getMessages().getString("Messages.command.repair.success").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%repaireddurability%", 
							durabilityToRepair+"").replace("%xpcost%", xpCost+""));
				} else if (args.length > 0) {
					
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
