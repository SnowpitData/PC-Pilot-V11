package avscience.pc;

import java.awt.*;
import java.awt.event.*;
import avscience.desktop.*;
import java.io.*;
import java.net.*;
import java.util.Properties;
import avscience.ppc.*;
import java.util.*;

public class MainFrame extends Frame implements ListFrame
{
    private int width = 360;
    private int height = 428;
    private MenuBar mainMenuBar = new java.awt.MenuBar();
    private Menu menu = new java.awt.Menu();
    private MenuItem addUserMenuItem = new java.awt.MenuItem();
    private MenuItem editUserMenuItem = new java.awt.MenuItem();
    private MenuItem preferencesMenuItem = new java.awt.MenuItem();
    private MenuItem locationsMenuItem = new java.awt.MenuItem();
    private MenuItem pitsMenuItem = new java.awt.MenuItem();
    private MenuItem deletePitsMenuItem = new java.awt.MenuItem();
    private MenuItem occsMenuItem = new java.awt.MenuItem();
    private MenuItem addPitMenuItem = new java.awt.MenuItem();
    private MenuItem addOccMenuItem = new java.awt.MenuItem();
    private MenuItem sendMenuItem = new java.awt.MenuItem();
    private MenuItem wcMenuItem = new java.awt.MenuItem();
    private MenuItem editNews = new java.awt.MenuItem();
    private MenuItem advQryMenuItem = new java.awt.MenuItem();
    private Button addPit = new Button("Add Pit");
    private Button addOcc = new Button("Add Occurence");
   // final static int maxDataLength=4500;
    public boolean smallscreen;
    Choice users  = new Choice();
    Choice locations;
    Choice pits = new Choice();
    Choice occs = new Choice();
    private static final String filename = "PCPILOTV5.DAT";
    private static final String propFile = "PROPS.DAT";
    public static final String server="http://www.kahrlconsulting.com:8087/avscience/PitListServlet";
    public static final String pitserver="http://www.kahrlconsulting.com:8087/avscience/PitServlet";
    final static int dataPushSize=8000;
    
    public avscience.pc.SPV5DataStore store = avscience.pc.SPV5DataStore.getInstance();
    public final static int bld = 58;
    final static String vDate = "V.10-8087 "+ bld;
    public final static String version = vDate+" PC: "+System.getProperty( "os.name" );
    public java.awt.List  pitList;
    public java.awt.List  occList;
    Hashtable pitSerials = new Hashtable();
    public boolean  macos;
    public String defaultUser = "";
    long lastDataSentTime = 0;
    long dataSendIterval = 5*24*60*60*1000;
    MainFrame thisframe;
	
    Logger logger = Logger.getInstance();
	
	public static void main(String[] args)
	{
		new MainFrame();
		byte b = 0;
		byte[] bb = new byte[1];
		bb[0]=b;
		String s = new String(bb);
	
	}
	
	private void loadProperties()
    {
    	Properties props = new Properties();
        try
        {
            File propsFile = new File(propFile);
            FileInputStream in = new FileInputStream(propsFile);
            props.load(in);
            defaultUser = props.getProperty("DEFAULTUSER");
        }
        catch(Exception e){logger.println(e.toString());}
    }
    
    private void saveProperties()
    {
    	Properties props = new Properties();
    	props.put("DEFAULTUSER", defaultUser);
    	try
    	{
    	    File propsFile = new File(propFile);
            FileOutputStream out = new FileOutputStream(propsFile);
            props.store(out, "UserInfo");
            out.close();
    	}
    	catch(Exception e){logger.println(e.toString());}
    }
	
