package il.ac.tau.cs.smlab.fw.property.impl;

import java.util.List;

import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.PairsEventTypeSeqGenerator;

public abstract class AlwaysFollowedByProperty extends AbstractInvariantProperty {

	@Override
	protected Boolean isInvariantTrue(ExecutionTrace t, EventTypeSeq e) {
		if (!t.contains(e.getEvent(0))) return true;
		return isAlwaysFollowedBy(t,e);
	}
	
	protected abstract Boolean isAlwaysFollowedBy(ExecutionTrace t, EventTypeSeq e);
	
	@Override
	public List<EventTypeSeq> generateAllEventSequences(Alphabet alphabet) {
		if (!alphabet.equals(this.alphabet)) {
			eventSeqs = null;
		}
		if (eventSeqs == null) {
			eventSeqs = new PairsEventTypeSeqGenerator().generate(alphabet,false);
			this.alphabet = alphabet;
		}
		return eventSeqs;
	}

	void addInitialSeqs(Alphabet alphabet) {
		EventType initial = new EventType("INITIAL");
		for (EventType e : alphabet.getEventTypes()) {
			eventSeqs.add(new EventTypeSeq(initial, e));
		}
	}
}
