package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import avscience.ppc.User;

public class TestSearchFrame extends Frame
{
	Choice operator = new Choice();
	Choice testType = new Choice();
	List testScore = new List();
	List testQ = new List();
	Label ttype;
	DepthTextItem depth;
	Choice depthOp = new Choice();
	Choice ctOp = new Choice();
	ElvTextItem ctscore;
	MainFrame mf;
	avscience.ppc.User user;
	int width = 282;
	int height = 420;
	
	
	public TestSearchFrame(PitSearchFrame psf)
	{
		super("Filter by stability test");
		this.mf = psf.mf;
		user = mf.getUser();
		this.setSize(width, height);
		this.setLayout(null);
		this.addWindowListener(new SymWindow());
		initControls();
		buildForm();
	}
	
	void buildForm()
	{
		removeAll();
		int ys=56;
    	int x=24;
    	int y=ys;
    	int vspace=26;
    	Label ul = new Label("User "+user.getName());
    	ul.setSize(160, 18);
    	ul.setLocation(x, y);
    	add(ul);
    	y+=vspace;
    	Label ol = new Label("Operator");
    	ol.setSize(80, 18);
    	ol.setLocation(x, y);
    	add(ol);
    	operator.setLocation(x+82, y);
    	add(operator);
    	y+=vspace;
    	Label sst = new Label("Select Stability Test Type");
    	sst.setSize(240, 18);
    	sst.setLocation(x, y);
    	add(sst);
    	y+=vspace;
    	testType.setLocation(x, y);
    	testType.setSize(180, 20);
    	add(testType);
    	y+=vspace;
    	Label ts = new Label("Test Score");
    	ts.setSize(200, 18);
    	ts.setLocation(x, y);
    	add(ts);
    	y+=vspace;
    	testScore=new List();
    	testScore.setLocation(x, y);
    	testScore.setSize(84, 60);
    	testScore.setMultipleMode(true);
    	String[] tss = ShearTests.getInstance().getShearTest(testType.getSelectedItem()).getScores();
    	for ( int i=0; i<tss.length; i++ )
    	{
    		testScore.add(tss[i]);
    	}
    	add(testScore);
    	y+=3*vspace;
    	
    	Label sq = new Label("Shear Quality");
    	sq.setLocation(x, y);
    	sq.setSize(180, 20);
    	add(sq);
    	y+=vspace;
    	String[] qls = new String[3];
    	
    	qls[0]="Q1";
    	qls[1]="Q2";
    	qls[2]="Q3";
    	testQ=new List();
    	testQ.setMultipleMode(true);
    	testQ.setLocation(x, y);
    	testQ.setSize(48, 40);
    	
    	for ( int i=0; i<qls.length; i++ )
    	{
    		testQ.add(qls[i]);
    	}
    	add(testQ);
    	//testQ.select(2);
    	y+=2*vspace;
    	ctscore = new ElvTextItem("CT Score", "m", x, y);
    	add(ctscore);
    	ctOp.setLocation(x+160, y);
    	add(ctOp);
    	if ( testType.getSelectedItem().trim().equals("Compression Test"))
    	{
    		ctscore.setVisible(true);
    		ctOp.setVisible(true);
    	}
    	else
    	{
    		ctscore.setVisible(false);
    		ctOp.setVisible(false);
    	}
    	y+=vspace;
    	depth = new DepthTextItem("Depth "+user.getDepthUnits(), x, y);
    	add(depth);
    	depthOp.setLocation(x+160, y);
    	add(depthOp);
	}
	
	public StabilityTestAttributes getAttributesFromForm()
	{
		System.out.println("getAttributesFromForm()");
		String op = operator.getSelectedItem();
		String type = testType.getSelectedItem();
		String[] scores = testScore.getSelectedItems();
		String[] ql = testQ.getSelectedItems();
		String units = user.getDepthUnits();
		int dpth =0;
		int ct=0;
		try
		{
			dpth = new Integer(depth.getText()).intValue();
		}
		catch(Exception e){}
		try
		{
			ct = new Integer(ctscore.getText()).intValue();
		}
		catch(Exception e){}
		char dop= depthOp.getSelectedItem().trim().toCharArray()[0];
		
		char cop = ctOp.getSelectedItem().trim().toCharArray()[0];
		return new StabilityTestAttributes(op, type, scores, ql, units, dpth, dop, ct, cop);
	}
	
	void setTestType()
    {
		String type=testType.getSelectedItem();
		if ( type.trim().equals("Compression Test"))
		{
			ctscore.setVisible(true);
		//	ctscore.setText("0");
			ctOp.setVisible(true);
		}
		else
		{
			ctscore.setVisible(false);
			ctOp.setVisible(false);
		}
		testScore.removeAll();
		String[] scores = ShearTests.getInstance().getShearTest(type).getScores();
		for ( int i=0; i<scores.length; i++ )
		{
			testScore.add(scores[i]);
		}
    }
	
	class TestChangeAction implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if ( object == testType ) setTestType();
		}
	}
	
	void initControls()
	{
		testType.addItemListener(new TestChangeAction());
    	String[] types = ShearTests.getInstance().getShearTestDescriptions();
    	for ( int i=0; i<types.length; i++ )
    	{
    		testType.add(types[i]);
    	}
    	String[] ops = {"=", "<", ">"};
    	for ( int i = 0; i < ops.length; i++ )
    	{
    		depthOp.add(ops[i]);
    		ctOp.add(ops[i]);
    	}
    	operator.add("OR");
    	operator.add("AND");
	}
	
	public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == TestSearchFrame.this )
			{
				TestSearchFrame.this.dispose();
			}
		}
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
		}
	}
}