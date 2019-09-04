/*     */ package org.inventivetalent.packetlistener;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Cancellable;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.inventivetalent.apihelper.API;
/*     */ import org.inventivetalent.apihelper.APIManager;
/*     */ import org.inventivetalent.packetlistener.channel.ChannelWrapper;
/*     */ import org.inventivetalent.packetlistener.handler.PacketHandler;
/*     */ import org.inventivetalent.packetlistener.handler.ReceivedPacket;
/*     */ import org.inventivetalent.packetlistener.handler.SentPacket;
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
/*     */ public class PacketListenerAPI
/*     */   implements IPacketListener, Listener, API
/*     */ {
/*     */   private ChannelInjector channelInjector;
/*  51 */   protected boolean injected = false;
/*     */   
/*  53 */   Logger logger = Logger.getLogger("PacketListenerAPI");
/*     */   
/*     */ 
/*     */   public void load()
/*     */   {
/*  58 */     this.channelInjector = new ChannelInjector();
/*  59 */     if ((this.injected = this.channelInjector.inject(this))) {
/*  60 */       this.channelInjector.addServerChannel();
/*  61 */       this.logger.info("Injected custom channel handlers.");
/*     */     } else {
/*  63 */       this.logger.severe("Failed to inject channel handlers");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init(Plugin plugin)
/*     */   {
/*  72 */     APIManager.registerEvents(this, this);
/*     */     
/*  74 */     this.logger.info("Adding channels for online players...");
/*  75 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  76 */       this.channelInjector.addChannel(player);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void disable(Plugin plugin)
/*     */   {
/*  83 */     if (!this.injected) {
/*  84 */       return;
/*     */     }
/*  86 */     this.logger.info("Removing channels for online players...");
/*  87 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  88 */       this.channelInjector.removeChannel(player);
/*     */     }
/*     */     
/*  91 */     this.logger.info("Removing packet handlers (" + PacketHandler.getHandlers().size() + ")...");
/*  92 */     while (!PacketHandler.getHandlers().isEmpty()) {
/*  93 */       PacketHandler.removeHandler((PacketHandler)PacketHandler.getHandlers().get(0));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean addPacketHandler(PacketHandler handler)
/*     */   {
/* 103 */     return PacketHandler.addHandler(handler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean removePacketHandler(PacketHandler handler)
/*     */   {
/* 112 */     return PacketHandler.removeHandler(handler);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onJoin(PlayerJoinEvent e) {
/* 117 */     this.channelInjector.addChannel(e.getPlayer());
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onQuit(PlayerQuitEvent e) {
/* 122 */     this.channelInjector.removeChannel(e.getPlayer());
/*     */   }
/*     */   
/*     */   public Object onPacketReceive(Object sender, Object packet, Cancellable cancellable) {
/*     */     ReceivedPacket receivedPacket;
/*     */     ReceivedPacket receivedPacket;
/* 128 */     if ((sender instanceof Player)) {
/* 129 */       receivedPacket = new ReceivedPacket(packet, cancellable, (Player)sender);
/*     */     } else {
/* 131 */       receivedPacket = new ReceivedPacket(packet, cancellable, (ChannelWrapper)sender);
/*     */     }
/* 133 */     PacketHandler.notifyHandlers(receivedPacket);
/* 134 */     if (receivedPacket.getPacket() != null) return receivedPacket.getPacket();
/* 135 */     return packet;
/*     */   }
/*     */   
/*     */   public Object onPacketSend(Object receiver, Object packet, Cancellable cancellable) {
/*     */     SentPacket sentPacket;
/*     */     SentPacket sentPacket;
/* 141 */     if ((receiver instanceof Player)) {
/* 142 */       sentPacket = new SentPacket(packet, cancellable, (Player)receiver);
/*     */     } else {
/* 144 */       sentPacket = new SentPacket(packet, cancellable, (ChannelWrapper)receiver);
/*     */     }
/* 146 */     PacketHandler.notifyHandlers(sentPacket);
/* 147 */     if (sentPacket.getPacket() != null) return sentPacket.getPacket();
/* 148 */     return packet;
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/PacketListenerAPI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */