package at.mario.economy.Manager;


import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class MoneyManager {

	private MoneyManager() { }
	
	private static MoneyManager instance = new MoneyManager();
	
	public static MoneyManager getInstance() {
		return instance;
	}
	
	private Plugin p;
	private FileConfiguration moneycfg;
	private File moneyfile;
	
	public void setup(Plugin p) {
		this.p = p;
		
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}
		
		moneyfile = new File(p.getDataFolder(), "money.yml");
		
		if (!moneyfile.exists()) {
			try { moneyfile.createNewFile(); }
			catch (Exception e) { e.printStackTrace(); }
		}
		moneycfg = YamlConfiguration.loadConfiguration(moneyfile);
	}
	
	public double getBalance(String p) {
		return moneycfg.getDouble("money." + p);
	}
	
	public void addBalance(String p, double amnt) {
		setBalance(p, getBalance(p) + amnt);
	}
	
	public boolean removeBalance(String p, double amnt) {
		if (getBalance(p) - amnt < 0) return false;
		
		setBalance(p, getBalance(p) - amnt);
		return true;
	}
	
	public void setBalance(String p, double amnt) {
		moneycfg.set("money." + p, amnt);
		save();
	}
	
	public ArrayList<String> getValues() {
		Map<String, Object> map = moneycfg.getValues(true);
		ArrayList<String> lines = new ArrayList<String>();
		
		for (Entry<String, Object> e : map.entrySet()) {
			lines.add(e.getValue() + " " + e.getKey());
		}
		
		return lines;
	}
	
	private void save() {
		try { 
			moneycfg.save(moneyfile); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Plugin getPlugin() {
		return p;
	}
}
