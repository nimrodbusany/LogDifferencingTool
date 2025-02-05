package il.ac.tau.cs.smlab.fw.models;

import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fsa.xml.XMLtoFSA;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

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
			
//			mutatedModels.add(this);
//			
//			List<FiniteStateAutomaton> list = mutateAutomaton(fsa, 2);
//			int index = 1;
//			
//			for (FiniteStateAutomaton fsaSec : list)
//			{
//				DavidFSAInputModel davidSec = new DavidFSAInputModel(name + index++, visits);
//				davidSec.setFsa(fsaSec);
//				
//				mutatedModels.add(davidSec);
//			}
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
		
		String newLog = logstr;
		
		/*int counter = StringUtils.countMatches(newLog, traceSeparator);
		
		//for (int i = 0; i < 10; i++)
		while (counter < 1000 && newLog.length() < 7000)
		{
			newLog += "\n" + logstr;
			
			counter = StringUtils.countMatches(newLog, traceSeparator);
		}*/

		FileUtils.writeStringToFile(log, newLog);
	}

	@Override
	public FiniteStateAutomaton convertToMutatesFsa(int numOfStatesToAdd,
			int numOfModuls, int numOfRegular) throws InvalidModelException 
	{
		if (fsa == null) 
		{
			fsa = XMLtoFSA.convertXmlToAutomaton(name);
			
			for (int i = 0; i < numOfRegular; i++)
			{
				mutatedModels.add(this);
			}
				
			List<FiniteStateAutomaton> list = mutateAutomaton(fsa, numOfStatesToAdd, numOfModuls - numOfRegular);
						
			for (FiniteStateAutomaton fsaSec : list)
			{
				DavidFSAInputModel davidSec = new DavidFSAInputModel(name, visits);
				davidSec.setFsa(fsaSec);
				
				mutatedModels.add(davidSec);
			}
		}
		
		return fsa;
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
		if (fsa == null) 
		{
			fsa = XMLtoFSA.convertXmlToAutomaton(name);
			
			FiniteStateAutomaton newFsa = fsa;
			
			mutatedModels.add(this);
			
			for (int i = 1; i < numOfModuls; i++)
			{
				List<FiniteStateAutomaton> list = mutateAutomaton(newFsa, 1, 1);
				
				newFsa = list.get(0);
				
				DavidFSAInputModel davidSec = new DavidFSAInputModel(name, visits);
				davidSec.setFsa(newFsa);
				
				mutatedModels.add(davidSec);
			}
		}
		
		return fsa;
	}
	
	public static void main(String[] args) throws InvalidModelException {
		
		FiniteStateAutomaton fsa = XMLtoFSA.convertXmlToAutomaton("Columba");
		System.out.println("Initial states " + fsa.getInitialState());
		System.out.println("Final states " + fsa.getFinalStates().length);
		System.out.println("Final states " + fsa.getTransitions().length);
				
	}
		
}


