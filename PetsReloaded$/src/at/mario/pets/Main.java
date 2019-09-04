package at.mario.pets;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.pets.commands.Pet;
import at.mario.pets.listener.DamageListener;
import at.mario.pets.listener.InventoryClick;
import at.mario.pets.listener.JoinListener;
import at.mario.pets.listener.MoveListener;
import at.mario.pets.listener.PlayerInteractListener;
import at.mario.pets.listener.Quitlistener;
import at.mario.pets.listener.SneakListener;
import at.mario.pets.manager.ConfigManagers.ConfigManager;
import at.mario.pets.manager.ConfigManagers.DataManager;
import at.mario.pets.manager.ConfigManagers.MessagesManager;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener {

	private MessagesManager mm;
	private ConfigManager cm;
	private DataManager dm;
	
	public static Economy eco = null;
	
	public static Main Plugin;
	
	public void onEnable() {
		Plugin = this;
		
		getCommand("pet").setExecutor(new Pet());

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InventoryClick(), this);
		pm.registerEvents(new Quitlistener(), this);
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new PlayerInteractListener(), this);
		pm.registerEvents(new MoveListener(), this);
		pm.registerEvents(new SneakListener(), this);
		
		if (setupEconomy()) {
			Bukkit.getConsoleSender().sendMessage("§aEco + Vault!");
		} else {
			Bukkit.getConsoleSender().sendMessage("§cEco + Vault! Error!");
		}
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §ePetsReloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: §e0.1");
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        loadConfigManager();
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	public void onDisable() {
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §ePetsReloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: §e0.1");
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §adeaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	private Boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		eco = rsp.getProvider();
		return (eco != null);
	}
	
	public void loadConfigManager() {
		cm = new ConfigManager();
		mm = new MessagesManager();
		dm = new DataManager();
		
		cm.setupConfig();
		cm.saveConfig();
		cm.reloadConfig();
		
		mm.setupMessages();
		mm.saveMessages();
		mm.reloadMessages();
		
		dm.setupData();
		dm.saveData();
		dm.reloadData();
	}
	
	public static Main getPlugin() {
		return Plugin;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
}
