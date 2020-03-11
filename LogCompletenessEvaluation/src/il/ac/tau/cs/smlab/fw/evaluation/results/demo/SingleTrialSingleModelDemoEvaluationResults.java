package il.ac.tau.cs.smlab.fw.evaluation.results.demo;

import il.ac.tau.cs.smlab.fw.evaluation.results.SingleTrialSingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SingleTrialSingleModelDemoEvaluationResults extends SingleTrialSingleModelEvaluationResults {
	
	List<Map<EventTypeSeq, Double>> qValues = new LinkedList<Map<EventTypeSeq, Double>>();
	List<Map<EventTypeSeq, Double>> confContribution = new LinkedList<Map<EventTypeSeq, Double>>();
	List<Map<EventTypeSeq, List<Boolean>>> traceValues = new LinkedList<Map<EventTypeSeq, List<Boolean>>>();

	public SingleTrialSingleModelDemoEvaluationResults(List<LogProperty<?,?>> properties) {
		super(properties);
	}

	public List<Map<EventTypeSeq, Double>> getqValues() {
		return qValues;
	}

	public void addQValues(Map<EventTypeSeq, Double> qValues) {
		this.qValues.add(qValues);
	}

	public List<Map<EventTypeSeq, Double>> getConfContribution() {
		return confContribution;
	}

	public void addConfContribution(Map<EventTypeSeq, Double> confContribution) {
		this.confContribution.add(confContribution);
	}
	
	public List<Map<EventTypeSeq, List<Boolean>>> getTraceValues() {
		return traceValues;
	}

	public void addTraceValues(Map<EventTypeSeq, List<Boolean>> traceValues) {
		this.traceValues.add(traceValues);
	}
	
}
