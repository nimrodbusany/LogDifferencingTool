package il.ac.tau.cs.smlab.fsa.validation;

import il.ac.tau.cs.smlab.algorithms.ktails.KTailsAlgorithm;
import il.ac.tau.cs.smlab.algorithms.ktails.KTailsInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticAlgorithm;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInvocation;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticTraceProvider;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.completeness.LogCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.completeness.RelativeCompletenessCalculator;
import il.ac.tau.cs.smlab.fw.evaluation.EvaluationModelsManager;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;
import il.ac.tau.cs.smlab.fw.property.impl.EventuallyProperty;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.property.log.PropertyCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProviderMock;
import il.ac.tau.cs.smlab.fw.trace.generator.TraceSlicer;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import synoptic.algorithms.KTails;
import synoptic.main.SynopticMain;
import synoptic.model.ChainsTraceGraph;
import synoptic.model.PartitionGraph;
import synoptic.model.export.GraphExporter;

public class LPValidation {

	public final static int K = 2;


	@Test
	public void ktailsValidation() throws SpecMiningAlgorithmException, InvalidModelException, IOException {
		KTailsAlgorithm algorithm = new KTailsAlgorithm(new KTailsInputParams(K));
	//	algorithm.addLogProperty(new TerminalEventProperty());
		// validate each model
		List<FSAInputModel> models = EvaluationModelsManager.getPradelModels();
		for (FSAInputModel model : models) {
			validateKtails(model,algorithm);
		}

	}


	@Test
	public void synopticValidation() throws Exception {
		SynopticAlgorithm algorithm = new SynopticAlgorithm();
		algorithm.addLogProperty(new EventuallyProperty());
		
		// validate each model
		List<FSAInputModel> models = EvaluationModelsManager.getPradelModels();
		for (FSAInputModel model : models) {
			validateSynoptic(model,algorithm);
		}

	}


	// load the full generated trace, then create a minimal partial log that has the exact same property values
	// run k-tails on both logs and compare generated models

	// supports only a single property for now
	public void validateKtails(FSAInputModel model,SpecMiningAlgorithm algorithm) throws SpecMiningAlgorithmException, InvalidModelException, IOException {

		// load full log, fullLog (thousands of traces)
		SynopticTraceProvider traceProvider = getTraceProvider(model.getModelName());
		List<PropertyCompletenessEstimator<?,?>> fullAgg = extractPropertyValuesFromLog(traceProvider,algorithm); // values in the full log
		String fullLogFilename = traceProvider.getLogFilename();
		System.out.println("Full Log:" + fullLogFilename);

		// produce a partial log that has the same property results, partialLog
		traceProvider = getTraceProvider(model.getModelName());
		List<Integer> tracesIndexes = generatePartialLog(traceProvider,algorithm,fullAgg); 
		String fname = traceProvider.getLogFilename();
		TraceSlicer slicer = new TraceSlicer(fname);
		String partialLogFilename = fname.substring(0, fname.indexOf(".txt")) + "_partial.txt";
		slicer.slice(tracesIndexes, partialLogFilename);
		System.out.println("Partial Log:" + partialLogFilename );
		// obtain graphs of both logs
		ChainsTraceGraph fullGraph = traceProvider.getFullTraceGraph();
		ChainsTraceGraph partialGraph = getTraceProvider(model.getModelName() + "_partial").getFullTraceGraph();

		System.out.println("Partial log contains " + partialGraph.getNumTraces() + " traces");
		System.out.println("Full log contains " + fullGraph.getNumTraces() + " traces");

		// compare generated models k-tails(fullLog) vs. k-tails(partialLog)
		PartitionGraph par = executeAndDrawKTails(partialGraph, SystemConstants.RESOURCES_DIRECTORY + "validation/ktails/" + model.getModelName() +"_partial.dot");
		PartitionGraph full = executeAndDrawKTails(fullGraph,SystemConstants.RESOURCES_DIRECTORY + "validation/ktails/" + model.getModelName()+ "_full.dot");

		compareGraphs(par,full);


	}

	private void compareGraphs(PartitionGraph par, PartitionGraph full) {

	}


	private PartitionGraph executeAndDrawKTails(ChainsTraceGraph g, String dotFileName) throws IOException {
		PartitionGraph p = KTails.performKTails(g, K-1);
		GraphExporter.exportGraph(dotFileName, p, false);
		GraphExporter.generatePngFileFromDotFile(dotFileName);
		return p;
	}


