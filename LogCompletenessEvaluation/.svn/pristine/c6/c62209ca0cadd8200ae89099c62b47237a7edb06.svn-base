package il.ac.tau.cs.smlab.fsa.validation;

import il.ac.tau.cs.smlab.fsa.validation.models.ChainFSAValidationModel;
import il.ac.tau.cs.smlab.fsa.validation.models.ChainWithForwardTransitionsFSAValidationModel;
import il.ac.tau.cs.smlab.fsa.validation.models.ChainWithRepetitionsFSAValidationModel;
import il.ac.tau.cs.smlab.fsa.validation.models.ChainWithSelfTransitionsFSAValidationModel;
import il.ac.tau.cs.smlab.fsa.validation.models.CliqueFSAValidationModel;
import il.ac.tau.cs.smlab.fsa.validation.models.LoopFSAValidationModel;
import il.ac.tau.cs.smlab.fsa.validation.models.MultiChainFSAValidationModel;
import il.ac.tau.cs.smlab.fw.models.FSAInputModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ValidationModelsManager {
	
	public static List<FSAInputModel> getValidationModels() {
		List<FSAInputModel> models = new ArrayList<FSAInputModel>();
		
		// size has no significant impact
		models.add(new ChainFSAValidationModel("Chain-S", 6));
		models.add(new ChainFSAValidationModel("Chain-L", 21));
		models.add(new LoopFSAValidationModel("Loop-S", 6));
		models.add(new LoopFSAValidationModel("Loop-L", 21));
		models.add(new ChainWithRepetitionsFSAValidationModel("Chain with event repetitions-S", 6));
		models.add(new ChainWithRepetitionsFSAValidationModel("Chain with event repetitions-L", 21));
		models.add(new ChainWithSelfTransitionsFSAValidationModel("Chain with self transitions-S", 6));
		models.add(new ChainWithSelfTransitionsFSAValidationModel("Chain with self transitions-L", 21));
		models.add(new MultiChainFSAValidationModel("MultiChain-S", MultiChainFSAValidationModel.getNumberofchains() * 3)); // 9 states
		models.add(new MultiChainFSAValidationModel("MultiChain-L", MultiChainFSAValidationModel.getNumberofchains() * 8)); // 24 states
		models.add(new ChainWithForwardTransitionsFSAValidationModel("Chain with forward transitions-S", 6));
		models.add(new ChainWithForwardTransitionsFSAValidationModel("Chain with forward transitions-L", 21));
		models.add(new CliqueFSAValidationModel("Clique-S", 6));
		models.add(new CliqueFSAValidationModel("Clique-L", 21));
		return models;
	}
	
	
	public static Map<String, List<String>> getTriggerCharts() {
		
		Map<String, List<String>> charts = new LinkedHashMap<String, List<String>>(7);
		charts.put("Chain-S", Arrays.asList("1","4"));
		charts.put("Chain-L", Arrays.asList("4","18"));
		charts.put("Loop-S", Arrays.asList("2","3"));
		charts.put("Loop-L", Arrays.asList("15","18"));
		charts.put("Chain with event repetitions-S", Arrays.asList("3","5"));
		charts.put("Chain with event repetitions-L", Arrays.asList("4","8"));
		charts.put("Chain with self transitions-S", Arrays.asList("1","3"));
		charts.put("Chain with self transitions-L", Arrays.asList("11","14"));
		charts.put("Chain with forward transitions-S", Arrays.asList("2", "3"));
		charts.put("Chain with forward transitions-L", Arrays.asList("8", "10"));
		charts.put("MultiChain-S", Arrays.asList("2", "3"));
		charts.put("MultiChain-L", Arrays.asList("19","21", "23"));
		charts.put("Clique-L", Arrays.asList("2", "4"));
		charts.put("Clique-S", Arrays.asList("6", "15"));
		return charts;
		
	}

}
