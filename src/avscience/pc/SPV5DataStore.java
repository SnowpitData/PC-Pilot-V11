package avscience.pc;

import java.util.*;
import avscience.ppc.*;

public class SPV5DataStore implements java.io.Serializable
{
    private static final SPV5DataStore instance  = new SPV5DataStore();
    private java.util.Hashtable<String, String> Pits = new java.util.Hashtable<String, String>();
    private java.util.Hashtable<Integer, String>  PitIndex = new java.util.Hashtable<Integer, String>();
    private java.util.Hashtable<Integer, String> OccIndex = new java.util.Hashtable();
    private java.util.Hashtable<String, String> Occs = new java.util.Hashtable<String, String>();
    private java.util.Hashtable<String, String> Users = new java.util.Hashtable<String, String>();
    private java.util.Hashtable<String, String> Locations = new java.util.Hashtable<String, String>();
    String username="";
    
    public static SPV5DataStore getInstance()
	{
		return instance;
	}
	
  	private SPV5DataStore()
  	{
            super();
            Properties props = System.getProperties();
            username = (String) props.get("user.name");
  	}
  	
  	
  	public java.util.Vector getPits()
  	{
  		java.util.Enumeration e = Pits.keys();
  		java.util.Vector v = new java.util.Vector();
  		while (e.hasMoreElements())
  		{
  			String serial = (String) e.nextElement();
	  		avscience.ppc.PitObs pit = getPit(serial);
	  		v.add(pit);
	  	}
  		return v;
  	}
  	
  	public java.util.Enumeration getOccs()
  	{
  		java.util.Enumeration e = Occs.keys();
  		java.util.Vector v = new java.util.Vector();
  		while (e.hasMoreElements())
  		{
  			String serial = (String) e.nextElement();
	  		avscience.ppc.AvOccurence occ = getOcc(serial);
	  		v.add(occ);
	  	}
  		return v.elements();
  	}
  	
  	
  	
  	public void addPit(avscience.ppc.PitObs pit)
  	{	   
            if ( pit!=null )
            {
                String serial = pit.getSerial();
		if ((serial==null) || (serial.trim().length()<1))
		{
                    serial = getNewSerial();
                    pit.setSerial(serial);
		}
		Pits.put(serial, pit.toString());
            }
  	}
        
  	public void addLocation(Location l)
  	{
  		Locations.put(l.getName().trim(), l.toString());
  	}
  	
  	public Location getLocation(String name)
  	{
  		String s = null;
  		if ((name!=null) && (name.trim().length()>0)) s = Locations.get(name).toString();
  		Location l = null;
  		if (s!=null) 
                try
                {
                    l = new Location(s);
                }
                catch(Exception e)
                {
                    System.out.println(e.toString());
                }
  		
  		if ( l==null ) l = new Location();
  		return l;
  	}
  	
  	public void removeLocation(String name)
  	{
  		Locations.remove(name);
  	}
  	
    public void addOcc(AvOccurence occ)
  	{
            if (occ!=null)
            {
                String serial = occ.getSerial();
		  		
		if ((serial==null) || (serial.trim().length()<1))
		{
                    serial = getNewSerial();
                    occ.setSerial(serial);
		}
		Occs.put(serial, occ.toString());
		  	
            }
  	}
  	
  	public avscience.ppc.PitObs getPitByName(String name)
  	{
  		if ((name==null) || (name.trim().length()<2)) return null;
  		System.out.println("getPitByName: "+name);
  		java.util.Vector v = getPits();
  		java.util.Enumeration e = v.elements();
  		while ( e.hasMoreElements())
  		{
  			avscience.ppc.PitObs pit = (avscience.ppc.PitObs) e.nextElement();
  			if (pit.getName().equals(name)) return pit;
  		}
  		return null;
  	}
  	
  	public avscience.ppc.AvOccurence getOccByName(String name)
  	{
  		if ((name==null) || (name.trim().length()<2)) return null;
  		java.util.Enumeration e = getOccs();
  		while ( e.hasMoreElements())
  		{
  			avscience.ppc.AvOccurence occ = (avscience.ppc.AvOccurence) e.nextElement();
  			if (occ.getPitName().equals(name)) return occ;
  		}
  		return null;
  	}
  	
  	public avscience.ppc.PitObs getPit(String serial)
  	{
  		if (( serial==null ) || ( serial.trim().length()<2 )) return null;
  		System.out.println("getPit: serial: "+serial);
  		if ( serial!=null )
  		{
	  		String data = (String) Pits.get(serial);
	  		if ((data!=null) && (data.trim().length()>0))
	  		{
                            avscience.ppc.PitObs pit = new avscience.ppc.PitObs();
                            try
                            {
                                pit = new avscience.ppc.PitObs(data);
                            }
                            catch(Exception e)
                            {
                                System.out.println(e.toString());
                            }
                            return pit;
	  		}
	  	}
	  	return null;
  	}
  
  	public avscience.ppc.AvOccurence getOcc(int idx)
  	{
  		String serial = getOccSerial(idx);
  		return getOcc(serial);
  	}
  	
