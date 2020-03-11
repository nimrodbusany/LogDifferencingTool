package il.ac.tau.cs.smlab.fsa.adapter;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import synoptic.model.Partition;
import synoptic.model.event.EventType;
import synoptic.model.event.StringEventType;
import synoptic.model.interfaces.INode;
import synoptic.model.interfaces.ITransition;

public class FSANode implements INode<FSANode> {

	EventType eventType;
	List<FSATransition> transitions;
	boolean initial = false;
	boolean terminal = false;

	private FSANode() {
		transitions = new LinkedList<FSATransition>();
	}

	public FSANode(State s) {
		this(s.getName());
	}

	public FSANode(String s) {
		this.eventType = new StringEventType(s);
		transitions = new LinkedList<FSATransition>();
	}


	public void setInitial(boolean initial) {
		this.initial = initial;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	@Override
	public EventType getEType() {
		return eventType;
	}

	@Override
	public Set<FSANode> getAllSuccessors() {
		Set<FSANode> successors = new LinkedHashSet<FSANode>();
		for (FSATransition e : transitions) {
			successors.add(e.getTarget());
		}
		return successors;
	}

	@Override
	public List<? extends ITransition<FSANode>> getAllTransitions() {
		return transitions;
	}

	@Override
	public List<? extends ITransition<FSANode>> getTransitionsWithExactRelations(
			Set<String> relations) {
		return transitions;
	}

	@Override
	public List<? extends ITransition<FSANode>> getTransitionsWithSubsetRelations(
			Set<String> relations) {
		return transitions;
	}

	@Override
	public List<? extends ITransition<FSANode>> getTransitionsWithIntersectingRelations(
			Set<String> relations) {
		return transitions;
	}

	@Override
	public List<? extends ITransition<FSANode>> getWeightedTransitions() {
		return transitions;
	}

	@Override
	public void setParent(Partition parent) {
	}

	@Override
	public Partition getParent() {
		return null;
	}

	@Override
	public boolean isTerminal() {
		return terminal;
	}

	@Override
	public boolean isInitial() {
		return initial;
	}

	public void addTransition(FSANode node, String label) {
		transitions.add(new FSATransition(this,node, label));
	}


	@Override
	public int compareTo(FSANode other) {
		if (this == other) {
			return 0;
		}

		// Compare labels of the two message events.
		int labelCmp = eventType.compareTo(other.getEType());
		if (labelCmp != 0) {
			return labelCmp;
		}

		// Compare number of children.
		int transitionCntCmp = Integer.valueOf(transitions.size()).compareTo(
				other.transitions.size());
		if (transitionCntCmp != 0) {
			return transitionCntCmp;
		}

		// Compare transitions to children.
		List<? extends ITransition<FSANode>> thisTrans = this
				.getWeightedTransitions();
		List<? extends ITransition<FSANode>> otherTrans = other
				.getWeightedTransitions();

		Collections.sort(thisTrans);
		Collections.sort(otherTrans);
		for (int i = 0; i < thisTrans.size(); i++) {
			int transCmp = thisTrans.get(i).compareTo(otherTrans.get(i));
			if (transCmp != 0) {
				return transCmp;
			}
		}
		return 0;
	}

	public static FSANode createInitial() {
		FSANode initial = new FSANode();
		initial.eventType = StringEventType.newInitialStringEventType();
		initial.initial = true;
		return initial;
	}

	public static FSANode createTerminal() {
		FSANode terminal = new FSANode();
		terminal.eventType = StringEventType.newTerminalStringEventType();
		terminal.terminal = true;
		return terminal;
	}
	public String toString() {
		return eventType.getETypeLabel();
	}

}
