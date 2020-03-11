package il.ac.tau.cs.smlab.fsa.generator.traces;

import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.CalculateCC;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.CreateInputTraces;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.Parsing;
import il.ac.tau.cs.smlab.fsa.generator.traces.util.STComponent;

import java.util.ArrayList;
import java.util.Vector;

/**
 * This class generates interaction traces from the input EFSA.
 * The generated traces satisfy the LINEAR INDIPENDENT PATH coverage criteria 
 * INPUTs:
 * automataName = it indicates the input EFSA used to generate traces
 * tracesFileName = it indicates the name of the file where interaction traces will be stored
 * nr = it indicates the number of time each generated transition must be replicated into the generated set 
**/

public class IndependentPathCoverage extends PathCoverage {
	
	public IndependentPathCoverage(int minNumOfTransitionVisits, String outputFile) {
		super(minNumOfTransitionVisits, outputFile);
		nr = minNumOfTransitionVisits;
	}

	//******************INPUTs**************
	 int nr;
	 String automataName = "outJSS.ser";
	 String tracesFileName = "independentPathCoverage.txt";
	//******************INPUTs**************	

/*	static String serFile = CreateFSA.projectDir + "generatedEFSAs/" + automataName;
	static String quarkFile = CreateFSA.projectDir + "generatedTraces/" + tracesFileName;*/
	
	// Matrice dei cammini indipendenti
	Vector<Vector<STComponent>> indipendetPath = null;
	// Vettore contenente tutte le transizioni
	ArrayList<Transition> allTrans = null;
	
	int cc = 0;
	
	
/*	public static void main(String args[]){
		
		IndependentPathCoverage indPath = new IndependentPathCoverage();
		indPath.indipendentPathCross();	
	}*/
	
	public void generateTraces(FiniteStateAutomaton automaton) {
		indipendentPathCross(automaton);
	}
	public void indipendentPathCross(FiniteStateAutomaton automaton){
		
		// Creo i cammini dell'automa
		pathCross(automaton);
		
		// Ordino le matrici
	    quickSort(matrixPathRetrieval, 0, matrixPathRetrieval.size()-1);
	//    System.out.println("Matrice dei cammini ordinata");
	    
	    // Calcolo i cammini inidipendenti
	    getIndipendentPath(automaton);

	    if(cc == indipendetPath.size()){
	//		System.out.println("\nInsieme di cammini indipendenti trovato correttamente:" + indipendetPath.size());
			
		    // Stampo la matrice
		    // printIndipendentPath();
		    
		    // Replico la matrice e Scrivo il file txt
		    ArrayList<String> matrixS = Parsing.parse(indipendetPath, 2);
		    
		    replicateTraces(matrixS,nr);
			CreateInputTraces.create(matrixS, outputFile);
	    }
		else
			System.out.println("\nInsieme dei cammini indipendenti NON trovato");
	    
	    
	}
	
	/**
	 * Funzione che calcola i cammini indipendenti
	 */
	private void getIndipendentPath(FiniteStateAutomaton efsa) {
			
		// Creo l'array di tutte le transizioni 
	    cc = CalculateCC.INSTANCE.cyclomaticComplexity(efsa);
	    Transition[] allTransition =  efsa.getTransitions();
	    allTrans = new ArrayList<Transition>();
	    for(int i=0; i<allTransition.length; i++){
			allTrans.add(allTransition[i]);
		}
		
	    indipendetPath = new Vector<Vector<STComponent>>();
	    
	    // Aggiungo il primo cammino della matrice matrixPathRetrieval
	    // alla matrice dei cammini indipendenti
		indipendetPath.add(matrixPathRetrieval.get(0));
		checkIndipendentPath(indipendetPath.get(0));
			
		for(int r = 1; r<matrixPathRetrieval.size(); r++){
			if(checkIndipendentPath(matrixPathRetrieval.get(r))){
				// E' un cammino indipendente, lo aggiungo alla matrice
				indipendetPath.add(matrixPathRetrieval.get(r));
			}
			
			if(allTrans.size() == 0){
				break;
			}
		}		
	}

	/**
	 * Verifico se il cammino ? un cammino indipendente 
	 * e torno true se lo ?.
	 * Inoltre elimino dall'array allTrans tutte le transizioni
	 * che compaiono all'interno del cammino indipendente
	 * @param vector
	 */
	private boolean checkIndipendentPath(Vector<STComponent> vector) {
		
		boolean flag = false;
		
		for(int i = 0; i< vector.size()-2;i++){
			STComponent component = vector.get(i);
			
				String trans = component.getTransition().toString();		
				for(int h=0;h< allTrans.size();h++){
					String transTemp = allTrans.get(h).toString();
					// Controllo se la label ? uguale
					if(trans.equals(transTemp))	
						// Controllo se gli stati iniziali corrispondono
						if(component.getTransition().getFromState().getName().equals(allTrans.get(h).getFromState().getName()))
							// Confronto se gli stati finali corrispondo
							if(component.getTransition().getToState().getName().equals(allTrans.get(h).getToState().getName())){
								flag = true;
								// Rimuovo l'oggetto dall'array delle transizioni
								//System.out.println("Rimuovo la trans:"+component.getTransition().getDescription());
								allTrans.remove(h);
							}
				}
		}
		
		return flag;
		
	}
	
	@SuppressWarnings("unused")
	private void printIndipendentPath(){
		for(int e = 0; e<indipendetPath.size();e++){
			System.out.println("\nINDIPENDENT: " + (indipendetPath.get(e).size()-2));
			for(int h = 0; h<indipendetPath.get(e).size()-2;h++){
				try{
					System.out.print(" - "+ indipendetPath.get(e).get(h).getTransition().getDescription().charAt(0));
				}catch(Exception ex){
					System.out.print(" - ");
				}
			}
		}
	}
	
	
	/**
	 * Funzione utilizzata dal quick sort
	 * @param arr
	 * @param left
	 * @param right
	 * @return
	 */
	private int partition(Vector<Vector<STComponent>> arr, int left, int right)
	{
	      int i = left, j = right;
	      Vector<STComponent> tmp;
	      Vector<STComponent> pivot = (Vector<STComponent>) arr.get(  (left + right) / 2  );
	      while (i <= j) {
	            while (arr.get(i).size() < pivot.size())
	                  i++;
	            while (arr.get(j).size() > pivot.size())
	                  j--;
	            if (i <= j) {
	                  tmp = arr.get(i);
	                  arr.set(i, arr.get(j));
	                  arr.set(j, tmp);
	                  i++;
	                  j--;
	            }
	      };
	      return i;
	}

	/**
	 * Ordina la matrice matrixPathRetrieval in base alla size di ogni
	 * vettore contenuto e utilizzando il metodo quicksort
	 */
	public void quickSort(Vector<Vector<STComponent>> arr, int left, int right) {
	      int index = partition(arr, left, right);
	      if (left < index - 1)
	            quickSort(arr, left, index - 1);
	      if (index < right)
	            quickSort(arr, index, right);
	}
  
}
