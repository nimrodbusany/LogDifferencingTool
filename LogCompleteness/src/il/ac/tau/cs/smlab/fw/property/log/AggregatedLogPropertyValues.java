package il.ac.tau.cs.smlab.fw.property.log;

import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.LinkedHashMap;
import java.util.Map;

public class AggregatedLogPropertyValues<D> {

	// Y^n
	Map<EventTypeSeq, D> aggLogPropVals = new LinkedHashMap<EventTypeSeq,D>();
	

	public Map<EventTypeSeq, D> getAggLogPropVals() {
		return aggLogPropVals;
	}


	public void setAggLogPropVals(Map<EventTypeSeq, D> aggLogPropVals) {
		this.aggLogPropVals = aggLogPropVals;
	}


	public AggregatedLogPropertyValues(Map<EventTypeSeq, D> aggLogPropVals) {
		this.aggLogPropVals = aggLogPropVals;
	}
}
