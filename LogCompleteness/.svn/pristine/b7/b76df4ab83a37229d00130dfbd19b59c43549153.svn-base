package il.ac.tau.cs.smlab.fw.trace;

import il.ac.tau.cs.smlab.fw.utils.CartesianProduct;
import il.ac.tau.cs.smlab.fw.utils.OrderedPair;

import java.util.ArrayList;
import java.util.List;

public class PairsEventTypeSeqGenerator implements EventTypeSeqGenerator {

	@Override
	public List<EventTypeSeq> generate(Alphabet alphabet) {
		return generate(alphabet, true);
	}
	
		
	public List<EventTypeSeq> generate(Alphabet alphabet, boolean reflexivity) {
		List<OrderedPair<EventType>> pairs =  new CartesianProduct<>(alphabet.getEventTypes(), alphabet.getEventTypes()).getOrderedPairs();
		List<EventTypeSeq> seqs = new ArrayList<EventTypeSeq>(pairs.size());
		for (OrderedPair<EventType> pair : pairs) {
			if (pair.getElement1().equals(pair.getElement2()) && !reflexivity) {
				continue;
			}
			seqs.add(new EventTypeSeq(pair.getElement1(), pair.getElement2()));
		}
		return seqs;
	}
}

