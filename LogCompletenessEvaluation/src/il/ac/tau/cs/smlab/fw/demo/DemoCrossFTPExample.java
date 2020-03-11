package il.ac.tau.cs.smlab.fw.demo;

import il.ac.tau.cs.smlab.algorithms.lsc.LSCAlgorithm;
import il.ac.tau.cs.smlab.algorithms.lsc.LSCInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticTraceProvider;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.completeness.LogCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResultsExporter;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleTrialSingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.demo.DemoResults;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DemoCrossFTPExample {


	SpecMiningAlgorithm algorithm = new LSCAlgorithm(new LSCInputParams(Arrays.asList(
			"void org.apache.ftpserver.ConnectionManagerImpl.newConnection(IConnection)",
			"long org.apache.ftpserver.RequestHandler.transfer(BufferedInputStream, BufferedOutputStream, int)"), false));

	@Test
	public void run() throws SpecMiningAlgorithmException, InvalidModelException {
		String model = "CrossFTP";
		TraceProvider traceProvider;
		EvaluationResults results = new DemoResults();
		SingleModelEvaluationResults modelEvaluationResults = results.newModel(model);

		traceProvider = getTraceProvider(false);
		System.out.println(traceProvider.getAlphabetInTrace().size());
		computeConfidence(modelEvaluationResults, traceProvider);
		traceProvider = getTraceProvider(true);
		System.out.println(traceProvider.getAlphabetInTrace().size());
		computeConfidence(modelEvaluationResults, traceProvider);

		EvaluationResultsExporter exporter = new EvaluationResultsExporter(results);
		exporter.setShowIntermediateConfidence(false);
		exporter.printToScreen();

	}


	private void computeConfidence(
			SingleModelEvaluationResults modelEvaluationResults, TraceProvider traceProvider)
					throws SpecMiningAlgorithmException, InvalidModelException {
		List<LogProperty<?,?>> properties = algorithm.getAlgorithmLogProperties();
		SingleTrialSingleModelEvaluationResults singleTrialResults = modelEvaluationResults.newTrial(properties);

		double logConfidence = 0.0D;
		Log log = new Log();
		LogCompletenessEstimator estimator = new LogCompletenessEstimator(properties, traceProvider.getAlphabetInTrace());
		while (traceProvider.hasNext()) {
			ExecutionTrace t = traceProvider.next();
			singleTrialResults.addEventsCount(t.size());
			log.addTrace(t);
			algorithm.beforePropertiesResults(t);
			estimator.updatePropertyValues(t);
			algorithm.afterPropertiesResults(t);
			logConfidence = estimator.estimate();
		}

/*		int count = 0;
		AggregatedLogPropertyValues aggVals = estimator.getPropEstimators().get(0).getAggregatedPropertyValues();
		for (Entry<EventTypeSeq, Boolean> e:aggVals.getAggLogPropVals().entrySet()) {
			if (e.getValue().equals(true)) {
				System.out.println(e.getKey());
				++count;
			}
		}*/
		singleTrialResults.setConfidence(logConfidence);
		singleTrialResults.setN(log.size());
	}


	private TraceProvider getTraceProvider(boolean full) throws SpecMiningAlgorithmException, InvalidModelException {
		if (full) {
			return new SynopticTraceProvider(getSynopticInputParams("crossftp-full"));
		}
		return new SynopticTraceProvider(getSynopticInputParams("crossftp-partial"));
	}


	private SynopticInputParams getSynopticInputParams(String model) {
		return new SynopticInputParams("crossftp/" + model, "--", "(?<TYPE>.*)", null);
	}

}

