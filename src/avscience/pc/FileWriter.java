package avscience.pc;

import avscience.desktop.*;
import avscience.ppc.*;
import avscience.wba.TempProfile;
import avscience.wba.DensityProfile;
import java.io.*;
import java.net.*;
import java.util.*;

public class FileWriter
{
	public static void main(String args[])
	{
		new FileWriter().init();
	}
	
	void init()
	{
		java.util.Enumeration e = null;
		try
		{
			e = getPitList("Karl").elements();
		}
		catch(Exception ex){System.out.println(ex.toString());}
		while (e.hasMoreElements())
		{
			String name = (String) e.nextElement();
			avscience.ppc.PitObs pit = getPit(name);
			writePitToFile(pit);
			
		}
	}
	
	private avscience.ppc.PitObs getPit(String name)
    {
        avscience.ppc.PitObs pit = null;
        try
        {
            URL url = new URL("http://kahrlconsulting.com/avscience/PitServlet");
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "object");
            props.put("TYPE", "PIT");
            props.put("PITNAME", name);
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            pit = (avscience.ppc.PitObs) result.readObject();
            if ( pit == null ) System.out.println("Pit null.");
            System.out.println("Pit: " + pit.getName());
            System.out.println("Detecting OS: ");
            String os_name = System.getProperty( "os.name" );
            if (os_name==null) os_name="NA";
			System.out.println("OS: "+os_name);
            System.out.println("Pit Loc: " + pit.getLocation().getName());
           
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return pit;
    }
	
	void writePitToFile(avscience.ppc.PitObs pit)
    {
                avscience.ppc.User u = pit.getUser();
    	
		StringBuffer buffer = new StringBuffer();
		Location loc = pit.getLocation();
		buffer.append(pit.getDateString()+ "\n");
		buffer.append("Observer ,"+u.getFirst()+ " "+ u.getLast()+ "\n");
		buffer.append("Location ,"+loc.getName()+ "\n");
		buffer.append("Mtn Range ,"+loc.getRange()+"\n");
		buffer.append("State/Prov ,"+loc.getState()+"\n");
		buffer.append("Elevation "+u.getElvUnits()+" ,"+loc.getElv()+"\n");
		buffer.append("Lat. ,"+loc.getLat()+"\n");
		buffer.append("Long. ,"+loc.getLongitude()+"\n");
		
		java.util.Hashtable labels = getPitLabels();
		java.util.Enumeration e = labels.keys();
		while ( e.hasMoreElements())
		{
                    String s = (String) e.nextElement();
                    String v ="";
                    try
                    {
                        v = (String) pit.get(s);
                    }
                    catch(Exception ex)
                    {
                        System.out.println(ex.toString());
                    }
                    String l = (String) labels.get(s);
                    s = l + " ," + v + "\n";
                    if (!( s.trim().equals("null")) ) buffer.append(s);
		}
		buffer.append("Activities: \n");
		Enumeration ee = pit.getActivities().elements();
		while ( ee.hasMoreElements())
		{
			String s = (String) ee.nextElement();
			buffer.append(s+"\n");
		}
		buffer.append("\n");
		buffer.append("Layer Data:, Depth units:, "+u.getDepthUnits()+", Density Units, "+u.getRhoUnits()+"\n");
		buffer.append("Layer start, Layer end, Hardness 1, Hardness 2, Crystal Form 1, Crystal Form 2, Crystal Size 1, Crystal Size 2, Size Units 1, Size Units 2, Density 1, Density 2, Water Content \n");
		Enumeration l = pit.getLayers();
		while ( l.hasMoreElements())
		{
			avscience.ppc.Layer layer = (avscience.ppc.Layer)l.nextElement();
			buffer.append(layer.getStartDepth()+", "+layer.getEndDepth()+", "+layer.getHardness1()+", "+layer.getHardness2()+", "+layer.getGrainType1()+", "+layer.getGrainType2()+", "+layer.getGrainSize1()+", "+layer.getGrainSize2()+", "+layer.getGrainSizeUnits1()+", "+layer.getGrainSizeUnits2()+", "+layer.getDensity1()+", "+layer.getDensity2()+", "+layer.getWaterContent()+"\n");
		}
		buffer.append("\n");
		buffer.append("Test Data: \n");
		buffer.append("Test, Score, Shear quality, Depth \n");
		Enumeration tests = pit.getShearTests();
		while ( tests.hasMoreElements())
		{
			ShearTestResult result = (ShearTestResult) tests.nextElement();
			buffer.append(result.getCode()+", "+result.getScore()+", "+result.getQuality()+", "+result.getDepth()+"\n");
		}
		
		buffer.append("\n");
		buffer.append("Temperature Data:, Temp Units:, "+u.getTempUnits()+"\n");
		buffer.append("Depth, Temperature \n");
		Enumeration dpths=null;
		if ( pit.hasTempProfile())
		{
			TempProfile tp = pit.getTempProfile();
			
			dpths = tp.getDepths().elements();
			
			if ( dpths!=null )
			{
				while (dpths.hasMoreElements())
				{
					Integer depth = (Integer)dpths.nextElement();
					int t = tp.getTemp(depth);
					t=t/10;
					buffer.append(depth.toString()+", "+t+"\n");
				}
			}
		}
		/////
		buffer.append("\n");
		buffer.append("Density Data:, Density Units:, "+u.getRhoUnits()+"\n");
		buffer.append("Depth, Density \n");
		DensityProfile dp = pit.getDensityProfile();
		
		if ( dp!=null )
		{
			Enumeration dts = dp.getDepths().elements();
		
			if ( dts!=null ) 
			{
				while (dts.hasMoreElements())
				{
					Integer depth = (Integer)dts.nextElement();
					String rho = dp.getDensity(depth);
					buffer.append(depth.toString()+", "+rho+"\n");
				}
			}
		}
		////////
		File file = new File(pit.getLocation().getName()+"-"+pit.getDate()+"-"+pit.getTime()+".txt");
		FileOutputStream out = null;
		PrintWriter writer = null;
		String fin = buffer.toString();
		fin.replaceAll("null", " ");
		
		try
		{
			out = new FileOutputStream(file);
			writer = new PrintWriter(out);
		}
		catch(Exception ex){System.out.println(ex.toString());}
		try
        {
        	writer.print(buffer.toString());
        	writer.flush();
        	writer.close();
        }
        catch(Exception ex){System.out.println(ex.toString());}
    }
    
