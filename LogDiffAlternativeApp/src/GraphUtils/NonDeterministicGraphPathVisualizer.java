package GraphUtils;

import java.util.ArrayList;
import java.util.HashSet;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

public class NonDeterministicGraphPathVisualizer {

	// ASSUMPTIONS: Single start node, deterministic automaton
	public static void visualizePathsOnTopOfGraph(Graph graph,
			ArrayList<ArrayList<String>> words, boolean wrapWord) {

		Node startState = findInitialNodes(graph);
		ArrayList<ArrayList<Edge>> paths = new ArrayList<ArrayList<Edge>>();
		for (ArrayList<String> word : words) {
			ArrayList<String> wrappedWord = word;
			if (wrapWord) {
				wrappedWord.add(0, "INITIAL");
				wrappedWord.add(wrappedWord.size(), "TERMINAL");
			}
			HashSet<Node> visitedNodes = new HashSet<Node>();
			visitedNodes.add(startState);
			findPaths(graph, wrappedWord, 0, paths, null, startState,
					visitedNodes);
		}
		System.out.println("total paths found:  " + paths.size());

		for (ArrayList<Edge> transitions : paths) {
			for (Edge transition : transitions) {
				String width = transition.getAttribute("penwidth");
				if (width == null) {
					transition.setAttribute("penwidth", "2");
					transition.setAttribute("color", "red");
				} else {
					int value = Integer.valueOf(width) + 1;
					transition.setAttribute("penwidth", String.valueOf(value));
					transition.setAttribute("color", "red");
				}
			}
		}

	}

	// Node startState = findInitialNodes(graph);
	//
	// for (ArrayList<String> path : words){
	// Node currentState = startState;
	// for (String event : path){
	// Edge transition = findTransition(graph, currentState, event);
	// String width = transition.getAttribute("penwidth");
	// if (width == null){
	// transition.setAttribute("penwidth", "2");
	// transition.setAttribute("color", "red");
	// }
	// else{
	// int value = Integer.valueOf(width) + 1;
	// transition.setAttribute("penwidth", String.valueOf(value));
	// transition.setAttribute("color", "red");
	// }
	// currentState = transition.getTarget().getNode();
	// }
	// if (!currentState.getAttribute("shape").equals("diamond")){
	// throw new IllegalStateException("Violating Key Assumption, all words must
	// be valid!!!");
	// }
	//
	// }

	private static void findPaths(Graph graph, ArrayList<String> word, int i,
			ArrayList<ArrayList<Edge>> paths, ArrayList<Edge> currentPath,
			Node currentState, HashSet<Node> visitedNodes) {

		if (currentPath == null) {
			currentPath = new ArrayList<Edge>();
		}

		String currentEdgeLabel = word.get(i);
		ArrayList<Edge> edges = graph.getEdges();
		for (Edge e : edges) {
			if (e.getSource().getNode() == currentState
					&& e.getAttribute("label").equals(currentEdgeLabel)) {
				ArrayList<Edge> extended_path = new ArrayList<Edge>(currentPath);
				extended_path.add(e);
				if (word.size() == i + 1) {
					if (e.getTarget().getNode().getAttribute("shape") != null
							&& e.getTarget().getNode().getAttribute("shape")
									.equals("diamond")) {
						paths.add(extended_path);
					}
				} else {
					// if (visitedNodes.contains(e.getTarget().getNode())) { //
					// THIS
					// // IS
					// // NOT
					// // A
					// // SIMPLE
					// // PATH
					// // SKIP
					// continue;
					// } else {
					HashSet<Node> cloned_visited_nodes = (HashSet<Node>) visitedNodes
							.clone();
					cloned_visited_nodes.add(e.getTarget().getNode());
					findPaths(graph, word, i + 1, paths, extended_path, e
							.getTarget().getNode(), cloned_visited_nodes);
					// }
				}
			}
		}
	}

	private static Node findInitialNodes(Graph graph) {

		Node startNode = null;
		for (Node n : graph.getNodes(true)) {
			if (n.getAttribute("shape") != null
					&& n.getAttribute("shape").equals("box")) {
				if (startNode != null) {
					throw new IllegalStateException(
							"Violating Key Assumption, single initial node is permitted!!!");
				}
				startNode = n;
			}
		}
		return startNode;
	}

	// private static Edge findTransition(Graph graph, Node currentState, String
	// event) {
	// for (Edge e: graph.getEdges()){
	// String label = e.getAttribute("label");
	// if (e.getSource().getNode().getId() == currentState.getId() &&
	// label.equals(event)){
	// return e;
	// }
	// }
	// return null;
	// }
	//

}
