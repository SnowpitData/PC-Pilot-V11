package avscience.pc;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class ProfileCanvas extends Frame
{
     int numProfiles = 10;
     Rectangle[] rects;
     int type = -1;
     int xr = 28;
     int yr = 48;
   	 ProfileDisplay display;
   	 
     public ProfileCanvas(ProfileDisplay display)
     {
     	this.display = display;
     	setLocation(560, 200);
     	setSize(166, 146);
     	setTitle("Snow Profile");
     	this.addWindowListener(new SymWindow());
     	this.addMouseListener(new Listener());
     	show();
     } 
     
     public int getProfileType()
     { 
        return type+1;
     }
     
     public void setProfileType(int type)
     {
        this.type=type-1;
     }
     
     public void setProfileType(String type)
     {
     	int t = new Integer(type).intValue();
     	setProfileType(t);
     }
     
     public void onEvent(MouseEvent e)
     {
        int x=0;
        int y=0;
        x=e.getX();
        y=e.getY();
       
        for ( int i=0; i<rects.length; i++ )
        {
            if ( rects[i].contains(x, y))
            {
                Rectangle rr = rects[i];
                type = i;
                paint(this.getGraphics());
                repaint();
               
            }
        } 
       	display.setType(type+1);
       	display.repaint();
     }
    
     
     public void paint(Graphics g)
     {
        int x = 8;
        int y = 36;
      
        g.drawString(new Integer(type+1).toString(),4, 4);
      
        rects = new Rectangle[numProfiles];
        Rectangle r = null;
        SnowProfiles profiles = new SnowProfiles(g);
        for (int i=0; i<5; i++)
        {
            rects[i] = new Rectangle(x,y, xr, yr);
            g.drawRect(x,y, xr, yr);
            profiles.drawProfile(i+1, x+1, y+1);
            if (i==type) g.drawRect(rects[i].x+1, rects[i].y+1, xr-2, yr-2);
            x+=(xr+2);
        }
        x =8;
        y+=(yr+2);
        for (int i=5; i<10; i++)
        {
            rects[i] = new Rectangle(x,y, xr, yr);
            g.drawRect(x,y, xr, yr);
            profiles.drawProfile(i+1, x+1, y+1);
            if (i==type) g.drawRect(rects[i].x+1, rects[i].y+1, xr-2, yr-2);
            x+=(xr+2);
        }
    }
    
    class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == ProfileCanvas.this ) ProfileCanvas.this.dispose();
		}
	}
    
    class Listener implements MouseListener
    {
    	public void mouseClicked(MouseEvent e)
    	{
    		onEvent(e);
    	}
    	
    	public void mouseEntered(MouseEvent e)
    	{
    		
    	}
    	
    	public void mouseExited(MouseEvent e)
    	{
    		
    	}
    	
    	public void mousePressed(MouseEvent e)
    	{
    		
    	}
    	
    	public void mouseReleased(MouseEvent e)
    	{
    		
    	}
    }
     
}