package avscience.pc;

import org.compiere.grid.ed.Calendar;

public class SPCalendar extends org.compiere.grid.ed.Calendar
{
	public SPCalendar(PitHeaderFrame frame)
	{
		super(frame, "Select Date", null, 16, frame);
       	setSize(380, 320);
       	setVisible(true);
	}
}