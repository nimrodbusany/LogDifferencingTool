package il.ac.tau.cs.smlab.algorithms.synoptic.property;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticExecutionTrace;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.FSAInvariantChecker;
import il.ac.tau.cs.smlab.fw.property.impl.AlwaysFollowedByProperty;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.List;

import synoptic.invariants.AlwaysFollowedInvariant;
import synoptic.model.event.StringEventType;

public class SynopticAlwaysFollowedByProperty extends AlwaysFollowedByProperty {

	@Override
	protected Boolean isAlwaysFollowedBy(ExecutionTrace t, EventTypeSeq e) {
		
		List<EventType> events = e.getEvents();
		SynopticExecutionTrace st = (SynopticExecutionTrace) t;
		
		StringEventType e1 = convertEType(events.get(0).getEvent());
		StringEventType e2 = convertEType(events.get(1).getEvent());
		
		return st.getMinedInvariants().getSet().contains(new AlwaysFollowedInvariant(e1, e2, "t"));
		
	}

	private StringEventType convertEType(String event) {
	
		if (event.equals("INITIAL")) {
			return StringEventType.newInitialStringEventType();
		}
		return new StringEventType(event);
	}

	@Override
	public Boolean isSatisfied(FSA fsa, EventTypeSeq e) {
		List<EventType> events = e.getEvents();
		AlwaysFollowedInvariant i = new AlwaysFollowedInvariant(events.get(0).getEvent(), events.get(1).getEvent(), "t") ;
		return FSAInvariantChecker.isSatisfied(i, fsa.getGraph());
	}

	@Override
	public String getPropertyName() {
		return "AlwaysFollowedBy";
	}


}
