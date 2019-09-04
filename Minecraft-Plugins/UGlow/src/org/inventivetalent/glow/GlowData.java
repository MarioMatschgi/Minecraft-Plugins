/*    */ package org.inventivetalent.glow;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ public class GlowData
/*    */ {
/* 10 */   public Map<UUID, GlowAPI.Color> colorMap = new HashMap();
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 14 */     if (this == o) return true;
/* 15 */     if ((o == null) || (getClass() != o.getClass())) { return false;
/*    */     }
/* 17 */     GlowData glowData = (GlowData)o;
/*    */     
/* 19 */     return glowData.colorMap == null ? true : this.colorMap != null ? this.colorMap.equals(glowData.colorMap) : false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 25 */     return this.colorMap != null ? this.colorMap.hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/GlowData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */