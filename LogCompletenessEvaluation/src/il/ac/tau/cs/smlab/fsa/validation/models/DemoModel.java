package il.ac.tau.cs.smlab.fsa.validation.models;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.validation.FSAValidationModel;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;

public class DemoModel extends FSAValidationModel {

	public DemoModel(String name) {
		super(name, 4);
		 LoopFSAValidationModel f = new LoopFSAValidationModel(name, 4);
		 fsa = f.createFSA(4);
		 State[] states = fsa.getStates();
		 State self = null;
		 for (State s : states) {
			 if (s.getLabel().equals("2")) 
				 self = s;
		 }
		 fsa.addTransition(new FSATransition(self,self,"2"));
	}

	@Override
	protected FiniteStateAutomaton createFSA(int alphabet) {
		
		return fsa;
	}
	
	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		return FSACoverageTraceGeneratorFactory.getStateCoverage(filenamePrefix, 30);
	}

	@Override
	public FiniteStateAutomaton convertToMutatesFsa(int numOfStatesToAdd,
			int numOfModuls) throws InvalidModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FiniteStateAutomaton convertToMutatesFsa(int numOfStatesToAdd,
			int numOfModuls, int numOfRegular) throws InvalidModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FiniteStateAutomaton makeRecurstionMutation(int numOfStatesToAdd,
			int numOfModuls) throws InvalidModelException {
		// TODO Auto-generated method stub
		return null;
	}

}
