
package avscience.desktop;

import java.awt.Component;
import java.awt.Event;
import phil.awt.RF;
import phil.awt.SmartTextField;

public class LimitedTextField extends SmartTextField
{
    int length;
    protected static final int ACTION_KEY_BASE__ = 1000;
    protected static final int INS_HELP_KEY__ = 5;
    protected static final int BACKSPACE_KEY__ = 8;
    protected static final int DEL_KEY__ = 127;
    
    public LimitedTextField(int i)
    {
        super(i);
        length = 20;
        length = i;
    }

    public boolean keyDown(Event event, int i)
    {
        String s = getText().trim();
        boolean flag = false;
        if(i == 9)
        {
            switch(event.modifiers)
            {
            case 0: // '\0'
                try
                {
                    java.awt.Container container = getParent();
                    phil.awt.RetroFocusable retrofocusable = RF.lastComponentOfContainer(container);
                    if(this == retrofocusable)
                    {
                        Component component = (Component)nextComponent();
                        component.requestFocus();
                    } else
                    {
                        nextFocus();
                    }
                }
                catch(Exception exception)
                {
                    nextFocus();
                }
                flag = true;
                break;

            case 1: // '\001'
                prevFocus();
                flag = true;
                break;

            default:
                flag = super.keyDown(event, i);
                break;
            }
        } else
        {
            if(i >= 1000 || i == 5 || i == 9 || i == 8 || i == 127)
                return false;
            if(getCaretPosition() >= length || getText().length() >= length)
                return true;
        }
        return flag;
    }

}
