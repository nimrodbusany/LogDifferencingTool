package il.ac.tau.cs.smlab.fw.evaluation;

import il.ac.tau.cs.smlab.fw.property.log.AggregatedLogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.model.ModelPropertyValues;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.Map.Entry;


public class PropertyEvaluation {

	private String propertyName;

	private ModelPropertyValues<?> modelVals;
	private AggregatedLogPropertyValues<?> logVals;
	private double completeness = -1;
	private int incorrect = -1;

	public PropertyEvaluation(String propertyName, ModelPropertyValues<?> modelVals, AggregatedLogPropertyValues<?> logVals) {
		this.propertyName = propertyName;
		this.modelVals = modelVals;
		this.logVals = logVals;
	}

	public double completeness() {
		if (completeness != -1) return completeness;
		int t = 0;
		incorrect = 0;
		for (Entry<EventTypeSeq, ?> e: modelVals.getPropVals().entrySet()) {
			EventTypeSeq seq = e.getKey();
			Object m = e.getValue();

			Object l = logVals.getAggLogPropVals().get(seq);
			assert(l != null);
			if (m.equals(l)) {
				++t;
			}
			else {
				System.out.println(seq + " is " + m + " in model but " + l + " in log" + propertyName);
				++incorrect;
			}
		}
		completeness = (double)t/modelVals.getPropVals().size();
/*		for (Entry<EventTypeSeq, Boolean> e: modelVals.getPropVals().entrySet()) {
			if (e.getValue().equals(true)) {
				System.out.println(e.getKey());
			}
		}*/
		return completeness;

	}

	public int incorrectSeqs() {
		if (incorrect == -1) {
			completeness();
		}
		return incorrect;
	}


	public String getPropertyName() {
		return propertyName;
	}


}
