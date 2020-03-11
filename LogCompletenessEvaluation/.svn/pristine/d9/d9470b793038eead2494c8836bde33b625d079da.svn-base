package il.ac.tau.cs.smlab.fw.demo;

import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticExecutionTrace;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInputParams;
import il.ac.tau.cs.smlab.algorithms.synoptic.SynopticInvocation;
import il.ac.tau.cs.smlab.fw.AbsractSpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.property.impl.DirectlyFollowsProperty;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.io.FileNotFoundException;

import synoptic.invariants.TemporalInvariantSet;
import synoptic.main.SynopticMain;

public class DemoAlgorithm extends AbsractSpecMiningAlgorithm {

	SynopticInputParams params;

	public DemoAlgorithm(SynopticInputParams params) {
		this.params = params;
		addLogProperty(new DirectlyFollowsProperty());
	}

	public DemoAlgorithm() {
		addLogProperty(new DirectlyFollowsProperty());
	}

	@Override
	public void beforePropertiesResults(ExecutionTrace t) throws SpecMiningAlgorithmException {
		SynopticExecutionTrace st = (SynopticExecutionTrace) t;

		// a trace graph with just the current trace
		assert(st.getTraceGraph().getNumTraces() == 1);

		SynopticMain synopticInstance = SynopticInvocation.getInstance();
		TemporalInvariantSet minedInvariants;
		try {
			minedInvariants = synopticInstance.mineInvariants(st.getTraceGraph(),t.getTraceId());
		} catch (FileNotFoundException e) {
			throw new SpecMiningAlgorithmException(e);
		}

		assert (minedInvariants != null);
		st.setMinedInvariants(minedInvariants);

	}


	@Override
	public void afterPropertiesResults(ExecutionTrace t) {
	}

	@Override
	public String getAlgorithmName() {
		return "DemoAlgorithm";
	}



}
