package logs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import kTailsObjects.KSeq;

public class TraceManipulatpor {

	int kParam = 0;

	public TraceManipulatpor(int k) {
		kParam = k;
	}

	public void printKs(ArrayList<ArrayList<String>> ks) {

		List<String> ks_as_strings = new ArrayList<String>();
		for (ArrayList<String> kSeq : ks) {
			String k_seq_str = "";
			for (String event : kSeq) {
				k_seq_str += event + ",";
			}
			k_seq_str = k_seq_str.substring(0, k_seq_str.length() - 1);
			ks_as_strings.add(k_seq_str);
		}
		Collections.sort(ks_as_strings);
		for (String e : ks_as_strings) {
			System.out.println(e);
		}
	}

	public ArrayList<ArrayList<String>> flipManipulation(
			ArrayList<ArrayList<String>> traces, int numOfTraceToAdd, int kParam) {

		ArrayList<ArrayList<String>> copy_traces = (ArrayList<ArrayList<String>>) traces.clone();
		
		ArrayList<ArrayList<String>> orig_traces =  (ArrayList<ArrayList<String>>) traces.clone();
		
		int numofpossibileks = numOfPossibileKs(orig_traces);
		
		ArrayList<ArrayList<String>> ks = LinearLogUtil.extractKs(traces,
				kParam);
		ArrayList<ArrayList<String>> new_ks = null;
		ArrayList<String> manTrace = null;
		
		while (numOfTraceToAdd > 0) 
		{
			manTrace = generatedManipluatedTrace(copy_traces);
			
			ArrayList<ArrayList<String>> tmp = new ArrayList<ArrayList<String>>();
			
			tmp.addAll(orig_traces);
			tmp.add(manTrace);
			
			new_ks = LinearLogUtil.extractKs(tmp, kParam);
			
			if (ks.size() >= numofpossibileks * 0.6)
			{
				System.out.println("We don't add ks");
				
			}
			else if (equalKs(ks, new_ks)) 
			{
				continue;
			}

			copy_traces.add(manTrace);
			//ks = LinearLogUtil.extractKs(copy_traces, kParam); // UPDATE SET OF K-SEQUENCES
			//if (numofpossibileks <= (ks.size()/2))
			//ks = LinearLogUtil.extractKs(orig_traces, kParam); 
			
			numOfTraceToAdd--;
		}

		System.out.println("traces added: "
				+ (copy_traces.size() - traces.size()));

		return copy_traces;
	}
	
	private int numOfPossibileKs(ArrayList<ArrayList<String>> traces)
	{
		Set<String> stringSet = new HashSet<String>();
		
		for (ArrayList<String> trace : traces)
		{
			stringSet.addAll(trace);
		}
		
		return stringSet.size() * stringSet.size();
	}

	private boolean equalKs(ArrayList<ArrayList<String>> ks,
			ArrayList<ArrayList<String>> other_ks) {

		if (other_ks == null || ks == null) {
			return false;
		}

		HashSet<KSeq> ksSet = new HashSet<KSeq>();
		for (ArrayList<String> ks_seq : ks) {
			KSeq k_seq_obj = new KSeq((ArrayList<String>) ks_seq);
			ksSet.add(k_seq_obj);
		}
		for (ArrayList<String> ks_seq : other_ks) {
			KSeq k_seq_obj = new KSeq((ArrayList<String>) ks_seq);
			if (!ksSet.contains(k_seq_obj)) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<String> generatedManipluatedTrace(
			ArrayList<ArrayList<String>> traces) {

		Random rand = new Random();

		ArrayList<String> trace = null;
		while (trace == null) {
			int n = rand.nextInt(traces.size() - 1);
			trace = traces.get(n);
			if (trace.size() < 2) {
				trace = null;
			}
		}
		int manipuluation_index = rand.nextInt(trace.size() - 1);
		ArrayList<String> new_trace = new ArrayList<String>();
		for (int i = 0; i < trace.size(); i++) {
			if (i == manipuluation_index) {
				new_trace.add(trace.get(i + 1));
				new_trace.add(trace.get(i));
				i += 1;
			} else {
				new_trace.add(trace.get(i));
			}
		}
		return new_trace;

	}

}