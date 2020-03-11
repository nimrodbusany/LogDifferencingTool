package il.ac.tau.cs.smlab.fw.trace.generator;

import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class TraceSlicer {

	String filename;

	public TraceSlicer(String filename) {
		this.filename = filename;
	}

	public void slice(List<Integer> indexes, String output) throws IOException {

		File full = new File(filename);
		String fullStr = FileUtils.readFileToString(full);
		String[] allTraces = fullStr.split("--");

		StringBuilder sb = new StringBuilder();
		for (int index : indexes) {
			if (index == 0) sb.append("\n");
			sb.append(allTraces[index]);
			sb.append("--");
		}
		File partial = new File(output);
		if (sb.subSequence(0, 1).equals("\n")) { 
			sb.deleteCharAt(0);}
		FileUtils.write(partial, sb.toString());
	}

	public static void main(String[] args) throws IOException {
		int slicedSize = 5;
		String filename = SystemConstants.LOG_DIRECTORY + "ORW_generated_trace_0_by_states.txt";
		String output = SystemConstants.LOG_DIRECTORY + "ORW_generated_trace_0_by_states_"+slicedSize+".txt";
		TraceSlicer slicer = new TraceSlicer(filename);

		List<Integer> list = Arrays.asList(19, 14, 24, 27, 3, 20, 22, 21, 18, 2, 17, 26, 28, 6, 10, 29, 16, 5, 8, 1, 11, 7, 23, 4, 0, 13, 15, 12, 9, 25
				).subList(0, slicedSize - 1);
		slicer.slice(list, output);
	}
}
