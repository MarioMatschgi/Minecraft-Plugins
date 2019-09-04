package at.mario.utilreloaded.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class PingCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				int ping = ((CraftPlayer) p).getHandle().playerConnection.player.ping;
				p.sendMessage(mm.getMessages().getString("Messages.command.ping.self").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%ping%", ping+""));
			} else if (args.length > 0) {
				Player player = Bukkit.getPlayer(args[0]);

				int ping = ((CraftPlayer) player).getHandle().playerConnection.player.ping;
				if (player != null && player.isOnline()) {
					p.sendMessage(mm.getMessages().getString("Messages.command.ping.other").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%ping%", ping+"").
							replace("%player%", player.getName()));
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
