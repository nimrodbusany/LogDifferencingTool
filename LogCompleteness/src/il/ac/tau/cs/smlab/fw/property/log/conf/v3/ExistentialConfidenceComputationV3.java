package il.ac.tau.cs.smlab.fw.property.log.conf.v3;

import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.log.conf.ExistentialConfidenceComputation;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.interval.BinomialConfidenceInterval;
import org.apache.commons.math3.stat.interval.ConfidenceInterval;
import org.apache.commons.math3.stat.interval.NormalApproximationInterval;

public class ExistentialConfidenceComputationV3 implements ExistentialConfidenceComputation {

	private ExistentialConfidenceComputationV3() {}

	private static ExistentialConfidenceComputationV3 INSTANCE = new ExistentialConfidenceComputationV3();

	public static ExistentialConfidenceComputation getInstance() {return INSTANCE;}

	private double confidenceLevel = 0.4;


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
			if (sum > 0) {
				if (sum == n) {
					avg = (double)sum / n;
					assert(avg > 0 && avg <= 1);
					confidence += Math.pow(1 - avg, n);
				}
				else {
					BinomialConfidenceInterval bci = new NormalApproximationInterval();
					ConfidenceInterval ci =  bci.createInterval(n, sum, confidenceLevel);
					Double lowerBound = ci.getUpperBound();
					confidence += Math.pow(1 - lowerBound, n);

				}
				confidence = Math.min(1, confidence);
			}
		}
		confidence = 1 - confidence;
		return confidence;
	}


	@Override
	public String getVersion() {
		return "3.0";
	}

}
