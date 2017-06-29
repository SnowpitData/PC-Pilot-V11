package avscience.desktop;

import avscience.ppc.*;
import java.util.Hashtable;
import avscience.ppc.Location;
import java.awt.Panel;
import java.awt.TextArea;
import java.io.PrintStream;
import java.util.*;

public class OccCanvas extends Panel
{
    private AvOccurence occ;
    private PitObs pit;
    static final int spc = 18;
    LinkedHashMap attributes;
    private TextArea txt;
    String dtime;

    public OccCanvas(AvOccurence avoccurence, PitObs pitobs)
    {
        attributes = new LinkedHashMap();
        txt = new TextArea();
        dtime = "";
        System.out.println("OccCanvas");
        StringBuffer stringbuffer = new StringBuffer();
        occ = avoccurence;
        pit = pitobs;
        setSize(780, 560);
        txt.setSize(760, 540);
        txt.setLocation(10, 10);
        txt.setVisible(true);
        add(txt);
        long l = pitobs.getTimestamp();
        try
        {
            if(l > 0L)
            {
                Date date = new Date(l);
                dtime = date.toString();
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
        }
        setLabels();
        Location location = pitobs.getLocation();
       // Hashtable hashtable = null;
        Object obj = null;
        if(pitobs.getUser() != null)
            stringbuffer.append("User: " + pitobs.getUser().getName() + "\n");
        stringbuffer.append("Avalanche Occurrence Record: \n");
        stringbuffer.append(avoccurence.getPitName() + "\n");
        stringbuffer.append("Location: \n");
        if(location != null)
            stringbuffer.append(location.toString() + "\n");
        try
        {
            avoccurence.put("dtime", dtime);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        
        Iterator it = avoccurence.sortedKeys();
        while(it.hasNext())
        {
            try
            {
                String s2 = it.next().toString();
                String s1 = (String)avoccurence.get(s2);
                String s = (String)attributes.get(s2);
                s2 = s + " " + s1 + "\n";
                if(s1 != null && s1.trim().length() > 0 && !s2.trim().equals("null"))
                    stringbuffer.append(s2);
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }

        txt.setText(stringbuffer.toString());
    }

    void setLabels()
    {
        User user = pit.getUser();
        if(user == null)
            user = new User();
        attributes.put("pitObs", "Name: ");
        attributes.put("dtime", "Date/Time: ");
        attributes.put("estDate", "Estimated date: ");
        attributes.put("estTime", "Estimated Time: ");
        attributes.put("elvStart", "Elevation Start: (" + user.getElvUnits() + ") ");
        attributes.put("elvDeposit", "Elevation Deposit: (" + user.getElvUnits() + ") ");
        attributes.put("fractureWidth", "Fracture Width: (" + user.getElvUnits() + ") ");
        attributes.put("fractureLength", "Fracture Length: (" + user.getElvUnits() + ") ");
        attributes.put("lengthOfAvalanche", "Avalanche Length: (" + user.getElvUnits() + ") ");
        attributes.put("aspect", "Primary Aspect: ");
        attributes.put("aspect1", "Aspect 1: ");
        attributes.put("aspect2", "Aspect 2: ");
        attributes.put("type", "Type: ");
        attributes.put("wcStart", "Water Content Start: ");
        attributes.put("wcDeposit", "Water Content Deposit: ");
        attributes.put("triggerType", "Trigger Type: ");
        attributes.put("triggerCode", "Trigger Code: ");
        attributes.put("causeOfRelease", "Cause of release: ");
        attributes.put("sympathetic", "Sympathetic? ");
        attributes.put("sympDistance", "Sympathetic/remote distance: ");
        attributes.put("USSize", "Size relative to Path: ");
        attributes.put("CASize", "Size destructive force: ");
        attributes.put("avgFractureDepth", "Average Fracture Depth: (" + user.getDepthUnits() + ") ");
        attributes.put("maxFractureDepth", "Max. Fracture Depth: (" + user.getDepthUnits() + ") ");
        attributes.put("levelOfBedSurface", "Level Of Bed Surface: ");
        attributes.put("weakLayerType", "Weak Layer Type: ");
        attributes.put("crystalSize", "Weak Layer Crystal Size: ");
        attributes.put("sizeSuffix", "Weak Layer Size suffix: ");
        attributes.put("weakLayerHardness", "Weak Layer Hardness: ");
        attributes.put("hsuffix", "Weak Layer Hardness suffix: ");
        attributes.put("crystalTypeAbove", "Crystal Type Above: ");
        attributes.put("crystalSizeAbove", "Crystal Size Above: ");
        attributes.put("sizeSuffixAbove", "Size suffix above: ");
        attributes.put("hardnessAbove", "Hardness above: ");
        attributes.put("hsuffixAbove", "Hardness suffix above: ");
        attributes.put("crystalTypeBelow", "Crystal Type Below: ");
        attributes.put("crystalSizeBelow", "Crystal Size Below: ");
        attributes.put("sizeSuffixBelow", "Size suffix below: ");
        attributes.put("hardnessBelow", "Hardness below: ");
        attributes.put("hsuffixBelow", "Hardness suffix below: ");
        attributes.put("snowPackType", "Snow Pack Typology: ");
        attributes.put("avgStartAngle", "Avg Start Angle: ");
        attributes.put("maxStartAngle", "Max Start Angle: ");
        attributes.put("minStartAngle", "Min Start Angle: ");
        attributes.put("alphaAngle", "Alpha Angle: ");
        attributes.put("depthOfDeposit", "Depth of deposit: (" + user.getDepthUnits() + ") ");
        attributes.put("lengthOfDeposit", "Length of deposit: ");
        attributes.put("widthOfDeposit", "Width of deposit: ");
        attributes.put("densityOfDeposit", "Density of deposit (" + user.getRhoUnits() + ") ");
        attributes.put("numPeopleCaught", "Number of people caught: ");
        attributes.put("numPeoplePartBuried", "Number of people part buried: ");
        attributes.put("numPeopleTotalBuried", "Number of people totally buried: ");
        attributes.put("injury", "Injuries: ");
        attributes.put("fatality", "Fatalites: ");
        attributes.put("bldgDmg", "Building Damage US $: ");
        attributes.put("eqDmg", "Equipment Damage US $: ");
        attributes.put("vehDmg", "Vehicle Damage US $: ");
        attributes.put("miscDmg", "Misc Damage US $: ");
        attributes.put("estDamage", "Total Damage US $: ");
        attributes.put("comments", "Comments: ");
        attributes.put("hasPit", "Has pit observation? ");
    }

}
