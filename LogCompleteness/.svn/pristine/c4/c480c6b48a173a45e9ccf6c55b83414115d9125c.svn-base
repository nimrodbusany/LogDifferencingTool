package il.ac.tau.cs.smlab.fw.trace.generator.coverage;

import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;

public abstract class FSACoverageTraceGenerator {
	
	
	protected String outputFile;
	protected int numOfVisits;

	protected FiniteStateAutomaton automaton;
	
	public FSACoverageTraceGenerator(String outputFile,int numOfVisits) {
		this.outputFile = outputFile;
		this.numOfVisits = numOfVisits;
	}
	
	public void setAutomaton(FiniteStateAutomaton automaton) {
		this.automaton = automaton;
	}
	
	public String generate() {
		generateInner(numOfVisits);
		assert(automaton != null);
		return outputFile;
		
	}
	
	protected abstract void generateInner(int numOfVisits);
	
	public abstract String getCoverageName();
	
	public int getNumOfVisits() {
		return numOfVisits;
	}
	
	public void setNumOfVisits(int numOfVisits) {
		this.numOfVisits = numOfVisits;
	}
	
	public String getOutputFile() {
		return outputFile;
	}
}
