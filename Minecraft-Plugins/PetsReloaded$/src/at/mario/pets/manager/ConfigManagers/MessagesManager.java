package at.mario.pets.manager.ConfigManagers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import at.mario.pets.Main;
import at.mario.pets.manager.CFGM;

public class MessagesManager {
	
	private Main plugin = Main.getPlugin(Main.class);
	private CFGM cfgm;
	
	public String[] MSGto = {"Comments.placeholders", "Messages.prefix", "Messages.balance.info.you", 
			"Messages.balance.info.other", "Messages.balance.enterANumber", "Messages.other.playerNotFound", "Messages.permission.noPermission"};
	
	public String[] MSGdata = {"# Use %player% fo Player, %money% for balance, %prefix% for the prefix", 
			"§f[§6Economy§f]", "%prefix% §aYour balance is §6%money% coins§a.", "%prefix% §b%player%§3's §abalance is now §6%money%",
			"%prefix% §cPlease enter a number", "%prefix% §cPlayer §b%player2% §ccould not be found!", "%prefix% §cYou dont't have the permission to do this!"};

	public boolean MessagesExists = false;
	
	public static FileConfiguration messagescfg;
	public static File messagesfile;
	
	public void setupMessages() {
		if (plugin.getDataFolder().exists() == false) {
			plugin.getDataFolder().mkdir();
		}
		messagesfile = new File(plugin.getDataFolder(), "messages.yml");
		if (!messagesfile.exists()) {
			try {
				messagesfile.createNewFile();
				
				messagescfg = YamlConfiguration.loadConfiguration(messagesfile);
				cfgm = new CFGM();
				
				for (int i = 0; i < MSGdata.length; i++) {
					cfgm.DefaultMessages(MSGto[i] + "", MSGdata[i] + "");
				}
				MessagesExists = true;
				Bukkit.getServer().getConsoleSender().sendMessage("§cMessage status: §aThe messages.yml file has been created");
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage("§cMessage status: Could not create the messages.yml file");
			}
		}
		messagescfg = YamlConfiguration.loadConfiguration(messagesfile);
	}
	
	public FileConfiguration getMessages() {
		return messagescfg;
	}
		
	public void saveMessages() {
		try {
			messagescfg.save(messagesfile);
			Bukkit.getServer().getConsoleSender().sendMessage("§cMessage status: §aThe messages.yml file has been saved");
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("§cMessage status: Could not save the messages.yml file");
		}
	}
		
	public void reloadMessages() {
		messagescfg = YamlConfiguration.loadConfiguration(messagesfile);
		Bukkit.getServer().getConsoleSender().sendMessage("§cMessage status: §aThe messages.yml file has been reload");
	}
}