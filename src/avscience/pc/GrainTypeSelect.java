package avscience.pc;

import avscience.desktop.GrainTypeSymbols;
import avscience.ppc.GrainTypeConvertor;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class GrainTypeSelect extends Canvas
{
 
  int x=8;
  int y;
  int size=3;
  int vspace=32;
  int wdth=340;
  int ht = vspace+size*vspace;
  String [] types = new String[size];
  Rectangle[] rects = new Rectangle[size];
  Rectangle srect;
  Rectangle drect;
  Image drpdwn;
  boolean dropped=false;
  String selectedSymbol;
  GrainTypeSelectionFrame frame;
  String type="PP";
  java.awt.Color myBlue = new java.awt.Color(0f, 0f, 0.6f, 0.5f);
  GrainTypeConvertor gtc = GrainTypeConvertor.getInstance();
  
  public GrainTypeSelect(GrainTypeSelectionFrame frame, String type)
  {
      System.out.println("GrainTypeSelect:type: "+type);
    setSize(wdth+8, 30);
    setVisible(true);
    this.frame=frame;
    this.type=type;
    drpdwn=loadDropDown();
    
    types = gtc.getSubTypesArray(type);
    size = types.length;
    rects = new Rectangle[types.length];
    
    ht = vspace+size*vspace;
    drect = new Rectangle(0,0,wdth+8,vspace);
    y+=26;
    for (int i=0; i<size; i++ )
   	{
   		rects[i] = new Rectangle(x, y, wdth, vspace);
   		y+=vspace;
   	}
   	y=0;
   addMouseMotionListener(new MListener());
   addMouseListener(new MoListener());
   	
  }
  
  public void reset()
  {
  	if (!dropped &(selectedSymbol==null)) return;
  	dropped=false;
  	selectedSymbol=null;
  	repaint();
  }
  
  Image loadDropDown()
  {
  	Image image=null;
  	try
  	{
  		InputStream in = getClass().getResourceAsStream("dropdwn.png");
  		image = ImageIO.read(in);
  	}
  	catch(Exception e)
  	{
  		System.out.println(e.toString());
  	}
  	return image;
  }
  
	
   public void paint(Graphics g)
   {
       System.out.println("GTS:paint()");
   	x=8;
   	y=0;
   	g.drawImage(drpdwn, x, y, null);
   	GrainTypeSymbols gts = GrainTypeSymbols.getInstance(g);
   	if (selectedSymbol!=null)
   	{
            System.out.println("Selected symbol: "+selectedSymbol);
            String scd = gtc.getSubTypeCode(selectedSymbol);
            System.out.println("scd: "+scd);
            gts.drawSymbol(24, 6, scd);
   	}
   		
        if ( dropped )
   	{
            setSize(wdth+8, ht+8);
            y+=26;
            g.setColor(Color.BLACK);
            g.drawRect(x+2,y,wdth-4,ht-vspace);
            y+=4;
	   	    
	   if ( srect!=null )
	   {
	   	g.setColor(myBlue);
	   	g.fillRect(srect.x+2, srect.y, srect.width-4, srect.height);
	   	g.setColor(Color.BLACK);
	   }
           
	   for (int i=0; i<types.length; i++ )
	   {
	   	String type = gtc.getSubTypeCode(types[i]);
                System.out.println("types[i]: "+types[i]);
                System.out.println("type: "+type);
	   	gts.drawSymbol(24, y, types[i]);
                String s = gtc.getUITypeFromCode(types[i]);
               // String s = types[i];
                if (s==null) s="null";
                if (type!=null) g.drawString(s, 68, y+12);
	   	y+=vspace;
	   }
   	}
   	else setSize(wdth+8, 30);
   }
   
   class MoListener implements MouseListener 
   {
   		public void mouseClicked(MouseEvent e)
   		{
   			int xx = e.getX();
   			int yy = e.getY();
   			if (!dropped)
   			{
   				if ( drect.contains(xx, yy))
	   			{
	   				dropped=true;
	   				repaint();
	   			}
	   			return;
   			}
   			else
   			{
   				for (int i=0; i<size; i++ )
	   			{
	   				if ( rects[i].contains(xx, yy))
	   				{
	   					frame.reset(GrainTypeSelect.this);
	   					selectedSymbol=types[i];
                                                frame.setType(selectedSymbol);
	   					dropped=false;
	   					repaint();
	   					return;
	   				}
	   			}
   			}
   			
   		}
   		
   		public void mousePressed(MouseEvent e){}
   		public void mouseReleased(MouseEvent e){}
   		public void mouseEntered(MouseEvent e){}
   		public void mouseExited(MouseEvent e){}
   }
   
   
   class MListener implements MouseMotionListener
   {
   		boolean[] hilites = new boolean[size];
   		public 	void mouseDragged(MouseEvent e){}
   		public void mouseMoved(MouseEvent e) 
   		{
   			int xx = e.getX();
   			int yy = e.getY();
   			for (int i=0; i<size; i++ )
   			{
   				if ( rects[i].contains(xx, yy))
   				{
   					srect=rects[i];
   					
   					if (!hilites[i]) 
   					{
   						repaint();
   						hilites[i]=true;
   					}
   					break;
   				}
   				else hilites[i]=false;
   			}
   			
   		}
   }
 
}