package il.ac.tau.cs.smlab.fw.demo;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticAlgorithm;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticTraceProvider;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.completeness.LogCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResultsExporter;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleTrialSingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.demo.DemoResults;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class DemoShoppingCartExample {


	SpecMiningAlgorithm algorithm = new SynopticAlgorithm();

	@Test
	public void run() throws SpecMiningAlgorithmException, InvalidModelException {
		String model = "shopping-cart";
		TraceProvider traceProvider;
		DemoResults results = new DemoResults();
		SingleModelEvaluationResults modelEvaluationResults = results.newModel(model);

		traceProvider = getTraceProvider("cart-partial-log");
		computeConfidence(modelEvaluationResults, traceProvider);
		traceProvider = getTraceProvider("cart-full-log");
		computeConfidence(modelEvaluationResults, traceProvider);
		traceProvider = getTraceProvider("cart-redundant-log");
		computeConfidence(modelEvaluationResults, traceProvider);
		new EvaluationResultsExporter(results).printToScreen();
	}


	@SuppressWarnings("unchecked")
	private void computeConfidence(
			SingleModelEvaluationResults modelEvaluationResults, TraceProvider traceProvider)
					throws SpecMiningAlgorithmException, InvalidModelException {
		List<LogProperty<?,?>> properties = algorithm.getAlgorithmLogProperties();
		SingleTrialSingleModelEvaluationResults singleTrialResults = modelEvaluationResults.newTrial(properties);

		double logConfidence = 0.0D;
		Log log = new Log();
		LogCompletenessEstimator estimator = new LogCompletenessEstimator(properties, getAlphabet());
		while (traceProvider.hasNext()) {

			ExecutionTrace t = traceProvider.next();
			singleTrialResults.addEventsCount(t.size());
			log.addTrace(t);
			algorithm.beforePropertiesResults(t);
			estimator.updatePropertyValues(t);
			algorithm.afterPropertiesResults(t);
			logConfidence = estimator.estimate();
		}
		List<ModelProperty<FSA,?>> modelP = new LinkedList<ModelProperty<FSA,?>>();
		for (LogProperty<?,?> logP : algorithm.getAlgorithmLogProperties()) {
			if (logP instanceof ModelProperty<?,?>)
				modelP.add((ModelProperty<FSA,?>) logP);
		}

		singleTrialResults.setConfidence(logConfidence);
		singleTrialResults.setN(log.size());
	}


	private TraceProvider getTraceProvider(String log) throws SpecMiningAlgorithmException, InvalidModelException {
		return new SynopticTraceProvider(getSynopticInputParams(log));
	}


	private SynopticInputParams getSynopticInputParams(String model) {
		return new SynopticInputParams("shopping-cart/" + model, null, "(?<ip>) .+ \"GET HTTP/1.1 /(?<TYPE>.+).php\"", "\\k<ip>");
	}

	private Alphabet getAlphabet() {
		Alphabet ab = new Alphabet();
		ab.addEvent("get-credit-card");
		ab.addEvent("reduce-price");
		ab.addEvent("invalid-coupon");
		ab.addEvent("check-out");
		ab.addEvent("valid-coupon");
		return ab;
	}
}

