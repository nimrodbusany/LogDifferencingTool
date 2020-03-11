package il.ac.tau.cs.smlab.fsa.validation.models;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.UUID;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.validation.FSAValidationModel;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;

public class ChainWithRepetitionsFSAValidationModel extends FSAValidationModel {
	static final int chainToAlphabetRatio = 3;
	
	
	public ChainWithRepetitionsFSAValidationModel(String name, int alphabet) {
		super(name, alphabet);
	}

	@Override
	protected FiniteStateAutomaton createFSA(int alphabet) {
		FiniteStateAutomaton fsa = new FiniteStateAutomaton();
		Point p = new Point(0,0);
		LinkedHashMap<String, State> states = new LinkedHashMap<String,State>(alphabet * chainToAlphabetRatio);
		
		// initial state
		State initial = fsa.createState(p);
		initial.setLabel("initial");
		initial.setName("initial");
		fsa.setInitialState(initial);
		states.put("initial", initial);
		// other states
		State s = null;
		for (int i=1 ; i<(alphabet*chainToAlphabetRatio) ; ++i) {
			s = fsa.createState(p);
			String name = UUID.randomUUID().toString();
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
		Random r = new Random();
		Object[] statesArr = states.values().toArray();
		State afterInitial = (State)statesArr[1];
		fsa.addTransition(new FSATransition(initial,afterInitial,String.valueOf(r.nextInt(alphabet-1)+1)));
		// add transitions
		for (int i = 1 ; i<(alphabet*chainToAlphabetRatio)-1 ; ++i) {
			// i -> i+1
			State source = (State)statesArr[i];
			State target = (State)statesArr[i+1];
			FSATransition t = new FSATransition(source,target,String.valueOf(r.nextInt(alphabet-1)+1));
			fsa.addTransition(t);
		}
		return fsa;
	}


	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		return FSACoverageTraceGeneratorFactory.getTransitionCoverage(filenamePrefix, 20);
	}
}
