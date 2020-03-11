package il.ac.tau.cs.smlab.fsa.adapter;

import java.util.HashSet;
import java.util.Set;

import synoptic.model.TransitionLabelsMap;
import synoptic.model.interfaces.ITransition;
import synoptic.util.time.ITime;
import synoptic.util.time.TimeSeries;

public class FSATransition implements ITransition<FSANode> {

	FSANode source;
	FSANode target;
	String label;


	static final Set<String> relation = new HashSet<String>();
	
	static {
		relation.add("t");
	}
	
	public FSATransition(FSANode source, FSANode target, String label) {
		this.source = source;
		this.target = target;
		this.label = label;
	}
	
	@Override
	public int compareTo(ITransition<FSANode> o) {
		return 0;
	}

	@Override
	public FSANode getTarget() {
		return target;
	}

	@Override
	public FSANode getSource() {
		return source;
	}

	@Override
	public Set<String> getRelation() {
		return relation;
	}

	@Override
	public void setTarget(FSANode target) {
		this.target = target;
		
	}

	@Override
	public void setSource(FSANode source) {
		this.source = source;
	}

	@Override
	public ITime getTimeDelta() {
		return null;
	}

	@Override
	public void setTimeDelta(ITime d) {
		
	}

	@Override
	public TimeSeries<ITime> getDeltaSeries() {
		return null;
	}

	@Override
	public void addTimeDeltaToSeries(ITime d) {
	}

	@Override
	public TransitionLabelsMap getLabels() {
		return null;
	}

	@Override
	public Double getProbability() {
		return null;
	}

	@Override
	public void setProbability(double fraction) {
	}

	@Override
	public Integer getCount() {
		return null;
	}

	@Override
	public void setCount(int count) {
		
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String toString() {
		return "["+source+","+label+","+target+"]";
	}
}
