/*    */ package org.inventivetalent.packetlistener;
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
/*    */ public class Cancellable
/*    */   implements org.bukkit.event.Cancellable
/*    */ {
/*    */   private boolean cancelled;
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
/*    */   public boolean isCancelled()
/*    */   {
/* 37 */     return this.cancelled;
/*    */   }
/*    */   
/*    */   public void setCancelled(boolean b)
/*    */   {
/* 42 */     this.cancelled = b;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/Cancellable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */