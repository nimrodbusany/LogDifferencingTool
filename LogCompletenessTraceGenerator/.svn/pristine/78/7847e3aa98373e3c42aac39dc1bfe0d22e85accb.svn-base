package il.ac.tau.cs.smlab.fsa.generator.traces.util;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;

public class CalculateCC{
	
	public static final CalculateCC INSTANCE = new CalculateCC();
	
	//numero stati, transizioni e complessitˆ ciclomatica ( v(G) = e - n + 2 ) dell'automa
	public int cyclomaticComplexity(FiniteStateAutomaton efsa){
		
		State states[] = efsa.getStates();
		
		int totStati = states.length;
		int numTransizioni = 0;
		
		for ( State state : states){

			if(efsa.getTransitionsFromState(state) !=null){	
					
				for ( Transition t : efsa.getTransitionsFromState(state) ){
					numTransizioni +=1;
				}
			}
		}
		
		int cc = (int)(numTransizioni - totStati + 2);
		return cc;
	}
}