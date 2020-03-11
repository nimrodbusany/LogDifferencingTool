package il.ac.tau.cs.smlab.fw.property.log.conf.v2;

import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.log.conf.ExistentialConfidenceComputation;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.List;
import java.util.Map.Entry;

public class ExistentialConfidenceComputationV2 implements ExistentialConfidenceComputation {
	
	private ExistentialConfidenceComputationV2() {}
	
	private static ExistentialConfidenceComputationV2 INSTANCE = new ExistentialConfidenceComputationV2();
	
	public static ExistentialConfidenceComputation getInstance() {return INSTANCE;}

	
	@Override
	public double propetyCompleteness(LogPropertyValues<Boolean> vals, int n) {
		double confidence = 1.0;
		int sum;
		double avg;
		for (Entry<EventTypeSeq, List<Boolean>> seqVals : vals.getPropVals().entrySet()) {
			sum = 0;
			avg = 0.0;
			for (Boolean l : seqVals.getValue()) {
				if (l) {
					sum+=1;
				}
			}
			if (sum > 0) {
				avg = (double)sum / n;
				assert(avg > 0 && avg <= 1);
				confidence *= (1-Math.pow(1 - avg, n));
			}
		}
		return confidence;
	}
	
	@Override
	public String getVersion() {
		return "2.0";
	}

}
