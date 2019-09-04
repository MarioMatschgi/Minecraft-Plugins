package at.mario.lobby.commands;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class PingCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				try {
					  Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
					  int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
					  
					  p.sendMessage(mm.getMessages().getString("Messages.ping.you").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%ping%", ping+""));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
					  p.sendMessage("§c[LobbyReloaded] Could not get ping of player: " + p.getName());
					}
			}
		} else {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				Player pArgs = null; // wird nachher geändert

				for (Player p2 : Bukkit.getOnlinePlayers()) {
					if (p2.getName().equals(args[0])) {
						pArgs = p2;
						continue;
					}
				}
				if (pArgs == null) {
					sender.sendMessage(mm.getMessages().getString("Messages.playerNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%msgprefix%", mm.getMessages().getString("Messages.msg.prefix")).replace("%player%", args[0]));
					return true;
				}
				
				try {
					Object entityPlayer = pArgs.getClass().getMethod("getHandle").invoke(pArgs);
					int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
				  
					p.sendMessage(mm.getMessages().getString("Messages.ping.other").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%ping%", ping+"").replace("%player%", pArgs.getName()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
					p.sendMessage("§c[LobbyReloaded] Could not get ping of player: " + p.getName());
				}
			}
		}
		
		return true;
	}
}
