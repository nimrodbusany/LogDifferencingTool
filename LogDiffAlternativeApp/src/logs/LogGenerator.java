package logs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

public class LogGenerator {

	private String path;
	private FSAInputModel inputModel;

	public LogGenerator(String path, FSAInputModel inputModel) {
		this.path = path;
		this.inputModel = inputModel;
	}

	public String generateLog() throws SpecMiningAlgorithmException, InvalidModelException, IOException {

		File baseDir = new File(path);
		baseDir.mkdir();

		try {
			FileUtils.cleanDirectory(baseDir);
		} catch (Exception e) {
			e.printStackTrace();
		}

		inputModel.convertToMutatesFsa(0, 1, 1); // Initialize model, no
													// mutation is done if
													// 0,1,1, is used
		String outputFile = generatedLog(inputModel); // CREATES LOGS FROM MODEL
		String newFileName = baseDir + "/" + inputModel.getModelName() + ".log";
		
		FileUtils.moveFile(new File(outputFile), new File(newFileName));
		
		return newFileName;

	}
	
	public String generateLogLocaly() throws SpecMiningAlgorithmException, InvalidModelException, IOException {

		File baseDir = new File(path);
		baseDir.mkdir();

		try {
			FileUtils.cleanDirectory(baseDir);
		} catch (Exception e) {
			e.printStackTrace();
		}

		inputModel.convertToMutatesFsa(0, 1, 1); // Initialize model, no
													// mutation is done if
													// 0,1,1, is used
		String outputFile = generatedLog(inputModel); // CREATES LOGS FROM MODEL
		
		return outputFile;
	}

	
	public void copyFile(String filePath, Double numOfCopies) throws SpecMiningAlgorithmException, InvalidModelException, IOException 
	{
		for (Integer i = 0; i < numOfCopies; i++)
		{
			FileUtils.copyFile(new File(filePath), new File(filePath.replace(".log", i.toString() + ".log")));
		}
	}
	
	public void cleanFolder(String base)
	{
		try {
			FileUtils.cleanDirectory(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<FSAInputModel> getMutatedModels(FSAInputModel model) {
		ArrayList<FSAInputModel> modelsList = new ArrayList<FSAInputModel>();

		try {
			model.convertToMutatesFsa(0, 1, 1);
		} catch (InvalidModelException e) {
			e.printStackTrace();
		}

		modelsList.addAll(model.getMutatedFSA());

		return modelsList;
	}

	private String generatedLog(FSAInputModel model)
			throws SpecMiningAlgorithmException, InvalidModelException {

		FSACoverageTraceGenerator c = model.getCoverage(model.getModelName());
		new FSARandomWalkTraceProvider(model, c);
		return c.getOutputFile();
	}

}
