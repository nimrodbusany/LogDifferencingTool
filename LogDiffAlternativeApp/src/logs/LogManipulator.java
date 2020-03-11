package logs;

import il.ac.tau.cs.smlab.fw.models.FSAInputModel;

import java.util.ArrayList;

public class LogManipulator {

	boolean verbose;
	int kParam;
	private ArrayList<ArrayList<String>> m_traces = null;
	private ArrayList<ArrayList<String>> traces = null;

	public LogManipulator(boolean verbose, int kParam) {
		this.verbose = verbose;
		this.kParam = kParam;
	}

	public ArrayList<ArrayList<String>> getTraces() {
		return traces;
	}

	public ArrayList<ArrayList<String>> getMutatedTraces() {
		return m_traces;
	}

	public void mutateLog(FSAInputModel inputModel, String path, int numOfTracesToAdd) {

		LinearLogUtil logUtil = new LinearLogUtil();
		TraceManipulatpor traceManipulator = new TraceManipulatpor(kParam);
		String logName = path + inputModel.getModelName() + ".log";
		String MutatedlogName = path + inputModel.getModelName() + "_Mutated.log";
		
		System.out.println("manupluating " + logName);
		
		ArrayList<ArrayList<String>> traces = LinearLogUtil.readTraces(logName);
		ArrayList<ArrayList<String>> m_traces = traceManipulator.flipManipulation(traces, numOfTracesToAdd, kParam);

		if (verbose) {
//			System.out.println("total traces: " + traces.size());
//			traceManipulator.printKs(LinearLogUtil.extractKs(traces));
//			System.out.println(" ************** ks after flip************** ");
//			traceManipulator.printKs(LinearLogUtil.extractKs(m_traces));
		}

		logUtil.writeTraces(m_traces, MutatedlogName);
		//logUtil.writeTraces(traces, logName);
		
		this.traces = traces;
		this.m_traces = m_traces;

	}
	
	public String mutateLogFromLog(FSAInputModel inputModel, String logName, int numOfTracesToAdd) 
	{
		LinearLogUtil logUtil = new LinearLogUtil();
		TraceManipulatpor traceManipulator = new TraceManipulatpor(kParam);
		String MutatedlogName = logName.replace(".log", Long.toString(System.nanoTime()) + ".log");
		
		System.out.println("manupluating " + logName);
		
		ArrayList<ArrayList<String>> traces = LinearLogUtil.readTraces(logName);
		ArrayList<ArrayList<String>> m_traces = traceManipulator.flipManipulation(traces, numOfTracesToAdd, kParam);

		if (verbose) {
//			System.out.println("total traces: " + traces.size());
//			traceManipulator.printKs(LinearLogUtil.extractKs(traces));
//			System.out.println(" ************** ks after flip************** ");
//			traceManipulator.printKs(LinearLogUtil.extractKs(m_traces));
		}

		logUtil.writeTraces(m_traces, MutatedlogName);
		//logUtil.writeTraces(traces, logName);
		
		this.traces = traces;
		this.m_traces = m_traces;
		
		return MutatedlogName;
	}


}
