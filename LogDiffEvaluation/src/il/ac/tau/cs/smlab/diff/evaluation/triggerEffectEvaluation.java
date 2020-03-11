package il.ac.tau.cs.smlab.diff.evaluation;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.st.sam.mine.RunExperimentTriggerDiff;
import org.st.sam.mine.orig.RunExperimentTrigger;

public class triggerEffectEvaluation 
{
	public static void runRegullarTriggerExperimentDiff(String[] args) throws IOException
	{
		RunExperimentTriggerDiff exp = new RunExperimentTriggerDiff();
	    
	    if (!exp.readCommandLine(args))
	    {
	    	return;
	    }
	    
	    exp.experiment();
	}
	
	public static void runRegullarTriggerExperimentRegular(String[] args) throws IOException
	{
		RunExperimentTrigger exp = new RunExperimentTrigger();
	    
	    if (!exp.readCommandLine(args)) 
	    	return;
	    
	    exp.experiment();
	}
	
	public static void main(String[] args)
	{
		try
		{
			runRegullarTriggerExperimentDiff(args);
			
			List<String> argsList = new LinkedList<String>(Arrays.asList(args));
			
			argsList.remove(1);
			
			args = new String[args.length - 1];
			argsList.toArray(args);
			
			runRegullarTriggerExperimentRegular(args);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.out.print(e);
		}
	}
}
