package avscience.pc;

import java.awt.*;
import javax.swing.*;
import java.awt.print.*;
import java.util.*;
import avscience.ppc.PitObs;

public class TextFrame extends Frame implements Printable
{
	String msg;
	static int width=420;
	static int height=160;
	
	JTextArea text = new JTextArea();
	JScrollPane pane = new JScrollPane(text);
	avscience.ppc.PitObs pit;
	
	public void doPaint(Graphics g)
	{
		text.paintAll(g);
	}
	
	public TextFrame(avscience.ppc.PitObs pit)
	{
		super();
		this.msg=pit.getPitNotes();
		this.pit=pit;
		this.setTitle(pit.getName()+"  "+pit.getDate()+"  User:  ");
		setSize(width, height);
		setLocation(860, 220);
		setLayout(null);
		//text.setSize(width-28, height);
		pane.setSize(width-8, height-8);
		pane.setLocation(4, 4);
		pane.setVisible(true);
		//text.setColumns(60);
        text.setLineWrap(true);
        //text.setRows(5);
        text.setWrapStyleWord(true);
		text.setBackground(this.getBackground());
		text.setEditable(false);
		text.setText("\n\n\n\n"+pit.getName()+"  "+pit.getDate()+"  User:  "+pit.getUser().getName()+"\n\n"+msg);
		text.setBackground(Color.WHITE);
		text.setCaretPosition(0);
		text.setMargin(new Insets(2, 8, 2, 6));
		//pane.getVerticalScrollBar().setVisibleAmount(0);
		add(pane);
		//pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
		addWindowListener(new MyWindow());
		requestFocus();
	//	pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
	//	//pane.getVerticalScrollBar().setValue(0);
		
	}
	
	public String[] breakLines(String raw)
	{
		int lnbrk=160;
		int start = 0;
		int end = lnbrk;
		Vector strings = new Vector();
		
		while (raw.length()>end)
		{
			end = getWhitespaceLineBreak(raw, lnbrk);
			//end = lnbrk;
			if (end>start)
			{
				String s = raw.substring(start, end);
				strings.add(s);
				raw=raw.substring(end, raw.length());
				raw.trim();
				if (raw.length()<1) break;
				if (raw.length()<=lnbrk)
				{
					strings.add(raw);
					break;
				}
			}
			//if (start>end)break;
			///start=end;
		}
		String[] lines = new String[strings.size()];
		Enumeration e = strings.elements();
		int i=0;
		while ( e.hasMoreElements() )
		{
			lines[i]=e.nextElement().toString();
			i++;
		}
		return lines;
	}
	
	private int getWhitespaceLineBreak(String raw, int lbrk)
    {
    	if ( raw.length()<lbrk) return raw.length();
    	int brk=0;
    	for ( brk=lbrk+3; brk>lbrk-15; brk--)
    	{
    		if ( raw.length()>=brk)
    		{
    			char c=raw.charAt(brk);
    			if (Character.isWhitespace(c)) return brk;
    		}
    	}
    	if ( brk > raw.length()) return raw.length();
    	if (lbrk > raw.length()) return raw.length();
    	return lbrk;
    }
	
	public String[] getPrintString()
	{
		String[] rs = {" "};
		
		String notes = pit.getPitNotes();
		notes.trim();
		if (notes.length()>126) return breakLines(notes);
		else rs[0]=notes;
		return rs;	
	}
	
	public int print(Graphics g, PageFormat pf, int pi) throws PrinterException 
    {
        
        if (pi >= 1) return Printable.NO_SUCH_PAGE;
        
        Graphics2D g2 = (Graphics2D) g;
        g.translate(100, 0);
        text.paintAll(g);
        return Printable.PAGE_EXISTS;
    }
    
    public void print()
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("SnowPilot"+pit.getName());
        Paper paper = new Paper();
        paper.setSize(width+200, height+40);
        paper.setImageableArea(0, 0, width+240, height+42);
        PageFormat format = new PageFormat();
        format.setPaper(paper);
        
        try
        {
            if(job.printDialog())
            {
                format = job.pageDialog(format);
                job.setPrintable(this, format);
                job.print(); 
            }
        }
        catch(Exception e){System.out.println(e.toString());}
    }
	
	class MyWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object ==TextFrame.this) TextFrame.this.dispose();
		}
	}
}