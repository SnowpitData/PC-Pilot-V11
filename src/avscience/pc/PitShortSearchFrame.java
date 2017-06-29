package avscience.pc;

import java.awt.*;
import avscience.wba.*;
import java.util.*;
import avscience.desktop.*;
import java.util.Date;
import avscience.ppc.*;
import org.compiere.grid.ed.Calendar;
import java.sql.Timestamp;
import java.net.*;
import java.io.*;
import java.awt.event.*;

public class PitShortSearchFrame extends Frame implements TimeFrame
{
	private static final String server="http://kahrlconsulting.com:8084/avscience/PitListServlet";
    org.compiere.grid.ed.Calendar estDate1;
    org.compiere.grid.ed.Calendar estDate2;
    java.awt.List locations = new java.awt.List();
    java.awt.List states = new java.awt.List();
    java.awt.List range = new java.awt.List();
    java.awt.List pits = new java.awt.List();
    Label time1 = new Label("");
    Label time2 = new Label("");
    Button setTime1 = new Button("Set Time");
    Button setTime2 = new Button("Set Time");
    Button getPits = new Button("Get Pits");
    Checkbox t1before;
    Checkbox t2before;
    Checkbox t1after;
    Checkbox t2after;
    CheckboxGroup tg1 = new CheckboxGroup();
    CheckboxGroup tg2 = new CheckboxGroup();
    ElvTextItemOp elv1;
    ElvTextItemOp elv2;
    int startx = 20;
    int starty = 60;
    int yspace=26;
    int colSpace = 320;
    int col3Space = 224;
    DegTextItemOp aspect1;
    DegTextItemOp aspect2;
    Choice operator = new Choice();
    TextArea query = new TextArea();
    Label noPits = new Label(" ");
    Hashtable pitSers = new Hashtable();
    long ts1;
    long ts2;
    Hashtable currentPits = new Hashtable();
    public MainFrame mf;
    Vector subFrames = new Vector();
    
    public PitShortSearchFrame(MainFrame mf)
    {
    	super("Search for Pits");
    	this.mf = mf;
    	this.setSize(930, 482);
    	setLayout(null);
    	SymWindow swindow = new SymWindow();
    	this.addWindowListener(swindow);
    	estDate1 = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", null, 16, this);
        estDate1.addWindowListener(swindow);
        estDate1.setVisible(false);
        
        estDate2 = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", null, 16, this);
        estDate2.addWindowListener(swindow);
        estDate2.setVisible(false);
        estDate2.setLocation(260, 80);
        MenuAction mnac = new MenuAction();
        setTime1.addActionListener(mnac);
        setTime2.addActionListener(mnac);
        getPits.addActionListener(mnac);
        pits.addActionListener(mnac);
        initControls();
    	buildForm();
    }
    
    public void updateEstDate()
    {
    	if ( estDate1.isVisible() )
        {
        	ts1= estDate1.getTimestamp().getTime();
        	System.out.println("TS: "+ts1);
        	Date d = new Date(ts1);
        	time1.setText(d.toString());
        	System.out.println(d.toString());
        }
        if ( estDate2.isVisible() )
        {
        	ts2= estDate2.getTimestamp().getTime();
        	Date d = new Date(ts2);
        	time2.setText(d.toString());
        }
    }
    
    void popPitList(boolean form, boolean testFilter, boolean layerFilter)
    {
    	noPits.setText("Getting pits from DB");
    	String qry=query.getText();
    	query.setText(" ");
    	repaint();
    	boolean and = operator.getSelectedItem().equals("AND");
    	int count = 0;
    	String whereClause = "";
    	try
    	{
	    	pits.removeAll();
	    	currentPits = new Hashtable();
	    	pitSers = new Hashtable();
	    	if ( form ) whereClause = getWhereClauseFromForm();
	    	else whereClause = qry;
	    	query.setText(whereClause);
	    	
	    	LinkedHashMap v = getPitsFromQuery(whereClause);
	    	Object[] keys = v.keySet().toArray();
			for ( int i = 0; i < keys.length; i++ )
	    	{
	    		String serial = (String) keys[i];
	    		String data = (String) v.get(serial);
	    		
	    		if (( data!=null) && (data.trim().length()>9))
	    		{
	    			try
	    			{
		    			avscience.ppc.PitObs pit = new avscience.ppc.PitObs(data); 
		    			if ( pit!=null )
		    			{
				    		String nm = pit.getName();
				    		System.out.println("pit name: "+nm);
				    		if ( nm != null )
				    		{
				    			boolean add = true;
				    			if ( add )
				    			{
				    				System.out.println("adding pit: "+pit.getName());
					    			pits.add(nm);
					    			currentPits.put(new Integer(count), pit);
					    			pitSers.put(new Integer(count), serial);
					    			count++;
					    		}
					  
				    		}
				    	}
				    }
				    catch(Exception ee){System.out.println(ee.toString());}
		    	}
	    	}
	    	if (testFilter | layerFilter) noPits.setText(pits.getItemCount()+" pits filtered.");
	    	
	    }
	    catch(Exception e){System.out.println(e.toString());}
    }
    
