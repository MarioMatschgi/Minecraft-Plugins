package at.mario.lobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class GamemodeCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (Main.getInstance().getConfig().getBoolean("Config.gm.enabled") == true) {
			if (sender instanceof Player) {
				MessagesManager mm = new MessagesManager();
				Player p = (Player) sender;

				if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.builder") || p.hasPermission("lobby.gamemode") || p.isOp()) {
					if (args.length == 0) {
						mm.sendMessage("Messages.gamemode.enterGamemode", p);
					} else {
						if (!Main.isInteger(args[0])) {
							if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("Survivalmode")) {
								p.setGameMode(GameMode.SURVIVAL);
								Main.giveLobbyItems(p);
								p.sendMessage(mm.getMessages().getString("Messages.gamemode.set").replace("%gamemode%", "survivalmode").replace("&", "§").replace("%prefix%", mm.getMessages().getString("Messages.prefix")) );
							} else if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("Creativemode")) {
								p.setGameMode(GameMode.CREATIVE);
								Main.removeLobbyItems(p);
								p.sendMessage(mm.getMessages().getString("Messages.gamemode.set").replace("%gamemode%", "creativemode").replace("&", "§").replace("%prefix%", mm.getMessages().getString("Messages.prefix")) );
							} else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("Spectatormode")) {
								p.setGameMode(GameMode.SPECTATOR);
								Main.giveLobbyItems(p);
								p.sendMessage(mm.getMessages().getString("Messages.gamemode.set").replace("%gamemode%", "spectatormode").replace("&", "§").replace("%prefix%", mm.getMessages().getString("Messages.prefix")) );
							} else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("Adventuremode")) {
								p.setGameMode(GameMode.ADVENTURE);
								Main.giveLobbyItems(p);
								p.sendMessage(mm.getMessages().getString("Messages.gamemode.set").replace("%gamemode%", "adventuremode").replace("&", "§").replace("%prefix%", mm.getMessages().getString("Messages.prefix")) );
							}
						} else {
							int gm = Main.parseInt(args[0]);
							if (gm == 0) {
								p.setGameMode(GameMode.SURVIVAL);
								Main.giveLobbyItems(p);
								p.sendMessage(mm.getMessages().getString("Messages.gamemode.set").replace("%gamemode%", "Survivalmode").replace("&", "§").replace("%prefix%", mm.getMessages().getString("Messages.prefix")) );
							} else if (gm == 1) {
								p.setGameMode(GameMode.CREATIVE);
								Main.removeLobbyItems(p);
								p.sendMessage(mm.getMessages().getString("Messages.gamemode.set").replace("%gamemode%", "Creativemode").replace("&", "§").replace("%prefix%", mm.getMessages().getString("Messages.prefix")) );
							} else if (gm == 2) {
								p.setGameMode(GameMode.ADVENTURE);
								Main.giveLobbyItems(p);
								p.sendMessage(mm.getMessages().getString("Messages.gamemode.set").replace("%gamemode%", "Adventuremode").replace("&", "§").replace("%prefix%", mm.getMessages().getString("Messages.prefix")) );
							} else if (gm == 3) {
								p.setGameMode(GameMode.SPECTATOR);
								Main.giveLobbyItems(p);
								p.sendMessage(mm.getMessages().getString("Messages.gamemode.set").replace("%gamemode%", "Spectatormode").replace("&", "§").replace("%prefix%", mm.getMessages().getString("Messages.prefix")) );
							}
						}
					}
				} else {
					mm.sendMessage("Messages.noPermission", p);
				}
			} else {
				sender.sendMessage(ChatColor.RED+"You have to be a player!");
			}
		} else {
			sender.sendMessage("§fUnknown command. Type \"/help\" for help.");
		}
		
		return false;
	}

}
