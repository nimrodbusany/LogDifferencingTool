package DiffRunners;

import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.catalina.connector.OutputBuffer;

import kTailsObjects.KSeq;
import logs.LinearLogUtil;
import GraphUtils.NonDeterministicGraphPathVisualizer;

import com.alexmerz.graphviz.objects.Graph;

public class KSeqBasedDiffRunner {

	private int k;
	private boolean wrapWords;

	public KSeqBasedDiffRunner(int k, boolean WRAPWORDS) {
		this.k = k;
		this.wrapWords = WRAPWORDS;
	}

	public void writeTracesToFile(ArrayList<ArrayList<String>> coveringTraces, String outDirKTails) throws IOException {
		
		FileWriter fr = new FileWriter(new File(outDirKTails));
		for (ArrayList<String> trace : coveringTraces) {
			for (String event : trace) {
				fr.write(event);
				fr.write("\n");
			}
			fr.write("--\n");
		}
		fr.close();
	}
	
	public HashMap<String, Graph> perfomKTailsDiffKSeqApproach(String outDirKTails,
			ArrayList<String> logfiles,
			ArrayList<Graph> graphs) throws IOException {

		if (graphs.size() != 2) {
			throw new IllegalArgumentException(
					"Currently, only supporting two graphs comparison");
		}

		System.out.println("Searching for counter examples");
		HashMap<String, Graph> diffs = new HashMap<String, Graph>();

		for (int i = 0; i < graphs.size(); i++) {

			ArrayList<ArrayList<String>> traces = LinearLogUtil
					.readTraces(logfiles.get(i));
			HashSet<KSeq> ks = LinearLogUtil.extractKSeqSet(traces, k);

			for (int j = 0; j < graphs.size(); j++) {
				if (i == j) {
					continue;
				}

				ArrayList<ArrayList<String>> other_traces = LinearLogUtil
						.readTraces(logfiles.get(j));
				HashSet<KSeq> other_ks = LinearLogUtil.extractKSeqSet(
						other_traces, k);
				ArrayList<KSeq> missing_ks = new ArrayList<KSeq>();
				for (KSeq k_seq : ks) {
					if (!other_ks.contains(k_seq)) {
						missing_ks.add(k_seq);
					}
				}
				if (missing_ks.size() == 0) {
					continue;
				}
				ArrayList<ArrayList<String>> coveringTraces = findCoveringSet(
						missing_ks, traces);
				// "Visualiazing counter example"
				String fileName = "log" + i + "_" + "log" + j;
				String fullPathName = outDirKTails + File.separator + fileName + ".log";
				writeTracesToFile(coveringTraces, fullPathName);
				
		
				// if (coveringTraces.size() > 0) {
				NonDeterministicGraphPathVisualizer.visualizePathsOnTopOfGraph(
						graphs.get(i), coveringTraces, wrapWords); // TODO:
																	// IMPLEMENT
				// graphs.get(i).clone()
				// to support more than
				// a pair of graphs
				// }*/
				diffs.put(fileName, graphs.get(i));
			}
		}

		return diffs;

	}

	private ArrayList<ArrayList<String>> findCoveringSet(
			ArrayList<KSeq> missing_ks, ArrayList<ArrayList<String>> traces) {

		ArrayList<ArrayList<String>> selectedTraces = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<Integer>> coveringKs = new ArrayList<ArrayList<Integer>>();

		for (ArrayList<String> trace : traces) {
			ArrayList<Integer> coveringKsForTrace = new ArrayList<Integer>();
			ArrayList<ArrayList<String>> tmpContainer = new ArrayList<ArrayList<String>>();
			tmpContainer.add(trace);
			HashSet<KSeq> ksInTrace = LinearLogUtil.extractKSeqSet(
					tmpContainer, k);
			for (int i = 0; i < missing_ks.size(); i++) {
				KSeq k_seq = missing_ks.get(i);
				if (ksInTrace.contains(k_seq)) {
					coveringKsForTrace.add(i);
				}
			}
			coveringKs.add(coveringKsForTrace);
		}

		HashSet<KSeq> covered = new HashSet<KSeq>();
		while (missing_ks.size() != covered.size()) {
			int i = findIndexOfTraceWithMaxKSeqCoverage(coveringKs);
			selectedTraces.add(traces.get(i));
			ArrayList<Integer> coveredKs = (ArrayList<Integer>) coveringKs.get(i).clone();
			for (Integer id : coveredKs) {
				covered.add(missing_ks.get(id));
			}
			UpdateMap(coveringKs, coveredKs);
		}

		if (selectedTraces.size() > 0) {
			System.out.println("selectedTraces:");
			for (ArrayList<String> trace : selectedTraces) {
				System.out.println(trace);
			}
			System.out.println("***************");
		}
		return selectedTraces;
	}

	private int findIndexOfTraceWithMaxKSeqCoverage(
			ArrayList<ArrayList<Integer>> coveringKs) {

		int max_coverage = -1;
		int index = -1;

		for (int i = 0; i < coveringKs.size(); i++) {
			if (coveringKs.get(i).size() > max_coverage) {
				max_coverage = coveringKs.get(i).size();
				index = i;
			}
		}

		return index;
	}

	private void UpdateMap(ArrayList<ArrayList<Integer>> coveringKs,
			ArrayList<Integer> coveredKs) { // CHECK THIS
		for (ArrayList<Integer> coveredByTrace : coveringKs) {
			if (coveredByTrace.size() == 0) {
				continue;
			}
			coveredByTrace.removeAll(coveredKs);
		}
	}

}
