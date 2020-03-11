package il.ac.tau.cs.smlab.algorithms.synoptic;

import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.util.LinkedList;
import java.util.List;

import synoptic.main.SynopticMain;

public class SynopticInvocation {

	private SynopticInputParams params;

	public SynopticInvocation(SynopticInputParams params) {
		this.params = params;
	}

	private List<String> generateSynopticArgsList() {
		List<String> synopticArgsList = new LinkedList<String>();
		if (params.separatorRegExp != null) {
			synopticArgsList.add("--separatorRegExp="+params.separatorRegExp);
		}
		if (params.partitionExp != null) {
			synopticArgsList.add("--partitionRegExp="+params.partitionExp);
		}
		synopticArgsList.add("--regExps="+params.regExp);
		synopticArgsList.add("--outputPathPrefix="+params.outputPathPrefix+params.logName);
		synopticArgsList.add("--dotExecutablePath="+params.dotLocation);
		synopticArgsList.add("--outputInvariantsToFile="+params.outputInvariantsToFile);
		synopticArgsList.add("--dumpTraceGraphPngFile="+params.dumpTraceGraphPngFile);
		synopticArgsList.add("--dumpInitialPartitionGraph=" + params.dumpInitialPartitionGraph);
		synopticArgsList.add("--onlyMineInvariants=" + params.onlyMineInvariants);

		synopticArgsList.add(SystemConstants.LOG_DIRECTORY+params.logName+SystemConstants.LOG_FILE_EXTENSION);
		return synopticArgsList;
	}

	public SynopticMain runSynoptic() throws SpecMiningAlgorithmException {

		String[] synopticArgsArray = generateArgs();

		// run Synoptic
		try {
			SynopticMain.main(synopticArgsArray);
		} catch (Exception e) {
			throw new SpecMiningAlgorithmException(e);
		}
		return SynopticMain.getInstanceWithExistenceCheck();
	}

	public SynopticMain processSynopticArgs() throws SpecMiningAlgorithmException {

		String[] synopticArgsArray = generateArgs();

		SynopticMain mainInstance = SynopticMain.getInstance();
		if (mainInstance == null) {
			// process Synoptic arguments
			try {
				mainInstance = SynopticMain.processArgs(synopticArgsArray);
			} catch (Exception e) {
				throw new SpecMiningAlgorithmException(e);
			}
		}
		else {
			mainInstance.options.logFilenames.set(0,
					SystemConstants.LOG_DIRECTORY+params.logName+SystemConstants.LOG_FILE_EXTENSION);
			mainInstance.options.onlyMineInvariants = params.onlyMineInvariants;
			mainInstance.options.outputPathPrefix = params.outputPathPrefix+params.logName ;
			mainInstance.options.outputInvariantsToFile = params.outputInvariantsToFile;
		}
		return mainInstance;
	}


	private String[] generateArgs() {
		List<String> synopticArgsList = generateSynopticArgsList();

		String[] synopticArgsArray = new String[synopticArgsList.size()];
		synopticArgsArray = synopticArgsList.toArray(synopticArgsArray);
		return synopticArgsArray;
	}
	
	public void updateLogName(String newLogFilename) {
		this.params.logName = newLogFilename;
	}
	
	
	public static SynopticMain getInstance() throws SpecMiningAlgorithmException {
		SynopticMain mainInstance = SynopticMain.getInstance();
		if (mainInstance == null) {
				throw new SpecMiningAlgorithmException("Synoptic was not initialized");
			}
		return mainInstance;
	}
	
}
