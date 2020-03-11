package il.ac.tau.cs.smlab.fsa.generator.traces;


import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.CalculateCC;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.CreateInputTraces;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.Parsing;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.STComponent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;

/**
 * This class generates interaction traces from the input EFSA.
 * The generated traces satisfy the PATH coverage criteria 
 * (however the coverage criteria is limited by the number of times each state is traversed)
 * INPUTs:
 * automataName = it indicates the input EFSA used to generate traces
 * tracesFileName = it indicates the name of the file where interaction traces will be stored
 * nr = it indicates the max number of time each state must be traversed 
 */

public class PathCoverage {
	//******************INPUTs**************
	 int nr = 1;
	 String automataName = "outJSS1.ser";
	 String tracesFileName = "pathCoverage.txt";
	 String outputFile;
	//******************INPUTs**************
	
/*	 String serFile = CreateFSA.projectDir + "generatedEFSAs/" + automataName;
	 String quarkFile = CreateFSA.projectDir + "generatedTraces/" + tracesFileName;*/
	
	protected Vector<Vector<STComponent>> matrixPathRetrieval;
	
	public PathCoverage(int maxNumOfStateVisits, String outputFile) {
		this.nr = maxNumOfStateVisits;
		this.outputFile = outputFile;
	}
	
	
/*	public static void main(String[] args){    
		
		PathCoverage main = new PathCoverage();

		main.pathCross();
		
		ArrayList<String> matrixS = Parsing.parse(main.matrixPathRetrieval, 2);
		CreateInputTraces.create(matrixS, quarkFile);
		
	}*/
	
	public void generateTraces(FiniteStateAutomaton automaton) {
		pathCross(automaton);
		ArrayList<String> matrixS = Parsing.parse(matrixPathRetrieval, 2);
		CreateInputTraces.create(matrixS, outputFile);
	}

/*	// Legge l'automa dal file .ser
	protected FiniteStateAutomaton readAutoma(){

		try{
			System.out.println(">> Loading ser file : "+serFile);
			FiniteStateAutomaton efsa = EFSASer.INSTANCE.loadEFSA(serFile);

			return efsa;
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return null;    
	}  */

