package il.ac.tau.cs.smlab.fw.property.impl;

import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fw.property.log.AbstractLogProperty;
import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.log.conf.ConfidenceComputationMethod;
import il.ac.tau.cs.smlab.fw.property.log.conf.ConfidenceComputationMethodFactory;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.property.model.ModelPropertyValues;
import il.ac.tau.cs.smlab.fw.property.model.fsa.FSAModelPropertyChecker;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.PairsEventTypeSeqGenerator;

import java.util.List;

public abstract class AbstractInvariantProperty extends AbstractLogProperty<Boolean,Boolean> implements ModelProperty<FSA,Boolean> {

	List<EventTypeSeq> eventSeqs;
	Alphabet alphabet;
	FSAModelPropertyChecker<Boolean> checker = new FSAModelPropertyChecker<Boolean>();
	ConfidenceComputationMethod<Boolean> confComputation;
	
	public AbstractInvariantProperty(String version) {
		this.confComputation = ConfidenceComputationMethodFactory.getInvariantConfidenceComputation(version);;
	}
	
	public AbstractInvariantProperty() {
		this.confComputation = ConfidenceComputationMethodFactory.getInvariantConfidenceComputation();
	}

	@Override
	public Boolean propertyResult(ExecutionTrace t, EventTypeSeq e) {
		return isInvariantTrue(t,e);
	}

	protected abstract Boolean isInvariantTrue(ExecutionTrace t, EventTypeSeq e);

	@Override
	public Boolean propertyAggregation(List<Boolean> vals) {
		return !vals.contains(false);
	}

	@Override
	public double propetyCompleteness(LogPropertyValues<Boolean> vals, int n) {
		return confComputation.propetyCompleteness(vals, n);
	}

	@Override
	public List<EventTypeSeq> generateAllEventSequences(Alphabet alphabet) {
		if (!alphabet.equals(this.alphabet)) {
			eventSeqs = null;
		}
		if (eventSeqs == null) {
			eventSeqs = new PairsEventTypeSeqGenerator().generate(alphabet);
			this.alphabet = alphabet;
		}
		return eventSeqs;
	}

	@Override
	public boolean prunable() {
		return false;
	}
	
	
	@Override
	public ModelPropertyValues<Boolean> check(
			FSA model) {
		return checker.check(this, model);
	}
	

}
