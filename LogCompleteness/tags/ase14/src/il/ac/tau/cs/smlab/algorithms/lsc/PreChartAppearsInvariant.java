package il.ac.tau.cs.smlab.algorithms.lsc;

import gov.nasa.ltl.graph.Graph;
import gov.nasa.ltl.trans.LTL2Buchi;
import gov.nasa.ltl.trans.ParseErrorException;
import il.ac.tau.cs.smlab.fw.trace.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import synoptic.invariants.ITemporalInvariant;
import synoptic.invariants.ltlchecker.LTLFormula;
import synoptic.model.interfaces.INode;
import synoptic.util.InternalSynopticException;

public class PreChartAppearsInvariant implements ITemporalInvariant{

    // CACHE:
    private Graph automaton;
    
    List<String> pre;
    List<String> main;
    
    
    public PreChartAppearsInvariant(List<EventType> preChart, List<EventType> mainChart) {
    	pre = new ArrayList<String>(preChart.size());
    	for (EventType e : preChart) {
    		pre.add(e.getEvent());
    	}
        main = new ArrayList<String>(mainChart.size());
      	for (EventType e : mainChart) {
      		main.add(e.getEvent());
    	}
    }
    
    @Override
    public gov.nasa.ltl.graph.Graph getAutomaton() {
        try {
            if (automaton == null) {
                String formula = LTLFormula.prepare(getLTLString());
            //    System.out.println("Prepared formula: " + formula);
                automaton = LTL2Buchi.translate("! (" + formula + ")");
            //    System.out.println("Translated formula: " + automaton);
            }
            return automaton;
        } catch (ParseErrorException e) {
            throw InternalSynopticException.wrap(e);
        }
    }

	@Override
	public String getLTLString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[]!(");
		sb.append("(");
	//	sb.append("(");
		for (String p:pre) {
			sb.append("!(did("+p+")) && ");
		}
		for (String m:main) {
			sb.append("!(did("+m+")) && ");
		}
		sb.setLength(sb.length() - 3);
		sb.append(")");
		String otherEvents = sb.substring(4);
		String br = "";
		for (String p:pre) {
			sb.append(" U(did("+p+") && X("+otherEvents);
			br = br + "))";
		} 
		sb.setLength(sb.length() - otherEvents.length() - 5); // remove last otherEvents
		sb.append(br);
		return sb.toString();
	}

	@Override
	public String getLongName() {
		return "PreChartAppears";
	}

	@Override
	public Set<synoptic.model.event.EventType> getPredicates() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRelation() {
		return "t";
	}

	@Override
	public String getShortName() {
		return "PreChartAppears";
	}

	@Override
	public <T extends INode<T>> List<T> shorten(List<T> arg0) {
		return arg0;
	}

}
