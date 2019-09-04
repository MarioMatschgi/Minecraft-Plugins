/*    */ package org.inventivetalent.reflection.resolver.wrapper;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class MethodWrapper<R>
/*    */   extends WrapperAbstract
/*    */ {
/*    */   private final Method method;
/*    */   
/*    */   public MethodWrapper(Method method)
/*    */   {
/* 38 */     this.method = method;
/*    */   }
/*    */   
/*    */   public boolean exists()
/*    */   {
/* 43 */     return this.method != null;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 47 */     return this.method.getName();
/*    */   }
/*    */   
/*    */   public R invoke(Object object, Object... args) {
/*    */     try {
/* 52 */       return (R)this.method.invoke(object, args);
/*    */     } catch (Exception e) {
/* 54 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public R invokeSilent(Object object, Object... args) {
/*    */     try {
/* 60 */       return (R)this.method.invoke(object, args);
/*    */     }
/*    */     catch (Exception localException) {}
/* 63 */     return null;
/*    */   }
/*    */   
/*    */   public Method getMethod() {
/* 67 */     return this.method;
/*    */   }
/*    */   
/*    */   public boolean equals(Object object)
/*    */   {
/* 72 */     if (this == object) return true;
/* 73 */     if ((object == null) || (getClass() != object.getClass())) { return false;
/*    */     }
/* 75 */     MethodWrapper<?> that = (MethodWrapper)object;
/*    */     
/* 77 */     return that.method == null ? true : this.method != null ? this.method.equals(that.method) : false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 83 */     return this.method != null ? this.method.hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/reflection/resolver/wrapper/MethodWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */