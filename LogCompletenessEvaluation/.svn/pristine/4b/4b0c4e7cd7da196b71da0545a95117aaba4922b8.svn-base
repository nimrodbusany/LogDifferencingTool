package il.ac.tau.cs.smlab.fw.evaluation.results;


import il.ac.tau.cs.smlab.fw.models.ModelStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import com.google.common.primitives.Doubles;

public class EvaluationResults {

	// number of allowed incorrect sequences;
	public double incorrectSeqs;

	protected List<SingleModelEvaluationResults> models = new LinkedList<SingleModelEvaluationResults>();

	public EvaluationResults(int incorrectSeqs) {
		this.incorrectSeqs = incorrectSeqs;
	}
	
	public EvaluationResults() {
		this(0);
	}

	public SingleModelEvaluationResults newModel(String model) {
		SingleModelEvaluationResults m =  new SingleModelEvaluationResults(model);
		models.add(m);
		return m;
	}

	public String generateToString(boolean showIntermediateConfidence) {

		List<Double> allLogSizes = new LinkedList<Double>();
		List<Double> allTraceSizes = new LinkedList<Double>();
		List<Double> allReliability = new LinkedList<Double>();
		List<Double> allConfidence = new LinkedList<Double>();
		List<Double> allRedundancy = new LinkedList<Double>();

		StringBuilder sb = new StringBuilder();

		for (SingleModelEvaluationResults mr : models) {

			List<Double> logSizes = new LinkedList<Double>();
			List<Double> traceSizes = new LinkedList<Double>();
			List<Double> completeness = new LinkedList<Double>();
			List<Double> confidence = new LinkedList<Double>();
			List<Double> redundancy = new LinkedList<Double>();
			List<Double> incorrect = new LinkedList<Double>();
			Map<Integer,List<Double>> intermediateConfidence = new TreeMap<Integer,List<Double>>();
			List<Long> time = new LinkedList<Long>();

			for (SingleTrialSingleModelEvaluationResults t:mr.getTrials()) {
				confidence.add(t.getConfidence());
				logSizes.add((double)t.getN());
				traceSizes.add(t.getAverageTraceSize());
				completeness.add(t.getAverageCompleteness());
				redundancy.add(t.getMinRedundancy());
				incorrect.add(t.getMaxIncorrectSeqs());
				time.add(t.getConfidenceComputationTime());
				if (showIntermediateConfidence) {
					addTrialIntermediateConfidence(intermediateConfidence,t.getIntermediateConfidence());
				}
			}

			int numOfTrials = mr.getTrials().size();
			int numOfComplete = countCompleteLogs(incorrect);
			double reliability = (double)numOfComplete/numOfTrials;
			double numOfSequences = maxIncorrectSeqs(incorrect);

			sb.append("Model: ");
			sb.append(mr.getModel());
			sb.append("\n");
			sb.append("log size (avg): " + avg(logSizes));
			sb.append(", trace size (avg): " + avg(traceSizes));
			sb.append("\n");
			sb.append("reliability: " + numOfComplete + "/" + numOfTrials + " (" + reliability + ") ");
			sb.append("\n");
			sb.append("confidence (mean,sd): " + avg(confidence) + "," + sd(confidence));
			sb.append("\n");
			sb.append("redundancy (mean,sd): " + avg(positiveFilter(redundancy)) + "," + sd(positiveFilter(redundancy)));
			sb.append("\n");
			sb.append("incorrect sequences(max): " + numOfSequences);
			sb.append("\n");
			sb.append("confidence computation time(avg): " + avgl(time));
			sb.append("\n");
			if (showIntermediateConfidence) {
				sb.append("intermediate confidence values (avg)[");
				for (int i : intermediateConfidence.keySet()) {
					sb.append(i+",");
				}
				sb.setLength(sb.length() - 1);
				sb.append("]=[");
				for (List<Double> l : intermediateConfidence.values()) {
					sb.append(avg(l)+",");
				}
				sb.setLength(sb.length() - 1);
				sb.append("]");

			}
			sb.append("\n");
			sb.append("\n");

			allLogSizes.addAll(logSizes);
			allTraceSizes.addAll(traceSizes);
			allConfidence.addAll(confidence);
			allRedundancy.addAll(redundancy);
			allReliability.add(reliability);
		}

		sb.append("Overall: ");
		sb.append("\n");

		sb.append("log size (avg): " + avg(allLogSizes));
		sb.append(", trace size (avg): " + avg(allTraceSizes));
		sb.append("\n");
		sb.append("reliability (avg): " + avg(allReliability) );
		sb.append("\n");
		sb.append("confidence (mean,sd): " + avg(allConfidence) + "," + sd(allConfidence));
		sb.append("\n");
		sb.append("redundancy (mean,sd): " + avg(positiveFilter(allRedundancy)) + "," + sd(positiveFilter(allRedundancy)));
		sb.append("\n");
		sb.append("\n");

		return sb.toString();

	}


	private void addTrialIntermediateConfidence(
			Map<Integer, List<Double>> modelConfs,
			Map<Integer, Double> trialConfs) {

		for (Entry<Integer,Double> e : trialConfs.entrySet()) {
			if (!modelConfs.containsKey(e.getKey())) {
				modelConfs.put(e.getKey(), new LinkedList<Double>());
			}
			List<Double> l = modelConfs.get(e.getKey());
			l.add(e.getValue());
		}
	}

	protected double avg(List<Double> l) {
		double avg = 0;
		for (Double d : l) {
			avg += (double) d;
		}
		avg = avg / l.size();
		return avg;
	}
	

