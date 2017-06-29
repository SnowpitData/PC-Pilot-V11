 package avscience.desktop;
 
 import java.awt.Color;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Font;
 import java.awt.RenderingHints;
 import java.io.*;
 import java.util.Hashtable;
 
 public class GrainTypeSymbols
 {
   private static final int size = 18;
   private float fsize = (float)size+6;
   private int mid = 9;
   private static final int height = 18;
   private int base = 16;
   private int startx;
   private int starty;
   private static final int inc = 2;
   private static final int bigInc = 3;
   private static Graphics2D g;
   private Hashtable<String, String> fontSymbols = new Hashtable<String, String>();
   Font ff;
   
   private static final GrainTypeSymbols instance  = new GrainTypeSymbols();
   
   private GrainTypeSymbols()
   { 
	ff=loadFont();
	if ( ff==null ) System.out.println("Could not load font.");
	else 
	{
	     System.out.println("Snow Symbol Font loaded! ");
	}
        initFontSymbols();
   }
   
   public static GrainTypeSymbols getInstance(Graphics paramGraphics)
   {
        g = ((Graphics2D)paramGraphics);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return instance;
    	
   }
   
   Font loadFont()
   {
   	try
   		{
            InputStream in = getClass().getResourceAsStream("SnowSymbolsIACS.ttf");
            if ( in!=null )
            {
            	Font f = Font.createFont(Font.TRUETYPE_FONT, in);
            	if (f!=null) return f.deriveFont(fsize);
            }
            else System.out.println("Could not open font file!!!!!!!!!!!!");
   		}
   		catch(Exception e)
   		{
   			System.out.println(e.toString());
   		}
   		return null;
   }
 
   public void scaleForPrint()
   {
     this.base = 24;
   }
 
  public void unscale()
   {
   }
 
   public void drawSymbol(int startx, int starty, String grainType)
   {
     this.startx = startx;
     this.starty = starty;
     grainType = grainType.trim();
     System.out.println("drawSymbol: "+grainType);
     
     Font f = this.g.getFont();
     this.g.setFont(ff);
     
     String symbol = fontSymbols.get(grainType);
     if (symbol != null) this.g.drawString(symbol, this.startx, this.starty+this.base);
     this.g.setFont(f);
   }

   public int getHeight()
   {
     return 18;
  }
   
   private void initFontSymbols()
   {
       fontSymbols.put("PP", "\uE000");
       fontSymbols.put("MM", "\uE001");
       fontSymbols.put("DF", "\uE002");
       fontSymbols.put("RG", "\uE003");
       fontSymbols.put("FC", "\uE004");
       fontSymbols.put("DH", "\uE005");
       fontSymbols.put("SH", "\uE006");
       fontSymbols.put("MF", "\uE007");
       fontSymbols.put("IF", "\uE008");
       
       fontSymbols.put("PPco", "\uE009");
       fontSymbols.put("PPnd", "\uE00A");
       fontSymbols.put("PPpl", "\uE00B");
       fontSymbols.put("PPsd", "\uE00C");
       fontSymbols.put("PPir", "\uE00D");
       fontSymbols.put("PPgp", "\uE00E");
       fontSymbols.put("PPhl", "\uE00F");
       fontSymbols.put("PPip", "\uE010");
       fontSymbols.put("PPrm", "\uE011");
       
       fontSymbols.put("MMrp", "\uE012");
       fontSymbols.put("MMci", "\uE013");
       
       fontSymbols.put("DFdc", "\uE014");
       fontSymbols.put("DFbk", "\uE015");
       
       fontSymbols.put("RGsr", "\uE016");
       fontSymbols.put("RGlr", "\uE017");
       fontSymbols.put("RGwp", "\uE018");
       fontSymbols.put("RGxf", "\uE019");
       
       fontSymbols.put("FCso", "\uE01A");
       fontSymbols.put("FCsf", "\uE01B");
       fontSymbols.put("FCxr", "\uE01C");
       
       fontSymbols.put("DHcp", "\uE01D");
       fontSymbols.put("DHpr", "\uE01E");
       fontSymbols.put("DHch", "\uE01F");
       fontSymbols.put("DHla", "\uE020");
       fontSymbols.put("DHxr", "\uE021");
       
       fontSymbols.put("SHsu", "\uE022");
       fontSymbols.put("SHcv", "\uE023");
       fontSymbols.put("SHxr", "\uE024");
       
       fontSymbols.put("MFcl", "\uE025");
       fontSymbols.put("MFpc", "\uE026");
       fontSymbols.put("MFsl", "\uE027");
       fontSymbols.put("MFcr", "\uE028");
       
       fontSymbols.put("IFil", "\uE029");
       fontSymbols.put("IFic", "\uE02A");
       fontSymbols.put("IFbi", "\uE02B");
       fontSymbols.put("IFrc", "\uE02C");
       fontSymbols.put("IFsc", "\uE02D");
   }
 }

