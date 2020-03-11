package il.ac.tau.cs.smlab.fsa.validation.models;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;

public class ChainWithSelfTransitionsFSAValidationModel extends ChainFSAValidationModel {

	public ChainWithSelfTransitionsFSAValidationModel(String name, int alphabet) {
		super(name, alphabet);
	}

	@Override
	protected FiniteStateAutomaton createFSA(int alphabet) {
		FiniteStateAutomaton fsa = super.createFSA(alphabet);
		for (State s :fsa.getStates()) {
			if (s.equals(fsa.getInitialState()) ||
					s.getLabel().equals("TERMINAL")) continue;
			fsa.addTransition(new FSATransition(s,s,s.getLabel()));
		}
		return fsa;
	}
}
