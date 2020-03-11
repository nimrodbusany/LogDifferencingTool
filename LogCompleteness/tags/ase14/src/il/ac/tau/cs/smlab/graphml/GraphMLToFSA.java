package il.ac.tau.cs.smlab.graphml;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;

public class GraphMLToFSA {

	FiniteStateAutomaton fsa = new FiniteStateAutomaton();

	public static FiniteStateAutomaton graphMLtoFSA(String model) throws InvalidModelException {
		GraphMLToFSA m = new GraphMLToFSA();
		try {
			m.loadMLGraphXML(model);
		} catch (FileNotFoundException | GraphIOException e) {
			throw new InvalidModelException(e);
		}
		return m.getFsa();
	}

	public Graph<State,FSATransition> loadMLGraphXML(String model) throws FileNotFoundException, GraphIOException {

		Reader fileReader = new FileReader(SystemConstants.RESOURCES_DIRECTORY + "models/zeller/" + model + ".xml");
		/* Create the graphMLReader2 */
		GraphMLReader2<Graph<State, FSATransition>, State, FSATransition>
		graphReader = new
		GraphMLReader2<Graph<State, FSATransition>, State, FSATransition>
		(fileReader, graphTransformer, vertexTransformer,
				edgeTransformer, hyperEdgeTransformer);
		return graphReader.readGraph();

	}

	/* Create the Graph Transformer */
	Transformer<GraphMetadata, Graph<State, FSATransition>>
	graphTransformer = new Transformer<GraphMetadata,
	Graph<State, FSATransition>>() {

		public Graph<State, FSATransition>
		transform(GraphMetadata metadata) {
			if (metadata.getEdgeDefault().equals(
					metadata.getEdgeDefault().DIRECTED)) {
				return new
						DirectedSparseGraph<State, FSATransition>();
			} else {
				return new
						UndirectedSparseGraph<State, FSATransition>();
			}
		}
	};

	/* Create the Vertex Transformer */
	Transformer<NodeMetadata, State> vertexTransformer
	= new Transformer<NodeMetadata, State>() {
		public State transform(NodeMetadata metadata) {
			Point p = new Point(0,0);
			State v = fsa.createState(p);
			v.setName(metadata.getId());
			if (v.getName().equals("start")) {
				fsa.setInitialState(v);
			}
			if (v.getName().equals("TERMINAL")) {
				fsa.addFinalState(v);
			}
			return v;
		}
	};


	/* Create the Edge Transformer */
	Transformer<EdgeMetadata, FSATransition> edgeTransformer =
			new Transformer<EdgeMetadata, FSATransition>() {
		public FSATransition transform(EdgeMetadata metadata) {
			FSATransition e = new FSATransition(getStateByName(metadata.getSource()),
					getStateByName(metadata.getTarget()),metadata.getProperty("key1"));
			fsa.addTransition(e);
			return e;
		}
	};


	/* Create the Hyperedge Transformer */
	Transformer<HyperEdgeMetadata, FSATransition> hyperEdgeTransformer
	= new Transformer<HyperEdgeMetadata, FSATransition>() {
		public FSATransition transform(HyperEdgeMetadata metadata) {
			FSATransition e = new FSATransition(null,null,null);
			return e;
		}
	};

	private State getStateByName(String name) {
		for (State s : fsa.getStates()) {
			if (s.getName().equals(name)) return s;
		}
		return null;
	}

	public FiniteStateAutomaton getFsa() {
		return fsa;
	}


}