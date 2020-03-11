package il.ac.tau.cs.smlab.algorithms.synoptic;

import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

public class SynopticInputParams {

	public SynopticInputParams(String logName, String separatorRegExp, String regExp, String partitionExp) {
		this(logName,separatorRegExp,regExp,partitionExp, SystemConstants.DOT_LOCATION);
	}
	
	public SynopticInputParams(String logName, String separatorRegExp, String regExp, String partitionExp, String dotLocation) {
		this.logName = logName;
		this.separatorRegExp = separatorRegExp;
		this.regExp = regExp;
		this.partitionExp = partitionExp;
		this.dotLocation = dotLocation;
	}

	public String logName;
	public String separatorRegExp;
	public String regExp;
	public String partitionExp;
	public String dotLocation;

	public boolean dumpInitialPartitionGraph = false;
	public boolean outputInvariantsToFile = false;
	public boolean dumpTraceGraphPngFile = false;
	public boolean onlyMineInvariants = true;
	public String outputPathPrefix = System.getProperty("user.dir") + "/resources/output/";
}
