package il.ac.tau.cs.smlab.fw.property.log.conf.v2;

import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.log.conf.InvariantConfidenceComputation;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.List;
import java.util.Map.Entry;

public class InvariantConfidenceComputationV2 implements InvariantConfidenceComputation {

	private InvariantConfidenceComputationV2() {}
	
	private static InvariantConfidenceComputationV2 INSTANCE = new InvariantConfidenceComputationV2();
	
	public static InvariantConfidenceComputation getInstance() {return INSTANCE;}
	
	
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
			if (sum < n) {
				avg = (double)sum / n;
				assert(avg >= 0 && avg < 1);
				confidence *= (1-Math.pow(avg, n));
			}
		}
		return confidence;
	}
	
	@Override
	public String getVersion() {
		return "2.0";
	}
	
}
