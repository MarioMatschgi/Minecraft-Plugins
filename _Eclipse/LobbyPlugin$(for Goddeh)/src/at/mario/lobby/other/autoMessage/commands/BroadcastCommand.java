package at.mario.lobby.other.autoMessage.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;
import at.mario.lobby.other.autoMessage.broadcasts.Broadcast;
import at.mario.lobby.other.autoMessage.broadcasts.BroadcastStatus;
import at.mario.lobby.other.autoMessage.broadcasts.ChatBroadcast;
import at.mario.lobby.other.autoMessage.util.IgnoreManager;
import at.mario.lobby.other.autoMessage.util.MessageManager;

public class BroadcastCommand implements CommandExecutor {
	
	private Broadcast broadcast = new Broadcast();
	private ChatBroadcast chatBroadcast = new ChatBroadcast();
	private IgnoreManager ignoreManager = new IgnoreManager();
	private MessagesManager mm = new MessagesManager();
	
	private String noAccessToCommand = mm.getMessages().getString("Messages.noPermission");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (Main.getInstance().getConfig().getBoolean("chat.enabled") == true) {
			if (args.length == 0) {
				if (!sender.hasPermission("lobby.broadcast.info")) {
					sender.sendMessage(noAccessToCommand);
					return true;
				}
				CMDInfo();
			/* Chat broadcast commands. */
			} else if (args.length > 1 && args[0].equalsIgnoreCase("chat")) {
				/* Start - command */
				if (args[1].equalsIgnoreCase("start")) {
					/* Checks if command sender has the required permission to start the chat broadcast. */
					if (!sender.hasPermission("lobby.broadcast.chat.start")) {
						sender.sendMessage(noAccessToCommand);
						return true;
					}
					/* Checks chat broadcast status and performs action. */
					if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.STOPPED) {
						broadcast.broadcast();
						sender.sendMessage("§2[SimpleBroadcast] §aSuccessfully started chat broadcast.");
					} else if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.DISABLED) {
						sender.sendMessage("&f[§6Broadcast§f] §cChat broadcast is disabled (as set in \"config.yml\").");
					} else {
						sender.sendMessage("&f[§6Broadcast§f] §cChat broadcast is already started.");
					}
				/* Stop - command */
				} else if (args[1].equalsIgnoreCase("stop")) {
					/* Checks if command sender has the required permission to stop the chat broadcast. */
					if (!sender.hasPermission("lobby.broadcast.chat.stop")) {
						sender.sendMessage(noAccessToCommand);
						return true;
					}
					/* Checks chat broadcast status and performs action. */
					if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.RUNNING) {
						broadcast.cancelChatBroadcast();
						sender.sendMessage("§2[SimpleBroadcast] §aSuccessfully stopped chat broadcast.");
					} else if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.DISABLED) {
						sender.sendMessage("&f[§6Broadcast§f] §cChat broadcast is disabled (as set in \"config.yml\").");
					} else {
						sender.sendMessage("&f[§6Broadcast§f] §cChat broadcast is already stopped.");
					}
				/* List - command */
				} else if (args[1].equalsIgnoreCase("list")) {
					/* Checks if command sender has the required permission to list the chat broadcast messages. */
					if (!sender.hasPermission("lobby.broadcast.chat.list")) {
						sender.sendMessage(noAccessToCommand);
						return true;
					}
					/* Loads all required parts of broadcast message. */
					String prefix = MessageManager.getChatPrefix();
					String suffix = MessageManager.getChatSuffix();
					/* Shows all chat broadcast messages to command sender. */
					sender.sendMessage("§e--------- §fChat broadcast messages: §e---------");
					/* Checks if any messages are configured. */
					if (MessageManager.getChatMessages().size() < 1) {
						sender.sendMessage("§cNo messages configured.");
						return true;
					}
					for (int messageID = 0; messageID < MessageManager.getChatMessages().size(); messageID++) {
						String message = MessageManager.getChatMessages().get(messageID).toString();
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "§6" + (messageID+1) + ".§f " + prefix + message + suffix));
					}
				/* Now - command */
				} else if (args[1].equalsIgnoreCase("now")) {
					/* Checks if command sender has the required permission to broadcast existing messages. */
					if (!sender.hasPermission("lobby.broadcast.chat.now")) {
						sender.sendMessage(noAccessToCommand);
						return true;
					}
					/* Checks if length of arguments is correct. */
					if (args.length < 3) {
						sender.sendMessage("&f[§6Broadcast§f] §cPleaser enter a message number.");
						return true;
					}
					/* Checks if length of arguments is correct. */
					if (args.length > 3) {
						sender.sendMessage("&f[§6Broadcast§f] §cPleaser enter only one message number.");
						return true;
					}
					try {
						int messageID = Integer.parseInt(args[2]) - 1;
						HashMap<Integer, String> chatMessages = MessageManager.getChatMessages();
						if (messageID > -1 && messageID < chatMessages.size()) {
							chatBroadcast.broadcastExistingMessage(messageID);
							sender.sendMessage("§2[SimpleBroadcast] §aSuccessfully broadcasted message #" + args[2] + ".");
						} else {
							sender.sendMessage("&f[§6Broadcast§f] §cPlease choose a number between 1 and " + chatMessages.size() + ".");
						}
					} catch(NumberFormatException nfe) {
						sender.sendMessage("&f[§6Broadcast§f] §cPlease enter a valid number.");
						return true;
					}
				/* Next - command */
				} else if (args[1].equalsIgnoreCase("next")) {
					/* Checks if command sender has the required permission to skip broadcast messages. */
					if (!sender.hasPermission("lobby.broadcast.chat.next")) {
						sender.sendMessage(noAccessToCommand);
						return true;
					}
					/* Checks if "randomizeMessages" is activated. */
					if (Main.getInstance().getConfig().getBoolean("chat.randomizeMessages")) {
						sender.sendMessage("&f[§6Broadcast§f] §cSkipping messages only is available when \"randomizeMessages\" is set to \"false\" in config.");
						return true;
					}
					/* Gets current message counter. */
					int messageCounter = ChatBroadcast.getMessageCounter();
					/* Skips message based on message counter. */
					if (messageCounter < MessageManager.getChatMessages().size()) {
						ChatBroadcast.setMessageCounter(messageCounter + 1);
						sender.sendMessage("§2[SimpleBroadcast] §aSuccessfully skipped message #" + (messageCounter + 1) + ".");
					} else {
						ChatBroadcast.setMessageCounter(1);
						sender.sendMessage("§2[SimpleBroadcast] §aSuccessfully skipped message #1.");
					}
				/* Ignore - command */
				} else if (args[1].equalsIgnoreCase("ignore")) {
					/* Checks if command sender has the required permission to add/remove players to ignore list. */
					if (!sender.hasPermission("lobby.broadcast.chat.ignore")) {
						sender.sendMessage(noAccessToCommand);
						return true;
					}
					/* Checks if length of arguments is correct. */
					if (args.length < 3) {
						sender.sendMessage("&f[§6Broadcast§f] §cPleaser enter a player name.");
						return true;
					}
					/* Checks if length of arguments is correct. */
					if (args.length > 3) {
						sender.sendMessage("&f[§6Broadcast§f] §cPleaser enter only one player name.");
						return true;
					}
					/* Gets player specified in command. */
					Player player = Bukkit.getServer().getPlayerExact(args[2]);
					/* Checks if player is online. */
					if (player == null) {
						// TODO Rewrite message
						sender.sendMessage("&f[§6Broadcast§f] §cYou can only add players who are currently online.");
						return true;
					}
					/* Checks if player already is listed in ignore list and performs action. */
					if (IgnoreManager.getChatIgnoreList().contains(player.getUniqueId().toString())) {
						IgnoreManager.getChatIgnoreList().remove(player.getUniqueId().toString());
						sender.sendMessage("§2[SimpleBroadcast] §aSuccessfully removed §7" + args[2] + "§2 from ignore list.");
					} else {
						IgnoreManager.getChatIgnoreList().add(player.getUniqueId().toString());
						sender.sendMessage("§2[SimpleBroadcast] §aSuccessfully added §7" + args[2] + "§2 to ignore list.");
					}
					ignoreManager.updateChatIgnoreList();
				}
			/* Boss bar broadcast commands. */
			} else if (args.length > 1 && args[0].equalsIgnoreCase("chat")) {
				
			} else if (args.length > 1 && args[0].equalsIgnoreCase("bossbar")) {
				
			}
		} else {
			sender.sendMessage("§fUnknown command. Type \"/help\" for help.");
		}
		
		return false;
	}

	private void CMDInfo() {
		// TODO Auto-generated method stub
		
	}
}