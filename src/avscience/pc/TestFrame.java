package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.awt.event.*;
import avscience.ppc.User;
import avscience.ppc.ShearTestResult;

public class TestFrame extends Frame
{
	private int width = 360;
	private int height = 500;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem addMenuItem = new java.awt.MenuItem();
	private MenuItem addSameItem = new java.awt.MenuItem();
	private MenuItem saveMenuItem = new java.awt.MenuItem();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
	private MenuItem backMenuItem = new java.awt.MenuItem();
	MainFrame mframe;
	PitHeaderFrame pframe;
	int vspace = 30;
	Choice tests = new Choice();
	Choice testType;
	Choice testScore;
	Choice testQ;
	Choice fractureChar;
	Choice releaseType;
	Label rtl;
	Button addSame = new Button("Same Result");
	Button save = new Button("save");
	Label ttype;
	DepthTextItem depth;
	DepthTextItem lengthOfCut;
	DepthTextItem lengthOfColumn;
	CTScoreItem ctscore;
        CTScoreItem dtscore; // DT Score, same format / rules as CT Score
	ECScoreItem ecscore;
	DegTextItem numOfTaps;
	TextItem notes;
	Label msg = new Label();
	Label sq;
	Label fc;
	avscience.ppc.User user;
	Button add;
	boolean editing;
	Label ts;
	void buildForm(boolean edit)
	{
            editing=edit;
            removeAll();
            buildMenu(edit);
            int ys=56;
            int x=24;
            int y=ys;
            int vspace=24;
            Label u = new Label("User "+user.getName());
            u.setLocation(x, y);
            u.setSize(220, 20);
            add(u);
            y+=vspace;
            if (!edit)
            {
                Label l = new Label("Current Stability Tests");
                l.setLocation(x, y);
                l.setSize(220, 20);
                add(l);
                y+=vspace;
                tests.removeAll();
                tests.setLocation(x, y);
                tests.setSize(200, 20);
                String[] tsts = pframe.getPit().getTestResultStrings();
                tests.add(" ");
                for ( int i=0; i<tsts.length; i++ )
                {
                    tests.add(tsts[i]);
                }
                add(tests);
                y+=vspace;
	    }
    	
            Label tt = new Label("Select Stability Test Type");
            tt.setLocation(x, y);
            tt.setSize(220, 20);
            add(tt);
            y+=vspace;
    	
            testType = new Choice();
            testType.setLocation(x, y);
            testType.setSize(200, 20);
            String[] types = ShearTests.getInstance().getShearTestDescriptions();
            for ( int i=0; i<types.length; i++ )
            {
    		testType.add(types[i]);
            }
            add(testType);
            y+=vspace;
            ts = new Label("Stability Test Score");
            ts.setLocation(x, y);
            ts.setSize(200, 20);
            add(ts);
            y+=vspace;
            testScore=new Choice();
            testScore.setLocation(x, y);
            testScore.setSize(116, 20);
            String[] tss = ShearTests.getInstance().getShearTest(testType.getSelectedItem()).getScores();
            for ( int i=0; i<tss.length; i++ )
            {
    		testScore.add(tss[i]);
            }
            add(testScore);
            y+=vspace;
            testQ=new Choice();
    
            sq = new Label("Shear Quality");
            sq.setLocation(x, y);
            sq.setSize(200, 20);
            add(sq);
            fc = new Label("Fracture Character");
            fc.setLocation(x, y);
            fc.setSize(200, 20);
            add(fc);
            y+=vspace;
            String[] qls = new String[4];
            qls[0]=" ";
            qls[1]="Q1";
            qls[2]="Q2";
            qls[3]="Q3";

            testQ.setLocation(x, y);
            testQ.setSize(120, 20);
    	
            for ( int i=0; i<qls.length; i++ )
            {
    		testQ.add(qls[i]);
            }
            testQ.setVisible(false);
            add(testQ);
            testQ.select("Q2");
    	
            String[] fcss = {" ", "SP", "SC", "PC", "RP", "BRK"};
            fractureChar=new Choice();
            fractureChar.setLocation(x, y);
            fractureChar.setSize(84, 20);
    	
            for ( int i=0; i<fcss.length; i++ )
            {
    		fractureChar.add(fcss[i]);
            }
            add(fractureChar);
    	
            System.out.println("Fracture CAT:: "+user.fractureCat);
            if ( user.fractureCat.equals("Fracture Character"))
            {
    		System.out.println("setting to fracture cat:");
    		fc.setVisible(true);
    		fractureChar.setVisible(true);
    		testQ.setVisible(false);
    		sq.setVisible(false);
            }
            else
            {
    		System.out.println("setting to shear qual.:");
    		fc.setVisible(false);
    		fractureChar.setVisible(false);
    		testQ.setVisible(true);
    		sq.setVisible(true);
            }
    
            y+=vspace;
            releaseType = new Choice();
            String[] rtypes = RBReleaseTypes.getInstance().getDisplayNames();
            for ( int i=0; i<rtypes.length; i++ )
            {
    		releaseType.add(rtypes[i]);
            }
            rtl = new Label("Release Type");
            rtl.setLocation(x, y);
            rtl.setSize(144, 20);
            add(rtl);
            lengthOfCut = new DepthTextItem("Length of Saw Cut "+user.getDepthUnits(), x, y);
            add(lengthOfCut);
            numOfTaps = new DegTextItem("Number of taps", x, y);
            add(numOfTaps);
            numOfTaps.setVisible(false);
            y+=vspace;
            releaseType.setLocation(x, y-8);
            releaseType.setSize(144, 32);
            add(releaseType);
            lengthOfColumn = new DepthTextItem("Length of Isolated Column "+user.getDepthUnits(), x, y);
            add(lengthOfColumn);
            ecscore = new ECScoreItem("ECT Score", x, y);
            ctscore = new CTScoreItem("CT Score", x, y);
            dtscore = new CTScoreItem("DT Score", x, y);
            testScore.addItemListener(new CTScoreAction());
            add(ctscore);
            add(ecscore);
            add(dtscore);
            
            if ( testType.getSelectedItem().trim().equals("Compression Test")) ctscore.setVisible(true);
            else ctscore.setVisible(false);
        
            if ( testType.getSelectedItem().trim().equals("Deep Tap Test")) dtscore.setVisible(true);
            else dtscore.setVisible(false);
    	
            if ( user.fractureCat.equals("Shear Quality"))
            {
    		if ( (testScore.getSelectedItem().trim().equals("ECTX"))) testQ.setVisible(false);
    		else testQ.setVisible(true);
            }
    	
            if (testScore.getSelectedItem().trim().equals("ECTP")) ecscore.setVisible(true); 
            else ecscore.setVisible(false);
    	
            if ( testType.getSelectedItem().trim().equals("Rutschblock Test"))
            {
    		rtl.setVisible(true);
    		releaseType.setVisible(true);
            }
            else 
            {
    		rtl.setVisible(false);
    		releaseType.setVisible(false);
            }
    
            y+=vspace; 
            depth = new DepthTextItem("Depth "+user.getDepthUnits(), x, y);
            add(depth);
            y+=vspace;
            notes = new TextItem("Comments", x, y, 16);
            add(notes);
            y+=vspace;
            Label nl = new Label("16 char. max");
            nl.setLocation(x, y);
            nl.setSize(120, 20);
            nl.setVisible(true);
            add(nl);
            testType.addItemListener(new TestChangeAction());
    	
            if ( testType.getSelectedItem().trim().equals("Extended Column Test")) numOfTaps.setVisible(true);
            else numOfTaps.setVisible(false);
    	
            if ( user.fractureCat.equals("Shear Quality"))
            {
	    	if ( testType.getSelectedItem().trim().equals("Propogation Saw Test"))
	    	{
                    lengthOfCut.setVisible(true);
                    lengthOfColumn.setVisible(true);
                    testQ.setVisible(false);
                    sq.setVisible(false);
	    	}
	    	else
	    	{
                    lengthOfCut.setVisible(false);
                    lengthOfColumn.setVisible(false);
                    testQ.setVisible(true);
                    sq.setVisible(true);
	    	}
            }
    	
            if (!edit)
            {
	    	y+=vspace;
	    	add = new Button("Add Test");
	    	add.setSize(72, 22);
	    	add.setLocation(x, y);
	    	add(add);
	    	add.addActionListener(new MenuAction());
	    }
	    else
	    {
	    	y+=vspace;
	    	save = new Button("Save");
	    	save.setSize(86, 22);
	    	save.setLocation(x+144, y);
	    	add(save);
	    	save.addActionListener(new MenuAction());
	    	addSame = new Button("Same Result");
	    	addSame.setSize(86, 22);
	    	addSame.setLocation(x, y);
	    	add(addSame);
	    	addSame.addActionListener(new MenuAction());
	    }
            y+=2*vspace;
            msg.setSize(240, 24);
            msg.setLocation(x, y);
            msg.setVisible(true);
            add(msg);
	}
	
