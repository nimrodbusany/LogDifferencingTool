package AutomatonSearchAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

public class BricSearchAlgorithms {

	public static String findPrefix(Automaton a) {
		return a.getCommonPrefix();
	}

	
	public static ArrayList<String> findSimplePaths(Automaton diff_a) {
		State s = diff_a.getInitialState();
		ArrayList<String> simplePaths = new ArrayList<String>();
		Set<State> visitedStates = new HashSet<State>();
		visitedStates.add(s);
		findAllPaths(s, visitedStates, simplePaths, "");
		return simplePaths;
	}

	private static void findAllPaths(State s, Set<State> visitedStates, ArrayList<String> simplePaths,
			String currentPath) {
		Set<Transition> transitions = s.getTransitions();
		if (s.isAccept()) {
			simplePaths.add(currentPath);
		}
		if (transitions == null) {
			return;
		}
		for (Transition t : transitions) {
			State nextState = t.getDest();
			if (visitedStates.contains(nextState)) {
				continue;
			}
			visitedStates.add(nextState);
			String newPath = currentPath + t.getMin();
			findAllPaths(nextState, visitedStates, simplePaths, newPath);
		}

	}

	public static ArrayList<String> findCounterExamples(Automaton diff_a, 
			int min_lenght, int max_lenght, int max_examples) {

		ArrayList<String> allStrings = new ArrayList<String>();
		for (int i = min_lenght; i < max_lenght; i++) {
			findCounterExamplesOfLenght(diff_a, i, allStrings);
			if (allStrings.size() > max_examples) {
				break;
			}
		}
		System.out.println("Total String in diff: " + allStrings.size());
		return allStrings;
	}

	private static void findCounterExamplesOfLenght(Automaton a, int lenght, ArrayList<String> allString) {
		Set<String> strings = a.getStrings(lenght);
		if (strings == null) {
			return;
		}
		allString.addAll(strings);
	}

}
