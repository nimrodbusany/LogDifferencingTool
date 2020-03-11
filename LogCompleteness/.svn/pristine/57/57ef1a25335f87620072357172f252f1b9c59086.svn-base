package il.ac.tau.cs.smlab.fw.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class StaminaToDotConverter {
	
	
	public static void main (String[] args) throws Exception {
		String model = "";
		
		String stamina = "C:\\svn\\LogCompletenessEvaluation\\resources\\models\\stamina\\" + model + ".net";
		String dot = stamina + ".dot";
		List<String> content;
		FileInputStream inputStream = new FileInputStream(stamina);
	    try {
	        content = IOUtils.readLines(inputStream);
	    } finally {
	        inputStream.close();
	    }
	    
	    StringBuffer sb = new StringBuffer();
	    sb.append("digraph Automaton {");
	    sb.append("\n");
	    
	    if (!content.get(0).equals("*Network")) {
	    	throw new Exception("File is not in expected format");
	    }
		
	    if (!content.get(1).contains("*Vertices")) {
	    	throw new Exception("File is not in expected format");
	    }
	    
	    String nodes = content.get(1).substring(10);
	    int numOfNodes = Integer.valueOf(nodes);
	    
	    for (int i = 0 ; i < numOfNodes ; i++) {
	    	sb.append(content.get(i + 2) + " [shape=circle,label=\"\"]");
	    	sb.append("\n");
	    }
	    	
	    if (!content.get(numOfNodes+2).equals("*Arcs")) {
	    	throw new Exception("File is not in expected format");
	    }	
	    	
	    for (int i = numOfNodes+3 ; i < content.size() ; i++) {
	    	String[] edge = content.get(i).split(" ");
	    	if (edge.length == 1) continue;
	    	sb.append(edge[0]);
	    	sb.append(" -> ");
	    	sb.append(edge[1]);
	    	sb.append(" [label=");
	    	sb.append(edge[3]);
	    	sb.append("]");
	    	sb.append("\n");
	    }
	    
	    
	    System.out.println(sb.toString());
	   
	    
	    FileOutputStream output = new FileOutputStream(new File(dot));
	    IOUtils.write(sb.toString(), output);
		
		
	}
	

}
