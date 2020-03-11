package il.ac.tau.cs.smlab.fw.property.log;

import il.ac.tau.cs.smlab.fw.property.Property;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.List;

// T = <T_sigma, T_agg>
public interface LogProperty<T,D> extends Property {

	// T_sigma(t, (e1,..ei))
	public T propertyResult(ExecutionTrace t, EventTypeSeq e);
	
	// T_agg({v1,...,vn})
	public D propertyAggregation(List<T> vals);
	
	// P[Y^n = f(pi)]
	public double propetyCompleteness(LogPropertyValues<T> vals, int n);

	
	public PropertyCompletenessEstimator<T,D> createCompletenessEstimator(Alphabet alphabet);
	
}
