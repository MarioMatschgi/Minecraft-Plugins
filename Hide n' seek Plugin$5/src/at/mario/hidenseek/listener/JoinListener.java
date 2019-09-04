package at.mario.hidenseek.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;
import at.mario.hidenseek.pictureLogin.me.itsnathang.picturelogin.util.PictureWrapper;

public class JoinListener implements Listener {
	
	private Main plugin;
	private Player player;
	  
	public JoinListener(Main plugin) {
		this.plugin = plugin;
	}
	  
	@EventHandler(priority=EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent e) {
		DataManager dm = new DataManager();
		
		player = e.getPlayer();
		Player p = e.getPlayer();
		
		if (!dm.getData().contains("Data." + p.getName() + ".joinmes")) {
			dm.getData().set("Data." + p.getName() + ".joinmes", 0);
			dm.saveData();
		}
		if (!dm.getData().contains("Data." + p.getName() + ".seekerPasses")) {
			dm.getData().set("Data." + p.getName() + ".seekerPasses", 0);
			dm.saveData();
		}
		
		if (!StatsManager.hasStats(p)) {
			StatsManager.setStats(p, 0, 0, 0, 0, 0, 0);
		}
		
		initialiseIMG();
	}
	  
	private void initialiseIMG() {
		PictureWrapper wrapper = new PictureWrapper(this.plugin, this.player);
		wrapper.sendNOImage();
	}
}
