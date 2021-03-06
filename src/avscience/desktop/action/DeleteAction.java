package avscience.desktop.action;
 
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class DeleteAction extends AbstractAction
{
    JTextComponent comp;
    
    public DeleteAction(JTextComponent comp)
    {
        super("Delete");
        this.comp = comp;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        comp.replaceSelection(null);
    }
    
    public boolean isEnabled()
    {
        return comp.isEditable()
        && comp.isEnabled()
        && comp.getSelectedText()!=null;
    }
}

