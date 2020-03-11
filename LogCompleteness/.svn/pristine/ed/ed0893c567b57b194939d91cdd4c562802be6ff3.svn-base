package il.ac.tau.cs.smlab.fw.property.model;

import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelPropertyValues<D>  {

	// f_n
	Map<EventTypeSeq,D> modelVals = new LinkedHashMap<EventTypeSeq,D>();
	
	public void addEventTypeSeq(EventTypeSeq seq) {
		if (!modelVals.containsKey(seq)) {
			addEventTypeSeq(seq,null);
		}
	}
	
	public void addEventTypeSeq(EventTypeSeq seq, D value) {
		modelVals.put(seq,value);
	}
	
	public void updateValue(EventTypeSeq seq, D v) {
		assert(modelVals.containsKey(seq));
		modelVals.put(seq, v);
	}
	
	
	public Map<EventTypeSeq,D> getPropVals() {
		return modelVals;
	}
	
	public String toString() {
		return modelVals.toString();
	}
	
}
