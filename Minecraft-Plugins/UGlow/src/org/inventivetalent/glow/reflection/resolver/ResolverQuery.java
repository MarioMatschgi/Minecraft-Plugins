/*     */ package org.inventivetalent.glow.reflection.resolver;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public class ResolverQuery
/*     */ {
/*     */   private String name;
/*     */   private Class<?>[] types;
/*     */   
/*     */   public ResolverQuery(String name, Class<?>... types)
/*     */   {
/*  46 */     this.name = name;
/*  47 */     this.types = types;
/*     */   }
/*     */   
/*     */   public ResolverQuery(String name) {
/*  51 */     this.name = name;
/*  52 */     this.types = new Class[0];
/*     */   }
/*     */   
/*     */   public ResolverQuery(Class<?>... types) {
/*  56 */     this.types = types;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  60 */     return this.name;
/*     */   }
/*     */   
/*     */   public Class<?>[] getTypes() {
/*  64 */     return this.types;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  69 */     if (this == o) return true;
/*  70 */     if ((o == null) || (getClass() != o.getClass())) { return false;
/*     */     }
/*  72 */     ResolverQuery that = (ResolverQuery)o;
/*     */     
/*  74 */     if (this.name != null ? !this.name.equals(that.name) : that.name != null) { return false;
/*     */     }
/*  76 */     return Arrays.equals(this.types, that.types);
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  82 */     int result = this.name != null ? this.name.hashCode() : 0;
/*  83 */     result = 31 * result + (this.types != null ? Arrays.hashCode(this.types) : 0);
/*  84 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  91 */     return "ResolverQuery{name='" + this.name + '\'' + ", types=" + Arrays.toString(this.types) + '}';
/*     */   }
/*     */   
/*     */   public static Builder builder()
/*     */   {
/*  96 */     return new Builder(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Builder
/*     */   {
/* 104 */     private List<ResolverQuery> queryList = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */     public Builder with(String name, Class<?>[] types)
/*     */     {
/* 110 */       this.queryList.add(new ResolverQuery(name, types));
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     public Builder with(String name) {
/* 115 */       this.queryList.add(new ResolverQuery(name));
/* 116 */       return this;
/*     */     }
/*     */     
/*     */     public Builder with(Class<?>[] types) {
/* 120 */       this.queryList.add(new ResolverQuery(types));
/* 121 */       return this;
/*     */     }
/*     */     
/*     */     public ResolverQuery[] build() {
/* 125 */       return (ResolverQuery[])this.queryList.toArray(new ResolverQuery[this.queryList.size()]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/reflection/resolver/ResolverQuery.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */