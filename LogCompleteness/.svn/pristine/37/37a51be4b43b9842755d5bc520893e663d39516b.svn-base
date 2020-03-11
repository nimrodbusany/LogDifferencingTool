package il.ac.tau.cs.smlab.fsa.xml;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.jaxb.FSAEntity;
import il.ac.tau.cs.smlab.fsa.xml.jaxb.FSAEntity.Nodes.Node;
import il.ac.tau.cs.smlab.fsa.xml.jaxb.FSAEntity.Nodes.Node.Gotos;
import il.ac.tau.cs.smlab.fsa.xml.jaxb.FSAEntity.Nodes.Node.Gotos.Goto;
import il.ac.tau.cs.smlab.fsa.xml.jaxb.ObjectFactory;
import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.awt.Point;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XMLtoFSA {

	public static FSAEntity unmarshall(String modelName) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		File xml = new File(SystemConstants.RESOURCES_DIRECTORY + "models/david/"+modelName+".xml");
		FSAEntity unmarshalledObject = 
				(FSAEntity)unmarshaller.unmarshal(xml);

		return unmarshalledObject;

	}   
	
	public static FiniteStateAutomaton convertXmlToAutomaton(String model) throws InvalidModelException {
		try {
			return convertFsaToAutomaton(unmarshall(model));
		} catch (JAXBException e) {
			throw new InvalidModelException(e);
		}
	}
	
	public static FiniteStateAutomaton convertXmlToAutomatonWithMutation(String model, int max) throws InvalidModelException {
		try {
			return convertFsaToAutomatonWithMutation(unmarshall(model), max);
		} catch (JAXBException e) {
			throw new InvalidModelException(e);
		}
	}
	
	public static FiniteStateAutomaton convertFsaToAutomatonWithMutation(FSAEntity fsa, int max) 
	{
		Map<String, State> nameToState = new HashMap<String,State>();
		Point p = new Point(0, 0);
		FiniteStateAutomaton automaton = new FiniteStateAutomaton();
		State initial = automaton.createState(p);
		initial.setName("INITIAL");
		automaton.setInitialState(initial);
		// add all states
		for (Node node : fsa.getNodes().getNode()) {
			State s = automaton.createState(p);
			s.setName(nodeNumToLetter(node.getName()));
			
			// add transition from dummy initial to fsa initial
			if (String.valueOf(fsa.getStartnode()).equals(node.getName())) {
				automaton.addTransition(new FSATransition(initial, s, s.getName()));
			}
			
			// mark terminal
			if (node.getName().equals("TERMINAL")) {
				automaton.addFinalState(s);
			}
			
			nameToState.put(node.getName(), s);
		}
		
		Random rand = new Random();
		int mutated = 0;
		
		// add transitions
		for (Node node : fsa.getNodes().getNode()) {
			
			for (Serializable gt : node.getContent()) {
				if (gt instanceof javax.xml.bind.JAXBElement) {
					@SuppressWarnings("unchecked")
					Gotos gotos = (Gotos) ((JAXBElement<Gotos>) gt).getValue();
					for (Goto trans : gotos.getGoto()) {
						State fromState = nameToState.get(node.getName());
						State toState = nameToState.get(trans.getValue());
						assert(fromState != null && toState != null);
						
						if (rand.nextBoolean() && mutated < max)
						{
							mutated++;
							continue;
						}
						
						FSATransition transition = new FSATransition(fromState, toState, toState.getName());
						automaton.addTransition(transition);	
					}
				}
			}
		}
		
		return automaton;
	}
	
	public static FiniteStateAutomaton convertFsaToAutomaton(FSAEntity fsa) {
		
		Map<String, State> nameToState = new HashMap<String,State>();
		Point p = new Point(0, 0);
		FiniteStateAutomaton automaton = new FiniteStateAutomaton();
		State initial = automaton.createState(p);
		initial.setName("INITIAL");
		automaton.setInitialState(initial);
		// add all states
		for (Node node : fsa.getNodes().getNode()) {
			State s = automaton.createState(p);
			s.setName(nodeNumToLetter(node.getName()));
			
			// add transition from dummy initial to fsa initial
			if (String.valueOf(fsa.getStartnode()).equals(node.getName())) {
				automaton.addTransition(new FSATransition(initial, s, s.getName()));
			}
			
			// mark terminal
			if (node.getName().equals("TERMINAL")) {
				automaton.addFinalState(s);
			}
			
			nameToState.put(node.getName(), s);
		}
		
		// add transitions
		for (Node node : fsa.getNodes().getNode()) {
			
			for (Serializable gt : node.getContent()) {
				if (gt instanceof javax.xml.bind.JAXBElement) {
					@SuppressWarnings("unchecked")
					Gotos gotos = (Gotos) ((JAXBElement<Gotos>) gt).getValue();
					for (Goto trans : gotos.getGoto()) {
						State fromState = nameToState.get(node.getName());
						State toState = nameToState.get(trans.getValue());
						assert(fromState != null && toState != null);
						FSATransition transition = new FSATransition(fromState, toState, toState.getName());
						automaton.addTransition(transition);
					}
				}
			}
		}
		
		return automaton;
	}
	
	
	public static void main (String[] args) throws JAXBException {
		
		String[] models = {"Columba", "Heretix", "JArgs", "Jeti", "jfreechart", "lucane",
				"OpenHospital", "RapidMiner", "tagsoup", "Thingamablog"};
		
		for (String model : models) {
			convertFsaToAutomaton(unmarshall(model));
		}
	}
	
	private static String nodeNumToLetter(String node) {
		if (node.equals("TERMINAL")) return node;
		int nodeId = Integer.valueOf(node);
		assert(nodeId >= 0 && nodeId <= 24);
		return "abcdefghijklmnopqrstuvwxyz".substring(nodeId, nodeId+1);
	}

}