package il.ac.tau.cs.smlab.fw.trace.generator;

import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fsa.xml.XMLtoFSA;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FSARandomWalkLogProvider {


	String logstr;

	public FSARandomWalkLogProvider(String model, FSACoverageTraceGenerator covGen) throws InvalidModelException {
		// create log
		String traceSeparator = "--";

		FiniteStateAutomaton fsa = XMLtoFSA.convertXmlToAutomaton(model);
		covGen.setAutomaton(fsa);
		File log = new File(covGen.generate());
		processLog(log, traceSeparator, "\n");

	}
	
	public String getLogString() {
		return logstr;
	}
	

	private void processLog(File log, String traceSeparator, String eventSeparator) throws InvalidModelException {

		try {
			logstr = FileUtils.readFileToString(log);

			// separate traces with traceSeparator instead of \n
			logstr = logstr.replaceAll("\n", "\n" + traceSeparator + "\n");
			logstr = logstr.substring(0, logstr.length() - 1);

			// separate events with \n instead of ::
			logstr = logstr.replace(":: ", "\n");
			// remove TERMINAL events
			logstr = logstr.replace("\nTERMINAL::", "");

			FileUtils.writeStringToFile(log, logstr);

		} catch (IOException e) {
			throw new InvalidModelException(e);
		}
	}
}


