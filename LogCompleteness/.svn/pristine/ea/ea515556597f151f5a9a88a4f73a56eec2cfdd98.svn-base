package il.ac.tau.cs.smlab.fw.property.log;

import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PropertyCompletenessEstimator<T,D> {

	// T
	LogProperty<T,D> property;

	// T values
	LogPropertyValues<T> propValues;
	
	// number of traces
	int n = 0;

	public PropertyCompletenessEstimator(LogProperty<T,D> property, Alphabet alphabet) {
		this.property = property;
		this.propValues = new LogPropertyValues<T>(); 
		List<EventTypeSeq> seqs = property.generateAllEventSequences(alphabet);
		for (EventTypeSeq seq : seqs) {
			propValues.addEventTypeSeq(seq);
		}
	}

	public void updatePropertyValues(ExecutionTrace t) {
		
		for (EventTypeSeq seq:propValues.getPropVals().keySet()) {
			propValues.addValue(seq, property.propertyResult(t, seq));
		}
		++n;
	}
	
	public double estimate() {
		return property.propetyCompleteness(propValues,n);
	}
	
	public LogPropertyValues<T> getPropValues() {
		return propValues;
	}
	
	public LogProperty<T,D> getProperty() {
		return property;
	}
	
	public AggregatedLogPropertyValues<D> getAggregatedPropertyValues() {
		Map<EventTypeSeq, D> agg = new LinkedHashMap<EventTypeSeq,D>(propValues.logPropVals.size());
		
		for (Entry<EventTypeSeq, List<T>> e:propValues.logPropVals.entrySet()) {
			agg.put(e.getKey(), property.propertyAggregation(e.getValue()));
		}
		return new AggregatedLogPropertyValues<D>(agg);
		
	}
	
	
	public AggregatedLogPropertyValues<D> getAggregatedPropertyValues(int prefix) {
		Map<EventTypeSeq, D> agg = new LinkedHashMap<EventTypeSeq,D>(propValues.logPropVals.size());
		
		for (Entry<EventTypeSeq, List<T>> e:propValues.logPropVals.entrySet()) {
			agg.put(e.getKey(), property.propertyAggregation(e.getValue().subList(0, prefix)));
		}
		return new AggregatedLogPropertyValues<D>(agg);
		
	}
	
	
	// finds smallest index p s.t T_agg(v1,...,vp) = T_agg(v1,...,vn)   
	// return p/n
	public double getRedundancy(int initialNumOfTraces) {
		int prefix = -1;
		boolean flag;
		if (initialNumOfTraces == n) {return 0;}
		for (int i = initialNumOfTraces + 1 ; i <= n ; ++i) {
			flag = false;
			for (Entry<EventTypeSeq, List<T>> e:propValues.logPropVals.entrySet()) {
				if (property.propertyAggregation(e.getValue()) != 
						property.propertyAggregation(e.getValue().subList(0, i))) {
					flag = true;
					break; // current i isn't good
				}
			}
			if (!flag) { // inner loop finished going over all sequences without breaking
				prefix = i;
				break;
			}
		}
	//	System.out.println(property.getPropertyName() + " prefix: " + prefix + " n: " + n);
		return 1-(((double)prefix)/n);
	}
	
}
