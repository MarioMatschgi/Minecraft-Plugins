/*     */ package org.inventivetalent.packetlistener.channel;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.inventivetalent.packetlistener.Cancellable;
/*     */ import org.inventivetalent.packetlistener.IPacketListener;
/*     */ import org.inventivetalent.reflection.resolver.FieldResolver;
/*     */ import org.inventivetalent.reflection.resolver.MethodResolver;
/*     */ import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
/*     */ import org.inventivetalent.reflection.util.AccessUtil;
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
/*     */ public abstract class ChannelAbstract
/*     */ {
/*  49 */   protected static final NMSClassResolver nmsClassResolver = new NMSClassResolver();
/*     */   
/*  51 */   static final Class<?> EntityPlayer = nmsClassResolver.resolveSilent(new String[] { "EntityPlayer" });
/*  52 */   static final Class<?> PlayerConnection = nmsClassResolver.resolveSilent(new String[] { "PlayerConnection" });
/*  53 */   static final Class<?> NetworkManager = nmsClassResolver.resolveSilent(new String[] { "NetworkManager" });
/*  54 */   static final Class<?> Packet = nmsClassResolver.resolveSilent(new String[] { "Packet" });
/*  55 */   static final Class<?> ServerConnection = nmsClassResolver.resolveSilent(new String[] { "ServerConnection" });
/*  56 */   static final Class<?> MinecraftServer = nmsClassResolver.resolveSilent(new String[] { "MinecraftServer" });
/*     */   
/*  58 */   protected static final FieldResolver entityPlayerFieldResolver = new FieldResolver(EntityPlayer);
/*  59 */   protected static final FieldResolver playerConnectionFieldResolver = new FieldResolver(PlayerConnection);
/*  60 */   protected static final FieldResolver networkManagerFieldResolver = new FieldResolver(NetworkManager);
/*  61 */   protected static final FieldResolver minecraftServerFieldResolver = new FieldResolver(MinecraftServer);
/*  62 */   protected static final FieldResolver serverConnectionFieldResolver = new FieldResolver(ServerConnection);
/*     */   
/*  64 */   static final Field networkManager = playerConnectionFieldResolver.resolveSilent(new String[] { "networkManager" });
/*  65 */   static final Field playerConnection = entityPlayerFieldResolver.resolveSilent(new String[] { "playerConnection" });
/*  66 */   static final Field serverConnection = minecraftServerFieldResolver.resolveByFirstTypeSilent(ServerConnection);
/*  67 */   static final Field connectionList = serverConnectionFieldResolver.resolveByLastTypeSilent(List.class);
/*     */   
/*  69 */   protected static final MethodResolver craftServerFieldResolver = new MethodResolver(Bukkit.getServer().getClass());
/*     */   
/*  71 */   static final Method getServer = craftServerFieldResolver.resolveSilent(new String[] { "getServer" });
/*     */   
/*  73 */   final Executor addChannelExecutor = Executors.newSingleThreadExecutor();
/*  74 */   final Executor removeChannelExecutor = Executors.newSingleThreadExecutor();
/*     */   
/*     */   static final String KEY_HANDLER = "packet_handler";
/*     */   static final String KEY_PLAYER = "packet_listener_player";
/*     */   static final String KEY_SERVER = "packet_listener_server";
/*     */   private IPacketListener iPacketListener;
/*     */   
/*     */   public ChannelAbstract(IPacketListener iPacketListener)
/*     */   {
/*  83 */     this.iPacketListener = iPacketListener;
/*     */   }
/*     */   
/*     */   public abstract void addChannel(Player paramPlayer);
/*     */   
/*     */   public abstract void removeChannel(Player paramPlayer);
/*     */   
/*     */   public void addServerChannel() {
/*     */     try {
/*  92 */       Object dedicatedServer = getServer.invoke(Bukkit.getServer(), new Object[0]);
/*  93 */       Object serverConnection = serverConnection.get(dedicatedServer);
/*  94 */       List currentList = (List)connectionList.get(serverConnection);
/*  95 */       Field superListField = AccessUtil.setAccessible(currentList.getClass().getSuperclass().getDeclaredField("list"));
/*  96 */       Object list = superListField.get(currentList);
/*  97 */       if (IListenerList.class.isAssignableFrom(list.getClass())) return;
/*  98 */       List newList = Collections.synchronizedList(newListenerList());
/*  99 */       for (Object o : currentList) {
/* 100 */         newList.add(o);
/*     */       }
/* 102 */       connectionList.set(serverConnection, newList);
/*     */     } catch (Exception e) {
/* 104 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract IListenerList newListenerList();
/*     */   
/*     */   protected final Object onPacketSend(Object receiver, Object packet, Cancellable cancellable) {
/* 111 */     return this.iPacketListener.onPacketSend(receiver, packet, cancellable);
/*     */   }
/*     */   
/*     */   protected final Object onPacketReceive(Object sender, Object packet, Cancellable cancellable) {
/* 115 */     return this.iPacketListener.onPacketReceive(sender, packet, cancellable);
/*     */   }
/*     */   
/*     */   static abstract interface IChannelWrapper {}
/*     */   
/*     */   static abstract interface IChannelHandler {}
/*     */   
/*     */   static abstract interface IListenerList<E>
/*     */     extends List<E>
/*     */   {}
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/channel/ChannelAbstract.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */