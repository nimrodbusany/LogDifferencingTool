package il.ac.tau.cs.smlab.fw.evaluation;

import il.ac.tau.cs.smlab.fw.completeness.LogCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResults;
import il.ac.tau.cs.smlab.fw.property.log.PropertyCompletenessEstimator;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class LogEvaluation<M> {

	M model;
	List<ModelProperty<M,?>> props;
	Map<String, PropertyEvaluation> evaluators;
	LogCompletenessEstimator estimator;

	LogEvaluation (M model, List<ModelProperty<M,?>> props ,LogCompletenessEstimator estimator) {
		this.model = model;
		this.props = props;
		this.estimator = estimator;
		this.evaluators = new LinkedHashMap<String,PropertyEvaluation>(props.size());
		
		for (PropertyCompletenessEstimator<?,?> e:estimator.getPropEstimators()) {
			String propName = e.getProperty().getPropertyName();
			ModelProperty<M,?> modelProp = getModelProperty(propName);
			if (modelProp != null) {
				evaluators.put(propName,new PropertyEvaluation(propName, modelProp.check(model), e.getAggregatedPropertyValues()));
			}
		}
	}
	
	public Map<String,Integer> incorrectSeqs() {
		Map<String,Integer> incorrect = new LinkedHashMap<String,Integer>(evaluators.size());
		for (Entry<String,PropertyEvaluation> e : evaluators.entrySet()) {
			incorrect.put(e.getKey(), e.getValue().incorrectSeqs());
		}
		return incorrect;
	}

	public Map<String,Double> completeness() {
		Map<String,Double> completeness = new LinkedHashMap<String,Double>(evaluators.size());
		for (Entry<String,PropertyEvaluation> e : evaluators.entrySet()) {
			completeness.put(e.getKey(), e.getValue().completeness());
		}
		return completeness;
	}
	
	public Map<String,Double> redundancy(int initialNumOfTraces) {
		Map<String,Double> redundancy = new LinkedHashMap<String,Double>(evaluators.size());
		for (Entry<String,PropertyEvaluation> e : evaluators.entrySet()) {
			double r = 0.0;
			if (e.getValue().completeness() >= EvaluationResults.COMPLETENESS_THRESHOLD) {
				r = estimator.getPropertyEstimatorByName(e.getKey()).getRedundancy(initialNumOfTraces); 
			}
			redundancy.put(e.getKey(), r);
		}
		return redundancy;
	}

	private ModelProperty<M,?> getModelProperty(String propName) {
		for(ModelProperty<M,?> prop:props) {
			if (prop.getPropertyName().equals(propName)) {
				return prop;
			}
		}
		return null;
	}

}
