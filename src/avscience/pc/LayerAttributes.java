package avscience.pc;

public class LayerAttributes
{
	public String operator;
	private int startDepth;
	private int endDepth;
	private String depthUnits;
	public char startDepthOp;
	public char endDepthOp;
	public String[] hardness;
	public String grainType1;
	public String grainType2;
	private String grainSizeUnits;
	private float grainSize;
	public char grainSizeOp;
	private float rho;
	public char rhoOp;
	private String rhoUnits;
	public String[] waterContent;
	
	public LayerAttributes(String operator, int startDepth, int endDepth, String depthUnits, char startDepthOp, char endDepthOp, String[] hardness, String grainType1, String grainType2, String grainSizeUnits, float grainSize, char grainSizeOp, float rho, char rhoOp, String rhoUnits, String[] waterContent)
	{
		this.operator = operator;
		this.startDepth = startDepth;
		this.endDepth = endDepth;
		this.depthUnits = depthUnits;
		this.startDepthOp = startDepthOp;
		this.endDepthOp = endDepthOp;
		this.hardness = hardness;
		this.grainType1 = grainType1;
		this.grainType2 = grainType2;
		this.grainSizeUnits = grainSizeUnits;
		this.grainSize = grainSize;
		this.grainSizeOp = grainSizeOp;
		this.rho = rho;
		this.rhoOp = rhoOp;
		this.rhoUnits = rhoUnits;
		this.waterContent = waterContent;
	}
	
	public int getStartDepth()
	{
		int sd = startDepth;
		if ( depthUnits.equals("inches")) sd = (int) java.lang.Math.round(sd/2.54);
		return sd;
	}
	
	public int getEndDepth()
	{
		int ed = endDepth;
		if ( depthUnits.equals("inches")) ed = (int) java.lang.Math.round(ed/2.54);
		return ed;
	}
	
	public float getGrainSize()
	{
		float gs = grainSize;
		if ( grainSizeUnits.equals("cm")) gs = 10*gs;
		return gs;
	}
	
	public float getRho()
	{
		float rh = rho;
		if ( rhoUnits.equals("lbs/cubic_ft")) rh = rh*16.19f;
		return rh;
	}
}