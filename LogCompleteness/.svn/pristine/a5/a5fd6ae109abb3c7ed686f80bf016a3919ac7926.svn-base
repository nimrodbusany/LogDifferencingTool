package il.ac.tau.cs.smlab.fw.property.log.conf.v2;

import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.log.conf.ExistentialConfidenceComputation;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.List;
import java.util.Map.Entry;

public class ExistentialConfidenceComputationForPartialCompletenessV2 implements ExistentialConfidenceComputation {
	
	private ExistentialConfidenceComputationForPartialCompletenessV2() {}
	
	private static ExistentialConfidenceComputationForPartialCompletenessV2 INSTANCE = new ExistentialConfidenceComputationForPartialCompletenessV2();
	
	public static ExistentialConfidenceComputation getInstance() {return INSTANCE;}

	
	@Override
	public double propetyCompleteness(LogPropertyValues<Boolean> vals, int n) {
		double confidence1 = 1.0;
		int sum;
		double avg;
		// complete log
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
				confidence1 *= (1-Math.pow(1 - avg, n));
			}
		}
		
		double confidence2 = 0.0;
		// exactly one incorrect sequence
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
				
				// all other seqs are correct 
				double otherSeqs = 1.0;
				int sum2;
				double avg2;
				for (Entry<EventTypeSeq, List<Boolean>> otherSeqVals : vals.getPropVals().entrySet()) {
					if (seqVals.getKey().equals(otherSeqVals.getKey())) continue;
					sum2 = 0;
					avg2 = 0.0;
					for (Boolean l : otherSeqVals.getValue()) {
						if (l) {
							sum2+=1;
						}
					}
					if (sum2 > 0) {
						avg2 = (double)sum2 / n;
						otherSeqs *= (1-Math.pow(1 - avg2, n));
					}
				}
				confidence2 += (Math.pow(1 - avg, n) * otherSeqs);
			}
		}
		double confidence = confidence1 + confidence2;
		confidence = Math.min(1, confidence);
		System.out.println("Confidence: " + confidence1 + " + " + confidence2 + "=" + confidence );
		return confidence;
	}
	
	@Override
	public String getVersion() {
		return "2.0";
	}

}
