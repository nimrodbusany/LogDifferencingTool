package il.ac.tau.cs.smlab.fw.models;

import java.io.File;
import java.io.IOException;
import java.util.List;

import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;

public interface FSAInputModel {
	
	public FiniteStateAutomaton convertToFsa() throws InvalidModelException;
	
	public FiniteStateAutomaton convertToMutatesFsa(int numOfStatesToAdd, int numOfModuls, int numOfRegular) throws InvalidModelException;
	
	public FiniteStateAutomaton convertToMutatesFsa(int numOfStatesToAdd, int numOfModuls) throws InvalidModelException;
	
	public FiniteStateAutomaton makeRecurstionMutation(int numOfStatesToAdd, int numOfModuls) throws InvalidModelException;
	
	public String getModelName();
	
	public void postprocessGeneratedLog(File log, String traceSeparator, String eventSeparator) throws IOException;
	
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix);
	
	public List<FSAInputModel> getMutatedFSA();
}
