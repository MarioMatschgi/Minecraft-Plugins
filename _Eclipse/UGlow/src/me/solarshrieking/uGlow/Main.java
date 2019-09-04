/*     */ package me.solarshrieking.uGlow;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;

/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.inventivetalent.apihelper.APIManager;
/*     */ import org.inventivetalent.glow.GlowAPI;
/*     */ import org.inventivetalent.packetlistener.PacketListenerAPI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */   extends JavaPlugin
/*     */   implements Listener
/*     */ {
/*  26 */   private Main plugin = this;
/*     */   private static YamlConfiguration LANG;
/*     */   private static File LANG_FILE;
/*  29 */   private Logger log = getLogger();
/*     */   
/*     */   public void onLoad()
/*     */   {
/*  33 */     APIManager.require(PacketListenerAPI.class, this);
/*  34 */     APIManager.require(GlowAPI.class, this);
/*  35 */     this.plugin = this;
/*     */   }
/*     */   
/*     */   public void onEnable()
/*     */   {
/*  40 */     APIManager.initAPI(PacketListenerAPI.class);
/*  41 */     APIManager.initAPI(GlowAPI.class);
/*  42 */     getLogger().info("uGlow is now loading!");
/*  43 */     new PlayerListener(this);
/*  44 */     loadLang();
/*  45 */     getConfig().options().copyDefaults(true);
/*  46 */     saveConfig();
/*  47 */     String commandPrefix = getConfig().getString("settings.customAlias");
/*  48 */     getCommand(commandPrefix).setExecutor(new Commands(this.plugin));
/*     */   }
/*     */   
/*     */   public void onDisable()
/*     */   {
/*  53 */     getLogger().info(ChatColor.GREEN + "uGlow has been disabled!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void loadLang()
/*     */   {
/*  64 */     File lang = new File(getDataFolder(), "lang.yml");
/*  65 */     YamlConfiguration defConfig; if (!lang.exists()) {
/*     */       try {
/*  67 */         getDataFolder().mkdir();
/*  68 */         lang.createNewFile();
/*  69 */         InputStream defConfigStream = getResource("lang.yml");
/*  70 */         if (defConfigStream != null) {
/*  71 */           defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/*  72 */           defConfig.save(lang);
/*  73 */           Lang.setFile(defConfig);
/*  74 */           return;
/*     */         }
/*     */       } catch (IOException e) {
/*  77 */         e.printStackTrace();
/*  78 */         this.log.severe("[uGlow] Couldn't create language file.");
/*  79 */         this.log.severe("[uGlow] This is a fatal error. Now disabling");
/*  80 */         setEnabled(false);
/*     */       }
/*     */     }
/*  83 */     YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
/*  84 */     for (Lang item : Lang.values()) {
/*  85 */       if (conf.getString(item.getPath()) == null) {
/*  86 */         conf.set(item.getPath(), item.getDefault());
/*     */       }
/*     */     }
/*  89 */     Lang.setFile(conf);
/*  90 */     LANG = conf;
/*  91 */     LANG_FILE = lang;
/*     */     try {
/*  93 */       conf.save(getLangFile());
/*     */     } catch (IOException e) {
/*  95 */       this.log.log(Level.WARNING, "PluginName: Failed to save lang.yml.");
/*  96 */       this.log.log(Level.WARNING, "PluginName: Report this stack trace to <your name>.");
/*  97 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public YamlConfiguration getLang()
/*     */   {
/* 108 */     return LANG;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private File getLangFile()
/*     */   {
/* 117 */     return LANG_FILE;
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/me/solarshrieking/uGlow/Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */