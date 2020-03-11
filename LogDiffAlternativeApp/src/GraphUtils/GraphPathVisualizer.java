package GraphUtils;

import java.util.ArrayList;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

public class GraphPathVisualizer {

	// ASSUMPTIONS: Single start node, deterministic automaton
	public static void visualizePathsOnTopOfGraph(Graph graph, ArrayList<ArrayList<String>> words) {

		Node startState = findInitialNodes(graph);
		
		for (ArrayList<String> path : words){
			Node currentState = startState;
			for (String event : path){
				Edge transition = findTransition(graph, currentState, event);
				String width = transition.getAttribute("penwidth");
				if (width == null){
					transition.setAttribute("penwidth", "2");
					transition.setAttribute("color", "red");
				}
				else{
					int value = Integer.valueOf(width) + 1;
					transition.setAttribute("penwidth", String.valueOf(value));
					transition.setAttribute("color", "red");
				}
				currentState = transition.getTarget().getNode();
			}
			if (!currentState.getAttribute("shape").equals("diamond")){
				throw new IllegalStateException("Violating Key Assumption, all words must be valid!!!");
			}
			
		}
		
	}

	private static Edge findTransition(Graph graph, Node currentState, String event) {
		for (Edge e: graph.getEdges()){
			String label = e.getAttribute("label");
			if (e.getSource().getNode().getId() == currentState.getId() && label.equals(event)){
				return e;
			}
		}
		return null;
	}

	private static Node findInitialNodes(Graph graph) {

		Node startNode = null;
		for (Node n : graph.getNodes(true)){
			if (n.getAttribute("shape") != null && n.getAttribute("shape").equals("box")){
				if (startNode != null){
					throw new IllegalStateException("Violating Key Assumption, single initial node is permitted!!!");
				}
				startNode = n;
			}
		}
		return startNode;
	}

	
}
