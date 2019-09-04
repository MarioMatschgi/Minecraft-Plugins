package at.mario.lobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class MsgCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		Player toP = null;
		
		if (args.length == 0) {
			sender.sendMessage(mm.getMessages().getString("Messages.msg.enterName").replace("%prefix%", mm.getMessages().getString("Messages.prefix").replace("%player%", sender.getName()).replace("%msgprefix%", mm.getMessages().getString("Messages.msg.prefix")).replace("%player%", sender.getName())));
		} else {
			for (Player p2 : Bukkit.getOnlinePlayers()) {
				if (p2.getName().equals(args[0])) {
					toP = p2;
					continue;
				}
			}
			if (toP == null) {
				sender.sendMessage(mm.getMessages().getString("Messages.playerNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%msgprefix%", mm.getMessages().getString("Messages.msg.prefix")).replace("%player%", args[0]));
				return true;
			}
			
			if (args.length == 1) {
				sender.sendMessage(mm.getMessages().getString("Messages.msg.enterMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", sender.getName()));
			} else if (args.length > 1) {	
				String message = "";
				for (int i = 1; i < (args.length); i++) {
					message = message + " " + args[i];
				}
				sender.sendMessage(mm.getMessages().getString("Messages.msg.messageSendFormat").replace("%msgprefix%", mm.getMessages().getString("Messages.msg.prefix")).replace("%player%", sender.getName()).replace("%message%", message).replace("%playerto%", toP.getName()));
				toP.sendMessage(mm.getMessages().getString("Messages.msg.messageFormat").replace("%msgprefix%", mm.getMessages().getString("Messages.msg.prefix")).replace("%player%", sender.getName()).replace("%message%", message).replace("%message%", message).replace("%playerto%", toP.getName()));
				// toP.sendMessage("[Message] TO: " + toP.getName() + " Message: " + message);
			}
		}
		return true;
	}
}