  	public avscience.ppc.PitObs getPit(int index)
  	{
  		System.out.println("getPit idx: "+index);
  		String serial = getSerial(index);
  		return getPit(serial);
  	}
  	
  	public void deletePit(int index)
  	{
  		
  		String serial = getSerial(index);
  		removePit(serial);
  	}
  	
  	public void deleteOcc(int index)
  	{
  		String serial = getOccSerial(index);
  		removeOcc(serial);
  		removePit(serial);
  	}
  	
  	public boolean hasPit(String serial)
  	{
  		if (( serial==null ) || ( serial.trim().length()<2 )) return false;
  		
  		String data = (String) Pits.get(serial);
  		if ( data!=null ) return true;
  		else return false;
  	}
  	
  	public avscience.ppc.AvOccurence getOcc(String serial)
  	{
  		System.out.println("getOcc() "+serial);
  		if (( serial==null ) || ( serial.trim().length()<2 )) return null;
  		
  		String data=null;
  		
  		data = (String) Occs.get(serial);
  		avscience.ppc.AvOccurence pocc = null;
  		if (data==null)
  		{
  			System.out.println("data null.");
  			return null;
  		}
  		else
  		{
  			try
  			{
  				pocc = new avscience.ppc.AvOccurence(data);
  			}
  			catch(Throwable t){System.out.println(t.toString());}
  		}
  		return pocc;
  	}
  	
  	public void removePit(String serial)
  	{
  		if (( serial==null ) || ( serial.trim().length()<2 ));
  		
  		if ( serial!=null )
  		{
	  		System.out.println("removePit: "+serial);
	  		Object o = Pits.remove(serial);
	  		if (o!=null ) System.out.println(serial+" :removed");
	  		else System.out.println("failed to remove pit: "+serial);
	  	}
  	}
  	
  	public void removeOcc(String serial)
  	{
  		if (( serial==null ) || ( serial.trim().length()<2 )) return;
  		
  		if (serial!=null)
  		{
	  		Occs.remove(serial);
	  		Pits.remove(serial);
	  	}
  	}
  	
  	public void addUser(avscience.ppc.User user)
  	{
            Users.put(user.getName(), user.toString());
  	}
  	
  	public avscience.ppc.User getUser(String name)
  	{
  		String s = null;
  		avscience.ppc.User u = null;
  		if ( ( name!=null ) && ( name.trim().length()>0) )
  		{
	  		s =  Users.get(name).toString();
                        try
                        {
                            u = new avscience.ppc.User(s);
                        }
                        catch(Exception e)
                        {
                            System.out.println(e.toString());
                        }
	  		
	  		if ( u==null ) u = new avscience.ppc.User();
	  	} 
  		return u;
    }
    
    public void removeUser(String name)
    {
    	if ( ( name!=null ) && ( name.trim().length()>0) )
  		{
	    	System.out.println("remove User: "+name);
	   
	    	Users.remove(name);	
    	}	
    	
    }
    
    public String[] getUserNames()
    {
    	String[] users = new String[Users.size()];
    	if (( Users!=null ) && ( Users.size()> 0 ))
    	{
    		java.util.Enumeration e = Users.keys();
    		int i=0;
    		while ( e.hasMoreElements() )
    		{
    			users[i]=(String) e.nextElement();
    			i++;
    		}
    	}
    	return users;
    }
    
    public String[] getLocationNames()
    {
    	String[] locations = new String[Locations.size()+1];
    	if (( Locations!=null ) && ( Locations.size()> 0 ))
    	{
    		java.util.Enumeration e = Locations.keys();
    		locations[0]=" ";
    		int i=1;
    		while ( e.hasMoreElements() )
    		{
    			locations[i]=(String) e.nextElement();
    			i++;
    		}
    	}
    	return locations;
    }
    
    java.util.Vector getPits(boolean crowns)
    {
    	java.util.Enumeration e = Pits.keys();
  		java.util.Vector v = new java.util.Vector();
  		while (e.hasMoreElements())
  		{
  		    String serial = (String) e.nextElement();
  		    avscience.ppc.PitObs pit = getPit(serial);
  		    if (!crowns)
  		    {
  		    	if (!pit.getCrownObs()) v.add(pit);
  		    }
  		   
  		}
  		v=sortPitsByTime(v);
  		return v;
    }
    
    public java.util.Vector sortPitsByTime(java.util.Vector pits)
    {
    	boolean sorted = false;
        int length = pits.size();
        int i = 0;
        avscience.ppc.PitObs pit;
        avscience.ppc.PitObs pitInc;

        if (length > 0)
        {
            while (!sorted)
            {
                sorted = true;
                for(i=0; i<length - 1; i++)
                {
                    pit = (avscience.ppc.PitObs) pits.elementAt(i);
                    long time = pit.getTimestamp();
                    pitInc = (avscience.ppc.PitObs) pits.elementAt(i+1);
                    long timeinc = pitInc.getTimestamp();
                  
                    if ( timeinc > time )
                    {
                            pits.setElementAt(pitInc, i);
                            pits.setElementAt(pit, i+1);
                            sorted = false;
                    }
                }
            }
        }
        return pits;
    }
  	
