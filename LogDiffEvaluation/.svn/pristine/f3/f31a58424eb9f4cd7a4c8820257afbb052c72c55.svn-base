package il.ac.tau.cs.smlab.diff.util;

import java.util.ArrayList;
import java.util.List;

public class Utils 
{
	public static List<List<String>> readFromFiles(String file, String seperator)
    {
    	List<List<String>> firstStrList = new ArrayList<List<String>>();
    
    	//List<String> lines = Util.readFile(file, 15000);
    	
    	String lines[] = file.split("\\r?\\n");
    	
    	List<String> tmpList = new ArrayList<String>(); 
    	
    	for (String line : lines)
		{
    		if (line.contains(seperator))
    		{
    			firstStrList.add(tmpList);
    			tmpList = new ArrayList<String>();
    			continue;
    		}
    		
    		tmpList.add(line);
		}
    	
    	if (tmpList.size() > 0)
    	{
    		firstStrList.add(tmpList);
    	}
    	
    	return firstStrList;
    }
	
	public static int numOfTraces(List<String> lines, String seperator)
	{
		int counter = 0;
		
		for(String line : lines)
		{
			counter = line.equals(seperator) ? counter + 1 : counter;
		}
		
		return counter;
	}
	
	//public static  readFromFiles(String)
}
