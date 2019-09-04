/*    */ package org.inventivetalent.glow.reflection.resolver.wrapper;
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
/*    */ public class ClassWrapper<R>
/*    */   extends WrapperAbstract
/*    */ {
/*    */   private final Class<R> clazz;
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
/*    */   public ClassWrapper(Class<R> clazz)
/*    */   {
/* 36 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public boolean exists()
/*    */   {
/* 41 */     return this.clazz != null;
/*    */   }
/*    */   
/*    */   public Class<R> getClazz() {
/* 45 */     return this.clazz;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 49 */     return this.clazz.getName();
/*    */   }
/*    */   
/*    */   public R newInstance() {
/*    */     try {
/* 54 */       return (R)this.clazz.newInstance();
/*    */     } catch (Exception e) {
/* 56 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public R newInstanceSilent() {
/*    */     try {
/* 62 */       return (R)this.clazz.newInstance();
/*    */     }
/*    */     catch (Exception localException) {}
/* 65 */     return null;
/*    */   }
/*    */   
/*    */   public boolean equals(Object object)
/*    */   {
/* 70 */     if (this == object) return true;
/* 71 */     if ((object == null) || (getClass() != object.getClass())) { return false;
/*    */     }
/* 73 */     ClassWrapper<?> that = (ClassWrapper)object;
/*    */     
/* 75 */     return that.clazz == null ? true : this.clazz != null ? this.clazz.equals(that.clazz) : false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 81 */     return this.clazz != null ? this.clazz.hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/reflection/resolver/wrapper/ClassWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */