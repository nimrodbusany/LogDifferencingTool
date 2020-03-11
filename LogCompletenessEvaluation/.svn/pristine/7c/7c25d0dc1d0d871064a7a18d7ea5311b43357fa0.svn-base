package il.ac.tau.cs.smlab.fsa.validation.models;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.validation.FSAValidationModel;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;

public class MultiChainFSAValidationModel extends FSAValidationModel {

	public static final int numberOfChains = 3;

	public MultiChainFSAValidationModel(String name, int alphabet) {
		super(name, alphabet);
	}

	@Override
	protected FiniteStateAutomaton createFSA(int alphabet) {
		FiniteStateAutomaton fsa = new FiniteStateAutomaton();
		int sizeOfChain = alphabet / numberOfChains;
		Point p = new Point(0,0);
		Map<String, State> states = new LinkedHashMap<String,State>(alphabet);

		// initial state
		State initial = fsa.createState(p);
		initial.setLabel("initial");
		initial.setName("initial");
		fsa.setInitialState(initial);
		states.put("initial", initial);

		// terminal state
		State terminal = fsa.createState(p);
		terminal.setLabel("TERMINAL");
		terminal.setName("TERMINAL");
		fsa.addFinalState(terminal);
		states.put("TERMINAL", terminal);

		for (int c=0 ; c<numberOfChains ; c++) {
			// single chain
			State s = null;
			for (int i=c*sizeOfChain ; i<((c+1)*sizeOfChain) ; ++i) {
				s = fsa.createState(p);
				String name = String.valueOf(i);
				s.setName(name);
				s.setLabel(name);
				states.put(name, s);
			}
			fsa.addTransition(new FSATransition(s,terminal,"TERMINAL"));
			fsa.addTransition(new FSATransition(initial,states.get(String.valueOf(c*sizeOfChain)),String.valueOf(c*sizeOfChain)));
			// add transitions
			for (int i = c*sizeOfChain ; i<((c+1)*sizeOfChain)-1 ; ++i) {
				// i -> i+1
				FSATransition t = new FSATransition(states.get(String.valueOf(i)),states.get(String.valueOf(i+1)),String.valueOf(i+1));
				fsa.addTransition(t);
			}
		}
		return fsa;
	}
	
	public static int getNumberofchains() {
		return numberOfChains;
	}
	
	
	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		return FSACoverageTraceGeneratorFactory.getStateCoverage(filenamePrefix, 10);
	}

}
