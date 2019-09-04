package at.mario.gravity.countdowns;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import at.mario.gravity.Main;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.manager.PackageSender;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;
import at.mario.gravity.utils.ChatUtil;

public class IngameLobbyCountdown extends Countdown {

	public GameCountdown gamecountdown;
	private HashMap<String, Integer> taskIDs;
	private HashMap<String, Integer> seconds;
	
	public IngameLobbyCountdown() {
		taskIDs = new HashMap<String, Integer>();
		seconds = new HashMap<String, Integer>();
	}
	
	@Override
	public void run(String arenaName) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (!seconds.containsKey(arenaName)) {
			seconds.put(arenaName, Main.getInstance().getConfig().getInt("Config.ingameLobbyWaitDuration"));
		}

		List<ConfigurationSection> usedMaps = IngameState.ArenaUsedMaps.get(arenaName);
		for (ConfigurationSection configurationSection : usedMaps) {
			for (int x = GameCountdown.smallerX(configurationSection.getName()).intValue(); x <= GameCountdown.biggerX(configurationSection.getName()).intValue(); x++) {
			    for (int y = GameCountdown.smallerY(configurationSection.getName()).intValue(); y <= GameCountdown.biggerY(configurationSection.getName()).intValue(); y++) {
			        for (int z = GameCountdown.smallerZ(configurationSection.getName()).intValue(); z <= GameCountdown.biggerZ(configurationSection.getName()).intValue(); z++) {
			            Location loc = new Location(Bukkit.getWorld(dm.getData().getString("Data.maps." + configurationSection.getName() + ".world")), x, y, z);
			            loc.getBlock().setType(Material.GLASS);
			        }
			    }
			}
		}
		
		taskIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if (Main.getInstance().getConfig().getIntegerList("Config.ingameLobbyWaitBroadcastAt").contains(seconds.get(arenaName))) {
					ChatUtil.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.ingameLobby.broadcast.chat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seconds%", seconds.get(arenaName)+""));
				}

				List<Player> players = Main.ArenaPlayer.get(arenaName);
				if (seconds.get(arenaName) == 5) {
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						PackageSender.sendTitle(player, mm.getMessages().getString("Messages.ingameLobby.broadcast.title").replace("%numberInCircle%", "§a§l⑤").replace("%num%", "5"), "", 1, 1, 1);
					}
				} else if (seconds.get(arenaName) == 4) {
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						PackageSender.sendTitle(player, mm.getMessages().getString("Messages.ingameLobby.broadcast.title").replace("%numberInCircle%", "§a§l④").replace("%num%", "4"), "", 1, 1, 1);
					}
				} else if (seconds.get(arenaName) == 3) {
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						PackageSender.sendTitle(player, mm.getMessages().getString("Messages.ingameLobby.broadcast.title").replace("%numberInCircle%", "§a§l③").replace("%num%", "3"), "", 1, 1, 1);
					}
				} else if (seconds.get(arenaName) == 2) {
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						PackageSender.sendTitle(player, mm.getMessages().getString("Messages.ingameLobby.broadcast.title").replace("%numberInCircle%", "§a§l②").replace("%num%", "2"), "", 1, 1, 1);
					}
				} else if (seconds.get(arenaName) == 1) {
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						PackageSender.sendTitle(player, mm.getMessages().getString("Messages.ingameLobby.broadcast.title").replace("%numberInCircle%", "§a§l①").replace("%num%", "1"), "", 1, 1, 1);
					}
				}
				
				if (seconds.get(arenaName) <= 0) {
					ChatUtil.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.ingameLobby.end.chat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						PackageSender.sendTitle(player, mm.getMessages().getString("Messages.ingameLobby.end.title").replace("%numberInCircle%", "§a§l⓪"), "", 1, 1, 1);
					}
					
					gamecountdown = new GameCountdown();
					gamecountdown.run(arenaName);
					
					cancel(arenaName);
				}
				
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);

					// ANDERES!!! GameScoreboard.setScoreboard(arenaName, player);
					List<ConfigurationSection> usedMaps = IngameState.ArenaUsedMaps.get(arenaName);
					String map1 = usedMaps.get(0).getName();
					String color1 = "§c";
					if (dm.getData().getInt("Data.maps." + map1 + ".difficulty") == 1) {
						color1 = "§a";
					} else if (dm.getData().getInt("Data.maps." + map1 + ".difficulty") == 2) {
						color1 = "§e";
					}
					String map2 = usedMaps.get(1).getName();
					String color2 = "§c";
					if (dm.getData().getInt("Data.maps." + map2 + ".difficulty") == 1) {
						color2 = "§a";
					} else if (dm.getData().getInt("Data.maps." + map2 + ".difficulty") == 2) {
						color2 = "§e";
					}
					String map3 = usedMaps.get(2).getName();
					String color3 = "§c";
					if (dm.getData().getInt("Data.maps." + map3 + ".difficulty") == 1) {
						color3 = "§a";
					} else if (dm.getData().getInt("Data.maps." + map3 + ".difficulty") == 2) {
						color3 = "§e";
					}
					String map4 = usedMaps.get(3).getName();
					String color4 = "§c";
					if (dm.getData().getInt("Data.maps." + map4 + ".difficulty") == 1) {
						color4 = "§a";
					} else if (dm.getData().getInt("Data.maps." + map4 + ".difficulty") == 2) {
						color4 = "§e";
					}
					String map5 = usedMaps.get(4).getName();
					String color5 = "§c";
					if (dm.getData().getInt("Data.maps." + map5 + ".difficulty") == 1) {
						color5 = "§a";
					} else if (dm.getData().getInt("Data.maps." + map5 + ".difficulty") == 2) {
						color5 = "§e";
					}
					PackageSender.sendActionbar(player, mm.getMessages().getString("Messages.actionbar.ingameLobby").replace("%map1%", map1).replace("%map2%", map2).replace("%map3%", map3).replace("%map4%", map4).replace("%map5%", map5).
							replace("%color1%", color1).replace("%color2%", color2).replace("%color3%", color3).replace("%color4%", color4).replace("%color5%", color5));
				}
				seconds.put(arenaName, seconds.get(arenaName) - 1);
			}
			
		}, 0, 1 * 20));
	}

	@Override
	public void cancel(String arenaName) {
		Bukkit.getScheduler().cancelTask(taskIDs.get(arenaName));
		
		seconds.put(arenaName, Main.getInstance().getConfig().getInt("Config.ingameLobbyWaitDuration"));
	}
}
