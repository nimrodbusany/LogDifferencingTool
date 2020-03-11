package GraphUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.jgrapht.ext.ExportException;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Id;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.PortNode;

import prefuse.data.io.DataIOException;

public class GraphConverter {

	public static Character labels2chars(Graph g,
			HashMap<String, Character> charsMap, Character starting_char)
			throws FileNotFoundException, DataIOException, IOException {

		Character current_char = starting_char;
		for (Edge edge : g.getEdges()) {
			String label = edge.getAttribute("label");
			if (label != null && charsMap.containsKey(label) == false) {
				charsMap.put(label, current_char);
				current_char = String.valueOf((char) (current_char + 1))
						.charAt(0);
			}
		}
		return current_char;
	}

	public static Graph convertGraphFromLabelOnNodesToEdges(
			com.alexmerz.graphviz.objects.Graph graph, String output_path,
			boolean useColorLabel, boolean skipTerminals, boolean addEdgeLabels)
			throws IOException, ExportException {

		Graph converted_graph = new Graph();
		HashMap<String, Node> fsa_nodes = new HashMap<String, Node>();
		HashMap<String, Node> Id2orgNodes = new HashMap<String, Node>();
		Node terminalNode = null;
		for (Node node : graph.getNodes(true)) {

			Node n = fsa_nodes.get(node.getId());
			if (n == null) {
				n = new Node();
				n.setId(node.getId());
				converted_graph.addNode(n);
				fsa_nodes.put(node.getId().getId(), n);
				Id2orgNodes.put(node.getId().getId(), node);
			}
			String shape = node.getAttribute("shape");
			if (shape != null) {
				if (shape.equals("diamond")) {
					terminalNode = new Node();
					converted_graph.addNode(terminalNode);
					terminalNode.setAttribute("shape", "diamond");
					Edge e = new Edge();
					e.setSource(new PortNode(n));
					e.setTarget(new PortNode(terminalNode));
					e.setAttribute("label", node.getAttribute("label"));
					converted_graph.addEdge(e);
				} else {
					n.setAttribute("shape", shape);
				}
			}
		}
		
		Id terminalId = new Id();
		terminalId.setId(Integer.toString(converted_graph.getNodes(true).size()+1));
		terminalNode.setId(terminalId);

		for (Edge edge : graph.getEdges()) {
			String srcId = edge.getSource().getNode().getId().getId();
			String trgId = edge.getTarget().getNode().getId().getId();
			Node srcNode = fsa_nodes.get(srcId);
			Node targetNode = fsa_nodes.get(trgId);

			Edge e = new Edge();
			e.setSource(new PortNode(srcNode));
			e.setTarget(new PortNode(targetNode));
			String label = Id2orgNodes.get(srcId).getAttribute("label");
			if (addEdgeLabels) {
				label += " " + edge.getAttribute("label");
			}
			e.setAttribute("label", label);
			if (!useColorLabel || edge.getAttributes().get("color") == null) {
				e.setAttribute("color", "black");
			} else {
				e.setAttribute("color", edge.getAttributes().get("color"));
			}
			converted_graph.addEdge(e);
		}
		if (converted_graph.getEdges().size() != (graph.getEdges().size() + 1)) {
			throw new ExportException(
					"export from graph to dot format filed, incossitent number of eges");
		}
		return converted_graph;
	}

}
