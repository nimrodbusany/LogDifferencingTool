package il.ac.tau.cs.smlab.fw.evaluation.results;

import il.ac.tau.cs.smlab.fw.models.ModelStats;
import il.ac.tau.cs.smlab.fw.property.log.LogProperty;

import java.util.LinkedList;
import java.util.List;

public class SingleModelEvaluationResults {
	
	String model; // model name
	ModelStats modelStats;// model properties
	List<SingleTrialSingleModelEvaluationResults> trials = new LinkedList<SingleTrialSingleModelEvaluationResults>();

	public SingleModelEvaluationResults(String model) {
		this.model = model;
	}
	
	public SingleTrialSingleModelEvaluationResults newTrial(List<LogProperty<?,?>> properties) {
		SingleTrialSingleModelEvaluationResults t =  new SingleTrialSingleModelEvaluationResults(properties);
		trials.add(t);
		return t;
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public List<SingleTrialSingleModelEvaluationResults> getTrials() {
		return trials;
	}

	public void setTrials(List<SingleTrialSingleModelEvaluationResults> trials) {
		this.trials = trials;
	}
	
	public void setModelStats(ModelStats modelStats) {
		this.modelStats = modelStats;
	}
	
	public ModelStats getModelStats() {
		return modelStats;
	}
}
