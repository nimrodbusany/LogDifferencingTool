package il.ac.tau.cs.smlab.fw.property.log;

import il.ac.tau.cs.smlab.fw.trace.Alphabet;

public abstract class AbstractLogProperty<T,D> implements LogProperty<T,D> {
	
	@Override
	public PropertyCompletenessEstimator<T,D> createCompletenessEstimator(Alphabet alphabet) {
		return new PropertyCompletenessEstimator<>(this, alphabet);
	}
	
}
