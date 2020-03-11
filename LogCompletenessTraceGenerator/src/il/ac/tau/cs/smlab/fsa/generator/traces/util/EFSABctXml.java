package il.ac.tau.cs.smlab.fsa.generator.traces.util;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
//import actionsMonitors.ActionData;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
//import xmlConverter.tools.efsa2xml.codec.api.EFSACodec;

public class EFSABctXml {
	//Gli atrtributi rappresentano lo stato di un oggetto.
	//Il codec non ha stato, se è invocato due volte di fila non deve ricordarsi cosa ha fatto la volta prima quindi non ha senso che abbia var di stato.
	

	public final static EFSABctXml INSTANCE = new EFSABctXml();

	public static class AutomataLoader{
		private HashMap<Integer,State> keyStatesMapLoad = new HashMap<Integer,State>();
		private String filename;
		private FiniteStateAutomaton efsa;
		private Integer initialStatePos;
		//This list contains arrays of three elements that represent the transition:
		//1st from, 2nd to, 3 description
		private ArrayList transitionsToSave = new ArrayList();
		
		AutomataLoader( String filename ){
			this.filename = filename;
		}
		
		/**
		 * Load the efsa from the file this loader is associated to
		 * 
		 * @return
		 * @throws IOException 
		 */
		public void loadEFSA() throws IOException{
			BufferedReader r = null;
			try{
			efsa  = new FiniteStateAutomaton();
			r = new BufferedReader( new FileReader(filename) );
			
			
			String line;
			while ( ( line = r.readLine() ) != null ){
				line = line.trim();

				if ( isState( line ) ){
					loadAndSetState( line );
				} else if ( isTransition( line ) ) {
					loadTransition( line );
				} else if ( isEFSA(line) ){
					loadEFSAElement(line);
				}
			}
			
			setTransitions();
			
			setInitialState();
			
			} finally {
				if ( r != null )
					r.close();
			}
		}
		
		/**
		 * Set the initial state in the automaton
		 * 
		 */
		private void setInitialState() {
			efsa.setInitialState(keyStatesMapLoad.get(initialStatePos));
		}

		/**
		 * Add the transitions to the automaton
		 * 
		 */
		private void setTransitions() {
			for ( Object o : transitionsToSave ){
				Object transitionElements[] = (Object[]) o;
				
				Transition t = new FSATransition((State)keyStatesMapLoad.get(transitionElements[0]),(State)keyStatesMapLoad.get(transitionElements[1]),(String)transitionElements[2]);
				efsa.addTransition(t);
			}
		}

		/**
		 * Load an element of type EFSA
		 * 
		 * @param line
		 */
		private void loadEFSAElement(String line) {
			String[] elements = line.split("[\" ]");
			for ( int i = 0; i < elements.length; ++i ){
				if ( elements[i].equals("initialState=") ){
					initialStatePos = Integer.valueOf(elements[i+1].substring(10));
					break;
				}
			}
				
		}

		private boolean isEFSA(String line) {
			return line.startsWith("<fsa:EFSA");
		}

		/**
		 * parse the given line and load the transition it represents
		 * 
		 * @param line
		 */
		private void loadTransition(String line) {
			String[] s2 = line.split("[\" ]");		
			String from = s2[5];
			String to = s2[3];
			String descr = "";
			
			for ( int i = 0; i < s2.length; ++i ){
				
				if ( s2[i].equals("to=") ){
					to = s2[++i];
				} else if ( s2[i].equals("from=") ){
					from = s2[++i];
				} else if ( s2[i].equals("description=") ){
					descr = getUnFormattedDescription(s2[++i]);
				}
			}
			
			int idFrom = Integer.parseInt(from.substring(10, from.length()));	
			int idTo = Integer.parseInt(to.substring(10, to.length()));
			
			transitionsToSave.add(new Object[]{idFrom,idTo,descr} );
			
			
		}

		/**
		 * Read the given line and create in the automaton th estate it represent
		 * 
		 * @param line
		 */
		private void loadAndSetState(String line) {
			String[] s = line.split("\"");
			State state = efsa.createState(new Point(0,0));
			state.setName(s[1]);
			
			if ( s[2].trim().equals("final=") ){
				if ( s[3].equals("true") ){
					efsa.addFinalState(state);
				}
			}
			
			keyStatesMapLoad.put(keyStatesMapLoad.size(),state);
		}
		
		/**
		 * Return the automaton that corresponds to the file
		 * 
		 * @return
		 * @throws IOException
		 */
		public FiniteStateAutomaton getEFSA() throws IOException{
			if ( efsa == null ){
				loadEFSA();
			}
			//System.out.println(fsa.toString());
			//System.out.println("Initial "+fsa.getInitialState());
			return efsa;
		}
	}
	
