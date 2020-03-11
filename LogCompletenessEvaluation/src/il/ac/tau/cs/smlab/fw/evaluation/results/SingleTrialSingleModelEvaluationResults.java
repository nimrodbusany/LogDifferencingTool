package il.ac.tau.cs.smlab.fw.evaluation.results;

import il.ac.tau.cs.smlab.fw.property.log.LogProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SingleTrialSingleModelEvaluationResults {
	
	// intermediate confidence intervals to store, default is to store every 10th confidence
	public static final int INTERMEDIATE_CONFIDENCE_INTERVAL = 1;
	
	int n = 0; // number of traces in L

	Map<String,PropertyResults> results;

	double confidence; // P(L is complete)
	
	int events = 0;
	
	Map<Integer,Double> intermediateConfidence;
	
	long confidenceComputationTime = 0; // in ms
	
	public SingleTrialSingleModelEvaluationResults(List<LogProperty<?,?>> properties) {
		results = new HashMap<String,PropertyResults>(properties.size());
		for (LogProperty<?,?> p : properties) {
			results.put(p.getPropertyName(),new PropertyResults(p.getPropertyName()));
		}
		intermediateConfidence = new HashMap<Integer,Double>();
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
	
	public void setIncorrectSeqs(Map<String,Integer> incorrect) {
		for (Entry<String,Integer> e : incorrect.entrySet()) {
			results.get(e.getKey()).setIncorrectSeqs(e.getValue());
		}
	}

	public void setCompleteness(Map<String,Double> completeness) {
		for (Entry<String,Double> e : completeness.entrySet()) {
			results.get(e.getKey()).setCompleteness(e.getValue());
		}
	}
	
	public void setRedundancy(Map<String,Double> redundancy) {
		for (Entry<String,Double> e : redundancy.entrySet()) {
			results.get(e.getKey()).setRedundancy(e.getValue());
		}
	}
	
	
	public Map<String, PropertyResults> getResults() {
		return results;
	}
	
	public double getAverageCompleteness() { // average between properties
		double avg = 0;
		for (PropertyResults p : results.values()) {
			avg += p.getCompleteness();
		}
		avg = avg / results.size();
		return avg;
	}
	
	public double getMaxIncorrectSeqs() {
		double max = 0;
		for (PropertyResults p : results.values()) {
			max = Math.max(max,p.getIncorrectSeqs());
		}
		return max;
	}
	
	
	public double getMinRedundancy() {
		double min = 1D;
		for (PropertyResults p : results.values()) {
			min = Math.min(min,p.getRedundancy());
		}
		return min;
	}
	
	
	public double getAverageRedundancy() { // average between properties
		double avg = 0;
		for (PropertyResults p : results.values()) {
			avg += p.getRedundancy();
		}
		avg = avg / results.size();
		return avg;
	}

	
	public void addEventsCount(int events) {
		this.events +=  events - 2; // minus initial and terminal
	}
	
	public double getAverageTraceSize() {
		return ((double)events)/n;
	}
	
	public void addIntermediateConfidence(int numOfTraces, double confidence) {
		if (numOfTraces % INTERMEDIATE_CONFIDENCE_INTERVAL == 0) {
			intermediateConfidence.put(numOfTraces, confidence);
		}
	}
	
	public Map<Integer,Double> getIntermediateConfidence() {
		return intermediateConfidence;
	}
	
	public long getConfidenceComputationTime() {
		return confidenceComputationTime;
	}

	public void setConfidenceComputationTime(long confidenceComputationTime) {
		this.confidenceComputationTime = confidenceComputationTime;
	}
	
	public class PropertyResults {

		double completeness; // |{p|p's result in log == p's result in model}| / |{p}|

		double redundancy; // shortest prefix of L that's complete / n
		
		int incorrectSeqs; // {e|p(e) in log != p(e) in model}

		String propertyName;

		public PropertyResults(String propertyName) {
			this.propertyName = propertyName;
		}

		public double getCompleteness() {
			return completeness;
		}

		public void setCompleteness(double completeness) {
			this.completeness = completeness;
		}

		public double getRedundancy() {
			return redundancy;
		}

		public void setRedundancy(double redundancy) {
			this.redundancy = redundancy;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}
		
		public void setIncorrectSeqs(int incorrectSeqs) {
			this.incorrectSeqs = incorrectSeqs;
		}
		
		public int getIncorrectSeqs() {
			return incorrectSeqs;
		}

	}

}
