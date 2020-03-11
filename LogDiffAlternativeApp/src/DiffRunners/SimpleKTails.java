package DiffRunners;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;

import il.ac.tau.cs.smlab.diff.util.Utils;
import synopticdiff.algorithms.KTails;
import synopticdiff.main.parser.ParseException;
import synopticdiff.main.parser.TraceParser;
import synopticdiff.model.ChainsTraceGraph;
import synopticdiff.model.PartitionGraph;
import synopticdiff.model.export.GraphExporter;
import synopticdiff.tests.SynopticTest;
import synopticdiff.util.InternalSynopticException;

public class SimpleKTails {

	public int numOfTraces = 0;
	public double tracesSize = 0;
	
	public Graph runKTails(int kParam, String log_file_name, String output_file, String baseDir)
			throws IOException, InternalSynopticException, ParseException {

		File log_file = new File(log_file_name);
		byte[] bytes = Files.readAllBytes(log_file.toPath());
		String log = new String(bytes);

		String partition = "^--$";

		String regExp = "^(?<TYPE>)$";

		List<List<String>> firstTracesList = null;

		firstTracesList = Utils.readFromFiles(log, "--");

		numOfTraces += firstTracesList.size();

		for (List<String> trace : firstTracesList) {
			tracesSize += trace.size();
		}

		String logLines[] = log.split("\\r?\\n");

		TraceParser defParser = null;

		if (partition == null || regExp == null) {
			defParser = SynopticTest.genDefParser();
		} else {
			defParser = new TraceParser();

			try {
				defParser.addRegex(regExp);

				defParser.addPartitionsSeparator(partition);
			} catch (Exception e) {
				defParser = SynopticTest.genDefParser();
			}
		}

		ChainsTraceGraph ret = (ChainsTraceGraph) SynopticTest.genChainsTraceGraph(logLines, defParser);

		double miliSecondsStart = System.currentTimeMillis();

		PartitionGraph result = null;

		result = KTails.performKTails(ret, kParam);

		double miliSecondsEnd = System.currentTimeMillis();

		//miliSecondsOverall = ((miliSecondsEnd - miliSecondsStart));

		List<Integer> tracesList = new ArrayList<Integer>();

		tracesList.add(firstTracesList.size());

		File resultDir = new File(baseDir + File.separator + "output");

		resultDir.mkdir();

		GraphExporter.exportGraph(output_file, result, true, tracesList);

		try 
		{
			FileReader in = null;

			File f = new File(output_file);

			in = new FileReader(f);
			Parser p = new Parser();
			p.parse(in);
			in.close();
			
			return p.getGraphs().get(0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
