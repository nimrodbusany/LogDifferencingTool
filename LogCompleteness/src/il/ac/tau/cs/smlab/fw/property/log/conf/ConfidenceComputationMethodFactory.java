package il.ac.tau.cs.smlab.fw.property.log.conf;

import il.ac.tau.cs.smlab.fw.property.log.conf.v1.*;
import il.ac.tau.cs.smlab.fw.property.log.conf.v2.*;
import il.ac.tau.cs.smlab.fw.property.log.conf.v3.ExistentialConfidenceComputationV3;
import il.ac.tau.cs.smlab.fw.property.log.conf.v3.InvariantConfidenceComputationV3;

import java.util.HashMap;
import java.util.Map;

public class ConfidenceComputationMethodFactory {
	
	private static Map<String,ExistentialConfidenceComputation> existentialComps = new HashMap<String,ExistentialConfidenceComputation>();
	private static Map<String,InvariantConfidenceComputation> invariantComps = new HashMap<String,InvariantConfidenceComputation>();
	
	static {
		addExistential(ExistentialConfidenceComputationV1.getInstance());
		addExistential(ExistentialConfidenceComputationV2.getInstance());
		addExistential(ExistentialConfidenceComputationV3.getInstance());
		addInvariant(InvariantConfidenceComputationV1.getInstance());
		addInvariant(InvariantConfidenceComputationV2.getInstance());
		addInvariant(InvariantConfidenceComputationV3.getInstance());
	}
	
	private static void addExistential(ExistentialConfidenceComputation c) {
		existentialComps.put(c.getVersion(), c);
	}
	
	private static void addInvariant(InvariantConfidenceComputation c) {
		invariantComps.put(c.getVersion(), c);
	}

	
	public static ConfidenceComputationMethod<Boolean> getExistentialConfidenceComputation(String version) {
		return existentialComps.get(version);
	}
	public static ConfidenceComputationMethod<Boolean> getInvariantConfidenceComputation(String version) {
		return invariantComps.get(version);
	}
	
	public static ConfidenceComputationMethod<Boolean> getExistentialConfidenceComputation() {
		return existentialComps.get("1.0");
	}
	public static ConfidenceComputationMethod<Boolean> getInvariantConfidenceComputation() {
		return invariantComps.get("1.0");
	}
}