	class TestListener implements ItemListener
        {
            public void itemStateChanged(ItemEvent e)
            {
                if ( e.getItemSelectable()==tests )
                {
                    editTest();
                }
            }
        }
	
	void displayMsg(String message)
	{
            System.out.println(message);
            msg.setText(message);
            
	}
	
	public void add(TextItemType item)
        {
            add(item.getLabel());
            add(item.getField());
       }
    
	public TestFrame(MainFrame mframe, PitHeaderFrame pframe)
	{
	    super("Snow Pilot - Stability Tests");
	    this.mframe = mframe;
	    this.pframe=pframe;
	    user = pframe.getPit().getUser();
            setLayout(null);
		
            this.setSize(width, height);
            this.setLocation(160, 90);
            this.setVisible(true);
	
            this.setMaximizedBounds(new Rectangle(width, height));
		
            SymWindow aSymWindow = new SymWindow();
            this.addWindowListener(aSymWindow);
            addMenuItem.setLabel("Add this test");
            addSameItem.setLabel("Same Result");
            saveMenuItem.setLabel("Save test");
            deleteMenuItem.setLabel("Delete test");
            backMenuItem.setLabel("back");
            menu.setLabel("Select..");
            mainMenuBar.add(menu);
            setMenuBar(mainMenuBar);
            MenuAction mac = new MenuAction();
            addMenuItem.addActionListener(mac);
            addSameItem.addActionListener(mac);
            saveMenuItem.addActionListener(mac);
            deleteMenuItem.addActionListener(mac);
            backMenuItem.addActionListener(mac);
            tests.addItemListener(new TestListener());
            buildForm(false);
            checkScore();
	}
	
