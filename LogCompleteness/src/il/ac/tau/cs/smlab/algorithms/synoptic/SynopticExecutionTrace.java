package il.ac.tau.cs.smlab.algorithms.synoptic;

import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.utils.TernaryValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import synoptic.invariants.TemporalInvariantSet;
import synoptic.model.ChainsTraceGraph;
import synoptic.model.EventNode;
import synoptic.model.interfaces.ITransition;

public class SynopticExecutionTrace extends ExecutionTrace {

	private ChainsTraceGraph traceGraph;
	private TemporalInvariantSet minedInvariants;
	private List<EventType> seen;

	public SynopticExecutionTrace(ChainsTraceGraph traceGraph, int traceId) {
		super(traceId);
		this.traceGraph = traceGraph;
		this.seen = getAllEventTypes();
	}

	@Override
	public boolean contains(EventType e) {
		return seen.contains(e);
	}


	@Override
	public boolean contains(EventTypeSeq events) {
		EventNode node =  traceGraph.getDummyInitialNode();
		List<EventType> t = new ArrayList<EventType>(size());
		while (node.getAllTransitions().size() != 0) {
			node = node.getAllTransitions().get(0).getTarget();
			t.add(new EventType(node.getEType().getETypeLabel()));
		}
		
		int index = Collections.indexOfSubList(t,events.getEvents());
		return (index != -1);
		
	}


	public ChainsTraceGraph getTraceGraph() {
		return traceGraph;
	}

	public TemporalInvariantSet getMinedInvariants() {
		return minedInvariants;
	}

	public void setMinedInvariants(TemporalInvariantSet minedInvariants) {
		this.minedInvariants = minedInvariants;
	}


	private List<EventType> getAllEventTypes() {
		EventNode initNode = traceGraph.getDummyInitialNode();
		Set<synoptic.model.event.EventType> eTypes = new LinkedHashSet<synoptic.model.event.EventType>();

		// Iterate through all the traces -- each transition from the
		// INITIAL node connects\holds a single trace.
		for (ITransition<EventNode> initTrans : initNode.getAllTransitions()) {

			EventNode cur = initTrans.getTarget();
			synoptic.model.event.EventType first = initNode.getEType();
			synoptic.model.event.EventType second = cur.getEType();

			while (true) {
				if (!eTypes.contains(first)) {
					eTypes.add(first);
				}
				if (cur.getAllTransitions().size() == 0) {
					// Add terminal event to the set.
					if (!eTypes.contains(second)) {
						eTypes.add(second);
					}
					break;
				}
				cur = cur.getAllTransitions().get(0).getTarget();
				first = second;
				second = cur.getEType();
			}
		}
		List<EventType> events = new ArrayList<EventType>(eTypes.size());
		for (synoptic.model.event.EventType e : eTypes) {
			events.add(new EventType(e.getETypeLabel()));
		}
		return events;
	}

	@Override
	public int size() {
		return traceGraph.getNodes().size();
	}

	public List<EventType> project(Alphabet alphabet) {
		List<EventType> prj = new LinkedList<EventType>();
		EventNode node = traceGraph.getDummyInitialNode();
		assert(node != null);

		while (node.getAllTransitions().size() != 0) {
			node = node.getAllTransitions().get(0).getTarget();
			// check if node should be projected
			if (alphabet.contains(node.getEType().getETypeLabel())) {
				prj.add(new EventType(node.getEType().getETypeLabel()));
			}
		}
		return prj;
	}
	
	List<EventType> project(List<EventType> l, List<EventType> ab) {
		List<EventType> p = new LinkedList<EventType>();
		for (EventType e:l) {
			if (ab.contains(e)) {
				p.add(e);
			}
		}
		return p;
	}

	
	@Override
	public TernaryValue adjacentUnderProjection(Alphabet ab,
			EventTypeSeq p, EventTypeSeq m, boolean mineTriggers) {
		
		List<EventType> projectedTrace = project(ab);
		List<EventType> preChart = p.getEvents();
		List<EventType> mainChart = m.getEvents();
		
		if (mineTriggers) {
			return mineTrigger(projectedTrace,preChart,mainChart);
		}
		return mineEffect(projectedTrace,preChart,mainChart);
	}
	
	
	private TernaryValue mineEffect(List<EventType> projectedTrace, List<EventType> preChart, List<EventType> mainChart) {
		boolean seenPre = false;
		while (true) {
			
			// search for p
			int index = Collections.indexOfSubList(projectedTrace, preChart);
			if (index == -1) { // p doesn't appear anymore
				if (seenPre) {
					return TernaryValue.TRUE; // pre was seen at least once with main following it
				}
				return TernaryValue.UNKNOWN; // pre was never seen at all in this trace
			}
			seenPre = true;
			// p appears, check if m is directly following it
			if (projectedTrace.size() < index + preChart.size() + mainChart.size()) {
				return TernaryValue.FALSE;
			}
			if (!projectedTrace.subList(index + preChart.size(), index + preChart.size() + mainChart.size()).equals(mainChart)) {
				return TernaryValue.FALSE; // p without m
			}
			projectedTrace = projectedTrace.subList(index + preChart.size() + mainChart.size(), projectedTrace.size());
		}
	}
	
	
	private TernaryValue mineTrigger(List<EventType> projectedTrace, List<EventType> preChart, List<EventType> mainChart) {
		while (true) {
			
			// search for m
			int index = Collections.indexOfSubList(projectedTrace, mainChart);
			if (index == -1) {
				return TernaryValue.TRUE; // m doesn't appear anymore
			}
			// m appears, check if p is directly before it
			if (0 > index - preChart.size()) {
				return TernaryValue.FALSE;
			}
			if (!projectedTrace.subList(index - preChart.size(), index).equals(preChart)) {
				return TernaryValue.FALSE; // m without p
			}
			projectedTrace = projectedTrace.subList(index + mainChart.size(), projectedTrace.size());
		}
	}

	@Override
	public boolean isTerminal(EventType e) {
		EventNode node =  traceGraph.getDummyInitialNode();
		while (node.getAllTransitions().size() != 0) {
			EventNode next = node.getAllTransitions().get(0).getTarget();
			if (next.isTerminal() && e.getEvent().equals(node.getEType().getETypeLabel())) return true; // e -> terminal
			node = next;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		EventNode node =  traceGraph.getDummyInitialNode();
		while (node.getAllTransitions().size() != 0) {
			node = node.getAllTransitions().get(0).getTarget();
			sb.append(node.getEType());
			sb.append("\n");
		}
		sb.append("--");
		return sb.toString();
	}
}
