package il.ac.tau.cs.smlab.fw.property.model;

import gov.nasa.ltl.graph.Edge;
import gov.nasa.ltl.graph.Graph;
import gov.nasa.ltl.graph.Node;
import gov.nasa.ltl.trans.ParseErrorException;
import il.ac.tau.cs.smlab.fsa.adapter.FSAGraph;
import il.ac.tau.cs.smlab.fsa.adapter.FSANode;
import il.ac.tau.cs.smlab.fsa.adapter.FSATransition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import synoptic.benchmarks.PerformanceMetrics;
import synoptic.benchmarks.TimedTask;
import synoptic.invariants.CExamplePath;
import synoptic.invariants.ITemporalInvariant;
import synoptic.invariants.ltlcheck.Counterexample;
import synoptic.invariants.ltlcheck.LtlModelChecker;
import synoptic.invariants.ltlchecker.GraphLTLChecker;
import synoptic.main.SynopticMain;
import synoptic.model.export.GraphExporter;
import synoptic.model.interfaces.IGraph;
import synoptic.model.interfaces.ITransition;
import synoptic.util.InternalSynopticException;

public class CompletenessLTLChecker extends GraphLTLChecker<FSANode> {

	private static Logger logger = Logger.getLogger("CompletenessLTLChecker Logger");
	private static final boolean DEBUG = false;

	/**
	 * Checks the formula after pre-processing it. So it's allowed to have
	 * things in it like WFAIR(a).
	 * 
	 * @param sourceGraph
	 *            The expression to check (it has to be evaluated before!)
	 * @param invariant
	 *            the formula to check
	 * @return a counter example, or {@code null} if the formula is satisfied
	 * @throws ParseErrorException
	 */
	public Counterexample check(IGraph<FSANode> sourceGraph,
			ITemporalInvariant invariant) throws ParseErrorException {

		// formula = LTLFormulaPreprocessor.preprocessFormula(formula);
		// monitor.subTask("Preprocessed LTL formula: " + formula);
		TimedTask transToMC = PerformanceMetrics.createTask("transToMC");

		Graph targetGraph = null;
		String relation = invariant.getRelation();
		// If we've already converted this source graph before then just look up
		// the target in the cache.
		if (lastSourceGraph.containsKey(relation)
				&& lastSourceGraph.get(relation).equals(sourceGraph)) {
			targetGraph = lastTargetGraph.get(relation);
		}

		if (lastSourceGraph.size() > 5) {
			lastSourceGraph.clear();
			lastTargetGraph.clear();
		}

		// Target is not cached, have to convert.
		if (targetGraph == null) {
			logger.finest("Building CCS Graph...");
			targetGraph = convertGraph(sourceGraph, relation);

			lastSourceGraph.put(relation, sourceGraph);
			lastTargetGraph.put(relation, targetGraph);
		}
		transToMC.stop();
		if (DEBUG) {
			SynopticMain.getInstanceWithExistenceCheck().exportNonInitialGraph(
					"c:\\sourceGraph-"+ relation, sourceGraph);
			writeDot(targetGraph, "c:\\targetGraph-" + relation + ".dot");
		}
		// Run the LTL model-checker on this graph structure.
		Counterexample c = LtlModelChecker.check(targetGraph, invariant);

		return c;
	}

