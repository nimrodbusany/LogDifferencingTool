package il.ac.tau.cs.smlab.fw.trace;

import java.util.Iterator;

// obtain the next execution trace of M
public interface TraceProvider extends Iterator<ExecutionTrace> {

	public Alphabet getAlphabetInTrace();
	
}
