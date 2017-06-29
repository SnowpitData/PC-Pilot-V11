// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextItem.java

package avscience.desktop;

import avscience.pc.TextItemType;
import java.awt.*;
import java.io.PrintStream;

// Referenced classes of package avscience.desktop:
//            LimitedTextField

public class TextItem
    implements TextItemType
{

    public Label getLabel()
    {
        return label;
    }

    public Component getField()
    {
        return field;
    }

    public TextItem(String s, int i, int j)
    {
        field = new LimitedTextField(64);
        xspace = 6;
        colWidth = 200;
        String s1 = System.getProperty("os.name");
        s = s.trim();
        label = new Label(s);
        label.setLocation(new Point(i, j));
        int k = (s.length() - 1) * 9;
        if(k < 24)
            k = 24;
        label.setSize(k, 18);
        label.setVisible(true);
        field.setLocation(i + k + xspace, j);
        field.setVisible(true);
        int l = colWidth - label.getWidth() - 2 * xspace;
        field.setSize(88, 18);
    }

    public TextItem(String s, int i, int j, int k)
    {
        field = new LimitedTextField(64);
        xspace = 6;
        colWidth = 200;
        field = new LimitedTextField(k);
        String s1 = System.getProperty("os.name");
        s = s.trim();
        label = new Label(s);
        label.setLocation(new Point(i, j));
        int l = (s.length() - 1) * 9;
        if(l < 24)
            l = 24;
        label.setSize(l, 18);
        label.setVisible(true);
        field.setLocation(i + l + xspace, j);
        field.setVisible(true);
        int i1 = colWidth - label.getWidth() - 2 * xspace;
        field.setSize(88, 18);
    }

    public TextItem(String s)
    {
        this(s, 1, 1);
    }

    public void setLocation(int i, int j)
    {
        label.setLocation(new Point(i, j));
        field.setLocation(i + label.getWidth() + xspace, j);
    }

    public void setSize(int i, int j)
    {
        field.setSize(i, j);
    }

    public void setText(String s)
    {
        try
        {
            field.setText(s);
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
        }
    }

    public String getText()
    {
        return field.getText();
    }

    public void setMaxLength(int i)
    {
        field.setColumns(i);
    }

    public Label label;
    public LimitedTextField field;
    public boolean macos;
    int xspace;
    public int colWidth;
}
