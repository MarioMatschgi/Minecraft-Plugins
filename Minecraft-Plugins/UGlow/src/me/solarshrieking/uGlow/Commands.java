/*     */ package me.solarshrieking.uGlow;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Random;

/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ import org.inventivetalent.glow.GlowAPI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Commands
/*     */   implements CommandExecutor
/*     */ {
/*     */   private Main plugin;
/*     */   private int task;
/*  32 */   private List<Player> viewGlowPerms = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */   Commands(Main plugin)
/*     */   {
/*  38 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getUUID(Player player)
/*     */   {
/*  46 */     return player.getUniqueId().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private GlowAPI.Color randColor()
/*     */   {
/*  53 */     int pick = new Random().nextInt(GlowAPI.Color.values().length);
/*  54 */     return GlowAPI.Color.values()[pick];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void viewGlowPerms()
/*     */   {
/*  62 */     for (Player player : )
/*     */     {
/*  64 */       if (player.hasPermission("uglow.view")) {
/*  65 */         this.viewGlowPerms.add(player);
/*     */       }
/*  67 */       else if (this.viewGlowPerms.contains(player)) {
/*  68 */         this.viewGlowPerms.remove(player);
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
/*     */   public void Rainbow(final Player player, Long ticks)
/*     */   {
/*  83 */     this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
/*     */     {
/*     */       public void run() {
/*  86 */         GlowAPI.setGlowing(player, Commands.this.randColor(), Commands.this.viewGlowPerms);
/*     */         
/*  88 */         if (Commands.this.cancelTask(player).booleanValue()) {
/*  89 */           Bukkit.getScheduler().cancelTask(Commands.this.task);
/*  90 */           GlowAPI.setGlowing(player, null, Commands.this.viewGlowPerms); } } }, 0L, ticks
/*     */     
/*     */ 
/*  93 */       .longValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean glowStatus(Player player)
/*     */   {
/* 104 */     Boolean glowStatus = Boolean.valueOf(this.plugin.getConfig().getBoolean("Players." + getUUID(player) + ".glowing"));
/* 105 */     return glowStatus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String glowColor(Player player)
/*     */   {
/* 116 */     String glowColor = this.plugin.getConfig().getString("Players." + getUUID(player) + ".color");
/* 117 */     return glowColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean cancelTask(Player player)
/*     */   {
/* 128 */     Boolean toggle = Boolean.valueOf(!glowStatus(player).booleanValue());
/* 129 */     return toggle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setGlowStatus(Player player, Boolean glowBoolean)
/*     */   {
/* 139 */     this.plugin.getConfig().set("Players." + getUUID(player) + ".glowing", glowBoolean);
/* 140 */     this.plugin.saveConfig();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGlowing(final Player player, Boolean glowing)
/*     */   {
/* 152 */     viewGlowPerms();
/* 153 */     setGlowStatus(player, glowing);
/*     */     
/* 155 */     if (glowStatus(player).booleanValue()) {
/* 156 */       cancelTask(player);
/*     */       
/*     */ 
/* 159 */       if (glowColor(player).equalsIgnoreCase("Rainbow"))
/*     */       {
/* 161 */         final long ticks = this.plugin.getConfig().getLong("settings.rainbow-ticks");
/*     */         
/*     */ 
/* 164 */         Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
/*     */         {
/*     */ 
/* 167 */           public void run() { Commands.this.Rainbow(player, Long.valueOf(ticks)); } }, 20L);
/*     */ 
/*     */       }
/* 170 */       else if (glowColor(player).equalsIgnoreCase("Custom"))
/*     */       {
/* 172 */         final long ticks = this.plugin.getConfig().getLong("customPattern.ticks");
/*     */         
/* 174 */         Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
/*     */         {
/*     */ 
/* 177 */           public void run() { Commands.this.Custom(player, ticks); } }, 20L);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 182 */         Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
/*     */         {
/*     */ 
/* 185 */           public void run() { GlowAPI.setGlowing(player, GlowAPI.Color.valueOf(Commands.this.glowColor(player)), Commands.this.viewGlowPerms); } }, 20L);
/*     */       }
/*     */     }
/*     */     
/* 189 */     if (!glowStatus(player).booleanValue()) {
/* 190 */       cancelTask(player);
/* 191 */       GlowAPI.setGlowing(player, null, this.viewGlowPerms);
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
/*     */   public void setGlowColor(final Player player, String glowColor)
/*     */   {
/* 204 */     String glowColorStr = glowColor.toUpperCase();
/* 205 */     this.plugin.getConfig().set("Players." + getUUID(player) + ".color", glowColorStr);
/* 206 */     this.plugin.saveConfig();
/* 207 */     final Boolean reserve = glowStatus(player);
/* 208 */     setGlowing(player, Boolean.valueOf(false));
/* 209 */     cancelTask(player);
/*     */     
/* 211 */     Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
/*     */     {
/*     */ 
/* 214 */       public void run() { Commands.this.setGlowing(player, reserve); } }, 20L);
/*     */   }
/*     */   
/*     */ 
/*     */   private void lang(CommandSender sender, String lang)
/*     */   {
/* 220 */     sender.sendMessage(Lang.pluginPrefix.toString() + lang);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void Custom(final Player player, long ticks)
/*     */   {
/* 238 */     final ArrayList<String> customList = new ArrayList(this.plugin.getConfig().getStringList("customPattern.colors"));
/* 239 */     final ListIterator<String> listIter = customList.listIterator();
/*     */     
/* 241 */     this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
/*     */     {
/*     */       public void run() {
/* 244 */         String str = Commands.this.getCustomColor(customList, listIter).toUpperCase();
/*     */         
/* 246 */         GlowAPI.setGlowing(player, GlowAPI.Color.valueOf(str), Commands.this.viewGlowPerms);
/*     */         
/* 248 */         if (Commands.this.cancelTask(player).booleanValue()) {
/* 249 */           Bukkit.getScheduler().cancelTask(Commands.this.task);
/* 250 */           GlowAPI.setGlowing(player, null, Commands.this.viewGlowPerms); } } }, 0L, ticks);
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
/*     */ 
/*     */   private String getCustomColor(ArrayList<String> customList, ListIterator<String> Iter)
/*     */   {
/* 265 */     if (!Iter.hasNext()) {
/* 266 */       for (int i = 0; i < customList.size(); i += 1) {
/* 267 */         Iter.previous();
/*     */       }
/* 269 */       return (String)Iter.next();
/*     */     }
/* 271 */     return (String)Iter.next();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean onCommand(CommandSender sender, Command command, String alias, String[] args)
/*     */   {
/* 277 */     if ((sender instanceof Player))
/*     */     {
/* 279 */       PluginDescriptionFile pdf = this.plugin.getDescription();
/* 280 */       String pluginName = pdf.getName();
/* 281 */       String pluginVersion = pdf.getVersion();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 287 */       Player player = (Player)sender;
/*     */       
/* 289 */       String commandAlias = this.plugin.getConfig().getString("settings.customAlias");
/*     */       
/* 291 */       if (alias.equalsIgnoreCase(commandAlias))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 296 */         if (args.length == 0)
/*     */         {
/* 298 */           if (player.hasPermission("uglow.main")) {
/* 299 */             sender.sendMessage(Lang.mainHeader.toString().replace("%version", pluginVersion).replace("%plugin", pluginName));
/* 300 */             sender.sendMessage(Lang.mainOnOff.toString());
/* 301 */             sender.sendMessage(Lang.mainColor.toString());
/* 302 */             sender.sendMessage(Lang.mainSpecial.toString());
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 310 */         if (args.length == 1)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 318 */           if (args[0].equalsIgnoreCase("reload")) {
/* 319 */             if (sender.hasPermission("uglow.reload")) {
/* 320 */               this.plugin.saveDefaultConfig();
/* 321 */               this.plugin.reloadConfig();
/* 322 */               this.plugin.loadLang();
/* 323 */               sender.sendMessage(Lang.pluginPrefix.toString() + Lang.pluginReload.toString());
/*     */             } else {
/* 325 */               sender.sendMessage(Lang.pluginPrefix.toString() + Lang.noPerm.toString());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/* 337 */           else if (args[0].equalsIgnoreCase("on")) {
/* 338 */             if (sender.hasPermission("uglow.on")) {
/* 339 */               if (!glowStatus(player).booleanValue()) {
/* 340 */                 setGlowing(player, Boolean.valueOf(true));
/* 341 */                 lang(sender, Lang.playerGlowOn.toString());
/* 342 */               } else { lang(sender, Lang.playerGlowAlready.toString());
/* 343 */               } } else { lang(sender, Lang.noPerm.toString());
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/* 353 */           else if (args[0].equalsIgnoreCase("off")) {
/* 354 */             if (sender.hasPermission("uglow.off"))
/*     */             {
/* 356 */               if (glowStatus(player).booleanValue()) {
/* 357 */                 setGlowing(player, Boolean.valueOf(false));
/* 358 */                 cancelTask(player);
/* 359 */                 lang(sender, Lang.playerGlowOff.toString());
/* 360 */               } else { lang(sender, Lang.playerGlowNotGlowing.toString());
/* 361 */               } } else { lang(sender, Lang.noPerm.toString());
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/* 371 */           else if (args[0].matches("(?i)AQUA|BLACK|BLUE|DARK_AQUA|DARK_BLUE|DARK_GRAY|DARK_GREEN|DARK_PURPLE|DARK_RED|GOLD|GRAY|GREEN|NONE|PURPLE|RED|WHITE|YELLOW|RAINBOW|CUSTOM"))
/*     */           {
/*     */ 
/*     */ 
/* 375 */             String glowColorStr = args[0].toUpperCase();
/* 376 */             if (player.hasPermission("uglow.color." + glowColorStr)) {
/* 377 */               setGlowColor(player, glowColorStr);
/* 378 */               lang(sender, Lang.playerGlowColor.toString().replace("%color", glowColorStr));
/*     */             } else {
/* 380 */               lang(player, Lang.noPerm.toString());
/*     */             }
/* 382 */           } else { lang(player, Lang.invalidGlowColor.toString().replace("%colorlist", Lang.listGlowColor.toString()));
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 391 */         if (args.length == 2)
/*     */         {
/* 393 */           if (Bukkit.getPlayerExact(args[0]) != null)
/*     */           {
/* 395 */             Player target = Bukkit.getPlayerExact(args[0]);
/*     */             
/* 397 */             glowColor(target);
/* 398 */             glowStatus(target);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 404 */             if (args[1].equalsIgnoreCase("on")) {
/* 405 */               if (sender.hasPermission("uglow.on.others")) {
/* 406 */                 if (glowStatus(target).booleanValue()) {
/* 407 */                   lang(sender, Lang.otherGlowAlready.toString().replace("%p", target.getName()));
/*     */                 }
/* 409 */                 if (!glowStatus(target).booleanValue()) {
/* 410 */                   setGlowing(target, Boolean.valueOf(true));
/* 411 */                   lang(sender, Lang.otherGlowOn.toString().replace("%p", target.getName()));
/*     */                 }
/* 413 */               } else { sender.sendMessage(Lang.pluginPrefix.toString() + Lang.noPerm.toString());
/*     */               }
/* 415 */             } else if (args[1].equalsIgnoreCase("off")) {
/* 416 */               if (sender.hasPermission("uglow.off.others")) {
/* 417 */                 if (glowStatus(target).booleanValue()) {
/* 418 */                   setGlowing(target, Boolean.valueOf(false));
/* 419 */                   lang(sender, Lang.otherGlowOff.toString().replace("%p", target.getName()));
/*     */                 }
/* 421 */                 if (!glowStatus(target).booleanValue())
/* 422 */                   lang(sender, Lang.otherGlowNotGlowing.toString().replace("%p", target.getName()));
/*     */               } else {
/* 424 */                 lang(sender, Lang.noPerm.toString());
/*     */               }
/* 426 */             } else if (args[1].matches("(?i)AQUA|BLACK|BLUE|DARK_AQUA|DARK_BLUE|DARK_GRAY|DARK_GREEN|DARK_PURPLE|DARK_RED|GOLD|GRAY|GREEN|NONE|PURPLE|RED|WHITE|YELLOW|RAINBOW|CUSTOM"))
/*     */             {
/*     */ 
/* 429 */               if (sender.hasPermission("uglow.color.others"))
/* 430 */                 setGlowColor(target, args[1]); else
/* 431 */                 lang(sender, Lang.noPerm.toString());
/*     */             } else {
/* 433 */               lang(sender, Lang.invalidGlowColor.toString().replace("%colorlist", Lang.listGlowColor.toString()));
/*     */             }
/*     */           }
/*     */         }
/* 437 */         if (args.length > 2) {
/* 438 */           sender.sendMessage(ChatColor.RED + "Error: Too many arguments!");
/*     */         }
/* 440 */         return true;
/*     */       }
/*     */     } else {
/* 443 */       sender.sendMessage(Color.RED + "You must be a player to enter this command!");
/* 444 */       return false;
/*     */     }
/* 446 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/me/solarshrieking/uGlow/Commands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */