package il.ac.tau.cs.smlab.fsa.validation.models;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.validation.FSAValidationModel;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChainFSAValidationModel extends FSAValidationModel {
	Map<String, State> states;

	public ChainFSAValidationModel(String name, int alphabet) {
		super(name, alphabet);
	}

	@Override
	protected FiniteStateAutomaton createFSA(int alphabet) {
		FiniteStateAutomaton fsa = new FiniteStateAutomaton();
		Point p = new Point(0,0);
		states = new LinkedHashMap<String,State>(alphabet);
		
		// initial state
		State initial = fsa.createState(p);
		initial.setLabel("initial");
		initial.setName("initial");
		fsa.setInitialState(initial);
		states.put("initial", initial);
		// other states
		State s = null;
		for (int i=1 ; i<alphabet ; ++i) {
			s = fsa.createState(p);
			String name = String.valueOf(i);
			s.setName(name);
			s.setLabel(name);
			states.put(name, s);
		}
		// terminal state
		State terminal = fsa.createState(p);
		terminal.setLabel("TERMINAL");
		terminal.setName("TERMINAL");
		fsa.addFinalState(terminal);
		states.put("TERMINAL", terminal);
		fsa.addTransition(new FSATransition(s,terminal,"TERMINAL"));
		fsa.addTransition(new FSATransition(initial,states.get("1"),"1"));
		// add transitions
		for (int i = 1 ; i<alphabet-1 ; ++i) {
			// i -> i+1
			FSATransition t = new FSATransition(states.get(String.valueOf(i)),states.get(String.valueOf(i+1)),String.valueOf(i+1));
			fsa.addTransition(t);
		}
		return fsa;
	}


	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		return FSACoverageTraceGeneratorFactory.getStateCoverage(filenamePrefix, 30);
	}
}