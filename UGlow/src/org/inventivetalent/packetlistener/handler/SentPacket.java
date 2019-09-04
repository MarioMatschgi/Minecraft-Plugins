/*    */ package org.inventivetalent.packetlistener.handler;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.inventivetalent.packetlistener.channel.ChannelWrapper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SentPacket
/*    */   extends PacketAbstract
/*    */ {
/*    */   public SentPacket(Object packet, Cancellable cancellable, Player player)
/*    */   {
/* 37 */     super(packet, cancellable, player);
/*    */   }
/*    */   
/*    */   public SentPacket(Object packet, Cancellable cancellable, ChannelWrapper channelWrapper) {
/* 41 */     super(packet, cancellable, channelWrapper);
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/handler/SentPacket.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */