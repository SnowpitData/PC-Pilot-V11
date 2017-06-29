/*     */ package phil.awt;
/*     */ 
/*     */ public class UTMTextField extends PosIntTextField
/*     */ {
/*     */   private static final boolean __DEBUG__ = false;
/*  11 */   public int MAX_VALUE__ = 99999999;
/*     */ 
/*     */   public UTMTextField(String num_text) throws NumberFormatException
/*     */   {
/*  15 */     super("", 8);
/*  16 */     if (num_text.trim().length() <= 0)
/*     */       return;
/*  18 */     super.setText(num_text);
/*     */   }
/*     */ 
/*     */   public void setValue(Number value)
/*     */     throws NumberFormatException
/*     */   {
/*  44 */     setValue_(value, this.MAX_VALUE__);
/*     */   }
/*     */ 
/*     */   public void setText(String t)
/*     */   {
/*  70 */     super.setText(t);
/*     */   }
/*     */ 
/*     */   Integer valueOf(String num_text)
/*     */     throws NumberFormatException
/*     */   {
/*  95 */     return valueOf__(num_text);
/*     */   }
/*     */ 
/*     */   private Integer valueOf__(String num_text)
/*     */     throws NumberFormatException
/*     */   {
/* 125 */     int val = -1;
/*     */     try
/*     */     {
/* 129 */       val = Integer.parseInt(num_text);
/*     */     }
/*     */     catch (NumberFormatException e)
/*     */     {
/*     */       try
/*     */       {
/* 135 */         val = Long.valueOf(num_text).intValue();
/*     */       }
/*     */       catch (NumberFormatException f)
/*     */       {
/*     */         try
/*     */         {
/* 141 */           val = Float.valueOf(num_text).intValue();
/*     */         }
/*     */         catch (NumberFormatException g)
/*     */         {
/* 145 */           val = Double.valueOf(num_text).intValue();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 150 */     if ((val < 0) || (val > this.MAX_VALUE__)) throw new NumberFormatException("DegreeTextField.valueOf__( " + num_text + " ): out of range");
/*     */ 
/* 152 */     return new Integer(val);
/*     */   }
/*     */ }

/* Location:           C:\PC-Pilot-build\
 * Qualified Name:     phil.awt.UTMTextField
 * JD-Core Version:    0.5.3
 */