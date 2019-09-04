/*    */ package org.inventivetalent.apihelper.exception;
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
/*    */ public class HostRegistrationException
/*    */   extends RuntimeException
/*    */ {
/*    */   public HostRegistrationException() {}
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
/*    */   public HostRegistrationException(String message)
/*    */   {
/* 36 */     super(message);
/*    */   }
/*    */   
/*    */   public HostRegistrationException(String message, Throwable cause) {
/* 40 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public HostRegistrationException(Throwable cause) {
/* 44 */     super(cause);
/*    */   }
/*    */   
/*    */   public HostRegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
/* 48 */     super(message, cause, enableSuppression, writableStackTrace);
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/apihelper/exception/HostRegistrationException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */