package DiffRunners;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import org.jgrapht.ext.ExportException;

import prefuse.data.io.DataIOException;
import AutomatonSearchAlgorithms.BricSearchAlgorithms;
import GraphUtils.AutomatonGraphicVisConverter;
import GraphUtils.GraphConverter;
import GraphUtils.NonDeterministicGraphPathVisualizer;

import com.alexmerz.graphviz.objects.Graph;

import dk.brics.automaton.Automaton;

public class AutomatonSubtractionDiffRunner {
	
	private boolean wrapWords;

	public AutomatonSubtractionDiffRunner(boolean WRAPWORDS){
		this.wrapWords = WRAPWORDS;
	}

	public HashMap<String, Graph> perfomKTailsDiff(String outDirKTails,
			boolean determinize, HashMap<Integer, String> graphid2filename,
			ArrayList<Graph> graphs) throws FileNotFoundException,
			DataIOException, IOException, ExportException {

		if (graphs.size() != 2) {
			throw new IllegalArgumentException(
					"Currently, only supporting two graphs comparison");
		}

		HashMap<String, Graph> diffs = new HashMap<String, Graph>();
		HashMap<String, Character> charsMap = new HashMap<String, Character>();
		ArrayList<Automaton> automata = new ArrayList<Automaton>();
		Character currentChar = 'a';

		System.out.println("Generating diff automaton");

		for (Graph graph : graphs) {
			currentChar = GraphConverter.labels2chars(graph, charsMap,
					currentChar);
			Automaton a = AutomatonGraphicVisConverter
					.fromGraphicVis2bricAutomaton(graph, !determinize,
							charsMap, false);
			if (determinize) {
				a.determinize();
			}
			automata.add(a);
		}

		System.out.println("Searching for counter examples");

		HashMap<Character, String> reverseNamingMap = reverseNamingMap(charsMap);

		for (int i = 0; i < automata.size(); i++) {
			Automaton a1 = automata.get(i);

			for (int j = 0; j < automata.size(); j++) {
				Automaton a2 = automata.get(j);

				if (a2 == a1) {
					continue;
				}

				Automaton diff_a = a1.minus(a2);
				if(diff_a.isEmpty()) { 
					System.out.println("a1, a2 diff is empty automaton!!");
				}
				// String common_prefix =
				// BricSearchAlgorithms.findPrefix(diff_a);
				// System.out.println(translateString(reverseNamingMap,
				// common_prefix));
				ArrayList<String> counterExamples = BricSearchAlgorithms
						.findSimplePaths(diff_a);
				
				if (counterExamples.isEmpty()) {
					//No diffs
					assert diff_a.isEmpty() == true;
					continue;
				}
				
				ArrayList<ArrayList<String>> tranlatedCounterExamples = new ArrayList<ArrayList<String>>();
				// ArrayList<String> counterExamples =
				// BricSearchAlgorithms.findCounterExamples(diff_a, 1, 20, 30);
				for (String example : counterExamples) {
					ArrayList<String> translated = translateString(
							reverseNamingMap, example);
					System.out.println("counter examlpe: " + translated);
					tranlatedCounterExamples.add(translated);
				}

				// Graph diff =
				// AutomatonGraphicVisConverter.frombricAutomaton2GraphicVisObject(diff_a,
				// reverseNamingMap);
				System.out.println("Visualiazing counter example");
				String fname = graphid2filename.get(i) + "_"
						+ graphid2filename.get(j);
				if (tranlatedCounterExamples.size() > 0) {
					NonDeterministicGraphPathVisualizer
							.visualizePathsOnTopOfGraph(graphs.get(i),
									tranlatedCounterExamples, wrapWords);
				}
				diffs.put(fname, graphs.get(i));
			}
		}

		return diffs;
	}
	
