package avscience.desktop;

import java.awt.*;
import phil.awt.*;
import avscience.pc.TextItemType;
public class DegTextItem implements TextItemType
{
    public Label label;
    public DegreeTextField field = new DegreeTextField();
    private int y;
    int xspace=6;
    public int colWidth = 200;
    
    public Label getLabel()
    {
    	return label;
    }
    
    public void setVisible(boolean visible)
    {
    	label.setVisible(visible);
    	field.setVisible(visible);
    }
    
    public Component getField()
    {
    	return (Component) field;
    }
    
    public void setLabelWidth(int w)
    {
    	label.setSize(w, 18);
    	field.setLocation(w+22, y);
    }
    
    public DegTextItem(String lab, int x, int y)
    {
        lab = lab.trim();
        this.label = new Label(lab);
        label.setLocation(new Point(x, y));
        int lw = (lab.length()-1)*9;
        label.setSize(lw, 18);
        label.setVisible(true);
       	this.y=y;
        field.setLocation(x+label.getWidth()+xspace, y);
        field.setVisible(true);
        int fwidth = colWidth-label.getWidth()-2*xspace;
        field.setSize(88, 18);
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