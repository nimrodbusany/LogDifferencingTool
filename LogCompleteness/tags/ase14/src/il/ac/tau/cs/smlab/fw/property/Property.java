package il.ac.tau.cs.smlab.fw.property;

import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.List;

public interface Property {
	
	// (e1,...,ei)
	public List<EventTypeSeq> generateAllEventSequences(Alphabet alphabet);
	
	public String getPropertyName();
	

}
