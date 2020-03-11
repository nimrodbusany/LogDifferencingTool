package il.ac.tau.cs.smlab.fw.trace.generator;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInvocation;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;

import java.io.File;
import java.util.List;

import synoptic.main.SynopticMain;
import synoptic.main.parser.TraceParser;
import synoptic.model.EventNode;

public class FSARandomWalkTraceProviderMock extends FSARandomWalkTraceProvider {

	public FSARandomWalkTraceProviderMock(FSAInputModel model, FSACoverageTraceGenerator covGen) throws InvalidModelException, SpecMiningAlgorithmException {
		modelName = model.getModelName();
		coverage = covGen;
		fsa = model.convertToFsa();
		covGen.setAutomaton(fsa);
		// load existing log
		File log = new File(covGen.getOutputFile());
		System.out.println("Loding pre-generated file: " + covGen.getOutputFile());
		logFileName = covGen.getOutputFile();
		if (!log.exists()) {
			String errorMsg = "Expected file " + covGen.getOutputFile() + " to be already generated";
			new InvalidModelException(errorMsg); 
		}
		String traceSeparator = "--";
		// use Synoptic to parse the log
		SynopticInputParams params = new SynopticInputParams(log.getName().substring(0, log.getName().length() - 4),traceSeparator, "(?<TYPE>.*)",null);
		SynopticMain synopticInstance = new SynopticInvocation(params).processSynopticArgs();
		try {

			TraceParser parser = new TraceParser(synopticInstance.options.regExps,
					synopticInstance.options.partitionRegExp, synopticInstance.options.separatorRegExp);
			List<EventNode> parsedEvents;

			parsedEvents = SynopticMain.parseEvents(parser, synopticInstance.options.logFilenames);

			fullTraceGraph = SynopticMain.genChainsTraceGraph(parser, parsedEvents);
			traceOrder = generateTraceOrder(fullTraceGraph.getNumTraces());
		} catch (Exception e) {
			throw new SpecMiningAlgorithmException(e);
		}

	}


}
