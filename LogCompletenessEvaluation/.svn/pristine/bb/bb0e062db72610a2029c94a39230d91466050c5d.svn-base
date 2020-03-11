package il.ac.tau.cs.smlab.fsa.validation;

import il.ac.tau.cs.smlab.algorithms.ktails.KTailsAlgorithm;
import il.ac.tau.cs.smlab.algorithms.ktails.KTailsInputParams;
import il.ac.tau.cs.smlab.algorithms.lsc.LSCAlgorithm;
import il.ac.tau.cs.smlab.algorithms.lsc.LSCInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticAlgorithm;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.evaluation.MainEvaluation;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResults;
import il.ac.tau.cs.smlab.fw.evaluation.results.EvaluationResultsExporter;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

public class MainValidation {
	
	@Test
	public void validationForSynoptic() throws SpecMiningAlgorithmException, InvalidModelException {
		// models
		List<FSAInputModel> models = ValidationModelsManager.getValidationModels();
		
		// run validation
		EvaluationResults results = (new MainEvaluation()).run(new SynopticAlgorithm(), models);
		EvaluationResultsExporter exporter = new EvaluationResultsExporter(results);
		exporter.exportToCSV("validation-Synoptic.csv");
		exporter.exportModelStats("validation-Synoptic-models" +".csv");
		exporter.printToScreen();
	}
	
	@Test
	public void validationForKTails() throws SpecMiningAlgorithmException, InvalidModelException {
		// algorithm
		int k = 2;
		SpecMiningAlgorithm algorithm = new KTailsAlgorithm(new KTailsInputParams(k));
		
		// models
		List<FSAInputModel> models = ValidationModelsManager.getValidationModels();
		
		// run validation
		EvaluationResults results = (new MainEvaluation()).run(algorithm, models);
		EvaluationResultsExporter exporter = new EvaluationResultsExporter(results);
		exporter.exportToCSV("validation-k-tails.csv");
		exporter.exportModelStats("validation-k-tails-models" +".csv");
		exporter.printToScreen();
	}
	
	
	@Test
	public void validationForLSC() throws FileNotFoundException, SpecMiningAlgorithmException, InvalidModelException {
		// algorithm
		SpecMiningAlgorithm algorithm = new LSCAlgorithm(
				new LSCInputParams(ValidationModelsManager.getTriggerCharts()));
		
		// models
		List<FSAInputModel> models = ValidationModelsManager.getValidationModels();
		
		// run validation
		EvaluationResults results = (new MainEvaluation()).run(algorithm, models);
		EvaluationResultsExporter exporter = new EvaluationResultsExporter(results);
		exporter.exportToCSV("validation-LSC.csv");
		exporter.exportModelStats("validation-LSC-models" + ".csv");
		exporter.printToScreen();
	}
}
