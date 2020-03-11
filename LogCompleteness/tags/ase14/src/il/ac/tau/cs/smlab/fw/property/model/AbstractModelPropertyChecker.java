package il.ac.tau.cs.smlab.fw.property.model;

import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.utils.TernaryValue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractModelPropertyChecker<M,D> implements ModelPropertyChecker<M,D> {

	protected ModelProperty<M,D> property;
	protected M model;
	private ModelPropertyValues<D> propValues;
	
	
	public ModelPropertyValues<D> check(ModelProperty<M,D> property, M model) {
		if (property.prunable()) {
			return checkWithPruning(property,model);
		}
		return checkWithoutPruning(property,model);
	}
	
	
	
	public ModelPropertyValues<D> checkWithoutPruning(ModelProperty<M,D> property, M model) {
		
		this.model = model;
		this.property = property;
		this.propValues = new ModelPropertyValues<D>(); 
		
		List<EventTypeSeq> seqs = property.generateAllEventSequences(getAlphabet());
		for (EventTypeSeq seq : seqs) {
			propValues.addEventTypeSeq(seq, property.isSatisfied(model, seq));
		}
		return propValues;
		
	}
	
	
	public ModelPropertyValues<D> checkWithPruning(ModelProperty<M,D> property, M model ) {
		
		this.model = model;
		this.property = property;
		this.propValues = new ModelPropertyValues<D>(); 
		
		Map<EventTypeSeq,D> toPrune = new LinkedHashMap<EventTypeSeq,D>();
		
		List<EventTypeSeq> seqs = property.generateAllEventSequences(getAlphabet());
		for (EventTypeSeq seq : seqs) {
			if (toPrune.containsKey(seq)) {
				propValues.addEventTypeSeq(seq, toPrune.get(seq));
				continue;
			}
			D result = property.isSatisfied(model, seq);
			propValues.addEventTypeSeq(seq, result);
			toPrune.putAll(prune(seq, result, seqs));
		}
		return propValues;
		
	}
	
	private Map<? extends EventTypeSeq,D> prune(EventTypeSeq seq,
			D result, List<EventTypeSeq> seqs) {
		Map<EventTypeSeq,D> prune = new LinkedHashMap<EventTypeSeq,D>();
		// this should be more generic than TernaryValue
		if (result.equals(TernaryValue.TRUE)) return prune;
		for (EventTypeSeq e : seqs) {
			if (e.prefix(seq)) {
				prune.put(e, result);
			}
		}
		return prune;
	}
	
	protected abstract Alphabet getAlphabet(); 
	
}
