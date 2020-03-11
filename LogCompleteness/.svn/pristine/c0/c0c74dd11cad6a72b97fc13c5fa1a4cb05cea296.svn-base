package il.ac.tau.cs.smlab.algorithms.lsc;

import il.ac.tau.cs.smlab.fw.AbsractSpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.property.impl.ChartConfidenceProperty;
import il.ac.tau.cs.smlab.fw.property.impl.ChartConfidenceTernaryProperty;

// LSC Triggers and Effects
public class LSCAlgorithm extends AbsractSpecMiningAlgorithm {

	LSCInputParams params;

	public LSCAlgorithm(LSCInputParams params) {
		this.params = params;
	//	addLogProperty(new ChartConfidenceProperty(params.getChart(), params.isMineTriggers()));
		addLogProperty(new ChartConfidenceTernaryProperty(params.getChart(), params.isMineTriggers()));
	}

	@Override
	public String getAlgorithmName() {
		return "LCS Mining";
	}
	
	public void initProperties(String model) {
		// set the model's trigger chart
		((ChartConfidenceTernaryProperty)properties.get(0)).setChart(params.charts.get(model));
	}

}
