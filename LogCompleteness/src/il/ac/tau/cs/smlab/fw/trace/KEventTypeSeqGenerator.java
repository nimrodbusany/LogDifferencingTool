package il.ac.tau.cs.smlab.fw.trace;

import il.ac.tau.cs.smlab.fw.utils.KPermutations;
import il.ac.tau.cs.smlab.fw.utils.KPermutationsNoElementRepetition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KEventTypeSeqGenerator implements EventTypeSeqGenerator {

	@Override
	public List<EventTypeSeq> generate(Alphabet alphabet) {
		return generate(alphabet, 2);
	}
	
	public List<EventTypeSeq> generate(Alphabet alphabet, int k) {
		 Collection<List<EventType>> kseqs = new KPermutations<EventType>().permutations(alphabet.getEventTypes(), k);
		List<EventTypeSeq> seqs = new ArrayList<EventTypeSeq>(kseqs.size());
		for (List<EventType> seq : kseqs) {
			seqs.add(new EventTypeSeq(seq));
		}
		return seqs;
	}
	
	
	public List<EventTypeSeq> generateWithoutElementRepetition(Alphabet alphabet, int k) {
		 Collection<List<EventType>> kseqs = new KPermutationsNoElementRepetition<EventType>().permutations(alphabet.getEventTypes(), k);
		List<EventTypeSeq> seqs = new ArrayList<EventTypeSeq>(kseqs.size());
		for (List<EventType> seq : kseqs) {
			seqs.add(new EventTypeSeq(seq));
		}
		return seqs;
	}
	
}