    private LinkedHashMap getPitsFromQuery(String whereClause)
    {
    	LinkedHashMap pits = new LinkedHashMap();
    	try
        {
            String err = null;
            URL url = new URL(server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "pitsfromquery");
            whereClause = URLEncoder.encode(whereClause, "UTF-8");
            props.put("q", whereClause);
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            pits =  (LinkedHashMap) result.readObject();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        System.out.println("# of PITS: "+pits.size());
        noPits.setText(pits.size()+" pits received.");
        return pits;
    }
    
    String getWhereClauseFromForm()
    {
    	boolean fa = false;
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("WHERE");
    	String s =" ( ";
    	String op = " "+operator.getSelectedItem()+" ";
    	String e =" ) ";
    	String[] sts = states.getSelectedItems();
    	if ( sts.length > 0 ) buffer.append(s);
    	for (int i = 0; i < sts.length; i++)
    	{
    		fa=true;
    		buffer.append(" STATE = '"+sts[i]+"'");
    		if (( sts.length > 1 ) && ( i != sts.length-1 ))buffer.append(" OR ");
    	}
    	if ( sts.length > 0 ) buffer.append(e);
    	
    	String[] rngs = range.getSelectedItems();
    	if ( fa && rngs.length > 0) buffer.append(op);
    	if ( rngs.length > 0 ) buffer.append(s);
    	for (int i = 0; i < rngs.length; i++)
    	{
    		fa=true;
    		buffer.append(" RANGE = '"+rngs[i]+"'");
    		if (( rngs.length > 1 ) && ( i != rngs.length-1 ))buffer.append(" OR ");
    	}
    	if ( rngs.length > 0 ) buffer.append(e);
    	
    	String[] locs = locations.getSelectedItems();
    	if ( fa && locs.length > 0 ) buffer.append(op);
    	if ( locs.length > 0 ) buffer.append(s);
    	for (int i = 0; i < locs.length; i++)
    	{
    		fa=true;
    		buffer.append(" LOC_NAME = '"+locs[i]+"'");
    		if (( locs.length > 1 ) && ( i != locs.length-1 ))buffer.append(" OR ");
    	}
    	if ( locs.length > 0 ) buffer.append(e);
    	String e1 = elv1.getText();
    	if ( e1.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" ELEVATION "+elv1.getOperatorValue()+ " "+e1);
    	}
    	
    	String e2 = elv2.getText();
    	if ( e2.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" ELEVATION "+elv2.getOperatorValue()+ " "+e2);
    	}
    	//////////
    	String a1 = aspect1.getText();
    	if ( a1.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" ASPECT "+aspect1.getOperatorValue()+ " "+a1);
    	}
    	
    	String a2 = aspect2.getText();
    	if ( a2.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" ASPECT "+aspect2.getOperatorValue()+ " "+a2);
    	}
    
    	Checkbox cb1 = tg1.getSelectedCheckbox();
    	if (( ts1 > 0 ) && (cb1!=null))
    	{
    		if ( fa ) buffer.append(op);
    		fa = true;
    		buffer.append(" OBS_DATETIME ");
    		if ( cb1 == t1after ) buffer.append("> ");
    		else buffer.append("< ");
    		buffer.append(getDateString(ts1));
    	}
    	
    	Checkbox cb2 = tg2.getSelectedCheckbox();
    	if (( ts2 > 0 ) && (cb2!=null))
    	{
    		if ( fa ) buffer.append(op);
    		fa = true;
    		buffer.append(" OBS_DATETIME ");
    		if ( cb2 == t2after ) buffer.append("> ");
    		else buffer.append("< ");
    		buffer.append(getDateString(ts2));
    	}
    	System.out.println("Where clause:: "+buffer.toString());
    	return buffer.toString();
    }

    
    String getDateString(long time)
    {
    	java.util.Calendar cal = java.util.Calendar.getInstance();
    	cal.setTimeInMillis(time);
    	String month = "";
    	String day = "";
    	int yr = cal.get(java.util.Calendar.YEAR);
    	String year = yr+"";
    	int mnth = cal.get(java.util.Calendar.MONTH)+1;
    	if ( mnth < 10 ) month = "0"+mnth;
    	else month = mnth+"";

    	int dy = cal.get(java.util.Calendar.DAY_OF_MONTH);
    	if ( dy < 10 ) day = "0"+dy;
    	else day = dy+"";
    	
    	int hr = cal.get(java.util.Calendar.HOUR_OF_DAY);
    	int mn = cal.get(java.util.Calendar.MINUTE);
    	int sc = cal.get(java.util.Calendar.SECOND);
    	
    	String hour = "";
    	if ( hr < 10 ) hour = "0"+hr;
    	else hour = hr+"";
    	String min = "";
    	if ( mn < 10 ) min = "0"+mn;
    	else min = mn+"";
    	String sec = "";
    	if ( sc < 10 ) sec = "0"+sc;
    	else sec = sc+"";
    	
    	
    	String res = year+month+day+hour+min+sec;
    	System.out.println("getDateString()");
    	System.out.println("res: "+res);
    	return res;
	}
	
    void buildForm()
    {
    	int xx = startx;
        int yy = starty;
        ///Label ol = new Label("Operator: ");
        ///ol.setSize(120, 20);
        ///ol.setLocation(xx, yy);
        ///add(ol);
        ///operator.setLocation(xx+124, yy);
        ///add(operator);
        operator.select("AND");
        yy+=yspace;
    	Label lcs = new Label("Locations:");
    	lcs.setSize(120, 20);
    	lcs.setLocation(xx, yy);
    	add(lcs);
    	locations.setLocation(xx, yy+yspace);
    	
    	add(locations);
    	locations.setSize(200, 120);
    	
    	xx+=col3Space;
    	Label sts = new Label("States/Provs:");
    	sts.setSize(128, 20);
    	sts.setLocation(xx, yy);
    	add(sts);
    	states.setLocation(xx, yy+yspace);
    	states.setMultipleMode(true);
    	Enumeration e = getStateList().elements();
    	while ( e.hasMoreElements())
    	{
    		String s = (String) e.nextElement();
    		states.add(s);
    	}
    	add(states);
    	states.setSize(200, 120);
    	
    	xx+=col3Space;
    	Label rngs = new Label("Ranges:");
    	rngs.setSize(128, 20);
    	rngs.setLocation(xx, yy);
    	add(rngs);
    	range.setLocation(xx, yy+yspace);
    	range.setMultipleMode(true);
    	e = getRangeList().elements();
    	while ( e.hasMoreElements())
    	{
    		String s = (String) e.nextElement();
    		range.add(s);
    	}
    	add(range);
    	range.setSize(200, 120);
    	
    	
    	xx=startx;
    	yy+=6*yspace;
    	Label t1 = new Label("Time 1");
    	t1.setSize(48, 20);
    	t1.setLocation(xx, yy);
    	add(t1);
    	time1.setSize(184, 20);
    	time1.setLocation(xx+50, yy);
    	add(time1);
    	xx+=colSpace;
    	Label t2 = new Label("Time 2");
    	t2.setSize(48, 20);
    	t2.setLocation(xx, yy);
    	add(t2);
    	time2.setSize(184, 20);
    	time2.setLocation(xx+50, yy);
    	add(time2);
    	yy+=yspace;
    	xx=startx;
    	
    	t1after = new Checkbox("after", tg1, false);
    	t2after = new Checkbox("after", tg2, false);
    	t1before = new Checkbox("before", tg1, false);
    	t2before = new Checkbox("before", tg2, false);
    	
    	t1before.setLocation(xx, yy);
    	t1before.setSize(90, 20);
    	add(t1before);
    	yy+=yspace;
    	t1after.setLocation(xx, yy);
    	t1after.setSize(90, 20);
    	add(t1after);
    	yy-=yspace;
    	setTime1.setSize(84, 24);
    	setTime1.setLocation(xx+96, yy);
    	add(setTime1);
    	xx+=colSpace;
    	t2before.setLocation(xx, yy);
    	t2before.setSize(90, 20);
    	add(t2before);
    	yy+=yspace;
    	t2after.setLocation(xx, yy);
    	t2after.setSize(90, 20);
    	add(t2after);
    	yy-=yspace;
    	setTime2.setSize(84, 24);
    	setTime2.setLocation(xx+96, yy);
    	add(setTime2);
    	
    	xx=startx;
    	yy+=2*yspace;
    	elv1 = new ElvTextItemOp("Elevation (m)", "m", xx, yy);
    	add(elv1);
    	xx+=colSpace;
    	elv2 = new ElvTextItemOp("Elevation (m)", "m", xx, yy);
    	add(elv2);
    	yy+=yspace;
    	xx=startx;
    	aspect1 = new DegTextItemOp("Aspect 1",xx, yy);
    	add(aspect1);
    
    	xx+=colSpace;
    	aspect2 = new DegTextItemOp("Aspect 2",xx, yy);
    	add(aspect2);
    	yy+=yspace;
    	xx=startx;
    	yy+=yspace;
    	xx=startx;
    	
    	query.setLocation(xx, yy-4);
    	query.setSize(620, 60);
    	add(query);
    	xx+=3*col3Space;
    	xx=3*col3Space+24;
    	yy = starty;
    	getPits.setLocation(xx, yy);
    	getPits.setSize(96, 22);
    	add(getPits);
    	yy+=yspace;
    	noPits.setSize(280, 20);
    	noPits.setLocation(xx, yy);
    	add(noPits);
    	yy+=yspace;
    	pits.setLocation(xx, yy);
    	pits.setSize(200, 338);
    	add(pits);
    }
    
    void initControls()
    {
    	String[] ops = {"<", ">", "="};
    	ItemListener listener = new iListener();
    	states.addItemListener(listener);
    	locations.addItemListener(listener);
    	range.addItemListener(listener);
        int i = 0;
        locations.setMultipleMode(true);
    	Enumeration e = getLocationList().elements();
    	while ( e.hasMoreElements())
    	{
    		String s = (String) e.nextElement();
    		locations.add(s);
    	}
        operator.add("OR");
        operator.add("AND");
        operator.select("OR");
    }
    
    void showDatePopup1()
    {
    	estDate1.setSize(380, 320);
       	estDate1.setVisible(true);
       	subFrames.add(estDate1);
    }
    
    void showDatePopup2()
    {
    	estDate2.setSize(380, 320);
       	estDate2.setVisible(true);
       	subFrames.add(estDate2);
    }
    
    public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
    
    public void add(TextItemOp item)
    {
        add(item.getLabel());
        add(item.getField());
      	add(item.getOperator());
    }
    
    
    
    public class iListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    		
    		if ( e.getItemSelectable()==states )
    		{
    			String item = e.getItem().toString();	
				if ( item.equals("0"))
				{
					boolean selected = states.isIndexSelected(0);
					for (int j = states.getItemCount(); j >= 0 ; j--)
					{
						if (selected ) states.select(j);
						else states.deselect(j);
					} 
				}
    		}
    	}
    }
    
    class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if ( object == setTime1 ) showDatePopup1();
			if ( object == setTime2 ) showDatePopup2();
			if ( object == getPits ) popPitList(true, false, false);
			if ( object == pits ) showPitFrame();
		}
			
	 }
	 
	void showPitFrame()
	{
		int idx = pits.getSelectedIndex();
		Integer I = new Integer(idx);
		String dbserial = (String) pitSers.get(I);
		avscience.ppc.PitObs pit = (avscience.ppc.PitObs) currentPits.get(I);
		subFrames.add(new avscience.pc.PitFrame(pit, mf, true, dbserial));
	} 
    
	private Vector getStateList()
    {
        Vector list = new Vector();
        try
        {
            URL url = new URL(server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "statelistall");
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            list = (Vector) result.readObject();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            return new Vector();
        }
        if ((list!=null) && (list.size()>0)) list.insertElementAt("ALL",0);
        
        return list;
    }
    
    private Vector getLocationList()
    {
        Vector list = new Vector();
        try
        {
            URL url = new URL(server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "locationlist");
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            list = (Vector) result.readObject();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            return new Vector();
        }
        return list;
    }
    
    private Vector getRangeList()
    {
        Vector list = new Vector();
        try
        {
            URL url = new URL(server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "rangelistall");
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            list = (Vector) result.readObject();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            return new Vector();
        }
              
        return list;
    }
    
    public void dispose()
    {
    	Enumeration e = subFrames.elements();
    	while ( e.hasMoreElements())
    	{
    		Object o = e.nextElement();
    		if (o instanceof Frame)
    		{
    			Frame f = (Frame)o;
    			try
    			{
    				f.dispose();
    			}
    			catch(Exception ex){System.out.println(ex.toString());}
    		}
    	}
    	super.dispose();
    }
    
    class SymWindow extends java.awt.event.WindowAdapter
    {   
        public void windowClosing(java.awt.event.WindowEvent event)
        {
        	System.out.println(getWhereClauseFromForm());
            Object object = event.getSource();
            if (object == PitShortSearchFrame.this) 
            {
            	dispose();
            	return;
            }
           /* if ( object == estDate1 )
            {
            	ts1= estDate1.getTimestamp().getTime();
            	System.out.println("TS: "+ts1);
            	Date d = new Date(ts1);
            	time1.setText(d.toString());
            	System.out.println(d.toString());
            }
            if ( object == estDate2 )
            {
            	ts2= estDate2.getTimestamp().getTime();
            	Date d = new Date(ts2);
            	time2.setText(d.toString());
            }*/
            
        }
    }
}