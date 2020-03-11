package il.ac.tau.cs.smlab.algorithms.synoptic.property;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticExecutionTrace;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.FSAInvariantChecker;
import il.ac.tau.cs.smlab.fw.property.impl.AlwaysPrecedesProperty;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.List;

import synoptic.invariants.AlwaysPrecedesInvariant;

public class SynopticAlwaysPrecedesProperty extends AlwaysPrecedesProperty {

	public SynopticAlwaysPrecedesProperty(String version) {
		super(version);
	}
	
	public SynopticAlwaysPrecedesProperty() {
		super();
	}
	
	
	@Override
	protected Boolean isAlwaysPrecedes(ExecutionTrace t, EventTypeSeq e) {
		List<EventType> events = e.getEvents();
		SynopticExecutionTrace st = (SynopticExecutionTrace) t;
		return st.getMinedInvariants().getSet().contains(new AlwaysPrecedesInvariant(events.get(0).getEvent(), events.get(1).getEvent(), "t"));
	}
	
	@Override
	public Boolean isSatisfied(FSA fsa, EventTypeSeq e) {
		List<EventType> events = e.getEvents();
		AlwaysPrecedesInvariant i = new AlwaysPrecedesInvariant(events.get(0).getEvent(), events.get(1).getEvent(), "t") ;
		return FSAInvariantChecker.isSatisfied(i, fsa.getGraph());
	}

	@Override
	public String getPropertyName() {
		return "AlwaysPrecedes";
	}

}
