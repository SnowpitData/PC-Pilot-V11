/**
 * @(#)EditNewsFrame.java
 *
 *
 * @author 
 * @version 1.00 2008/12/21
 */
package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Properties;

public class EditNewsFrame extends Frame
{
	SPTextArea news;
	Button submit =new Button("Submit");
	MainFrame mainFrame;
    public EditNewsFrame(MainFrame mainFrame) 
    {
    	setLayout(null);
    	this.mainFrame=mainFrame;
    	setTitle("Snow Pilot News");
    	setSize(600, 480);
    	buildForm();
    }
    
    public void buildForm()
    {
    	news = new SPTextArea();
        news.setLineWrap(true);
        news.setWrapStyleWord(true);
        news.setSize(500, 320);
        news.setBorder(BorderFactory.createLineBorder(Color.black));
      //  add(notes);
      	JScrollPane pane = new JScrollPane(news);
      	//pane.setLocation(xx, 572);
      	pane.setSize(500, 320);
      	pane.setLocation(50, 50);
      	pane.setVisible(true);
      	add(pane);
      	submit.setSize(132, 42);
      	submit.setLocation(240, 380);
      	addWindowListener(new SymWindow());
      	submit.addActionListener(new SendAction());
      	add(submit);
      	String s = mainFrame.getCurrentNews();
      	news.setText(s);
    }
    
    public void sendData()
    {
    	boolean sent=false;
    	try
        {
            URL url = new URL(mainFrame.server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "editnews");
            String s = URLEncoder.encode(news.getText());
            props.put("news", s);
	        msg.sendGetMessage(props);
	        if (msg!=null) sent=true;
	        if (sent)
	        {
	           OKDialog ok = new OKDialog(mainFrame, true);
	           ok.setLocation(220, 220);
	           ok.setVisible(true);
	        }
        }
        catch(Exception e)
        {
        	System.out.println(e.toString());
        }
            
    }
    
    class SendAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			sendData();
		}
	}
    
    class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == EditNewsFrame.this) EditNewsFrame.this.dispose();
		}
	}
}