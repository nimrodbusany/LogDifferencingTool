package il.ac.tau.cs.smlab.fw.trace.generator.coverage;

import il.ac.tau.cs.smlab.fsa.generator.traces.TransitionCoverage;

public class TransitionsCoverageTraceGenerator extends FSACoverageTraceGenerator {

	public TransitionsCoverageTraceGenerator(String outputFile, int numOfVisits) {
		super(outputFile, numOfVisits);
	}
	
	@Override
	public String getCoverageName() {
		return "transitions";
	}

	@Override
	protected void generateInner(int numOfVisits) {
		new TransitionCoverage(numOfVisits,outputFile).generateTraces(automaton);
	}

}
