package il.ac.tau.cs.smlab.fw.models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;


public abstract class AbstractFSAInputModel implements FSAInputModel {
	
	
	protected int visits = COVERAGE_VISITS_DEFAULT;
	private static final int COVERAGE_VISITS_DEFAULT = 3;
	
	protected String name;
	protected FiniteStateAutomaton fsa;
	
	List<FSAInputModel> mutatedModels = new ArrayList<FSAInputModel>();
	
	public AbstractFSAInputModel(String name) {
		this.name = name;
	}
	
	public AbstractFSAInputModel(String name,int visits) {
		this.name = name;
		this.visits = visits;
	}

	@Override
	public String getModelName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		//return FSACoverageTraceGeneratorFactory.getPathCoverage(filenamePrefix, visits);
		return FSACoverageTraceGeneratorFactory.getStateCoverage(filenamePrefix, visits);
	}

	public int getVisits() {
		return visits;
	}

	public void setVisits(int visits) {
		this.visits = visits;
	}
	
	public void setFsa(FiniteStateAutomaton fsa)
	{
		this.fsa = fsa;
	}
	
	public List<FSAInputModel> getMutatedFSA()
	{
		return mutatedModels;
	}
	
	public List<FiniteStateAutomaton> mutateAutomaton(FiniteStateAutomaton fsa, int numOfStates, int numOfModules)
	{
		List<FiniteStateAutomaton>  list = new ArrayList<FiniteStateAutomaton>();
		
//		if (num == 0)
//		{
//			return null;
//		}
//		else if (num == 1)
//		{
//			list.add(addEdge(fsa, 1));
//			
//			return list;
//		}
//		else
//		{
//			list.add(addEdge(fsa, 2));
//			
//			return list;
//		}
		
		for (int i = 0; i < numOfModules; i++)
		{
			list.add(addEdge(fsa, numOfStates));
		}
		
		return list;
	}
	
	public int getNumberOfOutEdges(FiniteStateAutomaton fsa, State s)
	{
		int counter = 0;
		
		for (Transition t : fsa.getTransitions())
		{
			if (t.getFromState() == s)
			{
				counter++;
			}
		}
		
		return counter;
	}
	
	public int getNumberOfInnesrEdges(FiniteStateAutomaton fsa, State s)
	{
		int counter = 0;
		
		for (Transition t : fsa.getTransitions())
		{
			if (t.getToState() == s)
			{
				counter++;
			}
		}
		
		return counter;
	}
	
	public void fixAutomaton(FiniteStateAutomaton fsa, Transition t)
	{
		State sourceState = t.getFromState();
		State destState = t.getToState();
		
		Set<State> sourceStates = new HashSet<State>();
		Set<State> destStates = new HashSet<State>();
		
		boolean sourceHasOut = false;
		boolean destHasIn = false;
		
		for (Transition trans : fsa.getTransitions())
		{
			if (t == trans)
			{
				continue;
			}
			
			if (trans.getToState() == destState)
			{
				destHasIn = true;
			}
			
			if (trans.getToState() == sourceState)
			{
				sourceStates.add(trans.getFromState());
			}
			
			if (trans.getFromState() == sourceState)
			{
				sourceHasOut = true;
			}
			
			if (trans.getFromState() == destState)
			{
				destStates.add(trans.getToState());
			}
			
			if (!destHasIn && !sourceHasOut) // removing the source
			{
				if (sourceStates.size() <  destStates.size())
				{
					for (State s : sourceStates)
					{
						fsa.addTransition(new FSATransition(s, destState, destState.getName()));
					}
					
					fsa.removeState(sourceState);
				}
				else
				{
					for (State s : destStates)
					{
						fsa.addTransition(new FSATransition(sourceState, s, s.getName()));
					}
					
					fsa.removeState(destState);
				}
			}
			else if (!destHasIn)
			{
				for (State s : destStates)
				{
					fsa.addTransition(new FSATransition(sourceState, s, s.getName()));
				}
			}
			else if (!sourceHasOut)
			{
				for (State s : sourceStates)
				{
					fsa.addTransition(new FSATransition(s, destState, destState.getName()));
				}
			}
		}
	}
	
	public FiniteStateAutomaton addEdge(FiniteStateAutomaton fsa, int numOfEdges)
	{
		FiniteStateAutomaton newFsa = (FiniteStateAutomaton) fsa.clone();
		
		Set<Integer> edgesIndex = new HashSet<Integer>();
		
		Random rand = new Random();
		
		Random randSeed = new Random(rand.nextLong());
		
		int transitionSize = newFsa.getTransitions().length;
		
		int stateSize = newFsa.getStates().length;
		
		while (numOfEdges-- > 0)
		{
			int first = rand.nextInt(transitionSize);
			
			while (edgesIndex.contains(first))
			{
				first = rand.nextInt(transitionSize);
			}
			
			edgesIndex.add(first);
		}
		
		int stateIndex = randSeed.nextInt(stateSize);
		
		State s = newFsa.getStates()[stateIndex];
		
		String name = s.getName();
		
		List<State> finalStates = Arrays.asList(newFsa.getFinalStates());
		
		while (s == newFsa.getInitialState() || finalStates.contains(s))
		{
			stateIndex = randSeed.nextInt(stateSize);
			
			s = newFsa.getStates()[stateIndex];
			
			name = s.getName();
		}
		
		Map<State, String> stateToLabel = new HashMap<State, String>();
		
		if (this instanceof PradelFSAInputModel)
		{
			for (Transition trans : newFsa.getTransitions())
			{
				FSATransition fsaTrans = (FSATransition) trans;
				
				//if (trans.getToState() == s)
				{
					stateToLabel.put(fsaTrans.getToState(), fsaTrans.getLabel());
				}
			}
			
			name = stateToLabel.get(s);
		}
		
		
		
		for (Integer counter : edgesIndex)
		{
			Transition t = newFsa.getTransitions()[counter];
			
			State source = t.getFromState();
			State dest = t.getToState();
			
			int i = 0;
			while (newFsa.getStateWithID(i) != null)
				i++;
			
			State temp = newFsa.createState(new Point(0, 0)); //new State(i, new Point(0, 0), newFsa);
			temp.setName(name);
			
			newFsa.removeTransition(t);
			
			//String firstTransName = temp.getName();
			String secondTransName = dest.getName();
			
			if (this instanceof PradelFSAInputModel)
			{
				secondTransName = stateToLabel.get(dest);
			}
			
			newFsa.addTransition(new FSATransition(source, temp, temp.getName()));
			newFsa.addTransition(new FSATransition(temp, dest, secondTransName));
			
		}
		
		return newFsa;
	}	
	
	public FiniteStateAutomaton removeEdge(FiniteStateAutomaton fsa, int numOfEdges)
	{
		FiniteStateAutomaton newFsa = (FiniteStateAutomaton) fsa.clone();
		
		int transitionSize = newFsa.getTransitions().length;
		
		Random rand = new Random();
		
		Set<Integer> edgesIndex = new HashSet<Integer>();
		
		while (numOfEdges-- > 0)
		{
			int first = rand.nextInt(transitionSize);
			
			while (edgesIndex.contains(first))
			{
				first = rand.nextInt(transitionSize);
			}
			
			edgesIndex.add(first);
		}
		
		int counter = 0;
		
		for (Transition t : newFsa.getTransitions())
		{
			List<State> finalStates = Arrays.asList(fsa.getFinalStates());
			
			if (edgesIndex.contains(counter++) && t.getToState() != fsa.getInitialState() && !finalStates.contains(t.getToState()))
			{
				//int countdest = getNumberOfOutEdges(fsa, t.getFromState());
				//int countSrc = getNumberOfOutEdges(fsa, t.getFromState());
				//if (countdest == 1)
				
					fixAutomaton(newFsa, t);
						
				
			}
		}
		
		return newFsa;
	}
}
