package prasers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.MinimizationOperations;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import idot.util.DotFileReader;
import prefuse.data.Graph;
import prefuse.data.Tuple;
import prefuse.data.io.DataIOException;
import prefuse.data.tuple.TupleSet;
import synopticdiff.model.export.GraphExporter;

public class DotParse {

	public static Automaton getFsaFromKTailsModelFile(String name) throws FileNotFoundException, DataIOException {
		DotFileReader reader = new DotFileReader();
		FileInputStream is;
		is = new FileInputStream(name); //SystemConstants.RESOURCES_DIRECTORY + "models/stamina/" + name + ".dot"
		Graph g = reader.readGraph(is);
		return convertStateLabelModelToClassicalFSA(g);
	}

	public static Automaton convertStateLabelModelToClassicalFSA(Graph g) {
		
		TupleSet nodes = g.getNodes();
		Automaton fsa = new Automaton();
		Iterator<Tuple> iter = nodes.tuples();
		Map<String,String> statesName2Label = new HashMap<String,String>();
		HashMap<String, State> fsa_nodes = new HashMap<String, State>();
		while (iter.hasNext()) {
			Tuple t = iter.next();
			String name = t.getString("nodename");
			if (name.contains("->")){
				String[] edge = name.split("->");
				System.out.println(edge[0] + ", " + edge[1]);
				String label = statesName2Label.get(edge[0]);
				char c = label.charAt(0);
				System.out.println(label + ", " + c);
				Transition trans = new Transition(c, fsa_nodes.get(edge[1]));
				fsa_nodes.get(edge[0]).addTransition(trans);
			}
			else{
				String label = t.getString("label");
				statesName2Label.put(t.getString("nodename"), label);
				State s = new State();
				fsa_nodes.put(t.getString("nodename"), s);
				String shape = t.getString("shape");
				if (shape.equals("box")){
					fsa.setInitialState(s);
				}
				if (shape.equals("diamond")){
					s.setAccept(true);
				}
			}
		}
		return fsa;
	}
	
	public static Automaton from_dot_file2bricAutomaton(String fsa1, boolean is_determinitic, HashMap<String, Character> charsMap) throws DataIOException, IOException{
		
		System.out.println("Reading models from file: " + fsa1);
		DotFileReader reader = new DotFileReader();
		FileInputStream is;
		is = new FileInputStream(fsa1); //SystemConstants.RESOURCES_DIRECTORY + "models/stamina/" + name + ".dot"
		Graph g = reader.readGraph(is);
		TupleSet nodes = g.getNodes();
		
		Iterator<Tuple> iter = nodes.tuples();
		Automaton fsa = new Automaton();
		fsa.setDeterministic(is_determinitic);
		HashMap<String, State> fsa_nodes = new HashMap<String, State>();
		
		while (iter.hasNext()) {
			Tuple t = iter.next();
			State s = fsa_nodes.get(t.getString("nodename"));
			if (s == null){
				s = new State();
				fsa_nodes.put(t.getString("nodename"), s);
			}
			String shape = t.getString("shape");
			if (shape.equals("box") || shape.equals("plaintext")){
				fsa.setInitialState(s);
			}
			if (shape.equals("diamond") || shape.equals("doublecircle")){
				s.setAccept(true);
			}
		}
		
		iter = g.getEdges().tuples();
		while (iter.hasNext()) {
			Tuple t = iter.next();
			String source = g.getNodeFromKey(t.getLong(0)).getString("nodename");
			String target = g.getNodeFromKey(t.getLong(1)).getString("nodename");
			State srcNode = fsa_nodes.get(source);
			State targetNode = fsa_nodes.get(target);
			String label = t.getString(2);
			if (label == null){
				label = "Z";
			}
			Transition transition = new Transition(charsMap.get(label), targetNode);
			srcNode.addTransition(transition);
		}
		
		return fsa;
	}
	
