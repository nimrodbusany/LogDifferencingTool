package il.ac.tau.cs.smlab.fw.evaluation.results;

import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import au.com.bytecode.opencsv.CSVWriter;

public class EvaluationResultsExporter {

	private EvaluationResults results;
	private boolean showIntermediateConfidence = true;

	public EvaluationResultsExporter(EvaluationResults results) {
		this.results = results;
	}


	public void exportToFile(String filename) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(SystemConstants.RESOURCES_DIRECTORY + "evaluation/" + filename);
		writer.write(results.toString(showIntermediateConfidence));
		writer.close();
	}

	public void printToScreen() {
		System.out.println(results.toString(showIntermediateConfidence));
	}

	public void setShowIntermediateConfidence(boolean showIntermediateConfidence) {
		this.showIntermediateConfidence = showIntermediateConfidence;
	}

	public void exportToCSV(String filename) {
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(SystemConstants.RESOURCES_DIRECTORY + "evaluation/" + filename),',', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeAll(results.getResultEntries());
			writer.close();
		} catch (IOException e) {}
	}
	
	public void exportModelStats(String filename) {
		
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(SystemConstants.RESOURCES_DIRECTORY + "evaluation/" + filename),',', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeAll(results.getModelsEntries());
			writer.close();
		} catch (IOException e) {}
	}
	
	public void exportConfidenceBreakdown(String filename) {
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(SystemConstants.RESOURCES_DIRECTORY + "evaluation/" + filename),',', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeAll(results.getBreakdownEntries());
			writer.close();
		} catch (IOException e) {}
	}

}
