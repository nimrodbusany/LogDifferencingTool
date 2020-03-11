package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jgrapht.ext.ExportException;

import il.ac.tau.cs.smlab.diff.util.CSVUtil;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.evaluation.EvaluationModelsManager;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;
import il.ac.tau.cs.smlab.fw.utils.SystemConstants;
import logs.LogGenerator;
import logs.LogManipulator;
import prefuse.data.io.DataIOException;
import synopticdiff.main.parser.ParseException;

public class ExperimentsManager {

	String modelsFolder = "";
	String logsFolder = "";
	String diffFolder = "";
	String summaryResultsFolder = "";
	int kParam = 2;
	final static boolean kSeqApproach = true;

	boolean verbose = true;

	public ExperimentsManager() {
	}

	private void runExperiment() throws SpecMiningAlgorithmException, InvalidModelException, IOException, ParseException, DataIOException, ExportException {

		List<FSAInputModel> models = EvaluationModelsManager.getAllEvaluationModels();

		HashMap<String, LogDiffAlt> times = new HashMap<String, LogDiffAlt>();
		HashMap<String, LogManipulator> logs = new HashMap<String, LogManipulator>();

		for (FSAInputModel inputModel : models) 
		{
			System.out.println("generating model for: " + inputModel.getModelName());
			// FOR EACH MODEL:

			// GENERATE A LOG
			String path = SystemConstants.LOG_DIRECTORY + File.separator + inputModel.getModelName() + "/";
			LogGenerator logGen = new LogGenerator(path, inputModel);
			logGen.generateLog();

			// MUTATE LOG
			LogManipulator log_man = new LogManipulator(verbose,kParam);
			log_man.mutateLog(inputModel, path, 1);

			// RUN BOTH METHODS
			
			LogDiffAlt l = new LogDiffAlt(kParam, kSeqApproach);
			l.run(path);
			times.put(inputModel.getModelName(), l);
			logs.put(inputModel.getModelName(), log_man);
			
		}
		
		output_experiment_summary(times, logs);
		
	}

	private void output_experiment_summary(HashMap<String, LogDiffAlt> times, HashMap<String, LogManipulator> logs) throws IOException {
		FileWriter fw = new FileWriter(new File(SystemConstants.EXPERIMENT_SUMMARY_OUTPUT_DIRECTORY + "times.csv"));
		fw.write("model, kTails, KTdiff \n");
		for (String model : times.keySet()){
			fw.write(model + "," + times.get(model).getTimeKTails() + 
					"," + times.get(model).getTimeKTDiff() +
					"," + logs.get(model).getTraces().size() + "," + 
					logs.get(model).getMutatedTraces().size() +"\n");
		}
		fw.close();
	}

