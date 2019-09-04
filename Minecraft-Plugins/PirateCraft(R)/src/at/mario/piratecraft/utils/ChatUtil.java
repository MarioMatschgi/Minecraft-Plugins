package at.mario.piratecraft.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.mario.piratecraft.Main;

public class ChatUtil {
	
	public static void sendToArenaOnly(String arenaname, String message) {
		List<Player> players = Main.ArenaPlayer.get(arenaname);
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			player.sendMessage(message);
		}
	}
	public static void sendToNormalOnly(String message) {
		if (Main.getInstance().getConfig().getBoolean("Config.chatSystem.enabled")) {
			if (Main.ArenaPlayer.values().isEmpty()) {
				for (Player player : Bukkit.getOnlinePlayers()) {
						player.sendMessage(message);
				}
			} else {
				for (List<Player> arenaPlayers : Main.ArenaPlayer.values()) {
					for (Player player : Bukkit.getOnlinePlayers()) {
						if (!arenaPlayers.contains(player)) {
							player.sendMessage(message);
						}
					}
				}
			}
		}
	}
	public static void sendToSpectatorsOnly(String arenaname, String message) {
		if (Main.getInstance().getConfig().getBoolean("Config.chatSystem.enabled")) {
			List<Player> players = Main.SpectateArenaPlayer.get(arenaname);
			
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				player.sendMessage(message);
			}
		}
	}
}
