package il.ac.tau.cs.smlab.fw.property.model;

import il.ac.tau.cs.smlab.fw.property.Property;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

public interface ModelProperty<M,D> extends Property {
	
	D isSatisfied(M model, EventTypeSeq seq);
	
	boolean prunable();
	
	ModelPropertyValues<D> check(M model);
}
