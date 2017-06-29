package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import avscience.ppc.User;

public class UserFrame extends Frame
{
	private int width = 300;
	private int height = 380;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem saveMenuItem = new java.awt.MenuItem();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
	TextItem username;
	TextItem email;
	TextItem last;
	TextItem first;
	TextItem phone;
	TextField proftype;
	Checkbox prof;
	Checkbox share;
	Label msg = new Label();
	MainFrame mframe;
	int vspace = 30;
	String origuser="";
	boolean edit;

	void popUserForm()
	{
		String un = mframe.users.getSelectedItem();
		avscience.ppc.User u = mframe.store.getUser(un);
		email.setText(u.getEmail());
		username.setText(u.getName());
		first.setText(u.getFirst());
		last.setText(u.getLast());
		phone.setText(u.getPhone());
		proftype.setText(u.getAffil());
		prof.setState(u.getProf());
		share.setState(u.getShare());
		origuser=u.getName();
	}
	
	User getUserFromForm()
	{
		String prf="false";
		String shre="false";
		
		if ( prof.getState() ) prf="true";
		if ( share.getState() ) shre="true";
		
		avscience.ppc.User user = new User(username.getText().trim(), email.getText().trim(), last.getText().trim(), first.getText().trim(), phone.getText().trim(), prf, proftype.getText(), shre);
		return user;
	}
	
	boolean validateForm()
	{
		boolean valid=true;
		if ( first.getText().trim().length() < 2 ) valid=false;
		if ( last.getText().trim().length() < 2 ) valid=false;
		if ( username.getText().trim().length() < 3 ) valid=false;
		if ( email.getText().trim().length() < 8 ) valid=false;
		if (!(email.getText().indexOf(".") > 0)) valid=false;
		if (!(email.getText().indexOf("@") > 1 )) valid=false;
		return valid;
	}
    
    void displayMsg(String message)
	{
		msg.setSize(240, 20);
		msg.setText(message);
		msg.setVisible(true);
		try
		{
			Thread.sleep(1400);
		}
		catch(Exception e){System.out.println(e.toString());}
		msg.setVisible(false);
	}
    
	void buildForm()
	{
		setLayout(null);
		int x=20;
		int y=50;
		Label l = new Label("User name must be 3 chars. min.");
		l.setSize(200, 18);
		l.setVisible(true);
		l.setLocation(x, y);
		add(l);
		y+=vspace;
		username = new TextItem("User Name", x, y);
	
		y+=vspace;
		email = new TextItem("Email: ", x, y);
		y+=vspace;
		first = new TextItem("First Name: ", x, y);
		y+=vspace;
		last = new TextItem("Last Name: ", x, y);
		y+=vspace;
		phone = new TextItem("Phone: ", x, y);
		y+=vspace;
		prof = new Checkbox("Professional");
		prof.setLocation(x, y);
		prof.setVisible(true);
		prof.setSize(180,20);
		y+=vspace;
		Label plabel = new Label("Professional Affiliation/Type:");
		plabel.setLocation(x, y);
		plabel.setSize(180,20);
		add(plabel);
		y+=vspace;
		proftype = new TextField();
		proftype.setLocation(x, y);
		proftype.setVisible(true);
		proftype.setSize(180,20);
		add(proftype);
		y+=vspace;
		share = new Checkbox("Share data?");
		share.setLocation(x, y);
		share.setVisible(true);
		share.setSize(180, 20);
		share.setState(true);
		add(username);
		add(email);
		add(first);
		add(last);
	
		add(phone);
		add(prof);
		add(proftype);
	///	add(share);
		
		y+=vspace;
		msg.setLocation(x, y);
		msg.setSize(220, 40);
		msg.setVisible(false);
		add(msg);
	}
	
	public void dispose(boolean save)
	{
		super.dispose();
	}
	
	public void dispose()
	{
		if ( validateForm())
		{
			User u = getUserFromForm();
			mframe.store.addUser(u);
			mframe.rebuildList();
			mframe.users.select(u.getName());
			super.dispose();
		}
		else displayMsg("Please fill out the form completely!");
	}
	
	public void saveUser(boolean exit)
	{
		if ( validateForm())
		{
			User u = getUserFromForm();
			if ( !origuser.equals(u.getName()) ) mframe.store.removeUser(origuser);
			mframe.store.addUser(u);
			mframe.rebuildList();
			mframe.users.select(u.getName());
			mframe.defaultUser = u.getName();
			if (!edit) mframe.showPreferencesFrame();
			dispose();
		}
		else if (!exit) displayMsg("Please fill out the form completely!");
		
	}
	
	public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
	
	public UserFrame(MainFrame mframe, boolean edit)
	{
		super("Snow Pilot - Add/Edit User");
	    this.mframe = mframe;
		this.edit=edit;
		setLayout(null);
		
		this.setSize(width, height);
		this.setLocation(120, 120);
		this.setVisible(true);
	
		this.setMaximizedBounds(new Rectangle(width, height));
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		
        buildMenu();
		buildForm();
		if ( edit ) popUserForm();
	}
	
	private void buildMenu()
    {
        saveMenuItem.setLabel("Save User");
        deleteMenuItem.setLabel("Delete User");
    	menu.setLabel("Select..");
        menu.add(saveMenuItem);
        menu.add(deleteMenuItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
        saveMenuItem.addActionListener(new MenuAction());
        deleteMenuItem.addActionListener(new MenuAction());
    }
    
    void deleteUser()
    {
    	String uname = username.getText();
    	System.out.println("deleteUser: "+uname);
    	if ( (uname!=null)&&(uname.trim().length()>0)) 
    	{
    		mframe.store.removeUser(uname);
    		mframe.rebuildList();
    		dispose(false);
    	}
    }
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == UserFrame.this)
			{
				saveUser(true);
				UserFrame.super.dispose();
			}
				
		}
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
            if ( object==saveMenuItem )
            {
            	saveUser(false);
            	
            }
            if ( object==deleteMenuItem )
            {
            	deleteUser();
            }
		}
	}
	void UserFrame_WindowClosing(java.awt.event.WindowEvent event)
	{ 
		dispose();
	}
}	
	