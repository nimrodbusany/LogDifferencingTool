package il.ac.tau.cs.smlab.fw.property.impl;

import java.util.List;

import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.PairsEventTypeSeqGenerator;

public abstract class AlwaysPrecedesProperty extends AbstractInvariantProperty {

	@Override
	protected Boolean isInvariantTrue(ExecutionTrace t, EventTypeSeq e) {
		if (!t.contains(e.getEvent(1))) return true;
		return isAlwaysPrecedes(t,e);
	}
	
	protected abstract Boolean isAlwaysPrecedes(ExecutionTrace t, EventTypeSeq e);

	
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
}