	public MainFrame()
	{
		super("Snow Pilot - Main Menu");
		
		thisframe = this;
		setLayout(null);
		try
		{
			readData();
		}
		catch(Exception e){logger.println(e.toString());}
		
		addPit.addActionListener(new MenuAction());
		addOcc.addActionListener(new MenuAction());
		pits.addItemListener(new PitoccListener());
		occs.addItemListener(new PitoccListener());
		users.addItemListener(new PitoccListener());
		this.setSize(width, height);
		this.setLocation(184, 8);
		this.setVisible(true);
		
		this.setMaximizedBounds(new Rectangle(width, height));
		
		String os_name = System.getProperty( "os.name" );
		logger.println("OS: "+os_name);
		if ( os_name.startsWith( "Mac" ) || os_name.startsWith( "mac" ) ) macos=true;
		if ( macos ) smallscreen = true;
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		
		loadProperties();
		checkGraphics();
        
                 buildForm();
                 buildMenu();
                String news = getCurrentNews();
        
                if ( news.trim().length()>1 ) 
                {
                    MessageFrame nmsg = new MessageFrame(news);
                    nmsg.setVisible(true);
                    nmsg.requestFocus();
                }
                if ((System.currentTimeMillis() - lastDataSentTime) > dataSendIterval) new Sender(false).start();
	}
        
	public Location getCurrentLocation()
	{
		Location l = new Location();
		String loc = locations.getSelectedItem();
		
		if ( (loc!=null) && ( loc.trim().length() > 0 ))
		{
			l = store.getLocation(loc);
		}
		return l;
	}
	
	public boolean getSmallScreen()
	{
		checkGraphics();
		return smallscreen;
	}
	
	public int getMaxScreenHeight()
	{
		GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = local.getDefaultScreenDevice();
		DisplayMode mode = device.getDisplayMode();
		return mode.getHeight();
	}
	
	public int getMaxScreenWidth()
	{
		GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = local.getDefaultScreenDevice();
		DisplayMode mode = device.getDisplayMode();
		return mode.getWidth();
	}
	
	public void checkGraphics()
	{
		GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = local.getDefaultScreenDevice();
		DisplayMode mode = device.getDisplayMode();
		int w = mode.getWidth();
		int h = mode.getHeight();
		if ((w < 1440) || (h < 900))smallscreen=true;
		else smallscreen=false;
		macos = false;
		String os_name = System.getProperty( "os.name" );
		//logger.println("OS: "+os_name);
		if ( os_name.startsWith( "Mac" ) || os_name.startsWith( "mac" ) ) macos=true;
		if (macos) smallscreen=true;
	}
	
	void buildForm()
	{
		int x=80;
		setLayout(null);
		Label lu = new Label("Users");
		lu.setLocation(x, 80);	
		lu.setSize(180, 20);
		lu.setVisible(true);
		add(lu);
		users.setSize(180, 20);

		users.setLocation(x, 110);
		users.setVisible(true);
		add(users);
		Label ll = new Label("Locations");
		ll.setLocation(x, 140);
		ll.setSize(180, 20);
		add(ll);
		locations=new Choice();
		locations.setSize(180, 20);
	
		locations.setLocation(x, 170);
		add(locations);
		pits.setSize(180, 20);
		pits.setVisible(true);
		add(pits);
		pits.setLocation(x, 230);
		
		Label pl = new Label("Pits");
		pl.setSize(180, 20);
		pl.setLocation(x, 200);
		add(pl);
		occs.setSize(180, 20);
		occs.setVisible(true);
		add(occs);
		occs.setLocation(x, 290);
		
		Label ol = new Label("Occurrences");
		ol.setSize(180, 20);
		ol.setLocation(x, 260);
		add(ol);
		
		addPit.setSize(80, 22);
		addPit.setLocation(x, 340);
		add(addPit);
		
		addOcc.setSize(92, 22);
		addOcc.setLocation(x+88, 340);
		add(addOcc);
		Label v = new Label(vDate);
		v.setSize(166, 22);
		v.setLocation(x+20, 380);
		add(v);
		rebuildList();
		
	}
	
    public class PitoccListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    		if ( e.getItemSelectable()==users )
    		{
    			defaultUser = users.getSelectedItem();
    		}
    		if ( e.getItemSelectable()==pits )
    		{
    			int idx = pits.getSelectedIndex();
                        System.out.println("Getting pit for idx: "+idx);
                        String serial = (String) pitSerials.get(idx);
                        System.out.println("PIT serial: "+serial);
                        avscience.ppc.PitObs pit = store.getPit(serial);
                        if (pit!=null) new avscience.pc.PitFrame(pit, MainFrame.this, false);
    		}
    		