	// TODO: refactor this code to instead use the GraphVizExporter
	@SuppressWarnings("unchecked")
	public void writeDot(Graph g, String filename) {
		try {
			File f = new File(filename);
			PrintWriter p = new PrintWriter(new FileOutputStream(f));
			p.println("digraph {");

			for (Node m : g.getNodes()) {
				p.println(m.hashCode() + " [label=\"" + m.getAttribute("post")
						+ "\"]; ");
			}

			for (Node n : g.getNodes()) {
				for (Edge e : n.getOutgoingEdges()) {
					p.println(e.getSource().hashCode() + " -> "
							+ e.getNext().hashCode() + " [label=\""
							+ e.getAction() + "\"];");
				}
			}

			p.println("}");
			p.close();
			GraphExporter.generatePngFileFromDotFile(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Converts an event-based sourceGraph into a transition-based targetGraph
	 * 
	 * <pre>
	 * A path is a sequence of state and transition labels. For example, a graph
	 * with two nodes A,B that are connected by an edge labeled with t, the
	 * sequence "A t B" is a path. The corresponding transition trace is "t",
	 * i.e. the path without states. The corresponding state trace is "A B",
	 * i.e. the path without transitions.
	 * 
	 * The model checker we employ checks that a in a given transition system
	 * (graph) the each transition trace satisfies a set of LTL-formulas. For
	 * our purpose, we have to check that each state trace of satisfies a set
	 * of LTL-formulas.
	 * 
	 * The translation with respect to a fixed relation r is defined
	 * as follows:
	 * 
	 * Let (V,E) be the input graph, and I be fresh states. Then we
	 * define V':={I} \cup V
	 * 
	 * E':={(I,i,i) | i initial state in (V,E)} \cup {(s,t,t) | (s,r,t)
	 *      is edge in (V,E)}
	 * 
	 * Claim: \sigma is a state trace in (E,V) if and only if \sigma is a
	 * transition trace in (V', E')
	 * 
	 * Proof: TODO.
	 * </pre>
	 * 
	 * @param sourceGraph
	 *            The sourceGraph to convert
	 * @param relation
	 *            The set of relations to consider in the sourceGraph.
	 * @return The transition-based target graph
	 */
	private Graph convertGraph(IGraph<FSANode> sourceGraph, String relation) {
		Graph targetGraph = new Graph();

		FSAGraph fsa = (FSAGraph) sourceGraph; 

		// Set<T> initialMessages = sourceGraph.getDummyInitialNode();
		FSANode initialMessage = sourceGraph.getDummyInitialNode();

		Set<FSANode> allNodes = sourceGraph.getNodes();
		Node initialState = new Node(targetGraph);
		initialState.setAttribute("post", "P:initial");
		LinkedHashMap<FSANode, Node> nextState = new LinkedHashMap<FSANode, Node>();
		LinkedHashMap<FSANode, Set<Node>> prevStates = new LinkedHashMap<FSANode, Set<Node>>();

		// for (T initialMessage : initialMessages) {
		if (!prevStates.containsKey(initialMessage)) {
			prevStates.put(initialMessage, new LinkedHashSet<Node>());
		}
		prevStates.get(initialMessage).add(initialState);
		// }

		for (FSANode m : allNodes) {
			Node n = new Node(targetGraph);
			nextState.put(m, n);
			n.setAttribute("post", "P:" + m.getEType());
		}

		// TODO: retrieve an interned copy of this set.
		Set<String> relationSet = new LinkedHashSet<String>();
		relationSet.add(relation);

		for (FSANode m : allNodes) {
			for (ITransition<FSANode> t : m
					.getTransitionsWithExactRelations(relationSet)) {
				FSANode n = t.getTarget();
				if (!prevStates.containsKey(n)) {
					prevStates.put(n, new LinkedHashSet<Node>());
				}
				prevStates.get(n).add(nextState.get(t.getSource()));
			}
		}
		for (FSANode m : allNodes) {
			if (prevStates.get(m) == null) {
				throw new InternalSynopticException("null in prevStates");
			}
			if (nextState.get(m) == null) {
				throw new InternalSynopticException("null in nextState");
			}
			for (Node prev : prevStates.get(m)) {
				FSANode n = findFSANodeByNode(prev, nextState);
				if (n == null) { // prev is the initial node
					assert(prev.equals(initialState));
					Edge e = new Edge(prev, nextState.get(m), "-",m.getEType().toString(), null);
					e.setAttribute("inode", m);
				}
				else {
					Set<FSATransition> transitions = fsa.getTransitionsBetweenNodes(n, m);
					for (FSATransition t: transitions) {
						Edge e = new Edge(prev, nextState.get(m), "-",t.getLabel(), null);
						e.setAttribute("inode", m);
					}
				}
			}
		}
		return targetGraph;
	}

	private FSANode findFSANodeByNode(Node n, LinkedHashMap<FSANode, Node> nextState) {
		for (Entry<FSANode, Node> e : nextState.entrySet()) {
			if (e.getValue().equals(n)) {
				return e.getKey();
			}
		}
		return null;
	}




	/**
	 * Returns a counter-example path that violates a specific invariant in a
	 * graph.
	 * 
	 * @param inv
	 *            invariant for which we are to find a violating path * @param
	 * @param g
	 *            the graph within which the violating path must be found
	 * @return a path in g that violates inv or null if one doesn't exist
	 */
	public CExamplePath<FSANode> getCounterExample(ITemporalInvariant inv, IGraph<FSANode> g) {
		Counterexample ce;
		try {
			ce = this.check(g, inv);
		} catch (ParseErrorException e) {
			throw InternalSynopticException.wrap(e);
		}
		if (ce == null) {
			return null;
		}
		logger.finest("raw-counter-example: " + ce.toString());
		return new CExamplePath<FSANode>(inv,null);
	}

}
