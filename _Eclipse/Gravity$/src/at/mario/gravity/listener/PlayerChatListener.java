package at.mario.gravity.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import at.mario.gravity.Main;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;
import at.mario.gravity.utils.ChatUtil;

public class PlayerChatListener implements Listener {
	
	
	public ArrayList<Player> chatDisabled = new ArrayList<Player>();
	 
	 
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		Player p = e.getPlayer();
		
		String msg = mm.getMessages().getString("Messages.chatFormat");
		if (msg.isBlank()) {
			return;
		}
		
		msg = msg.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%msg%", e.getMessage()).replace("%player%", p.getDisplayName());
		
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			Boolean isInArena = false;
			for (String key : configSection.getKeys(false)) {
				if (Main.ArenaPlayer.containsKey(key)) {
					if (Main.ArenaPlayer.get(key).contains(p)) {
						// ist Spieler
						e.setCancelled(true);
						ChatUtil.sendToArenaOnly(key, msg);
						isInArena = true;
						break;
					}
				} else if (Main.SpectateArenaPlayer.containsKey(key)) {
					if (Main.SpectateArenaPlayer.get(key).contains(p)) {
						// ist Spectator
						e.setCancelled(true);
						ChatUtil.sendToSpectatorsOnly(key, msg);
						isInArena = true;
						break;
					}
				}
			}
			if (isInArena == false) {
				/*
				String msg2 = mm.getMessages().getString("Messages.chatFormatForNoneArena").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%world%", p.getWorld().getName())
						.replace("%msg%", e.getMessage()).replace("%player%", p.getDisplayName());
				
				e.setCancelled(true);
				Main.sendToNormalOnly(msg2);
				*/
				

				for (String key : configSection.getKeys(false)) {
					if (Main.ArenaPlayer.containsKey(key)) {
						chatDisabled.add((Player) Main.ArenaPlayer.get(key));
					}
				}
			       
		        // DISABLING CHAT / CHECKING
		        
				if (chatDisabled.contains(p)) {
					e.setCancelled(true);
					
					// need to cancel receiving messages
					e.getRecipients().remove(p);
		           
					for(Player pl : Bukkit.getOnlinePlayers()) {
						e.getRecipients().remove(pl);
					}
				}
				
				
				
				
				
			}
		}
	}
}
