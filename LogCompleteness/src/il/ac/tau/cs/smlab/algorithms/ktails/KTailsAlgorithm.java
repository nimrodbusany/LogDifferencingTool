package il.ac.tau.cs.smlab.algorithms.ktails;

import il.ac.tau.cs.smlab.fw.AbsractSpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.property.impl.DirectlyFollowsProperty;


public class KTailsAlgorithm extends AbsractSpecMiningAlgorithm {

	KTailsInputParams params;

	public KTailsAlgorithm(KTailsInputParams params) {
		this.params = params;
		// add k-directly-follows
		addLogProperty(new DirectlyFollowsProperty(params.k));
	}

	@Override
	public String getAlgorithmName() {
		return "k-tails";
	}
	
	

}
