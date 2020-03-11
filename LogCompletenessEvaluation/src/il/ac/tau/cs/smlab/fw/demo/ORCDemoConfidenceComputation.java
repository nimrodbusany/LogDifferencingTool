package il.ac.tau.cs.smlab.fw.demo;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticTraceProvider;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.validation.models.DemoModel;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.completeness.LogCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResultsExporter;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleTrialSingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.demo.DemoResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.demo.SingleModelDemoEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.demo.SingleTrialSingleModelDemoEvaluationResults;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;
import il.ac.tau.cs.smlab.fw.models.FSAModelStatsExtractor;
import il.ac.tau.cs.smlab.fw.models.ModelStatsExtractor;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProviderMock;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import synoptic.model.export.GraphExporter;

public class ORCDemoConfidenceComputation {

	@Test
	public void run() throws SpecMiningAlgorithmException, InvalidModelException, IOException {

		// run demo computation with a single property and a single model
		
		
		
		DemoResults results = run(new DemoAlgorithm(),
				new DemoModel("ORC"));
		
		results.setComputationBreakdown(true);
		
		EvaluationResultsExporter exporter = new EvaluationResultsExporter(results);
	//	exporter.exportToCSV("demo-confidence.csv");
	//	exporter.exportModelStats("demo-confidence-model.csv");
		exporter.exportConfidenceBreakdown("demo-confidence-breakdown.csv");
		exporter.printToScreen();

	}


	@SuppressWarnings("unchecked")
	public DemoResults run(SpecMiningAlgorithm algorithm, FSAInputModel model) throws SpecMiningAlgorithmException, InvalidModelException, IOException {
		/**
		 * 
		 * Evaluation parameters
		 * 
		 */

		// confidence to reach in generated logs
		double minConfidence = 0.999; 
		// maximum number of traces in a log
		int maxNumOfTraces = 25000; 
		// minimum number of traces in a log
		int initialNumOfTraces = 3;

		/**
		 * 
		 * Demo execution
		 * 
		 */
		List<LogProperty<?,?>> props = algorithm.getAlgorithmLogProperties();
		ModelStatsExtractor<FiniteStateAutomaton> modelStatsExtractor = new FSAModelStatsExtractor();
		DemoResults results = new DemoResults();
		System.out.println(model.getModelName());
		SingleModelDemoEvaluationResults modelEvaluationResults = results.newModel(model.getModelName());
		modelEvaluationResults.setModelStats(modelStatsExtractor.getStats(model.convertToFsa()));
		SingleTrialSingleModelEvaluationResults singleTrialResults = modelEvaluationResults.newTrial(props);
		// create trace provider
		FSACoverageTraceGenerator c = model.getCoverage(model.getModelName());
		FSARandomWalkTraceProvider traceProvider = new FSARandomWalkTraceProviderMock(model,c);
		FSA fsa = ((FSARandomWalkTraceProvider) traceProvider).getFsa();
	//	GraphExporter.exportGraph("c:\\demo.dot.txt", fsa.getGraph(), false);
	//	GraphExporter.generatePngFileFromDotFile("c:\\demo.dot.txt");
//		TraceProvider traceProvider = getTraceProvider("ORC2");
	//	System.out.println(traceProvider.getTraceOrder());
		double logConfidence = 0.0D;
		Log log = new Log();
	//	log.setCoverage(traceProvider.getCoverage().getCoverageName());
	//	log.setCoverageParam(traceProvider.getCoverage().getNumOfVisits());
		LogCompletenessEstimator estimator = new LogCompletenessEstimator(props, traceProvider.getAlphabetInTrace());
		while (true) {
			if (log.size() >= initialNumOfTraces && logConfidence > minConfidence) {
				break;
			}
			if (log.size() > maxNumOfTraces) {
				break;
			}
			if (!traceProvider.hasNext()) { // not enough traces to reach confidence
				break;
			}
			ExecutionTrace t = traceProvider.next();
			singleTrialResults.addEventsCount(t.size());
			log.addTrace(t);
			long computationTime = System.currentTimeMillis();
			algorithm.beforePropertiesResults(t);
			estimator.updatePropertyValues(t);
			algorithm.afterPropertiesResults(t);
			logConfidence = estimator.estimate();
			computationTime = System.currentTimeMillis() - computationTime;
			singleTrialResults.setConfidenceComputationTime(computationTime);
			singleTrialResults.addIntermediateConfidence(log.size(), logConfidence);
			addComputationBreakdown(singleTrialResults, estimator,log.size());
		}
		List<ModelProperty<FSA,?>> modelP = new LinkedList<ModelProperty<FSA,?>>();
		for (LogProperty<?,?> logP : algorithm.getAlgorithmLogProperties()) {
			if (logP instanceof ModelProperty<?,?>)
				modelP.add((ModelProperty<FSA,?>) logP);
		}
	//	FSA fsa = ((FSARandomWalkTraceProvider) traceProvider).getFsa();
	//	FSALogEvaluation evaluator = new FSALogEvaluation(fsa,modelP,estimator);
		singleTrialResults.setConfidence(logConfidence);
	//	singleTrialResults.setCompleteness(evaluator.completeness());
	//	singleTrialResults.setRedundancy(evaluator.redundancy(initialNumOfTraces));
	//	singleTrialResults.setIncorrectSeqs(evaluator.incorrectSeqs());
		singleTrialResults.setN(log.size());
		return results;
	}

	
	private void addComputationBreakdown(
			SingleTrialSingleModelEvaluationResults singleTrialResults,
			LogCompletenessEstimator estimator, int n) {
		SingleTrialSingleModelDemoEvaluationResults demoTrialResults = (SingleTrialSingleModelDemoEvaluationResults) singleTrialResults;
		@SuppressWarnings("unchecked")
		LogPropertyValues<Boolean> vals = (LogPropertyValues<Boolean>) estimator.getPropEstimators().get(0).getPropValues();
		Map<EventTypeSeq, Double> conf = new HashMap<EventTypeSeq, Double>(vals.getPropVals().size());
		Map<EventTypeSeq, Double> q = new HashMap<EventTypeSeq, Double>(vals.getPropVals().size());
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
/*			if (sum < n) {
				avg = (double)sum / n; // q_seq
				q.put(seqVals.getKey(), avg);
				conf.put(seqVals.getKey(), Math.pow(avg, n));
			}*/
			if (sum > 0) {
				avg = (double)sum / n;
				q.put(seqVals.getKey(), avg);
				conf.put(seqVals.getKey(), Math.pow(1 - avg, n));
			}
			else {
				q.put(seqVals.getKey(), 0.0);
				conf.put(seqVals.getKey(), 0.0);
			}
		}
		confidence = 1 - confidence;
		demoTrialResults.addConfContribution(conf);
		demoTrialResults.addQValues(q);
		demoTrialResults.addTraceValues(vals.getPropVals());
	}
	
	private TraceProvider getTraceProvider(String log) throws SpecMiningAlgorithmException, InvalidModelException {
		return new SynopticTraceProvider(getSynopticInputParams(log));
	}

	private SynopticInputParams getSynopticInputParams(String model) {
		return new SynopticInputParams("demo/" + model, "--", "(?<TYPE>.*)", null);
	}

}
