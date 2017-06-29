package avscience.pc;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class SymbolChoice extends JList
{
	private float fsize = 24f;
	Font f;
    public SymbolChoice(Object[] els) 
    {
    	super(els);
    //	f=loadFont();
    //	super.setFont(f);
    }
    
    
    Font loadFont()
	  {
	   		try
	   		{
	   			File file = new File("C:\\SnowSymbolsIACS.ttf");
                FileInputStream in = new FileInputStream(file);
	           // InputStream in = getClass().getResourceAsStream("SnowSymbolsIACS.ttf");
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
}