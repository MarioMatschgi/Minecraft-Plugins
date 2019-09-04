package at.mario.lobby.other.autoMessage.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.lobby.Main;
import at.mario.lobby.commands.LobbyCMD;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;
import at.mario.lobby.other.autoMessage.broadcasts.Broadcast;
import at.mario.lobby.other.autoMessage.broadcasts.BroadcastStatus;
import at.mario.lobby.other.autoMessage.broadcasts.ChatBroadcast;
import at.mario.lobby.other.autoMessage.util.IgnoreManager;
import at.mario.lobby.other.autoMessage.util.MessageManager;

public class BroadcastCMD implements CommandExecutor {
	
	private Broadcast broadcast = new Broadcast();
	private ChatBroadcast chatBroadcast = new ChatBroadcast();
	private IgnoreManager ignoreManager = new IgnoreManager();
	private MessagesManager mm = new MessagesManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				if (!sender.hasPermission("lobby.broadcast.info")) {
					mm.sendMessage("Messages.noPermission", p);
					return true;
				}
				CmdInfo(p);
			} else if (args.length > 1 && args[0].equalsIgnoreCase("chat")) {
				if (args[1].equalsIgnoreCase("start")) {
					if (!sender.hasPermission("lobby.broadcast.chat.start")) {
						mm.sendMessage("Messages.noPermission", p);
						return true;
					}
					/* Checks chat broadcast status and performs action. */
					if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.STOPPED) {
						broadcast.broadcast();
						mm.sendReplacedMessage("%prefix% Successfully started chat broadcast.", p);
					} else if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.DISABLED) {
						mm.sendReplacedMessage("%prefix% Chat broadcast is disabled (as set in \"config.yml\").", p);
					} else {
						mm.sendReplacedMessage("%prefix% Chat broadcast is already started.", p);
					}
				} else if (args[1].equalsIgnoreCase("stop")) {
					if (!sender.hasPermission("lobby.broadcast.chat.stop")) {
						mm.sendMessage("Messages.noPermission", p);
						return true;
					}
					/* Checks chat broadcast status and performs action. */
					if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.RUNNING) {
						broadcast.cancelChatBroadcast();
						mm.sendReplacedMessage("%prefix% Successfully stopped chat broadcast.", p);
					} else if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.DISABLED) {
						mm.sendReplacedMessage("%prefix% §cChat broadcast is disabled (as set in \"config.yml\").", p);
					} else {
						mm.sendReplacedMessage("%prefix% §cChat broadcast is already stopped.", p);
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (!sender.hasPermission("lobby.broadcast.chat.list")) {
						mm.sendMessage("Messages.noPermission", p);
						return true;
					}
					/* Loads all required parts of broadcast message. */
					String prefix = MessageManager.getChatPrefix();
					String suffix = MessageManager.getChatSuffix();
					/* Shows all chat broadcast messages to command sender. */
					p.sendMessage("§e--------- §fChat broadcast messages: §e---------");
					/* Checks if any messages are configured. */
					if (MessageManager.getChatMessages().size() < 1) {
						p.sendMessage("No messages configured.");
						return true;
					}
					for (int messageID = 0; messageID < MessageManager.getChatMessages().size(); messageID++) {
						String message = MessageManager.getChatMessages().get(messageID).toString();
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "§6" + (messageID+1) + ".§f " + prefix + message + suffix));
					}
				} else if (args[1].equalsIgnoreCase("now")) {
					if (!sender.hasPermission("lobby.broadcast.chat.now")) {
						mm.sendMessage("Messages.noPermission", p);
						return true;
					}
					/* Checks if length of arguments is correct. */
					if (args.length < 3) {
						mm.sendReplacedMessage("%prefix% Pleaser enter a message number.", p);
						return true;
					}
					/* Checks if length of arguments is correct. */
					if (args.length > 3) {
						mm.sendReplacedMessage("%prefix% §cPleaser enter only one message number.", p);
						return true;
					}
					try {
						int messageID = Integer.parseInt(args[2]) - 1;
						HashMap<Integer, String> chatMessages = MessageManager.getChatMessages();
						if (messageID > -1 && messageID < chatMessages.size()) {
							chatBroadcast.broadcastExistingMessage(messageID);
							mm.sendReplacedMessage("%prefix% Successfully broadcasted message #" + args[2] + ".", p);
						} else {
							mm.sendReplacedMessage("%prefix% §cPlease choose a number between 1 and " + chatMessages.size() + ".", p);
						}
					} catch(NumberFormatException nfe) {
						mm.sendReplacedMessage("%prefix% §cPlease enter a valid number.", p);
						return true;
					}
				} else if (args[1].equalsIgnoreCase("next")) {
					if (!sender.hasPermission("lobby.broadcast.chat.next")) {
						mm.sendMessage("Messages.noPermission", p);
						return true;
					}
					/* Checks if "randomizeMessages" is activated. */
					if (Main.getInstance().getConfig().getBoolean("chat.randomizeMessages")) {
						mm.sendReplacedMessage("%prefix% §cSkipping messages only is available when \"randomizeMessages\" is set to \"false\" in config.", p);
						return true;
					}
					/* Gets current message counter. */
					int messageCounter = ChatBroadcast.getMessageCounter();
					/* Skips message based on message counter. */
					if (messageCounter < MessageManager.getChatMessages().size()) {
						ChatBroadcast.setMessageCounter(messageCounter + 1);
						mm.sendReplacedMessage("%prefix% Successfully skipped message #" + (messageCounter + 1) + ".", p);
					} else {
						ChatBroadcast.setMessageCounter(1);
						mm.sendReplacedMessage("%prefix% Successfully skipped message #1.", p);
					}
				} else if (args[1].equalsIgnoreCase("ignore")) {
					if (!sender.hasPermission("lobby.broadcast.chat.ignore")) {
						mm.sendMessage("Messages.noPermission", p);
						return true;
					}
					/* Checks if length of arguments is correct. */
					if (args.length < 3) {
						mm.sendReplacedMessage("%prefix% §cPleaser enter a player name.", p);
						return true;
					}
					/* Checks if length of arguments is correct. */
					if (args.length > 3) {
						mm.sendReplacedMessage("%prefix% $cPleaser enter only one player name.", p);
						return true;
					}
					/* Gets player specified in command. */
					Player player = Bukkit.getServer().getPlayerExact(args[2]);
					/* Checks if player is online. */
					if (player == null) {
						mm.sendReplacedMessage("%prefix% §cYou can only add players who are currently online.", p);
						return true;
					}
					/* Checks if player already is listed in ignore list and performs action. */
					if (IgnoreManager.getChatIgnoreList().contains(player.getUniqueId().toString())) {
						IgnoreManager.getChatIgnoreList().remove(player.getUniqueId().toString());
						mm.sendReplacedMessage("%prefix% Successfully removed §7" + args[2] + "§2 from ignore list.", p);
					} else {
						IgnoreManager.getChatIgnoreList().add(player.getUniqueId().toString());
						mm.sendReplacedMessage("%prefix% Successfully added §7" + args[2] + "§2 to ignore list.", p);
					}
					ignoreManager.updateChatIgnoreList();
				}
			} else if (args.length > 1 && args[0].equalsIgnoreCase("bossbar")) {
				// TODO
			} else {
				thisCmdInfo(p);
			}
			return false;
		} else {
			sender.sendMessage(ChatColor.RED + "You have to be a player!");
		}
		return true;
	}

	private void thisCmdInfo(Player p) {
		p.sendMessage("----§3Lobby§bReloaded§f----(§9Broadcast§f)-----------------------");
		p.sendMessage("§6/bradcast §f| §eMain command");
		p.sendMessage("§6/bradcast chat §f| §eChat bradcast main command");
		p.sendMessage(" ");
		p.sendMessage("§6/bradcast chat start §f| §eStarts chat bradcast");
		p.sendMessage("§6/bradcast chat list §f| §eLists all chat bradcast messages");
		p.sendMessage("§6/bradcast chat now {MSGnumber} §f| §eBroadcast the message {MSGnumber}");
		p.sendMessage("§6/bradcast chat next §f| §eSkipps to the next message");
		p.sendMessage("§6/bradcast chat ignore {player} §f| §eThe player {player} don't gets any broadcasted messages");
		p.sendMessage("§f------------------------------------------------------");
	}

	private void CmdInfo(Player p) {
		LobbyCMD.cmdInfo(p);
	}
}