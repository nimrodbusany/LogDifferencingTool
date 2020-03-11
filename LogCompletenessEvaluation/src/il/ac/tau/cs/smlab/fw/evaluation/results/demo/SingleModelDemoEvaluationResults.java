package il.ac.tau.cs.smlab.fw.evaluation.results.demo;

import il.ac.tau.cs.smlab.fw.evaluation.results.SingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.SingleTrialSingleModelEvaluationResults;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;

import java.util.LinkedList;
import java.util.List;

public class SingleModelDemoEvaluationResults extends SingleModelEvaluationResults {
	
	public SingleModelDemoEvaluationResults(String model) {
		super(model);
	}

	List<SingleTrialSingleModelEvaluationResults> trials = new LinkedList<SingleTrialSingleModelEvaluationResults>();

	
	public SingleTrialSingleModelDemoEvaluationResults newTrial(List<LogProperty<?,?>> properties) {
		SingleTrialSingleModelDemoEvaluationResults t =  new SingleTrialSingleModelDemoEvaluationResults(properties);
		trials.add(t);
		return t;
	}
	
	public List<SingleTrialSingleModelEvaluationResults> getTrials() {
		return trials;
	}

	public void setTrials(List<SingleTrialSingleModelEvaluationResults> trials) {
		this.trials = trials;
	}
	
}
