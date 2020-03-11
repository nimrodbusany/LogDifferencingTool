package il.ac.tau.cs.smlab.fw.property.log.conf.v1;

import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.log.conf.InvariantConfidenceComputation;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.List;
import java.util.Map.Entry;

public class InvariantConfidenceComputationV1 implements InvariantConfidenceComputation {
	
	private InvariantConfidenceComputationV1() {}
	
	private static InvariantConfidenceComputationV1 INSTANCE = new InvariantConfidenceComputationV1();
	
	public static InvariantConfidenceComputation getInstance() {return INSTANCE;}

	@Override
	public double propetyCompleteness(LogPropertyValues<Boolean> vals, int n) {
		double confidence = 0.0;
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
				confidence += Math.pow(avg, n);
				confidence = Math.min(1, confidence);
			}
		}
		confidence = 1 - confidence;
		return confidence;
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
	
}
