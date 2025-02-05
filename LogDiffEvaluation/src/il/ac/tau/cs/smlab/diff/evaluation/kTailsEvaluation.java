package il.ac.tau.cs.smlab.diff.evaluation;
import idot.util.DotFileReader;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticExecutionTrace;
import il.ac.tau.cs.smlab.diff.util.CSVUtil;
import il.ac.tau.cs.smlab.diff.util.Utils;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.evaluation.EvaluationModelsManager;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;
import il.ac.tau.cs.smlab.fw.trace.Log;
import il.ac.tau.cs.smlab.fw.trace.TraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.FSARandomWalkTraceProvider;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
//import org.eclipse.jdt.internal.compiler.problem.ShouldNotImplement;
import org.junit.Test;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Id;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;









import com.sun.org.apache.xerces.internal.impl.dv.dtd.NMTOKENDatatypeValidator;

import sun.text.normalizer.CharTrie.FriendAgent;
//import prefuse.data.Graph;
import synopticdiff.algorithms.KTails;
import synopticdiff.main.parser.TraceParser;
import synopticdiff.model.ChainsTraceGraph;
import synopticdiff.model.PartitionGraph;
import synopticdiff.model.export.GraphExporter;
import synopticdiff.tests.SynopticTest;

public class kTailsEvaluation 
{
	public class ResultObject
	{
		String modelName;
		String kparam;
		String numOfMutations;
		String numberOfLogs;
		String averageTracesPerLog;
		String averageTraceSize;
		String regularExecutionTime;
		String diffExecutionTime;
		String relationExecutionTime;
		String numOfOrig;
	}
	
	static String baseDir;
	static final String REG_EX = "^(?<TYPE>)$";
	static final String SEP = "^--$";
	
	static Map<String, String> fileToReqex = new HashMap<String, String>();
	static Map<String, String> fileToPartition = new HashMap<String, String>();
	
	// number of trials
	int trials = 3;
	// confidence to reach in generated logs
	double minConfidence = 0.95; 
	// maximum number of traces in a log
	int maxNumOfTraces = 100; 
	// minimum number of traces in a log
	int initialNumOfTraces = 10;
	// minimum length of a trace
	int minTraceLength = 0;
	
	static boolean shouldExport = false;
	
	public static void init()
	{
		fileToReqex.put("read", REG_EX);
		fileToPartition.put("read", SEP);
		
		fileToReqex.put("ZipOutputStream", REG_EX);
		fileToPartition.put("ZipOutputStream", SEP);
		
		fileToReqex.put("shop", "(?<ip>) .+ GET HTTP/1.1 /(?<TYPE>.+).php");
		fileToPartition.put("ZipOutputStream", "\\k<ip>");
	}
	
	public static void deleteIrrelevatFiles()
	{
		File[] filesList = new File(baseDir).listFiles();
		
		for (File file: filesList)
		{
			if (!file.getName().endsWith("txt"))
			{
				file.delete();
			}
		}
	}
	
