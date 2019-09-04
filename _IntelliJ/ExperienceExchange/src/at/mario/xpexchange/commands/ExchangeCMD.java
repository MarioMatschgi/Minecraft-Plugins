package at.mario.xpexchange.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.xpexchange.Main;
import at.mario.xpexchange.manager.ConfigManagers.MessagesManager;

public class ExchangeCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				CMDInfo(p);
				return true;
			} else if (args.length > 0) {
				Player player = null;
				if (args[0] instanceof String && Bukkit.getPlayer(args[0]) != null && Bukkit.getPlayer(args[0]).isOnline()) {
					player = Bukkit.getPlayer(args[0]);
				} else {
					// Nachricht weil falscher spielername
					p.sendMessage(mm.getMessages().getString("Messages.command.playerNotOnline").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", args[0]));
					return true;
				}

				if (args.length > 1) {
					int xp = 0;
					if (Main.isInteger(args[1])) {
						xp = Main.parseInt(args[1]);
					} else {
						// Nachricht weil falscher xp wert
						p.sendMessage(mm.getMessages().getString("Messages.command.noNumber").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", args[0]));
						return true;
					}
					
					if (xp != 0) {
						if (xp < 0 && Main.getInstance().getConfig().getBoolean("Config.onlyPositiveExchanges")) {
							// Nachricht weil Negativer Betrag
							p.sendMessage(mm.getMessages().getString("Messages.command.enterNumberBiggerZero").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", args[0]));
							return true;
						}
						
						if (p.getLevel() - xp < 0) {
							p.sendMessage(mm.getMessages().getString("Messages.command.notEnoughXp").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", args[0]));
							// Nachricht weil nicht genug level
							return true;
						}
						player.setLevel(player.getLevel() + xp);
						p.setLevel(p.getLevel() - xp);

						p.sendMessage(mm.getMessages().getString("Messages.command.successToTransferer").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", player.getName()).
								replace("%xp%", xp+""));
						player.sendMessage(mm.getMessages().getString("Messages.command.successToReceiver").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).
								replace("%xp%", xp+""));
					}
				} else {
					p.sendMessage(mm.getMessages().getString("Messages.command.noNumber").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", args[0]));
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
