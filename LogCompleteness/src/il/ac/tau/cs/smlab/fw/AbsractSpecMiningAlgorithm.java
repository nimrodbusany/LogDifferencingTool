package il.ac.tau.cs.smlab.fw;

import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.LinkedList;
import java.util.List;

public abstract class AbsractSpecMiningAlgorithm implements SpecMiningAlgorithm {
	
	protected List<LogProperty<?,?>> properties = new LinkedList<LogProperty<?,?>>();
	
	// define T = T1, T2, ...
	public List<LogProperty<?,?>> getAlgorithmLogProperties() { return properties; }
	
	public void addLogProperty(LogProperty<?,?> property) {
		if (!properties.contains(property)) {
			properties.add(property);
		}
	}
	
	// empty default implementation
	@Override
	public void beforePropertiesResults(ExecutionTrace t)
			throws SpecMiningAlgorithmException {
	}

	// empty default implementation
	@Override
	public void afterPropertiesResults(ExecutionTrace t)
			throws SpecMiningAlgorithmException {
	}
	
	// empty default implementation
	public void initProperties(String model) {
	}
	
}