	public HashMap<String, Graph> perfomKTailsDiffUpToLen(String outDirKTails,
			boolean determinize, HashMap<Integer, String> graphid2filename,
			ArrayList<Graph> graphs, int len) throws FileNotFoundException,
			DataIOException, IOException, ExportException {

		if (graphs.size() != 2) {
			throw new IllegalArgumentException(
					"Currently, only supporting two graphs comparison");
		}

		HashMap<String, Graph> diffs = new HashMap<String, Graph>();
		HashMap<String, Character> charsMap = new HashMap<String, Character>();
		ArrayList<Automaton> automata = new ArrayList<Automaton>();
		Character currentChar = 'a';

		System.out.println("Generating diff automaton");

		for (Graph graph : graphs) {
			currentChar = GraphConverter.labels2chars(graph, charsMap,
					currentChar);
			Automaton a = AutomatonGraphicVisConverter
					.fromGraphicVis2bricAutomaton(graph, !determinize,
							charsMap, false);
			if (determinize) {
				a.determinize();
			}
			automata.add(a);
		}

		System.out.println("Searching for counter examples");

		HashMap<Character, String> reverseNamingMap = reverseNamingMap(charsMap);

		for (int i = 0; i < automata.size(); i++) {
			Automaton a1 = automata.get(i);

			for (int j = 0; j < automata.size(); j++) {
				Automaton a2 = automata.get(j);

				if (a2 == a1) {
					continue;
				}

				Automaton diff_a = a1.minus(a2);
				if(diff_a.isEmpty()) { 
					System.out.println("a1, a2 diff is empty automaton!!");
				}
				// String common_prefix =
				// BricSearchAlgorithms.findPrefix(diff_a);
				// System.out.println(translateString(reverseNamingMap,
				// common_prefix));
				ArrayList<String> counterExamples = BricSearchAlgorithms
						.findSimplePaths(diff_a);
				
				if (counterExamples.isEmpty()) {
					//No diffs
					assert diff_a.isEmpty() == true;
					continue;
				}
				
				ArrayList<ArrayList<String>> tranlatedCounterExamples = new ArrayList<ArrayList<String>>();
				// ArrayList<String> counterExamples =
				// BricSearchAlgorithms.findCounterExamples(diff_a, 1, 20, 30);
				for (String example : counterExamples) {
					ArrayList<String> translated = translateString(
							reverseNamingMap, example);
					System.out.println("counter examlpe: " + translated);
					tranlatedCounterExamples.add(translated);
				}

				// Graph diff =
				// AutomatonGraphicVisConverter.frombricAutomaton2GraphicVisObject(diff_a,
				// reverseNamingMap);
				System.out.println("Visualiazing counter example");
				String fname = graphid2filename.get(i) + "_"
						+ graphid2filename.get(j);
				if (tranlatedCounterExamples.size() > 0) {
					NonDeterministicGraphPathVisualizer
							.visualizePathsOnTopOfGraph(graphs.get(i),
									tranlatedCounterExamples, wrapWords);
				}
				diffs.put(fname, graphs.get(i));
			}
		}

		return diffs;
	}

	private HashMap<Character, String> reverseNamingMap(
			HashMap<String, Character> charsMap) {
		HashMap<Character, String> reverseNamingMap = new HashMap<Character, String>();
		for (String key : charsMap.keySet()) {
			reverseNamingMap.put(charsMap.get(key), key);
		}
		return reverseNamingMap;
	}

	private ArrayList<String> translateString(
			HashMap<Character, String> reverseNamingMap, String s) {
		ArrayList<String> translatedString = new ArrayList<String>();
		for (int i = 0; i < s.length(); i++) {
			translatedString.add(reverseNamingMap.get(s.charAt(i)));
		}
		return translatedString;
	}

	public long getNumOfSimplePathUpToLen(Graph graph, int maxPathLen) throws 
	FileNotFoundException, DataIOException, IOException, ExportException {
		ArrayList<String> counterExamples = getAllSimplePathUpToLen(graph, maxPathLen, new ArrayList<String>());
		return new HashSet<String>(counterExamples).size();
	}

