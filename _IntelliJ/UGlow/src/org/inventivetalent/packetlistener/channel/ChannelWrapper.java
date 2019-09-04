/*    */ package org.inventivetalent.packetlistener.channel;
/*    */ 
/*    */ import java.net.SocketAddress;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChannelWrapper<T>
/*    */ {
/*    */   private T channel;
/*    */   
/*    */   public ChannelWrapper(T channel)
/*    */   {
/* 41 */     this.channel = channel;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public T channel()
/*    */   {
/* 48 */     return (T)this.channel;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public SocketAddress getRemoteAddress()
/*    */   {
/* 55 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public SocketAddress getLocalAddress()
/*    */   {
/* 62 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/channel/ChannelWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */