package il.ac.tau.cs.smlab.fw.evaluation.results.demo;

import il.ac.tau.cs.smlab.fw.trace.EventType;
import il.ac.tau.cs.smlab.fw.trace.EventTypeSeq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatrixDemoResults extends DemoResults {
	
	@Override
	public List<String[]> getBreakdownEntries() {

		SingleTrialSingleModelDemoEvaluationResults dt = (SingleTrialSingleModelDemoEvaluationResults) models.get(0).getTrials().get(0);
		
		Map<EventTypeSeq, Double> last = dt.getqValues().get(dt.getqValues().size()-1);
		Set<EventType>  events = new HashSet<EventType>();
		for (EventTypeSeq s : last.keySet()) {
			events.addAll(s.getEvents());
		}
		events.remove(new EventType("TERMINAL"));
		events.remove(new EventType("INITIAL"));
		EventType[] ab = events.toArray(new EventType[0]);
		
		List<String[]> entries = new ArrayList<String[]>(ab.length + 1);
		
		// add headline
		String[] headlineArr = new String[ab.length + 1];
		headlineArr[0] = "event";
		for (int i = 1 ; i<= ab.length ; ++i) {
			headlineArr[i] = ab[i-1].getEvent();
		}

		List<String> headline = new ArrayList<String>(headlineArr.length);
		headline.addAll(Arrays.asList(headlineArr));
		entries.add(headlineArr);
		
		assert(dt.getqValues().size() == dt.getConfContribution().size());
		assert(dt.getTraceValues().size() == dt.getConfContribution().size());
		double total = 0;
		Map<EventTypeSeq, Double> qs = dt.getqValues().get(dt.getqValues().size()-1);
		Map<EventTypeSeq, Double> confs = dt.getConfContribution().get(dt.getConfContribution().size()-1);
		for (EventType e : ab) { // row of event e
			String[] entry = new String[(headlineArr.length)];
			entry[headline.indexOf("event")] =  e.getEvent();
			for (int i = 0 ; i < ab.length; i++) { // col of event ab[i]
				EventTypeSeq es = new EventTypeSeq(e,ab[i]);
				if (qs.get(es) == null || confs.get(es) == null) {
					continue;
				}
				String val = String.format("%.3f",qs.get(es)) + String.format(" (-%.3f)",confs.get(es));
				entry[headline.indexOf(ab[i].getEvent())]=val;
			}
			entries.add(entry);
		}
		String[] entry = new String[2];
		entry[headline.indexOf("event")]="total";
		entry[1]=String.format("%.3f",1-sum(confs.values()));
		entries.add(entry);
		return entries;
	}

}
