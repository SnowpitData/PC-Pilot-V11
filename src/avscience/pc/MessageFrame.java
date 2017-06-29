package avscience.pc;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class MessageFrame extends Frame
{
	String msg;
	static int width=332;
	static int height=232;
	
	JTextArea text = new JTextArea();
	JScrollPane pane = new JScrollPane(text);
	
	public MessageFrame(String msg)
	{
		super();
		this.msg=msg;
		this.setTitle("Snow Pilot News");
		setSize(width, height);
		setLocation(550, 80);
		setLayout(null);
		pane.setSize(width-6, height-8);
		pane.setLocation(4, 4);
		pane.setVisible(true);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
		text.setBackground(this.getBackground());
		text.setEditable(false);
		text.setText("\n\n"+msg);
		text.setBackground(Color.WHITE);
		text.setCaretPosition(0);
		text.setMargin(new Insets(2, 4, 2, 6));
		add(pane);
		addWindowListener(new MyWindow());
		requestFocus();
		
	}
	
	class MyWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object ==MessageFrame.this) MessageFrame.this.dispose();
		}
	}	
	
}