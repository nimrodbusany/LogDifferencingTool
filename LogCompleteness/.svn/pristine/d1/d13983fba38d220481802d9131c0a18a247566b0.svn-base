package il.ac.tau.cs.smlab.fw.completeness;

import java.util.Collection;

public class MinPropertiesEstimatesAggregator implements PropertiesEstimatesAggregator {

	@Override
	public double aggregate(Collection<Double> estimates) {
		double min = Double.MAX_VALUE;
		for (Double est :estimates) {
			min = Math.min(min, est);
		}
		return min;
	}

}