    		if ( e.getItemSelectable()==occs )
    		{
    			logger.println("Editing occ.........");
    			
    			int idx = occs.getSelectedIndex();
    			if ( idx > 0 )
    			{
	    			avscience.ppc.AvOccurence occ = store.getOcc(idx);
	    			new OccFrame(occ, null, MainFrame.this, true, null, null).show();
	    		}
	    		
    		}
    	}
    }
    
    public boolean readData()
    {
    	store = null;
    	File file = null;
    	FileInputStream fin = null;
    	ObjectInputStream oin = null;
    	logger.println("readData()");
    	try
        {
            try
        	{
            	file = new File( filename);
            }
            catch(Exception e)
            {
            	logger.println(e.toString());
            	return false;
            }
            fin = new FileInputStream(file);
            oin = new ObjectInputStream(fin);
            try
            {
            	store = (avscience.pc.SPV5DataStore) oin.readObject();
            }
            catch(ClassNotFoundException e){logger.println(e.toString());}
            fin.close();
        }
        catch(IOException e){logger.println(e.toString());}
      
        if (store==null) 
        {
        	logger.println("initializing datastore");
        	store=avscience.pc.SPV5DataStore.getInstance();
        }
        return true;
    }
    
    void sendData(boolean showDialog)
	{
        boolean sent = false;
        store.getPitNames();
        //store.getOccNames();
        String[] pitSers = store.getPitSerials();
        //String[] occSers = store.getOccSerials();
        int size = pitSers.length;
        SendDialog send = new SendDialog(this, false);
        if ( showDialog)
        {
            
            send.setLocation(300, 300);
            send.setVisible(true);
        }
        
        logger.println("Sending data to web app..");
        logger.println("Number of pits: "+size);
        for ( int i=0; i<size; i++ )
        {
            try
            {
                avscience.ppc.PitObs pit = store.getPit(pitSers[i]);
                logger.println("Pit name: "+pit.getName());
                String rs = sendPitToServer(pit);
                logger.println(rs);
            }
            catch(Exception e)
            {
                logger.println(e.toString());
            }
        }
               
        if (showDialog) send.dispose();
       /* if (sent)
        {
           OKDialog ok = new OKDialog(this, true);
           ok.setLocation(220, 220);
           ok.setVisible(true);
        }*/
        rebuildList();
        lastDataSentTime = System.currentTimeMillis();
    }
    
    private String sendPitToServer(avscience.ppc.PitObs pit)
	{
		System.out.println("bounceObjectToServer()");
		Object result=null;
		try
		{
			StringBuffer encUrl = new StringBuffer(pitserver);
			encUrl.append("?TYPE=XMLPIT_SEND");
			encUrl.append("");
			
			System.out.println("Url: "+encUrl.toString());
			URL servletUrl = new URL(encUrl.toString());
			HttpURLConnection servletConnection = (HttpURLConnection)servletUrl.openConnection();
		   
			servletConnection.setChunkedStreamingMode(dataPushSize);
			servletConnection.setRequestProperty("Content-type","text/xml");

			servletConnection.setRequestMethod("POST");
			servletConnection.setDoOutput(true);
                        servletConnection.setDoInput(true);	
			servletConnection.setUseCaches(false);
			servletConnection.connect();

			char[] chars = new XMLWriter().getXML(pit);
			
			OutputStreamWriter oos = new OutputStreamWriter(servletConnection.getOutputStream());
			oos.write(chars, 0, chars.length);
			oos.flush(); 
			oos.close();
		
			logger.println("READING OBJECT RESPONSE");
			ObjectInputStream ois = new ObjectInputStream(servletConnection.getInputStream());
			if ( ois!=null )result = ois.readObject();
		}
		catch(Exception e){System.out.println(e.toString());}
		logger.println("result: "+result);
		return result.toString();
	}
    
    class Sender extends Thread
    {
        boolean showDialog=false;
        public Sender(boolean showDialog)
        {
            this.showDialog = showDialog;
        }
        
    	public void run()
    	{
    		sendData(showDialog);
    	}
    }
	
	public void rebuildList()
	{
		users.removeAll();
		String[] usrs = store.getUserNames();
		for ( int i = 0; i < usrs.length; i++ )
		{
			users.add(usrs[i]);
		}
		users.select(defaultUser);
		
		locations.removeAll();
		String [] lcs = store.getLocationNames();
		for ( int i = 0; i < lcs.length; i++ )
		{
			if (lcs[i]!=null) locations.add(lcs[i]);
		}
		
		pits.removeAll();
                java.util.Vector pts = store.getPits();
                pts = sortPitsByTime(pts);
		for (int i = 0; i < pts.size(); i++ )
		{
                    avscience.ppc.PitObs mpt = (avscience.ppc.PitObs) pts.elementAt(i);
                    String nm = mpt.getName();
                    pits.add(nm);
                    pitSerials.put(i, mpt.getSerial());
		}
		
		occs.removeAll();
	//	occs.add(" ");
		String[] ocs = store.getOccNames();
		for (int i = 0; i < ocs.length; i++ )
		{
			occs.add(ocs[i]);
		}
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof ListFrame) && (f!=null))
			{
				ListFrame lf = (ListFrame) f;
				if (lf!=this) lf.rebuildList();
			}
		}
		saveData();
	}
        
        /////////////////////
        public java.util.Vector sortPitsByTime(java.util.Vector pits)
    {
    	boolean sorted = false;
        int length = pits.size();
        //Vector v = new Vector(length);
        int i = 0;
        avscience.ppc.PitObs pit;
        avscience.ppc.PitObs pitInc;

        if (length > 0)
        {
            while (!sorted)
            {
                sorted = true;
                for(i=0; i<length - 1; i++)
                {
                    pit = (avscience.ppc.PitObs) pits.elementAt(i);
                    long time = pit.getTimestamp();
                    pitInc = (avscience.ppc.PitObs) pits.elementAt(i+1);
                    long timeinc = pitInc.getTimestamp();
                  
                    if ( timeinc > time )
                    {
                            pits.setElementAt(pitInc, i);
                            pits.setElementAt(pit, i+1);
                            sorted = false;
                    }
                }
            }
        }
        return pits;
    }
        /////////////////
	
	public void saveData()
    {  
    	logger.println(" saveData()");
	    File file;
	    FileOutputStream fout;
	    ObjectOutputStream oout;
   
        try
        {  
            file = new File(filename);
            fout = new FileOutputStream(file);
            oout = new ObjectOutputStream(fout);
            oout.writeObject(store);
            oout.flush();
            fout.close();
        }
        
        catch(Exception e){logger.println(e.toString());}
    }

	private void buildMenu()
    {
        addUserMenuItem.setLabel("Add User");
        editUserMenuItem.setLabel("Edit User");
        preferencesMenuItem.setLabel("Preferences");
        locationsMenuItem.setLabel("Locations");
        pitsMenuItem.setLabel("Edit Pits");
        deletePitsMenuItem.setLabel("Delete Pits");
        occsMenuItem.setLabel("Edit Occurences");
        addPitMenuItem.setLabel("Add Pit");
        addOccMenuItem.setLabel("Add Occurence");
        int size=12;
        if (macos) size=14;
        Font bold = new Font(null, Font.BOLD, size);
        sendMenuItem.setFont(bold);
        sendMenuItem.setLabel("Send Data to web");
        wcMenuItem.setLabel("Web Database");
      //  qryMenuItem.setLabel("Search For Pits");
        advQryMenuItem.setLabel("Advanced Search");
        editNews.setLabel("Edit News");
    	menu.setLabel("Select..");
    	
        menu.add(addUserMenuItem);
        menu.add(editUserMenuItem);
        menu.add(preferencesMenuItem);
        menu.add(locationsMenuItem);
        menu.add(pitsMenuItem);
        menu.add(deletePitsMenuItem);
        menu.add(occsMenuItem);
        menu.add(addPitMenuItem);
        menu.add(addOccMenuItem);
       	menu.add(sendMenuItem);
       	menu.add(wcMenuItem);
       	if ( checkSuperUser()) menu.add(editNews);
       	menu.add(advQryMenuItem);
       	///else menu.add(qryMenuItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
    
        MenuAction mnac = new MenuAction();
        addUserMenuItem.addActionListener(mnac);
        editUserMenuItem.addActionListener(mnac);
        locationsMenuItem.addActionListener(mnac);
        preferencesMenuItem.addActionListener(mnac);
        deletePitsMenuItem.addActionListener(mnac);
        pitsMenuItem.addActionListener(mnac);
        occsMenuItem.addActionListener(mnac);
        addPitMenuItem.addActionListener(mnac);
        addOccMenuItem.addActionListener(mnac);
        sendMenuItem.addActionListener(mnac);
        wcMenuItem.addActionListener(mnac);
  ///      qryMenuItem.addActionListener(mnac);
        advQryMenuItem.addActionListener(mnac);
        editNews.addActionListener(mnac);
    }
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == MainFrame.this)
				MainFrame_WindowClosing(event);
		}
	}
	
	public avscience.ppc.User getDefaultUser()
	{
		System.out.println("MF::getDefaultUser(): ");
	//	String un = users.getSelectedItem();
	//	System.out.println("username: "+un);
		return store.getUser(defaultUser);
	}
	
	public avscience.ppc.User getUser()
	{
		avscience.ppc.User u = new avscience.ppc.User();
		System.out.println("MF::getUser(): ");
		String un = users.getSelectedItem();
		System.out.println("username: "+un);
		u=store.getUser(un);
		if (u==null) u = new avscience.ppc.User();
		return u;
	}
	
	void startWebClient()
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof avscience.desktop.PitApplet) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing)
		{
			avscience.desktop.PitApplet pc = new avscience.desktop.PitApplet(this);
			pc.init();
			pc.setTitle("Snow Pilot - Current Web Data");
		}
	}
	
	public String getCurrentNews()
	{
		String news="";
		try
    	{
	    	URL url = new URL(pitserver);
	        HttpMessage msg = new HttpMessage(url);
	        Properties props = new Properties();
	        props.put("TYPE", "GET_NEWS");
	        
	        InputStream in = msg.sendGetMessage(props);
	        ObjectInputStream result = new ObjectInputStream(in);
	       	news  = (String) result.readObject();
	       	if ( news == null )news="";
	       	
	     }
	     catch(Exception e)
         {
            System.out.println(e.toString());
            e.printStackTrace();
         }
		return news;
	}
	
	public boolean checkSuperUser()
    {
    	logger.println("checkSuperUser()");
    	boolean superuser = false;
    	avscience.ppc.User user = getUser();
    	try
    	{
	    	URL url = new URL(pitserver);
	        HttpMessage msg = new HttpMessage(url);
	        Properties props = new Properties();
	        
	        String uname = "";
	        if (user.getName()!=null) uname=user.getName();
	        String email = "";
	        if (user.getEmail()!=null) email=user.getEmail();
	        props.put("TYPE", "AUTHSUPERUSER");
	        props.put("USERNAME", uname);
	        props.put("EMAIL", email);
	        InputStream in = msg.sendGetMessage(props);
	        ObjectInputStream result = new ObjectInputStream(in);
	       	String check  = (String) result.readObject();
	       	if ( check != null )
	       	{
	       		check = check.trim();
	       		if ( check.equals("TRUE")) superuser = true;
	       	}
	     }
	     catch(Exception e)
         {
            System.out.println(e.toString());
            e.printStackTrace();
         }
         logger.println("superuser: "+superuser);
         return superuser;
    }
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			String uname=users.getSelectedItem();
			
			Object object = event.getSource();
			if ( object == addUserMenuItem )
			{
				showUserFrame(false);
				return;
			}
			if ((uname!=null)&&(uname.trim().length()>0))
			{	
				if ( object == editUserMenuItem) showUserFrame(true);
				if ( object == preferencesMenuItem ) showPreferencesFrame();
				if ( object == locationsMenuItem ) showLocationListFrame();
				if ( object == pitsMenuItem ) showPitListFrame();
				if ( object == deletePitsMenuItem ) showPitDeleteFrame();
				if ( object == occsMenuItem ) showOccListFrame();
			//	if ( object == qryMenuItem ) showQueryShortFrame();
				if ( object == advQryMenuItem ) showQueryFrame();
				if (( object == addPitMenuItem )||( object == addPit )) showPitHeaderFrame(false, null, null);
				if (( object == addOccMenuItem )||( object == addOcc )) showOccFrame(false);
			}
                        else showUserFrame(false);
                        if ( object == sendMenuItem ) new Sender(true).start();///sendData();
                        if ( object == wcMenuItem ) startWebClient();
                        if ( object == editNews ) editNews();
		}
	}
	
	void editNews()
	{
		showEditNewsFrame(new EditNewsFrame(this));
	}
	
	void showPitHeaderFrame(avscience.pc.PitFrame pframe)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof PitHeaderFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new PitHeaderFrame(pframe).setVisible(true);
		
	}
	////
	void showEditNewsFrame(avscience.pc.EditNewsFrame pframe)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof EditNewsFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new EditNewsFrame(this).setVisible(true);
        }
	
	void showPitSumFrame(avscience.pc.PitFrame pframe)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof PitSumFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new PitSumFrame(pframe).setVisible(true);
		
	}
	
	void showUserFrame(boolean edit)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof UserFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new UserFrame(thisframe, edit).setVisible(true);
		
	}
	
	void showLayerFrame(avscience.pc.PitHeaderFrame pframe)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof LayerFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing)
		{
			LayerFrame lframe = new LayerFrame(thisframe, pframe);
			lframe.setVisible(true);
			pframe.subFrames.add(lframe);
		}
	}
	
	void showILayerFrame(avscience.pc.PitHeaderFrame pframe)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof ILayerFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing)
		{
			ILayerFrame lframe = new ILayerFrame(thisframe, pframe);
			lframe.setVisible(true);
			pframe.subFrames.add(lframe);
		}
	}
	
	void showTempFrame(avscience.pc.PitHeaderFrame pframe)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof TempFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing)
		{
			TempFrame tframe = new TempFrame(thisframe, pframe);
			tframe.setVisible(true);
			pframe.subFrames.add(tframe);
		}
	}
	
	void showDensityFrame(avscience.pc.PitHeaderFrame pframe)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof DensityFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing)
		{
			DensityFrame dframe = new DensityFrame(thisframe, pframe);
			dframe.setVisible(true);
			pframe.subFrames.add(dframe);
		}
	}
	
	void showTestFrame(avscience.pc.PitHeaderFrame pframe)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof TestFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing)
		{
			TestFrame tframe = new TestFrame(thisframe, pframe);
			tframe.setVisible(true);
			pframe.subFrames.add(tframe);
		}
	}
	
	public void popPrefFrame(avscience.ppc.User u)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		PreferencesFrame pf = null;
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof PreferencesFrame) && (f!=null))
			{
				pf = (PreferencesFrame) f;
				if (f.isShowing())
				{
					f.show();
					showing = true;
					pf.popForm(u);
					break;
				}
			}
		}
		if (!showing) 
		{
			pf = new PreferencesFrame(this);
			pf.setVisible(true);
			pf.popForm(u);
		}
			
	}
	
	void showPreferencesFrame()
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof PreferencesFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new PreferencesFrame(this).setVisible(true);
		
	}
	
	void showLocationListFrame()
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof LocationListFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new LocationListFrame(thisframe).setVisible(true);
		
	}
	
	void showPitListFrame()
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof PitListFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new PitListFrame(thisframe).setVisible(true);
		
	}
	
	void showPitDeleteFrame()
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof PitDeleteFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new PitDeleteFrame(thisframe).setVisible(true);
		
	}
	
	void showQueryFrame()
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof PitSearchFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new PitSearchFrame(this).setVisible(true);
	}
	
	void showQueryShortFrame()
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof PitShortSearchFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new PitShortSearchFrame(this).setVisible(true);
	}
	
	
	
	void showOccListFrame()
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof OccListFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new OccListFrame(thisframe).setVisible(true);
	}
	
	void showPitHeaderFrame(boolean edit, avscience.ppc.PitObs pit, PitListFrame frame)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof PitHeaderFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new PitHeaderFrame(thisframe, edit, pit, frame).setVisible(true);
		
	}
	
	void showOccFrame(boolean edit)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof OccFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new OccFrame(edit, this).setVisible(true);
		
	}
	
	public void showOccFrame(avscience.ppc.AvOccurence occ)
	{
		boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof OccFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) new OccFrame(occ, null, this, true, null, null).setVisible(true);
		
	}
	
	void MainFrame_WindowClosing(java.awt.event.WindowEvent event)
	{ 
		saveProperties();
		saveData();
		this.dispose();
		System.exit(0);
	}
       
}	
	