	public void dispose()
	{
            if (!editing)
            {
		if (depth.getText().trim().length()>0) addTest();
            }
            else updateTest();
            pframe.saveWO();
            super.dispose();
	}
	
	void addTest()
	{
            System.out.println("Add Test.");
            double dpth = 0;
            String tScore = testScore.getSelectedItem();
            if (tScore==null) tScore = "";
            boolean noFail = false;
            if (depth.getText().trim().length() > 0) dpth = new Double(depth.getText()).doubleValue();
            if ( tScore.equals("SBN") || tScore.equals("CTN") || tScore.equals("RB7") || tScore.equals("STN") || tScore.equals("ECTX") || tScore.equals("DTN")) noFail=true;
            if ( (( depth.getText().trim().length() > 0)&&(dpth>=0)) || noFail)
            {
        	if ( validateForm())
        	{
                    String ntes = "";
	           
	            avscience.ppc.ShearTestResult result = getTestResultFromForm();
	            
	            String name = result.toUIString().trim();
	            boolean hasTest = false;
	            for ( int i=0; i<tests.getItemCount(); i++ )
	            {
	            	String tn = tests.getItem(i);
	            	tn=tn.trim();
	            	if ( tn.equals(name))
	            	{ 
	            		hasTest=true;
	            		break;
	            	}
	            }
	            if ( !hasTest )
	            {
	                tests.add(name);
	              	pframe.getPit().addShearTestResult(result);
	              	displayMsg("Stability Test Added.");
	            }
	        }
            }
            else
	    {
	    	displayMsg("Please enter a depth.");
	    }
	    clearForm();
	}
	
