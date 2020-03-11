package il.ac.tau.cs.smlab.fw.completeness;

import java.util.Collection;

public interface PropertiesEstimatesAggregator {

	public double aggregate(Collection<Double> estimates);
}
