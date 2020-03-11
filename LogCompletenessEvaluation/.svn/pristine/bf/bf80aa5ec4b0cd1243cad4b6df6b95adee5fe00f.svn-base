package il.ac.tau.cs.smlab.fw.evaluation;

import il.ac.tau.cs.smlab.algorithms.ktails.KTailsAlgorithm;
import il.ac.tau.cs.smlab.algorithms.ktails.KTailsInputParams;
import il.ac.tau.cs.smlab.algorithms.lsc.LSCAlgorithm;
import il.ac.tau.cs.smlab.algorithms.lsc.LSCInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticAlgorithm;
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
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProviderMock;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class MainEvaluation {
	
	@SuppressWarnings("unchecked")
	public EvaluationResults run(SpecMiningAlgorithm algorithm, List<FSAInputModel> models) throws SpecMiningAlgorithmException, InvalidModelException {
		/**
		 * 
		 * Evaluation parameters
		 * 
		 */

		// number of trials
		int trials = 200;
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

		List<LogProperty<?,?>> properties = algorithm.getAlgorithmLogProperties();
		ModelStatsExtractor<FiniteStateAutomaton> modelStatsExtractor = new FSAModelStatsExtractor();
		EvaluationResults results = new EvaluationResults();
		for (FSAInputModel model : models) {
			System.out.println(model.getModelName());
			algorithm.initProperties(model.getModelName());
			SingleModelEvaluationResults modelEvaluationResults = results.newModel(model.getModelName());
			modelEvaluationResults.setModelStats(modelStatsExtractor.getStats(model.convertToFsa()));
			for (int i = 0 ; i < trials ; i++) {
				SingleTrialSingleModelEvaluationResults singleTrialResults = modelEvaluationResults.newTrial(properties);
				List<TraceProvider> allTraceProviders = getTraceProviders(model);

				for (TraceProvider traceProvider : allTraceProviders) {
					double logConfidence = 0.0D;
					Log log = new Log();
					log.setCoverage(((FSARandomWalkTraceProvider) traceProvider).getCoverage().getCoverageName());
					log.setCoverageParam(((FSARandomWalkTraceProvider) traceProvider).getCoverage().getNumOfVisits());
					LogCompletenessEstimator estimator = new LogCompletenessEstimator(properties, traceProvider.getAlphabetInTrace());
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
						if (t.size() <= minTraceLength) continue;
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
					}
					List<ModelProperty<FSA,?>> modelP = new LinkedList<ModelProperty<FSA,?>>();
					for (LogProperty<?,?> logP : algorithm.getAlgorithmLogProperties()) {
						if (logP instanceof ModelProperty<?,?>)
							modelP.add((ModelProperty<FSA,?>) logP);
					}
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
		return results;
	}
	
	@Test
	public void evaluationForSynoptic() throws SpecMiningAlgorithmException, InvalidModelException {
		// models
		List<FSAInputModel> models = EvaluationModelsManager.getDavidEvaluationModels();
		
		// run evaluation
		EvaluationResults results = run(new SynopticAlgorithm(), models);
		EvaluationResultsExporter exporter = new EvaluationResultsExporter(results);
		exporter.exportToCSV("evaluation-Synoptic.csv");
		exporter.exportModelStats("evaluation-Synoptic-models" +".csv");
		exporter.printToScreen();
	
	}
	
	@Test
	public void evaluationForKTails() throws SpecMiningAlgorithmException, InvalidModelException {
		// algorithm
		int k = 2;
		SpecMiningAlgorithm algorithm = new KTailsAlgorithm(new KTailsInputParams(k));
		
		// models
		List<FSAInputModel> models = EvaluationModelsManager.getPradelModels();
		
		// run evaluation
		EvaluationResults results = run(algorithm, models);
		EvaluationResultsExporter exporter = new EvaluationResultsExporter(results);
		exporter.exportToCSV("evaluation-k-tails.csv");
		exporter.exportModelStats("evaluation-k-tails-models" +".csv");
		exporter.printToScreen();
	}
	
	
	@Test
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
	}

	private List<TraceProvider> getTraceProviders(FSAInputModel model) throws SpecMiningAlgorithmException, InvalidModelException {
		List<TraceProvider> traceProviders = new ArrayList<TraceProvider>(1);
		FSACoverageTraceGenerator c = model.getCoverage(model.getModelName());
		traceProviders.add(new FSARandomWalkTraceProviderMock(model,c));
		return traceProviders;
	}
}