	public boolean validateForm()
	{
            boolean valid=true;
            String tScore = testScore.getSelectedItem();
            
            /// check CT scores for CT test type
            if ( tScore.equals("CTE") && ctscore.getText().trim().equals("0"))
            {
        	valid=false;
         	displayMsg("CTE score must be 1-10");
         	return false;
            }
        
            if ( tScore.equals("CTM"))
            {
         	int ct = new Integer(ctscore.getText()).intValue();
         	if ( ct < 11 )
         	{
         		valid=false;
         		displayMsg("CTM score must be 11-20");
         		return false;
         	}
            }
         
            if ( tScore.equals("CTH"))
            {
         	int ct = new Integer(ctscore.getText()).intValue();
         	if ( ct < 21 )
         	{
         		valid=false;
         		displayMsg("CTH score must be 21-30");
         		return false;
           	}
            }
            //// check DT Score for DT test type
            if ( tScore.equals("DTE") && dtscore.getText().trim().equals("0"))
            {
        	valid=false;
         	displayMsg("DTE score must be 1-10");
         	return false;
            }
        
            if ( tScore.equals("DTM"))
            {
         	int ct = new Integer(dtscore.getText()).intValue();
         	if ( ct < 11 )
         	{
         		valid=false;
         		displayMsg("DTM score must be 11-20");
         		return false;
         	}
            }
         
            if ( tScore.equals("DTH"))
            {
         	int ct = new Integer(dtscore.getText()).intValue();
         	if ( ct < 21 )
         	{
         		valid=false;
         		displayMsg("DTH score must be 21-30");
         		return false;
           	}
            }
           
            return true;
        }
	
	avscience.ppc.ShearTestResult getTestResultFromForm()
	{
            String desc = testType.getSelectedItem();
            String code = ShearTests.getInstance().getCode(desc);
            String score = testScore.getSelectedItem();
            String quality = testQ.getSelectedItem();
            String character=fractureChar.getSelectedItem();
            String fcat = user.fractureCat;
            boolean noFail = false;
            if ( score.equals("SBN") || score.equals("CTN") || score.equals("RB7") || score.equals("STN") || score.equals("ECTX") || score.equals("DTN") ) noFail=true;
            if ( noFail ) quality = " ";
            if (score.equals("ECTN")) quality = " ";
            String dpth = depth.getText();
            System.out.println("Depth: "+dpth+" nofail: "+noFail);
            if (noFail) dpth=" ";
            String dunits = user.getDepthUnits();
            String comments = notes.getText();
            String cscore = ctscore.getText();
            String dscore = dtscore.getText();
            String escore = ecscore.getText();
            String numTaps = numOfTaps.getText();
            if ( score.equals("ECTP") && escore.equals("0")) score = "ECTPV";
            if ( score.equals("ECTP") && escore.trim().length()<1) score = "ECTPV";
            avscience.ppc.ShearTestResult result = new avscience.ppc.ShearTestResult(code, score, quality, dpth, dunits, comments, cscore, dscore, escore, character, fcat);
            result.numberOfTaps=numTaps;
            if ( score.contains("RB"))
            {
                    if (!(score.equals("RB7"))) result.releaseType=releaseType.getSelectedItem();
            }
            if (code.equals("PST"))
            {
                    result.lengthOfCut=lengthOfCut.getText();
                    result.lengthOfColumn=lengthOfColumn.getText();
                    result.code="PST";
            }
        
            return result;
	}
	
	void editTest()
	{
            clearForm();
            String tname = tests.getSelectedItem();
            if (( tname!=null ) && ( tname.trim().length() > 0 ) )
            {
		pframe.getPit().setCurrentEditTest(tname);
		avscience.ppc.ShearTestResult result = pframe.getPit().getShearTestResult(tname);
		if ( result!=null )
		{
                    buildForm(true);
                    popForm(result);
		}
            }
            checkScore();
	}
	