	private List<Integer> generatePartialLog(SynopticTraceProvider traceProvider,
			SpecMiningAlgorithm algorithm, List<PropertyCompletenessEstimator<?,?>> fullAgg) throws SpecMiningAlgorithmException {

		List<LogProperty<?,?>> properties = algorithm.getAlgorithmLogProperties();
		LogCompletenessEstimator estimator = new LogCompletenessEstimator(properties, traceProvider.getAlphabetInTrace());
		double relativeCompleteness;
		Log log = new Log();
		while (true) {
			ExecutionTrace t = traceProvider.next();
			log.addTrace(t);
			algorithm.beforePropertiesResults(t);
			estimator.updatePropertyValues(t);
			algorithm.afterPropertiesResults(t);

			relativeCompleteness = RelativeCompletenessCalculator.compute(fullAgg, estimator.getPropEstimators());
			if (relativeCompleteness == 1.0 ) { // full and partial contain the exact same property values
				return traceProvider.getTraceOrder().subList(0, log.size());
			}
		}
	}


	private List<PropertyCompletenessEstimator<?,?>> extractPropertyValuesFromLog(TraceProvider traceProvider,SpecMiningAlgorithm algorithm) throws SpecMiningAlgorithmException {
		List<LogProperty<?,?>> properties = algorithm.getAlgorithmLogProperties();
		LogCompletenessEstimator estimator = new LogCompletenessEstimator(properties, traceProvider.getAlphabetInTrace());
		Log log = new Log();
		while (true) {
			if (!traceProvider.hasNext()) { // reached end of log
				return estimator.getPropEstimators();
			}
			ExecutionTrace t = traceProvider.next();
			log.addTrace(t);
			algorithm.beforePropertiesResults(t);
			estimator.updatePropertyValues(t);
			algorithm.afterPropertiesResults(t);
		}
	}

	private SynopticTraceProvider getTraceProviderForFullLog(FSAInputModel model) throws SpecMiningAlgorithmException, InvalidModelException {
		FSACoverageTraceGenerator c = model.getCoverage(model.getModelName());
		return new FSARandomWalkTraceProviderMock(model,c);
	}

	private SynopticTraceProvider getTraceProviderForPartialLog(String log) throws SpecMiningAlgorithmException, InvalidModelException {
		return new SynopticTraceProvider(getSynopticInputParams(log));
	}

	private SynopticTraceProvider getTraceProvider(String log) throws SpecMiningAlgorithmException, InvalidModelException {
		return new SynopticTraceProvider(getSynopticInputParams(log));
	}

	private SynopticInputParams getSynopticInputParams(String log) {
		return new SynopticInputParams("validation/" + log, "--", "(?<TYPE>.*)", null);
	}


	public void validateSynoptic(FSAInputModel model,SpecMiningAlgorithm algorithm) throws Exception {

		// load full log, fullLog (thousands of traces)
		SynopticTraceProvider traceProvider = getTraceProvider(model.getModelName());
		List<PropertyCompletenessEstimator<?,?>> fullAgg = extractPropertyValuesFromLog(traceProvider,algorithm); // values in the full log
		String fullLogFilename = traceProvider.getLogFilename();
		System.out.println("Full Log:" + fullLogFilename);

		// produce a partial log that has the same property results, partialLog
		traceProvider = getTraceProvider(model.getModelName());
		List<Integer> tracesIndexes = generatePartialLog(traceProvider,algorithm,fullAgg); 
		String fname = traceProvider.getLogFilename();
		TraceSlicer slicer = new TraceSlicer(fname);
		String partialLogFilename = fname.substring(0, fname.indexOf(".txt")) + "_partial.txt";
		slicer.slice(tracesIndexes, partialLogFilename);
		System.out.println("Partial Log:" + partialLogFilename );
		// obtain graphs of both logs
		ChainsTraceGraph fullGraph = traceProvider.getFullTraceGraph();

		System.out.println("Partial log contains " + tracesIndexes.size() + " traces");
		System.out.println("Full log contains " + fullGraph.getNumTraces() + " traces");

		// compare generated models Synoptic(fullLog) vs. Synoptic(partialLog)
		executeAndDrawSynoptic(model.getModelName() +"_partial");
		executeAndDrawSynoptic(model.getModelName());

	}


	private void executeAndDrawSynoptic(String log) throws Exception {
		SynopticInputParams params = getSynopticInputParams(log);
		params.onlyMineInvariants = false;
		params.outputInvariantsToFile = true;
		SynopticInvocation invc = new SynopticInvocation(params);
		SynopticMain mainInstance = invc.processSynopticArgs();
		
	     PartitionGraph pGraph = mainInstance.createInitialPartitionGraph();
         if (pGraph != null) {
             mainInstance.runSynoptic(pGraph);
         }
	}

}
