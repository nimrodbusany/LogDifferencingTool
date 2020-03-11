package il.ac.tau.cs.smlab.fw.property.log.conf.v3;

import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.log.conf.InvariantConfidenceComputation;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.interval.BinomialConfidenceInterval;
import org.apache.commons.math3.stat.interval.ConfidenceInterval;
import org.apache.commons.math3.stat.interval.NormalApproximationInterval;

public class InvariantConfidenceComputationV3 implements InvariantConfidenceComputation {

	private InvariantConfidenceComputationV3() {}

	private static InvariantConfidenceComputationV3 INSTANCE = new InvariantConfidenceComputationV3();

	public static InvariantConfidenceComputation getInstance() {return INSTANCE;}

	private double confidenceLevel = 0.4;

	@Override
	public double propetyCompleteness(LogPropertyValues<Boolean> vals, int n) {
		double confidence = 0.0;
		int sum;
		for (Entry<EventTypeSeq, List<Boolean>> seqVals : vals.getPropVals().entrySet()) {
			sum = 0;
			for (Boolean l : seqVals.getValue()) {
				if (l) {
					sum+=1;
				}
			}
			if (sum < n) {
				if (sum == 0) {
					confidence += 0.0D;
				}
				else {
					BinomialConfidenceInterval bci = new NormalApproximationInterval();
					ConfidenceInterval ci =  bci.createInterval(n, sum, confidenceLevel);
					Double upperBound = ci.getLowerBound();
					confidence += Math.pow(upperBound, n);
					confidence = Math.min(1, confidence);
				}
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