    java.util.Hashtable getPitLabels()
    {
    	java.util.Hashtable attributes = new java.util.Hashtable();
    	
    	attributes.put("aspect", "Aspect");
    	attributes.put("incline", "Slope Angle");
    	attributes.put("precip", "Precipitation");
    	attributes.put("sky", "Sky Cover");
    	attributes.put("windspeed", "Wind Speed");
    	attributes.put("winDir", "Wind Direction");
    	attributes.put("windLoading", "Wind Loading");
    	
    	attributes.put("airTemp", "Air Temperature");
    	attributes.put("stability", "Stability on simular slopes");
    	
    	attributes.put("measureFrom", "Measure from: ");
    	
        attributes.put("date", "Date");
        attributes.put("time", "Time");
    	attributes.put("pitNotes", "Notes");
    	return attributes;
    }
    
	
	private java.util.Vector getPitList(String user) throws Exception
    {
    	String whereClause = " WHERE USERNAME = '"+user+"'";
        Object o = null;
        java.util.Vector list = null;
        try
        {
            String err = null;
            URL url = new URL("http://kahrlconsulting.com/avscience/PitListServlet");
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "pitlistquery");
            //String clause = whereClause();
            whereClause = URLEncoder.encode(whereClause, "UTF-8");
            props.put("q", whereClause);
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            o =  result.readObject();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        
        if ( o instanceof java.util.Vector)
        {
            list = (java.util.Vector) o;
            whereClause = "";
            return list;
        }
        else
        {
            
            String err = (String) o;
            System.out.println("Exception on query: " + err);
            throw new Exception(err);
        }
        
    }
}