package il.ac.tau.cs.smlab.fw.property.impl;

import il.ac.tau.cs.smlab.fsa.FSA;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.distribution.BinomialDistribution;
/**
 * 
 * Currently this property is not being used by any mining algorithm
 *
 */
public class ChartSupportProperty extends AbstractLogProperty<Boolean,Boolean> implements ModelProperty<FSA,Boolean> {

	private List<String> chart;
	private int support;
	List<EventTypeSeq> eventSeqs;
	FSAModelPropertyChecker<Boolean> checker = new FSAModelPropertyChecker<Boolean>();
	

	public ChartSupportProperty(List<String> chart, int support) {
		this.chart = chart;
		this.support = support;
	}

	@Override
	public List<EventTypeSeq> generateAllEventSequences(Alphabet alphabet) {
		if (eventSeqs == null) {
			eventSeqs = new LinkedList<EventTypeSeq>();
			KEventTypeSeqGenerator generator = new KEventTypeSeqGenerator();

			// {alphabet} \ {alphabet(chart)}
			Alphabet abToMine = alphabet.remove(chart);

			// since event repetitions are not allowed, generate permutations of abToMine
			for (int k = 1 ; k <= abToMine.size() ; ++k) {
				eventSeqs.addAll(generator.generate(abToMine,k));
			}
		}
		return eventSeqs;
	}

	@Override
	public String getPropertyName() {
		return "Support";
	}

	// check if t++e is in t
	@Override
	public Boolean propertyResult(ExecutionTrace t, EventTypeSeq e) {
		// create the alphabet of t++e
		List<EventType> ab = new LinkedList<EventType>();
		ab.addAll(e.getEvents());
		for (String s : chart) {
			EventType c = new EventType(s);
			if (!ab.contains(c)) {
				ab.add(c);
			}
		}
		
		// create t++e
		List<EventType> te = new ArrayList<EventType>(e.size() + chart.size());
		for (String s : chart) {
			te.add(new EventType(s));
		}
		te.addAll(e.getEvents());
		
		return true;
		
	}

	@Override
	public Boolean propertyAggregation(List<Boolean> vals) {
		int count = 0;
		for (boolean v : vals) {
			if (v) {
				++count;
			}
		}
		return count >= support;
	}

	@Override
	public double propetyCompleteness(LogPropertyValues<Boolean> vals, int n) {
		// Cumulative distribution function of binomial distribution
		// B(n, q_t++e) where q_t++e is estimated by avg of vals
		
		double confidence = 0.0;
		int sum;
		for (Entry<EventTypeSeq, List<Boolean>> seqVals : vals.getPropVals().entrySet()) {
			sum = 0;
			for (Boolean l : seqVals.getValue()) {
				if (l) {
					sum+=1;
				}
			}
			if (sum >= support) {
				double p = (double)sum / n; // q_t++e
				
				// P[T_agg = 0] = P(X <= support), X~B(n,p)
				BinomialDistribution distribution = new BinomialDistribution(n,p);
				confidence += distribution.cumulativeProbability(support); 
				confidence = Math.min(1, confidence);
			}
		}
		confidence = 1 - confidence;
		System.out.println(this.getPropertyName());
		System.out.println("confidence=" + confidence);
		return confidence;
	}

	@Override
	public Boolean isSatisfied(FSA model, EventTypeSeq seq) {
		return true;
	}

	@Override
	public boolean prunable() {
		return false;
	}

	@Override
	public ModelPropertyValues<Boolean> check(
			FSA model) {
		return checker.check(this, model);
	}
	

}
