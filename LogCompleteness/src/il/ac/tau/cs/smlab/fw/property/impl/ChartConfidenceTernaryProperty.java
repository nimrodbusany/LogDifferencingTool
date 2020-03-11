package il.ac.tau.cs.smlab.fw.property.impl;

import il.ac.tau.cs.smlab.algorithms.lsc.ChartAlwaysFollowedByChartInvariant;
import il.ac.tau.cs.smlab.algorithms.lsc.PreChartAppearsInvariant;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.FSAInvariantChecker;
import il.ac.tau.cs.smlab.fw.property.log.AbstractLogProperty;
import il.ac.tau.cs.smlab.fw.property.log.LogPropertyValues;
import il.ac.tau.cs.smlab.fw.property.model.ModelProperty;
import il.ac.tau.cs.smlab.fw.property.model.ModelPropertyValues;
import il.ac.tau.cs.smlab.fw.property.model.fsa.FSAModelPropertyChecker;
import il.ac.tau.cs.smlab.fw.trace.Alphabet;
import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;
import il.ac.tau.cs.smlab.fw.trace.KEventTypeSeqGenerator;
import il.ac.tau.cs.smlab.fw.utils.TernaryValue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class ChartConfidenceTernaryProperty extends AbstractLogProperty<TernaryValue,TernaryValue> implements ModelProperty<FSA,TernaryValue> {

	private List<String> chart;
	private boolean mineTriggers;
	List<EventTypeSeq> eventSeqs;
	Alphabet alphabet;
	private final static int MAX_CHART_SIZE = 2;
	FSAModelPropertyChecker<TernaryValue> checker = new FSAModelPropertyChecker<TernaryValue>();

	public ChartConfidenceTernaryProperty(List<String> chart, boolean mineTriggers) {
		this.mineTriggers = mineTriggers;
		this.chart = chart;
	}

	@Override
	public List<EventTypeSeq> generateAllEventSequences(Alphabet alphabet) {
		
		if (!alphabet.equals(this.alphabet)) {
			eventSeqs = null;
		}
		
		if (eventSeqs == null) {
			eventSeqs = new LinkedList<EventTypeSeq>();
			KEventTypeSeqGenerator generator = new KEventTypeSeqGenerator();

			// {alphabet} \ {alphabet(chart)}
			Alphabet abToMine = alphabet.remove(chart);

			// since event repetitions are not allowed, generate permutations of abToMine
			for (int k = 1 ; k <= MAX_CHART_SIZE ; ++k) {
				eventSeqs.addAll(generator.generateWithoutElementRepetition(abToMine,k));
			}
			this.alphabet = alphabet;
		}
		return eventSeqs;
	}

	@Override
	public String getPropertyName() {
		return "Confidence";
	}

	// check if t++e is in t
	@Override
	public TernaryValue propertyResult(ExecutionTrace t, EventTypeSeq e) {

		// create the alphabet of t++e
		List<EventType> ab = new ArrayList<EventType>(e.size() + chart.size());
		ab.addAll(e.getEvents());

		List<EventType> chartSeq = new ArrayList<EventType>(chart.size());
		for (String s : chart) {
			EventType c = new EventType(s);
			assert(!ab.contains(c)); // verify that pre and main are disjoint
			chartSeq.add(c);
		}
		ab.addAll(chartSeq);
		Alphabet alphabet = new Alphabet(ab);

		EventTypeSeq c = new EventTypeSeq(chartSeq);
		TernaryValue result;
		if (mineTriggers) { // e++chart
			result = t.adjacentUnderProjection(alphabet, e, c, mineTriggers);
		}
		else { // mine effects, chart++e
			result = t.adjacentUnderProjection(alphabet, c, e, mineTriggers);
		}
		return result;
	}

	@Override
	public TernaryValue isSatisfied(FSA fsa, EventTypeSeq e) {
		List<EventType> chartSeq = new ArrayList<EventType>(chart.size());
		for (String s : chart) {
			chartSeq.add(new EventType(s));
		}
		if (mineTriggers) throw new UnsupportedOperationException();

		PreChartAppearsInvariant j = new PreChartAppearsInvariant(chartSeq,e.getEvents()); 
		// first check if pre-chart appears
		if (FSAInvariantChecker.isSatisfied(j, fsa.getGraph())) {
			return TernaryValue.UNKNOWN;
		}
		ChartAlwaysFollowedByChartInvariant i = new ChartAlwaysFollowedByChartInvariant(chartSeq,e.getEvents()) ;
		boolean result = FSAInvariantChecker.isSatisfied(i, fsa.getGraph());
		if (result) {
			System.out.println(chart + "++" + e +  " was found to be true in the model");
		}
		if (result) {
			return TernaryValue.TRUE;
		}
		return TernaryValue.FALSE;
	}

	@Override
	public boolean prunable() {
		return false;
	}

	@Override
	public TernaryValue propertyAggregation(List<TernaryValue> vals) {
		if (vals.contains(TernaryValue.TRUE) && !vals.contains(TernaryValue.FALSE)) return TernaryValue.TRUE;
		if (vals.contains(TernaryValue.FALSE)) return TernaryValue.FALSE;
		return TernaryValue.UNKNOWN;
	}

	@Override
	public double propetyCompleteness(LogPropertyValues<TernaryValue> vals, int n) {
		double confidence;
		double c1 = 0.0;
		double c2 = 0.0;
		int t,f,u;

		for (Entry<EventTypeSeq, List<TernaryValue>> seqVals : vals.getPropVals().entrySet()) {
			t = 0;
			f = 0;
			u = 0;
			for (TernaryValue l : seqVals.getValue()) {
				if (l.equals(TernaryValue.TRUE)) {
					t+=1;
				}
				if (l.equals(TernaryValue.UNKNOWN)) {
					u+=1;
				}
				if (l.equals(TernaryValue.FALSE)) {
					f+=1;
				}
			}
			double p_u = (double)u/n;
			
			// to account for f = 0, Y = 1
			if (f > 0) { 
			//	double p_t = (double)t/n;
				double p_tu = (double)(t+u)/n;
				c1 += (1-Math.pow(p_u, n)) * Math.pow(p_tu,n);
			//	c1 += (1-Math.pow(1-p_t, n)) * Math.pow(p_tu,n);
				c1 = Math.min(1, c1);
			}
			
			// to account for f != -1, Y = -1
			if (u!=n) { // q_tg > 0
				c2 += Math.pow(p_u, n);
				c2 = Math.min(1, c2);
			}

		}
	//	c1 = 1 - c1;
	//	c2 = 1 - c2;
		confidence = 1 - c1 - c2;
		confidence = Math.max(0, confidence);
		return confidence;
	}
	
	
	public List<String> getChart() {
		return chart;
	}

	public void setChart(List<String> chart) {
		this.chart = chart;
		this.eventSeqs = null;
	}


	@Override
	public ModelPropertyValues<TernaryValue> check(FSA model) {
		return checker.check(this, model);
	}
}
