 package avscience.pc;
 
 import avscience.desktop.GrainTypeSymbols;
 import java.awt.Canvas;
 import java.awt.Graphics;
 
 public class TypeDisplay extends Canvas
 {
  String type;

  public TypeDisplay()
  {
     this.type = "";
     setSize(30, 24);
     setVisible(true);
  }

  public TypeDisplay(String type)
  {
        this.type = type;
        setSize(24, 24);
        setVisible(true);
   }
  
  public void paint(Graphics paramGraphics)
  {
       GrainTypeSymbols localGrainTypeSymbols = GrainTypeSymbols.getInstance(paramGraphics);
       localGrainTypeSymbols.drawSymbol(1, 1, this.type);
  }

  public String getType()
  {
    return this.type;
  }

   public void setType(String type)
   {
        this.type = type;
   }
}
