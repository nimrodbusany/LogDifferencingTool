package il.ac.tau.cs.smlab.fsa.validation.models;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;

public class ChainWithForwardTransitionsFSAValidationModel extends ChainFSAValidationModel {

	public ChainWithForwardTransitionsFSAValidationModel(String name, int alphabet) {
		super(name, alphabet);
	}

	@Override
	protected FiniteStateAutomaton createFSA(int alphabet) {
		FiniteStateAutomaton fsa = super.createFSA(alphabet);
		for (int i = 1 ;i < states.size()-1 ; ++i) {
			State s = states.get(String.valueOf(i));
			if (s.equals(fsa.getInitialState()) ||
					s.getLabel().equals("TERMINAL")) continue;
			for (int j = i+1 ; j<states.size()-1 ; ++j ) {
				State f = states.get(String.valueOf(j));
				if (f.equals(fsa.getInitialState())) continue;
				fsa.addTransition(new FSATransition(s,f,f.getLabel()));
			}
		}
		return fsa;
	}
	
	
	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		return FSACoverageTraceGeneratorFactory.getStateCoverage(filenamePrefix, 100);
	}
}
