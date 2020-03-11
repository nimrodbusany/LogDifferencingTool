
package il.ac.tau.cs.smlab.fw.demo;

import il.ac.tau.cs.smlab.algorithms.ktails.KTailsAlgorithm;
import il.ac.tau.cs.smlab.algorithms.ktails.KTailsInputParams;
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
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import synoptic.algorithms.KTails;
import synoptic.model.ChainsTraceGraph;
import synoptic.model.PartitionGraph;
import synoptic.model.export.GraphExporter;

public class DemoZipOutputStreamExample {

	
	SpecMiningAlgorithm algorithm = new KTailsAlgorithm(new KTailsInputParams(2));

	@Test
	public void run() throws SpecMiningAlgorithmException, InvalidModelException {
		String model = "ZipOutputStream";
		TraceProvider traceProvider;
		DemoResults results = new DemoResults();
		SingleModelEvaluationResults modelEvaluationResults = results.newModel(model);

		traceProvider = getTraceProvider("zip-partial");
		computeConfidence(modelEvaluationResults, traceProvider);
		traceProvider = getTraceProvider("zip-full");
		computeConfidence(modelEvaluationResults, traceProvider);
		traceProvider = getTraceProvider("zip-redundant");
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
		LogCompletenessEstimator estimator = new LogCompletenessEstimator(properties, traceProvider.getAlphabetInTrace());
		System.out.println(traceProvider.getAlphabetInTrace().size());
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


	private SynopticTraceProvider getTraceProvider(String log) throws SpecMiningAlgorithmException, InvalidModelException {
		return new SynopticTraceProvider(getSynopticInputParams(log));
	}


	private static SynopticInputParams getSynopticInputParams(String model) {
		return new SynopticInputParams("zipoutputstream/" + model, "--", "(?<TYPE>.*)", null);
	}
	
	@Test
	public void executeKtails() throws SpecMiningAlgorithmException, InvalidModelException, IOException {
		SynopticTraceProvider traceProvider = getTraceProvider("zip-full");
		ChainsTraceGraph fullGraph = traceProvider.getFullTraceGraph();

		System.out.println("Full log contains " + fullGraph.getNumTraces() + " traces");

		executeAndDrawKTails(fullGraph,"c:\\ktails\\" + "zip"+ "_full.dot");
		
		traceProvider = getTraceProvider("zip-partial");
		fullGraph = traceProvider.getFullTraceGraph();
		System.out.println("Partial log contains " + fullGraph.getNumTraces() + " traces");
		executeAndDrawKTails(fullGraph,"c:\\ktails\\" + "zip"+ "_partial.dot");
		
		traceProvider = getTraceProvider("zip-redundant");
		fullGraph = traceProvider.getFullTraceGraph();
		System.out.println("Redundant log contains " + fullGraph.getNumTraces() + " traces");
		executeAndDrawKTails(fullGraph,"c:\\ktails\\" + "zip"+ "_redundant.dot");
		
		
	}
	
	private PartitionGraph executeAndDrawKTails(ChainsTraceGraph g, String dotFileName) throws IOException {
	//	PartitionGraph pGraph = new PartitionGraph(g, false, null);
	//	GraphExporter.exportGraph(dotFileName, pGraph, false);
	//	GraphExporter.generatePngFileFromDotFile(dotFileName);
		
		
		PartitionGraph p = KTails.performKTails(g, 1);
		GraphExporter.exportGraph(dotFileName, p, false);
		GraphExporter.generatePngFileFromDotFile(dotFileName);
		return p;
	}

}
