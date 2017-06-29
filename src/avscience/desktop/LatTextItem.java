package avscience.desktop;

import java.awt.*;
import phil.awt.*;
import avscience.pc.TextItemType;
public class LatTextItem implements TextItemType
{
    public Label label;
    public LatTextField field = new LatTextField();
    
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
    
    
    public LatTextItem(String lab, int x, int y)
    {
        lab = lab.trim();
        this.label = new Label(lab);
        label.setLocation(new Point(x, y));
        int lw = (lab.length()-1)*9;
        label.setSize(lw, 18);
        label.setVisible(true);
        field.setLocation(x+label.getWidth()+xspace, y);
        field.setVisible(true);
        int fwidth = colWidth-label.getWidth()-2*xspace;
        field.setSize(84, 18);
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