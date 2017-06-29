package avscience.pc;
import java.util.*;

public class StateProv
{
	Vector v = new Vector();
	
	public StateProv()
	{
		v.add("AK");
		v.add("AZ");
		v.add("CA");
		v.add("CO");
	
		v.add("ID");
		v.add("MT");
		
		v.add("NH");
		v.add("NM");
		v.add("NV");
		v.add("OR");
		v.add("UT");
		v.add("VT");
		
		v.add("WA");
		
		v.add("WY");
		//// Canadian
		v.add("AB");
		v.add("BC");
		v.add("Argentina");
		v.add("Chile");
		v.add("New Zealand");
		v.add("Switzerland");
		v.add("Spain");
		v.add("Germany");
		v.add("Austria");
		v.add("Italy");
		v.add("France");
                v.add("Iceland");
		v.add("Norway");
		v.add("Sweden");
                v.add("Finland");
		v.add("UK");
		v.add("Japan");
		v.add("India");
		v.add("Europe");
		v.add("Asia");
	
		v.add("South America");
		v.add("Arctic");
		v.add("Antarctic");
		v.add("other");
	}
	
	public String[] getList()
	{
		String[] codes = new String[v.size()];
		int i=0;
		Enumeration e = v.elements();
		while (e.hasMoreElements())
		{
			String s = (String) e.nextElement();
			codes[i]=s;
			i++;
		}
		return codes;
	}

}