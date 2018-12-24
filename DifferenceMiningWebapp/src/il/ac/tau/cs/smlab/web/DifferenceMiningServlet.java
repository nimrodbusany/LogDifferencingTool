package il.ac.tau.cs.smlab.web;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONObject;

import il.ac.tau.cs.smlab.diff.util.Utils;
import main.LogDiffAlt;
import synopticdiff.algorithms.KTails;
import synopticdiff.main.parser.TraceParser;
import synopticdiff.model.ChainsTraceGraph;
import synopticdiff.model.PartitionGraph;
import synopticdiff.model.export.GraphExporter;
import synopticdiff.tests.SynopticTest;

//@WebServlet(name = "DifferenceMiningServlet", urlPatterns = { "/dms" })
public class DifferenceMiningServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String SKstrings = "skstrings";
	private static final String KTDiff = "KTDiff";
	private static final String K_Tails = "KTails";
	private static final String DiffKT = "DiffKT";
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		return;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String algorithm = request.getParameter("algorithmparam");
		
		int kParam = Integer.parseInt(request.getParameter("kparam").replaceAll("\\s",""));
		String traceSeperator = request.getParameter("seperator");
		
		String[] logs = getLogsFromRequest(request);
		
		String firstLog = null;
		String secondLog = null;
		
		
		if (!algorithm.equals(KTDiff)){
			if (logs.length > 2){
				throw new ServletException("Two logs must be provided");
			}
			firstLog = logs[0];
			if (!algorithm.equals(K_Tails)) {
				secondLog = logs[1];
			}
		}
		String pngFileName = "";
		HashSet<String> files = null;
		if (algorithm.equals(SKstrings))
		{
			runSKtrings(request, kParam, traceSeperator, firstLog, secondLog);
		}
		else if (algorithm.equals(KTDiff)) {

			pngFileName = runKTDiff(request, kParam, traceSeperator, logs, pngFileName); 
	    
		}
		else if (algorithm.equals(K_Tails)) {
			pngFileName = runKTails(request, kParam, traceSeperator, logs, pngFileName); 
	    
		}
		else
		{
			files = runDiffKT(kParam, firstLog, secondLog, pngFileName);
			for (String file : files) {
				if (file.endsWith(".png")) {
					pngFileName = file;
					files.remove(file);
					break;
				}
			}
		}
		
		response.setContentType("text/x-json;charset=UTF-8");           
	    response.setHeader("Cache-Control", "no-cache");
		
	    try
	    {
	    	JSONObject json = new JSONObject();
	    								
	    	json.append("imgpath", "img/"+ pngFileName);
	    	json.append("confidence", "10");
	    	
	    	if (files != null) {
	    		json.append("filePaths", String.join(",", files) );
	    	}
	    	
	    	response.getWriter().write(json.toString());
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    return;
	}

	private HashSet<String> runDiffKT(int kParam, String firstLog, String secondLog, String pngFileName) throws IOException {
		LogDiffAlt l = new LogDiffAlt(kParam, true);
		
		File baseFolder = new File(getServletContext().getRealPath("/") + File.separator + "ktails");
		
		baseFolder.mkdir();
		FileUtils.cleanDirectory(baseFolder);
		
		HashSet<String> paths = new HashSet<String>();
		
		try
		{
			FileWriter firstFile = new FileWriter(baseFolder.getAbsolutePath() + File.separator + "first.log");
			FileWriter secondFile = new FileWriter(baseFolder.getAbsolutePath()  + File.separator + "second.log");
			
			firstFile.write(firstLog);
			secondFile.write(secondLog);
			
			firstFile.close();
			secondFile.close();
			
			Set<String> files =  l.runKtailsOnly(baseFolder.getAbsolutePath() + File.separator);
			Set<String> pngPath = new HashSet<String>();
			
			
			for (String filename : files) {
				if (filename.endsWith(".png")) {
					pngPath.add(filename);
				}
				else {
					paths.add(filename); 
				}
			}
			if (pngPath.size() > 1)
			{
				List<String> pngPathsList = new ArrayList<String>(pngPath);
				Collections.sort(pngPathsList);
				
				Iterator<String> it = pngPathsList.iterator();
				BufferedImage img1 = ImageIO.read(new File(it.next()));
				BufferedImage img2 = ImageIO.read(new File(it.next()));
				BufferedImage resutImage = joinBufferedImage(img2, img1);
				
				File result =  new File(getServletContext().getRealPath("/") + File.separator + "img" + File.separator + "result.png");
			
				ImageIO.write(resutImage, "png", result);
			
				pngFileName = result.getName();
			}
			else if (pngPath.size() > 0)
			{
				File result =  new File(getServletContext().getRealPath("/") + File.separator + "img" + File.separator + "result.png");
				
				Files.copy(new File(pngPath.iterator().next()).toPath(), result.toPath(), StandardCopyOption.REPLACE_EXISTING);
				
				pngFileName = result.getName();
			}
			else
			{
				pngFileName = "NoDiff.png";
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		paths.add(pngFileName);
		return paths;
	}

	private void runSKtrings(HttpServletRequest request, int kParam, String traceSeperator, String firstLog,
			String secondLog) {
//		double sParam = Double.parseDouble(request.getParameter("sparam"));
//		List<List<String>> firstTracesList = Utils.readFromFiles(firstLog, traceSeperator);
//		List<List<String>> secondTracesList = Utils.readFromFiles(secondLog, traceSeperator);
		
//			file = SKString.extractDifference(kParam, sParam, 10, firstTracesList, secondTracesList);
//			
//			File picFile = new File(file);
		
//			File dstFile = new File(getServletContext().getRealPath("/") + File.separator + "img" + File.separator + file);
					
//		try
//		{
//				Files.copy(picFile.toPath(), dstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//		}
//		catch (Exception e)
//		{
//			
//		}
//		return kParam;
	}

	private String runKTDiff(HttpServletRequest request, int kParam, String traceSeperator, String[] logs,
			String pngFileName) {
		
//		String reg = request.getParameter("reqexp");
//		String par = request.getParameter("par");
		
		List<Integer> numOfTraces = new ArrayList<Integer>();
		ArrayList<String> allevents = new ArrayList<String>();

		for (String log : logs){
			List<List<String>> logTracesList = Utils.readFromFiles(log, traceSeperator);
			String[] logLines = log.split("\\r?\\n");
			allevents.addAll(Arrays.asList(logLines));
			numOfTraces.add(logTracesList.size());
			
		}
		
		TraceParser defParser = null;
		
		defParser = SynopticTest.genDefParser();
		
		try 
		{
			//KTailsTests test = new KTailsTests();
			
			ChainsTraceGraph ret = (ChainsTraceGraph) SynopticTest.genChainsTraceGraph(allevents.toArray(new String[allevents.size()]),
					defParser); 
			
			PartitionGraph result = KTails.performKTails(ret, kParam);
			
			String baseDir = getServletContext().getRealPath("/");
			
			String file = baseDir + File.separator + "kTails_" + Integer.toString(kParam); 
			
			
			GraphExporter.exportGraph(file, result, true, numOfTraces);
							
			file = GraphExporter.generatePngFileFromDotFile(file);
			
			File picFile = new File(file);
			
			File dstFile = new File(getServletContext().getRealPath("/") + File.separator + "img" + File.separator + picFile.getName());
										
			try
			{
				Files.copy(picFile.toPath(), dstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (Exception e)
			{
				
			}
			
			pngFileName = dstFile.getName();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pngFileName;
	}

	private String runKTails(HttpServletRequest request, int kParam, String traceSeperator, String[] logs,
			String pngFileName) {
		
//		String reg = request.getParameter("reqexp");
//		String par = request.getParameter("par");
		
		
		List<Integer> numOfTraces = new ArrayList<Integer>();
		ArrayList<String> allevents = new ArrayList<String>();

		for (String log : logs){
			List<List<String>> logTracesList = Utils.readFromFiles(log, traceSeperator);
			String[] logLines = log.split("\\r?\\n");
			allevents.addAll(Arrays.asList(logLines));
			numOfTraces.add(logTracesList.size());
			
		}
		
		TraceParser defParser = null;
		
		defParser = SynopticTest.genDefParser();
		
		try 
		{
			//KTailsTests test = new KTailsTests();
			
			ChainsTraceGraph ret = (ChainsTraceGraph) SynopticTest.genChainsTraceGraph(allevents.toArray(new String[allevents.size()]),
					defParser); 
			
			PartitionGraph result = KTails.performKTails(ret, kParam);
			
			String baseDir = getServletContext().getRealPath("/");
			
			String file = baseDir + File.separator + "kTails_" + Integer.toString(kParam); 
			
			
			GraphExporter.exportGraph(file, result, false);
							
			file = GraphExporter.generatePngFileFromDotFile(file);
			
			File picFile = new File(file);
			
			File dstFile = new File(getServletContext().getRealPath("/") + File.separator + "img" + File.separator + picFile.getName());
										
			try
			{
				Files.copy(picFile.toPath(), dstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (Exception e)
			{
				
			}
			
			pngFileName = dstFile.getName();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pngFileName;
	}
	
	private String[] getLogsFromRequest(HttpServletRequest request) {
		
		Map<String, String[]> map = request.getParameterMap();
		String[] logs = null;
		
		for (Map.Entry<String, String[]> mapEntry : map.entrySet()) {
			String value[] = mapEntry.getValue();
			if (mapEntry.getKey().equals("logs[]")){
				logs = new String[value.length];
				for (int i=0;i<value.length;i++){
					logs[i] = value[i];
				}
			}
		}
		return logs;
	}
	
	public static BufferedImage joinBufferedImage(BufferedImage img1, BufferedImage img2) {

        //do some calculate first
        int offset  = 5;
        int wid = img1.getWidth()+img2.getWidth()+offset;
        int height = Math.max(img1.getHeight(),img2.getHeight())+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth()+offset, 0);
        g2.dispose();
        return newImage;
    }
	
	
	byte[] readImg(String relativePath, HttpServletResponse response) throws IOException
	{
		ServletContext cntx= getServletContext();
		// Get the absolute path of the image
		String filename = cntx.getRealPath("Images/button.png");
		//retrieve mimeType dynamically
		
		String mime = cntx.getMimeType(filename);
	   
		if (mime == null) 
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}

		response.setContentType(mime);
	    File file = new File(filename);
	    response.setContentLength((int)file.length());

	    FileInputStream in = new FileInputStream(file);
	    OutputStream out = response.getOutputStream();

	    //Copy the contents of the file to the output stream
	    byte[] buf = new byte[1024];
	    int count = 0;
	    
	    while ((count = in.read(buf)) >= 0) 
	    {
	    	out.write(buf, 0, count);
	    }
	    out.close();
	    in.close();
		return buf;
	}

}
