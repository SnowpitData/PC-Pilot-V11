package avscience.desktop.action;
 
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class CutAction extends AbstractAction
{
    JTextComponent comp;
    
     public CutAction(JTextComponent comp)
    {
        super("Cut" );
        this.comp = comp;
    }
    public void actionPerformed(ActionEvent e)
    {
        comp.cut();
    }
    public boolean isEnabled()
    {
        return comp.isEditable()
        && comp.isEnabled()
        && comp.getSelectedText()!=null;
    }
}

