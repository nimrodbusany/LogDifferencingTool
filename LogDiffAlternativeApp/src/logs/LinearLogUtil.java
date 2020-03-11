package logs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import kTailsObjects.KSeq;

public class LinearLogUtil {

	public static ArrayList<ArrayList<String>> readTraces(String filePath) {

		ArrayList<ArrayList<String>> traces = new ArrayList<ArrayList<String>>();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath)));

			String line;
			ArrayList<String> trace = new ArrayList<String>();
			while ((line = br.readLine()) != null) {

				if (line.equals("--")) {
					traces.add(trace);
					trace = new ArrayList<String>();
					continue;
				}
				trace.add(line);

			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return traces;
	}

	public static ArrayList<ArrayList<String>> writeTraces(
			ArrayList<ArrayList<String>> traces, String filePath) {

		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(filePath));

			for (ArrayList<String> trace : traces) {
				for (String event : trace) {
					fw.write(event + "\n");
				}
				fw.write("--\n");
			}
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return traces;
	}

	public static ArrayList<ArrayList<String>> extractKs(
			ArrayList<ArrayList<String>> traces, int kParam) {

		HashSet<KSeq> Ks = new HashSet<KSeq>();
		extractSeqOfLenghtK(traces, Ks, kParam);

		ArrayList<ArrayList<String>> k_seqs = new ArrayList<ArrayList<String>>();
		for (KSeq k_seq : Ks) {
			k_seqs.add(k_seq.getKSeqString());
		}
		return k_seqs;
	}

	public static HashSet<KSeq> extractKSeqSet(
			ArrayList<ArrayList<String>> traces, int kParam) {

		HashSet<KSeq> Ks = new HashSet<KSeq>();
		for (int cur_k = 1; cur_k <= kParam; cur_k++) {
			extractSeqOfLenghtK(traces, Ks, cur_k);
		}

		return Ks;
	}

	private static void extractSeqOfLenghtK(
			ArrayList<ArrayList<String>> traces, HashSet<KSeq> Ks, int cur_k) {
		for (ArrayList<String> trace : traces) {
			ArrayList<String> currentK = new ArrayList<String>();
			int i = 0;

			if (trace.size() < cur_k) { // Skip if trace has less than k
											// events
				continue;
			}

			while (i < cur_k) { // Handle first k
				currentK.add(trace.get(i));
				i += 1;
			}
			Ks.add(new KSeq((ArrayList<String>) currentK.clone()));
			for (int j = i; j < trace.size(); j++) { // Handle successive ks
														// in
														// trace
				String event = trace.get(j);
				currentK.remove(0);
				currentK.add(event);
				if (currentK.size() == cur_k) {
					KSeq k_seq = new KSeq(
							(ArrayList<String>) currentK.clone());
					Ks.add(k_seq);
				}
			}
		}
	}

}
