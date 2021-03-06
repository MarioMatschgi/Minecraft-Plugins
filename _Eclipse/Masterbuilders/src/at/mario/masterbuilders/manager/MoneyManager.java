package at.mario.masterbuilders.manager;

import org.bukkit.entity.Player;

import at.mario.masterbuilders.Main;

public class MoneyManager {
	public static void add(Player p, Integer amt) {
		Main.eco.depositPlayer(p, amt);
	}
	
	public static void remove(Player p, Integer amt) {
		Main.eco.withdrawPlayer(p, amt);
	}
	
	public static void set(Player p, Integer amt) {
		Main.eco.withdrawPlayer(p, Main.eco.getBalance(p));
		Main.eco.depositPlayer(p, amt);
	}
	
	public static Integer getMoney(Player p) {
		Integer amt = (int) Main.eco.getBalance(p);
		return amt;
	}
}
