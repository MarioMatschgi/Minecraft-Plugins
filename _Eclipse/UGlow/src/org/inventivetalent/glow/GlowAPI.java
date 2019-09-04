/*     */ package org.inventivetalent.glow;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.inventivetalent.apihelper.APIManager;
/*     */ import org.inventivetalent.glow.reflection.minecraft.DataWatcher.V1_9.ValueType;
/*     */ import org.inventivetalent.glow.reflection.minecraft.Minecraft;
/*     */ import org.inventivetalent.glow.reflection.resolver.ConstructorResolver;
/*     */ import org.inventivetalent.glow.reflection.resolver.FieldResolver;
/*     */ import org.inventivetalent.glow.reflection.resolver.MethodResolver;
/*     */ import org.inventivetalent.glow.reflection.resolver.ResolverQuery;
/*     */ import org.inventivetalent.glow.reflection.resolver.minecraft.NMSClassResolver;
/*     */ import org.inventivetalent.glow.reflection.resolver.minecraft.OBCClassResolver;
/*     */ import org.inventivetalent.packetlistener.PacketListenerAPI;
/*     */ import org.inventivetalent.packetlistener.handler.PacketHandler;
/*     */ import org.inventivetalent.packetlistener.handler.ReceivedPacket;
/*     */ import org.inventivetalent.packetlistener.handler.SentPacket;
/*     */ 
/*     */ public class GlowAPI implements org.inventivetalent.apihelper.API, org.bukkit.event.Listener
/*     */ {
/*  33 */   private static Map<UUID, GlowData> dataMap = new java.util.HashMap();
/*     */   
/*  35 */   private static final NMSClassResolver NMS_CLASS_RESOLVER = new NMSClassResolver();
/*     */   
/*     */   private static Class<?> PacketPlayOutEntityMetadata;
/*     */   
/*     */   static Class<?> DataWatcher;
/*     */   
/*     */   static Class<?> DataWatcherItem;
/*     */   
/*     */   private static Class<?> Entity;
/*     */   
/*     */   private static FieldResolver PacketPlayOutMetadataFieldResolver;
/*     */   
/*     */   private static FieldResolver EntityFieldResolver;
/*     */   
/*     */   private static FieldResolver DataWatcherFieldResolver;
/*     */   
/*     */   static FieldResolver DataWatcherItemFieldResolver;
/*     */   
/*     */   private static ConstructorResolver DataWatcherItemConstructorResolver;
/*     */   
/*     */   private static MethodResolver DataWatcherMethodResolver;
/*     */   
/*     */   static MethodResolver DataWatcherItemMethodResolver;
/*     */   
/*     */   private static MethodResolver EntityMethodResolver;
/*     */   
/*     */   private static Class<?> PacketPlayOutScoreboardTeam;
/*     */   
/*     */   private static FieldResolver PacketScoreboardTeamFieldResolver;
/*     */   
/*     */   private static FieldResolver EntityPlayerFieldResolver;
/*     */   private static MethodResolver PlayerConnectionMethodResolver;
/*  67 */   public static String TEAM_TAG_VISIBILITY = "always";
/*     */   
/*     */ 
/*     */ 
/*  71 */   public static String TEAM_PUSH = "always";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setGlowing(Entity entity, Color color, String tagVisibility, String push, Player receiver)
/*     */   {
/*  83 */     if (receiver == null) { return;
/*     */     }
/*  85 */     boolean glowing = color != null;
/*  86 */     if (entity == null) glowing = false;
/*  87 */     if (((entity instanceof OfflinePlayer)) && (!((OfflinePlayer)entity).isOnline())) { glowing = false;
/*     */     }
/*  89 */     boolean wasGlowing = dataMap.containsKey(entity != null ? entity.getUniqueId() : null);
/*     */     GlowData glowData;
/*  91 */     GlowData glowData; if ((wasGlowing) && (entity != null)) glowData = (GlowData)dataMap.get(entity.getUniqueId()); else { glowData = new GlowData();
/*     */     }
/*  93 */     Color oldColor = wasGlowing ? (Color)glowData.colorMap.get(receiver.getUniqueId()) : null;
/*     */     
/*  95 */     if (glowing) {
/*  96 */       glowData.colorMap.put(receiver.getUniqueId(), color);
/*     */     } else {
/*  98 */       glowData.colorMap.remove(receiver.getUniqueId());
/*     */     }
/* 100 */     if (glowData.colorMap.isEmpty()) {
/* 101 */       dataMap.remove(entity != null ? entity.getUniqueId() : null);
/*     */     }
/* 103 */     else if (entity != null) {
/* 104 */       dataMap.put(entity.getUniqueId(), glowData);
/*     */     }
/*     */     
/*     */ 
/* 108 */     if ((color != null) && (oldColor == color)) return;
/* 109 */     if (entity == null) return;
/* 110 */     if (((entity instanceof OfflinePlayer)) && (!((OfflinePlayer)entity).isOnline())) return;
/* 111 */     if (!receiver.isOnline()) { return;
/*     */     }
/* 113 */     sendGlowPacket(entity, wasGlowing, glowing, receiver);
/* 114 */     if ((oldColor != null) && (oldColor != Color.NONE)) {
/* 115 */       sendTeamPacket(entity, oldColor, false, false, tagVisibility, push, receiver);
/*     */     }
/* 117 */     if (glowing) {
/* 118 */       sendTeamPacket(entity, color, false, color != Color.NONE, tagVisibility, push, receiver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setGlowing(Entity entity, Color color, Player receiver)
/*     */   {
/* 130 */     setGlowing(entity, color, "always", "always", receiver);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setGlowing(Entity entity, boolean glowing, Player receiver)
/*     */   {
/* 142 */     setGlowing(entity, glowing ? Color.NONE : null, receiver);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setGlowing(Entity entity, boolean glowing, Collection<? extends Player> receivers)
/*     */   {
/* 154 */     for (Player receiver : receivers) {
/* 155 */       setGlowing(entity, glowing, receiver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setGlowing(Entity entity, Color color, Collection<? extends Player> receivers)
/*     */   {
/* 167 */     for (Player receiver : receivers) {
/* 168 */       setGlowing(entity, color, receiver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setGlowing(Collection<? extends Entity> entities, Color color, Player receiver)
/*     */   {
/* 180 */     for (Entity entity : entities) {
/* 181 */       setGlowing(entity, color, receiver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setGlowing(Collection<? extends Entity> entities, Color color, Collection<? extends Player> receivers)
/*     */   {
/* 193 */     for (Entity entity : entities) {
/* 194 */       setGlowing(entity, color, receivers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isGlowing(Entity entity, Player receiver)
/*     */   {
/* 206 */     return getGlowColor(entity, receiver) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isGlowing(Entity entity, Collection<? extends Player> receivers, boolean checkAll)
/*     */   {
/*     */     boolean glowing;
/*     */     
/*     */ 
/*     */ 
/* 218 */     if (checkAll) {
/* 219 */       glowing = true;
/* 220 */       for (Player receiver : receivers) {
/* 221 */         if (!isGlowing(entity, receiver)) {
/* 222 */           glowing = false;
/*     */         }
/*     */       }
/* 225 */       return glowing;
/*     */     }
/* 227 */     for (Player receiver : receivers) {
/* 228 */       if (isGlowing(entity, receiver)) { return true;
/*     */       }
/*     */     }
/* 231 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Color getGlowColor(Entity entity, Player receiver)
/*     */   {
/* 242 */     if (!dataMap.containsKey(entity.getUniqueId())) return null;
/* 243 */     GlowData data = (GlowData)dataMap.get(entity.getUniqueId());
/* 244 */     return (Color)data.colorMap.get(receiver.getUniqueId());
/*     */   }
/*     */   
/*     */   protected static void sendGlowPacket(Entity entity, boolean wasGlowing, boolean glowing, Player receiver) {
/*     */     try {
/* 249 */       if (PacketPlayOutEntityMetadata == null) {
/* 250 */         PacketPlayOutEntityMetadata = NMS_CLASS_RESOLVER.resolve(new String[] { "PacketPlayOutEntityMetadata" });
/*     */       }
/* 252 */       if (DataWatcher == null) {
/* 253 */         DataWatcher = NMS_CLASS_RESOLVER.resolve(new String[] { "DataWatcher" });
/*     */       }
/* 255 */       if (DataWatcherItem == null) {
/* 256 */         DataWatcherItem = NMS_CLASS_RESOLVER.resolve(new String[] { "DataWatcher$Item" });
/*     */       }
/* 258 */       if (Entity == null) {
/* 259 */         Entity = NMS_CLASS_RESOLVER.resolve(new String[] { "Entity" });
/*     */       }
/* 261 */       if (PacketPlayOutMetadataFieldResolver == null) {
/* 262 */         PacketPlayOutMetadataFieldResolver = new FieldResolver(PacketPlayOutEntityMetadata);
/*     */       }
/* 264 */       if (DataWatcherItemConstructorResolver == null) {
/* 265 */         DataWatcherItemConstructorResolver = new ConstructorResolver(DataWatcherItem);
/*     */       }
/* 267 */       if (EntityFieldResolver == null) {
/* 268 */         EntityFieldResolver = new FieldResolver(Entity);
/*     */       }
/* 270 */       if (DataWatcherMethodResolver == null) {
/* 271 */         DataWatcherMethodResolver = new MethodResolver(DataWatcher);
/*     */       }
/* 273 */       if (DataWatcherItemMethodResolver == null) {
/* 274 */         DataWatcherItemMethodResolver = new MethodResolver(DataWatcherItem);
/*     */       }
/* 276 */       if (EntityMethodResolver == null) {
/* 277 */         EntityMethodResolver = new MethodResolver(Entity);
/*     */       }
/* 279 */       if (DataWatcherFieldResolver == null) {
/* 280 */         DataWatcherFieldResolver = new FieldResolver(DataWatcher);
/*     */       }
/*     */       
/* 283 */       List list = new java.util.ArrayList();
/*     */       
/*     */ 
/* 286 */       Object dataWatcher = EntityMethodResolver.resolve(new String[] { "getDataWatcher" }).invoke(Minecraft.getHandle(entity), new Object[0]);
/* 287 */       Map<Integer, Object> dataWatcherItems = (Map)DataWatcherFieldResolver.resolve(new String[] { "c" }).get(dataWatcher);
/*     */       
/*     */ 
/* 290 */       Object dataWatcherObject = DataWatcher.V1_9.ValueType.ENTITY_FLAG.getType();
/* 291 */       byte prev = ((Byte)(dataWatcherItems.isEmpty() ? Integer.valueOf(0) : DataWatcherItemMethodResolver.resolve(new String[] { "b" }).invoke(dataWatcherItems.get(Integer.valueOf(0)), new Object[0]))).byteValue();
/* 292 */       byte b = (byte)(glowing ? prev | 0x40 : prev & 0xFFFFFFBF);
/* 293 */       Object dataWatcherItem = DataWatcherItemConstructorResolver.resolveFirstConstructor().newInstance(new Object[] { dataWatcherObject, Byte.valueOf(b) });
/*     */       
/*     */ 
/* 296 */       list.add(dataWatcherItem);
/*     */       
/* 298 */       Object packetMetadata = PacketPlayOutEntityMetadata.newInstance();
/* 299 */       PacketPlayOutMetadataFieldResolver.resolve(new String[] { "a" }).set(packetMetadata, Integer.valueOf(-entity.getEntityId()));
/* 300 */       PacketPlayOutMetadataFieldResolver.resolve(new String[] { "b" }).set(packetMetadata, list);
/*     */       
/* 302 */       sendPacket(packetMetadata, receiver);
/*     */     } catch (ReflectiveOperationException e) {
/* 304 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void initTeam(Player receiver, String tagVisibility, String push)
/*     */   {
/* 316 */     for (Color color : ) {
/* 317 */       sendTeamPacket(null, color, true, false, tagVisibility, push, receiver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void initTeam(Player receiver)
/*     */   {
/* 327 */     initTeam(receiver, TEAM_TAG_VISIBILITY, TEAM_PUSH);
/*     */   }
/*     */   
/*     */   protected static void sendTeamPacket(Entity entity, Color color, boolean createNewTeam, boolean addEntity, String tagVisibility, String push, Player receiver) {
/*     */     try {
/* 332 */       if (PacketPlayOutScoreboardTeam == null) {
/* 333 */         PacketPlayOutScoreboardTeam = NMS_CLASS_RESOLVER.resolve(new String[] { "PacketPlayOutScoreboardTeam" });
/*     */       }
/* 335 */       if (PacketScoreboardTeamFieldResolver == null) {
/* 336 */         PacketScoreboardTeamFieldResolver = new FieldResolver(PacketPlayOutScoreboardTeam);
/*     */       }
/*     */       
/* 339 */       Object packetScoreboardTeam = PacketPlayOutScoreboardTeam.newInstance();
/* 340 */       PacketScoreboardTeamFieldResolver.resolve(new String[] { "i" }).set(packetScoreboardTeam, Integer.valueOf(addEntity ? 3 : createNewTeam ? 0 : 4));
/* 341 */       PacketScoreboardTeamFieldResolver.resolve(new String[] { "a" }).set(packetScoreboardTeam, color.getTeamName());
/* 342 */       PacketScoreboardTeamFieldResolver.resolve(new String[] { "e" }).set(packetScoreboardTeam, tagVisibility);
/* 343 */       PacketScoreboardTeamFieldResolver.resolve(new String[] { "f" }).set(packetScoreboardTeam, push);
/*     */       
/* 345 */       if (createNewTeam) {
/* 346 */         PacketScoreboardTeamFieldResolver.resolve(new String[] { "g" }).set(packetScoreboardTeam, Integer.valueOf(color.packetValue));
/* 347 */         PacketScoreboardTeamFieldResolver.resolve(new String[] { "c" }).set(packetScoreboardTeam, "§" + color.colorCode);
/*     */         
/* 349 */         PacketScoreboardTeamFieldResolver.resolve(new String[] { "b" }).set(packetScoreboardTeam, color.getTeamName());
/* 350 */         PacketScoreboardTeamFieldResolver.resolve(new String[] { "d" }).set(packetScoreboardTeam, "");
/* 351 */         PacketScoreboardTeamFieldResolver.resolve(new String[] { "j" }).set(packetScoreboardTeam, Integer.valueOf(0));
/*     */       }
/*     */       
/* 354 */       if (!createNewTeam)
/*     */       {
/* 356 */         Collection<String> collection = (Collection)PacketScoreboardTeamFieldResolver.resolve(new String[] { "h" }).get(packetScoreboardTeam);
/* 357 */         if ((entity instanceof OfflinePlayer)) {
/* 358 */           collection.add(entity.getName());
/*     */         } else {
/* 360 */           collection.add(entity.getUniqueId().toString());
/*     */         }
/*     */       }
/*     */       
/* 364 */       sendPacket(packetScoreboardTeam, receiver);
/*     */     } catch (ReflectiveOperationException e) {
/* 366 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static void sendPacket(Object packet, Player p) throws IllegalArgumentException, IllegalAccessException, java.lang.reflect.InvocationTargetException, ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
/* 371 */     if (EntityPlayerFieldResolver == null) {
/* 372 */       EntityPlayerFieldResolver = new FieldResolver(NMS_CLASS_RESOLVER.resolve(new String[] { "EntityPlayer" }));
/*     */     }
/* 374 */     if (PlayerConnectionMethodResolver == null) {
/* 375 */       PlayerConnectionMethodResolver = new MethodResolver(NMS_CLASS_RESOLVER.resolve(new String[] { "PlayerConnection" }));
/*     */     }
/*     */     try
/*     */     {
/* 379 */       Object handle = Minecraft.getHandle(p);
/* 380 */       Object connection = EntityPlayerFieldResolver.resolve(new String[] { "playerConnection" }).get(handle);
/* 381 */       PlayerConnectionMethodResolver.resolve(new String[] { "sendPacket" }).invoke(connection, new Object[] { packet });
/*     */     } catch (ReflectiveOperationException e) {
/* 383 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum Color
/*     */   {
/* 392 */     BLACK(0, "0"), 
/* 393 */     DARK_BLUE(1, "1"), 
/* 394 */     DARK_GREEN(2, "2"), 
/* 395 */     DARK_AQUA(3, "3"), 
/* 396 */     DARK_RED(4, "4"), 
/* 397 */     DARK_PURPLE(5, "5"), 
/* 398 */     GOLD(6, "6"), 
/* 399 */     GRAY(7, "7"), 
/* 400 */     DARK_GRAY(8, "8"), 
/* 401 */     BLUE(9, "9"), 
/* 402 */     GREEN(10, "a"), 
/* 403 */     AQUA(11, "b"), 
/* 404 */     RED(12, "c"), 
/* 405 */     PURPLE(13, "d"), 
/* 406 */     YELLOW(14, "e"), 
/* 407 */     WHITE(15, "f"), 
/* 408 */     NONE(-1, "");
/*     */     
/*     */     int packetValue;
/*     */     String colorCode;
/*     */     
/*     */     private Color(int packetValue, String colorCode) {
/* 414 */       this.packetValue = packetValue;
/* 415 */       this.colorCode = colorCode;
/*     */     }
/*     */     
/*     */     String getTeamName() {
/* 419 */       String name = String.format("GAPI#%s", new Object[] { name() });
/* 420 */       if (name.length() > 16) {
/* 421 */         name = name.substring(0, 16);
/*     */       }
/* 423 */       return name;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void load()
/*     */   {
/* 431 */     APIManager.require(PacketListenerAPI.class, GlowPlugin.instance);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void init(Plugin plugin)
/*     */   {
/* 438 */     APIManager.initAPI(PacketListenerAPI.class);
/*     */     
/*     */ 
/* 441 */     APIManager.registerEvents(this, this);
/*     */     
/* 443 */     PacketHandler.addHandler(new PacketHandler(GlowPlugin.instance != null ? GlowPlugin.instance : plugin)
/*     */     {
/*     */       @org.inventivetalent.packetlistener.handler.PacketOptions(forcePlayer=true)
/*     */       public void onSend(SentPacket sentPacket) {
/* 447 */         if ("PacketPlayOutEntityMetadata".equals(sentPacket.getPacketName())) {
/* 448 */           int a = ((Integer)sentPacket.getPacketValue("a")).intValue();
/* 449 */           if (a < 0)
/*     */           {
/* 451 */             sentPacket.setPacketValue("a", Integer.valueOf(-a));
/* 452 */             return;
/*     */           }
/*     */           
/* 455 */           List b = (List)sentPacket.getPacketValue("b");
/* 456 */           if ((b == null) || (b.isEmpty())) {
/* 457 */             return;
/*     */           }
/*     */           
/* 460 */           Entity entity = GlowAPI.getEntityById(sentPacket.getPlayer().getWorld(), a);
/* 461 */           if (entity != null)
/*     */           {
/* 463 */             if (GlowAPI.isGlowing(entity, sentPacket.getPlayer())) {
/* 464 */               if (GlowAPI.DataWatcherItemMethodResolver == null) {
/* 465 */                 GlowAPI.DataWatcherItemMethodResolver = new MethodResolver(GlowAPI.DataWatcherItem);
/*     */               }
/* 467 */               if (GlowAPI.DataWatcherItemFieldResolver == null) {
/* 468 */                 GlowAPI.DataWatcherItemFieldResolver = new FieldResolver(GlowAPI.DataWatcherItem);
/*     */               }
/*     */               
/*     */               try
/*     */               {
/* 473 */                 Object prevItem = b.get(0);
/* 474 */                 Object prevObj = GlowAPI.DataWatcherItemMethodResolver.resolve(new String[] { "b" }).invoke(prevItem, new Object[0]);
/* 475 */                 if ((prevObj instanceof Byte)) {
/* 476 */                   byte prev = ((Byte)prevObj).byteValue();
/* 477 */                   byte bte = (byte)(prev | 0x40);
/* 478 */                   GlowAPI.DataWatcherItemFieldResolver.resolve(new String[] { "b" }).set(prevItem, Byte.valueOf(bte));
/*     */                 }
/*     */               } catch (Exception e) {
/* 481 */                 throw new RuntimeException(e);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       public void onReceive(ReceivedPacket receivedPacket) {}
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   public void disable(Plugin plugin) {}
/*     */   
/*     */ 
/*     */   @EventHandler
/*     */   public void onJoin(PlayerJoinEvent event)
/*     */   {
/* 501 */     initTeam(event.getPlayer());
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onQuit(PlayerQuitEvent event) {
/* 506 */     for (Player receiver : ) {
/* 507 */       if (isGlowing(event.getPlayer(), receiver)) {
/* 508 */         setGlowing(event.getPlayer(), null, receiver);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 513 */   protected static NMSClassResolver nmsClassResolver = new NMSClassResolver();
/* 514 */   protected static OBCClassResolver obcClassResolver = new OBCClassResolver();
/*     */   private static FieldResolver CraftWorldFieldResolver;
/*     */   private static FieldResolver WorldFieldResolver;
/*     */   private static MethodResolver IntHashMapMethodResolver;
/*     */   
/*     */   public static Entity getEntityById(World world, int entityId)
/*     */   {
/*     */     try {
/* 522 */       if (CraftWorldFieldResolver == null) {
/* 523 */         CraftWorldFieldResolver = new FieldResolver(obcClassResolver.resolve(new String[] { "CraftWorld" }));
/*     */       }
/* 525 */       if (WorldFieldResolver == null) {
/* 526 */         WorldFieldResolver = new FieldResolver(nmsClassResolver.resolve(new String[] { "World" }));
/*     */       }
/* 528 */       if (IntHashMapMethodResolver == null) {
/* 529 */         IntHashMapMethodResolver = new MethodResolver(nmsClassResolver.resolve(new String[] { "IntHashMap" }));
/*     */       }
/* 531 */       if (EntityMethodResolver == null) {
/* 532 */         EntityMethodResolver = new MethodResolver(nmsClassResolver.resolve(new String[] { "Entity" }));
/*     */       }
/*     */       
/* 535 */       Object entitiesById = WorldFieldResolver.resolve(new String[] { "entitiesById" }).get(CraftWorldFieldResolver.resolve(new String[] { "world" }).get(world));
/* 536 */       Object entity = IntHashMapMethodResolver.resolve(new ResolverQuery[] { new ResolverQuery("get", new Class[] { Integer.TYPE }) }).invoke(entitiesById, new Object[] { Integer.valueOf(entityId) });
/* 537 */       if (entity == null) return null;
/* 538 */       return (Entity)EntityMethodResolver.resolve(new String[] { "getBukkitEntity" }).invoke(entity, new Object[0]);
/*     */     } catch (Exception e) {
/* 540 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/GlowAPI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */