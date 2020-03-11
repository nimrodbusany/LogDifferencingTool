package il.ac.tau.cs.smlab.algorithms.synoptic;

import il.ac.tau.cs.smlab.algorithms.synoptic.property.SynopticAlwaysFollowedByProperty;
import il.ac.tau.cs.smlab.algorithms.synoptic.property.SynopticAlwaysPrecedesProperty;
import il.ac.tau.cs.smlab.algorithms.synoptic.property.SynopticNeverFollowedByProperty;
import il.ac.tau.cs.smlab.fw.AbsractSpecMiningAlgorithm;
import il.ac.tau.cs.smlab.fw.SpecMiningAlgorithmException;
import il.ac.tau.cs.smlab.fw.property.impl.DirectlyFollowsProperty;
import il.ac.tau.cs.smlab.fw.trace.ExecutionTrace;

import java.io.FileNotFoundException;

import synoptic.invariants.TemporalInvariantSet;
import synoptic.main.SynopticMain;


public class SynopticAlgorithm extends AbsractSpecMiningAlgorithm {

	SynopticInputParams params;

	public SynopticAlgorithm(SynopticInputParams params) {
		// add directly-follows, always-followed, always-precedes, never-followed
		this.params = params;
		addLogProperty(new DirectlyFollowsProperty());
		addLogProperty(new SynopticAlwaysFollowedByProperty());
		addLogProperty(new SynopticNeverFollowedByProperty());
		addLogProperty(new SynopticAlwaysPrecedesProperty());
	}

	public SynopticAlgorithm() {
		// add directly-follows, always-followed, always-precedes, never-followed
		addLogProperty(new DirectlyFollowsProperty());
		addLogProperty(new SynopticAlwaysFollowedByProperty());
		addLogProperty(new SynopticNeverFollowedByProperty());
		addLogProperty(new SynopticAlwaysPrecedesProperty());
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
		return "Synoptic";
	}


}
