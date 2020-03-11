package il.ac.tau.cs.smlab.fw.models;

import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;

import java.util.HashSet;
import java.util.Set;

public class FSAModelStatsExtractor implements ModelStatsExtractor<FiniteStateAutomaton> {
	
	@Override
	public ModelStats getStats(FiniteStateAutomaton fsa) {
		
		int states = fsa.getStates().length; 
		int transitions = fsa.getTransitions().length;
		
		Set<String> l = new HashSet<String>();
		for (Transition s : fsa.getTransitions()) {
			if (!l.contains(s.getDescription()) && !s.getDescription().equals("TERMINAL")) {
				l.add(s.getDescription());}
		}
		int alphabet = l.size();
		
		ModelStats s = new ModelStats();
		s.add("states", String.valueOf(states));
		s.add("transitions", String.valueOf(transitions));
		s.add("alphabet",String.valueOf(alphabet));
		return s;
	}

}
