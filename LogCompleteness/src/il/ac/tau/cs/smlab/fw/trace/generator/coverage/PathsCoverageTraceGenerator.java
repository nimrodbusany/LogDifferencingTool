package il.ac.tau.cs.smlab.fw.trace.generator.coverage;

import il.ac.tau.cs.smlab.fsa.generator.traces.PathCoverage;

public class PathsCoverageTraceGenerator extends FSACoverageTraceGenerator {


	public PathsCoverageTraceGenerator(String outputFile, int numOfVisits) {
		super(outputFile,numOfVisits);
	}
	
	@Override
	public String getCoverageName() {
		return "paths";
	}

	@Override
	protected void generateInner(int numOfVisits) {
		new PathCoverage(numOfVisits,outputFile).generateTraces(automaton);
	}

}
