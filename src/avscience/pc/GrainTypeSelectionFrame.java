package avscience.pc;

import avscience.ppc.GrainTypeConvertor;
import java.awt.*;

public class GrainTypeSelectionFrame extends Frame
{
	  TypeDisplay selectedType;
	  Label selectedText;
	  int wdth=384;
	  int ht=550;
	  private float fsize = 24f;
	  Font font;
	  TypeDisplay display;
          GrainTypeConvertor gtc = GrainTypeConvertor.getInstance();
          GrainTypeSelect[] basicTypes = new GrainTypeSelect[gtc.numberOfTypes];
	  
	  public GrainTypeSelectionFrame(TypeDisplay display)
	  {
	  	this.display=display;
	  	SWindow aSWindow = new SWindow();
		addWindowListener(aSWindow);
	  	buildForm();
	  	if (display.getType()!=null) setType(display.getType());
	  }
	  
	  public GrainTypeSelectionFrame(TypeDisplay display, int x, int y)
	  {
	  	this.display=display;
	  	SWindow aSWindow = new SWindow();
		addWindowListener(aSWindow);
	  	buildForm();
	  	if (display.getType()!=null) setType(display.getType());
	  	setLocation(x, y);
	  }
	  
	  public void setType(String type)
	  {
	  	selectedText.setText(gtc.getUITypeFromCode(type));
	  	selectedType.setType(type);
	  	display.setType(type);
	  	display.repaint();
	  	selectedType.repaint();
	  }
	  
	  void buildForm()
	  {
	  	setTitle("Grain Type Select");
	  	setLayout(null);
	  	setSize(wdth, ht);
	  	setVisible(true);
	 	int ys=40;
                int x=24;
                int y=ys;
                int vspace=34;
    	
                selectedType = new TypeDisplay("PP");
                selectedType.setLocation(20, y);
                add(selectedType);

                selectedText = new Label(gtc.getUITypeFromCode("PP"));
                selectedText.setLocation(x+50, y);
                selectedText.setSize(380, 22);
                add(selectedText);

                y+=vspace;
                
                String[] bTypes = gtc.basicTypeCodes;
                
                for ( int i=0; i < bTypes.length; i++ )
                {
                    TypeDisplay df = new TypeDisplay(bTypes[i]);
                    df.setLocation(20, y);
                    add(df);
                    basicTypes[i] = new GrainTypeSelect(this, bTypes[i]);
                    basicTypes[i].setLocation(x+50, y);
                    add(basicTypes[i]);
                    y+=vspace;
                }
	  }
	  
	  public void reset(GrainTypeSelect origin)
	  {
              for (int i=0; i < basicTypes.length; i++)
              {
                  if (origin!=basicTypes[i]) basicTypes[i].reset();
              }
	  }
	  
	  
	class SWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == GrainTypeSelectionFrame.this )
			{
				GrainTypeSelectionFrame.this.dispose();
			}
		}
	}
}