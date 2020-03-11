package logs;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	

	public static List<List<String>> readFromFiles(String file, String seperator) {
		
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
	
}
