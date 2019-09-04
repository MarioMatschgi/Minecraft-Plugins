/*    */ package org.inventivetalent.glow.reflection.resolver.wrapper;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
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
/*    */ public class ConstructorWrapper<R>
/*    */   extends WrapperAbstract
/*    */ {
/*    */   private final Constructor<R> constructor;
/*    */   
/*    */   public ConstructorWrapper(Constructor<R> constructor)
/*    */   {
/* 38 */     this.constructor = constructor;
/*    */   }
/*    */   
/*    */   public boolean exists()
/*    */   {
/* 43 */     return this.constructor != null;
/*    */   }
/*    */   
/*    */   public R newInstance(Object... args) {
/*    */     try {
/* 48 */       return (R)this.constructor.newInstance(args);
/*    */     } catch (Exception e) {
/* 50 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public R newInstanceSilent(Object... args) {
/*    */     try {
/* 56 */       return (R)this.constructor.newInstance(args);
/*    */     }
/*    */     catch (Exception localException) {}
/* 59 */     return null;
/*    */   }
/*    */   
/*    */   public Class<?>[] getParameterTypes() {
/* 63 */     return this.constructor.getParameterTypes();
/*    */   }
/*    */   
/*    */   public Constructor<R> getConstructor() {
/* 67 */     return this.constructor;
/*    */   }
/*    */   
/*    */   public boolean equals(Object object)
/*    */   {
/* 72 */     if (this == object) return true;
/* 73 */     if ((object == null) || (getClass() != object.getClass())) { return false;
/*    */     }
/* 75 */     ConstructorWrapper<?> that = (ConstructorWrapper)object;
/*    */     
/* 77 */     return that.constructor == null ? true : this.constructor != null ? this.constructor.equals(that.constructor) : false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 83 */     return this.constructor != null ? this.constructor.hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/reflection/resolver/wrapper/ConstructorWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */