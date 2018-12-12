package avscience.desktop;

import avscience.pc.MainFrame;
import avscience.pc.PitFrame;
import avscience.ppc.AvOccurence;
import avscience.ppc.PitObs;
import avscience.ppc.Location;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class PitApplet extends Frame
{
    private static final String all = "ALL";
    public List pitList;
    private Label pitLabel;
    private List occList;
    private Label occLabel;
    private Label statLabel;
    private Label infoLabel;
    private List nameList;
    private Label nameLabel;
    private List stateList;
    private Label stateLabel;
    private Label filter;
    private Label Nof;
    private Label Sof;
    private Label Eof;
    private Label Wof;
    private TextField N;
    private TextField S;
    private TextField E;
    private TextField W;
    private Button showPit;
    private Button showOcc;
    private Button filterList;
    private PitCanvas c;
    private Panel pitPanel;
    private Panel mainPanel;
    private Panel occPanel;
    private Panel txtPanel;
    private TextArea qryBox;
    private TextArea msgBox;
    private Button qryButton;
    private Label qryLabel;
    private Label msgLabel;
    private Button txtButton;
    private ActListener alistener;
    private String breadCrumb;
    Button main;
    private Button back;
    int width;
    int height;
    private static final String server = "http://kahrlconsulting.com:8087/avscience/PitListServlet";
    private static final String pitserver = "http://kahrlconsulting.com:8087/avscience/PitServlet";
    private static final int yoffset = 36;
    public String pitlist[][];
    private String occlist[][];
    private MainFrame mframe;
    public boolean superuser;
    
    class ActListener implements ActionListener
    {
        public void actionPerformed(ActionEvent actionevent)
        {
            Object obj = actionevent.getSource();
            if(obj == pitList || obj == showPit)
            {
                String s = pitList.getSelectedItem();
                int i = pitList.getSelectedIndex();
                String s2 = pitlist[1][i];
                System.out.println("loading pit: # " + s2);
                statLabel.setText("Loading pit: " + s);
                PitObs pitobs1 = getPit(s2);
                PitFrame pitframe = new PitFrame(pitobs1, mframe, true, PitApplet.this);
                statLabel.setText("");
            }
            if(obj == main)
                init();
            if(obj == occList || obj == showOcc)
            {
                PitObs pitobs = null;
                String s1 = occList.getSelectedItem();
                statLabel.setText("Loading Occ: " + s1);
                int j = occList.getSelectedIndex();
                String s3 = occlist[1][j];
                AvOccurence avoccurence = getOcc(s3);
                String s4 = avoccurence.getSerial();
                if(s4 == null || s4.trim().length() < 1)
                {
                    System.out.println("getPitByName()");
                    pitobs = getPitByName(s1);
                } else
                {
                    System.out.println("getPitBySerial()");
                    String s5 = getCrownObs(s4);
                    try
                    {
                        pitobs = new PitObs(s5);
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.toString());
                    }
                }
                if(pitobs == null)
                    System.out.println("apit null.");
                ///new OccFrame(mframe, avoccureprivate static final AvalancheType instance = new AvalancheType();nce, pitobs, null, null, PitApplet.this);
                statLabel.setText("");
            }
            if(obj == filterList || obj == stateList || obj == nameList)
            {
                System.out.println(" Filtering list: ");
                System.out.println(whereClause());
                if(whereClause() != null)
                    popList(true);
                else
                    popList(false);
            }
            if(obj == txtButton)
                buildTxtPanel();
            StringBuffer stringbuffer;
            if(obj == qryButton)
                stringbuffer = new StringBuffer();
            if(obj == back)
            {
                if(pitPanel != null)
                {
                    pitPanel.setVisible(false);
                    remove(pitPanel);
                    pitPanel = null;
                }
                if(occPanel != null)
                {
                    occPanel.setVisible(false);
                    remove(occPanel);
                    occPanel = null;
                }
                if(breadCrumb.equals("qry"))
                    buildTxtPanel();
                else
                    showMain();
            }
            if(obj == PitApplet.this)
            {
                remove(c);
                statLabel.setText("Loading pit list: ");
                init();
            }
        }

        ActListener()
        {
        }
    }

    class SymWindow extends WindowAdapter
    {

        public void windowClosing(WindowEvent windowevent)
        {
            Object obj = windowevent.getSource();
            if(obj == PitApplet.this)
                dispose();
        }

        SymWindow()
        {
        }
    }


    public void init()
    {
        if(txtPanel != null)
        {
            remove(txtPanel);
            txtPanel.setVisible(false);
            txtPanel = null;
        }
        setVisible(true);
        filterList = new Button("Filter");
        pitList = new List();
        occList = new List();
        breadCrumb = "main";
        mainPanel = new Panel();
        mainPanel.setSize(width, height);
        mainPanel.setVisible(true);
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(null);
        setBackground(Color.white);
        setLayout(null);
        popList(false);
        statLabel.setText("");
        setSize(width, height);
        statLabel.setSize(220, 30);
        statLabel.setLocation(40, 536);
        statLabel.setVisible(true);
        pitLabel.setSize(180, 14);
        pitLabel.setLocation(256, 42);
        pitList.setSize(240, 220);
        pitList.setLocation(256, 60);
        occLabel.setSize(180, 14);
        occLabel.setLocation(256, 306);
        occList.setSize(240, 220);
        occList.setLocation(256, 324);
        nameLabel.setSize(180, 14);
        nameLabel.setLocation(50, 306);
        nameList.setSize(180, 220);
        nameList.setLocation(50, 324);
        stateLabel.setSize(180, 14);
        stateLabel.setLocation(50, 42);
        stateList.setSize(160, 220);
        stateList.setLocation(50, 60);
        setVisible(true);
        pitLabel.setVisible(true);
        pitList.setVisible(true);
        occLabel.setVisible(true);
        occList.setVisible(true);
        mainPanel.add(pitLabel);
        mainPanel.add(pitList);
        mainPanel.add(occLabel);
        mainPanel.add(occList);
        mainPanel.add(statLabel);
        mainPanel.add(nameLabel);
        mainPanel.add(nameList);
        mainPanel.add(stateLabel);
        mainPanel.add(stateList);
        add(mainPanel);
        pitList.addActionListener(alistener);
        occList.addActionListener(alistener);
        stateList.addActionListener(alistener);
        nameList.addActionListener(alistener);
        back.setSize(44, 18);
        back.setVisible(true);
        back.addActionListener(alistener);
        back.setVisible(false);
        back.setLocation(18, 40);
        add(back);
        addWindowListener(new SymWindow());
        setLocation(110, 110);
    }

    public void reinit()
    {
        pitList.removeAll();
        occList.removeAll();
        popList(false);
    }

    public String cleanString(String s)
    {
        if(s.trim().length() < 2)
            return s;
        System.out.println("cleanString()");
        try
        {
            char ac[] = s.toCharArray();
            int i = ac.length;
            System.out.println("String length: " + i);
            for(int j = 0; j < i; j++)
            {
                int k = j;
                if(k >= i || k <= 0)
                    continue;
                char c1 = ac[k];
                if(c1 < 0)
                    ac[k] = '\0';
            }

            String s1 = "";
            s1 = new String(ac);
            if(s1 != null && s1.trim().length() > 5)
                s = s1;
        }
        catch(Throwable throwable)
        {
            System.out.println("cleanString failed: " + throwable.toString());
        }
        return s;
    }

    private void showMain()
    {
        breadCrumb = "main";
        statLabel.setText("");
        back.setVisible(false);
        mainPanel.setVisible(true);
        add(mainPanel);
    }

    private void buildTxtPanel()
    {
        breadCrumb = "qry";
        remove(mainPanel);
        mainPanel.setVisible(false);
        txtPanel = new Panel();
        txtPanel.setSize(800, 600);
        txtPanel.setVisible(true);
        txtPanel.setLocation(0, 36);
        txtPanel.setBackground(Color.white);
        txtPanel.setLayout(null);
        popList(false);
        mainPanel = null;
        statLabel.setText("");
        txtPanel.setSize(800, 600);
        statLabel.setSize(220, 30);
        statLabel.setLocation(40, 500);
        statLabel.setVisible(true);
        pitLabel.setSize(180, 14);
        pitLabel.setLocation(50, 6);
        pitLabel.setVisible(true);
        pitList.setSize(180, 220);
        pitList.setLocation(50, 24);
        pitList.setVisible(true);
        occLabel.setSize(180, 14);
        occLabel.setLocation(50, 250);
        occList.setSize(160, 220);
        occList.setLocation(50, 268);
        qryBox = new TextArea();
        qryBox.setSize(300, 220);
        qryBox.setLocation(256, 24);
        qryLabel.setSize(180, 14);
        qryLabel.setLocation(256, 6);
        msgLabel.setSize(180, 14);
        msgLabel.setLocation(256, 262);
        msgBox = new TextArea();
        msgBox.setSize(300, 80);
        msgBox.setLocation(256, 280);
        qryButton.setSize(120, 30);
        showPit.setLocation(256, 372);
        showOcc.setLocation(256, 432);
        qryButton.setLocation(400, 372);
        main = new Button("Main Menu");
        main.setSize(120, 30);
        main.setVisible(true);
        main.setLocation(400, 432);
        txtPanel.add(statLabel);
        txtPanel.add(pitLabel);
        txtPanel.add(occLabel);
        txtPanel.add(pitList);
        txtPanel.add(occList);
        txtPanel.add(qryLabel);
        txtPanel.add(qryBox);
        txtPanel.add(msgLabel);
        txtPanel.add(msgBox);
        txtPanel.add(qryButton);
        txtPanel.add(showOcc);
        txtPanel.add(showPit);
        txtPanel.add(main);
        qryButton.addActionListener(alistener);
        main.addActionListener(alistener);
        add(txtPanel);
    }

    private String whereClause()
    {
        boolean flag = false;
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("WHERE");
        String s = nameList.getSelectedItem();
        if(s != null && s.trim().length() > 0)
        {
            s = s.trim();
            if(!s.equals("ALL"))
            {
                stringbuffer.append(" MTN_RANGE = '" + s + "' ");
                flag = true;
            }
        }
        String s1 = stateList.getSelectedItem();
        if(s1 != null && s1.trim().length() > 0)
        {
            s1 = s1.trim();
            if(!s1.equals("ALL"))
            {
                if(flag)
                    stringbuffer.append(" AND STATE = '" + s1 + "' ");
                else
                    stringbuffer.append(" STATE = '" + s1 + "' ");
                flag = true;
            }
        }
        String s2 = N.getText();
        s2 = s2.trim();
        if(s2.length() > 0)
        {
            float f = (new Float(s2)).floatValue();
            if(flag)
                stringbuffer.append(" AND LAT > " + f);
            else
                stringbuffer.append(" LAT > " + f);
            flag = true;
        }
        s2 = S.getText();
        s2 = s2.trim();
        if(s2.length() > 0)
        {
            float f1 = (new Float(s2)).floatValue();
            if(flag)
                stringbuffer.append(" AND LAT < " + f1);
            else
                stringbuffer.append(" LAT < " + f1);
            flag = true;
        }
        s2 = W.getText();
        s2 = s2.trim();
        if(s2.length() > 0)
        {
            float f2 = (new Float(s2)).floatValue();
            if(flag)
                stringbuffer.append(" AND LONGITUDE > " + f2);
            else
                stringbuffer.append(" LONGITUDE > " + f2);
            flag = true;
        }
        s2 = E.getText();
        s2 = s2.trim();
        if(s2.length() > 0)
        {
            float f3 = (new Float(s2)).floatValue();
            if(flag)
                stringbuffer.append(" AND LONGITUDE < " + f3);
            else
                stringbuffer.append(" LONGITUDE < " + f3);
            flag = true;
        }
        if(flag)
        {
            String res = stringbuffer.toString();
            res.replaceAll(" ", "%20");
            return res;
        }
        else
            return null;
    }

   /* private String[][] getPitListArrayFromQuery(String s)
        throws Exception
    {
        Object obj = null;
        String as[][] = (String[][])null;
        try
        {
            Object obj1 = null;
            URL url = new URL(pitserver);
            HttpMessage httpmessage = new HttpMessage(url);
            java.util.Properties properties = new java.util.Properties();
            properties.put("TYPE", "JSONPITLIST_FROMQUERY");
            s = URLEncoder.encode(s, "UTF-8");
            properties.put("q", s);
            java.io.InputStream inputstream = httpmessage.sendGetMessage(properties);
            ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
            obj = objectinputstream.readObject();
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
       
        as = (String[][])obj;
        return as;
    }*/

    private String[][] getOccListArrayFromQuery(String s)
        throws Exception
    {
        Object obj = null;
        String as[][] = (String[][])null;
        try
        {
            Object obj1 = null;
            URL url = new URL(server);
            HttpMessage httpmessage = new HttpMessage(url);
            java.util.Properties properties = new java.util.Properties();
            properties.put("format", "occlistarrayquery");
            s = URLEncoder.encode(s, "UTF-8");
            properties.put("q", s);
            java.io.InputStream inputstream = httpmessage.sendGetMessage(properties);
            ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
            obj = objectinputstream.readObject();
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        if(obj instanceof String[][])
        {
            String as1[][] = (String[][])obj;
            s = "";
            return as1;
        } else
        {
            String s1 = (String)obj;
            System.out.println("Exception on query: " + s1);
            throw new Exception(s1);
        }
    }

    
    
    private String[][] getPitListArrayFromQuery(String query)
    {
        String s = query.trim();
        query = s.replace(" ", "%20");
        String as[][] = (String[][])null;
        try
        {
            ///URL url = new URL("http://www.kahrlconsulting.com:8087/avscience/PitServlet?TYPE=JSONPITLIST_FROMQUERY&QUERY=WHERE%20STATE%20=%20%27CA%27");
            URL url = new URL("http://www.kahrlconsulting.com:8087/avscience/PitServlet?TYPE=JSONPITLIST_FROMQUERY&QUERY="+query);
            HttpURLConnection servletConnection = (HttpURLConnection)url.openConnection();
            servletConnection.setRequestProperty("Content-type","text/xml");
            servletConnection.setDoInput(true);	
            servletConnection.setUseCaches(false);
            servletConnection.connect();
            ObjectInputStream objectinputstream = new ObjectInputStream(servletConnection.getInputStream());
            as = (String[][])objectinputstream.readObject();
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return as;
    }
    
    private String[][] getPitListArray()
    {
        String as[][] = (String[][])null;
        try
        {
            URL url = new URL("http://www.kahrlconsulting.com:8087/avscience/PitServlet?TYPE=JSONPITLIST");
            HttpURLConnection servletConnection = (HttpURLConnection)url.openConnection();
            servletConnection.setRequestProperty("Content-type","text/xml");
            servletConnection.setDoInput(true);	
            servletConnection.setUseCaches(false);
            servletConnection.connect();
            ObjectInputStream objectinputstream = new ObjectInputStream(servletConnection.getInputStream());
            as = (String[][])objectinputstream.readObject();
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return as;
    }

    private String[][] getOccListArray()
    {
        String as[][] = (String[][])null;
        try
        {
            URL url = new URL("http://www.kahrlconsulting.com:8087/avscience/PitServlet?TYPE=JSONOCCLIST");
            HttpURLConnection servletConnection = (HttpURLConnection)url.openConnection();
            servletConnection.setRequestProperty("Content-type","text/xml");
            servletConnection.setDoInput(true);	
            servletConnection.setUseCaches(false);
            servletConnection.connect();
            ObjectInputStream objectinputstream = new ObjectInputStream(servletConnection.getInputStream());
            as = (String[][])objectinputstream.readObject();
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return as;
    }

    private String[] getStateList()
    {
        String[] sts = null;
        try
        {
            /////////////////
            URL url = new URL("http://www.kahrlconsulting.com:8087/avscience/PitServlet?TYPE=STATELIST");
            HttpURLConnection servletConnection = (HttpURLConnection)url.openConnection();
            servletConnection.setRequestProperty("Content-type","text/xml");
            servletConnection.setDoInput(true);	
            servletConnection.setUseCaches(false);
            servletConnection.connect();
            ObjectInputStream objectinputstream = new ObjectInputStream(servletConnection.getInputStream());
            sts = (String[])objectinputstream.readObject();
            //////////////////
          
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return sts;
    }

    private java.util.Vector getLocationList()
    {
        java.util.Vector vector = null;
        try
        {
            URL url = new URL(server);
            HttpMessage httpmessage = new HttpMessage(url);
            java.util.Properties properties = new java.util.Properties();
            properties.put("format", "locationlist");
            java.io.InputStream inputstream = httpmessage.sendGetMessage(properties);
            ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
            vector = (java.util.Vector)objectinputstream.readObject();
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return vector;
    }

    private String[] getRangeList()
    {
        String[] sts = null;
        try
        {
            /////////////////
            URL url = new URL("http://www.kahrlconsulting.com:8087/avscience/PitServlet?TYPE=RANGELIST");
            HttpURLConnection servletConnection = (HttpURLConnection)url.openConnection();
            servletConnection.setRequestProperty("Content-type","text/xml");
            servletConnection.setDoInput(true);	
            servletConnection.setUseCaches(false);
            servletConnection.connect();
            ObjectInputStream objectinputstream = new ObjectInputStream(servletConnection.getInputStream());
            sts = (String[])objectinputstream.readObject();
            //////////////////
          
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return sts;
    }

    public PitObs getPit(String s)
    {
        PitObs pitobs = null;
        try
        {
            URL url = new URL(pitserver);
            HttpMessage httpmessage = new HttpMessage(url);
            java.util.Properties properties = new java.util.Properties();
            properties.put("TYPE", "JSONPIT");
            properties.put("SERIAL", s);
            java.io.InputStream inputstream = httpmessage.sendGetMessage(properties);
            ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
            String s1 = (String)objectinputstream.readObject();
            if(s1 == null || s1.trim().length() < 3)
                System.out.println("No data for pit # " + s);
            s1 = cleanString(s1);
            pitobs = new PitObs(s1);
            if(pitobs == null)
                System.out.println("Pit null.");
            System.out.println("Pit Loc: " + pitobs.getLocation().getName());
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return pitobs;
    }

    private PitObs getPitByName(String s)
    {
        System.out.println("Get pit by name: " + s);
        PitObs pitobs = null;
        try
        {
            URL url = new URL(pitserver);
            HttpMessage httpmessage = new HttpMessage(url);
            java.util.Properties properties = new java.util.Properties();
            properties.put("TYPE", "PITSTRING");
            properties.put("PITNAME", s);
            java.io.InputStream inputstream = httpmessage.sendGetMessage(properties);
            ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
            String s1 = (String)objectinputstream.readObject();
            pitobs = new PitObs(s1);
            System.out.println("Pit: " + pitobs.getName());
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return pitobs;
    }

    private AvOccurence getOcc(String s)
    {
        System.out.println("getOcc: " + s);
        AvOccurence avoccurence = null;
        try
        {
            URL url = new URL(pitserver);
            HttpMessage httpmessage = new HttpMessage(url);
            java.util.Properties properties = new java.util.Properties();
            properties.put("TYPE", "PPCOCC");
            properties.put("SERIAL", s);
            java.io.InputStream inputstream = httpmessage.sendGetMessage(properties);
            ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
            String s1 = (String)objectinputstream.readObject();
            System.out.println("Occ data: " + s1);
            avoccurence = new AvOccurence(s1);
            if(avoccurence == null)
                System.out.println("Occ null.");
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return avoccurence;
    }

    private AvOccurence getWbaOcc(String s)
    {
        System.out.println("getWbaOcc: " + s);
        AvOccurence avoccurence = null;
        try
        {
            URL url = new URL(pitserver);
            HttpMessage httpmessage = new HttpMessage(url);
            java.util.Properties properties = new java.util.Properties();
            properties.put("TYPE", "PPCOCC");
            properties.put("SERIAL", s);
            java.io.InputStream inputstream = httpmessage.sendGetMessage(properties);
            ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
            String s1 = (String)objectinputstream.readObject();
            System.out.println("Occ data: " + s1);
            avoccurence = new AvOccurence(s1);
            if(avoccurence == null)
                System.out.println("Wba Occ null.");
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return avoccurence;
    }

    private String getCrownObs(String s)
    {
        System.out.println("getCrownObs: " + s);
        String s1 = "";
        try
        {
            URL url = new URL(pitserver);
            HttpMessage httpmessage = new HttpMessage(url);
            java.util.Properties properties = new java.util.Properties();
            properties.put("TYPE", "OCCCROWNOBS");
            properties.put("SERIAL", s);
            java.io.InputStream inputstream = httpmessage.sendGetMessage(properties);
            ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
            s1 = (String)objectinputstream.readObject();
            System.out.println("data length: " + s1.length());
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
        return s1;
    }

    private void popList(boolean flag)
    {
        try
        {
            Object obj = null;
            if(!flag)
                pitlist = getPitListArray();
            else
                pitlist = getPitListArrayFromQuery(whereClause());
            pitList.removeAll();
            for(int i = 0; i < pitlist[0].length; i++)
                pitList.add(pitlist[0][i]);

           /* if(!flag)
               occlist = getOccListArray();
            else
               occlist = getOccListArrayFromQuery(whereClause());
            occList.removeAll();
            for(int j = 0; j < occlist[0].length; j++)
                occList.add(occlist[0][j]);
                */
            nameList.removeAll();
            nameList.add("ALL");
            for (String s : getRangeList())
            {
                nameList.add(s);
            }
            

            stateList.removeAll();
            stateList.add("ALL");
            for (String s : getStateList())
            {
                stateList.add(s);
            }
           
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
        }
    }

    private void showOcc(AvOccurence avoccurence, PitObs pitobs)
    {
        mainPanel.setVisible(false);
        remove(mainPanel);
        if(txtPanel != null)
        {
            txtPanel.setVisible(false);
            remove(txtPanel);
        }
        occPanel = new Panel();
        occPanel.setSize(800, 600);
        occPanel.setBackground(Color.white);
        occPanel.setVisible(true);
        java.awt.Graphics g = occPanel.getGraphics();
        OccCanvas occcanvas = new OccCanvas(avoccurence, pitobs);
        occcanvas.setVisible(true);
        occcanvas.setLocation(10, 10);
        occPanel.add(occcanvas);
        back.setVisible(true);
        add(occPanel);
    }

    private void status(String s)
    {
        statLabel.setText(s);
        statLabel.setVisible(true);
        add(statLabel);
        repaint();
    }

    public PitApplet(MainFrame mainframe)
    {
        pitList = new List();
        pitLabel = new Label("Current Pits");
        occList = new List();
        occLabel = new Label("Current Occurrences");
        statLabel = new Label("");
        infoLabel = new Label("Degrees Lat/Long decimal format.");
        nameList = new List();
        nameLabel = new Label("Mountain Range");
        stateList = new List();
        stateLabel = new Label("State-Prov");
        filter = new Label("Filter by:");
        Nof = new Label("North Of:");
        Sof = new Label("South Of:");
        Eof = new Label("East Of:");
        Wof = new Label("West Of:");
        N = new TextField(5);
        S = new TextField(5);
        E = new TextField(5);
        W = new TextField(5);
        showPit = new Button("Show Pit");
        showOcc = new Button("Show Occurrence");
        filterList = new Button("Filter");
        qryButton = new Button("Execute Query");
        qryLabel = new Label("Enter query: ");
        msgLabel = new Label("Messages: ");
        txtButton = new Button("Text query: ");
        alistener = new ActListener();
        breadCrumb = "main";
        back = new Button("back");
        width = 530;
        height = 600;
        superuser = false;
        mframe = mainframe;
        superuser = mainframe.checkSuperUser();
    }
}