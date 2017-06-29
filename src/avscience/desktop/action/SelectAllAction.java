package avscience.desktop.action;
 
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

public class SelectAllAction extends AbstractAction
{
	
 protected JTextComponent comp;

 public SelectAllAction(JTextComponent comp)
{
	super("Select All");
 	this.comp = comp;
}
   
    public void actionPerformed(ActionEvent e)
    {
        comp.selectAll();
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                comp.selectAll();
            }
        });
        
    }
    
    public boolean isEnabled()
    {
        return comp.isEnabled() && comp.getText().length()>0;
    }
}

    
