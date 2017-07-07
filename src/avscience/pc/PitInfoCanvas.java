package avscience.pc;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import avscience.ppc.*;
import avscience.wba.SkyCover;
import java.util.Date;

public class PitInfoCanvas extends Canvas implements MouseListener
{
	static int width = 680;
	static int height = 160;
	private avscience.ppc.PitObs pit;
	final static int margin = 16;
	final static int vspace = 18;
	int hspace;
	boolean invert = false;
	final static int lineLength = 98;
	// bounds for mouse events
	final static int xmin = margin;
    static int xmax = width-margin;
    static int ymin = height-36;
    static int ymax = height;
    public TextFrame tframe;
    public int linebrk=126;
    public boolean longNotes=false;
	boolean macos;
	
	public void update(avscience.ppc.PitObs pit)
	{
		this.pit = pit;
		tframe=null;
		repaint();
		System.out.println("PIT NOTES LENGTH::  "+pit.getPitNotes().length());
		if (pit.getPitNotes().length()>464) 
		{
			longNotes=true;
			popTextBox();
		}
	}
	
	public PitInfoCanvas(avscience.ppc.PitObs pit, int width, int height, boolean macos)
	{
		super();
		System.out.println("PitInfoCanvas()");
		this.pit = pit;
		this.width = width;
		this.height = height;
		this.macos=macos;
		xmax = width-margin;
		ymin = height-36;
		ymax = height;
		hspace = width/4;
		addMouseListener(this);
		this.setSize(this.width, this.height);
		if ( pit.getMeasureFrom()==null ) invert = true;
  		else if (!( pit.getMeasureFrom().equals("top") )) invert = true;
  		System.out.println("PIT NOTES LENGTH::  "+pit.getPitNotes().length());
		if (pit.getPitNotes().length()>464) 
		{
			longNotes=true;
			popTextBox();
		}
	}
	
	
	public void paint(Graphics g)
    {
        Font f = g.getFont();
        float size = f.getSize();
        System.out.println("Font Size: "+size);
        if ( macos ) 
        {
        	size=size-2.0f;
        	f = f.deriveFont(size);
        }
        
        Font bold=null;
    	f = new Font(f.getName(), Font.PLAIN, f.getSize());
   		bold = new Font(f.getName(), Font.BOLD, f.getSize());
   
        int x = margin;
        int y = vspace;
        avscience.ppc.Location l = pit.getLocation();
        if ( l==null ) l = new avscience.ppc.Location();
        avscience.ppc.User u = pit.getUser();
        if (u==null) u=new avscience.ppc.User();
        g.setColor(java.awt.Color.black);
        g.drawString("Snow Pit Profile", x, y);
        y+=vspace;
        g.setFont(bold);
        int ml=25;
        String name=l.getLocName();
        if ((l.getName()!=null) && (l.getName().length()>ml)) name = l.getName().substring(0, ml-1);
        g.drawString(name, x, y);
        y+=vspace;
        String s = l.getRange()+", "+l.getState();
       
        if (s.length()>23) s=s.substring(0, 22);
        
        g.drawString(s, x, y);
        y+=vspace;
        g.setFont(f);
        g.drawString("Elevation ("+u.getElvUnits()+") ", x, y);
        g.setFont(bold);
        g.drawString(l.getElv(), x+78, y);
        g.setFont(f);
        y+=vspace;
        g.drawString("Aspect:", x, y);
        g.setFont(bold);
        String aspect = pit.getAspect();
        if (aspect==null) aspect = "0";
        g.drawString(aspect, x+60, y);
        g.setFont(f);
        x=66;
        y = vspace;
        x+=hspace/2-24;
        g.drawString("Observer:", x, y);
        g.setFont(bold);
       
        g.drawString(u.getFirst()+" "+u.getLast(), x+60, y);
        g.setFont(f);
        y+=vspace;
        
        
        long ts = pit.getTimestamp();
        if ( ts > 1 )
        {
        	g.setFont(bold);
        	g.drawString(new Date(ts).toString(), x, y);
        }
        else
        {
        	///g.drawString("Date/Time (24h)", x, y);
	        g.setFont(bold);
	        String dt = pit.getDate();
	        if ( dt==null ) dt=" ";
	      	boolean udate = true;
	      	if (dt.trim().length()<8) udate = false;
	      	//String time = pit.getTime();
	      //	if (time==null)time=" ";
	      /*	if ( udate )
	      	{
		      	String yr="0";
		      	String mnth="0";
		      	String dy="0";
		      	String hr = "0";
		      	String min = "0";
		      	if (!(dt.trim().length()<8)) 
		      	{
		      		yr = dt.substring(0, 4);
		      		mnth = dt.substring(4, 6);
		      		dy = dt.substring(6, 8);
		      	}
		      	
		      	if ( !(time.trim().length()<4))
		      	{
		      		hr = time.substring(0, 2);
		      		min = time.substring(2, 4);
		      	}
		      	try
                        {
		      	int yy = new Integer(yr).intValue();
		      	int m = new Integer(mnth).intValue()-1;
		      	int d = new Integer(dy).intValue();
		      	int h = new Integer(hr).intValue();
		      	int mn = new Integer(min).intValue();
		      	java.util.Calendar cal = java.util.Calendar.getInstance();
		      	cal.set(yy, m, d, h, mn);
		      	ts = cal.getTimeInMillis();
		      	Date date = new Date(ts);
		        g.drawString(date.toString(), x, y);
                        }
                        catch(Exception e){System.out.println(e.toString());}
		    }*/
                    g.drawString(pit.getDateString(), x, y);
		}
        	
        g.setFont(f);
        y+=vspace;
        ///////////////////
        g.drawString("Co-ord:", x, y);
        g.setFont(bold);
        String coordType = l.type;
        if ( coordType==null )coordType="";
        if ( coordType.equals("UTM"))
        {
        	g.drawString(l.zone+" "+l.east+" "+l.north, x+48, y);
        }
        else
        {
        	g.drawString(l.getLat()+" "+l.getLatType()+" "+l.getLongitude()+" "+l.getLongType(), x+48, y);
       
        }
        ////////////////////
        y+=vspace;
        g.setFont(f);
        g.drawString("Slope:", x, y);
        g.setFont(bold);
        String incline = pit.getIncline();
        if ( incline==null ) incline ="";
        g.drawString(incline, x+42, y);
        g.setFont(f);
        y+=vspace;
        g.drawString("Wind loading:", x, y);
        g.setFont(bold);
        String wl = pit.getWindLoading();
        if (wl==null) wl="";
        g.drawString(wl, x+82, y);
        g.setFont(f);
        y+=vspace;
               
        y = vspace;
        x+=hspace;
        g.drawString("Stability on similar slopes:",x, y);
        g.setFont(bold);
        String stab=pit.getStability();
        if (stab==null) stab="";
        g.drawString(stab, x+150, y);
        g.setFont(f);
        y+=vspace;
       
        g.drawString("Air Temperature:", x,y);
        g.setFont(bold);
        String temp = pit.getAirTemp();
        if (temp==null) temp="";
        g.drawString(temp+" "+u.getTempUnits(),x+98, y);
        g.setFont(f);
        y+=vspace;
        g.drawString("Sky Cover:", x,y);
        g.setFont(bold);
        String sky = SkyCover.getInstance().getDescription(pit.getSky());
        if ( sky==null ) sky="";
        g.drawString(sky,x+62, y);
        g.setFont(f);
        y+=vspace;
        g.drawString("Precipitation:",x,y);
        g.setFont(bold);
        String prec = pit.getPrecip();
        if ( prec==null ) prec="";
        g.drawString(prec, x+76, y);
        g.setFont(f);
        y+=vspace;
        g.drawString("Wind:",x,y);
        g.setFont(bold);
        String win = pit.getWinDir();
        if ( win==null) win="";
        String wndspd = pit.getWindspeed();
        if ( wndspd==null ) wndspd = "";
        g.drawString(win + " "+wndspd, x+38, y);
       // 
             
        y = vspace;
        x+=hspace-16;
        //////
        StringBuffer buff = new StringBuffer();
    	String ftSki = pit.getSkiBoot();
    	if (ftSki==null)ftSki="";
    	String pf = "PF";
    	if ( ftSki.equals("Ski")) pf = "PS";
    	if (pit.getSurfacePen().trim().length()>0)
    	{
    		buff.append(pf);
    		buff.append(pit.getSurfacePen());
    	}
    	
    	String hsp = pit.getHeightOfSnowpack();
    	hsp=hsp.trim();
    	
    	if ( hsp.length()>0 )
    	{
    		buff.append(" ");
    		buff.append("HS");
    		buff.append(hsp);
    	}
        String sos = buff.toString();
        sos=sos.trim();
        if ( sos.length()>1)
        {
        	g.drawString(sos, x, y);
        	y+=vspace;
        }
        g.setFont(f);
        g.drawString("Stability Test Notes:", x, y);
        java.util.Vector v = new java.util.Vector();
        java.util.Enumeration e = pit.getShearTests();
        Vector tests = new Vector();
        while ( e.hasMoreElements() )
        {
        	avscience.ppc.ShearTestResult str = (avscience.ppc.ShearTestResult) e.nextElement();
        	v.add(str);
        }
        if ( invert ) v = Sorter.sortDescendingTests(v);
        else v = Sorter.sortAscendingTests(v);
        e = v.elements();
        g.setFont(bold);
        if (e!=null)
        {
	        while ( e.hasMoreElements())
	        {
	        	avscience.ppc.ShearTestResult test = null;
	        	test = (avscience.ppc.ShearTestResult) e.nextElement();
	      
	           	String ss = test.getDepth(); 
	        	String coms = test.getComments();
	        
	        	String sss = ss+": "+coms;
	        	
	        	if ( coms.trim().length()>1 )
	        	{
	        		y+=vspace;
	        		g.drawString(sss, x, y);
	        	}
	        }
	    }	
        g.setFont(f);
        x+=hspace-88;
        y=vspace;
        g.drawString("Layer notes:", x, y);
        v = new java.util.Vector();
        e = pit.getLayers();
        while ( e.hasMoreElements())
        {
            avscience.ppc.Layer la = (avscience.ppc.Layer) e.nextElement();
            v.add(la);
        }
        if ( invert ) v = Sorter.sortDescendingLayers(v);
        else v = Sorter.sortAscendingLayers(v);
        java.util.Enumeration ee = v.elements();
        while ( ee.hasMoreElements())
        {
        	boolean probLayer=false;
            avscience.ppc.Layer ll = (avscience.ppc.Layer) ee.nextElement();
            String n = ll.getComments();
            if (n==null)n="";
            String ln = ll.getLayerNumberString();
           // if ( (pit.iLayerNumber.equals(ln)) && (pit.iDepth.trim().length()<1) ) n="Problematic Layer "+n;
            if (pit.iLayerNumber.equals(ln))  
            {
            	///n="Problematic Layer "+n;
            	probLayer=true;
            }
            
          
            if (( n.trim().length()>0) || probLayer)
            {
                y+=vspace;
                g.setFont(f);
                s = ll.shortString();
                if ( s==null ) s = "";
                g.setFont(bold);
                //String lc = ll.getComments();
               // if ( n==null ) n = " ";
                if (probLayer)
                {
                	g.drawString(s+": "+"Problematic Layer ", x, y);
                	y+=vspace;
                	g.drawString(n, x, y);
                }
                else g.drawString(s+": "+n, x, y);
            }
        }
        
        y = 5*vspace;
        x = margin;
     
        StringBuffer buffer = new StringBuffer();
        if (pit.isPracticePit()) buffer.append("Practice Pit; ");
        if (pit.isSkiAreaPit()) buffer.append("Ski Area Pit; ");
        if (pit.isBCPit()) buffer.append("BC Pit; ");
        if (pit.isAviPit()) 
        {
        	buffer.append("Avalanche Pit: "+pit.aviLoc+"; ");
        }
        if ((pit.getActivities()!=null) && (pit.getActivities().elements()!=null))
        {
	        e = pit.getActivities().elements();
	        while ( e.hasMoreElements() )
	        {
	            buffer.append( (String) e.nextElement());
	            if (e.hasMoreElements() )buffer.append(" ");
	        }
	    }
        s = buffer.toString();
        y+=vspace;
        g.setFont(f);
        g.drawString("Specifics:", x, y);
        g.setFont(bold);
        g.drawString(s, x+54, y);
        
        y+=vspace;
        g.setFont(f);
      ///  g.drawString("Notes:", x, y);
        g.setFont(bold);
        
        
        String notes = pit.getPitNotes();
        System.out.println("NOTES::: "+notes);
        if ( notes==null ) notes="";
        if ( longNotes ) return;
       // if (pit.getPitNotes().length()>2*linebrk) 
       // {
        //	g.drawString("See attached notes.", x+42, y);
        //	longNotes=true;
       // 	return;
      //  }
      /*  int brk = getWhitespaceLineBreak();
        if ( notes.length() > brk )
        {
        	String nt1 = notes.substring(0, brk);
        	String nt2 = notes.substring(brk, notes.length());
        	if (nt2.length()>linebrk) nt2 = nt2.substring(0, linebrk);
        	g.drawString(nt1, x+42, y);
        	if ( nt2.trim().length()> 0) g.drawString(nt2, x+42, y+12);
        }
        else g.drawString(notes, x+42, y);*/
    }
    
    private int getWhitespaceLineBreak()
    {
    	String notes=pit.getPitNotes();
    	if ( notes.length()<linebrk) return linebrk;
    	int brk=linebrk;
    	for ( brk=linebrk+3; brk>linebrk-12; brk--)
    	{
    		if ( notes.length()>=brk)
    		{
    			char c=notes.charAt(brk);
    			if (Character.isWhitespace(c)) return brk;
    		}
    	}
    	return linebrk;
    }
    
    void popTextBox()
    {
    	System.out.println("TextBox");
    	if ( pit.getPitNotes().length()<152) return;
    	//if ( (tframe!=null) && (tframe.isVisible()) ) return;
    	if (tframe == null) 
    	{
    		tframe = new TextFrame(pit);
    		tframe.setVisible(true);
    		tframe.requestFocus();
    	}
    	else return;
    //	else 
    //	{
    	//	tframe.setVisible(true);
    		
    //	}
    }
    
    public void mouseClicked(MouseEvent e)
    {
    	int x=e.getX();
    	int y=e.getY();
    	
    //	if ((x>xmin) && (x<xmax) && (y>ymin) && (y<ymax)) popTextBox();
    }
    
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}