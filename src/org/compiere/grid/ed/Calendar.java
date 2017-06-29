// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 9/7/2007 8:53:36 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Calendar.java

package org.compiere.grid.ed;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.sql.*;
import java.text.*;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.compiere.plaf.CompiereColor;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.*;
import org.compiere.util.*;
import avscience.pc.TimeFrame;

// Referenced classes of package org.compiere.grid.ed:
//            MinuteModel

public class Calendar extends JDialog
    implements ActionListener, MouseListener, ChangeListener, KeyListener
{

	TimeFrame tf;
    public Calendar(Frame frame)
    {
        this(frame, Msg.getMsg(Env.getCtx(), "Calendar"), null, 15, null);
    }

    public Calendar(Frame frame, String s, Timestamp timestamp, int i, TimeFrame tf)
    {
        super(frame, s, true);
        this.tf=tf;
        m_hasAM_PM = false;
        m_current24Hour = 0;
        m_currentMinute = 0;
        m_setting = true;
        m_abort = true;
        m_lastClick = System.currentTimeMillis();
        m_lastDay = -1;
        mainPanel = new CPanel();
        monthPanel = new CPanel();
        cMonth = new CComboBox();
        cYear = new JSpinner(new SpinnerNumberModel(2000, 1900, 2100, 1));
        mainLayout = new BorderLayout();
        dayPanel = new CPanel();
        dayLayout = new GridLayout();
        monthLayout = new GridBagLayout();
        bNext = new CButton();
        bBack = new CButton();
        timePanel = new CPanel();
        fHour = new CComboBox(getHours());
        lTimeSep = new CLabel();
        fMinute = new JSpinner(new MinuteModel(5));
        cbPM = new JCheckBox();
        lTZ = new JLabel();
        save = new CButton("Save");
        cancel = new CButton("Cancel");
      //  bOK = new CButton();
        timeLayout = new GridBagLayout();
        log.info(timestamp == null ? "null" : timestamp.toString() + " " + i);
        m_displayType = i;
        try
        {
            jbInit();
            setDefaultCloseOperation(2);
        }
        catch(Exception exception)
        {
            log.log(Level.SEVERE, "Calendar", exception);
        }
        loadData(timestamp);
    }

    private void jbInit()
        throws Exception
    {
        CompiereColor.setBackground(this);
        addKeyListener(this);
        mainPanel.setLayout(mainLayout);
        mainLayout.setHgap(2);
        mainLayout.setVgap(2);
        mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        getContentPane().add(mainPanel);
        monthPanel.setLayout(monthLayout);
        monthPanel.add(bBack, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 0), 0, 0));
        monthPanel.add(cYear, new GridBagConstraints(3, 0, 1, 1, 1.0D, 0.0D, 14, 2, new Insets(0, 5, 0, 0), 0, 0));
        monthPanel.add(bNext, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
        monthPanel.add(cMonth, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        mainPanel.add(monthPanel, "North");
        cMonth.addActionListener(this);
        cYear.addChangeListener(this);
        bBack.setIcon(Env.getImageIcon("Parent16.gif"));
        bBack.setMargin(new Insets(0, 0, 0, 0));
        bBack.addActionListener(this);
        bNext.setIcon(Env.getImageIcon("Detail16.gif"));
        bNext.setMargin(new Insets(0, 0, 0, 0));
        bNext.addActionListener(this);
        dayPanel.setLayout(dayLayout);
        dayLayout.setColumns(7);
        dayLayout.setHgap(2);
        dayLayout.setRows(7);
        dayLayout.setVgap(2);
        mainPanel.add(dayPanel, "Center");
        timePanel.setLayout(timeLayout);
        lTimeSep.setText(" : ");
        timePanel.add(fHour, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 13, 2, new Insets(0, 6, 0, 0), 0, 0));
        timePanel.add(lTimeSep, new GridBagConstraints(1, 0, 1, 1, 0.0D, 1.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        timePanel.add(fMinute, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 17, 2, new Insets(0, 0, 0, 0), 0, 0));
        timePanel.add(cbPM, new GridBagConstraints(3, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 5, 0, 0), 0, 0));
        timePanel.add(lTZ, new GridBagConstraints(4, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 4, 0, 0), 0, 0));
     //   timePanel.add(bOK, new GridBagConstraints(5, 0, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 6, 0, 2), 0, 0));
      //  mainPanel.add(timePanel, "East");
        CPanel p = new CPanel();
        p.setLayout(new BorderLayout());
        p.add(timePanel, "Center");
        p.add(cancel, "East");
        p.add(save, "West");
        mainPanel.add(p, "South");
        fHour.addKeyListener(this);
        fHour.addActionListener(this);
        cancel.addActionListener(this);
        save.addActionListener(this);
        ((javax.swing.JSpinner.DefaultEditor)fMinute.getEditor()).getTextField().addKeyListener(this);
        fMinute.addChangeListener(this);
        cbPM.addActionListener(this);
        cbPM.addKeyListener(this);
     //   bOK.setIcon(Env.getImageIcon("Ok16.gif"));
     //   bOK.setMargin(new Insets(0, 1, 0, 1));
     //   bOK.addActionListener(this);
    }

    protected void processWindowEvent(WindowEvent windowevent)
    {
        super.processWindowEvent(windowevent);
        if(windowevent.getID() == 200)
            if(m_displayType == 24)
                fHour.requestFocus();
            else
            if(m_today != null)
                m_today.requestFocus();
    }

    public void loadData(Timestamp timestamp)
    {
    	System.out.println("Load data: ");
        m_calendar = new GregorianCalendar(Language.getLoginLanguage().getLocale());
        if(timestamp == null)   m_calendar.setTimeInMillis(System.currentTimeMillis());
        else  m_calendar.setTime(timestamp);
        m_firstDay = m_calendar.getFirstDayOfWeek();
        java.util.Locale locale = Language.getLoginLanguage().getLocale();
        SimpleDateFormat simpledateformat = (SimpleDateFormat)DateFormat.getDateInstance(1, locale);
        SimpleDateFormat simpledateformat1 = (SimpleDateFormat)DateFormat.getTimeInstance(3, locale);
        m_hasAM_PM = simpledateformat1.toPattern().indexOf('a') != -1;
        if(m_hasAM_PM)
            cbPM.setText(simpledateformat1.getDateFormatSymbols().getAmPmStrings()[1]);
        else
            cbPM.setVisible(false);
        m_currentYear = m_calendar.get(1);
        cYear.setEditor(new javax.swing.JSpinner.NumberEditor(cYear, "0000"));
        cYear.setValue(new Integer(m_currentYear));
        String as[] = simpledateformat.getDateFormatSymbols().getMonths();
        for(int i = 0; i < as.length; i++)
        {
            KeyNamePair keynamepair = new KeyNamePair(i + 1, as[i]);
            if(!as[i].equals(""))
                cMonth.addItem(keynamepair);
        }

        m_currentMonth = m_calendar.get(2) + 1;
        cMonth.setSelectedIndex(m_currentMonth - 1);
        String as1[] = simpledateformat.getDateFormatSymbols().getShortWeekdays();
        for(int j = m_firstDay; j < 7 + m_firstDay; j++)
        {
            int l = j > 7 ? j - 7 : j;
            dayPanel.add(createWeekday(as1[l]), null);
        }

        m_days = new CButton[42];
        m_currentDay = m_calendar.get(5);
        for(int k = 0; k < 6; k++)
        {
            for(int i1 = 0; i1 < 7; i1++)
            {
                int j1 = k * 7 + i1;
                m_days[j1] = createDay();
                dayPanel.add(m_days[j1], null);
            }

        }

        m_days[m_days.length - 1].setBackground(Color.green);
        m_days[m_days.length - 1].setText("Today");
        m_days[m_days.length - 1].setToolTipText(Msg.getMsg(Env.getCtx(), "Today"));
        m_current24Hour = m_calendar.get(11);
        m_currentMinute = m_calendar.get(12);
        timePanel.setVisible(m_displayType == 16 || m_displayType == 24);
        monthPanel.setVisible(m_displayType != 24);
        dayPanel.setVisible(m_displayType != 24);
        m_setting = false;
        setCalendar();
    }

    private JLabel createWeekday(String s)
    {
        JLabel jlabel = new JLabel(s);
        jlabel.setBorder(BorderFactory.createRaisedBevelBorder());
        jlabel.setHorizontalAlignment(0);
        jlabel.setHorizontalTextPosition(0);
        jlabel.setRequestFocusEnabled(false);
        return jlabel;
    }

    private CButton createDay()
    {
        CButton cbutton = new CButton();
        cbutton.setBorder(BorderFactory.createLoweredBevelBorder());
        cbutton.setHorizontalTextPosition(0);
        cbutton.setMargin(ZERO_INSETS);
        cbutton.addActionListener(this);
        cbutton.addMouseListener(this);
        cbutton.addKeyListener(this);
        return cbutton;
    }

    private Object[] getHours()
    {
        java.util.Locale locale = Language.getLoginLanguage().getLocale();
        SimpleDateFormat simpledateformat = (SimpleDateFormat)DateFormat.getTimeInstance(3, locale);
        m_hasAM_PM = simpledateformat.toPattern().indexOf('a') != -1;
        Object aobj[] = new Object[m_hasAM_PM ? 12 : 24];
        if(m_hasAM_PM)
        {
            aobj[0] = "12";
            for(int i = 1; i < 10; i++)
                aobj[i] = " " + String.valueOf(i);

            for(int j = 10; j < 12; j++)
                aobj[j] = String.valueOf(j);

        } else
        {
            for(int k = 0; k < 10; k++)
                aobj[k] = "0" + String.valueOf(k);

            for(int l = 10; l < 24; l++)
                aobj[l] = String.valueOf(l);

        }
        return aobj;
    }

    private void setCalendar()
    {
    	System.out.println("setCalendar");
        if(m_setting)
            return;
        m_setting = true;
        cMonth.setSelectedIndex(m_currentMonth - 1);
        cYear.setValue(new Integer(m_currentYear));
        m_setting = false;
        m_calendar.set(m_currentYear, m_currentMonth - 1, 1);
        int i = m_calendar.get(7);
        int j = m_calendar.getActualMaximum(5);
        i -= m_firstDay;
        if(i < 0)
            i += 7;
        j += i - 1;
        int k = 1;
        for(int l = 0; l < m_days.length - 1; l++)
            if(l >= i && l <= j)
            {
                if(m_currentDay == k)
                {
                    m_days[l].setBackground(Color.blue);
                    m_days[l].setForeground(Color.yellow);
                    m_today = m_days[l];
                    m_today.requestFocus();
                } else
                {
                    m_days[l].setBackground(Color.white);
                    m_days[l].setForeground(Color.black);
                }
                m_days[l].setText(String.valueOf(k++));
                m_days[l].setReadWrite(true);
            } else
            {
                m_days[l].setText("");
                m_days[l].setReadWrite(false);
                m_days[l].setBackground(CompierePLAF.getFieldBackground_Inactive());
            }

        boolean flag = m_current24Hour > 12;
        int i1 = m_current24Hour;
        if(flag && m_hasAM_PM)
            i1 -= 12;
        if(i1 < 0 || i1 >= fHour.getItemCount())
            i1 = 0;
        fHour.setSelectedIndex(i1);
        int j1 = m_calendar.get(12);
        fMinute.setValue(new Integer(j1));
        cbPM.setSelected(flag);
        TimeZone timezone = m_calendar.getTimeZone();
        lTZ.setText(timezone.getDisplayName(timezone.inDaylightTime(m_calendar.getTime()), 0));
        m_calendar.set(m_currentYear, m_currentMonth - 1, m_currentDay, m_current24Hour, m_currentMinute, 0);
        m_calendar.set(14, 0);
    }

    private void setTime()
    {
    	System.out.println("setTime()");
        int i = fHour.getSelectedIndex();
        m_current24Hour = i;
        if(m_hasAM_PM && cbPM.isSelected())  m_current24Hour += 12;
        if(m_current24Hour < 0 || m_current24Hour > 23) m_current24Hour = 0;
        Integer integer = (Integer)fMinute.getValue();
        m_currentMinute = integer.intValue();
        if(m_currentMinute < 0 || m_currentMinute > 59)
         m_currentMinute = 0;
    }
    
    

    public Timestamp getTimestamp()
    {
    	System.out.println("getTimestamp()");
        m_calendar.set(m_currentYear, m_currentMonth - 1, m_currentDay, m_current24Hour, m_currentMinute, 0);
        m_calendar.set(14, 0);
        long l = m_calendar.getTimeInMillis();
        if(m_displayType == 15)
            l = (new Date(l)).getTime();
        else
        if(m_displayType == 24)
            l = (new Time(l)).getTime();
        return new Timestamp(l);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
    	if (actionevent.getSource()==cancel)
    	{
    		hide();
    		return;
    	}
    	if (actionevent.getSource()==save)
    	{
    	//	setTime();
    		System.out.println("Save...");
    		tf.updateEstDate();
    		hide();
    		return;
    	}
    	if (actionevent.getSource()==fHour)
    	{
    		setTime();
            return;
    	}
        if(m_setting)
            return;
        setTime();
       /* if(actionevent.getSource() == bOK)
        {
            m_abort = false;
            dispose();
            return;
        }*/
        if(actionevent.getSource() == bBack)
        {
            if(--m_currentMonth < 1)
            {
                m_currentMonth = 12;
                m_currentYear--;
            }
            m_lastDay = -1;
        } else
        if(actionevent.getSource() == bNext)
        {
            if(++m_currentMonth > 12)
            {
                m_currentMonth = 1;
                m_currentYear++;
            }
            m_lastDay = -1;
        } else
        if(actionevent.getSource() instanceof JButton)
        {
            JButton jbutton = (JButton)actionevent.getSource();
            String s = jbutton.getText();
            if(s.equals("Today"))
            {
                m_calendar.setTime(new Timestamp(System.currentTimeMillis()));
                m_currentDay = m_calendar.get(5);
                m_currentMonth = m_calendar.get(2) + 1;
                m_currentYear = m_calendar.get(1);
            } else
            if(s.length() > 0)
            {
                m_currentDay = Integer.parseInt(s);
                long l = System.currentTimeMillis();
                if(m_currentDay == m_lastDay && l - m_lastClick < 1000L)
                {
                    m_abort = false;
                    dispose();
                    return;
                }
                m_lastClick = l;
                m_lastDay = m_currentDay;
            }
        } else
        if(actionevent.getSource() == cbPM)
        {
            setTime();
            m_lastDay = -1;
            return;
        } else
        {
            m_currentMonth = cMonth.getSelectedIndex() + 1;
            m_lastDay = -1;
        }
        
        
        	
        setCalendar();
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        if(m_setting)
            return;
        if(changeevent.getSource() == fMinute)
        {
            setTime();
            return;
        } else
        {
            m_currentYear = ((Integer)cYear.getValue()).intValue();
            m_lastDay = -1;
            setCalendar();
            return;
        }
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(mouseevent.getClickCount() == 2)
        {
            m_abort = false;
            dispose();
        }
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void keyReleased(KeyEvent keyevent)
    {
        if(keyevent.getSource() instanceof JButton)
        {
            if(keyevent.getKeyCode() == 34)
            {
                if(++m_currentMonth > 12)
                {
                    m_currentMonth = 1;
                    m_currentYear++;
                }
                setCalendar();
                return;
            }
            if(keyevent.getKeyCode() == 33)
            {
                if(--m_currentMonth < 1)
                {
                    m_currentMonth = 12;
                    m_currentYear--;
                }
                setCalendar();
                return;
            }
            byte byte0 = 0;
            if(keyevent.getKeyCode() == 39)
                byte0 = 1;
            else
            if(keyevent.getKeyCode() == 37)
                byte0 = -1;
            else
            if(keyevent.getKeyCode() == 38)
                byte0 = -7;
            else
            if(keyevent.getKeyCode() == 40)
                byte0 = 7;
            if(byte0 != 0)
            {
                System.out.println(m_calendar.getTime() + "  offset=" + byte0);
                m_calendar.add(6, byte0);
                System.out.println(m_calendar.getTime());
                m_currentDay = m_calendar.get(5);
                m_currentMonth = m_calendar.get(2) + 1;
                m_currentYear = m_calendar.get(1);
                setCalendar();
                return;
            }
            actionPerformed(new ActionEvent(keyevent.getSource(), 1001, ""));
        }
        if(keyevent.getKeyCode() == 10)
        {
            m_abort = false;
            setTime();
            dispose();
            return;
        } else
        {
            setTime();
            m_lastDay = -1;
            return;
        }
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    public void keyPressed(KeyEvent keyevent)
    {
    }

    static Class _mthclass$(String s)
    {
    	try
    	{
    		return Class.forName(s);
    	}
        catch(Exception exception)
        {
        	System.out.println(exception.toString());
        }
        
        return null;
    }

    

    private int m_displayType;
    private GregorianCalendar m_calendar;
    private boolean m_hasAM_PM;
    private CButton m_days[];
    private CButton m_today;
    private int m_firstDay;
    private int m_currentDay;
    private int m_currentMonth;
    private int m_currentYear;
    private int m_current24Hour;
    private int m_currentMinute;
    private boolean m_setting;
    private boolean m_abort;
    private long m_lastClick;
    private int m_lastDay;
    private static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);
    private static CLogger log;
    private CPanel mainPanel;
    private CPanel monthPanel;
    private CComboBox cMonth;
    private JSpinner cYear;
    private BorderLayout mainLayout;
    private CPanel dayPanel;
    private GridLayout dayLayout;
    private GridBagLayout monthLayout;
    private CButton bNext;
    private CButton bBack;
    private CButton cancel;
    private CButton save;
    private CPanel timePanel;
    private CComboBox fHour;
    private CLabel lTimeSep;
    private JSpinner fMinute;
    private JCheckBox cbPM;
    private JLabel lTZ;
   // private CButton bOK;
    private GridBagLayout timeLayout;

    static 
    {
        log = CLogger.getCLogger(org.compiere.grid.ed.Calendar.class);
    }
}