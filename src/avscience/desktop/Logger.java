
package avscience.desktop;

import java.io.*;

public final class Logger
{
    
    private static final Logger instance = new Logger();
    String filename;
    File log;
    FileOutputStream fout;
    PrintStream out;

    public static Logger getInstance()
    {
        return instance;
    }

    private Logger()
    {
        filename = "SnowPilotDesktop_log.txt";
        log = new File(filename);
        init();
    }

    private void init()
    {
        try
        {
            fout = new FileOutputStream(log);
            out = new PrintStream(fout);
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
        }
    }

    public void println(String s)
    {
        out.println(s);
        System.out.println(s);
    }

}
