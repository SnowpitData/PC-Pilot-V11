package avscience.desktop;

import avscience.ppc.PitObs;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Properties;

public class HttpMessage
{
    private URL servlet;
    private String args;
    
    public HttpMessage(URL url)
    {
        servlet = url;
    }

    public InputStream sendGetMessage(Properties properties)
        throws IOException
    {
        String s = "";
        if(properties != null)
            s = "?" + toEncodedString(properties);
        URL url = new URL(servlet.toExternalForm() + s);
        URLConnection urlconnection = url.openConnection();
        urlconnection.setUseCaches(false);
        return urlconnection.getInputStream();
    }

    public void sendPit(PitObs pitobs)
        throws IOException
    {
        String s = pitobs.toXML();
        s = URLEncoder.encode(s, "UTF-8");
        String s1 = "";
        Properties properties = new Properties();
        properties.put("data", s);
        s1 = "?" + toEncodedString(properties);
        URL url = new URL(servlet.toExternalForm() + s1);
        URLConnection urlconnection = url.openConnection();
        urlconnection.setUseCaches(false);
        urlconnection.connect();
    }

    public OutputStream sendRecieveMessage(Properties properties)
        throws IOException
    {
        String s = "";
        if(properties != null)
            s = "?" + toEncodedString(properties);
        URL url = new URL(servlet.toExternalForm());
        URLConnection urlconnection = url.openConnection();
        urlconnection.setUseCaches(false);
        urlconnection.setDoOutput(true);
        urlconnection.connect();
        return urlconnection.getOutputStream();
    }

    private String toEncodedString(Properties properties)
    {
        StringBuffer stringbuffer = new StringBuffer();
        Enumeration enumeration = properties.propertyNames();
        do
        {
            if(!enumeration.hasMoreElements())
                break;
            String s = (String)enumeration.nextElement();
            String s1 = properties.getProperty(s);
            stringbuffer.append(URLEncoder.encode(s) + "=" + URLEncoder.encode(s1));
            if(enumeration.hasMoreElements())
                stringbuffer.append("&");
        } while(true);
        return stringbuffer.toString();
    }

}