	private static void substract(Automaton a1, Automaton a2, String out_fsa) throws IOException, DataIOException, ClassCastException, ClassNotFoundException {
		
		HashSet<State> in_state = new HashSet<State>();
		if (!a1.isDeterministic()){
			System.out.println("A1 WAS DETERMIZINED");
			a1.determinize();
		}
		if (!a2.isDeterministic()){
			System.out.println("A2 WAS DETERMIZINED");
			a2.determinize();
		}
		Automaton a3 = BasicOperations.minus(a1, a2);
		MinimizationOperations.minimizeBrzozowski(a3);
		exportAutomaton2Files(out_fsa, a3);
		
	}
	
	public static void outputlabel2chars(String filename, HashMap<String, Character> charsMap) throws IOException {

		HashMap<Character, String> chars2labels = new  HashMap<Character, String>();
		for (String label : charsMap.keySet()){
			chars2labels.put(charsMap.get(label),label);
		}
		
		FileWriter fw = new FileWriter(new File(filename));
		ArrayList<Character> arraylist = new ArrayList<Character>(chars2labels.keySet());
		Collections.sort(arraylist);
		for (Character cc : arraylist){ 
			fw.write("" + cc + ": " + chars2labels.get(cc) + "\n");
		}
		fw.close();

	}

	public static Character labels2chars(String a1_file, HashMap<String, Character> charsMap, Character starting_char)
			throws FileNotFoundException, DataIOException, IOException {
		 
		Character current_char = starting_char;
		DotFileReader reader = new DotFileReader();
		FileInputStream is;
		is = new FileInputStream(a1_file); //SystemConstants.RESOURCES_DIRECTORY + "models/stamina/" + name + ".dot"
		Graph g = reader.readGraph(is);
		Iterator<Tuple> iter = g.getEdges().tuples();
		charsMap.put("Z",'Z');
		while (iter.hasNext()) {
			Tuple t = iter.next();
			String label = t.getString(2);
			if (label != null && charsMap.containsKey(label) == false){
				charsMap.put(label, current_char);
				current_char = String.valueOf( (char) (current_char + 1)).charAt(0);
			}
		}
		return current_char;
	}
	
	public static void main(String[] args) throws DataIOException, IOException, ClassCastException, ClassNotFoundException {
		
		int mode = 1;
		if (mode == 0){
			Automaton fsa = getFsaFromKTailsModelFile("D:/Log diff projects/LogDiffEvaluation/resources/kTails/Columba/output/Columba_generated_trace_1_by_states_1");
			String filename = "C:/Users/user/log_diff_projects/LogDiffAlternativeApp/out/translated_2.dot";
			exportAutomaton2Files(filename, fsa);
		}
		else{
			String a1_file = "C:/Users/user/log_diff_projects/LogDiffAlternativeApp/out/ctas.net.dot";//"D:/Log diff projects/LogDiffEvaluation/resources/kTails/Columba/output/Columba_generated_trace_0_by_states_1";
			String a2_file = "C:/Users/user/log_diff_projects/LogDiffAlternativeApp/out/ctas.net2.dot";
			String out_fsa = "C:/Users/user/log_diff_projects/LogDiffAlternativeApp/out/substraction_left.dot";
			String out_fsa2 = "C:/Users/user/log_diff_projects/LogDiffAlternativeApp/out/substraction_right.dot";
			
			HashMap<String, Character> charsMap = new HashMap<String, Character>();
			Character currentChar = labels2chars(a1_file, charsMap, 'a');
			labels2chars(a2_file, charsMap, currentChar);
			outputlabel2chars(a1_file + "_labels2chars.txt", charsMap);
			
			Automaton  a1 = from_dot_file2bricAutomaton(a1_file, true, charsMap);
			Automaton  a2 = from_dot_file2bricAutomaton(a2_file, true, charsMap);
			substract(a1,a2,out_fsa);
			substract(a2,a1,out_fsa2);
			
		}
	}

	private static void exportAutomaton2Files(String out_fsa, Automaton a1) throws IOException {
		FileWriter fw = new FileWriter(new File(out_fsa));
		fw.write(a1.toDot());
		fw.close();
		GraphExporter.generatePngFileFromDotFile(out_fsa);
	}

	
}