	public static ArrayList<GraphImpl> testRegularKtails(int kParam, ResultObject resultObjet)
	{
		double miliSecondsOverall = 0;
		
		ArrayList<GraphImpl> graphList = new ArrayList<GraphImpl>();
		
		int logIndex = 1;
		
		File[] filesList = new File(baseDir).listFiles();
		
		double averageTraceSize = 0;
		double numOfTraces = 0;
		double numOfLogs = 0;
		
		for (File file: filesList)
		{
			try
			{
				if  (!file.toString().endsWith("txt"))	
				{
					continue;
				}
				
				byte[] bytes = Files.readAllBytes(file.toPath());
				String log = new String(bytes);
				
				String partition = getPartition(filesList[0].getName());
				
				String regExp = getReqExp(filesList[0].getName());
				
				List<List<String>> firstTracesList = null;
								
				firstTracesList = Utils.readFromFiles(log, "--");
				
				numOfTraces += firstTracesList.size();
				
				for (List<String> trace : firstTracesList)
				{
					averageTraceSize += trace.size();
				}
				
				String logLines[] = log.split("\\r?\\n");
				
				TraceParser defParser = null; 
				
				if (partition == null ||
					regExp == null)
				{
					defParser = SynopticTest.genDefParser();
				}
				 else
				{
					defParser = new TraceParser();
					
					try
					{
						defParser.addRegex(regExp);
											
						defParser.addPartitionsSeparator(partition);
					} 
					catch (Exception e)
					{
						defParser = SynopticTest.genDefParser();
					}
				}
								
				numOfLogs++;
				
				ChainsTraceGraph ret = (ChainsTraceGraph) SynopticTest.genChainsTraceGraph(logLines,
						defParser); 
				
				double miliSecondsStart = System.currentTimeMillis();
				
				PartitionGraph result = null;
				
				result = KTails.performKTails(ret, kParam);
								
 				double miliSecondsEnd = System.currentTimeMillis();
 				
 				miliSecondsOverall = miliSecondsOverall + ((miliSecondsEnd - miliSecondsStart));
 				
 				List<Integer> tracesList = new ArrayList<Integer>();
 				
 				tracesList.add(firstTracesList.size());
 				
 				File resultDir = new File(baseDir + File.separator + "output");
 				
 				resultDir.mkdir();
 								
 				String resultFile = resultDir.getAbsolutePath() + File.separator  
 					+ file.getName().substring(0, file.getName().lastIndexOf('.')) + "_" + 
 						Integer.toString(kParam); 
 				 				
				GraphExporter.exportGraph(resultFile, result, true, tracesList);
				
				if (shouldExport)
				{
					GraphExporter.generatePngFileFromDotFile(resultFile);
				}
		        
				 try 
				 {
					FileReader in = null;
				        
					File f = new File(resultFile);
			        
					in = new FileReader(f);
			        Parser p = new Parser();
			        p.parse(in);
			        
			        ArrayList<Graph> graph = p.getGraphs();
			        
			        graphList.add(new GraphImpl(graph.get(0), logIndex++, resultFile));
				 }
				 catch (Exception e) 
				 {
					 e.printStackTrace();
				 }
			}
			catch(Exception e)
			{
				System.out.println("Error reading " + file);
				e.printStackTrace();
			}
		}		
		
		if (numOfLogs == 0 || numOfTraces == 0)
		{
			try {
				System.out.println(numOfLogs + " " + numOfTraces);
				Thread.sleep(1000000000);
				Thread.sleep(1000000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (resultObjet != null)
		{
			resultObjet.averageTracesPerLog = Double.toString(numOfTraces/numOfLogs);
			resultObjet.averageTraceSize = Double.toString(averageTraceSize/ numOfTraces);
			resultObjet.regularExecutionTime = Double.toString(miliSecondsOverall);
		}
		
		System.out.println("Regular\tkTails\tk\t= " + kParam + "\tTime\t-\t" + (miliSecondsOverall));
		
		return graphList;
	}
	
	private static String getPartition(String fileName)
	{
		for (String localPartition : fileToPartition.keySet())
		{
			if (fileName.contains(localPartition))
			{
				return fileToPartition.get(localPartition);
			}
		}
		
		return SEP;
	}
	
	private static String getReqExp(String fileName)
	{
		for (String localPartition : fileToReqex.keySet())
		{
			if (fileName.contains(localPartition))
			{
				return fileToReqex.get(localPartition);
			}
		}
		
		return REG_EX;
	}
	
	public static Graph testDiffKtails(int kParam, ResultObject resultObject)
	{
		List<Integer> integerTraces = new ArrayList<Integer>();
		List<String> logLines = new ArrayList<String>();
		
		File resultDir = new File(baseDir + File.separator + "output");
		
		resultDir.mkdir();
						
		String resultFile = resultDir.getAbsolutePath() + File.separator + File.separator + "result_file_" + Integer.toString(kParam); 
		
		File resultFileHandle = new File(resultFile);
		
		resultFileHandle.delete();
		
		(new File(resultFile + ".png")).delete();
		
		File[] filesList = new File(baseDir).listFiles();
		
		Arrays.sort(filesList);
		
		String partition = getPartition(filesList[0].getName());
		
		String regExp = getReqExp(filesList[0].getName());
								
		for (File file: filesList)
		{
			try
			{
				if  (!file.toString().endsWith("txt"))	
				{
					continue;
				}
				
				byte[] bytes = Files.readAllBytes(file.toPath());
				String log = new String(bytes);
				
				List<List<String>> tracesList = null;
				
				if (partition != null)
				{
					tracesList = Utils.readFromFiles(log, "--");
				}
				
				integerTraces.add(tracesList.size());
				
				String localLogLines[] = log.split("\\r?\\n");
				
				logLines.addAll(Arrays.asList(localLogLines));
				
				logLines.add("--");
			}
			catch(Exception e)
			{
				System.out.println("Error reading " + file);
				e.printStackTrace();
			}
		}
		
		logLines.remove(logLines.size()-1);
		
		TraceParser defParser = null; 
		
		if (partition == null ||
			regExp == null)
		{
			defParser = SynopticTest.genDefParser();
		}
		 else
		{
			defParser = new TraceParser();
			
			try
			{
				defParser.addRegex(regExp);
									
				defParser.addPartitionsSeparator(partition);
			} 
			catch (Exception e)
			{
				defParser = SynopticTest.genDefParser();
			}
		}
		
		
		String[] logLinesArray = new String[logLines.size()];
		logLines.toArray(logLinesArray);
		
		try
		{
			
			
			ChainsTraceGraph ret = (ChainsTraceGraph) SynopticTest.genChainsTraceGraph(logLinesArray,
					defParser);
			
			double miliSecondsStart =  System.currentTimeMillis();
			
			PartitionGraph result = null;
			
			result = KTails.performKTails(ret, kParam);
						
			double miliSecondsEnd = System.currentTimeMillis();
			
			double miliSecondsOverall = (miliSecondsEnd - miliSecondsStart);
			
			resultObject.diffExecutionTime = Double.toString(miliSecondsOverall);
			
			System.out.println("Diff\tkTails\tk\t= " + kParam + "\tTime\t-\t" + (miliSecondsOverall));
			
			GraphExporter.exportGraph(resultFile, result, true, integerTraces);
									
			if (shouldExport)
			{
				GraphExporter.generatePngFileFromDotFile(resultFile);
			}
							
			FileReader in=null;
	        
			File f = new File(resultFile);
			
			try 
			{
		        in = new FileReader(f);
		        Parser p = new Parser();
		        p.parse(in);
		        
		        ArrayList<Graph> graph = p.getGraphs();
		        
		        return graph.get(0);
			 }
			 catch (Exception e) 
			 {
		            // do something if the file couldn't be found
			 }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void testLogs(List<String> logsPaht)
	{
		System.out.println("Staring evalution for n logs");
	}
	
	public void makeTest(int k, ResultObject resultObject)
	{
		List<GraphImpl> graphs = kTailsEvaluation.testRegularKtails(k, resultObject);
		
		Graph graph = kTailsEvaluation.testDiffKtails(k, resultObject);

		
		//validation code - comment out from preformance reason
		//System.out.println("Starting validation:");
		
//		for (GraphImpl impl : graphs)
//		{
//			Graph result = createResultClone(graph,  Integer.toString(impl.logIndex));
//			
//			System.out.println("Validation log: " + impl.logIndex + " file: " + impl.file );
//	
//			if (!compareGraphs(result, impl.currentGraph))
//			{
//				System.err.println("Error in validate graph:" + impl.logIndex); 
//			}
//			else
//			{
//				System.out.println("Log: " + impl.logIndex + " is validated" );
//			}
//		}
	}
	
	
	public void runKtails(String modelName)
	{
		kTailsEvaluation.baseDir = "resources" + File.separator + "kTails" + File.separator + modelName;
		
		init();
		
		System.out.println("Testing ktails for k = 1, file:" + modelName);
		
		makeTest(1, null);
		
		System.out.println("Testing ktails for k = 2, file:" + modelName);
		
		makeTest(2, null);
	}
	
	@Test
	public void testKtails()
	{
		// models
		List<FSAInputModel> models = EvaluationModelsManager.getAllEvaluationModels();
		
		for (FSAInputModel inputModel : models)
		{
			kTailsEvaluation.baseDir = "resources" + File.separator + "kTails" + File.separator + inputModel.getModelName();
			
			System.out.println("Testing ktails for  model:" + inputModel.getModelName());		
			
			File baseDir = new File(kTailsEvaluation.baseDir);
			
			baseDir.mkdir();
			
			try
			{
				FileUtils.cleanDirectory(baseDir);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			testModelWithMutation(inputModel, baseDir, 1, 2);
			
			runKtails(inputModel.getModelName());
		}
		
	}
	
	public void testModelWithMutation(FSAInputModel model, File baseDir, int numOfStates, int numOfModules)
	{
		File logsFolder = new File(SystemConstants.LOG_DIRECTORY);
		
		boolean exceptionThrown = false;
		
		do 
		{
			exceptionThrown = false;
			
			try
			{
				FileUtils.cleanDirectory(logsFolder);
			}
			catch (Exception e)
			{
				exceptionThrown = true;
				
				try 
				{
					Thread.sleep(1000);
				} 
				catch (InterruptedException e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		while (exceptionThrown);
			
		for (FSAInputModel mutatedModel : getMutatedModels(model, numOfStates, numOfModules, 1, false))
		{
			try 
			{
				writeModelLogs(mutatedModel);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		try 
		{
			// Create a filter for ".txt" files
			IOFileFilter txtSuffixFilter = FileFilterUtils.suffixFileFilter(".txt");
			
			FileFilter filter = FileFilterUtils.and(txtSuffixFilter);
			
			FileUtils.copyDirectory(logsFolder, baseDir, filter);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	public List<FSAInputModel> getMutatedModels(FSAInputModel model, int numOfStates, int numOfModules, int numOfRegualr, boolean isRecursive)
	{
		ArrayList<FSAInputModel> modelsList = new ArrayList<FSAInputModel>();
		
		try 
		{
			if (!isRecursive)
			{
				model.convertToMutatesFsa(numOfStates, numOfModules, numOfRegualr);
			}
			else
			{
				model.makeRecurstionMutation(numOfStates,numOfModules);
			}
		} 
		catch (InvalidModelException e) 
		{
			e.printStackTrace();
		}
		
		modelsList.addAll(model.getMutatedFSA());
		
		return modelsList; 
	}
	
	private List<TraceProvider> getTraceProviders(FSAInputModel model) throws SpecMiningAlgorithmException, InvalidModelException {
		List<TraceProvider> traceProviders = new ArrayList<TraceProvider>(1);
		FSACoverageTraceGenerator c = model.getCoverage(model.getModelName());
		traceProviders.add(new FSARandomWalkTraceProvider(model,c));
		return traceProviders;
	}
	
	public void writeModelLogs(FSAInputModel model) throws Exception
	{
		List<TraceProvider> allTraceProviders = getTraceProviders(model);
		
//		for (TraceProvider traceProvider : allTraceProviders) 
//		{
//			double logConfidence = 0.0D;
//			Log log = new Log();
//			log.setCoverage(((FSARandomWalkTraceProvider) traceProvider).getCoverage().getCoverageName());
//			log.setCoverageParam(((FSARandomWalkTraceProvider) traceProvider).getCoverage().getNumOfVisits());
//			
//			//BufferedWriter writer = new BufferedWriter(new FileWriter(modelFile));
//			
//			while (true) 
//			{
//				if (log.size() >= initialNumOfTraces && logConfidence > minConfidence) 
//				{
//					break;
//				}
//				
//				if (log.size() > maxNumOfTraces) 
//				{
//					break;
//				}
//				
//				if (!traceProvider.hasNext()) 
//				{ // not enough traces to reach confidence
//					break;
//				}
//				
//				SynopticExecutionTrace t = (SynopticExecutionTrace) traceProvider.next();
//				
//				if (t.size() <= minTraceLength) continue;
//				
//				//writer.append(t.toString());
//				
//				log.addTrace(t);
//			}
//		}
	}
		
	public static void main(String[] args)
	{
		if (args[0].equals("CreateModel"))
		{
			String modelName = args[1];
			int numOfStates = Integer.parseInt(args[2]);
			int numOfModules = Integer.parseInt(args[3]);
			
			int numOfOrig = 1;
			
			if (args.length > 3)
			{
				numOfOrig = Integer.parseInt(args[4]);
			}
			
			kTailsEvaluation ktailsEvaluation = new kTailsEvaluation();
			
			ktailsEvaluation.writeLogs(modelName, numOfStates, numOfModules, numOfOrig, false);
		}
		else if (args[0].equals("CreateModelRecursive"))
		{
			String modelName = args[1];
			int numOfStates = Integer.parseInt(args[2]);
			int numOfModules = Integer.parseInt(args[3]);
			
			int numOfOrig = 1;
			
			if (args.length > 3)
			{
				numOfOrig = Integer.parseInt(args[4]);
			}
			
			kTailsEvaluation ktailsEvaluation = new kTailsEvaluation();
			
			ktailsEvaluation.writeLogs(modelName, numOfStates, numOfModules, numOfOrig, true);
		}
		else if (args[0].equals("Testktails"))
		{
			String modelName = args[1];
			String numberOfStates = args[2];
			String numOfModules = args[3];
			String k = args[4];
			int numOfOrig = Integer.parseInt(args[5]);
			String isRecursive = "false"; 
			
			kTailsEvaluation.shouldExport = false;
			
			if (args.length > 6)
			{
				isRecursive = args[6]; 
			}
			
			if (args.length > 7)
			{
				 
			}
			
			kTailsEvaluation.shouldExport = true;
			
			kTailsEvaluation.baseDir = "resources" + File.separator + "kTails" + File.separator + modelName;
			
			System.out.println("Testing ktails for  model:" + modelName);		
			
			File baseDir = new File(kTailsEvaluation.baseDir);
			
			baseDir.mkdir();
					
			try 
			{
				FileUtils.cleanDirectory(baseDir);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			// Create a filter for ".txt" files
			IOFileFilter txtSuffixFilter = FileFilterUtils.suffixFileFilter(".txt");
			
			FileFilter filter = FileFilterUtils.and(txtSuffixFilter);
			
			File logsFolder = new File(SystemConstants.LOG_DIRECTORY);
			
			kTailsEvaluation ktailsEvaluation = new kTailsEvaluation();
			
			if (logsFolder.listFiles().length == 0)
			{
				ktailsEvaluation.writeLogs(modelName, Integer.parseInt(numberOfStates), Integer.parseInt(numOfModules), numOfOrig, false);
			}
			
			try 
			{
				FileUtils.copyDirectory(logsFolder, baseDir, filter);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}

			kTailsEvaluation.init();
			
			ResultObject resultObject = ktailsEvaluation.new ResultObject();
			
			resultObject.numOfOrig = Integer.toString(numOfOrig);
			
			ktailsEvaluation.makeTest(Integer.parseInt(k), resultObject);
			
			double regularTime = Double.parseDouble(resultObject.regularExecutionTime);
			
			if (regularTime == 0)
			{
				regularTime++;
				
			}
			double relation = (Double.parseDouble(resultObject.diffExecutionTime)) / (regularTime); 
			
			resultObject.relationExecutionTime = Double.toString(relation);
			
			String csvFile = "nexExperiment.csv";
			
			System.out.println("writing to " + csvFile);
	        FileWriter writer;
			
	        try 
			{
				writer = new FileWriter(csvFile, true);
				
				CSVUtil.writeLine(writer, Arrays.asList(modelName, numOfModules, resultObject.numOfOrig, numberOfStates, resultObject.averageTraceSize, resultObject.averageTracesPerLog, 
						k, resultObject.diffExecutionTime, resultObject.regularExecutionTime, resultObject.relationExecutionTime, isRecursive));
				
				writer.flush();
				
				writer.close();
						
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	       
			
		}
		else
		{
			System.out.print("Problematic args list or Testktails Heretix k or CreateModel modelname numofstate numofmodules");
		}
	}
	
	public void writeLogs(String modelName, int numOfStates, int numOfModules, int numOfOrig, boolean isRecursive)
	{
		File logsFolder = new File(SystemConstants.LOG_DIRECTORY);
		
		logsFolder.mkdir();
		
		try
		{
			FileUtils.cleanDirectory(logsFolder);
			
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
			return;
		}
		
		List<FSAInputModel> list = EvaluationModelsManager.getAllEvaluationModels();
		
		FSAInputModel model = null;
		
		for (FSAInputModel fsa : list)
		{
			if (fsa.getModelName().equals(modelName))
			{
				model = fsa;
				break;
			}
		}
		
		if (model == null)
		{
			System.out.println("Error finding model name" + modelName);
			
			return;
		}
		
		List<FSAInputModel> listOfModels = getMutatedModels(model, numOfStates, numOfModules, numOfOrig, isRecursive);
		
		if (listOfModels.size() == 0)
		{
			System.out.println("Errrorrr rrr r ");
		}
		
		int num = 0;
		
		do 
		{
			for (FSAInputModel mutatedModel : listOfModels)
			{
				try 
				{
					writeModelLogs(mutatedModel);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					try {
						Thread.sleep(1000000000);
						Thread.sleep(1000000000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
			
			
			try
			{
				 num = logsFolder.list().length;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				
				try 
				{
					Thread.sleep(1000000000);
					Thread.sleep(1000000000);
				} 
				catch (InterruptedException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		} while (num == 0);
	}
	
	static Graph createResultClone(Graph graph, String logIndex)
	{
		Graph newResult = new Graph();
		
		newResult.setId(new Id());
		
		Set<Node> nodeSet = new HashSet<Node>();
		
		/*for (Node node : graph.getNodes(false))
		{
			newResult.addNode(node);
			node.
		}*/
		
		for (Edge edge : graph.getEdges())
		{
			Map<String, String> attribute  = edge.getAttributes();
			
			if (attribute.get("label").contains(logIndex))
			{
				Node src = edge.getSource().getNode();
				Node dst = edge.getTarget().getNode();
				
				if (!nodeSet.contains(src))
				{
					newResult.addNode(src);
					nodeSet.add(src);
				}
				
				if (!nodeSet.contains(dst))
				{
					newResult.addNode(dst);
					nodeSet.add(dst);
				}
				
				newResult.addEdge(edge);
			}
		}
		
		return newResult;
	}
	
	static boolean compareGraphs(Graph first, Graph second)
	{
		List<Vertex> firstVertex = convertToVertexes(first.getNodes(false));
		List<Vertex> secondVertex = convertToVertexes(second.getNodes(false));
		
		List<EdgeImp> firstEdge = convertToEdges(first.getEdges());
		List<EdgeImp> secondEdge = convertToEdges(second.getEdges());
		
		//boolean nodeCompare = firstVertex.equals(secondVertex);
		
		boolean nodeCompare = true;
		
		if (firstVertex.size() == secondVertex.size())
		{		
			for (Vertex v : firstVertex)
			{
				if (!secondVertex.contains(v))
				{
					nodeCompare = false;
					break;
				}
			}
		}
		else
		{
			nodeCompare = false;
		}
		
		if (!nodeCompare)
		{
			System.out.println(first.toString());
			System.out.println(second.toString());
			System.err.println("Error validated states");
		}
		
		boolean edgeCompare = true;
		
		if (firstEdge.size() == secondEdge.size())
		{
			for (EdgeImp v : firstEdge)
			{
				if (!secondEdge.contains(v))
				{
					System.out.println(first.toString() + " " + second.toString());
					edgeCompare = false;
					break;
				}
			}
		}
		else
		{
			edgeCompare = false;
		}
		
		if (!edgeCompare)
		{
			System.err.println("Error validated edges " + firstEdge.toString() + " " + secondEdge.toString());
		}
		
		return nodeCompare && edgeCompare;
	}
	
	static List<Vertex> convertToVertexes(ArrayList<Node> arrayList)
	{
		List<Vertex> vertexSet = new ArrayList<Vertex>();
		
		for (Node node : arrayList)
		{
			vertexSet.add(new Vertex(node));
		}
		
		return vertexSet;
	}
	
	static List<EdgeImp> convertToEdges(ArrayList<Edge> arrayList)
	{
		List<EdgeImp> edgeSet = new ArrayList<EdgeImp>();
		
		for (Edge edge : arrayList)
		{
			edgeSet.add(new EdgeImp(edge));
		}
		
		return edgeSet;
	}
}

