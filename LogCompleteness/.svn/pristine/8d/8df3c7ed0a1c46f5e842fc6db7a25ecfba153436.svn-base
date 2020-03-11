package il.ac.tau.cs.smlab.fw.models;

import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fsa.xml.XMLtoFSA;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class DavidFSAInputModel extends AbstractFSAInputModel {

	public DavidFSAInputModel(String name) {
		super(name);
	}

	public DavidFSAInputModel(String name,int visits) {
		super(name,visits);
	}

	@Override
	public FiniteStateAutomaton convertToFsa() throws InvalidModelException {
		if (fsa == null) {
			fsa = XMLtoFSA.convertXmlToAutomaton(name);
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
		logstr = logstr.replace(":: ", "\n");
		// remove TERMINAL events
		logstr = logstr.replace("\nTERMINAL::", "");

		FileUtils.writeStringToFile(log, logstr);

	}


}
