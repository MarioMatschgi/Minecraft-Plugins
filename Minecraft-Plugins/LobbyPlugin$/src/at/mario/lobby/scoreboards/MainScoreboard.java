package at.mario.lobby.scoreboards;


import java.text.NumberFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.ScoreboardCFGManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MainScoreboard {

	public static HashMap<String, Integer> taskIDs = new HashMap<String, Integer>();
	static int index = 1;
	
	public static void setScoreboard(Player p) {
		DataManager dm = new DataManager();
		ScoreboardCFGManager sm = new ScoreboardCFGManager();
		
		taskIDs.put(p.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (sm.getScoreboardCFG().getBoolean("Scoreboard.enabled") == false || p.isOnline() == false) {
					try {
						Bukkit.getScheduler().cancelTask(taskIDs.get(p.getName()));
					} catch (Exception exeption) {					}
					// removeScoreboard(p);
					return;
				}
				
				if (sm.getScoreboardCFG().getString("Scoreboard.region").equalsIgnoreCase("lobby")) {
					if (!Main.isinLobby(p.getLocation())) {
						try {
							Bukkit.getScheduler().cancelTask(taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}

						return;
					}
				} else if (sm.getScoreboardCFG().getString("Scoreboard.region").equalsIgnoreCase("world")) {
					Location location = (Location) dm.getData().get("Data.lobby.location.loc1");
					if (!p.getLocation().getWorld().equals(location.getWorld())) {
						try {
							Bukkit.getScheduler().cancelTask(taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}

						return;
					}
				}
				
				Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
				Objective obj = board.registerNewObjective("MainBoars1464234", "MainBoars1464234");
				
				obj.setDisplaySlot(DisplaySlot.SIDEBAR);
				obj.setDisplayName(sm.getScoreboardCFG().getString("Scoreboard.title"));
				
				if (!sm.getScoreboardCFG().contains("Scoreboard.lines." + index)) {
					index = 1;
				}
				int spaceIndex = 0;
				for (int i = 0; i < sm.getScoreboardCFG().getList("Scoreboard.lines." + index).size(); i++) {
					String name = (String) sm.getScoreboardCFG().getList("Scoreboard.lines." + index).get(i);
					if (name.equals("") || name == "" || name == null) {
						for (int j = 0; j < spaceIndex; j++) {
							name = name + " ";
						}
						spaceIndex++;
					}
					Double moNey = Main.eco.getBalance(p);
					int Money = moNey.intValue();
					String money = NumberFormat.getInstance().format(Money);
					
					String prefix = PermissionsEx.getUser(p).getGroups()[0].getPrefix();
					if (prefix == null) {
						prefix = "No Rank";
					}
					obj.getScore(name.replace("%player%", p.getName()).replace("%money%", money).replace("%rank%", prefix.replace("&", "§")) + "").setScore((i *-1) + sm.getScoreboardCFG().getList("Scoreboard.lines." + index).size());
				}
				obj.getScore("                                        ").setScore(sm.getScoreboardCFG().getList("Scoreboard.lines." + index).size());
				
				index++;
				p.setScoreboard(board);
			}
		}, 0, sm.getScoreboardCFG().getInt("Scoreboard.time")) );
	}
	
	public static void removeScoreboard(Player p) {
		if (p.getScoreboard().getObjective("MainBoars1464234") != null) {
			try {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					public void run() {
						p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
					}
				}, 20L);
			} catch (Exception ex) {		}
		}
	}
}
/*
		int index = 1;
		for (int i = 0; i < sm.getScoreboardCFG().getList("Scoreboard.lines").size(); i++) {
			String name = (String) sm.getScoreboardCFG().getList("Scoreboard.lines").get(i);
			if (name.equals("") || name == "" || name == null) {
				for (int j = 0; j < index; j++) {
					name = name + " ";
				}
				index++;
			}
			obj.getScore(name+"").setScore((i *-1) + sm.getScoreboardCFG().getList("Scoreboard.lines").size());
		}
		obj.getScore("").setScore(sm.getScoreboardCFG().getList("Scoreboard.lines").size());
*/