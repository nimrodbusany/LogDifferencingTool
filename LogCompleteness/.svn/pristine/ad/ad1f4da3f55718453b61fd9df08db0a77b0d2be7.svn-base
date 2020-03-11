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
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.io.DataIOException;
import prefuse.data.tuple.TupleSet;


public class PradelFSAInputModel extends AbstractFSAInputModel {

	public PradelFSAInputModel(String name) throws InvalidModelException, IOException {
		super(name);
	}


	private void setFsa(String name, int numOfStates, int numOfModules, int numOfReuglar) throws InvalidModelException {
		DotFileReader reader = new DotFileReader();
		FileInputStream is;
		try {
			is = new FileInputStream(SystemConstants.RESOURCES_DIRECTORY + "models/pradel/" + name + ".dot");
			Graph g = reader.readGraph(is);
			fsa = convertToFSA(g);
			
			for (int i = 0; i <  numOfReuglar; i++)
			{
				mutatedModels.add(this);
			}
						
			List<FiniteStateAutomaton> list = mutateAutomaton(fsa, numOfStates, numOfModules - numOfReuglar);

			try
			{
				for (FiniteStateAutomaton fsaSec : list)
				{
					PradelFSAInputModel davidSec = new PradelFSAInputModel(name, visits);
					davidSec.setFsa(fsaSec);
					
					mutatedModels.add(davidSec);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		} catch (DataIOException|FileNotFoundException e) {
			throw new InvalidModelException(e);
		}
	}
	
	private void setFsaRecursive(String name, int numOfStates, int numOfModules) throws InvalidModelException {
		DotFileReader reader = new DotFileReader();
		FileInputStream is;
		try {
			is = new FileInputStream(SystemConstants.RESOURCES_DIRECTORY + "models/pradel/" + name + ".dot");
			Graph g = reader.readGraph(is);
			fsa = convertToFSA(g);
			
			FiniteStateAutomaton newFsa = fsa;
			
			mutatedModels.add(this);
			
			for (int i = 1; i <  numOfModules; i++)
			{
				List<FiniteStateAutomaton> list = mutateAutomaton(newFsa, 1, 1);
				
				newFsa = list.get(0);

				try
				{
						PradelFSAInputModel davidSec = new PradelFSAInputModel(name, visits);
						davidSec.setFsa(newFsa);
						
						mutatedModels.add(davidSec);
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
				
		} catch (DataIOException|FileNotFoundException e) {
			throw new InvalidModelException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Graph createMutatedFSARemoveFromGraph(Graph g)
	{
		Random rand = new Random();
		
		int index = rand.nextInt(g.getEdgeCount());
		
		boolean remove = g.removeEdge(index);
		
		return g;
	}
	
	@SuppressWarnings("unchecked")
	private FiniteStateAutomaton createMutatedFSARemove(Graph g) 
	{
		FiniteStateAutomaton fsa = new FiniteStateAutomaton();
	
		//boolean edgeRemoved = false;
		
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
		
		int counter = 0;
		while (iter.hasNext()) 
		{
			iter.next();
			counter++;
		}
		
		Random rand = new Random();
		
		int index = rand.nextInt(counter);
		
		if (index == 0) index++;
		counter = 0;
		
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

			if (counter++ == index)
			{
				continue;
			}
			
			fsa.addTransition(transition);
		}
		
		return fsa;
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
			setFsa(name, 0 , 0, 0);
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
		logstr = logstr.replace("TERMINAL\n", "");
		
		/*while (logstr.length() < 5000)
		{
			logstr += "\n" + logstr;
		}*/
		
		FileUtils.writeStringToFile(log, logstr);

	}
	
	@Override
	public FSACoverageTraceGenerator getCoverage(String filenamePrefix) {
		return FSACoverageTraceGeneratorFactory.getStateCoverage(filenamePrefix, visits);
	}


	@Override
	public FiniteStateAutomaton convertToMutatesFsa(int numOfStatesToAdd,
			int numOfModuls, int numOfRegular) throws InvalidModelException {
		if (fsa == null) {
			setFsa(name, numOfStatesToAdd , numOfModuls, numOfRegular);
		}
		
		return fsa;
	}


	@Override
	public FiniteStateAutomaton convertToMutatesFsa(int numOfStatesToAdd,
			int numOfModuls) throws InvalidModelException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public FiniteStateAutomaton makeRecurstionMutation(int numOfStatesToAdd,
			int numOfModuls) throws InvalidModelException {
		if (fsa == null) {
			setFsaRecursive(name, numOfStatesToAdd, numOfModuls);
		}
		
		return fsa;
	}
}

