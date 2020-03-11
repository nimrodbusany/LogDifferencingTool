package il.ac.tau.cs.smlab.fw.trace.generator;

import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class DuplicateTraceRemover {
	
	public static void remove(String filename) throws IOException {

		File full = new File(filename);
		String fullStr = FileUtils.readFileToString(full);
		String[] allTraces = fullStr.split("--");

		Set<String> uniqueTraces = new LinkedHashSet<String>(Arrays.asList(allTraces));
		
		StringBuilder sb = new StringBuilder();
		
		for (String t : uniqueTraces) {
			sb.append(t);
			sb.append("--");
		}
		
		File partial = new File(filename);
		FileUtils.write(partial, sb.toString());
	}

	public static void main(String[] args) throws IOException {
		String filename = SystemConstants.LOG_DIRECTORY + "Heretix_generated_trace_1_by_states.txt";
	//	String output = SystemConstants.LOG_DIRECTORY + "Heretix_generated_trace_2_by_states_no_duplicates"+".txt";

		remove(filename);
	}
	
}