	public ArrayList<String> getAllSimplePathUpToLen(Graph graph, int maxPathLen, List<String> sensitiveLabels)
			throws FileNotFoundException, DataIOException, IOException, ExportException {
		HashMap<String, Character> charsMap = new HashMap<String, Character>();
		Character currentChar = 'a';

		currentChar = GraphConverter.labels2chars(graph, charsMap,
				currentChar);
		Automaton a = AutomatonGraphicVisConverter
				.fromGraphicVis2bricAutomaton(graph, false,
						charsMap, false);
		a.determinize();
		
		ArrayList<String> counterExamples = BricSearchAlgorithms
				.findCounterExamples(a, 0, maxPathLen, Integer.MAX_VALUE);
		
		if (!sensitiveLabels.isEmpty())
			filterPaths(sensitiveLabels, charsMap, counterExamples);	
		return counterExamples;
	}

	public void filterPaths(List<String> sensitiveLabels, HashMap<String, Character> charsMap,
			ArrayList<String> counterExamples) {
		ListIterator<String> iter = counterExamples.listIterator();
		while(iter.hasNext()){
			if (!isSensitivePath(iter.next(), sensitiveLabels, charsMap))
				iter.remove();
		}
	}
	
	private boolean isSensitivePath(String path, List<String> sensitiveLabels, HashMap<String, Character> charsMap) {
		for (String label : sensitiveLabels) {
			Character c = charsMap.get(label);
			if (path.indexOf(c) >= 0)
				return true;
		}
		return false;
	}

	public long getNumOfSimplePathUpToLenInDiff(ArrayList<Graph> graphs, int maxPathLen) throws 
	FileNotFoundException, DataIOException, IOException, ExportException {
		
		ArrayList<String> counterExamples = getAllSimlePathUpToLenInDiff(graphs, maxPathLen, new ArrayList<String>());
		return new HashSet<String>(counterExamples).size();
	}

	public ArrayList<String> getAllSimlePathUpToLenInDiff(ArrayList<Graph> graphs, int maxPathLen, List<String> sensitiveLabels)
			throws FileNotFoundException, DataIOException, IOException, ExportException {
		if (graphs.size() != 2) {
			throw new IllegalArgumentException(
					"Currently, only supporting two graphs comparison");
		}
		
		HashMap<String, Character> charsMap = new HashMap<String, Character>();
		ArrayList<Automaton> automata = new ArrayList<Automaton>();
		Character currentChar = 'a';

		System.out.println("Generating diff automaton");

		for (Graph graph : graphs) {
			currentChar = GraphConverter.labels2chars(graph, charsMap,
					currentChar);
			Automaton a = AutomatonGraphicVisConverter
					.fromGraphicVis2bricAutomaton(graph, false,
							charsMap, false);
			a.determinize();
			automata.add(a);
		}
		
		ArrayList<String> counterExamples = BricSearchAlgorithms
				.findCounterExamples(automata.get(1).minus(automata.get(0)), 0, maxPathLen, Integer.MAX_VALUE);
		
		if (!sensitiveLabels.isEmpty())
			filterPaths(sensitiveLabels, charsMap, counterExamples);	
		return counterExamples;
	}

	public long getNumOfSimplePathUpToLenWRT(List<String> sensitiveLabels, Graph graph, int maxPathLen) throws
	FileNotFoundException, DataIOException, IOException, ExportException {
		ArrayList<String> counterExamples = getAllSimplePathUpToLen(graph, maxPathLen, sensitiveLabels);
		return new HashSet<String>(counterExamples).size();
	}

	public long getNumOfSimplePathUpToLenInDiffWRT(List<String> sensitiveLabels, ArrayList<Graph> graphs, int maxPathLen) throws
	FileNotFoundException, DataIOException, IOException, ExportException {
		ArrayList<String> counterExamples = getAllSimlePathUpToLenInDiff(graphs, maxPathLen, sensitiveLabels);
		return new HashSet<String>(counterExamples).size();
	}

}
