package exporters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Node;

import dk.brics.automaton.Automaton;

public class GraphExporter {
	
	
	public static String exportGraph(String output_path, 
			com.alexmerz.graphviz.objects.Graph graph) throws IOException {

		FileWriter fw = new FileWriter(new File(output_path));
		ArrayList<Node> nodes = graph.getNodes(true);
		ArrayList<Edge> edges = graph.getEdges();
		fw.write("digraph G {\n");
		fw.write("rankdir=\"TB\"\n");
		for (Node n : nodes) {
			
			if (n.getAttribute("shape") == null) {
				fw.write(n.getId().getId() + " [label=\"" + n.getId().getId() + "\"];\n");
			} else {
				fw.write(n.getId().getId() + " [label=\"" + n.getId().getId() + "\", shape= "
						+ n.getAttribute("shape") + "];\n");
			}
		}

		for (Edge e : edges) {

			String attributes = " [label= \""+ e.getAttribute("label") + "\"";
			if (e.getAttribute("color") != null){
				attributes += ", color = " + e.getAttribute("color"); 
			}
			if (e.getAttribute("penwidth") != null){
				attributes += ", penwidth = " + e.getAttribute("penwidth"); 
			}
			attributes += "]";
			fw.write(e.getSource().getNode().getId().getId() + "->" + e.getTarget().getNode().getId().getId() + attributes);
//					+ e.getAttribute("label") + "\", color = " + e.getAttribute("color") + "];\n");
//			if (e.getAttribute("color") != null){
//				fw.write(e.getSource().getNode().getId().getId() + "->" + e.getTarget().getNode().getId().getId() + " [label= \""
//						+ e.getAttribute("label") + "\", color = " + e.getAttribute("color") + "];\n");
//			}
//			else{
//				fw.write(e.getSource().getNode().getId().getId() + "->" + e.getTarget().getNode().getId().getId() + " [label= \""
//						+ e.getAttribute("label") + "\"];\n");
//			}
		}
		fw.write("} \n");
		fw.close();
		
		return synopticdiff.model.export.GraphExporter.generatePngFileFromDotFile(output_path);
	}

	
	public static void outputBricAutomaton(Automaton a, String out_fsa) throws IOException {

		FileWriter fw = new FileWriter(new File(out_fsa));
		fw.write(a.toDot());
		fw.close();
		synopticdiff.model.export.GraphExporter.generatePngFileFromDotFile(out_fsa);
	}

	
}