	void updateTest()
	{
            avscience.ppc.ShearTestResult result = getTestResultFromForm();
            pframe.getPit().updateCurrentTestResult(result);
            buildForm(false);
	}
	
	void deleteTest()
	{
            String s = (String)tests.getSelectedItem();
            pframe.getPit().removeShearTestResult(s); 
            tests.remove(s);
            buildForm(false);
	}
	
	void clearForm()
	{
            depth.setText("");
            notes.setText("");
            ctscore.setText("");
            dtscore.setText("");
            ecscore.setText("");
            numOfTaps.setText("");
	}
	
	void popForm(ShearTestResult result)
	{
            System.out.println("code:: "+result.getCode());
            String type = ShearTests.getInstance().getShearTestByCode(result.getCode()).getType();
            testType.select(type);
            setTestType();
            testScore.select(result.getScore());
            ctscore.setScore(result.getScore());
            ctscore.setText(result.getCTScore());
            dtscore.setScore(result.getScore());
            dtscore.setText(result.getDTScore());
            releaseType.select(result.getReleaseType());
            testQ.select(result.getQuality());
            depth.setText(result.getDepth());
            notes.setText(result.getComments());
            lengthOfCut.setText(result.lengthOfCut);
            lengthOfColumn.setText(result.lengthOfColumn);
            numOfTaps.setText(result.numberOfTaps);
            String escore = result.getECScore();
            if ( escore.trim().length() > 0 )
            {
                ecscore.setVisible(true);
                ecscore.setText(escore);
            }
            fractureChar.select(result.character);
        }
	
	void checkScore()
	{
            String score = testScore.getSelectedItem();
            boolean noFail = false;
            if ( score.equals("SBN") || score.equals("CTN") || score.equals("DTN") || score.equals("RB7") || score.equals("STN") || score.equals("ECTX")) noFail=true;
            if ( (score.trim().equals("ECTX")) || noFail)
            {
                testQ.setVisible(false);
		sq.setVisible(false);
		fc.setVisible(false);
		fractureChar.setVisible(false);
		ecscore.setVisible(false);
		ctscore.setVisible(false);
                dtscore.setVisible(false);
            }
            else 
            {
	    	if ( user.fractureCat.equals("Fracture Character"))
	    	{
                    fc.setVisible(true);
                    fractureChar.setVisible(true);
                    testQ.setVisible(false);
                    sq.setVisible(false);
	    	}
	    	else
	    	{
                    fc.setVisible(false);
                    fractureChar.setVisible(false);
                    if ( user.fractureCat.equals("Shear Quality"))
                    {
	    		testQ.setVisible(true);
	    		sq.setVisible(true);
                    }
	    	}
    		if ( score.contains("CT"))
    		{
                    if ((score.equals("CTV")) || (score.equals("CTN"))) ctscore.setVisible(false);
                    else ctscore.setVisible(true);
    		}
                if ( score.contains("DT"))
    		{
                    if ((score.equals("DTV")) || (score.equals("DTN"))) dtscore.setVisible(false);
                    else dtscore.setVisible(true);
    		}
    		if ( score.contains("ECTP")) ctscore.setVisible(false);
    	}
         
        if ( score.trim().equals("ECTN")) 
        {
            numOfTaps.setVisible(true);
            ctscore.setVisible(false);
            testQ.setVisible(false);
        }
        else numOfTaps.setVisible(false);
   
    	if (testScore.getSelectedItem().trim().equals("ECTP")) 
    	{
            ecscore.setVisible(true); 
            ctscore.setVisible(false);
            dtscore.setVisible(false);
    	}
    	else ecscore.setVisible(false);
    	
    	if ( noFail ) depth.setVisible(false);
        else depth.setVisible(true);
    	
    	if (!testType.getSelectedItem().trim().equals("Extended Column Test"))ecscore.setVisible(false); 
    	if (testType.getSelectedItem().trim().equals("Rutschblock Test"))
    	{
            if ( noFail )
            {
                rtl.setVisible(false);
    		releaseType.setVisible(false);	
            }
            else
            {
    		rtl.setVisible(true);
    		releaseType.setVisible(true);
            }
    	}
    	if ( testType.getSelectedItem().trim().equals("Propogation Saw Test"))
    	{
            lengthOfCut.setVisible(true);
            lengthOfColumn.setVisible(true);
            fc.setVisible(false);
            fractureChar.setVisible(false);
            testQ.setVisible(false);
            sq.setVisible(false);
    	}
    	else
    	{
            lengthOfCut.setVisible(false);
            lengthOfColumn.setVisible(false);
    	}
    	setCTScore();
        setDTScore();
    	repaint();
    }
	
