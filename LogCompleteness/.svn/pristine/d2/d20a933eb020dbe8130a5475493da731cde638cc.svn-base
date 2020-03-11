package il.ac.tau.cs.smlab.fw.trace.generator.coverage;

import il.ac.tau.cs.smlab.fsa.generator.traces.StateCoverage;

public class StatesCoverageTraceGenerator extends FSACoverageTraceGenerator {


	public StatesCoverageTraceGenerator(String outputFile, int numOfVisits) {
		super(outputFile,numOfVisits);
	}
	
	@Override
	public String getCoverageName() {
		return "states";
	}

	@Override
	protected void generateInner(int numOfVisits) {
		new StateCoverage(numOfVisits,outputFile).genrateTraces(automaton);
	}

}
