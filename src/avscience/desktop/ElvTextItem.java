package avscience.desktop;

import java.awt.*;
import phil.awt.*;
import avscience.pc.TextItemType;
public class ElvTextItem implements TextItemType
{
    public Label label;
    public ElevationTextField field = new ElevationTextField("", "ft");
    private int y;
    private int x;
    int xspace=6;
    int yspace=24;
    public int colWidth = 200;
    boolean vert;
    public Label getLabel()
    {
    	return label;
    }
    
    public Component getField()
    {
    	return (Component) field;
    }
    
    
    public ElvTextItem(String lab, String units, int x, int y)
    {
        lab = lab.trim();
        this.label = new Label(lab);
        field = new ElevationTextField("", units);
        this.y=y;
        this.x=x;
        label.setLocation(new Point(x, y));
        int lw = (lab.length()-1)*9;
        label.setSize(lw, 18);
        label.setVisible(true);
        field.setLocation(x+label.getWidth()+xspace, y);
        field.setVisible(true);
        int fwidth = colWidth-label.getWidth()-2*xspace;
        field.setSize(72, 18);
    }
    
    public ElvTextItem(String lab, String units, int x, int y, boolean vert)
    {
    	this.vert=vert;
    	lab = lab.trim();
        this.label = new Label(lab);
        field = new ElevationTextField("", units);
        this.y=y;
        this.x=x;
        label.setLocation(new Point(x, y));
        int lw = (lab.length()-1)*9;
        label.setSize(lw, 18);
        label.setVisible(true);
       // field.setColumns(20);
        field.setLocation(x, y+yspace);
        field.setVisible(true);
        int fwidth = colWidth-label.getWidth()-2*xspace;
        field.setSize(72, 18);
    }
    
    public ElvTextItem(String lab, String units)
    {
    	this(lab, units, 1, 1);
    }
    
    public void setLabelWidth(int w)
    {
    	label.setSize(w, 18);
    	if (vert) field.setLocation(x, y+yspace);
    	else field.setLocation(x+w+4, y);
    }
    
    public void setLocation(int x, int y)
    {
    	label.setLocation(new Point(x, y));
    	field.setLocation(x+label.getWidth()+xspace, y);
    }
    
    public void setFocus()
    {
    	field.requestFocus();
    }
    
    public void setText(String text)
    {
        try
        {
        	field.setText(text);
        }
        catch(Exception e){System.out.println(e.toString());}
    }
    
    public void setVisible(boolean vis)
    {
    	field.setVisible(vis);
    	label.setVisible(vis);
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