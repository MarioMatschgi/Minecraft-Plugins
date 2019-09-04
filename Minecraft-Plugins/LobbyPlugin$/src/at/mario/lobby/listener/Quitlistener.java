package at.mario.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.mario.lobby.Main;
import at.mario.lobby.Pets;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;
import at.mario.lobby.other.autoMessage.broadcasts.Broadcast;
import at.mario.lobby.other.autoMessage.broadcasts.BroadcastStatus;

public class Quitlistener implements Listener {

	private Broadcast broadcast = new Broadcast();
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		Pets pets = new Pets();
		Player p = e.getPlayer();
		
		if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".pet") && dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") != null) {
			pets.removePet(p);
		}
		
		/* Checks if chat broadcast status is not disabled. */
		if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.DISABLED) {
			return;
		}
		/* TODO Comment */
		if (Main.getInstance().getConfig().getBoolean("chat.requireOnlinePlayers") && Bukkit.getServer().getOnlinePlayers().size() == 1) {
			broadcast.cancelChatBroadcast();
			Broadcast.setChatBroadcastStatus(BroadcastStatus.WAITING);
		}
		
		if (Main.getInstance().getConfig().getBoolean("Config.customQuitMessage") == true) {
			e.setQuitMessage(mm.getMessages().getString("Messages.quitMessage").replace("%player%", p.getName()).replace("&", "§"));
		}
	}
}
