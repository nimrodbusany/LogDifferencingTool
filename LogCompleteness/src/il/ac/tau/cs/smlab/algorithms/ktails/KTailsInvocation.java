package il.ac.tau.cs.smlab.algorithms.ktails;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticTraceProvider;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;

import java.io.IOException;

import org.junit.Test;

import synoptic.algorithms.KTails;
import synoptic.model.PartitionGraph;
import synoptic.model.export.GraphExporter;

public class KTailsInvocation {

	
	@Test
	public void run() throws SpecMiningAlgorithmException, InvalidModelException, IOException {
		SynopticTraceProvider traceProvider = getTraceProvider("columba");
		int k = 2;
		PartitionGraph g = KTails.performKTails(traceProvider.getFullTraceGraph(), k);
		String dotFileName = "c:\\ktails_full.dot.txt";
		GraphExporter.exportGraph(dotFileName, g, false);
		GraphExporter.generatePngFileFromDotFile(dotFileName);
		
		traceProvider = getTraceProvider("columba_15");
		g = KTails.performKTails(traceProvider.getFullTraceGraph(), k);
		dotFileName = "c:\\ktails_partial.dot.txt";
		GraphExporter.exportGraph(dotFileName, g, false);
		GraphExporter.generatePngFileFromDotFile(dotFileName);
		
	}
	
	
	private SynopticTraceProvider getTraceProvider(String log) throws SpecMiningAlgorithmException, InvalidModelException {
		return new SynopticTraceProvider(getSynopticInputParams(log));
	}


	private SynopticInputParams getSynopticInputParams(String model) {
		return new SynopticInputParams("ktails/" + model, "--", "(?<TYPE>.*)", null);
	}
	
}
