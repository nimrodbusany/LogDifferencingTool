package il.ac.tau.cs.smlab.fw.trace.generator.coverage;

import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FSACoverageTraceGeneratorFactory {

	private static Map<String,AtomicInteger> prefixToNextIndex = new HashMap<String,AtomicInteger>();

	public static List<FSACoverageTraceGenerator> getStateCoverageWithRange(String filenamePrefix,int min, int max) {
		List<FSACoverageTraceGenerator> coverages = new ArrayList<FSACoverageTraceGenerator>((max - min + 1));
		for (int i = min ; i<= max ; ++i) {
			coverages.add(new StatesCoverageTraceGenerator(generateOutputFileName(filenamePrefix, "states"),i));
		}
		return coverages;
	}


	public static List<FSACoverageTraceGenerator> getPathCoverageWithRange(String filenamePrefix ,int min, int max) {
		List<FSACoverageTraceGenerator> coverages = new ArrayList<FSACoverageTraceGenerator>(4 * (max - min + 1));
		for (int i = min ; i<= max ; ++i) {
			coverages.add(new PathsCoverageTraceGenerator(generateOutputFileName(filenamePrefix,"paths"),i));
		}
		return coverages;
	}

	public static FSACoverageTraceGenerator getPathCoverage(String filenamePrefix ,int visits) {
		return new PathsCoverageTraceGenerator(generateOutputFileName(filenamePrefix,"paths"),visits);
	}

	public static FSACoverageTraceGenerator getStateCoverage(String filenamePrefix,int visits) {
		return new StatesCoverageTraceGenerator(generateOutputFileName(filenamePrefix, "states"),visits);
	}
	
	public static FSACoverageTraceGenerator getTransitionCoverage(String filenamePrefix,int visits) {
		return new TransitionsCoverageTraceGenerator(generateOutputFileName(filenamePrefix, "transitions"),visits);
	}
	
	public static FSACoverageTraceGenerator getIndependentPathsCoverage(String filenamePrefix,int visits) {
		return new IndependentPathsCoverageTraceGenerator(generateOutputFileName(filenamePrefix, "indPaths"),visits);
	}

	public static List<FSACoverageTraceGenerator> getTransitionCoverageWithRange(String filenamePrefix,int min, int max) {
		List<FSACoverageTraceGenerator> coverages = new ArrayList<FSACoverageTraceGenerator>(4 * (max - min + 1));
		for (int i = min ; i<= max ; ++i) {
			coverages.add(new TransitionsCoverageTraceGenerator(generateOutputFileName(filenamePrefix,"transitions"),i));
		}
		return coverages;
	}


	public static List<FSACoverageTraceGenerator> getAllCoveragesWithRange(String filenamePrefix, int min, int max) {
		List<FSACoverageTraceGenerator> coverages = new ArrayList<FSACoverageTraceGenerator>(4 * (max - min + 1));
		for (int i = min ; i <= max ; ++i) {
			coverages.add(new PathsCoverageTraceGenerator(generateOutputFileName(filenamePrefix, "paths"),i));
			coverages.add(new StatesCoverageTraceGenerator(generateOutputFileName(filenamePrefix, "states"),i));
			coverages.add(new TransitionsCoverageTraceGenerator(generateOutputFileName(filenamePrefix, "transition"),i));
		}
		return coverages;
	}

	private static String generateOutputFileName(String filenamePrefix, String coverage) {
		if (!prefixToNextIndex.containsKey(filenamePrefix)) {
			prefixToNextIndex.put(filenamePrefix, new AtomicInteger(0));
		}
		return  SystemConstants.LOG_DIRECTORY + filenamePrefix + "_generated_trace_" + prefixToNextIndex.get(filenamePrefix).getAndIncrement() + "_by_" + coverage + SystemConstants.LOG_FILE_EXTENSION;
	}


}
