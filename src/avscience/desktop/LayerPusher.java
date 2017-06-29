package avscience.desktop;


public class LayerPusher 
{
	private int minThick=24;
	int numberOfLayers;
	int minDepth;
	int maxDepth;
	int[][]layers;
	
    public LayerPusher() 
    {
    }
    
    public void getPushedLayers(int[][] olayers)
    {
    	numberOfLayers=olayers[0].length;
    	minDepth=olayers[0][0];
    	maxDepth=olayers[1][numberOfLayers-1];
    	this.layers=olayers;
    	int count=0;
    	while (hasThinLayers())
    	{
    		for (int i=0;i<numberOfLayers;i++)
    		{
    			if ((isLayerThin(i)) && (isLayerThick(i+1))) expandLayerDown(i);	
    		}
    		
    		for (int i=0;i<numberOfLayers;i++)
    		{
    			if ((isLayerThin(i)) && (isLayerThick(i-1))) expandLayerUp(i);
    		}
    		
    		for (int i=0;i<numberOfLayers;i++)
    		{
    			if (isLayerThick(i)) shrinkLayerDown(i);
    		}
    		
    		for (int i=0;i<numberOfLayers;i++)
    		{
    			if (isLayerThick(i)) shrinkLayerUp(i);
    		}
                
    		count++;
    		if (count > 2000) 
    		{
    			System.out.println("NO CONVERGENCE !!!!!!!!!!!!!!! COUNT IS: "+count);
    			break;
    		}
    	}
    }
    
    boolean isLayerThin(int i)
    {
    	if (i<0) return false;
    	if (i>numberOfLayers-1) return false;
    	int delt = layers[1][i]-layers[0][i];
    	if (delt<0) delt =-delt;
    	return delt<minThick;
    }
    
    boolean isLayerThick(int i)
    {
    	if (i<0) return false;
    	if (i>numberOfLayers-1) return false;
    	int delt = layers[1][i]-layers[0][i];
    	if (delt<0) delt =-delt;
    	return delt>minThick;
    }
    
    boolean isLayerMin(int i)
    {
    	if (i<0) return false;
    	if (i>numberOfLayers-1) return false;
    	int delt = layers[1][i]-layers[0][i];
    	if (delt<0) delt =-delt;
    	return delt==minThick;
    }
    
    void expandLayerDown(int i)
    {
    	if (i==(numberOfLayers-1)) return;
    	layers[1][i]++;
    	layers[0][i+1]++;
    }
    
    void shrinkLayerDown(int i)
    {
    	if (i==(numberOfLayers-1)) return;
    	layers[1][i]--;
    	layers[0][i+1]--;
    }
    
    void moveLayerDown(int i)
    {
    	if (i==(numberOfLayers-1)) return;
    	if (!isLayerThick(i+1)) return;
    	layers[0][i]++;
    	layers[1][i]++;
    	layers[1][i-1]++;
    }
    ///////////
    void expandLayerUp(int i)
    {
    	if (i==0) return;
    	layers[0][i]--;
    	layers[1][i-1]--;
    }
    
    void shrinkLayerUp(int i)
    {
    	if (i<1)return;
    	layers[0][i]++;
    	layers[1][i-1]++;
    }
    
    void moveLayerUp(int i)
    {
    	if (i<1) return;
    	if (!isLayerThick(i-1)) return;
    	layers[0][i]--;
    	layers[1][i]--;
    	layers[1][i-1]--;
    }
    
    boolean hasThinLayersAbove(int l)
    {
    	if (l<1) return false;
    	for (int i=l-1; i>-1; i--)
    	{
    		if (isLayerThin(i)) return true;
    	}
    	return false;
    }
    
    boolean hasThinLayersBelow(int l)
    {
    	if (l==numberOfLayers-1) return false;
    	for (int i=l+1; i<numberOfLayers; i++)
    	{
    		if (isLayerThin(i)) return true;
    	}
    	return false;
    }
    
    boolean hasThinLayers()
    {
    	for (int i=0;i<numberOfLayers;i++)
    	{
    		if (isLayerThin(i)) return true;
    	}
    	return false;
    }
}