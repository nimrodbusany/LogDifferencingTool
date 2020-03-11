package il.ac.tau.cs.smlab.fw.evaluation;

import il.ac.tau.cs.smlab.algorithms.ktails.KTailsAlgorithm;
import il.ac.tau.cs.smlab.algorithms.ktails.KTailsInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticAlgorithm;
import il.ac.tau.cs.smlab.algorithms.synoptic.property.SynopticAlwaysFollowedByProperty;
import il.ac.tau.cs.smlab.algorithms.synoptic.property.SynopticAlwaysPrecedesProperty;
import il.ac.tau.cs.smlab.algorithms.synoptic.property.SynopticNeverFollowedByProperty;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.completeness.LogCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResultsExporter;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleTrialSingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;
import il.ac.tau.cs.smlab.fw.models.FSAModelStatsExtractor;
import il.ac.tau.cs.smlab.fw.models.ModelStatsExtractor;
import il.ac.tau.cs.smlab.fw.property.impl.DirectlyFollowsProperty;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProviderMock;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class MainEvaluationWithConfComputationComparison {

	@SuppressWarnings("unchecked")
	public Map<String,EvaluationResults> run(SpecMiningAlgorithm algorithm, List<FSAInputModel> models, Map<String,List<LogProperty<?,?>>> properties) throws SpecMiningAlgorithmException, InvalidModelException {
		/**
		 * 
		 * Evaluation parameters
		 * 
		 */

		// number of trials
		int trials = 100;
		// confidence to reach in generated logs
		double minConfidence = 0.95; 
		// maximum number of traces in a log
		int maxNumOfTraces = 25000; 
		// minimum number of traces in a log
		int initialNumOfTraces = 10;
		// minimum length of a trace
		int minTraceLength = 0;

		/**
		 * 
		 * Evaluation execution
		 * 
		 */

		ModelStatsExtractor<FiniteStateAutomaton> modelStatsExtractor = new FSAModelStatsExtractor();
		Map<String,EvaluationResults> results = new HashMap<String,EvaluationResults>(properties.size());
		for (String version:properties.keySet()) {
			results.put(version, new EvaluationResults());
		}
		for (FSAInputModel model : models) {
			System.out.println(model.getModelName());
			algorithm.initProperties(model.getModelName());

			// results per property version
			Map<String,SingleModelEvaluationResults> modelEvaluationResults = new HashMap<String,SingleModelEvaluationResults>(properties.size());
			for (String version:properties.keySet()) {
				SingleModelEvaluationResults m = results.get(version).newModel(model.getModelName());
				m.setModelStats(modelStatsExtractor.getStats(model.convertToFsa()));
				modelEvaluationResults.put(version,m);
			}

			for (int i = 0 ; i < trials ; i++) {

				// add a trial for each result object
				Map<String,SingleTrialSingleModelEvaluationResults> currentTrials = new HashMap<String,SingleTrialSingleModelEvaluationResults>(properties.size());

				for (Entry<String,SingleModelEvaluationResults> e:modelEvaluationResults.entrySet()) {
					SingleTrialSingleModelEvaluationResults singleTrialResults = e.getValue().newTrial(properties.get(e.getKey()));
					currentTrials.put(e.getKey(), singleTrialResults);
				}
				TraceProvider traceProvider = getTraceProvider(model);

				// log and confidence for each property version
				Map<String,Double> confidences = new HashMap<String,Double>(properties.size());
				Map<String,Log> logs = new HashMap<String,Log>(properties.size());
				for (String version : properties.keySet()) {
					Log log = new Log();
					log.setCoverage(((FSARandomWalkTraceProvider) traceProvider).getCoverage().getCoverageName());
					log.setCoverageParam(((FSARandomWalkTraceProvider) traceProvider).getCoverage().getNumOfVisits());
					logs.put(version,log);
					confidences.put(version, 0.0D);
				}

				Map<String,LogCompletenessEstimator> estimators = new HashMap<String,LogCompletenessEstimator>(properties.size());
				for (Entry<String,List<LogProperty<?,?>>> e : properties.entrySet()) {
					LogCompletenessEstimator estimator = new LogCompletenessEstimator(e.getValue(), traceProvider.getAlphabetInTrace());
					estimators.put(e.getKey(), estimator);
				}

				Set<String> remaining = new HashSet<String>(properties.size());
				remaining.addAll(properties.keySet());
				Log globalLog = new Log();
				while (!remaining.isEmpty()) {
					if (globalLog.size() > maxNumOfTraces) {
						remaining.clear();
						break;
					}
					if (!traceProvider.hasNext()) { // not enough traces to reach confidence
						remaining.clear();
						break;
					}
					ExecutionTrace t = traceProvider.next();
					if (t.size() <= minTraceLength) continue;
					globalLog.addTrace(t);
					for (String version:remaining) {
						Log log = logs.get(version);
						Double logConfidence = confidences.get(version);
						SingleTrialSingleModelEvaluationResults singleTrialResults = currentTrials.get(version);
						LogCompletenessEstimator estimator = estimators.get(version);
						if (log.size() >= initialNumOfTraces && logConfidence > minConfidence) {
							remaining.remove(version);
							break;
						}
						singleTrialResults.addEventsCount(t.size());
						log.addTrace(t);
						long computationTime = System.currentTimeMillis();
						algorithm.beforePropertiesResults(t);
						estimator.updatePropertyValues(t);
						algorithm.afterPropertiesResults(t);
						logConfidence = estimator.estimate();
						confidences.put(version, logConfidence);
						computationTime = System.currentTimeMillis() - computationTime;
						singleTrialResults.setConfidenceComputationTime(computationTime);
						singleTrialResults.addIntermediateConfidence(log.size(), logConfidence);
					}
				}
				for (String version:properties.keySet()) {
					List<ModelProperty<FSA,?>> modelP = new LinkedList<ModelProperty<FSA,?>>();
					LogCompletenessEstimator estimator = estimators.get(version);
					Log log = logs.get(version);
					SingleTrialSingleModelEvaluationResults singleTrialResults = currentTrials.get(version);
					double logConfidence = confidences.get(version);
					for (LogProperty<?,?> logP : properties.get(version)) {
						if (logP instanceof ModelProperty<?,?>)
							modelP.add((ModelProperty<FSA,?>) logP);

						FSA fsa = ((FSARandomWalkTraceProvider) traceProvider).getFsa();
						FSALogEvaluation evaluator = new FSALogEvaluation(fsa,modelP,estimator);
						singleTrialResults.setConfidence(logConfidence);
						singleTrialResults.setCompleteness(evaluator.completeness());
						singleTrialResults.setRedundancy(evaluator.redundancy(initialNumOfTraces));
						singleTrialResults.setIncorrectSeqs(evaluator.incorrectSeqs());
						singleTrialResults.setN(log.size());
					}
				}
			}
		}
		return results;
	}

	@Test
	public void evaluationForSynoptic() throws SpecMiningAlgorithmException, InvalidModelException {
		// models
		List<FSAInputModel> models = EvaluationModelsManager.getAllEvaluationModels();

		// compare between V1 and V2 invariant properties
		Map<String,List<LogProperty<?,?>>> properties = ImmutableMap.<String, List<LogProperty<?,?>>>builder()
				.put("V1", ImmutableList.<LogProperty<?,?>>builder()
				.add(new DirectlyFollowsProperty())
				.add(new SynopticAlwaysFollowedByProperty())
				.add(new SynopticNeverFollowedByProperty())
				.add(new SynopticAlwaysPrecedesProperty())
				.build())
				
				.put("V2", ImmutableList.<LogProperty<?,?>>builder()
				.add(new DirectlyFollowsProperty("2.0"))
				.add(new SynopticAlwaysFollowedByProperty("2.0"))
				.add(new SynopticNeverFollowedByProperty("2.0"))
				.add(new SynopticAlwaysPrecedesProperty("2.0"))
				.build())
				
				.put("V3", ImmutableList.<LogProperty<?,?>>builder()
				.add(new DirectlyFollowsProperty("3.0"))
				.add(new SynopticAlwaysFollowedByProperty("3.0"))
				.add(new SynopticNeverFollowedByProperty("3.0"))
				.add(new SynopticAlwaysPrecedesProperty("3.0"))
				.build())
				.build();

		// run evaluation
		Map<String,EvaluationResults> results = run(new SynopticAlgorithm(), models,properties);
		for (String version:properties.keySet()) {
			EvaluationResultsExporter exporter = new EvaluationResultsExporter(results.get(version));
			exporter.exportToCSV("evaluation-Synoptic-" +version+ ".csv");
			exporter.exportModelStats("evaluation-Synoptic-models-" +version +".csv");
			exporter.printToScreen();
		}
	}

	@Test
	public void evaluationForKTails() throws SpecMiningAlgorithmException, InvalidModelException {
		// algorithm
		int k = 2;
		SpecMiningAlgorithm algorithm = new KTailsAlgorithm(new KTailsInputParams(k));

		// compare between V1 and V2 existential properties
		Map<String,List<LogProperty<?,?>>> properties = ImmutableMap.<String, List<LogProperty<?,?>>>builder()
				.put("V1", ImmutableList.<LogProperty<?,?>>builder().add(new DirectlyFollowsProperty(k,"1.0")).build()) 
				.put("V3", ImmutableList.<LogProperty<?,?>>builder().add(new DirectlyFollowsProperty(k,"3.0")).build())
				.build();

		// models
		List<FSAInputModel> models = EvaluationModelsManager.getAllEvaluationModels();

		// run evaluation
		Map<String,EvaluationResults> results = run(algorithm, models,properties);
		for (String version:properties.keySet()) {
			EvaluationResultsExporter exporter = new EvaluationResultsExporter(results.get(version));
			exporter.exportToCSV("evaluation-k-tails-" +version+ ".csv");
			exporter.exportModelStats("evaluation-k-tails-models-" +version +".csv");
			exporter.printToScreen();
		}
	}


	/*	@Test
	public void evaluationForLSC() throws FileNotFoundException, SpecMiningAlgorithmException, InvalidModelException {
		// algorithm
		SpecMiningAlgorithm algorithm = new LSCAlgorithm(
				new LSCInputParams(EvaluationModelsManager.getTriggerCharts()));

		// models
		List<FSAInputModel> models = EvaluationModelsManager.getAllEvaluationModels();

		// run evaluation
		EvaluationResults results = run(algorithm, models);
		EvaluationResultsExporter exporter = new EvaluationResultsExporter(results);
		exporter.exportToCSV("evaluation-LSC.csv");
		exporter.exportModelStats("evaluation-LSC-models" + ".csv");
		exporter.printToScreen();
	}*/

	private TraceProvider getTraceProvider(FSAInputModel model) throws SpecMiningAlgorithmException, InvalidModelException {
		FSACoverageTraceGenerator c = model.getCoverage(model.getModelName());
		return new FSARandomWalkTraceProviderMock(model,c);
	}
}

