package at.mario.gravity.scoreboards;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import at.mario.gravity.Main;
import at.mario.gravity.Roles;
import at.mario.gravity.countdowns.GameCountdown;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.gamestates.LobbyState;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;
import at.mario.gravity.manager.ConfigManagers.ScoreboardCFGManager;

public class FinishedScoreboard {
	
	public static void setScoreboard(String arenaName, Player p) {
		MessagesManager mm = new MessagesManager();
		
		List<String> list = ScoreboardCFGManager.getScoreboardCFG().getStringList("Scoreboard.finishedScoreboard.lines");

		HashMap<List<ConfigurationSection>, Integer> allMaps = LobbyState.ArenaVotes.get(arenaName);
		int[] votes = new int[Main.getInstance().getConfig().getInt("Config.levelAmount")];
		int highestVote = 0;
		
		int temp = 0;
		for (Entry<List<ConfigurationSection>, Integer> entry : allMaps.entrySet()) {
		    // List<ConfigurationSection> key = entry.getKey();
		    Integer value = entry.getValue();
		    
		    votes[temp] = value;
		    temp++;
		}
		
		for (int counter = 1; counter < votes.length; counter++) {
			if (votes[counter] > highestVote) {
				highestVote = votes[counter];
			}
		}
		List<ConfigurationSection> votedMaps = new ArrayList<ConfigurationSection>();
		for (Entry<List<ConfigurationSection>, Integer> entry : allMaps.entrySet()) {
		    List<ConfigurationSection> key2 = entry.getKey();
		    Integer value = entry.getValue();
		    
		    if (value == highestVote) {
		    	votedMaps = key2;
		    }
		}
		
		
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("game", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		String map = mm.getMessages().getString("Messages.scoreboard.finished");
		if (IngameState.ArenaFinishedPlayerLevels.containsKey(arenaName) && IngameState.ArenaFinishedPlayerLevels.get(arenaName).containsKey(p) && IngameState.ArenaFinishedPlayerLevels.get(arenaName) != null && 
				IngameState.ArenaFinishedPlayerLevels.get(arenaName).get(p) != null) {
			if (votedMaps.get(IngameState.ArenaFinishedPlayerLevels.get(arenaName).get(p) - 1) != null) {
				map = votedMaps.get(IngameState.ArenaFinishedPlayerLevels.get(arenaName).get(p) - 1).getName();
			}
		}
		obj.setDisplayName(ScoreboardCFGManager.getScoreboardCFG().getString("Scoreboard.gameScoreboard.title").replace("%arena%", arenaName).replace("%map%", map));

		int spaceIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);

			if (string.equals("") || string == "" || string == null) {
				for (int j = 0; j < spaceIndex; j++) {
					string = string + " ";
				}
				spaceIndex++;
			}
			
			Double moNey = 0.0;
			if (Main.eco != null) {
				if (Main.eco.hasAccount(p)) {
					moNey = Main.eco.getBalance(p);
				}
			}
			int Money = moNey.intValue();
			String money = NumberFormat.getInstance().format(Money);
			
			HashMap<String, Integer> secondsMap = GameCountdown.getSeconds();
			int seconds = Main.getInstance().getConfig().getInt("Config.maxGameLenght");
			if (secondsMap.containsKey(arenaName)) {
				seconds = secondsMap.get(arenaName);
			}
			
			int minutes = seconds / 60;
			 seconds = seconds - (minutes * 60);
			int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
			int fallers = 0;
			
			for (Object value : Roles.roles.values()) {
				if (value == Roles.FALLER) {
					fallers++;
				}
			}

			HashMap<Player, Long> playersMap = new HashMap<Player, Long>();
			if (IngameState.ArenaFinishedPlayers.containsKey(arenaName) && IngameState.ArenaFinishedPlayers.get(arenaName) != null) {
				playersMap = IngameState.ArenaFinishedPlayers.get(arenaName);
			}
			playersMap = Main.sortHashMapByValues(playersMap);
			
			List<Player> players = new ArrayList<Player>();
			for (Player player : playersMap.keySet()) {
				players.add(player);
			}
			
			String first = mm.getMessages().getString("Messages.scoreboard.noPlayer");
			if (players != null && !players.isEmpty()) {
				if (players.size() >= 1) {
					first = "§9" + (players.get(0).getName());
				}
			}
			String second = mm.getMessages().getString("Messages.scoreboard.noPlayer");
			if (players != null && !players.isEmpty()) {
				if (players.size() >= 2) {
					second = "§9" + (players.get(1).getName());
				}
			}
			String third = mm.getMessages().getString("Messages.scoreboard.noPlayer");
			if (players != null && !players.isEmpty()) {
				if (players.size() >= 3) {
					third = "§9" + (players.get(2).getName());
				}
			}
			String fourth = mm.getMessages().getString("Messages.scoreboard.noPlayer");
			if (players != null && !players.isEmpty()) {
				if (players.size() >= 4) {
					fourth = "§9" + (players.get(3).getName());
				}
			}
			String fifth = mm.getMessages().getString("Messages.scoreboard.noPlayer");
			if (players != null && !players.isEmpty()) {
				if (players.size() >= 5) {
					fifth = "§9" + (players.get(4).getName());
				}
			}
			
			String color1 = mm.getMessages().getString("Messages.scoreboard.colorIfNoPlayer");
			if (!first.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				color1 = mm.getMessages().getString("Messages.scoreboard.colorIfPlayer");
			}
			String color2 = mm.getMessages().getString("Messages.scoreboard.colorIfNoPlayer");
			if (!second.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				color2 = mm.getMessages().getString("Messages.scoreboard.colorIfPlayer");
			}
			String color3 = mm.getMessages().getString("Messages.scoreboard.colorIfNoPlayer");
			if (!third.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				color3 = mm.getMessages().getString("Messages.scoreboard.colorIfPlayer");
			}
			String color4 = mm.getMessages().getString("Messages.scoreboard.colorIfNoPlayer");
			if (!fourth.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				color4 = mm.getMessages().getString("Messages.scoreboard.colorIfPlayer");
			}
			String color5 = mm.getMessages().getString("Messages.scoreboard.colorIfNoPlayer");
			if (!fifth.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				color5 = mm.getMessages().getString("Messages.scoreboard.colorIfPlayer");
			}
			
			String time1 = "";
			if (!first.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				int[] time = splitToComponentTimes(new BigDecimal(TimeUnit.MILLISECONDS.toSeconds(IngameState.ArenaFinishedPlayers.get(arenaName).get(players.get(0)))));
				time1 = time[0] + ":" + String.format("%02d", time[1]);
			}
			String time2 = "";
			if (!second.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				int[] time = splitToComponentTimes(new BigDecimal(TimeUnit.MILLISECONDS.toSeconds(IngameState.ArenaFinishedPlayers.get(arenaName).get(players.get(1)))));
				time2 = time[0] + ":" + String.format("%02d", time[1]);
			}
			String time3 = "";
			if (!third.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				int[] time = splitToComponentTimes(new BigDecimal(TimeUnit.MILLISECONDS.toSeconds(IngameState.ArenaFinishedPlayers.get(arenaName).get(players.get(2)))));
				time3 = time[0] + ":" + String.format("%02d", time[1]);
			}
			String time4 = "";
			if (!fourth.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				int[] time = splitToComponentTimes(new BigDecimal(TimeUnit.MILLISECONDS.toSeconds(IngameState.ArenaFinishedPlayers.get(arenaName).get(players.get(3)))));
				time4 = time[0] + ":" + String.format("%02d", time[1]);
			}
			String time5 = "";
			if (!fifth.equals(mm.getMessages().getString("Messages.scoreboard.noPlayer"))) {
				int[] time = splitToComponentTimes(new BigDecimal(TimeUnit.MILLISECONDS.toSeconds(IngameState.ArenaFinishedPlayers.get(arenaName).get(players.get(4)))));
				time5 = time[0] + ":" + String.format("%02d", time[1]);
			}
			
			obj.getScore(string.replace("%money%", money).replace("%arena%", arenaName).replace("%currentplayers%", currentPlayers+"").replace("%seconds%", String.format("%02d", seconds)).replace("%minutes%", minutes+"").
					replace("%timeleft%", minutes + ":" + String.format("%02d", seconds)).replace("%fallers%", fallers+"").replace("%map%", map).
					replace("%stagenumber%", IngameState.ArenaFinishedPlayerLevels.get(arenaName).get(p)+"").
					replace("%player1%", first).replace("%player2%", second).replace("%player3%", third).replace("%player4%", fourth).replace("%player5%", fifth).
					replace("%color1%", color1).replace("%color2%", color2).replace("%color3%", color3).replace("%color4%", color4).replace("%color5%", color5).
					replace("%time1%", time1).replace("%time2%", time2).replace("%time3%", time3).replace("%time4%", time4).replace("%time5%", time5))
					
					.setScore( (i * -1) + list.size());
		}
		obj.getScore("                                        ").setScore(list.size() + 1);
		
		p.setScoreboard(board);
	}
	
	public static void removeScoreboard(Player p) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
		}, 20L);
	}
	
	public static int[] splitToComponentTimes(BigDecimal biggy) {
		long longVal = biggy.longValue();
		int hours = (int) longVal / 3600;
		int remainder = (int) longVal - hours * 3600;
		int mins = remainder / 60;
		remainder = remainder - mins * 60;
		int secs = remainder;

		int[] ints = {mins , secs};
		return ints;
	}
}
