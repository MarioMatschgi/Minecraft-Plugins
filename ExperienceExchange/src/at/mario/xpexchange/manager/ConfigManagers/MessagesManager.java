package at.mario.xpexchange.manager.ConfigManagers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import at.mario.xpexchange.Main;

public class MessagesManager {
	
	public FileConfiguration getMessages() {
		return Main.getInstance().getMessageConfig();
	}
	
	public void reloadMessages() {
		Main.getInstance().messagesEnglish = YamlConfiguration.loadConfiguration(Main.getInstance().MessagesEnglishfile);
		Main.getInstance().messagesGerman = YamlConfiguration.loadConfiguration(Main.getInstance().MessagesGermanfile);
	}
	
	public void sendReplacedMessage(String message, Player p) {
		p.sendMessage(message.replace("%prefix%", getMessages().getString("Messages.prefix").replace("%player%", p.getName())));
	}
	
	public void sendMessage(String path, Player p) {
		p.sendMessage(getMessages().getString(path).replace("%prefix%", getMessages().getString("Messages.prefix").replace("%player%", p.getName())));
	}
}