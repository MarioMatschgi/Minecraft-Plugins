package at.mario.testplugin.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.testplugin.Main;

public class HealCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				p.setHealth(p.getMaxHealth());
				p.setFoodLevel(20);
				p.setSaturation(20);
				
				p.sendMessage(Main.PREFIX + "§aYou have been healed§f!");
				
				return true;
			} else {
				healPlayers(sender, args);
			}
		} else {
			if (args.length > 0) {
				healPlayers(sender, args);
			} else {
				sender.sendMessage(Main.PREFIX + "§cYou have to be a player§f!");
			}
		}
		
		return true;
	}
	
	public void healPlayers(CommandSender sender, String[] args) {
		List<Player> healedPlayers = new ArrayList<Player>();
		
		for (int i = 0; i < args.length; i++) {
			String spielerName = args[i];
			Player player = Bukkit.getPlayer(spielerName);

			if (player != null && player.isOnline()) {
				player.setHealth(player.getMaxHealth());
				player.setFoodLevel(20);
				player.setSaturation(20);
				
				healedPlayers.add(player);
				player.sendMessage(Main.PREFIX + "§aYou have been healed§f!");
			}
		}
		
		String players = "";
		for (int i = 0; i < healedPlayers.size(); i++) {
			Player player = healedPlayers.get(i);
			
			if (i != 0) {
				players = players + "§f, ";
			}
			players = players + "§6" + player.getName();
		}
		
		sender.sendMessage(Main.PREFIX + "§aThe following players have been healed§f: " + players);
	}
}
