package avscience.desktop;

import java.awt.*;
import phil.awt.*;
import avscience.pc.TextItemType;
public class DepthTextItem implements TextItemType
{
    public Label label;
    public DepthTextField field = new DepthTextField();
    
    int xspace=6;
    public int colWidth = 200;
    int yspace=24;
    boolean vert;
    
    public void setVisible(boolean visible)
    {
    	label.setVisible(visible);
    	field.setVisible(visible);
    }
    
    public Label getLabel()
    {
    	return label;
    }
    
    public Component getField()
    {
    	return (Component) field;
    }
    
    public DepthTextItem(String lab)
    {
    	this(lab, 0, 0);
    }
    
    public void setLabelWidth(int w)
    {
    	label.setSize(w, 18);
    }
    
    public DepthTextItem(String lab, int x, int y)
    {
        lab = lab.trim();
        this.label = new Label(lab);
        label.setLocation(new Point(x, y));
        int lw = (lab.length()-1)*9;
        if ( lab.length()>20 ) lw=(lab.length()-2)*7;
        label.setSize(lw, 18);
        label.setVisible(true);
        field.setLocation(x+label.getWidth()+xspace, y);
        field.setVisible(true);
        int fwidth = colWidth-label.getWidth()-2*xspace;
        field.setSize(88, 18);
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
    
    public String getText()
    {
        return field.getText();
    }
}