	protected void pathCross(FiniteStateAutomaton efsa){

		int count_state = 3;
	//	System.out.println("Complessitˆ Ciclomatica: "+CalculateCC.INSTANCE.cyclomaticComplexity(efsa));

		// Inizializza la matrice dove verrano memorizzati i cammini
		Vector<Vector<STComponent>> matrixPath = new Vector<Vector<STComponent>>();
		Vector<STComponent> paths = new Vector<STComponent>();
		STComponent statoIniziale = new STComponent(efsa.getInitialState(), null);
		paths.add(statoIniziale);
		matrixPath.add(paths);

		// Crea Matrice con tutti i cammini dell'automa
		while(true){
			for(int index=0; index < matrixPath.size(); index++){
				while( !(matrixPath.get(index).lastElement().isEndSTComponent()) ){
					if( ((checkFinalState(efsa, matrixPath.get(index).lastElement().getState())) && efsa.getTransitionsFromState(matrixPath.get(index).lastElement().getState()).length == 0) ) {
						//Final State without transitions -> Insert endState 
						STComponent endState = new STComponent(STComponent.ENDSTCOMPONENT);
						matrixPath.get(index).add(endState);
					}else
						if(checkElementCounter(matrixPath.get(index),nr)){
							//Actual State repeated > count_state -> Insert endState
							STComponent endState = new STComponent(STComponent.ENDSTCOMPONENT);
							matrixPath.get(index).set(matrixPath.get(index).size()-1, endState);
						}else
							if(efsa.getTransitionsFromState(matrixPath.get(index).lastElement().getState()).length == 1){
								//Insert State when number of Transitions from actual State == 1
								if(checkFinalState(efsa, matrixPath.get(index).lastElement().getState())){
									STComponent endPath = new STComponent(STComponent.ENDPATH);
									Transition[] tran = efsa.getTransitionsFromState(matrixPath.get(index).lastElement().getState());
									matrixPath.get(index).lastElement().setTransition(tran[0]);									
									matrixPath.get(index).add(endPath);
									matrixPath.get(index).add(new STComponent(tran[0].getToState(), null));
								}else{
									Transition[] tran = efsa.getTransitionsFromState(matrixPath.get(index).lastElement().getState());
									matrixPath.get(index).lastElement().setTransition(tran[0]);
									matrixPath.get(index).add(new STComponent(tran[0].getToState(), null));
								}

							}else
								if(efsa.getTransitionsFromState(matrixPath.get(index).lastElement().getState()).length > 1){
									// when number of Transitions from actual State > 1
									if(checkFinalState(efsa, matrixPath.get(index).lastElement().getState())){
										STComponent endPath = new STComponent(STComponent.ENDPATH);
										Transition[] trans = efsa.getTransitionsFromState(matrixPath.get(index).lastElement().getState());
										for(int i=1; i < trans.length; i++){
											matrixPath.add(deepCopy(matrixPath.get(index)));
											matrixPath.get(matrixPath.size()-1).lastElement().setTransition(trans[i]);
											matrixPath.get(matrixPath.size()-1).add(endPath);
											matrixPath.get(matrixPath.size()-1).add(new STComponent(trans[i].getToState(), null));
										}
										matrixPath.get(index).lastElement().setTransition(trans[0]);
										matrixPath.get(index).add(endPath);
										matrixPath.get(index).add(new STComponent(trans[0].getToState(), null));
									}else{
										Transition[] trans = efsa.getTransitionsFromState(matrixPath.get(index).lastElement().getState());
										for(int i=1; i < trans.length; i++){
											matrixPath.add(deepCopy(matrixPath.get(index)));								
											matrixPath.get(matrixPath.size()-1).lastElement().setTransition(trans[i]);
											matrixPath.get(matrixPath.size()-1).add(new STComponent(trans[i].getToState(), null));
										}
										matrixPath.get(index).lastElement().setTransition(trans[0]);
										matrixPath.get(index).add(new STComponent(trans[0].getToState(), null));								
									}	
								}else{
									try {
										throw new Exception("NO Stato finale ma nessuna transizione uscente");
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
				}
			}
			if(checkMatrixPath(matrixPath)){
				break;
			}
		}

		//Ricavare tutti i possibili Path da matrixPath
		pathRetrieval(matrixPath, efsa);
	}

	//Metodo per effettuare il DeepCopy
	protected Vector<STComponent> deepCopy(Vector<STComponent> src){
		Vector<STComponent> dest = new Vector<STComponent>();

		for(int i=0; i< src.size(); i++){
			STComponent temp = new STComponent(src.get(i).getState(),src.get(i).getTransition(), src.get(i).isEndSTComponent(), src.get(i).isEndPath());
			dest.add(temp);
		}
		return dest;
	}

	//Recupero di tutti i path
	protected void pathRetrieval(Vector<Vector<STComponent>> matrixPath, FiniteStateAutomaton efsa){
		matrixPathRetrieval = new Vector<Vector<STComponent>>();
		for(int i=0; i < matrixPath.size(); i++){
			matrixPathRetrieval.add(deepCopy(matrixPath.get(i)));
		}

		//Cancella Stati non finali alla fine del cammino
		for(int i=0; i < matrixPathRetrieval.size(); i++){
			int j = matrixPathRetrieval.get(i).indexOf(matrixPathRetrieval.get(i).lastElement())-1;
			while(!checkFinalState(efsa, matrixPathRetrieval.get(i).get(j).getState())){
				if(j == 0){
					matrixPathRetrieval.remove(i);
					i--;
					break;
				}else{
					matrixPathRetrieval.get(i).remove(j);
					j--;
				}
			}
		}
		
//		String fileA = "/Volumes/MacintoshHD/Tesi/my_workspace/output/txt/matrixA.txt" ;
//		printMatrix(matrixPathRetrieval, fileA);
		
		int index = matrixPathRetrieval.size();

		for(int i=0; i < index; i++){
			int j=0;
			if(controlEndPath(matrixPathRetrieval.get(i))){
				matrixPathRetrieval.add(new Vector<STComponent>());
				while(!matrixPathRetrieval.get(i).get(j).isEndSTComponent()){
					if(matrixPathRetrieval.get(i).get(j).isEndPath()){
						matrixPathRetrieval.get(i).remove(j);
						STComponent endState = new STComponent(STComponent.ENDSTCOMPONENT);
						matrixPathRetrieval.get(matrixPathRetrieval.size()-1).add(endState);
						matrixPathRetrieval.add(new Vector<STComponent>());
						j=0;
					}else{
						matrixPathRetrieval.get(matrixPathRetrieval.size()-1).add(matrixPathRetrieval.get(i).get(j));
						j++;
					}
				}
				if(matrixPathRetrieval.get(i).get(j).isEndSTComponent()){
					STComponent endState = new STComponent(STComponent.ENDSTCOMPONENT);
					matrixPathRetrieval.get(matrixPathRetrieval.size()-1).add(endState);
				}
			}	
		}
		
//		String fileB = "/Volumes/MacintoshHD/Tesi/my_workspace/output/txt/matrixB.txt";
//		printMatrix(matrixPathRetrieval, fileB);
	
	}
	
	//Controlla se c'� ENDPATH
	protected boolean controlEndPath(Vector<STComponent> path){
		for(int i=0; i < path.size(); i++){
			if(path.get(i).isEndPath()){
				return true;
			}
		}
		return false;
	}

	// Stampa la matrice in un fie di testo
	protected void printMatrix(Vector<Vector<STComponent>> matrixPathRetrieval, String fileName){

		System.out.println("Inizio scrittura file...");

		try {
			FileOutputStream file = new FileOutputStream(fileName);
			PrintStream Output = new PrintStream(file);

			for(int n=0;n<matrixPathRetrieval.size();n++){

				for(int i=0;i<matrixPathRetrieval.get(n).size()-2;i++){
					if(!(matrixPathRetrieval.get(n).get(i).isEndPath())){
						String[] tmp = matrixPathRetrieval.get(n).get(i).getTransition().getDescription().split("\n \n|" + "\n");
						String union = "";
						for(int u=0; u<tmp.length;u++){
							union = union.concat(tmp[u] + " ");
						}
						Output.print(union.concat(", "));
						//Output.print(matrixPathRetrieval.get(n).get(i).getTransition().getDescription()+" , ");
					}
				}
				Output.println();
			}
		} catch (IOException e) {
			System.out.println("Errore Scrittura file della matrice: " + e);
			System.exit(1);
		}
		System.out.println("Fine scrittura file!");
	}

	protected boolean checkMatrixPath(Vector<Vector<STComponent>> matrixPath){
		int count = matrixPath.size();
		int controlCount = 0;

		for(int i=0;i < count; i++){
			if(matrixPath.get(i).lastElement().isEndSTComponent()){
				controlCount++;
			}
		}
		if(count == controlCount){
			return true;
		}else
			return false;
	}

	//controlla quante volte � passato su uno stato
	protected boolean checkElementCounter(Vector<STComponent> paths, int cc){

		int count = 0;
		for(int i=0; i < paths.size(); i++){
			if(paths.get(i).getState() == paths.lastElement().getState()){
				count++;
				if(count > cc){
					return true;
				}
			}
		}
		return false;
	}

	// controlla se lo stato � uno stato finale
	protected boolean checkFinalState(FiniteStateAutomaton efsa, State state) {
		for(State finalState : efsa.getFinalStates()) {
			if(finalState == state)
				return true;
		}
		return false;
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
