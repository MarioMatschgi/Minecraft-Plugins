package at.mario.resourcebattlereloaded.countdowns;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import at.mario.resourcebattlereloaded.Main;
import at.mario.resourcebattlereloaded.scoreboards.MainScoreboard;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle.EnumTitleAction;

public class MainCountdown {
	
	public static boolean isRunning;
	
	private static int taskID;
	public static int ticks;
	
	public static void start(int minutes) {
		//MessagesManager mm = new MessagesManager();
		
		ticks = minutes * 60 * 20;
		isRunning = true;
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				for (Player player: Bukkit.getOnlinePlayers()) {
					Main.UpdateScore(player);
					MainScoreboard.setScoreboard(player);

					
					float a = ticks / 20.0f;
					if ( a % 1 == 0 && ((List<Integer>) Main.getInstance().getConfig().get("Config.title.showAtTimes")).contains((int) a) ) {
						PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"§cOnly §9" + ticks / 20 + " §csecond left\"}"), 
								Main.getInstance().getConfig().getInt("Config.title.scheduled.fadeIn"), 
								Main.getInstance().getConfig().getInt("Config.title.scheduled.stay"), 
								Main.getInstance().getConfig().getInt("Config.title.scheduled.fadeOut"));
		                PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"§cHurry up!\"}"),
								Main.getInstance().getConfig().getInt("Config.title.scheduled.fadeIn"), 
								Main.getInstance().getConfig().getInt("Config.title.scheduled.stay"), 
								Main.getInstance().getConfig().getInt("Config.title.scheduled.fadeOut"));
		                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
					}
					else if ((ticks / 20.0f / 60.0f) != 0 && (ticks / 20.0f / 60.0f) % Main.getInstance().getConfig().getInt("Config.title.showAllMinutes") == 0 ) {
						PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"§6" + (ticks / 20 / 60) + " §bminutes left\"}"),
								Main.getInstance().getConfig().getInt("Config.title.other.fadeIn"), 
								Main.getInstance().getConfig().getInt("Config.title.other.stay"), 
								Main.getInstance().getConfig().getInt("Config.title.other.fadeOut"));
		                PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"§3Dont hurry§f, §3you got plenty of time\"}"),
								Main.getInstance().getConfig().getInt("Config.title.other.fadeIn"), 
								Main.getInstance().getConfig().getInt("Config.title.other.stay"), 
								Main.getInstance().getConfig().getInt("Config.title.other.fadeOut"));
		                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
					}
				}
				
				ticks -= Main.getInstance().getConfig().getInt("Config.scoreboardUpdateTicks");
				if (ticks < 0)
					cancel();
			}
			
		}, 0, Main.getInstance().getConfig().getInt("Config.scoreboardUpdateTicks"));
	}

	public static void cancel() {
		Bukkit.getScheduler().cancelTask(taskID);

		Main.StopBattle();
		
		isRunning = false;
	}
}
