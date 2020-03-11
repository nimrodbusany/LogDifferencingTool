package il.ac.tau.cs.smlab.fw.trace.generator.coverage;

import il.ac.tau.cs.smlab.fsa.generator.traces.IndependentPathCoverage;

public class IndependentPathsCoverageTraceGenerator extends FSACoverageTraceGenerator {

	public IndependentPathsCoverageTraceGenerator(String outputFile, int numOfVisits) {
		super(outputFile,numOfVisits);
	}
	
	@Override
	public String getCoverageName() {
		return "indepdendentPaths";
	}

	@Override
	protected void generateInner(int numOfVisits) {
		new IndependentPathCoverage(numOfVisits,outputFile).generateTraces(automaton);
	}

}