	protected List<Double> positiveFilter(List<Double> l) {
		List<Double> p = new ArrayList<Double>(l.size());
		for (Double d : l) {
			if (d > 0) {
				p.add(d);
			}
		}
		if (p.isEmpty()) p.add(0.0);
		return p;
	}
	
	protected long avgl(List<Long> l) {
		long avg = 0;
		for (Long d : l) {
			avg += d;
		}
		avg = avg / l.size();
		return avg;
	}


	protected double sd(List<Double> l) {
		StandardDeviation sd = new StandardDeviation();
		return sd.evaluate(Doubles.toArray(l));
	}

	protected int countCompleteLogs(List<Double> incorrect) {
		int sum = 0;
		for (double c: incorrect) {
			if (c <= incorrectSeqs) {
				++sum;
			}
		}
		return sum;
	}

	protected double maxIncorrectSeqs(List<Double> incorrect) {
		double max = 0;
		for (int i = 0 ; i<incorrect.size();++i) {
				max = Math.max(incorrect.get(i),max);
		}
		return max;
	}

	@Override
	public String toString() {
		return generateToString(false);
	}

	public String toString(boolean showIntermediateConfidence) {
		return generateToString(showIntermediateConfidence);
	}

	public List<String[]> getResultEntries() {
		List<String[]> entries = new ArrayList<String[]>(models.size() + 2);

		// add headline 
		String[] headlineArr = new String[]{"model","log size (mean)","confidence (mean)/confidence (sd)",
				"reliability (mean)/reliability (sd)","redundancy (mean)/redundancy (sd)","trace size (mean)",
				"complete logs","trials", "incorrect sequences(max)", "computation time (mean)"};

		List<String> headline = new ArrayList<String>(headlineArr.length);
		headline.addAll(Arrays.asList(headlineArr));
		entries.add(headlineArr);

		List<Double> allLogSizes = new LinkedList<Double>();
		List<Double> allTraceSizes = new LinkedList<Double>();
		List<Double> allReliability = new LinkedList<Double>();
		List<Double> allConfidence = new LinkedList<Double>();
		List<Double> allRedundancy = new LinkedList<Double>();

		for (SingleModelEvaluationResults mr : models) {

			List<Double> logSizes = new LinkedList<Double>();
			List<Double> traceSizes = new LinkedList<Double>();
			List<Double> completeness = new LinkedList<Double>();
			List<Double> confidence = new LinkedList<Double>();
			List<Double> redundancy = new LinkedList<Double>();
			List<Double> incorrect = new LinkedList<Double>();
			List<Long> time = new LinkedList<Long>();
			

			for (SingleTrialSingleModelEvaluationResults t:mr.getTrials()) {
				confidence.add(t.getConfidence());
				logSizes.add((double)t.getN());
				traceSizes.add(t.getAverageTraceSize());
				completeness.add(t.getAverageCompleteness());
				redundancy.add(t.getMinRedundancy());
				incorrect.add(t.getMaxIncorrectSeqs());
				time.add(t.getConfidenceComputationTime());
			}

			int numOfTrials = mr.getTrials().size();
			int numOfComplete = countCompleteLogs(incorrect);
			double reliability = (double)numOfComplete/numOfTrials;
			double numOfSequences = maxIncorrectSeqs(incorrect);

			String[] entry = new String[(headlineArr.length)];
			entry[headline.indexOf("model")] = mr.getModel();
			entry[headline.indexOf("log size (mean)")] = String.valueOf(avg(logSizes));
			entry[headline.indexOf("trace size (mean)")] = String.valueOf(avg(traceSizes));
			entry[headline.indexOf("redundancy (mean)/redundancy (sd)")] = String.format("%.2f",avg(positiveFilter(redundancy))) + "/" + String.format("%.2f",sd(positiveFilter(redundancy)));
			entry[headline.indexOf("reliability (mean)/reliability (sd)")] = String.format("%.2f",reliability);
			entry[headline.indexOf("complete logs")] = String.valueOf(numOfComplete);
			entry[headline.indexOf("trials")] = String.valueOf(numOfTrials);
			entry[headline.indexOf("confidence (mean)/confidence (sd)")] = String.format("%.2f",avg(confidence));
			entry[headline.indexOf("incorrect sequences(max)")] = String.valueOf(numOfSequences);
			entry[headline.indexOf("computation time (mean)")] = String.valueOf(avgl(time));
			

			allLogSizes.addAll(logSizes);
			allTraceSizes.addAll(traceSizes);
			allConfidence.addAll(confidence);
			allRedundancy.addAll(redundancy);
			allReliability.add(reliability);

			entries.add(entry);
		}
		return entries;
	}

	public List<String[]> getModelsEntries() {

		List<String[]> entries = new ArrayList<String[]>(4);
		// add headline, optimally this part would be model-type independent
		String[] headlineArr = new String[]{"model","alphabet","states","transitions"};
		List<String> headline = new ArrayList<String>(headlineArr.length);
		headline.addAll(Arrays.asList(headlineArr));
		entries.add(headlineArr);

		for (SingleModelEvaluationResults mr : models) {
			ModelStats s = mr.getModelStats();
			
			String[] entry = new String[(headlineArr.length)];
			entry[headline.indexOf("model")] = mr.getModel();
			entry[headline.indexOf("alphabet")] = s.get("alphabet");
			entry[headline.indexOf("states")] = s.get("states");
			entry[headline.indexOf("transitions")] = s.get("transitions");
			entries.add(entry);
		}
		return entries;
	}

	public List<String[]> getBreakdownEntries() {
		throw new UnsupportedOperationException("Only supported for DemoResults");
	}
	
}
