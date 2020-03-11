package il.ac.tau.cs.smlab.fw.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelStats {
	
	Map<String,String> info;
	
	public ModelStats(Map<String,String> info) {
		this.info = info;
	}
	
	public ModelStats() {
		this.info = new LinkedHashMap<String,String>();
	}
	
	public void add(String label, String val) {
		info.put(label, val);
	}
	
	public String get(String label) {
		return info.get(label);
	}
}
