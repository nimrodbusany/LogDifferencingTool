package il.ac.tau.cs.smlab.fw.property.impl;

import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fw.property.log.AbstractLogProperty;
import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.log.conf.ConfidenceComputationMethod;
import il.ac.tau.cs.smlab.fw.property.log.conf.ConfidenceComputationMethodFactory;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.property.model.ModelPropertyValues;
import il.ac.tau.cs.smlab.fw.property.model.fsa.FSAModelPropertyChecker;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.KEventTypeSeqGenerator;

import java.util.Arrays;
import java.util.List;

public class DirectlyFollowsProperty extends AbstractLogProperty<Boolean,Boolean> implements ModelProperty<FSA,Boolean> {

	
	public DirectlyFollowsProperty(int k, String version) {
		this.k = k;
		this.confComputation = ConfidenceComputationMethodFactory.getExistentialConfidenceComputation(version);
	}
	
	public DirectlyFollowsProperty(int k) {
		this.k = k;
		this.confComputation = ConfidenceComputationMethodFactory.getExistentialConfidenceComputation();
	}
	
	public DirectlyFollowsProperty(String version) {
		this.k = 2;
		this.confComputation = ConfidenceComputationMethodFactory.getExistentialConfidenceComputation(version);
	}
	
	// default values
	public DirectlyFollowsProperty() {
		this.k = 2;
		this.confComputation = ConfidenceComputationMethodFactory.getExistentialConfidenceComputation();
	}
	
	int k;
	List<EventTypeSeq> eventSeqs;
	Alphabet alphabet;
	FSAModelPropertyChecker<Boolean> checker = new FSAModelPropertyChecker<Boolean>();
	ConfidenceComputationMethod<Boolean> confComputation;
	
	@Override
	public Boolean propertyResult(ExecutionTrace t, EventTypeSeq e) {
		return t.contains(e);
	}

	@Override
	public Boolean propertyAggregation(List<Boolean> vals) {
		return vals.contains(true);
	}

	@Override
	public List<EventTypeSeq> generateAllEventSequences(Alphabet alphabet) {
		if (!alphabet.equals(this.alphabet)) {
			eventSeqs = null;
		}
		
		if (eventSeqs == null) {
			eventSeqs = new KEventTypeSeqGenerator().generate(alphabet,k);
			addTerminalSeqs(alphabet);
			this.alphabet = alphabet;
		}
		return eventSeqs;
	}
	
	private void addTerminalSeqs(Alphabet alphabet) {
		assert(k==2);
		EventType terminal = new EventType("TERMINAL");
		for (EventType e : alphabet.getEventTypes()) {
			eventSeqs.add(new EventTypeSeq(e,terminal));
		}
	}


	@Override
	public String getPropertyName() {
		return "DirectlyFollows";
	}

	@Override
	public Boolean isSatisfied(FSA model, EventTypeSeq seq) {
		//  k=2,3 for now, impossible to translate to LTL and use model checking
		if (k == 2) {
			return pairDirectlyFollows(model, seq);
		}
		if (k == 3) {
			return tripletDirectlyFollows(model, seq);
		}
		throw new UnsupportedOperationException("Only k={2,3} is supported");
	}

	
	
	
	private boolean tripletDirectlyFollows(FSA model, EventTypeSeq seq) {
		
		String a = seq.getEvent(0).getEvent();
		String b = seq.getEvent(1).getEvent();
		String c = seq.getEvent(2).getEvent();
		for (Transition t1 : model.getAutomaton().getTransitions()) {
			if (t1.getDescription().equals(a)) {
				State s = t1.getToState();
				for (Transition t2: model.getAutomaton().getTransitionsFromState(s)) {
					if (t2.getDescription().equals(b)) {
						State s2 = t2.getToState();
						for (Transition t3: model.getAutomaton().getTransitionsFromState(s2)) {
							if (t3.getDescription().equals(c)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean pairDirectlyFollows(FSA model, EventTypeSeq seq) {
		FiniteStateAutomaton fsa = model.getAutomaton();
		String a = seq.getEvent(0).getEvent();
		String b = seq.getEvent(1).getEvent();
		for (Transition t1 : fsa.getTransitions()) {
			if (t1.getDescription().equals(a)) {
				State s = t1.getToState();
				if (b.equals("TERMINAL") && (fsa.getTransitionsFromState(s).length == 0 || Arrays.asList(fsa.getFinalStates()).contains(s))) return true;
				for (Transition t2: fsa.getTransitionsFromState(s)) {
					if (t2.getDescription().equals(b)) {
						return true;
					}
				}
			}
		}
		return false;
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

	@Override
	public double propetyCompleteness(LogPropertyValues<Boolean> vals, int n) {
		return confComputation.propetyCompleteness(vals, n);
	}

}
