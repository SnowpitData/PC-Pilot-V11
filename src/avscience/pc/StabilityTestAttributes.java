package avscience.pc;

public class StabilityTestAttributes
{
	public String operator;
	public String testType;
	public String[] testScores;
	public String[] shearQuality;
	private String depthUnits;
	private int depth;
	public char depthOp;
	public int CTScore;
	public char CTScoreOp;
	
	public StabilityTestAttributes(String operator, String testType, String[] testScores, String[] shearQuality, String depthUnits, int depth, char depthOp, int CTScore, char CTScoreOp)
	{
		this.operator = operator;
		this.testType = testType;
		this.testScores = testScores;
		this.shearQuality = shearQuality;
		this.depthUnits = depthUnits;
		this.depth = depth;
		this.depthOp = depthOp;
		this.CTScore = CTScore;
		this.CTScoreOp = CTScoreOp;
		if ( depthUnits.equals("inches") ) depth = convertDepth(depth);
	}
	
	public int convertDepth(int dpth)
	{
		return (int) java.lang.Math.round(dpth/2.54);
	}
	
	public int getDepth()
	{
		return depth;
	}
}