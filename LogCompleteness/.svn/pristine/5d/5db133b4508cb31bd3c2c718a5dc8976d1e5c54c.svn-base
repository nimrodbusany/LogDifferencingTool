package il.ac.tau.cs.smlab.fw.property.impl;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticExecutionTrace;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.FSAInvariantChecker;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.ArrayList;
import java.util.List;

import synoptic.invariants.AlwaysFollowedInvariant;
import synoptic.model.event.StringEventType;

public class EventuallyProperty extends AbstractInvariantProperty {

	public EventuallyProperty(String version) {
		super(version);
	}
	
	public EventuallyProperty() {
		super();
	}
	
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
		return "Eventually";
	}

	@Override
	protected Boolean isInvariantTrue(ExecutionTrace t, EventTypeSeq e) {
		List<EventType> events = e.getEvents();
		SynopticExecutionTrace st = (SynopticExecutionTrace) t;
		return st.getMinedInvariants().getSet().contains(new AlwaysFollowedInvariant(StringEventType
                .newInitialStringEventType(), events.get(0).getEvent(), "t"));
	}
	

	@Override
	public Boolean isSatisfied(FSA fsa, EventTypeSeq e) {
		List<EventType> events = e.getEvents();
		AlwaysFollowedInvariant i = new AlwaysFollowedInvariant(StringEventType
                .newInitialStringEventType(), events.get(0).getEvent(), "t") ;
		return FSAInvariantChecker.isSatisfied(i, fsa.getGraph());
	}

}
