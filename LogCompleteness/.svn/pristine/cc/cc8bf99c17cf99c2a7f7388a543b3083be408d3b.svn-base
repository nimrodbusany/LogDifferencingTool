package il.ac.tau.cs.smlab.algorithms.synoptic;

import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import synoptic.main.SynopticMain;
import synoptic.main.parser.TraceParser;
import synoptic.model.ChainsTraceGraph;
import synoptic.model.EventNode;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;


// provides traces from a .txt log file
public class SynopticTraceProvider implements TraceProvider {

	protected ChainsTraceGraph fullTraceGraph;
	protected int currTraceIndex = 0;
	protected List<Integer> traceOrder;
	protected String logFileName;

	protected SynopticTraceProvider() {}

	public SynopticTraceProvider(SynopticInputParams params) throws SpecMiningAlgorithmException {
		SynopticMain synopticInstance = new SynopticInvocation(params).processSynopticArgs();
		try {
			TraceParser parser = new TraceParser(synopticInstance.options.regExps,
					synopticInstance.options.partitionRegExp, synopticInstance.options.separatorRegExp);
			List<EventNode> parsedEvents;

			parsedEvents = SynopticMain.parseEvents(parser, synopticInstance.options.logFilenames);
			fullTraceGraph = SynopticMain.genChainsTraceGraph(parser, parsedEvents);
			
			traceOrder = generateTraceOrder(fullTraceGraph.getNumTraces());
			logFileName = synopticInstance.options.logFilenames.get(0);
		} catch (Exception e) {
			throw new SpecMiningAlgorithmException(e);
		}

	}


	protected List<Integer> generateTraceOrder(int i) {
		List<Integer> order = new ArrayList<Integer>(i);
		for (int j = 0 ; j < i ; j++) {
			order.add(j);
		}
		Collections.shuffle(order);
		return order;
	}

	@Override
	public ExecutionTrace next() {
		int currTraceId = traceOrder.get(currTraceIndex);
		Collection<EventNode> nodes = Collections2.filter(fullTraceGraph.getNodes(), new TracePredicate(currTraceId));
		List<EventNode> events = new ArrayList<EventNode>(nodes.size());
		events.addAll(nodes);
		ChainsTraceGraph traceGraph = new ChainsTraceGraph(events);
		Set<EventNode> traceInitialNode = fullTraceGraph.getTraceIdToInitNodes().get(currTraceId);
		traceGraph.setDummyTerminalNode(fullTraceGraph.getDummyTerminalNode());
		traceGraph.addTrace(fullTraceGraph.getTraces().get(currTraceId),traceInitialNode);
		++currTraceIndex;
		return new SynopticExecutionTrace(traceGraph,currTraceId);
		

	}

	@Override
	public void remove() {

	}

	@Override
	public boolean hasNext() {
		return fullTraceGraph.getNumTraces() - currTraceIndex > 0;
	}

	@Override
	public Alphabet getAlphabetInTrace() {
		Alphabet alphabet = new Alphabet();
		for (EventNode n: fullTraceGraph.getNodes()) {
			String e = n.getEType().getETypeLabel();
			if (!n.getEType().isSpecialEventType() && !alphabet.contains(e)) {
		//	if (!alphabet.contains(e)) {
				alphabet.addEvent(e);
			}
		}
		return alphabet;
	}


	protected class TracePredicate implements Predicate<EventNode> {

		private int traceId;

		private TracePredicate(int traceId) {
			this.traceId = traceId;
		}

		@Override
		public boolean apply(EventNode e) {
			return e.getTraceID() == traceId && !e.isInitial() && !e.isTerminal();
		}

	}
	
	public List<Integer> getTraceOrder() {
		return traceOrder;
	}
	
	public ChainsTraceGraph getFullTraceGraph() {
		return fullTraceGraph;
	}

	public String getLogFilename() {
		return logFileName;
	}
}
