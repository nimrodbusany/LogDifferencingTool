package il.ac.tau.cs.smlab.fw.completeness;

import il.ac.tau.cs.smlab.fw.property.log.PropertyCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RelativeCompletenessCalculator  {


	public static double compute(List<PropertyCompletenessEstimator<?,?>> fullAgg, List<PropertyCompletenessEstimator<?,?>> partialAgg) {
		double relativeCompleteness = 0;
		// calculate relative completeness 
		assert(fullAgg.size() == partialAgg.size());

		for (int i = 0 ; i<fullAgg.size() ; i++) {
			Map<EventTypeSeq, ?> fullAggVals = fullAgg.get(i).getAggregatedPropertyValues().getAggLogPropVals();
			Map<EventTypeSeq, ?> partialAggVals = partialAgg.get(i).getAggregatedPropertyValues().getAggLogPropVals();

			assert (fullAggVals.size() == partialAggVals.size());
			int count = 0;

			for (Entry<EventTypeSeq, ?> ef : fullAggVals.entrySet()) {

				Object rf = ef.getValue(); // result in full
				Object rp = partialAggVals.get(ef.getKey()); // result if partial

				if (rf.equals(rp)) {
					++count;
				}
			}
			relativeCompleteness += ((double)count)/fullAggVals.size();
		}
		// should use a generic aggregator here instead of average
		relativeCompleteness = relativeCompleteness / fullAgg.size();
		return relativeCompleteness;
	}




}
