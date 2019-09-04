package at.mario.masterbuilders.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import at.mario.masterbuilders.Main;
import at.mario.masterbuilders.manager.ConfigManagers.DataManager;
import at.mario.masterbuilders.manager.ConfigManagers.StatsManager;
import at.mario.masterbuilders.pictureLogin.me.itsnathang.picturelogin.util.PictureWrapper;

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
		
		if (!StatsManager.hasStats(p)) {
			StatsManager.setStats(p, 0, 0, 0, 0, 0, 0);
		}

		dm.getData().set("Data." + p.getName() + ".position.world", p.getLocation().getWorld().getName());
		dm.getData().set("Data." + p.getName() + ".position.pitch", p.getLocation().getPitch());
		dm.getData().set("Data." + p.getName() + ".position.yaw", p.getLocation().getYaw());
		dm.getData().set("Data." + p.getName() + ".position.x", p.getLocation().getX());
		dm.getData().set("Data." + p.getName() + ".position.y", p.getLocation().getY());
		dm.getData().set("Data." + p.getName() + ".position.z", p.getLocation().getZ());
		dm.saveData();
		
		initialiseIMG();
	}
	  
	private void initialiseIMG() {
		PictureWrapper wrapper = new PictureWrapper(this.plugin, this.player);
		wrapper.sendNOImage();
	}
}
