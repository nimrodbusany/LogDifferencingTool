package il.ac.tau.cs.smlab.algorithms.synoptic.property;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticExecutionTrace;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.FSAInvariantChecker;
import il.ac.tau.cs.smlab.fw.property.impl.NeverFollowedByProperty;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.List;

import synoptic.invariants.NeverFollowedInvariant;

public class SynopticNeverFollowedByProperty extends NeverFollowedByProperty {

	@Override
	protected Boolean isNeverFollowedBy(ExecutionTrace t, EventTypeSeq e) {
		List<EventType> events = e.getEvents();
		SynopticExecutionTrace st = (SynopticExecutionTrace) t;
		return st.getMinedInvariants().getSet().contains(new NeverFollowedInvariant(events.get(0).getEvent(), events.get(1).getEvent(), "t"));
	}

	
	@Override
	public Boolean isSatisfied(FSA fsa, EventTypeSeq e) {
		List<EventType> events = e.getEvents();
		NeverFollowedInvariant i = new NeverFollowedInvariant(events.get(0).getEvent(), events.get(1).getEvent(), "t") ;
		return FSAInvariantChecker.isSatisfied(i, fsa.getGraph());
	}


	@Override
	public String getPropertyName() {
		return "NeverFollowedBy";
	}
}