  	public String[] getPitNames(boolean crowns)
  	{
  		System.out.println("getPitNames()");
  		PitIndex = new java.util.Hashtable();
  		java.util.Vector pts = getPits(crowns);
  		String[] pits = new String[pts.size()+1];
  		pits[0]=" ";
  		System.out.println("# of pits: "+pts.size());
        if ((pts!=null) && (pts.size()>0))
        {
      		java.util.Enumeration e = pts.elements();
      		int i = 1;
      		while (e.hasMoreElements())
      		{
                    avscience.ppc.PitObs pit = (avscience.ppc.PitObs)e.nextElement();
                    String serial = pit.getSerial();
      		    pits[i]=pit.getName();
      		    System.out.println("Idx: "+i+" Pit Name: "+pits[i]+" Serial: "+serial);
      		    PitIndex.put(new Integer(i), serial);
      		    i++;
      		}
      	}
  		return pits;
  	}
  	
  	public String[] getPitNames()
  	{
  		System.out.println("getPitNames()");
  		PitIndex = new java.util.Hashtable();
  		java.util.Vector pts = getPits();
  		String[] pits = new String[pts.size()];
  		
        if ((pts!=null) && (pts.size()>0))
        {
      		java.util.Enumeration e = pts.elements();
      		int i = 0;
      		while (e.hasMoreElements())
      		{
                    avscience.ppc.PitObs pit = (avscience.ppc.PitObs)e.nextElement();
                    String serial = pit.getSerial();
      		    pits[i]=pit.getName();
      		    System.out.println("Idx: "+i+" Pit Name: "+pits[i]+" Serial: "+serial);
      		    PitIndex.put(new Integer(i), serial);
      		    i++;
      		}
      	}
  		return pits;
  	}
  	
  	void updatePitIndexes()
  	{
  		System.out.println("updatePitIndexes()");
  		PitIndex = new java.util.Hashtable();
  		java.util.Vector v = getPits();
  		java.util.Enumeration e = v.elements();
  		String[] pits = new String[v.size()];
        if ((pits!=null) && (pits.length>0))
        {
      		int i = 0;
      		while (e.hasMoreElements())
      		{
      		   	avscience.ppc.PitObs pit = (avscience.ppc.PitObs)e.nextElement();
      		   	String serial = pit.getSerial();
      		    pits[i]=pit.getName();
      		    System.out.println("Idx: "+i+" Pit Name: "+pits[i]+" Serial: "+serial);
      		    PitIndex.put(new Integer(i), serial);
      		    i++;
      		}
      	}
  	}
  	
  	public String[] getPitSerials()
  	{
  		getPitNames();
  		String[] serials = new String[PitIndex.size()];
  		if (PitIndex.size()>0)
  		{
  			java.util.Enumeration e = PitIndex.elements();
  			int i=0;
  			while (e.hasMoreElements())
  			{
  				String s = (String) e.nextElement();
  				serials[i]=s;
  				i++;
  			}
  		}
  		return serials;	
  	}
  	
  	public String[] getOccNames()
  	{
  		OccIndex = new java.util.Hashtable();
  		String[] occs = new String[Occs.size()+1];
  		occs[0]=" ";
        if ((Occs!=null) && (Occs.size()>0))
        {
      		java.util.Enumeration e = Occs.keys();
      		int i = 1;
      		while (e.hasMoreElements())
      		{
      		    String serial = (String) e.nextElement();
      		    avscience.ppc.AvOccurence occ = getOcc(serial);
      		    occs[i]=occ.getPitName();
      		    OccIndex.put(new Integer(i), serial);
      		    i++;
      		}
      	}
  		return occs;
  	}
  	
  	public String[] getOccSerials()
  	{
  		getOccNames();
  		String[] serials = new String[OccIndex.size()];
  		if (OccIndex.size()>0)
  		{
  			java.util.Enumeration e = OccIndex.elements();
  			int i=0;
  			while (e.hasMoreElements())
  			{
  				String s = (String) e.nextElement();
  				serials[i]=s;
  				i++;
  			}
  		}
  		return serials;	
  	}
  	
  	private String getOccSerial(int index)
  	{
  		Integer I = new Integer(index);
  		String serial = (String) OccIndex.get(I);
  		return serial;
  	}
  	
  	
  	private String getSerial(int index)
  	{
  		System.out.println("getSerial: idx: "+index);
  		Integer I = new Integer(index);
  		String serial = (String) PitIndex.get(I);
  		System.out.println("serial: "+serial);
  		return serial;
  	}
  	
    public String getNewSerial()
    {
    	System.out.println("getNewSerial()");
    	long time = System.currentTimeMillis();
    	String serial = username+time;
    	while ( Pits.containsKey(serial)) 
    	{
    		time = System.currentTimeMillis();
    		serial = username+time;
    	}
    	System.out.println("serial: "+serial);
    	return serial;
    }
}