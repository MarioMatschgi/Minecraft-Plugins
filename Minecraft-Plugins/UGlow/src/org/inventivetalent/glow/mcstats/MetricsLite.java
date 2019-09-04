/*     */ package org.inventivetalent.glow.mcstats;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Collection;
/*     */ import java.util.UUID;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfigurationOptions;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetricsLite
/*     */ {
/*     */   private static final int REVISION = 7;
/*     */   private static final String BASE_URL = "http://report.mcstats.org";
/*     */   private static final String REPORT_URL = "/plugin/%s";
/*     */   private static final int PING_INTERVAL = 15;
/*     */   private final Plugin plugin;
/*     */   private final YamlConfiguration configuration;
/*     */   private final File configurationFile;
/*     */   private final String guid;
/*     */   private final boolean debug;
/* 107 */   private final Object optOutLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 112 */   private volatile BukkitTask task = null;
/*     */   
/*     */   public MetricsLite(Plugin plugin) throws IOException {
/* 115 */     if (plugin == null) {
/* 116 */       throw new IllegalArgumentException("Plugin cannot be null");
/*     */     }
/*     */     
/* 119 */     this.plugin = plugin;
/*     */     
/*     */ 
/* 122 */     this.configurationFile = getConfigFile();
/* 123 */     this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile);
/*     */     
/*     */ 
/* 126 */     this.configuration.addDefault("opt-out", Boolean.valueOf(false));
/* 127 */     this.configuration.addDefault("guid", UUID.randomUUID().toString());
/* 128 */     this.configuration.addDefault("debug", Boolean.valueOf(false));
/*     */     
/*     */ 
/* 131 */     if (this.configuration.get("guid", null) == null) {
/* 132 */       this.configuration.options().header("http://mcstats.org").copyDefaults(true);
/* 133 */       this.configuration.save(this.configurationFile);
/*     */     }
/*     */     
/*     */ 
/* 137 */     this.guid = this.configuration.getString("guid");
/* 138 */     this.debug = this.configuration.getBoolean("debug", false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean start()
/*     */   {
/* 149 */     synchronized (this.optOutLock)
/*     */     {
/* 151 */       if (isOptOut()) {
/* 152 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 156 */       if (this.task != null) {
/* 157 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 161 */       this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, new Runnable()
/*     */       {
/* 163 */         private boolean firstPost = true;
/*     */         
/*     */         public void run()
/*     */         {
/*     */           try {
/* 168 */             synchronized (MetricsLite.this.optOutLock)
/*     */             {
/* 170 */               if ((MetricsLite.this.isOptOut()) && (MetricsLite.this.task != null)) {
/* 171 */                 MetricsLite.this.task.cancel();
/* 172 */                 MetricsLite.this.task = null;
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 179 */             MetricsLite.this.postPlugin(!this.firstPost);
/*     */             
/*     */ 
/*     */ 
/* 183 */             this.firstPost = false;
/*     */           } catch (IOException e) {
/* 185 */             if (MetricsLite.this.debug)
/* 186 */               Bukkit.getLogger().log(Level.INFO, "[Metrics] " + e.getMessage()); } } }, 0L, 18000L);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOptOut()
/*     */   {
/* 202 */     synchronized (this.optOutLock)
/*     */     {
/*     */       try {
/* 205 */         this.configuration.load(getConfigFile());
/*     */       } catch (IOException ex) {
/* 207 */         if (this.debug) {
/* 208 */           Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
/*     */         }
/* 210 */         return true;
/*     */       } catch (InvalidConfigurationException ex) {
/* 212 */         if (this.debug) {
/* 213 */           Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
/*     */         }
/* 215 */         return true;
/*     */       }
/* 217 */       return this.configuration.getBoolean("opt-out", false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enable()
/*     */     throws IOException
/*     */   {
/* 228 */     synchronized (this.optOutLock)
/*     */     {
/* 230 */       if (isOptOut()) {
/* 231 */         this.configuration.set("opt-out", Boolean.valueOf(false));
/* 232 */         this.configuration.save(this.configurationFile);
/*     */       }
/*     */       
/*     */ 
/* 236 */       if (this.task == null) {
/* 237 */         start();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disable()
/*     */     throws IOException
/*     */   {
/* 249 */     synchronized (this.optOutLock)
/*     */     {
/* 251 */       if (!isOptOut()) {
/* 252 */         this.configuration.set("opt-out", Boolean.valueOf(true));
/* 253 */         this.configuration.save(this.configurationFile);
/*     */       }
/*     */       
/*     */ 
/* 257 */       if (this.task != null) {
/* 258 */         this.task.cancel();
/* 259 */         this.task = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getConfigFile()
/*     */   {
/* 275 */     File pluginsFolder = this.plugin.getDataFolder().getParentFile();
/*     */     
/*     */ 
/* 278 */     return new File(new File(pluginsFolder, "PluginMetrics"), "config.yml");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getOnlinePlayers()
/*     */   {
/*     */     try
/*     */     {
/* 288 */       Method onlinePlayerMethod = Server.class.getMethod("getOnlinePlayers", new Class[0]);
/* 289 */       if (onlinePlayerMethod.getReturnType().equals(Collection.class)) {
/* 290 */         return ((Collection)onlinePlayerMethod.invoke(Bukkit.getServer(), new Object[0])).size();
/*     */       }
/* 292 */       return ((Player[])onlinePlayerMethod.invoke(Bukkit.getServer(), new Object[0])).length;
/*     */     }
/*     */     catch (Exception ex) {
/* 295 */       if (this.debug) {
/* 296 */         Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
/*     */       }
/*     */     }
/*     */     
/* 300 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void postPlugin(boolean isPing)
/*     */     throws IOException
/*     */   {
/* 308 */     PluginDescriptionFile description = this.plugin.getDescription();
/* 309 */     String pluginName = description.getName();
/* 310 */     boolean onlineMode = Bukkit.getServer().getOnlineMode();
/* 311 */     String pluginVersion = description.getVersion();
/* 312 */     String serverVersion = Bukkit.getVersion();
/* 313 */     int playersOnline = getOnlinePlayers();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 318 */     StringBuilder json = new StringBuilder(1024);
/* 319 */     json.append('{');
/*     */     
/*     */ 
/* 322 */     appendJSONPair(json, "guid", this.guid);
/* 323 */     appendJSONPair(json, "plugin_version", pluginVersion);
/* 324 */     appendJSONPair(json, "server_version", serverVersion);
/* 325 */     appendJSONPair(json, "players_online", Integer.toString(playersOnline));
/*     */     
/*     */ 
/* 328 */     String osname = System.getProperty("os.name");
/* 329 */     String osarch = System.getProperty("os.arch");
/* 330 */     String osversion = System.getProperty("os.version");
/* 331 */     String java_version = System.getProperty("java.version");
/* 332 */     int coreCount = Runtime.getRuntime().availableProcessors();
/*     */     
/*     */ 
/* 335 */     if (osarch.equals("amd64")) {
/* 336 */       osarch = "x86_64";
/*     */     }
/*     */     
/* 339 */     appendJSONPair(json, "osname", osname);
/* 340 */     appendJSONPair(json, "osarch", osarch);
/* 341 */     appendJSONPair(json, "osversion", osversion);
/* 342 */     appendJSONPair(json, "cores", Integer.toString(coreCount));
/* 343 */     appendJSONPair(json, "auth_mode", onlineMode ? "1" : "0");
/* 344 */     appendJSONPair(json, "java_version", java_version);
/*     */     
/*     */ 
/* 347 */     if (isPing) {
/* 348 */       appendJSONPair(json, "ping", "1");
/*     */     }
/*     */     
/*     */ 
/* 352 */     json.append('}');
/*     */     
/*     */ 
/* 355 */     URL url = new URL("http://report.mcstats.org" + String.format("/plugin/%s", new Object[] { urlEncode(pluginName) }));
/*     */     
/*     */ 
/*     */     URLConnection connection;
/*     */     
/*     */     URLConnection connection;
/*     */     
/* 362 */     if (isMineshafterPresent()) {
/* 363 */       connection = url.openConnection(Proxy.NO_PROXY);
/*     */     } else {
/* 365 */       connection = url.openConnection();
/*     */     }
/*     */     
/*     */ 
/* 369 */     byte[] uncompressed = json.toString().getBytes();
/* 370 */     byte[] compressed = gzip(json.toString());
/*     */     
/*     */ 
/* 373 */     connection.addRequestProperty("User-Agent", "MCStats/7");
/* 374 */     connection.addRequestProperty("Content-Type", "application/json");
/* 375 */     connection.addRequestProperty("Content-Encoding", "gzip");
/* 376 */     connection.addRequestProperty("Content-Length", Integer.toString(compressed.length));
/* 377 */     connection.addRequestProperty("Accept", "application/json");
/* 378 */     connection.addRequestProperty("Connection", "close");
/*     */     
/* 380 */     connection.setDoOutput(true);
/*     */     
/* 382 */     if (this.debug) {
/* 383 */       System.out.println("[Metrics] Prepared request for " + pluginName + " uncompressed=" + uncompressed.length + " compressed=" + compressed.length);
/*     */     }
/*     */     
/*     */ 
/* 387 */     OutputStream os = connection.getOutputStream();
/* 388 */     os.write(compressed);
/* 389 */     os.flush();
/*     */     
/*     */ 
/* 392 */     BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/* 393 */     String response = reader.readLine();
/*     */     
/*     */ 
/* 396 */     os.close();
/* 397 */     reader.close();
/*     */     
/* 399 */     if ((response == null) || (response.startsWith("ERR")) || (response.startsWith("7"))) {
/* 400 */       if (response == null) {
/* 401 */         response = "null";
/* 402 */       } else if (response.startsWith("7")) {
/* 403 */         response = response.substring(response.startsWith("7,") ? 2 : 1);
/*     */       }
/*     */       
/* 406 */       throw new IOException(response);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] gzip(String input)
/*     */   {
/* 417 */     baos = new ByteArrayOutputStream();
/* 418 */     GZIPOutputStream gzos = null;
/*     */     try
/*     */     {
/* 421 */       gzos = new GZIPOutputStream(baos);
/* 422 */       gzos.write(input.getBytes("UTF-8"));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 432 */       return baos.toByteArray();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 424 */       e.printStackTrace();
/*     */     } finally {
/* 426 */       if (gzos != null) {
/* 427 */         try { gzos.close();
/*     */         }
/*     */         catch (IOException localIOException3) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isMineshafterPresent()
/*     */   {
/*     */     try
/*     */     {
/* 442 */       Class.forName("mineshafter.MineServer");
/* 443 */       return true;
/*     */     } catch (Exception e) {}
/* 445 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void appendJSONPair(StringBuilder json, String key, String value)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 458 */     boolean isValueNumeric = false;
/*     */     try
/*     */     {
/* 461 */       if ((value.equals("0")) || (!value.endsWith("0"))) {
/* 462 */         Double.parseDouble(value);
/* 463 */         isValueNumeric = true;
/*     */       }
/*     */     } catch (NumberFormatException e) {
/* 466 */       isValueNumeric = false;
/*     */     }
/*     */     
/* 469 */     if (json.charAt(json.length() - 1) != '{') {
/* 470 */       json.append(',');
/*     */     }
/*     */     
/* 473 */     json.append(escapeJSON(key));
/* 474 */     json.append(':');
/*     */     
/* 476 */     if (isValueNumeric) {
/* 477 */       json.append(value);
/*     */     } else {
/* 479 */       json.append(escapeJSON(value));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String escapeJSON(String text)
/*     */   {
/* 490 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 492 */     builder.append('"');
/* 493 */     for (int index = 0; index < text.length(); index++) {
/* 494 */       char chr = text.charAt(index);
/*     */       
/* 496 */       switch (chr) {
/*     */       case '"': 
/*     */       case '\\': 
/* 499 */         builder.append('\\');
/* 500 */         builder.append(chr);
/* 501 */         break;
/*     */       case '\b': 
/* 503 */         builder.append("\\b");
/* 504 */         break;
/*     */       case '\t': 
/* 506 */         builder.append("\\t");
/* 507 */         break;
/*     */       case '\n': 
/* 509 */         builder.append("\\n");
/* 510 */         break;
/*     */       case '\r': 
/* 512 */         builder.append("\\r");
/* 513 */         break;
/*     */       default: 
/* 515 */         if (chr < ' ') {
/* 516 */           String t = "000" + Integer.toHexString(chr);
/* 517 */           builder.append("\\u" + t.substring(t.length() - 4));
/*     */         } else {
/* 519 */           builder.append(chr);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 524 */     builder.append('"');
/*     */     
/* 526 */     return builder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String urlEncode(String text)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 536 */     return URLEncoder.encode(text, "UTF-8");
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/mcstats/MetricsLite.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */