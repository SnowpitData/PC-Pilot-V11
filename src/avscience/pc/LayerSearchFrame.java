package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import avscience.ppc.User;

public class LayerSearchFrame extends Frame
{
	private int width = 340;
	private int height = 500;
	Choice operator = new Choice();
	DepthTextItem startDepth;
	DepthTextItem endDepth;
	Choice sDepthOp = new Choice();
	Choice eDepthOp = new Choice();
	List hard = new List();
	Choice sizeOp = new Choice();
	Choice size = new Choice();
	Choice sunits = new Choice();
	
	TypeDisplay type1;
	TypeDisplay type2;
	List waterContent = new List();
	RhoTextItem rho;
	Choice rhoOp = new Choice();
	Button selectGT1;
	Button selectGT2;
	avscience.ppc.User user;
	MainFrame mframe;
	Vector subFrames = new Vector();
	boolean multType;
	
	public LayerSearchFrame(PitSearchFrame pframe)
	{
		super("Filter by Layer");
		this.mframe = pframe.mf;
		user = mframe.getUser();
		setLayout(null);
		initControls();
		setSize(width, height);
		setVisible(true);
		addWindowListener(new SymWindow());
		buildForm();
	}
	
	void initControls()
	{
		hard.setMultipleMode(true);
		waterContent.setMultipleMode(true);
		MenuAction mnac = new MenuAction();
		String[] ops = {">", "<", "="};
	    String[] hardness = Hardness.getInstance().getCodes();
	    String[] sizes = GrainSize.getInstance().getCodes();
	    operator.add("OR");
	    operator.add("AND");
	    
	    sunits.add("mm");
	    sunits.add("cm");
	    for (int i = 0; i < ops.length; i++)
	    {
	    	sDepthOp.add(ops[i]);
	    	eDepthOp.add(ops[i]);
	    	sizeOp.add(ops[i]);
	    	rhoOp.add(ops[i]);
	    }
	    
	    for ( int i=0; i<hardness.length; i++ )
	    {
	    	hard.add(hardness[i]);
	 	}
	 	
	 	String[] wc = WaterContent.getInstance().getCodes();
	 	for ( int i=0; i<wc.length; i++ )
	 	{
	 		waterContent.add(wc[i]);
	 	}
	 	
	 	for (int i =0; i < sizes.length-1; i++ )
	 	{
	 		size.add(sizes[i]);
	 	}
	 	type1 = new TypeDisplay();
		type2 = new TypeDisplay();
		rho = new RhoTextItem("Density "+user.getRhoUnits()+"  ",user.getRhoUnits());
		startDepth = new DepthTextItem("Start Depth "+user.getDepthUnits());
	    endDepth = new DepthTextItem("End Depth "+user.getDepthUnits());
	    selectGT1 = new Button("Select Grain Type 1");
	    selectGT2 = new Button("Select Grain Type 2");
	    selectGT1.setSize(118, 24);
	    selectGT2.setSize(118, 24);
	    selectGT1.addActionListener(mnac);
	    selectGT2.addActionListener(mnac);
	    
	}
	
	public LayerAttributes getAttributesFromForm()
	{
		String op = operator.getSelectedItem();
		int sDepth=-1;
		int eDepth=-1;
		
		try
		{
			sDepth = new Integer(startDepth.getText()).intValue();
		}
		catch(Exception e){}
		
		try
		{
			eDepth = new Integer(endDepth.getText()).intValue();
		}
		catch(Exception e){}
		String du = user.getDepthUnits();
		char sdOp = sDepthOp.getSelectedItem().trim().toCharArray()[0];
		char edOp = eDepthOp.getSelectedItem().trim().toCharArray()[0];
		
		String[] hardness = hard.getSelectedItems();
		String gt1 = type1.getType();
		String gt2 = type2.getType();
		
		float sz=0f;
		try
		{
			sz = new Float(size.getSelectedItem()).floatValue();
		}
		catch(Exception e){}
		String su = sunits.getSelectedItem();
		char szOp = sizeOp.getSelectedItem().trim().toCharArray()[0];
		
		float rh=0f;
		try
		{
			rh = new Float(rho.getText()).floatValue();
		}
		catch(Exception e){}
		char rhop = rhoOp.getSelectedItem().trim().toCharArray()[0];
		
		String[] wc = waterContent.getSelectedItems();
		String rnits = user.getRhoUnits();
		
		return new LayerAttributes(op, sDepth, eDepth, du, sdOp, edOp, hardness, gt1, gt2, su, sz, szOp, rh, rhop, rnits, wc);
	}
	
	void clearSubFrames()
	{
		Enumeration e = subFrames.elements();
		while ( e.hasMoreElements())
		{
			Frame f = (Frame) e.nextElement();
			f.dispose();
		}
	}
	
	
	/*void clearForm()
	{
		startDepth.setText("");
		endDepth.setText("");
		hard1.select(0);
		hard2.select(0);
		hsuffix1.select(0);
		hsuffix2.select(0);
		size1.select(0);
		size2.select(0);
		sizeSuffix1.select(0);
		sizeSuffix2.select(0);
		sunits1.select("mm");
		sunits2.select("mm");
		type1.setType("");
		type2.setType("");
		rho1.setText("");
		rho2.setText("");
		waterContent.select(0);
		notes.setText("");
	}*/
	
	public void dispose()
	{
		clearSubFrames();
		super.dispose();
	}
	
	void buildForm()
	{
		removeAll();
		int startx = 24;
		int ys=56;
    	int x=startx;
    	int y=ys;
    	int vspace=26;
    	
    	Label ol = new Label("Operator:");
    	ol.setSize(90, 18);
    	ol.setLocation(x, y);
    	add(ol);
    	operator.setLocation(x+90, y);
    	add(operator);
    	y+=vspace;
    	startDepth.setLocation(x, y);
    	add(startDepth);
    	sDepthOp.setLocation(x+210, y);
    	add(sDepthOp);
    	y+=vspace;
    	endDepth.setLocation(x, y);
    	add(endDepth);
    	eDepthOp.setLocation(x+210, y);
    	add(eDepthOp);
    	y+=vspace;
    	Label hl = new Label("Hardness");
    	hl.setSize(80, 18);
    	hl.setLocation(x, y);
    	add(hl);
    	y+=vspace;
    	hard.setLocation(x, y);
    	hard.setSize(54, 110);
    	add(hard);
    	y-=vspace;
    	x+=84;
    	Label gt1 = new Label("Grain Type 1");
    	gt1.setSize(84, 18);
    	gt1.setLocation(x, y);
    	add(gt1);
    	type1.setLocation(x+90, y);
    	add(type1);
    	y+=1.4*vspace;
    	selectGT1.setLocation(x, y);
    	add(selectGT1);
    	y+=vspace;
    	Label gt2 = new Label("Grain Type 2");
    	gt2.setSize(84, 18);
    	gt2.setLocation(x, y);
    	add(gt2);
    	type2.setLocation(x+90, y);
    	add(type2);
    	y+=1.4*vspace;
    	selectGT2.setLocation(x, y);
    	add(selectGT2);
    	y+=2*vspace;
    	x=startx;
    	
    	Label gsu = new Label("Grain Size Units");
    	gsu.setSize(92, 18);
    	gsu.setLocation(x, y);
    	add(gsu);
    	sunits.setLocation(x+96, y);
    	add(sunits);
    	y+=vspace;
    	Label gsl = new Label("Grain Size");
    	gsl.setSize(72, 18);
    	gsl.setLocation(x, y);
    	add(gsl);
    //	y+=vspace;
    	size.setLocation(x+74, y);
    	add(size);
    	sizeOp.setLocation(x+132, y);
    	add(sizeOp);
    	y+=vspace;
    	rho.setLocation(x, y);
    	add(rho);
    	rhoOp.setLocation(x+260, y);
    	add(rhoOp);
    	y+=vspace;
    	Label wc = new Label("Water Content");
    	wc.setSize(100, 18);
    	wc.setLocation(x, y);
    	add(wc);
    	y+=vspace;
    	waterContent.setLocation(x, y);
    	waterContent.setSize(84, 72);
    	add(waterContent);
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
			if (object == LayerSearchFrame.this )
			{
				LayerSearchFrame.this.dispose();
			}
		}
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
            
            if ( object == selectGT1 )
            {
            	//if (!multType) subFrames.add(new TypeDisplay(type1));
            	//else subFrames.add(new TypeDisplay(type1)); 
            }
            //if ( object == selectGT2 ) subFrames.add(new TypeDisplay(type2));
            
		}
	}
}	
	