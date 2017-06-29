package avscience.desktop.action;
 
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

public class PasteAction extends AbstractAction
{
	
 protected JTextComponent comp;

 public PasteAction(JTextComponent comp)
{
	super("Paste");
 	this.comp = comp;
}
   
    public void actionPerformed(ActionEvent e)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                comp.paste();
            }
        });
        
    }
    
    public boolean isEnabled()
    {
        if (comp.isEditable() && comp.isEnabled())
        {
            Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
            return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        }
        else return false;

    }
}

    
