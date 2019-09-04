/*    */ package org.inventivetalent.glow.reflection.resolver.wrapper;
/*    */ 
/*    */ import java.lang.reflect.Field;
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
/*    */ public class FieldWrapper<R>
/*    */   extends WrapperAbstract
/*    */ {
/*    */   private final Field field;
/*    */   
/*    */   public FieldWrapper(Field field)
/*    */   {
/* 38 */     this.field = field;
/*    */   }
/*    */   
/*    */   public boolean exists()
/*    */   {
/* 43 */     return this.field != null;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 47 */     return this.field.getName();
/*    */   }
/*    */   
/*    */   public R get(Object object) {
/*    */     try {
/* 52 */       return (R)this.field.get(object);
/*    */     } catch (Exception e) {
/* 54 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public R getSilent(Object object) {
/*    */     try {
/* 60 */       return (R)this.field.get(object);
/*    */     }
/*    */     catch (Exception localException) {}
/* 63 */     return null;
/*    */   }
/*    */   
/*    */   public void set(Object object, R value) {
/*    */     try {
/* 68 */       this.field.set(object, value);
/*    */     } catch (Exception e) {
/* 70 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public void setSilent(Object object, R value) {
/*    */     try {
/* 76 */       this.field.set(object, value);
/*    */     }
/*    */     catch (Exception localException) {}
/*    */   }
/*    */   
/*    */   public Field getField() {
/* 82 */     return this.field;
/*    */   }
/*    */   
/*    */   public boolean equals(Object object)
/*    */   {
/* 87 */     if (this == object) return true;
/* 88 */     if ((object == null) || (getClass() != object.getClass())) { return false;
/*    */     }
/* 90 */     FieldWrapper<?> that = (FieldWrapper)object;
/*    */     
/* 92 */     if (this.field != null ? !this.field.equals(that.field) : that.field != null) { return false;
/*    */     }
/* 94 */     return true;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 99 */     return this.field != null ? this.field.hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/reflection/resolver/wrapper/FieldWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */