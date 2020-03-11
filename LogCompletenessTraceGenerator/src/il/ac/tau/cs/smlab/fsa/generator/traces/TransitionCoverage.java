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
 * The generated traces satisfy the TRANSITION coverage criteria 
 * (however the coverage criteria is limited by the number of times each transition is traversed)
 * INPUTs:
 * automataName = it indicates the input EFSA used to generate traces
 * tracesFileName = it indicates the name of the file where interaction traces will be stored
 * n = it indicates the min number of time each transition must be traversed 
 **/

public class TransitionCoverage {
	//******************INPUTs**************
	int n = 2;
	String automataName = "outJSS.ser";
	String tracesFileName = "transitionCoverage.txt";
	//******************INPUTs**************

	/*	static String serFile = CreateFSA.projectDir + "generatedEFSAs/" + automataName;
	static String quarkFile = CreateFSA.projectDir + "generatedTraces/" + tracesFileName;*/

	int nr = 1;

	protected Vector<Vector<STComponent>> matrixTrans = null;
	private String outputFile;

/*	public static void main(String[] args){

		TransitionCoverage main = new TransitionCoverage();

		FiniteStateAutomaton efsa = main.readAutoma();
		main.TransitionCross(efsa);

		ArrayList<String> matrixS = Parsing.parse(main.matrixTrans, 1);
		main.replicateTraces(matrixS,nr);
		CreateInputTraces.create(matrixS, quarkFile);		
	}
*/

	public TransitionCoverage(int minNumOfTransitionVisits, String outputFile) {
		this.n = minNumOfTransitionVisits;
		this.outputFile = outputFile;
	}

	public void generateTraces(FiniteStateAutomaton efsa) {
		TransitionCross(efsa);
		ArrayList<String> matrixS = Parsing.parse(matrixTrans, 1);
		replicateTraces(matrixS,nr);
		CreateInputTraces.create(matrixS, outputFile);	
	}

	//Legge il file .ser e restituisce l'automa su cui verrˆ effettuato la Copertura delle Transizioni
	/*	protected FiniteStateAutomaton readAutoma(){

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

	/**
	 * Crea le tracce attraversando tutte le transizioni
	 * 1 o n volte
	 * @param efsa
	 */
	protected void TransitionCross(final FiniteStateAutomaton efsa){

		//		int cc = CalculateCC.INSTANCE.cyclomaticComplexity(efsa);
		//		System.out.println("Complessitˆ Ciclomatica: "+cc);

		//numero massimo di tracce che possono essere generate
		Integer MaxTraceGen = SettingsManager.getMaxTraceGen();

		//array con tutte le transizioni da percorrere
		Transition[] transEfsa = efsa.getTransitions();
		ArrayList<Transition> allTrans = new ArrayList<Transition>();
		for(int i=0; i<transEfsa.length; i++){
			int j=1;
			while(j <= n){
				allTrans.add(transEfsa[i]);
				j++;
			}
		}

		//inizializza la matrice dove verranno memorizzate tutte le tracce con lo stato iniziale dell'automa
		matrixTrans = new Vector<Vector<STComponent>>();
		Vector<STComponent> trans = new Vector<STComponent>();
		STComponent statoIniziale = new STComponent(efsa.getInitialState(), null);
		trans.add(statoIniziale);
		matrixTrans.add(trans);

		while(true){

			//controlla se siamo in uno stato finale con 0 transizioni uscenti
			if( ((checkFinalState(efsa, matrixTrans.lastElement().lastElement().getState())) && efsa.getTransitionsFromState(matrixTrans.lastElement().lastElement().getState()).length == 0) ){

				if(allTrans.size() >= 1 && MaxTraceGen > 1){
					matrixTrans.add(new Vector<STComponent>());
					matrixTrans.lastElement().add(new STComponent(efsa.getInitialState(),null));
					MaxTraceGen--;
				}
				else{
					//stampa se il TransitionCoverage ha finito coprendo tutte le transizioni o meno
					if(allTrans.size() == 0){
		//				System.out.println("Copertura di TUTTE le transizioni RAGGIUNTA!");
					}
					else{
		//				System.out.println("Copertura di TUTTE le transizioni NON RAGGIUNTA!");
					}
					break;
				}

				//controlla se siamo in uno stato finale con >0 transizioni uscenti allora decidiamo se continuare o fermarci
			}else if( ((checkFinalState(efsa, matrixTrans.lastElement().lastElement().getState())) && efsa.getTransitionsFromState(matrixTrans.lastElement().lastElement().getState()).length >=1 ) ){

				if(allTrans.size() >= 1 && MaxTraceGen > 1){

					Random brand = new Random();
					boolean b = brand.nextBoolean();

					if(b){
						pickTransitionToCross(efsa, matrixTrans, allTrans);

					}else{
						matrixTrans.add(new Vector<STComponent>());
						matrixTrans.lastElement().add(new STComponent(efsa.getInitialState(),null));
						MaxTraceGen--;
					}

				}
				else{
					//stampa se il TransitionCoverage ha finito coprendo tutte le transizioni o meno
					if(allTrans.size() == 0){
						System.out.println("Copertura di TUTTE le transizioni RAGGIUNTA!");
					}
					else{
						System.out.println("Copertura di TUTTE le transizioni NON RAGGIUNTA!");
					}
					break;
				}

			}else{
				pickTransitionToCross(efsa, matrixTrans, allTrans);
			}

		}

	}

	//Controlla se si tratta di uno stato finale
	protected boolean checkFinalState(FiniteStateAutomaton efsa, State state) {

		for(State finalState : efsa.getFinalStates()) {
			if(finalState == state)
				return true;
		}
		return false;
	}

	//sceglie una transizione a caso da percorrere
	protected void pickTransitionToCross(FiniteStateAutomaton efsa, Vector<Vector<STComponent>> matrixTrans, ArrayList<Transition> allTrans){

		Transition[] tmptrans = efsa.getTransitionsFromState(matrixTrans.lastElement().lastElement().getState());		
		Random irand = new Random();
		int index = irand.nextInt(tmptrans.length);
		matrixTrans.lastElement().lastElement().setTransition(tmptrans[index]);
		matrixTrans.lastElement().add(new STComponent(tmptrans[index].getToState(), null));
		deleteTransition(allTrans, tmptrans[index]);
	}	

	//Cancella le transizioni che sono state esplorate
	protected ArrayList<Transition> deleteTransition(ArrayList<Transition> trans, Transition t){

		if(trans.contains(t)){
			trans.remove(t);
		}

		return trans;
	}


	/**
	 * Replica la matrice n volte
	 * @param n
	 */
	protected void replicateTraces(ArrayList<String> m, int n){
		int iniSize = m.size();

		for(int j= 0; j<n-1; j++){
			int k = 0;
			for(int i = 0; i<iniSize;i++){
				String s = (String)m.get(k);
				m.add(k+1, s);
				k += j+2;
			}
		}
	}
}
