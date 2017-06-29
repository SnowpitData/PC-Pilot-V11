package avscience.desktop;

import java.awt.*;
import phil.awt.*;
import avscience.pc.TextItemType;
public class NoteTextItem implements TextItemType
{
    public Label label;
    
    public LimitedTextField field = new LimitedTextField(137);
    public boolean macos;
    int xspace=6;
    public int colWidth = 200;
    
    public Label getLabel()
    {
    	return label;
    }
    
    public Component getField()
    {
    	return (Component) field;
    }
    
    public NoteTextItem(int x, int y)
    {
        this.label = new Label("Notes:");
        label.setLocation(new Point(x, y));
        int lw = (5)*9;
        if ( lw < 24 ) lw=24;
        
        label.setSize(lw, 18);
        label.setVisible(true);
        field.setLocation(x+lw+xspace, y);
        field.setVisible(true);
        int fwidth = colWidth-label.getWidth()-2*xspace;
        field.setSize(88, 18);
    }
    
    public void setLabel(String s)
    {
    	label.setText(s);
    }
    
    public NoteTextItem(String lab, int x, int y, int size)
    {
    	field = new LimitedTextField(size);
    	
        this.label = new Label("Notes:");
        label.setLocation(new Point(x, y));
        int lw = (lab.length()-1)*9;
        if ( lw < 24 ) lw=24;
        
        label.setSize(lw, 18);
        label.setVisible(true);
        field.setLocation(x+lw+xspace, y);
        field.setVisible(true);
        int fwidth = colWidth-label.getWidth()-2*xspace;
        field.setSize(88, 18);
	}
    public NoteTextItem()
    {
    	this(1, 1);
    }
    
    public void setLocation(int x, int y)
    {
    	label.setLocation(new Point(x, y));
    	field.setLocation(x+label.getWidth()+xspace, y);
    }
    
    public void setSize(int x, int y)
    {
    	field.setSize(x, y);
    }
    
    
    public void setText(String text)
    {
    	try
    	{
        	field.setText(text);
        }
        catch(Exception e){System.out.println(e.toString());}
    }
    
    public String getText()
    {
        return field.getText();
    }
    
    public void setMaxLength(int length)
    {
    	field.setColumns(length);
    }
}