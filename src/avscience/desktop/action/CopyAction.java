package avscience.desktop.action;
 
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class CopyAction extends AbstractAction
{
    JTextComponent comp;
    
    public CopyAction(JTextComponent comp)
    {
        super("Copy");
        this.comp = comp;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        comp.copy();
    }
    
    public boolean isEnabled()
    {
        return comp.isEnabled()
        && comp.getSelectedText()!=null;
    }
}
