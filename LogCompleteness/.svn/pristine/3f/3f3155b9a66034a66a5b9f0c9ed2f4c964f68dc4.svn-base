package il.ac.tau.cs.smlab.fw.property.log;

import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LogPropertyValues<T>  {

	// Y_1, ..., Y_n
	Map<EventTypeSeq,List<T>> logPropVals = new LinkedHashMap<EventTypeSeq, List<T>>();
	
	public void addEventTypeSeq(EventTypeSeq seq) {
		
		if (!logPropVals.containsKey(seq)) {
			logPropVals.put(seq, new LinkedList<T>());
		}
	}
	
	public void addValue(EventTypeSeq seq, T v) {
		assert(logPropVals.containsKey(seq));
		logPropVals.get(seq).add(v);
	}
	
	public Map<EventTypeSeq,List<T>> getPropVals() {
		return logPropVals;
	}
	
	public String toString() {
		return logPropVals.toString();
	}
	
}
