package il.ac.tau.cs.smlab.fw.property.impl;

import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.ArrayList;
import java.util.List;

public class TerminalEventProperty extends DirectlyFollowsProperty implements ModelProperty<FSA,Boolean> {

	@Override
	public List<EventTypeSeq> generateAllEventSequences(Alphabet alphabet) {
		List<EventTypeSeq> seqs = new ArrayList<EventTypeSeq>(alphabet.size());
		for (EventType e : alphabet.getEventTypes()) {
			seqs.add(new EventTypeSeq(e));
		}
		return seqs;
	}

	@Override
	public String getPropertyName() {
		return "TerminalEvents";
	}

	@Override
	public Boolean propertyResult(ExecutionTrace t, EventTypeSeq e) {
		return t.isTerminal(e.getEvent(0));
	}

	@Override
	public Boolean isSatisfied(FSA model, EventTypeSeq seq) {
		String e = seq.getEvent(0).getEvent();
		for (Transition t1 : model.getAutomaton().getTransitions()) {
			if (t1.getDescription().equals(e)) {
				State s = t1.getToState();
				if (s.getLabel().equals("TERMINAL")) return true;
			}
		}
		return false;
	}

}
