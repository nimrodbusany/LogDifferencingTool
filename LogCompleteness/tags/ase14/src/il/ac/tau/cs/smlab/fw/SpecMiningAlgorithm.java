package il.ac.tau.cs.smlab.fw;

import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.List;

public interface SpecMiningAlgorithm {

	
	// define T = T1, T2, ...
	public List<LogProperty<?,?>> getAlgorithmLogProperties();
	
	// method to execute before computing properties results
	public void beforePropertiesResults(ExecutionTrace t) throws SpecMiningAlgorithmException;
	
	// method to execute after computing properties results
	public void afterPropertiesResults(ExecutionTrace t) throws SpecMiningAlgorithmException;
	
	public String getAlgorithmName();
	
	public void initProperties(String model);
	
}
