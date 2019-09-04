package at.mario.lobby.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.Pets;
import at.mario.lobby.inventories.ParticleInventory;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.ScoreboardCFGManager;
import at.mario.lobby.manager.ConfigManagers.TablistCFGManager;
import at.mario.lobby.scoreboards.MainScoreboard;

public class MoveListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		DataManager dm = new DataManager();
		TablistCFGManager tm = new TablistCFGManager();
		ScoreboardCFGManager sm = new ScoreboardCFGManager();
		Pets pets = new Pets();
		
		Player p = e.getPlayer();
		//Entity entity = InventoryClick.pets.get(p.getName());
		
		if (dm.getData().contains("Data.lobby.spawn")) {
			ItemStack wand = new ItemStack(Material.BLAZE_ROD);
			ItemMeta wandMeta = wand.getItemMeta();
			wandMeta.setDisplayName(Main.wandName);
			ArrayList<String> wandList = new ArrayList<String>();
			wandList.add("§2Leftclick to set the first position");
			wandList.add("§aRightclick to set the second position");
			wandMeta.setLore(wandList);
			wand.setItemMeta(wandMeta);
			
			if (isinLobby(p) == false && (wasinLobby(p)) == true) {
				try {
					Main.removeLobbyItems(p);
				} catch (Exception e2) {  }
				MainScoreboard.removeScoreboard(p);
				
				try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				pets.removePet(p);
				
				if (sm.getScoreboardCFG().getString("Scoreboard.region").equalsIgnoreCase("lobby")) {
					if (Main.isinLobby(p.getLocation())) {
						try {
							Bukkit.getScheduler().cancelTask(MainScoreboard.taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}
						
						MainScoreboard.setScoreboard(p);
					} else {
						// MainScoreboard.removeScoreboard(p);
					}
				} else if (sm.getScoreboardCFG().getString("Scoreboard.region").equalsIgnoreCase("world")) {
					Location location = (Location) dm.getData().get("Data.lobby.location.loc1");
					if (p.getLocation().getWorld().equals(location.getWorld())) {
						try {
							Bukkit.getScheduler().cancelTask(MainScoreboard.taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}

						MainScoreboard.setScoreboard(p);
					} else {
						MainScoreboard.removeScoreboard(p);
					}
				} else {
					MainScoreboard.setScoreboard(p);
				}
				

				JoinListener jl = new JoinListener();
				if (tm.getTablistCFG().getString("Tablist.region").equalsIgnoreCase("lobby")) {
					if (Main.isinLobby(p.getLocation())) {
						try {
							Bukkit.getScheduler().cancelTask(JoinListener.taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}
						
						jl.setTablist(p);
					} else {
						jl.removeTablist(p);
					}
				} else if (tm.getTablistCFG().getString("Tablist.region").equalsIgnoreCase("world")) {
					Location location = (Location) dm.getData().get("Data.lobby.location.loc1");
					if (p.getLocation().getWorld().equals(location.getWorld())) {
						try {
							Bukkit.getScheduler().cancelTask(JoinListener.taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}

						jl.setTablist(p);
					} else {
						jl.removeTablist(p);
					}
				} else {
					jl.setTablist(p);
				}
				
				
			} else if (isinLobby(p) == true && (wasinLobby(p)) == false) {
				Main.giveLobbyItems(p);
				p.setFoodLevel(20);
				p.setSaturation(20);
				p.setHealth(20);
				
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") != null) {
					pets.createPet(p, dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet"));
				}
				
				if (sm.getScoreboardCFG().getString("Scoreboard.region").equalsIgnoreCase("lobby")) {
					if (Main.isinLobby(p.getLocation())) {
						try {
							Bukkit.getScheduler().cancelTask(MainScoreboard.taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}
						
						MainScoreboard.setScoreboard(p);
					} else {
						// MainScoreboard.removeScoreboard(p);
					}
				} else if (sm.getScoreboardCFG().getString("Scoreboard.region").equalsIgnoreCase("world")) {
					Location location = (Location) dm.getData().get("Data.lobby.location.loc1");
					if (p.getLocation().getWorld().equals(location.getWorld())) {
						try {
							Bukkit.getScheduler().cancelTask(MainScoreboard.taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}

						MainScoreboard.setScoreboard(p);
					} else {
						// MainScoreboard.removeScoreboard(p);
					}
				} else {
					MainScoreboard.setScoreboard(p);
				}
				

				JoinListener jl = new JoinListener();
				if (tm.getTablistCFG().getString("Tablist.region").equalsIgnoreCase("lobby")) {
					if (Main.isinLobby(p.getLocation())) {
						try {
							Bukkit.getScheduler().cancelTask(JoinListener.taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}
						
						jl.setTablist(p);
					} else {
						jl.removeTablist(p);
					}
				} else if (tm.getTablistCFG().getString("Tablist.region").equalsIgnoreCase("world")) {
					Location location = (Location) dm.getData().get("Data.lobby.location.loc1");
					if (p.getLocation().getWorld().equals(location.getWorld())) {
						try {
							Bukkit.getScheduler().cancelTask(JoinListener.taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}

						jl.setTablist(p);
					} else {
						jl.removeTablist(p);
					}
				} else {
					jl.setTablist(p);
				}
				
				
			}
		}
		if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".doubleJump")) {
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".doubleJump").equalsIgnoreCase("enabled")) {
				if (Main.isinLobby(p.getLocation()) && (p.getGameMode() != GameMode.CREATIVE) && (p.isOnGround()) && (!p.isFlying())) {
					if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".fly")) {
						if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".fly") == false) {
							p.setAllowFlight(true);
						}
					} else {
						p.setAllowFlight(true);
					}
				}
			} else {
				if (Main.isinLobby(p.getLocation()) && (p.getGameMode() != GameMode.CREATIVE) && (p.isOnGround()) && (!p.isFlying())) {
					if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".fly")) {
						if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".fly") == false) {
							if (p.getGameMode() != GameMode.CREATIVE) {
								p.setAllowFlight(false);
							}
						}
					}
				}
			}
		} else {
			dm.getData().set("Data." + p.getName().toLowerCase() + ".doubleJump", "disabled");
			dm.saveData();
		}

		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.world", p.getLocation().getWorld().getName());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.pitch", p.getLocation().getPitch());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.yaw", p.getLocation().getYaw());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.x", p.getLocation().getX());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.y", p.getLocation().getY());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.z", p.getLocation().getZ());
		dm.saveData();
	}
	
	
	public Boolean isinLobby(Player p) {
		if ( ( (p.getLocation().getX() >= smallerX()) && (p.getLocation().getY() >= smallerY()) && (p.getLocation().getZ() >= smallerZ()) ) && 
				( (p.getLocation().getX() <= biggerX()) && (p.getLocation().getY() <= biggerY()) && (p.getLocation().getZ() <= biggerZ()) ) ) {
			return true;
		} else {
			return false;
		}
		// return false;
	}
	
	public Boolean wasinLobby(Player p) {
		DataManager dm = new DataManager();
		
		Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data." + p.getName().toLowerCase() +".position.world")));
		loc.setPitch((float) dm.getData().get("Data." + p.getName().toLowerCase() +".position.pitch"));
		loc.setYaw((float) dm.getData().get("Data." + p.getName().toLowerCase() +".position.yaw"));
		loc.setX(dm.getData().getDouble("Data." + p.getName().toLowerCase() +".position.x"));
		loc.setY(dm.getData().getDouble("Data." + p.getName().toLowerCase() +".position.y"));
		loc.setZ(dm.getData().getDouble("Data." + p.getName().toLowerCase() +".position.z"));
		
		if ( ( (loc.getX() >= smallerX()) && (loc.getY() >= smallerY()) && (loc.getZ() >= smallerZ()) ) && 
				( (loc.getX() <= biggerX()) && (loc.getY() <= biggerY()) && (loc.getZ() <= biggerZ()) ) ) {
			return true;
		} else {
			return false;
		}
	}
	
	public Double smallerX() {
		DataManager dm = new DataManager();

		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc1.world")));
		loc1.setX(dm.getData().getDouble("Data.lobby.location.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.lobby.location.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.lobby.location.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc2.world")));
		loc2.setX(dm.getData().getDouble("Data.lobby.location.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.lobby.location.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.lobby.location.loc2.z"));

		if (loc1.getX() < loc2.getX()) {
			return loc1.getX();
		} else {
			return loc2.getX();
		}
	}
	
	public Double smallerY() {
		DataManager dm = new DataManager();

		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc1.world")));
		loc1.setX(dm.getData().getDouble("Data.lobby.location.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.lobby.location.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.lobby.location.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc2.world")));
		loc2.setX(dm.getData().getDouble("Data.lobby.location.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.lobby.location.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.lobby.location.loc2.z"));
		
		if (loc1.getY() < loc2.getY()) {
			return loc1.getY();
		} else {
			return loc2.getY();
		}
	}
	
	public Double smallerZ() {
		DataManager dm = new DataManager();

		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc1.world")));
		loc1.setX(dm.getData().getDouble("Data.lobby.location.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.lobby.location.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.lobby.location.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc2.world")));
		loc2.setX(dm.getData().getDouble("Data.lobby.location.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.lobby.location.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.lobby.location.loc2.z"));
		
		if (loc1.getZ() < loc2.getZ()) {
			return loc1.getZ();
		} else {
			return loc2.getZ();
		}
	}
	
	public Double biggerX() {
		DataManager dm = new DataManager();

		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc1.world")));
		loc1.setX(dm.getData().getDouble("Data.lobby.location.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.lobby.location.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.lobby.location.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc2.world")));
		loc2.setX(dm.getData().getDouble("Data.lobby.location.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.lobby.location.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.lobby.location.loc2.z"));
		
		if (loc1.getX() > loc2.getX()) {
			return loc1.getX();
		} else {
			return loc2.getX();
		}
	}
	
	public Double biggerY() {
		DataManager dm = new DataManager();

		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc1.world")));
		loc1.setX(dm.getData().getDouble("Data.lobby.location.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.lobby.location.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.lobby.location.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc2.world")));
		loc2.setX(dm.getData().getDouble("Data.lobby.location.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.lobby.location.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.lobby.location.loc2.z"));
		
		if (loc1.getY() > loc2.getY()) {
			return loc1.getY();
		} else {
			return loc2.getY();
		}
	}
	
	public Double biggerZ() {
		DataManager dm = new DataManager();

		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc1.world")));
		loc1.setX(dm.getData().getDouble("Data.lobby.location.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.lobby.location.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.lobby.location.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.lobby.location.loc2.world")));
		loc2.setX(dm.getData().getDouble("Data.lobby.location.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.lobby.location.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.lobby.location.loc2.z"));
		
		if (loc1.getZ() > loc2.getZ()) {
			return loc1.getZ();
		} else {
			return loc2.getZ();
		}
	}
}
