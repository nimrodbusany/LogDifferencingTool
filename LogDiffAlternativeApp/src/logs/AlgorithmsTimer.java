package logs;

public class AlgorithmsTimer 
{
	double time;
	double start;
	
	public Double logSize;
	public Double traceSize;
	
	public Double averageLogSize;
	public Double averageTraceSize;
	
	public AlgorithmsTimer()
	{
		time = 0;
		start = 0;
		
		logSize = 0.0;
		traceSize= 0.0;
		
		averageLogSize= 0.0;
		averageTraceSize= 0.0;
	}
	
	public void stop()
	{
		time += System.currentTimeMillis() - start;
		start = 0;
	}
	
	public void run()
	{
		start = System.currentTimeMillis();
	}
	
	public Double getTime()
	{
		return time;
	}
}
