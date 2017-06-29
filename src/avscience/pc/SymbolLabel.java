package avscience.pc;

import java.awt.*;
import java.io.*;

public class SymbolLabel extends Component
{
	private float fsize = 24f;
	String text;
    public SymbolLabel() 
    {
    	super();
    	setFont(loadFont());
    }
    
    public SymbolLabel(String text) 
    {
    	///super(text);
    	this.text=text;
    	setFont(loadFont());
    }
    
    public void paint(Graphics g)
    {
    	g.drawString(text, 10, 10);
    }
    
    public void paintAll(Graphics g)
    {
    	
    }
    
     Font loadFont()
	  {
	   		try
	   		{
	   			File file = new File("C:\\SnowSymbolsIACS.ttf");
                FileInputStream in = new FileInputStream(file);
	          ///  InputStream in = getClass().getResourceAsStream("SnowSymbolsIACS.ttf");
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