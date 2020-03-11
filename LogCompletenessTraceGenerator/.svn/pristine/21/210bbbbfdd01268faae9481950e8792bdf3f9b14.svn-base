package il.ac.tau.cs.smlab.fsa.generator.traces.util;


/**
 * 
 * @author Anda Hoxhaj
 *
 */

public class SettingsManager {

	static String curDir = System.getProperty("user.dir");
	private static Integer MaxValue;
	private static Integer MinValue;
	private static Integer MaxTraceGen;

	public static Integer getMax(Integer k)
	{
		String tmp = "100";

		//If value of MaxValue variable is null assign default value Integer.MAX_VALUE
		if(tmp.equals("null"))
		{
			MaxValue = Integer.MAX_VALUE;
		}else{
			MaxValue = Math.min(Integer.parseInt(tmp) + k, Integer.MAX_VALUE);
		}

		return MaxValue;
	}

	public static Integer getMin(Integer k)
	{
		String tmp = "-100";

		//If value of MaxValue variable is null assign default value Integer.MIN_VALUE
		if(tmp.equals("null"))
		{
			MinValue = Integer.MIN_VALUE;
		}else{
			MinValue = Math.max(Integer.parseInt(tmp) + k, Integer.MIN_VALUE);
		}

		return MinValue;
	}

	public static Integer getMaxTraceGen(){
		MaxTraceGen = 15000;
		return MaxTraceGen;
	}
}
