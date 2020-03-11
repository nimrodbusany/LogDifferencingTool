package il.ac.tau.cs.smlab.fw.evaluation.results.demo;

import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleTrialSingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.models.ModelStats;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


// class for our demo examples
// the output is more detailed than the regular evaluation results output (also includes individual trial info)
// but it does not contain reliability/redundancy info since we don't have an actual FSA model 
public class DemoResults extends EvaluationResults {

	boolean showComputationBreakdown = false;
	protected List<SingleModelDemoEvaluationResults> models = new LinkedList<SingleModelDemoEvaluationResults>();

	public SingleModelDemoEvaluationResults newModel(String model) {
		SingleModelDemoEvaluationResults m =  new SingleModelDemoEvaluationResults(model);
		models.add(m);
		return m;
	}

	public void setComputationBreakdown(boolean showComputationBreakdown) {
		this.showComputationBreakdown = showComputationBreakdown;
	}

	@Override
	public String generateToString(boolean showIntermediateConfidence) {

		StringBuilder sb = new StringBuilder();

		for (SingleModelDemoEvaluationResults mr : models) {

			List<Double> logSizes = new LinkedList<Double>();
			List<Double> traceSizes = new LinkedList<Double>();
			List<Double> confidence = new LinkedList<Double>();

			for (SingleTrialSingleModelEvaluationResults t:mr.getTrials()) {
				confidence.add(t.getConfidence());
				logSizes.add((double)t.getN());
				traceSizes.add(t.getAverageTraceSize());
			}

			sb.append("Model: ");
			sb.append(mr.getModel());
			sb.append("\n");
			sb.append("log sizes: " + logSizes);
			sb.append(", trace size (avg): " + avg(traceSizes));
			sb.append("\n");
			sb.append("confidence: " + confidence); 
			sb.append("\n");
			sb.append("\n");

			if (showComputationBreakdown) {
				assert(mr.getTrials().size() == 1);
				SingleTrialSingleModelDemoEvaluationResults dt = (SingleTrialSingleModelDemoEvaluationResults) mr.getTrials().get(0);

				// title
				sb.append("es");
				sb.append("\t\t");
				for (int i = 1 ; i <= dt.getN() ; i++) {
					sb.append("n="+i);
					sb.append("\t\t\t");
				}
				sb.append("\n");
				// check that we have the values for all sequences
				assert(dt.getqValues().size() == dt.getConfContribution().size());
				assert(dt.getTraceValues().size() == dt.getConfContribution().size());

				Set<EventTypeSeq> seqs = dt.getqValues().get(0).keySet();
				for (EventTypeSeq es : seqs) {
					sb.append(es);
					for (int i = 0 ; i< dt.getN(); i++) {
						sb.append("\t\t");
						sb.append(String.format("%.3f",dt.getqValues().get(i).get(es)));
						sb.append(String.format(" (-%.3f)",dt.getConfContribution().get(i).get(es)));
					}
					sb.append("\n");
				}
				sb.append("total");
				for (int i = 0 ; i< dt.getN(); i++) {
					sb.append("\t\t\t");
					sb.append(String.format("%.3f",1-sum(dt.getConfContribution().get(i).values())));
				}
			}
		}
		return sb.toString();

	}

	private double sum(Collection<Double> values) {
		double sum = 0;
		for (double val: values) {
			sum += val;
		}
		return sum;
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
	
	public List<String[]> getResultEntries() {
		List<String[]> entries = new ArrayList<String[]>(models.size() + 2);

		// add headline 
		String[] headlineArr = new String[]{"model","confidence (mean)", "confidence (sd)", "log size (mean)", "trace size (mean)",
				"reliability (mean)", "reliability (sd)", "redundancy (mean)", "redundancy (sd)",
				"complete logs","trials", "incorrect sequences(avg)", "computation time (mean)"};

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
				redundancy.add(t.getAverageRedundancy());
				incorrect.add(t.getMaxIncorrectSeqs());
				time.add(t.getConfidenceComputationTime());
			}

			int numOfTrials = mr.getTrials().size();
			int numOfComplete = countCompleteLogs(completeness);
			double reliability = (double)numOfComplete/numOfTrials;
			double numOfSequences = maxIncorrectSeqs(incorrect,completeness);

			String[] entry = new String[(headlineArr.length)];
			entry[headline.indexOf("model")] = mr.getModel();
			entry[headline.indexOf("log size (mean)")] = String.valueOf(avg(logSizes));
			entry[headline.indexOf("trace size (mean)")] = String.valueOf(avg(traceSizes));
			entry[headline.indexOf("redundancy (mean)")] = String.valueOf(avg(redundancy));
			entry[headline.indexOf("redundancy (sd)")] = String.valueOf(sd(redundancy));
			entry[headline.indexOf("reliability (mean)")] = String.valueOf(reliability);
			entry[headline.indexOf("reliability (sd)")] = String.valueOf(sd(completeness));
			entry[headline.indexOf("complete logs")] = String.valueOf(numOfComplete);
			entry[headline.indexOf("trials")] = String.valueOf(numOfTrials);
			entry[headline.indexOf("confidence (mean)")] = String.valueOf(avg(confidence));
			entry[headline.indexOf("confidence (sd)")] = String.valueOf(sd(confidence));
			entry[headline.indexOf("incorrect sequences(avg)")] = String.valueOf(numOfSequences);
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

	@Override
	public List<String[]> getBreakdownEntries() {

		SingleTrialSingleModelDemoEvaluationResults dt = (SingleTrialSingleModelDemoEvaluationResults) models.get(0).getTrials().get(0);
		
		List<String[]> entries = new ArrayList<String[]>(models.size() + 2);
		
		// add headline
		String[] headlineArr = new String[dt.getN() + 1];
		headlineArr[0] = "es";
		for (int i = 1 ; i<= dt.getN() ; ++i) {
			headlineArr[i] = "n="+i;
		}

		List<String> headline = new ArrayList<String>(headlineArr.length);
		headline.addAll(Arrays.asList(headlineArr));
		entries.add(headlineArr);
		
		assert(dt.getqValues().size() == dt.getConfContribution().size());
		assert(dt.getTraceValues().size() == dt.getConfContribution().size());

		Set<EventTypeSeq> seqs = dt.getqValues().get(0).keySet();
		for (EventTypeSeq es : seqs) {
			String[] entry = new String[(headlineArr.length)];
			entry[headline.indexOf("es")] = "["+es.getEvent(0) + ";" + es.getEvent(1) + "]";
			for (int i = 0 ; i < dt.getN(); i++) {
				String val = String.format("%.2f",dt.getqValues().get(i).get(es)) + String.format(" (-%.2f)",dt.getConfContribution().get(i).get(es));
				entry[headline.indexOf("n="+String.valueOf(i+1))]=val;
			}
			entries.add(entry);
		}
		String[] entry = new String[(headlineArr.length)];
		entry[headline.indexOf("es")]="total";
		for (int i = 0 ; i < dt.getN(); i++) {
			entry[headline.indexOf("n="+String.valueOf(i+1))]=String.format("%.2f",1-sum(dt.getConfContribution().get(i).values()));
		}
		entries.add(entry);
		return entries;
	}
}
