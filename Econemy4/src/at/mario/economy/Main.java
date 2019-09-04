package at.mario.economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.economy.Manager.MoneyManager;
import at.mario.economy.Manager.VaultConector;
import at.mario.economy.Manager.ConfigManagers.ConfigManager;
import at.mario.economy.Manager.ConfigManagers.DataManager;
import at.mario.economy.Manager.ConfigManagers.MessagesManager;
import at.mario.economy.commands.Money;
import at.mario.economy.listener.JoinListener;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener {
	
	private MessagesManager mm;
	private ConfigManager cm;
	private DataManager dm;
	
	public static Main Plugin;
	
	public void onEnable() {
		Plugin = this;

		getCommand("money").setExecutor(new Money());
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new JoinListener(), this);
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eEconemy");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: §e0.7");
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        loadConfigManager();
        
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
        		Bukkit.getServer().getServicesManager().register(Economy.class, new VaultConector(), this, ServicePriority.Highest);
        }
        
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eEconemy");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: §e0.7");
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §cdeaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
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
		
		MoneyManager.getInstance().setup(Plugin);
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
