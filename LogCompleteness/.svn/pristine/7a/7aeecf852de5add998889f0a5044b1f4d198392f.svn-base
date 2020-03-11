package il.ac.tau.cs.smlab.fw.models;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;
import il.ac.tau.cs.smlab.graphml.GraphMLToFSA;

public class ZellerFSAInputModel extends AbstractFSAInputModel {

	public ZellerFSAInputModel(String name) {
		super(name);
	}
	
	public ZellerFSAInputModel(String name,int visits) {
		super(name,visits);
	}

	@Override
	public FiniteStateAutomaton convertToFsa() throws InvalidModelException {
		if (fsa == null) {
			fsa = GraphMLToFSA.graphMLtoFSA(name);
		}
		return fsa;
	}

	@Override
	public void postprocessGeneratedLog(File log, String traceSeparator, String eventSeparator) throws IOException {

		String logstr = FileUtils.readFileToString(log);
		// separate traces with traceSeparator instead of \n
		logstr = logstr.replaceAll("\n", "\n" + traceSeparator + "\n");
		logstr = logstr.substring(0, logstr.length() - 1);
		// separate events with \n instead of ::
		logstr = logstr.replaceAll("::","\n");
		logstr = logstr.replace("\r\n" + traceSeparator, traceSeparator);
		FileUtils.writeStringToFile(log, logstr);

	}
	
	
	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		return FSACoverageTraceGeneratorFactory.getStateCoverage(filenamePrefix, visits);
	}

	@Override
	public FiniteStateAutomaton convertToMutatesFsa(int numOfStatesToAdd,
			int numOfModuls,  int numofregualr) throws InvalidModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FiniteStateAutomaton convertToMutatesFsa(int numOfStatesToAdd,
			int numOfModuls) throws InvalidModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FiniteStateAutomaton makeRecurstionMutation(int numOfStatesToAdd,
			int numOfModuls) throws InvalidModelException {
		// TODO Auto-generated method stub
		return null;
	}

}
