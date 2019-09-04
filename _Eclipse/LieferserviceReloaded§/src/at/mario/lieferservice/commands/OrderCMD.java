package at.mario.lieferservice.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lieferservice.Main;
import at.mario.lieferservice.Request;
import at.mario.lieferservice.RequestStatus;
import at.mario.lieferservice.manager.ConfigManagers.DataManager;
import at.mario.lieferservice.manager.ConfigManagers.MessagesManager;

public class OrderCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length >= 1) {
				Material material = Material.getMaterial(args[0]);
				if (material == null) {
					material = Material.matchMaterial(args[0]);
				}
				if (material == null) {
					try {
						material = Material.valueOf(args[0]);
					} catch (Exception e) {	}
				}
				if (material == null) {
					p.sendMessage(mm.getMessages().getString("Messages.itemNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%item%", args[0]));
					return true;
				}
				if (args.length >= 2) {
					int amount = -1;
					boolean stacks = false;
					if (Main.isInteger(args[1])) {
						amount = Main.parseInt(args[1]);
						if (amount <= 0) {
							p.sendMessage(mm.getMessages().getString("Messages.enterNumber").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							return true;
						}
					} else {
						p.sendMessage(mm.getMessages().getString("Messages.enterNumber").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
						return true;
					}
					if (args.length >= 3) {
						if (args[2].equalsIgnoreCase("stacks")) {
							amount *= material.getMaxStackSize();
							stacks = true;
						} else if (args[2].equalsIgnoreCase("items")) {
							
						} else {
							p.sendMessage(mm.getMessages().getString("Messages.command.order.wrongMultiplier").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							return true;
						}
					}
					
					
					ItemStack requestedItem = new ItemStack(material, amount);
					ItemMeta meta = requestedItem.getItemMeta();
					String name = material.name();
					name = name.replace("_", " ");
					name = name.replace("item", "");
					name = name.toLowerCase();
					meta.setDisplayName(name);
					requestedItem.setItemMeta(meta);
					
					Request request = new Request(Main.requests.size() + 1, p, requestedItem, RequestStatus.NEW_REQUEST, p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
					Main.requests.add(request);
					dm.getData().set("Data.requests", Main.requests);
					dm.saveData();
					
					
					String stacksStr = "";
					if (stacks) {
						stacksStr = " stacks";
						amount /= material.getMaxStackSize();
					}
					
					p.sendMessage(mm.getMessages().getString("Messages.command.order.success").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%amount%", amount+"").replace("%stacks%", stacksStr).replace("%item%", name));

					if (Bukkit.getPlayer("Mario_Matschgi") != null && Bukkit.getPlayer("Mario_Matschgi").isOnline()) {
						Bukkit.getPlayer("Mario_Matschgi").sendMessage(mm.getMessages().getString("Messages.newRequest.newWhilePlaying").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
								replace("§nl", "\n").replace("%player%", p.getName()));
					}
				} else {
					p.sendMessage(mm.getMessages().getString("Messages.command.order.enterAmount").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					return true;
				}
			} else {
				CMDInfo(p);
			}
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