    private void buildMenu(boolean edit)
    {
        menu.removeAll();
        if (!edit) menu.add(addMenuItem);
        else
        {
            menu.add(addSameItem);
            menu.add(saveMenuItem);
            menu.add(deleteMenuItem);
            menu.add(backMenuItem);
        }
   }
    
    void setTestType()
    {
	String type=testType.getSelectedItem();
	if ( type.trim().equals("Compression Test")) ctscore.setVisible(true);
	else ctscore.setVisible(false);
        if ( type.trim().equals("Deep Tap Test")) dtscore.setVisible(true);
	else dtscore.setVisible(false);
	if ( type.trim().equals("Propogation Saw Test")) 
	{
            ts.setText("Data Code");
            fc.setVisible(false);
            testQ.setVisible(false);
            sq.setVisible(false);
            fractureChar.setVisible(false);
            lengthOfCut.setVisible(true);
            lengthOfColumn.setVisible(true);
	}
	else 
	{
            ts.setText("Stability Test Score");
            fc.setVisible(true);
            fractureChar.setVisible(true);
            lengthOfCut.setVisible(false);
            lengthOfColumn.setVisible(false);
	}
		
	if ( testType.getSelectedItem().trim().equals("Rutschblock Test"))
    	{
            rtl.setVisible(true);
            releaseType.setVisible(true);
    	}
    	else 
    	{
            rtl.setVisible(false);
            releaseType.setVisible(false);
    	}
		
        testScore.removeAll();
	String[] scores = ShearTests.getInstance().getShearTest(type).getScores();
	for ( int i=0; i<scores.length; i++ )
	{
            testScore.add(scores[i]);
	}
      }
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
             public void windowClosing(java.awt.event.WindowEvent event)
            {
		Object object = event.getSource();
		if (object == TestFrame.this ) TestFrame.this.dispose();
            }
	}
	
	void setCTScore()
	{
            String score = testScore.getSelectedItem();
            ctscore.setScore(score);
	}
        
        void setDTScore()
	{
            String score = testScore.getSelectedItem();
            dtscore.setScore(score);
	}
	
	class CTScoreAction implements java.awt.event.ItemListener
	{
            public void itemStateChanged(java.awt.event.ItemEvent event)
            {
		Object object = event.getSource();
		if ( object == testScore ) 
		{
                    String type=testType.getSelectedItem();
                    if ( type.trim().equals("Compression Test"))setCTScore();
                    if ( type.trim().equals("Deep Tap Test"))setDTScore();
                    checkScore();
                }
            }
	}
	
	class TestChangeAction implements java.awt.event.ItemListener
	{
            public void itemStateChanged(java.awt.event.ItemEvent event)
            {
		Object object = event.getSource();
                if ( object == testType ) setTestType();
                checkScore();
            }
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
            public void actionPerformed(java.awt.event.ActionEvent event)
            {
                Object object = event.getSource();
                if ((object == addMenuItem )||(object==add)) addTest();
            
                if ((object==addSame)||(object==addSameItem))
                {
                    addTest();
                    buildForm(false);
                }
                if ( object == deleteMenuItem ) deleteTest();
                if (( object == saveMenuItem ) || (object==save)) updateTest();
                if ( object == backMenuItem ) buildForm(false);
            }
        }
}	
	