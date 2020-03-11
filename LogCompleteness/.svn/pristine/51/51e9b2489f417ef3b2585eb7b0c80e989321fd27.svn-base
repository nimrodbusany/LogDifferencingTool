/*package il.ac.tau.cs.smlab.fw;

import il.ac.tau.cs.smlab.algorithms.ktails.KTailsAlgorithm;
import il.ac.tau.cs.smlab.algorithms.ktails.KTailsInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticAlgorithm;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInvocation;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.FSAGenerator;
import il.ac.tau.cs.smlab.fsa.FSAInvariantChecker;
import il.ac.tau.cs.smlab.fsa.adapter.FSAGraph;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fsa.xml.XMLtoFSA;
import il.ac.tau.cs.smlab.fw.completeness.AveragePropertiesEstimatesAggregator;
import il.ac.tau.cs.smlab.fw.completeness.LogCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.evaluation.FSALogEvaluation;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.StatesCoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import synoptic.invariants.AlwaysFollowedInvariant;
import synoptic.invariants.BinaryInvariant;
import synoptic.model.export.GraphExporter;


public class Main {

	SynopticInputParams synopticInputParams;
	KTailsInputParams ktailsInputParams;
	String[] models = {"Columba", "Heretix", "JArgs", "Jeti", "jfreechart", "lucane",
			"OpenHospital", "RapidMiner", "tagsoup", "Thingamablog"};

	private SynopticInputParams getSynopticInputParams() {
		if (synopticInputParams == null) {
			synopticInputParams =  new SynopticInputParams("shopping-cart-long", null, "(?<ip>) .+ \"GET HTTP/1.1 /(?<TYPE>.+).php\"", "\\k<ip>");
		}
		return synopticInputParams;
	}

	private KTailsInputParams getKTailsInputParams() {
		if (ktailsInputParams == null) {
			ktailsInputParams =  new KTailsInputParams(3);
		}
		return ktailsInputParams;
	}

	private List<TraceProvider> getTraceProviders(String model) throws SpecMiningAlgorithmException, InvalidModelException {
		// visits range
		int min = 50;
		int max = 50;
		List<FSACoverageTraceGenerator> coverages = FSACoverageTraceGeneratorFactory.getAllCoveragesWithRange(model,min,max);

		List<TraceProvider> traceProviders = new ArrayList<TraceProvider>(coverages.size());
		for (FSACoverageTraceGenerator c : coverages) {
			traceProviders.add(new FSARandomWalkTraceProvider(model,c));
		}

		return traceProviders;

	}

	private void initSynoptic() throws SpecMiningAlgorithmException {
		new SynopticInvocation(getSynopticInputParams()).processSynopticArgs();
	}

	@Test
	public void synopticCompletenessSingleModel() throws SpecMiningAlgorithmException, InvalidModelException {
		SpecMiningAlgorithm synopticAlgo = new SynopticAlgorithm();
		List<String> m = new ArrayList<String>(1); m.add("lucane");
		generateCompleteLog(synopticAlgo,m);
	}
	
	
	@Test
	public void synopticCompleteness() throws SpecMiningAlgorithmException, InvalidModelException {
		SpecMiningAlgorithm synopticAlgo = new SynopticAlgorithm();
		generateCompleteLog(synopticAlgo,Arrays.asList(models));
	}

	@Test
	public void ktailsCompleteness() throws SpecMiningAlgorithmException, InvalidModelException {
		SpecMiningAlgorithm ktailsAlgo = new KTailsAlgorithm(getKTailsInputParams());
		generateCompleteLog(ktailsAlgo,Arrays.asList(models));
	}


	@Test
	public void drawModels() throws InvalidModelException, IOException, SpecMiningAlgorithmException {
		initSynoptic();
		for (String model : models) {
			FiniteStateAutomaton automaton = XMLtoFSA.convertXmlToAutomaton(model);
			String dotFileName = SystemConstants.RESOURCES_DIRECTORY + "/models/dot/" + model + ".dot";
			GraphExporter.exportGraph(dotFileName, new FSAGraph(automaton), false);
			GraphExporter.generatePngFileFromDotFile(dotFileName);
		}

	}


	@Test
	public void invariantModelChecker() throws SpecMiningAlgorithmException, InvalidModelException {
	//	initSynoptic();
		String model = "Heretix";
		FiniteStateAutomaton automaton = XMLtoFSA.convertXmlToAutomaton(model);
		BinaryInvariant i = new AlwaysFollowedInvariant("k", "m", "t");
		FSAGraph g = new FSAGraph(automaton);
		System.out.println(FSAInvariantChecker.isSatisfied(i ,g));
	}


	public void generateCompleteLog (SpecMiningAlgorithm algorithm, Collection<String> models) throws SpecMiningAlgorithmException, InvalidModelException {
		for (String model : models) {

			List<LogProperty<?>> properties = algorithm.getAlgorithmLogProperties();


			double minConfidence = 0.2; // should be a parameter
			int maxNumOfTraces = 300; // should be a parameter
			int initialNumOfTraces = 3;
			List<TraceProvider> allTraceProviders = getTraceProviders(model);
			Map<Log,Double> confidences = new LinkedHashMap<Log,Double>();
			Map<Log,Map<String,Double>> trueCompleteness = new LinkedHashMap<Log,Map<String,Double>>();
			for (TraceProvider traceProvider : allTraceProviders) {
				double logConfidence = 0.0D;
				Log log = new Log();
				log.setCoverage(((FSARandomWalkTraceProvider) traceProvider).getCoverage().getCoverageName());
				log.setCoverageParam(((FSARandomWalkTraceProvider) traceProvider).getCoverage().getNumOfVisits());
				LogCompletenessEstimator estimator = new LogCompletenessEstimator(properties, traceProvider.getAlphabetInTrace());
				while (true) {
					if (log.size() >= initialNumOfTraces && logConfidence > minConfidence) {
						System.out.println("reached confidence");
						break;
					}
					if (log.size() > maxNumOfTraces) {
						break;
					}
					if (!traceProvider.hasNext()) {
						System.out.println("NOT ENOUGH TRACES (" + log.size() + ") " +  "CONFIDENCE: " + logConfidence);
						break;
					}
					ExecutionTrace t = traceProvider.next();
					if (t.size() < 8) continue;
					log.addTrace(t);
					algorithm.beforePropertiesResults(t);
					estimator.updatePropertyValues(t);
					algorithm.afterPropertiesResults(t);
					logConfidence = estimator.estimate();

				}
				//	System.out.println("Completeness estimation for " + log.size() + " traces: " + logConfidence);
				List<ModelProperty<FSA>> modelP = new LinkedList<ModelProperty<FSA>>();
				for (LogProperty<?> logP : algorithm.getAlgorithmLogProperties()) {
					if (logP instanceof ModelProperty<?>)
						modelP.add((ModelProperty<FSA>) logP);
				}
				FSALogEvaluation evaluator = new FSALogEvaluation(((FSARandomWalkTraceProvider) traceProvider).getFsa(),modelP,estimator);
				//		System.out.println("true completeness: " + evaluator.completeness());

				confidences.put(log, logConfidence);
				trueCompleteness.put(log, evaluator.completeness());

			}
			AveragePropertiesEstimatesAggregator agg = new AveragePropertiesEstimatesAggregator();
			System.out.println("******** Model: " + model + " *********");

			for (Entry<Log, Double> e: confidences.entrySet()) {
				System.out.print(e.getKey().getCoverage() + "-" + e.getKey().getCoverageParam() + "(" + e.getKey().getTraces().size() + " traces)" + ":");
				System.out.println(" confidence " + e.getValue());
				System.out.println("true completeness " + agg.aggregate(trueCompleteness.get(e.getKey()).values()));
			}
		}
	}

	public static void setNoExitSecurityManager() {
		NoExitSecurityManager secManager = new NoExitSecurityManager();
		System.setSecurityManager(secManager);
	}

}
*/