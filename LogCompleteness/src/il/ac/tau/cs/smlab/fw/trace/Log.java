package il.ac.tau.cs.smlab.fw.trace;

import java.util.LinkedList;
import java.util.List;

public class Log {

	private List<ExecutionTrace> traces = new LinkedList<ExecutionTrace>();
	
	// these two should be wrapped in a single class
	private String coverage;
	private int coverageParam;

	public List<ExecutionTrace> getTraces() {
		return traces;
	}

	public void setTraces(List<ExecutionTrace> traces) {
		this.traces = traces;
	}
	
	public void addTrace(ExecutionTrace t) {
		traces.add(t);
	}
	
	public int size() {
		return traces.size();
	}
	
	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
	
	public int getCoverageParam() {
		return coverageParam;
	}

	public void setCoverageParam(int coverageParam) {
		this.coverageParam = coverageParam;
	}

}
