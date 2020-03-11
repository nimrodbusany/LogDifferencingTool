package DiffRunners;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;

import logs.Utils;
import synopticdiff.algorithms.KTails;
import synopticdiff.main.parser.TraceParser;
import synopticdiff.model.ChainsTraceGraph;
import synopticdiff.model.PartitionGraph;
import synopticdiff.model.export.GraphExporter;
import synopticdiff.tests.SynopticTest;

public class nKTDiffRunner {

	private Double diffExecutionTime = 0.0;
	public Double numOfTraces = 0.0;
	public Double numLetters = 0.0;
	
	public Graph runDiffKtails(int kParam, String logs_folder, String resultFile, String baseDir) throws IOException
	{
		List<Integer> integerTraces = new ArrayList<Integer>();
		List<String> logLines = new ArrayList<String>();
		
		File resultDir = new File(baseDir + File.separator + "output");
		
		resultDir.mkdir();
						
		File resultFileHandle = new File(resultFile);
		
		resultFileHandle.delete();
		
		(new File(resultFile + ".png")).delete();
		
		File[] filesList = new File(logs_folder).listFiles();
		
		Arrays.sort(filesList);
		
		String partition = "^--$";
		
		String regExp = "^(?<TYPE>)$";
								
		for (File file: filesList)
		{
			try
			{
				if  (!file.toString().endsWith("log"))	
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
				
				numOfTraces += tracesList.size();
				
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
		numLetters += logLines.size();
		
		try
		{
			
			
			ChainsTraceGraph ret = (ChainsTraceGraph) SynopticTest.genChainsTraceGraph(logLinesArray,
					defParser);
			
			double miliSecondsStart =  System.currentTimeMillis();
			
			PartitionGraph result = null;
			
			result = KTails.performKTails(ret, kParam);
						
			double miliSecondsEnd = System.currentTimeMillis();
			
			double miliSecondsOverall = (miliSecondsEnd - miliSecondsStart);
			
			this.diffExecutionTime += miliSecondsOverall;
			
			System.out.println("Diff\tkTails\tk\t= " + kParam + "\tTime\t-\t" + (miliSecondsOverall));
			
			GraphExporter.exportGraph(resultFile, result, true, integerTraces);
									
			//GraphExporter.generatePngFileFromDotFile(resultFile);
							
			FileReader in=null;
	        
			File f = new File(resultFile);
			
			try 
			{
		        in = new FileReader(f);
		        Parser p = new Parser();
		        p.parse(in);
		        
		        in.close();
		        
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
	
}
