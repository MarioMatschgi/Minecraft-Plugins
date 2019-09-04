/*     */ package org.inventivetalent.glow.reflection.resolver;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.inventivetalent.glow.reflection.resolver.wrapper.FieldWrapper;
/*     */ import org.inventivetalent.glow.reflection.util.AccessUtil;
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
/*     */ 
/*     */ public class FieldResolver
/*     */   extends MemberResolver<Field>
/*     */ {
/*     */   public FieldResolver(Class<?> clazz)
/*     */   {
/*  42 */     super(clazz);
/*     */   }
/*     */   
/*     */   public FieldResolver(String className) throws ClassNotFoundException {
/*  46 */     super(className);
/*     */   }
/*     */   
/*     */   public Field resolveIndex(int index) throws IndexOutOfBoundsException, ReflectiveOperationException
/*     */   {
/*  51 */     return AccessUtil.setAccessible(this.clazz.getDeclaredFields()[index]);
/*     */   }
/*     */   
/*     */   public Field resolveIndexSilent(int index)
/*     */   {
/*     */     try {
/*  57 */       return resolveIndex(index);
/*     */     }
/*     */     catch (IndexOutOfBoundsException|ReflectiveOperationException localIndexOutOfBoundsException) {}
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public FieldWrapper resolveIndexWrapper(int index)
/*     */   {
/*  65 */     return new FieldWrapper(resolveIndexSilent(index));
/*     */   }
/*     */   
/*     */   public FieldWrapper resolveWrapper(String... names) {
/*  69 */     return new FieldWrapper(resolveSilent(names));
/*     */   }
/*     */   
/*     */   public Field resolveSilent(String... names) {
/*     */     try {
/*  74 */       return resolve(names);
/*     */     }
/*     */     catch (Exception localException) {}
/*  77 */     return null;
/*     */   }
/*     */   
/*     */   public Field resolve(String... names) throws NoSuchFieldException {
/*  81 */     ResolverQuery.Builder builder = ResolverQuery.builder();
/*  82 */     for (String name : names)
/*  83 */       builder.with(name);
/*     */     try {
/*  85 */       return (Field)super.resolve(builder.build());
/*     */     } catch (ReflectiveOperationException e) {
/*  87 */       throw ((NoSuchFieldException)e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Field resolveSilent(ResolverQuery... queries) {
/*     */     try {
/*  93 */       return resolve(queries);
/*     */     }
/*     */     catch (Exception localException) {}
/*  96 */     return null;
/*     */   }
/*     */   
/*     */   public Field resolve(ResolverQuery... queries) throws NoSuchFieldException {
/*     */     try {
/* 101 */       return (Field)super.resolve(queries);
/*     */     } catch (ReflectiveOperationException e) {
/* 103 */       throw ((NoSuchFieldException)e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Field resolveObject(ResolverQuery query) throws ReflectiveOperationException
/*     */   {
/* 109 */     if ((query.getTypes() == null) || (query.getTypes().length == 0)) {
/* 110 */       return AccessUtil.setAccessible(this.clazz.getDeclaredField(query.getName()));
/*     */     }
/* 112 */     for (Field field : this.clazz.getDeclaredFields()) {
/* 113 */       if (field.getName().equals(query.getName())) {
/* 114 */         for (Class type : query.getTypes()) {
/* 115 */           if (field.getType().equals(type)) {
/* 116 */             return field;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 122 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Field resolveByFirstType(Class<?> type)
/*     */     throws ReflectiveOperationException
/*     */   {
/* 134 */     for (Field field : this.clazz.getDeclaredFields()) {
/* 135 */       if (field.getType().equals(type)) {
/* 136 */         return AccessUtil.setAccessible(field);
/*     */       }
/*     */     }
/* 139 */     throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + this.clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Field resolveByFirstTypeSilent(Class<?> type)
/*     */   {
/*     */     try
/*     */     {
/* 151 */       return resolveByFirstType(type);
/*     */     }
/*     */     catch (Exception localException) {}
/* 154 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Field resolveByLastType(Class<?> type)
/*     */     throws ReflectiveOperationException
/*     */   {
/* 166 */     Field field = null;
/* 167 */     for (Field field1 : this.clazz.getDeclaredFields()) {
/* 168 */       if (field1.getType().equals(type)) {
/* 169 */         field = field1;
/*     */       }
/*     */     }
/* 172 */     if (field == null) throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + this.clazz);
/* 173 */     return AccessUtil.setAccessible(field);
/*     */   }
/*     */   
/*     */   public Field resolveByLastTypeSilent(Class<?> type) {
/*     */     try {
/* 178 */       return resolveByLastType(type);
/*     */     }
/*     */     catch (Exception localException) {}
/* 181 */     return null;
/*     */   }
/*     */   
/*     */   protected NoSuchFieldException notFoundException(String joinedNames)
/*     */   {
/* 186 */     return new NoSuchFieldException("Could not resolve field for " + joinedNames + " in class " + this.clazz);
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/reflection/resolver/FieldResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */