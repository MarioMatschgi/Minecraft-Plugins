package at.mario.luckyploit;

import java.io.File;

import org.lwjgl.opengl.Display;

import at.mario.luckyploit.modules.ModuleManager;
import at.mario.luckyploit.utils.Logger;

public class Luckyploit {
	
	public static Luckyploit instance;
	
	public static final String CLIENT_NAME = "Lucky-Ploit";
	public static final String CLIENT_VERSION = "1.0";
	public static final String MINECRAFT_VERSION = "1.8.8";
	
	public static String prefix = "§f[§bLucky§3Ploit§f] ";

	public File directory;
	
	private static Logger logger;
	private static ModuleManager moduleManager;
	
	public Luckyploit() {
		instance = this;
		logger = new Logger();
		moduleManager = new ModuleManager();
	}
	
	public static void startClient() {
		Display.setTitle(CLIENT_NAME + " v" + CLIENT_VERSION + " Mc: " + MINECRAFT_VERSION);
	}
	
	
	public static Logger getLogger() {
		return logger;
	}
	public static ModuleManager getModuleManager() {
		return moduleManager;
	}
	
	public static Luckyploit getInstance() {
		return instance;
	}
}
