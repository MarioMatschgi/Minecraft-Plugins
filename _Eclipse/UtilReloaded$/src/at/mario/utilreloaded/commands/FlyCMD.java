package at.mario.utilreloaded.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.utilreloaded.Main;
import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class FlyCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				if (!Main.flyData.containsKey(p)) {
					Main.flyData.put(p, false);
					p.setAllowFlight(Main.flyData.get(p));
				}
				
				p.setAllowFlight(!Main.flyData.get(p));
				Main.flyData.put(p, !Main.flyData.get(p));
				
				if (Main.flyData.get(p)) {
					p.sendMessage(mm.getMessages().getString("Messages.command.fly.enabled").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				} else {
					p.sendMessage(mm.getMessages().getString("Messages.command.fly.disabled").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				}
			} else if (args.length > 0) {
				Player player = Bukkit.getPlayer(args[0]);
				
				if (player != null && player.isOnline()) {
					if (!Main.flyData.containsKey(player)) {
						Main.flyData.put(player, true);
						player.setAllowFlight(Main.flyData.get(player));
					} else {
						player.setAllowFlight(!Main.flyData.get(player));
					}
					if (Main.flyData.get(player)) {
						player.sendMessage(mm.getMessages().getString("Messages.command.fly.enabled").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					} else {
						player.sendMessage(mm.getMessages().getString("Messages.command.fly.disabled").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					}
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
