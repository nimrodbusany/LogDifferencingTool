package GraphUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.jgrapht.ext.ExportException;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Id;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.PortNode;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import prefuse.data.io.DataIOException;

public class AutomatonGraphicVisConverter {

	public static Automaton fromGraphicVis2bricAutomaton(Graph graph, boolean is_determinitic,
			HashMap<String, Character> charsMap, boolean labelsOverNodes)
			throws DataIOException, IOException, ExportException {
		if (labelsOverNodes) {
			return fromGraphicVisWithLabelsOverNodes2bricAutomaton(graph, is_determinitic, charsMap);
		} else {
			return fromGraphicVisWithLabelsOverEdges2bricAutomaton(graph, is_determinitic, charsMap);
		}
	}

	private static Automaton fromGraphicVisWithLabelsOverEdges2bricAutomaton(Graph graph, boolean is_determinitic,
			HashMap<String, Character> charsMap) throws ExportException {

		ArrayList<Node> nodes = graph.getNodes(true);
		ArrayList<Edge> edges = graph.getEdges();

		Automaton fsa = new Automaton();
		fsa.setDeterministic(is_determinitic);
		HashMap<Id, State> fsa_nodes = new HashMap<Id, State>();
		HashMap<Id, Node> Id2orgNodes = new HashMap<Id, Node>();

		for (Node node : nodes) {
			State s = fsa_nodes.get(node.getId());

			if (s == null) {
				s = new State();
				fsa_nodes.put(node.getId(), s);
				Id2orgNodes.put(node.getId(), node);
			} else {
				System.out.println(node.getId());
				System.out.println("ERROR NODE POPPED TWICE");
				// throw new ExportException("unable to convert from GraphicVis
				// With Labels Over Nodes 2 bric Automaton");
			}
			String shape = node.getAttribute("shape");
			if (shape == null) {
				shape = "";
			}
			if (shape.equals("box") || shape.equals("plaintext")) {
				fsa.setInitialState(s);
			}
			if (shape.equals("diamond") || shape.equals("doublecircle")) {
				s.setAccept(true);
			}
		}

		for (Edge edge : edges) {
			Id srcId = edge.getSource().getNode().getId();
			Id tgtId = edge.getTarget().getNode().getId();
			State srcNode = fsa_nodes.get(srcId);
			State targetNode = fsa_nodes.get(tgtId);
			String label = edge.getAttribute("label");
			Transition transition = new Transition(charsMap.get(label), targetNode);
			srcNode.addTransition(transition);
		}

		return fsa;

	}

	private static Automaton fromGraphicVisWithLabelsOverNodes2bricAutomaton(Graph graph, boolean is_determinitic,
			HashMap<String, Character> charsMap) // , String outpath
			throws DataIOException, IOException, ExportException {

		ArrayList<Node> nodes = graph.getNodes(true);
		ArrayList<Edge> edges = graph.getEdges();

		Automaton fsa = new Automaton();
		fsa.setDeterministic(is_determinitic);
		HashMap<Id, State> fsa_nodes = new HashMap<Id, State>();
		HashMap<Id, Node> Id2orgNodes = new HashMap<Id, Node>();

		for (Node node : nodes) {
			State s = fsa_nodes.get(node.getId());

			if (s == null) {
				s = new State();
				fsa_nodes.put(node.getId(), s);
				Id2orgNodes.put(node.getId(), node);
			} else {
				System.out.println("ERROR NODE POPPED TWICE");
				throw new ExportException("unable to convert from GraphicVis With Labels Over Nodes 2 bric Automaton");
			}
			String shape = node.getAttribute("shape");
			if (shape == null) {
				shape = "";
			}
			if (shape.equals("box") || shape.equals("plaintext")) {
				fsa.setInitialState(s);
			}
			if (shape.equals("diamond") || shape.equals("doublecircle")) {
				s.setAccept(true);
			}
		}

		for (Edge edge : edges) {
			Id srcId = edge.getSource().getNode().getId();
			Id tgtId = edge.getTarget().getNode().getId();
			State srcNode = fsa_nodes.get(srcId);
			State targetNode = fsa_nodes.get(tgtId);
			String label = Id2orgNodes.get(srcId).getAttribute("label");
			Transition transition = new Transition(charsMap.get(label), targetNode);
			srcNode.addTransition(transition);
		}

		return fsa;
	}

	public static Graph frombricAutomaton2GraphicVisObject(Automaton automaton, HashMap<Character, String> namingMap) // ,
																														// String
																														// outpath
			throws DataIOException, IOException {

		Graph graph = new Graph();
		State startState = automaton.getInitialState();

		Set<State> states = automaton.getLiveStates();
		HashMap<State, Node> old2newModelStateMapping = new HashMap<State, Node>();
		int i = 0;

		for (State s : states) {
			Node n = new Node();
			Id id = new Id();
			id.setId(Integer.toString(i));
			i += 1;
			n.setId(id);
			graph.addNode(n);
			old2newModelStateMapping.put(s, n);
			if (s == startState) {
				n.setAttribute("shape", "box");
			}
			if (s.isAccept()) {
				n.setAttribute("shape", "diamond");
			}
		}

		for (State source : states) {
			Set<Transition> trans = source.getTransitions();
			for (Transition t : trans) {
				State target = t.getDest();
				Character c = t.getMax();
				Edge e = new Edge();
				e.setSource(new PortNode(old2newModelStateMapping.get(source)));
				e.setTarget(new PortNode(old2newModelStateMapping.get(target)));
				e.setAttribute("label", namingMap.get(c));
				graph.addEdge(e);
			}
		}
//		System.out.println("total nodes:" + graph.getNodes(true).size());
		return graph;
	}

}
