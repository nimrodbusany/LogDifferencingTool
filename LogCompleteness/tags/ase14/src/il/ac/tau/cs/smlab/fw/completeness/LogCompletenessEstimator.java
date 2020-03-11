package il.ac.tau.cs.smlab.fw.completeness;

import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.property.log.PropertyCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.ArrayList;
import java.util.List;

public class LogCompletenessEstimator {
	
	List<PropertyCompletenessEstimator<?,?>> propEstimators;
	
	PropertiesEstimatesAggregator defaultAggregator = new MinPropertiesEstimatesAggregator();

	public LogCompletenessEstimator(List<LogProperty<?,?>> properties, Alphabet alphabet) {
		propEstimators = new ArrayList<PropertyCompletenessEstimator<?,?>>(properties.size());
		for (LogProperty<?,?> property:properties) {
			propEstimators.add(property.createCompletenessEstimator(alphabet));
		}
	}
	
	
	public void updatePropertyValues(ExecutionTrace t) {
		for (PropertyCompletenessEstimator<?,?> estimator :propEstimators) {
			estimator.updatePropertyValues(t);
		}
	}
	
	public double estimate(PropertiesEstimatesAggregator aggregator) {
		List<Double> estimates = new ArrayList<Double>(propEstimators.size());
		for (PropertyCompletenessEstimator<?,?> estimator :propEstimators) {
			estimates.add(estimator.estimate());
		}
		return aggregator.aggregate(estimates);
	}
	
	
	public double estimate() {
		return estimate(defaultAggregator);
	}
	
	
	public List<PropertyCompletenessEstimator<?,?>> getPropEstimators() {
		return propEstimators;
	}
	
	public LogProperty<?,?> getPropertyByName(String name) {
		for (PropertyCompletenessEstimator<?,?> p:propEstimators) {
			if (p.getProperty().getPropertyName().equals(name)) {
				return p.getProperty();
			}
		}
		return null;
	}
	
	
	public PropertyCompletenessEstimator<?,?> getPropertyEstimatorByName(String name) {
		for (PropertyCompletenessEstimator<?,?> p:propEstimators) {
			if (p.getProperty().getPropertyName().equals(name)) {
				return p;
			}
		}
		return null;
	}
	
}
