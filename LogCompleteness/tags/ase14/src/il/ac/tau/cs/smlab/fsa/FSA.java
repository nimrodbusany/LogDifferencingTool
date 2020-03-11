package il.ac.tau.cs.smlab.fsa;

import il.ac.tau.cs.smlab.fsa.adapter.FSAGraph;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;

public class FSA {

	FiniteStateAutomaton automaton; // for trace generation
	FSAGraph graph; // for model checking
	Alphabet alphabet;
	String name;

	public FSA(String name, FiniteStateAutomaton automaton) {
		this.name = name;
		this.automaton = automaton;
		this.graph = new FSAGraph(automaton);
		this.alphabet = new Alphabet();
		for (Transition s : automaton.getTransitions()) {
			if (!alphabet.contains(s.getDescription()) && !s.getDescription().equals("TERMINAL") && !s.getDescription().equals("initial")) {
		//	if (!alphabet.contains(s.getDescription())) {
				alphabet.addEvent(s.getDescription());}
		}
	}

	public FSAGraph getGraph() {
		return graph;
	}

	public FiniteStateAutomaton getAutomaton() {
		return automaton;
	}

	public Alphabet getAlphabet() {
		return alphabet;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
