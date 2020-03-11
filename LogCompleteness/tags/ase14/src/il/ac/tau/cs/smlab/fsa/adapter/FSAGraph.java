package il.ac.tau.cs.smlab.fsa.adapter;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import synoptic.model.interfaces.IGraph;
import synoptic.model.interfaces.ITransition;

public class FSAGraph implements IGraph<FSANode> {

	Set<FSANode> nodes; 
	FSANode initial;
	FSANode terminal;
	static final Set<String> relation = new HashSet<String>();

	static {
		relation.add("t");
	}

	public FSAGraph(FiniteStateAutomaton fsa) {
		initial = FSANode.createInitial();
		terminal = FSANode.createTerminal();
		Map<State, FSANode> stateMap = new HashMap<State, FSANode>(fsa.getStates().length);
		nodes = new LinkedHashSet<FSANode>(fsa.getStates().length);
		nodes.add(initial);
		nodes.add(terminal);
		// add nodes
		for (State s: fsa.getStates()) {
			FSANode node = new FSANode(s.getName());
			if (fsa.getInitialState().equals(s)) {
				initial.addTransition(node,"initial");
			}
			if (s.getName().equals("TERMINAL")) {
				// transition from the final node in the model to an additional dummy node
				// so that there will be an action "TERMINAL" in every complete path
				node.addTransition(terminal, "TERMINAL");
			}
/*			else if (Arrays.asList(fsa.getFinalStates()).contains(s)) {
				node.addTransition(terminal, "TERMINAL");
			}*/
			nodes.add(node);
			stateMap.put(s, node);
		}
		// add transitions 
		for (Transition t : fsa.getTransitions()) {
			FSANode from = stateMap.get(t.getFromState());
			FSANode to = stateMap.get(t.getToState());
			assert (to != null && from != null);
			
			from.addTransition(to, t.getDescription());
		}
		
	}

	@Override
	public Set<FSANode> getNodes() {
		return nodes;
	}

	@Override
	public Set<String> getRelations() {
		return relation;
	}

	@Override
	public FSANode getDummyInitialNode() {
		return initial;
	}

	@Override
	public Set<FSANode> getAdjacentNodes(FSANode node) {
		return node.getAllSuccessors();
	}

	@Override
	public void add(FSANode node) {
		nodes.add(node);
	}

	public Set<FSATransition> getTransitionsBetweenNodes(FSANode from, FSANode to) {
		Set<FSATransition> ts = new HashSet<FSATransition>();
		for (FSATransition t: from.transitions) {
			if (t.target.equals(to)) {
				ts.add(t);
			}
		}
		return ts;
	}
}
