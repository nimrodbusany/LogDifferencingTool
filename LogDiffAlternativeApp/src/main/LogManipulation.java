package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logs.LinearLogUtil;
import logs.TraceManipulatpor;

import org.jgrapht.ext.ExportException;

import prefuse.data.io.DataIOException;
import synopticdiff.main.parser.ParseException;
import synopticdiff.util.InternalSynopticException;

public class LogManipulation  {

	int k = -1;
	
	public LogManipulation (int kParam){
		this.k = kParam;
	}
	
	
	private static void printKs(ArrayList<ArrayList<String>> traces, ArrayList<ArrayList<String>> ks ) {
		
		List<String> ks_as_strings = new ArrayList<String>();
		for (ArrayList<String> kSeq : ks){
			String k_seq_str = "";
			for (String event : kSeq){
				k_seq_str += event + ",";
			}
			k_seq_str = k_seq_str.substring(0, k_seq_str.length()-1);
			ks_as_strings.add(k_seq_str);
		}
		Collections.sort(ks_as_strings);
		for (String e : ks_as_strings){
			System.out.println(e);
		}
	}
	
	public static void main(String[] args)
			throws IOException, DataIOException, InternalSynopticException, ParseException, ExportException {

		LogManipulation l = new LogManipulation(2);
		LinearLogUtil logUtil = new LinearLogUtil();
		String logFile = "C:/LogDiffFolder/Columba/log1.log";
		ArrayList<ArrayList<String>> traces = LinearLogUtil.readTraces(logFile);
		System.out.println("total traces: " + traces.size());
		ArrayList<ArrayList<String>> ks = LinearLogUtil.extractKs(traces, l.k);
		printKs(traces, ks);
		System.out.println(" ********************************** ");
		TraceManipulatpor trace_man = new TraceManipulatpor(l.k);
		ArrayList<ArrayList<String>> m_traces = trace_man.flipManipulation(traces, 0, l.k);
		ks = LinearLogUtil.extractKs(m_traces, l.k);
		printKs(m_traces, ks);
	}
	
}
