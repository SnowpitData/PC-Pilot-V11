package avscience.pc;
import java.awt.*;

public interface TextItemOp
{
	public Label getLabel();
	public Component getField();
	public Choice getOperator();
	public String getText();
	public String getOperatorValue();
}