	/**
	 * Load the automaton given its path
	 * 
	 */
	public FiniteStateAutomaton loadEFSA(String filename) throws IOException,
	ClassNotFoundException {
		AutomataLoader loader = new AutomataLoader(filename);
		return loader.getEFSA();

	}

	

	/**
	 * This method return whether or not a line represent a transition
	 * @param line
	 * @return
	 */
	private static boolean isTransition(String line) {
		return line.startsWith("<transitions ");
	}

	/**
	 * This method returns whether or not a line describe a state
	 * 
	 * @param line
	 * @return
	 */
	private static boolean isState(String line) {
		return line.startsWith("<states ");
	}





	public FiniteStateAutomaton loadEFSA(File file) throws IOException,
	ClassNotFoundException {

		return loadEFSA(file.getAbsolutePath());
	}
	
	
	
	/**
	 * Save the passed EFSA to the given filename
	 * 
	 */
	public void saveEFSA(FiniteStateAutomaton efsa, String filename)
	throws FileNotFoundException, IOException {

		File file = new File(filename);

		//Build  a map in which for every state we associate a position number
		
		HashMap<State, Integer> statesMap = new HashMap<State,Integer>();
		State states[] = efsa.getStates();
		for ( int i = 0; i< states.length; ++i ){
			statesMap.put(states[i],i);
		}

		//Write down states and transitions
		
		BufferedWriter w = null;
		try{
			w = new BufferedWriter( new FileWriter(file) );

			writeHeader(efsa,w,statesMap);

			writeStates(efsa,w,statesMap);

			writeTransition(efsa,w,statesMap);

			writeFooter(efsa,w,statesMap);

			w.close();
		} finally { 
			if ( w != null ){
				w.close();
			}	
		}
	}



	private void writeFooter(FiniteStateAutomaton efsa, BufferedWriter w, HashMap<State, Integer> statesMap) throws IOException {
		w.write("</fsa:EFSA>");
		
	}



	private void writeTransition(FiniteStateAutomaton efsa, BufferedWriter w, HashMap<State, Integer> statesMap) throws IOException {

		if(efsa.getTransitions()!=null){
			for ( Transition t : efsa.getTransitions() ){
				w.write("<transitions ");
				if(t.getDescription()!=null)
					w.write("description=\""+getFormattedDescription(t.getDescription())+"\" ");	
				w.write("to=\"//@states."+statesMap.get(t.getToState())+"\"");
				w.write(" from=\"//@states."+statesMap.get(t.getFromState())+"\"");
				w.write("/>");
				w.write("\n");

			}
		}

	}




	/**
	 * This method replace special characters in description fields in order to be able to save them in xml
	 *
	 *  
	 *  Replaces < with &lt;
	 *  
	 * @param description
	 * @return
	 */
	private String getFormattedDescription(String description) {
		return description.replace("<", "&lt;");
	}


	/**
	 * This method replace get a description formatted for xml saving and return it in original format
	 *  
	 *  Replaces &lt; with <
	 *  
	 *  
	 * @param description
	 * @return
	 */
	private static String getUnFormattedDescription(String description) {
		return description.replace("&lt;","<");
	}

	/**
	 * Write the states in the order defined by the map
	 * 
	 * @param efsa
	 * @param w
	 * @param statesMap
	 * @throws IOException
	 */
	private void writeStates(FiniteStateAutomaton efsa, BufferedWriter w, HashMap<State, Integer> statesMap) throws IOException {
		
		State states[] = new State[statesMap.size()];
		for ( State state : statesMap.keySet() ){
			Integer pos = statesMap.get(state);
			states[pos] = state;
		}

		for ( State state : states ){
			
			w.write("<states name=\""+state.getName()+"\" ");
			if ( efsa.isFinalState(state) ){
				w.write("final=\"true\" "); 
			}
			w.write("fsa=\"/\"");
			w.write("/>");
			w.write("\n");
		}
	}




	/**
	 * Write the header
	 * 
	 * @param efsa
	 * @param w
	 * @param statesMap
	 * @throws IOException
	 */
	private void writeHeader(FiniteStateAutomaton efsa, BufferedWriter w, HashMap<State, Integer> statesMap) throws IOException {

		w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		w.write("\n");
		w.write("<fsa:FSA xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:fsa=\"efsa\""); 
		if ( efsa.getInitialState()!=null ){
			w.write(" initialState=\"//@states."+statesMap.get(efsa.getInitialState())+"\"");
		}
		w.write(">\n");

	}





	public void saveEFSA(FiniteStateAutomaton o, File file)
	throws FileNotFoundException, IOException {

		saveEFSA(o, file.getAbsolutePath());
	}


}
