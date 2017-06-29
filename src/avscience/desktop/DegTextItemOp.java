package avscience.desktop;

import java.awt.*;
import phil.awt.*;
import avscience.pc.TextItemOp;
public class DegTextItemOp implements TextItemOp
{
    public Label label;
    public DegreeTextField field = new DegreeTextField();
    private int y;
    int xspace=6;
    public int colWidth = 200;
    Choice op;
    
    public Label getLabel()
    {
    	return label;
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
    
    public DegTextItemOp(String lab, int x, int y)
    {
        lab = lab.trim();
        this.label = new Label(lab);
        label.setLocation(new Point(x, y));
        int lw = (lab.length()-1)*9;
        label.setSize(lw, 18);
        label.setVisible(true);
       	this.y=y;
        field.setLocation(x+label.getWidth()+10*xspace, y);
        field.setVisible(true);
        int fwidth = colWidth-label.getWidth()-2*xspace;
        field.setSize(88, 18);
        op = new Choice();
    	String[] ops = {"<", ">", "="};
    	
    	for (int i = 0; i < ops.length; i++)
    	{
    		op.add(ops[i]);
    	}
    	op.setVisible(true);
    	op.setLocation(x+label.getWidth()+xspace, y);
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
    
    public Choice getOperator()
    {
    	return op;
    }
    
    public String getOperatorValue()
    {
    	return op.getSelectedItem();
    }
}