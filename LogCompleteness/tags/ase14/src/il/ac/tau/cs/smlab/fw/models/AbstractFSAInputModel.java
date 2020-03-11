package il.ac.tau.cs.smlab.fw.models;

import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;


public abstract class AbstractFSAInputModel implements FSAInputModel {
	
	protected int visits = COVERAGE_VISITS_DEFAULT;
	private static final int COVERAGE_VISITS_DEFAULT = 3;
	
	protected String name;
	protected FiniteStateAutomaton fsa;

	public AbstractFSAInputModel(String name) {
		this.name = name;
	}
	
	
	public AbstractFSAInputModel(String name,int visits) {
		this.name = name;
		this.visits = visits;
	}

	@Override
	public String getModelName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		return FSACoverageTraceGeneratorFactory.getPathCoverage(filenamePrefix, visits);
	}

	public int getVisits() {
		return visits;
	}

	public void setVisits(int visits) {
		this.visits = visits;
	}
}
