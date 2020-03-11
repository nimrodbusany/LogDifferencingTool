package il.ac.tau.cs.smlab.fw.models;

import idot.util.DotFileReader;
import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.xml.InvalidModelException;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGenerator;
import il.ac.tau.cs.smlab.fw.trace.generator.coverage.FSACoverageTraceGeneratorFactory;
import il.ac.tau.cs.smlab.fw.utils.SystemConstants;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.io.DataIOException;
import prefuse.data.tuple.TupleSet;


public class PradelFSAInputModel extends AbstractFSAInputModel {

	public PradelFSAInputModel(String name) throws InvalidModelException, IOException {
		super(name);
	}


	private void setFsa(String name) throws InvalidModelException {
		DotFileReader reader = new DotFileReader();
		FileInputStream is;
		try {
			is = new FileInputStream(SystemConstants.RESOURCES_DIRECTORY + "models/pradel/" + name + ".dot");
			Graph g = reader.readGraph(is);
			fsa = convertToFSA(g);
		} catch (DataIOException|FileNotFoundException e) {
			throw new InvalidModelException(e);
		}
	}
	
	
	public PradelFSAInputModel(String name,int visits) {
		super(name,visits);
	}
	
	
	public static void main (String[] args) throws InvalidModelException, IOException {
		new PradelFSAInputModel("java.util.Formatter");
	}
	
	@SuppressWarnings("unchecked")
	private FiniteStateAutomaton convertToFSA(Graph g) {
		FiniteStateAutomaton fsa = new FiniteStateAutomaton();

		TupleSet nodes = g.getNodes();

		Iterator<Tuple> iter = nodes.tuples();
		Point p = new Point(0,0);
		Map<String,State> statesMap = new HashMap<String,State>();
		State terminal = fsa.createState(p);
		terminal.setLabel("TERMINAL");
		terminal.setName("TERMINAL");
		fsa.addFinalState(terminal);
		while (iter.hasNext()) {
			Tuple t = iter.next();
			State s = fsa.createState(p);
			s.setName(t.getString("nodename"));
			s.setLabel(t.getString("nodename"));
			if (t.getString("shape").equals("doublecircle")) {
				fsa.addTransition(new FSATransition(s,terminal,"TERMINAL"));
			}
			if (s.getName().equals("initial")) {
				fsa.setInitialState(s);
			}
			statesMap.put(t.getString("nodename"), s);
		}
		
		iter = g.getEdgeTable().tuples();
		while (iter.hasNext()) {
			Tuple t = iter.next();
			Node source = g.getNodeFromKey(t.getLong(0));
			Node target = g.getNodeFromKey(t.getLong(1));
			State ss = statesMap.get(source.getString("nodename"));
			State ts = statesMap.get(target.getString("nodename"));
			assert(ss != null);
			assert(ts != null);
			String label = t.getString(2);
			if (label == null) label = "initial";
			Transition transition = new FSATransition(ss,ts,label);
			fsa.addTransition(transition);
		}
		return fsa;
	}

	@Override
	public FiniteStateAutomaton convertToFsa() throws InvalidModelException {
		if (fsa == null) {
			setFsa(name);
		}
		return fsa;
	}

	
	@Override
	public void postprocessGeneratedLog(File log, String traceSeparator, String eventSeparator) throws IOException {

		String logstr = FileUtils.readFileToString(log);
		logstr = logstr.replaceAll("initial::", "");
		// temp workaround to avoid traces with a single init event
	//	logstr = logstr.replaceAll("java.(.*).init\\(\\)::\r\n", "");
		// separate traces with traceSeparator instead of \n
		logstr = logstr.replaceAll("\n", "\n" + traceSeparator + "\n");
		logstr = logstr.substring(0, logstr.length() - 1);
		// separate events with \n instead of ::
		logstr = logstr.replaceAll("::::","\n");
		logstr = logstr.replaceAll("::","\n");
		logstr = logstr.replace("\r\n" + traceSeparator, traceSeparator);
		FileUtils.writeStringToFile(log, logstr);

	}
	
	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		return FSACoverageTraceGeneratorFactory.getStateCoverage(filenamePrefix, visits);
	}

}