	public void modelEvaluation(FSAInputModel inputModel)
	{
		List<Integer> numOfTraceToAdd = new ArrayList<Integer>(Arrays.asList(10));
	
		try
		{
			for (Integer addedTraces : numOfTraceToAdd)
			{
				System.out.println("generating model for: " + inputModel.getModelName() + "  adding traces: " + addedTraces);
				
				// GENERATE A LOG
				String path = SystemConstants.LOG_DIRECTORY + File.separator + inputModel.getModelName() + "/";
				LogGenerator logGen = new LogGenerator(path, inputModel);
				logGen.generateLog();
		
				// MUTATE LOG
				LogManipulator log_man = new LogManipulator(verbose, kParam);
				log_man.mutateLog(inputModel, path, addedTraces);
				
				// RUN BOTH METHODS
				LogDiffAlt l = new LogDiffAlt(kParam,kSeqApproach);
				l.run(path);
				
				//times.put(inputModel.getModelName(), l);
				//logs.put(inputModel.getModelName(), log_man);
				
				FileWriter writer = new FileWriter(SystemConstants.LOG_DIRECTORY + "\\result.cvs", true);
				
				CSVUtil.writeLine(writer, Arrays.asList(inputModel.getModelName(), l.ktailsTimer.getTime().toString(), l.ktdiffTimer.getTime().toString(), 
						l.ktailsTimer.averageTraceSize.toString(), l.ktailsTimer.averageLogSize.toString(), addedTraces.toString()));
				
				writer.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void modelKtdiffEvaluation(FSAInputModel inputModel)
	{
		List<Integer> mutationType = new ArrayList<Integer>(Arrays.asList(0, 1, 2));
		
		List<Double> numOfLogsOptions = new ArrayList<Double>(Arrays.asList(2.0, 4.0, 6.0, 8.0));
	
		try
		{
			//for (Double numOfLogs : numOfLogsOptions)
			// GENERATE A LOG
			String path = SystemConstants.LOG_DIRECTORY + File.separator + inputModel.getModelName() + "/";
			LogGenerator logGen = new LogGenerator(path, inputModel);
			
			String filePath = logGen.generateLogLocaly();
			
			for (Integer type : mutationType)
			{
				//for (Integer type : mutationType)
				for (Double numOfLogs : numOfLogsOptions)
				{
					System.out.println("generating model for: " + inputModel.getModelName() + "  type: " + type + 
							" num of logs:" + numOfLogs);
					
					logGen.cleanFolder(path);
					
					String newFileName = path + "/" + inputModel.getModelName() + ".log";
					
					FileUtils.copyFile(new File(filePath), new File(newFileName));
					
					
					if (type == 2)
					{
						LogManipulator log_man = new LogManipulator(verbose, kParam);	
						String newPath = newFileName;
						
						for (int i = 0; i < numOfLogs - 1; i++)
						{							
							
							newPath = log_man.mutateLogFromLog(inputModel, newPath, 1);
						}
					}
					else
					{
						logGen.copyFile(newFileName, numOfLogs - 2);
				
						// MUTATE LOG
						LogManipulator log_man = new LogManipulator(verbose, kParam);
						log_man.mutateLog(inputModel, path, type);
					}
						
					// RUN BOTH METHODS
					LogDiffAlt l = new LogDiffAlt(kParam, kSeqApproach);
					l.runKtDiffOnly(path);
					
					//times.put(inputModel.getModelName(), l);
					//logs.put(inputModel.getModelName(), log_man);
					
					FileWriter writer = new FileWriter(SystemConstants.LOG_DIRECTORY + "\\resultktdiffonly.cvs", true);
					
					Double averageTracePerLog = l.ktdiffRunner.numOfTraces  / numOfLogs;
					Double averageTraceSize = l.ktdiffRunner.numLetters / l.ktdiffRunner.numOfTraces;
					
					String policy = new String();
					
					if (type == 0)
					{
						policy = "NoMutation";
					}
					else if (type == 1)
					{
						policy = "SingleMutation";
					}
					else
					{
						policy = "RecursiceMutation";
					}
					
					CSVUtil.writeLine(writer, Arrays.asList(inputModel.getModelName(), l.ktdiffTimer.getTime().toString(), averageTracePerLog.toString(),
							averageTraceSize.toString(), numOfLogs.toString(), policy)); 
									
					writer.close();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void runEvaluation(String modelName)
	{
		try
		{
			List<FSAInputModel> models = EvaluationModelsManager.getAllEvaluationModels();
	
			for (int i = 0; i < 7; i++)
			{
				for (FSAInputModel inputModel : models) 
				{
					//if (inputModel.getModelName().equals(modelName))
					{
						//modelEvaluation(inputModel);
						modelKtdiffEvaluation(inputModel);
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Error in model evaluation " + e.getStackTrace().toString());
		}
	}

	public static void main(String[] args) throws SpecMiningAlgorithmException, InvalidModelException, IOException, ParseException, DataIOException, ExportException {

		ExperimentsManager e = new ExperimentsManager();
		//e.runExperiment();
		e.runEvaluation("Columba");
	}
}
