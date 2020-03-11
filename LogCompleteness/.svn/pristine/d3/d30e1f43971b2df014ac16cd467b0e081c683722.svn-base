package il.ac.tau.cs.smlab.fsa;

import il.ac.tau.cs.smlab.fsa.generator.CreateFSA;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventType;

import java.util.ArrayList;
import java.util.List;

public class FSAGenerator {

	public static FiniteStateAutomaton generate(Alphabet alphabet, int numOfStates, int cyclomaticComplexity) {
		
		List<String> labels = new ArrayList<String>(alphabet.size());
		for (EventType e: alphabet.getEventTypes()) {
			labels.add(e.getEvent());
		}
		CreateFSA createFSA = new CreateFSA(labels,numOfStates, cyclomaticComplexity);
		return createFSA.createAutoma();
		
	}
	
	
}
