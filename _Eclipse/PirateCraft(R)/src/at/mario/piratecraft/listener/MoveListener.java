package at.mario.piratecraft.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;

public class MoveListener implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		DataManager dm = new DataManager();
		
		Player p = e.getPlayer();
		
		Boolean inArena = false;
		for (List<Player> players : Main.ArenaPlayer.values()) {
			if (players.contains(p)) {
				inArena = true;
				break;
			}
		}
		if (inArena == false) {
			dm.getData().set("Data." + p.getName() + ".position.world", p.getLocation().getWorld().getName());
			dm.getData().set("Data." + p.getName() + ".position.pitch", p.getLocation().getPitch());
			dm.getData().set("Data." + p.getName() + ".position.yaw", p.getLocation().getYaw());
			dm.getData().set("Data." + p.getName() + ".position.x", p.getLocation().getX());
			dm.getData().set("Data." + p.getName() + ".position.y", p.getLocation().getY());
			dm.getData().set("Data." + p.getName() + ".position.z", p.getLocation().getZ());
			dm.saveData();
		}
	}
	
	
	public Boolean isinArena(String arenaName, Player p) {
		if ( ( (p.getLocation().getX() >= smallerX(arenaName)) && (p.getLocation().getY() >= smallerY(arenaName)) && (p.getLocation().getZ() >= smallerZ(arenaName)) ) && 
				( (p.getLocation().getX() <= biggerX(arenaName)) && (p.getLocation().getY() <= biggerY(arenaName)) && (p.getLocation().getZ() <= biggerZ(arenaName)) ) ) {
			return true;
		} else {
			return false;
		}
		// return false;
	}
	
	public Boolean wasinArena(String arenaName, Player p) {
		DataManager dm = new DataManager();
		
		Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data." + p.getName() + ".position.world")));
		loc.setPitch((float) dm.getData().get("Data." + p.getName() + ".position.pitch"));
		loc.setYaw((float) dm.getData().get("Data." + p.getName() + ".position.yaw"));
		loc.setX(dm.getData().getDouble("Data." + p.getName() + ".position.x"));
		loc.setY(dm.getData().getDouble("Data." + p.getName() + ".position.y"));
		loc.setZ(dm.getData().getDouble("Data." + p.getName() + ".position.z"));
		
		if ( ( (loc.getX() >= smallerX(arenaName)) && (loc.getY() >= smallerY(arenaName)) && (loc.getZ() >= smallerZ(arenaName)) ) && 
				( (loc.getX() <= biggerX(arenaName)) && (loc.getY() <= biggerY(arenaName)) && (loc.getZ() <= biggerZ(arenaName)) ) ) {
			return true;
		} else {
			return false;
		}
	}
	
	public Double smallerX(String arenaName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.z"));

		if (loc1.getX() < loc2.getX()) {
			return loc1.getX();
		} else {
			return loc2.getX();
		}
	}
	
	public Double smallerY(String arenaName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.z"));
		
		if (loc1.getY() < loc2.getY()) {
			return loc1.getY();
		} else {
			return loc2.getY();
		}
	}
	
	public Double smallerZ(String arenaName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.z"));
		
		if (loc1.getZ() < loc2.getZ()) {
			return loc1.getZ();
		} else {
			return loc2.getZ();
		}
	}
	
	public Double biggerX(String arenaName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.z"));
		
		if (loc1.getX() > loc2.getX()) {
			return loc1.getX();
		} else {
			return loc2.getX();
		}
	}
	
	public Double biggerY(String arenaName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.z"));
		
		if (loc1.getY() > loc2.getY()) {
			return loc1.getY();
		} else {
			return loc2.getY();
		}
	}
	
	public Double biggerZ(String arenaName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.z"));
		
		if (loc1.getZ() > loc2.getZ()) {
			return loc1.getZ();
		} else {
			return loc2.getZ();
		}
	}
}
