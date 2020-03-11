package il.ac.tau.cs.smlab.fsa;

import il.ac.tau.cs.smlab.fsa.adapter.FSAGraph;
import il.ac.tau.cs.smlab.fsa.adapter.FSANode;
import il.ac.tau.cs.smlab.fw.property.model.CompletenessLTLChecker;
import synoptic.invariants.ITemporalInvariant;
import synoptic.invariants.ltlchecker.GraphLTLChecker;
import synoptic.model.ChainsTraceGraph;
import synoptic.model.EventNode;

public class FSAInvariantChecker {
	
	static GraphLTLChecker<FSANode> checker = new GraphLTLChecker<FSANode>();
	
	static CompletenessLTLChecker transitionBasedGraphchecker = new CompletenessLTLChecker();
	
	static GraphLTLChecker<EventNode> checkerTrace = new GraphLTLChecker<EventNode>();
	
	public static boolean isSatisfied(ITemporalInvariant i, FSAGraph fsaGraph) {
		return transitionBasedGraphchecker.getCounterExample(i, fsaGraph) == null;
	}
	
	public static boolean isSatisfied(ITemporalInvariant i, ChainsTraceGraph tgraph) {
		return checkerTrace.getCounterExample(i, tgraph) == null;
	}
	
}
