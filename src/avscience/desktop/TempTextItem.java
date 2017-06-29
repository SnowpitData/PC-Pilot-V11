package avscience.desktop;

import java.awt.*;
import phil.awt.*;
import avscience.pc.TextItemType;
public class TempTextItem implements TextItemType
{
    public Label label;
    public TempTextField field;
    
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
    public TempTextItem(String lab, String units, int x, int y)
    {
    	field = new TempTextField(units);
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
    
    public TempTextItem(String lab, String units)
    {
    	this(lab, units, 1, 1);
    }
    
    public void setLocation(int x, int y)
    {
    	label.setLocation(new Point(x, y));
    	field.setLocation(x+label.getWidth()+xspace, y);
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