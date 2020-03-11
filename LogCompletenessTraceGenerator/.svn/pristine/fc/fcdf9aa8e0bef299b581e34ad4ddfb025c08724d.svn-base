package il.ac.tau.cs.smlab.fsa.generator.traces;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.CreateInputTraces;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.Parsing;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.STComponent;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.SettingsManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * This class generates interaction traces from the input EFSA.
 * The generated traces satisfy the STATE coverage criteria 
 * (however the coverage criteria is limited by the number of times each state is traversed)
 * INPUTs:
 * automataName = it indicates the input EFSA used to generate traces
 * tracesFileName = it indicates the name of the file where interaction traces will be stored
 * n = it indicates the min number of time each state must be traversed 
**/

public class StateCoverage {
	//******************INPUTs**************
	 int n;
	 String outputFile;
	 String automataName = "outJSS1.ser";
	 String tracesFileName = "stateCoverage.txt";
	//******************INPUTs**************	

/*	static String serFile = CreateFSA.projectDir + "generatedEFSAs/" + automataName;
	static String quarkFile = CreateFSA.projectDir + "generatedTraces/" + tracesFileName;*/

	
	
	public StateCoverage(int minNumOfStateVisits, String outputFile) {
		this.n= minNumOfStateVisits;
		this.outputFile = outputFile;
	}
	
	public void genrateTraces(FiniteStateAutomaton automaton) {
		stateCross(automaton);
	}
/*	public static void main(String[] args){

		StateCoverage main = new StateCoverage();
		
		FiniteStateAutomaton efsa = main.readAutoma();
		main.StateCross(efsa);

	}*/
	
/*	//Legge il file .ser e restituisce l'automa su cui verrˆ effettuato la Copertura delle Transizioni
	private FiniteStateAutomaton readAutoma(){

		try{
			System.out.println(">> Loading ser file : "+serFile);
			FiniteStateAutomaton efsa = EFSASer.INSTANCE.loadEFSA(serFile);

			return efsa;
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return null;    
	}*/

	private void stateCross(final FiniteStateAutomaton efsa){
		
//		int cc = CalculateCC.INSTANCE.cyclomaticComplexity(efsa);
//		System.out.println("Complessitˆ Ciclomatica: "+cc);
		
		//numero massimo di tracce che possono essere generate
		Integer MaxTraceGen = SettingsManager.getMaxTraceGen();
		
		//array con tutti gli stati da percorrere	
		State[] statesEfsa = efsa.getStates();
		
		ArrayList<State> allStates = new ArrayList<State>();
		for(int i=0; i<statesEfsa.length; i++){
			int j=1;
			while(j <= n){
				allStates.add(statesEfsa[i]);
				j++;
			}
		}
		
		//inizializza la matrice dove verranno memorizzate tutte le tracce con lo stato iniziale dell'automa
		Vector<Vector<STComponent>> matrixTrans = new Vector<Vector<STComponent>>();
		Vector<STComponent> trans = new Vector<STComponent>();
		STComponent statoIniziale = new STComponent(efsa.getInitialState(), null);
		trans.add(statoIniziale);
		matrixTrans.add(trans);
		deleteState(allStates, efsa.getInitialState());
		
		while(true){
			
			//controlla se siamo in uno stato finale con 0 transizioni uscenti
			if( ((checkFinalState(efsa, matrixTrans.lastElement().lastElement().getState())) && efsa.getTransitionsFromState(matrixTrans.lastElement().lastElement().getState()).length == 0) ){
				
				if(allStates.size() >= 1 && MaxTraceGen > 1){
					matrixTrans.add(new Vector<STComponent>());
					matrixTrans.lastElement().add(new STComponent(efsa.getInitialState(),null));
					deleteState(allStates, efsa.getInitialState());
					MaxTraceGen--;
				}
				else{
					//stampa se lo StateCoverage ha finito coprendo tutti gli stati o meno
					if(allStates.size() == 0){
			//			System.out.println("Copertura di TUTTI gli stati RAGGIUNTA!");
					}
					else{
			//			System.out.println("Copertura di TUTTI gli stati NON RAGGIUNTA!");
					}
					break;
				}

			//controlla se siamo in uno stato finale con >0 transizioni uscenti allora decidiamo se continuare o fermarci
			}else if( ((checkFinalState(efsa, matrixTrans.lastElement().lastElement().getState())) && efsa.getTransitionsFromState(matrixTrans.lastElement().lastElement().getState()).length >=1 ) ){

				if(allStates.size() >= 1 && MaxTraceGen > 1){

					Random brand = new Random();
					boolean b = brand.nextBoolean();

					if(b){
						pickTransitionToCross(efsa, matrixTrans, allStates);

					}else{
						matrixTrans.add(new Vector<STComponent>());
						matrixTrans.lastElement().add(new STComponent(efsa.getInitialState(),null));
						deleteState(allStates, efsa.getInitialState());
						MaxTraceGen--;
					}

				}
				else{
					//stampa se lo StateCoverage ha finito coprendo tutti gli stati o meno
					if(allStates.size() == 0){
				//		System.out.println("Copertura di TUTTI gli stati RAGGIUNTA!");
					}
					else{
				//		System.out.println("Copertura di TUTTI gil stati NON RAGGIUNTA!");
					}
					break;
				}

			}else{
				pickTransitionToCross(efsa, matrixTrans, allStates);
			}
			
		}
		
		ArrayList<String> matrixS = Parsing.parse(matrixTrans, 1);
		CreateInputTraces.create(matrixS, outputFile);
	}

	//Controlla se si tratta di uno stato finale
	private boolean checkFinalState(FiniteStateAutomaton efsa, State state) {

		for(State finalState : efsa.getFinalStates()) {
			if(finalState == state)
				return true;
		}
		return false;
	}
	
	//sceglie una transizione a caso da percorrere
	private void pickTransitionToCross(FiniteStateAutomaton efsa, Vector<Vector<STComponent>> matrixTrans, ArrayList<State> allStates){

		Transition[] tmptrans = efsa.getTransitionsFromState(matrixTrans.lastElement().lastElement().getState());		
		Random irand = new Random();
		int index = irand.nextInt(tmptrans.length);
		matrixTrans.lastElement().lastElement().setTransition(tmptrans[index]);
		matrixTrans.lastElement().add(new STComponent(tmptrans[index].getToState(), null));
		deleteState(allStates, tmptrans[index].getToState());
	}
	
	//Cancella gli stati che sono stati esplorati
	private ArrayList<State> deleteState(ArrayList<State> states, State s){

		if(states.contains(s)){
			states.remove(s);
		}

		return states;
	}

}
