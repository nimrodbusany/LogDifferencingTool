package il.ac.tau.cs.smlab.fw.trace.generator;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInvocation;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticTraceProvider;
import il.ac.tau.cs.smlab.fsa.FSA;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import synoptic.main.SynopticMain;
import synoptic.main.parser.TraceParser;
import synoptic.model.EventNode;

public class FSARandomWalkTraceProvider extends SynopticTraceProvider {

	FiniteStateAutomaton fsa;
	FSACoverageTraceGenerator coverage;	
	String modelName;
	
	public FSARandomWalkTraceProvider(FSAInputModel model, FSACoverageTraceGenerator covGen) throws InvalidModelException, SpecMiningAlgorithmException {
		modelName = model.getModelName();
		coverage = covGen;
		// create log
		String traceSeparator = "--";
		
		fsa = model.convertToFsa();
		covGen.setAutomaton(fsa);

	//	System.out.println("Generating traces by " + coverage.getCoverageName() + " n = " + coverage.getNumOfVisits());
		File log = new File(covGen.generate());
		logFileName = covGen.getOutputFile();
		try {
			model.postprocessGeneratedLog(log, traceSeparator, "\n");
		} catch (IOException e) {
			throw new InvalidModelException(e);
		}
		
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
	
	public FSA getFsa() {
		return new FSA(modelName,fsa);
	}
	
	public FSACoverageTraceGenerator getCoverage() {
		return coverage;
	}
	
	protected FSARandomWalkTraceProvider() {}

}
