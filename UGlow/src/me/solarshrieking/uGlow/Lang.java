/*    */ package me.solarshrieking.uGlow;
/*    */ 
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */  enum Lang
/*    */ {
/* 12 */   pluginPrefix("pluginPrefix", "&7[&auGlow&7]&b "), 
/* 13 */   pluginReload("pluginReload", "&aPlugin has been reloaded!"), 
/* 14 */   playerGlowOn("playerGlowOn", "&aYour glow has been toggled ON"), 
/* 15 */   playerGlowOff("playerGlowOff", "&cYour glow has been toggled OFF"), 
/* 16 */   playerGlowOnJoin("playerGlowPerm", "&aYour glowing status has been restored!"), 
/* 17 */   playerGlowAlready("playerGlowAlready", "&cYou are already glowing!"), 
/* 18 */   playerGlowNotGlowing("playerGlowNotGlowing", "&cYou aren't glowing!"), 
/* 19 */   playerGlowColor("playerGlowColor", "&aYou have set your glow color to %color!"), 
/*    */   
/* 21 */   invalidGlowColor("invalidGlowColor", "Invalid Color! Please use one of the following: %colorlist"), 
/* 22 */   listGlowColor("listGlowColor", "Aqua, Black, Blue, Dark_Aqua, Dark_Blue, Dark_Gray, Dark_Green, Dark_Purple, Dark_Red, Gold, Gray, Green, Red, White, Yellow, Rainbow, Custom"), 
/*    */   
/* 24 */   otherGlowOn("otherGlowOn", "&aYou have toggled %p's glow ON!"), 
/* 25 */   otherGlowOff("otherGlowOff", "&cYou have toggled %p's glow OFF"), 
/* 26 */   otherGlowAlready("otherGlowAlready", "&c%p is already glowing!"), 
/* 27 */   otherGlowNotGlowing("otherGlowNotGlowing", "&c%p isn't glowing!"), 
/* 28 */   otherGlowColor("otherGlowColor", "&aYou have set %p's glow color to %color!"), 
/* 29 */   otherGlowOffline("otherGlowOffline", "&c%p is either offline or does not exist!"), 
/*    */   
/* 31 */   rainbowInvalidTicks("rainbowInvalidTicks", "&cTick count must be above 1L!"), 
/*    */   
/* 33 */   noPerm("noPermOn", "&cYou do not have permission to use this command!"), 
/*    */   
/* 35 */   mainHeader("mainHeader", "&a&m&l+-------- &e%plugin v%version&a&m&l --------+"), 
/* 36 */   mainOnOff("mainOnOff", "&b/uGlow [on|off]:     Toggle your glow status!"), 
/* 37 */   mainColor("mainColor", "&b/uGlow [color]:      Change your glow color!"), 
/* 38 */   mainSpecial("mainSpecial", "&b/uGlow [rainbow]:   Change your special glows!"), 
/*    */   
/*    */ 
/* 41 */   entityNull("entityNull", "&cPlease look at an entity while running this command!");
/*    */   
/*    */ 
/*    */   private String path;
/*    */   
/*    */   private String def;
/*    */   
/*    */   private static YamlConfiguration LANG;
/*    */   
/*    */ 
/*    */   private Lang(String path, String start)
/*    */   {
/* 53 */     this.path = path;
/* 54 */     this.def = start;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void setFile(YamlConfiguration config)
/*    */   {
/* 62 */     LANG = config;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 67 */     if (this == pluginPrefix)
/* 68 */       return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def)) + " ";
/* 69 */     return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDefault()
/*    */   {
/* 77 */     return this.def;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getPath()
/*    */   {
/* 85 */     return this.path;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/me/solarshrieking/uGlow/Lang.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */