package il.ac.tau.cs.smlab.fw.property.impl;

import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

public abstract class NeverFollowedByProperty extends AbstractInvariantProperty {

	public NeverFollowedByProperty(String version) {
		super(version);
	}
	
	public NeverFollowedByProperty() {
		super();
	}
	
	@Override
	protected Boolean isInvariantTrue(ExecutionTrace t, EventTypeSeq e) {
/*		ITemporalInvariant i = new NeverFollowedInvariant(e.getEvent(0).getEvent(), e.getEvent(1).getEvent(), "t");
		return FSAInvariantChecker.isSatisfied(i,((SynopticExecutionTrace) t).getTraceGraph());*/
		if (!t.contains(e.getEvent(0)) || !t.contains(e.getEvent(1))) return true;
		return isNeverFollowedBy(t,e);

	}
	
	protected abstract Boolean isNeverFollowedBy(ExecutionTrace t, EventTypeSeq e);

}
