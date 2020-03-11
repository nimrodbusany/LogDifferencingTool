package il.ac.tau.cs.smlab.fsa.validation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.models.AbstractFSAInputModel;

public abstract class FSAValidationModel extends AbstractFSAInputModel {

	protected abstract FiniteStateAutomaton createFSA(int alphabet); 
	
	public FSAValidationModel(String name, int alphabet) {
		super(name);
		fsa = createFSA(alphabet);

	}

	@Override
	public FiniteStateAutomaton convertToFsa() throws InvalidModelException {
		return fsa;
	}

	@Override
	public void postprocessGeneratedLog(File log, String traceSeparator, String eventSeparator) throws IOException {

		String logstr = FileUtils.readFileToString(log);
		// separate traces with traceSeparator instead of \n
		logstr = logstr.replaceAll("\n", "\n" + traceSeparator + "\n");
		logstr = logstr.substring(0, logstr.length() - 1);
		// separate events with \n instead of ::
		logstr = logstr.replaceAll(":: ","\n");
		logstr = logstr.replaceAll(": ","\n");
		logstr = logstr.replaceAll("::","");
		logstr = logstr.replaceAll(":","");
		logstr = logstr.replace("\nTERMINAL", "");
		FileUtils.writeStringToFile(log, logstr);

	}
	

}
