package il.ac.tau.cs.smlab.fw.completeness;

import java.util.Collection;

public class AveragePropertiesEstimatesAggregator implements PropertiesEstimatesAggregator {

	@Override
	public double aggregate(Collection<Double> estimates) {
		double avg = 0.0;
		for (Double est :estimates) {
			avg += est;
		}
		avg /= estimates.size();
		return avg;
	}

}
