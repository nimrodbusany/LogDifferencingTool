package il.ac.tau.cs.smlab.fw.trace;

import il.ac.tau.cs.smlab.fw.utils.TernaryValue;

import java.util.List;

public abstract class ExecutionTrace {

	List<EventType> events;
	int traceId;

	public ExecutionTrace(int traceId) {
		this.traceId = traceId;
	}

	// true iff trace contains the event e
	public abstract boolean contains(EventType event);

	// true iff trace contains the sequence <e1,e2,e3,...> 
	public abstract boolean contains(EventTypeSeq events);

	public int getTraceId() {
		return traceId;
	}

	// true iff trace contains all the events (not necessarily in a particular order)
	public boolean containsAll(EventTypeSeq events) {
		for (EventType e: events.getEvents()) {
			if (!contains(e))
				return false;
		}
		return true;
	}
	
	public int size() {
		return events.size();
	}
	
	// TRUE iff p++m holds under the projection of ab
	// FALSE iff p++m doesn't hold under the project of ab
	// UNKNOWN iff p doesn't appear at all
	public abstract TernaryValue adjacentUnderProjection(Alphabet ab, EventTypeSeq p, EventTypeSeq m, boolean mineTriggers);
	
	public abstract boolean isTerminal(EventType e);
	
}
