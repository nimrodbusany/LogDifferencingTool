package il.ac.tau.cs.smlab.fw.evaluation;

import il.ac.tau.cs.smlab.fw.models.DavidFSAInputModel;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;
import il.ac.tau.cs.smlab.fw.models.PradelFSAInputModel;
import il.ac.tau.cs.smlab.fw.models.ZellerFSAInputModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

public class EvaluationModelsManager {

	private static Map<String,Integer> modelsDavid = ImmutableMap.<String, Integer>builder()
			.put("Columba", 4) 
			.put("Heretix", 3) 
			.put("JArgs", 3) 
			.put("Jeti", 4) 
			.put("lucane", 4) 
			.put("OpenHospital", 4) 
			.put("RapidMiner", 3) 
			.put("tagsoup", 3) 
			.put("Thingamablog", 4)
			.build();


	private static Map<String,Integer> modelsZeller = ImmutableMap.<String, Integer>builder()
			.put("ZipOutputStream", 1000) 
			.put("Signature", 1000)
			.build();

	private static Map<String,Integer> modelsTesting = ImmutableMap.<String, Integer>builder()
			.put("Testing", 40) 
			.build();


	private static Map<String,Integer> modelsPradel = ImmutableMap.<String, Integer>builder()
			.put("java.util.Formatter", 1000) 
			.put("java.util.StringTokenizer", 1000) 
			.put("java.net.DatagramSocket", 1200) 
	    	.put("java.net.MultiCastSocket", 1500)
			.put("java.net.Socket", 800)
			.put("java.net.URL", 500)
			.build();


	public static Map<String, List<String>> getTriggerCharts() {

		Map<String, List<String>> charts = new LinkedHashMap<String, List<String>>(12);

		charts.put("Columba", Arrays.asList("i","j"));
		charts.put("Heretix", Arrays.asList("f","g"));
		charts.put("JArgs", Arrays.asList("c","d"));
		charts.put("Jeti", Arrays.asList("b","g"));
		charts.put("lucane", Arrays.asList("c", "f"));
		charts.put("OpenHospital", Arrays.asList("i", "r"));
		charts.put("RapidMiner", Arrays.asList("j", "n"));
		charts.put("tagsoup", Arrays.asList("b"));
		charts.put("Thingamablog", Arrays.asList("d","g"));

		charts.put("SMTPProtocol", Arrays.asList("openPort","auth"));
		charts.put("Signature", Arrays.asList("initSign","sign"));
		charts.put("ZipOutputStream", Arrays.asList("putNextEntry","write"));
		
		charts.put("java.util.Formatter", Arrays.asList("java.util.Formatter.format","java.util.Formatter.flush"));
		charts.put("java.util.StringTokenizer", Arrays.asList("java.util.StringTokenizer.hasMoreTokens","java.util.StringTokenizer.nextToken"));
		charts.put("java.net.DatagramSocket", Arrays.asList("java.net.DatagramSocket.send"));
		charts.put("java.net.MultiCastSocket", Arrays.asList("java.net.MulticastSocket.send","java.net.MulticastSocket.leaveGroup"));
		charts.put("java.net.Socket", Arrays.asList("java.net.Socket.getInputStream","java.net.Socket.getOutputStream"));
		charts.put("java.net.URL", Arrays.asList("java.net.URL.getContent","java.net.URL.openStream"));
		return charts;

	}

	public static List<FSAInputModel> getAllEvaluationModels() {
		List<FSAInputModel> allModels = new ArrayList<FSAInputModel>(modelsDavid.size() + modelsZeller.size());
		allModels.addAll(getDavidEvaluationModels());
		allModels.addAll(getZellerEvaluationModels());
		allModels.addAll(getPradelModels());
		return allModels;
	}

	public static List<FSAInputModel> getZellerEvaluationModels() {
		List<FSAInputModel> models = new ArrayList<FSAInputModel>(modelsZeller.size());
		for (Entry<String, Integer> e : modelsZeller.entrySet()) {
			models.add(new ZellerFSAInputModel(e.getKey(), e.getValue()));
		}
		return models;
	}

	public static List<FSAInputModel> getDavidEvaluationModels() {
		List<FSAInputModel> allModels = new ArrayList<FSAInputModel>(modelsDavid.size());
		for (Entry<String, Integer> e : modelsDavid.entrySet()) {
			allModels.add(new DavidFSAInputModel(e.getKey(), e.getValue()));
		}
		return allModels;
	}


	public static List<FSAInputModel> getTestingModels() {
		List<FSAInputModel> models = new ArrayList<FSAInputModel>(modelsTesting.size());
		for (Entry<String, Integer> e : modelsTesting.entrySet()) {
			models.add(new ZellerFSAInputModel(e.getKey(), e.getValue()));
		}
		return models;
	}

	public static List<FSAInputModel> getPradelModels() {
		List<FSAInputModel> models = new ArrayList<FSAInputModel>(modelsPradel.size());
		for (Entry<String, Integer> e : modelsPradel.entrySet()) {
			models.add(new PradelFSAInputModel(e.getKey(), e.getValue()));
		}
		return models;
	}


}
