package at.mario.pets.manager;

import java.io.IOException;

import org.bukkit.Bukkit;

import at.mario.pets.manager.ConfigManagers.ConfigManager;
import at.mario.pets.manager.ConfigManagers.DataManager;
import at.mario.pets.manager.ConfigManagers.MessagesManager;

public class CFGM {
	
	private ConfigManager cm = new ConfigManager();
	private MessagesManager mm = new MessagesManager();
	private DataManager dm = new DataManager();

	public void setup() {
		cm.setupConfig();
		mm.setupMessages();
		dm.setupData();
	}
	
	public void reload() {
		cm.reloadConfig();
		mm.reloadMessages();
		dm.reloadData();
	}
	
	public void DefaultMessages(String to, String data)  {
		MessagesManager.messagescfg.set(to, data);
		try {
			MessagesManager.messagescfg.save(MessagesManager.messagesfile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("§cMessage status: §cCould not save Messages!");
		}
	}
	
	public void DefaultConfig(String to, String data)  {
		ConfigManager.configcfg.set(to, data);
		try {
			ConfigManager.configcfg.save(ConfigManager.configfile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("§cMessage status: §cCould not save Messages!");
		}
	}
	
	public void DefaultData(String to, String data)  {
		DataManager.datacfg.set(to, data);
		try {
			DataManager.datacfg.save(DataManager.datafile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("§cData status: §cCould not save Data!");
		}
	}
}