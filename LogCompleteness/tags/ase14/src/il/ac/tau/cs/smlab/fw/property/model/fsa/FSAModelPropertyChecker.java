package il.ac.tau.cs.smlab.fw.property.model.fsa;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fw.property.model.AbstractModelPropertyChecker;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.property.model.ModelPropertyValues;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;

public class FSAModelPropertyChecker<D> extends AbstractModelPropertyChecker<FSA,D>{
	
	public static Map<Entry<String,ModelProperty<FSA,?>>,ModelPropertyValues<?>> cache = new HashMap<Entry<String,ModelProperty<FSA,?>>,ModelPropertyValues<?>>();
	
	@SuppressWarnings("unchecked")
	@Override
	public ModelPropertyValues<D> check(ModelProperty<FSA,D> property, FSA model) {
		Entry<String, ModelProperty<FSA, ?>> e = new AbstractMap.SimpleEntry<String,ModelProperty<FSA,?>>(model.getName(),property);

		if (cache.containsKey(e)) {
			System.out.println("loading pre-computed model results");
			return (ModelPropertyValues<D>) cache.get(e);
		}
		ModelPropertyValues<D> result;
		if (property.prunable()) {
			result = checkWithPruning(property,model);
		}
		else {
			result = checkWithoutPruning(property,model);
		}
		cache.put(e, result);
		return result;
	}

	@Override
	protected Alphabet getAlphabet() {
		return model.getAlphabet();
	}

}
