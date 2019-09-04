package at.mario.utilreloaded.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.mario.utilreloaded.Main;
import at.mario.utilreloaded.inventories.EnderchestInventory;
import at.mario.utilreloaded.manager.ConfigManagers.DataManager;
import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class EnderchestCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;

			p.openInventory(getEnderchestInventory(p));
		} else {
			sender.sendMessage("§cYou have to be a player");
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	public Inventory getEnderchestInventory(Player p) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		Inventory ecInv = Main.getPlugin().getServer().createInventory(null, 12 * 9, mm.getMessages().getString("Messages.ecTitle"));
		
		if (dm.getData().get("Data." + p.getName() + ".privateenderchest") == null) {
			EnderchestInventory.getInstance().newInventory(p);
		}

		ArrayList<ItemStack> data = (ArrayList<ItemStack>) dm.getData().get("Data." + p.getName() + ".privateenderchest");
		ItemStack[] content = new ItemStack[data.size()];
		
		for (int i = 0; i < data.size(); i++) {
			content[i] = data.get(i);
		}
		
		
		ecInv.setContents(content);
		
		return ecInv;
	}
}
