package il.ac.tau.cs.smlab.fsa.generator;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition;
import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * this class creates EFSAs. 
 * The generated EFSA is useful to generate interaction traces for gk-Tail
 * INPUTs:
 * dimensione = it indicates the number of state for the generated EFSA
 * complessita = it indicates the cyclomatic complexity of the generated EFSA
 * projectDir = the absolute path where your eclipse project is located
 * automataName = file containing the generated EFSA (it always must be a .ser file) 
 *
 */
public class CreateFSA {
	//******************INPUTs**************

	int dimensione = 10;
	int complessita = 5;
	//******************INPUTs**************

	List<String> labels;

	int lPerc = 100;
	int iPerc = 100;
	int tPerc = 100;

	public CreateFSA(List<String> labels, int numOfStates, int cyclomaticComplexity) {
		this.dimensione = numOfStates;
		this.complessita = cyclomaticComplexity;
		this.labels = labels;
	}


	public FiniteStateAutomaton createAutoma() {

		//Create STATES
		FiniteStateAutomaton efsa = new FiniteStateAutomaton();
		Point p = new Point(0, 0);
		State states[] = new State[dimensione];

		for (int i=0; i < dimensione; i++ ){
			states[i] = efsa.createState(p);
		}

		efsa.setInitialState(states[0]);
		efsa.addFinalState(states[dimensione-1]);

		int numTrans = complessita - 2 + dimensione;
		FSATransition trans[] = new FSATransition[numTrans];
		Random randomGenerator = new Random();
		Vector <Integer> temp = new Vector <Integer>();

		while(temp.capacity() < dimensione){
			int id = randomGenerator.nextInt(dimensione);
			if(!(temp.contains(id))){
				temp.add(id);
			}
		}

		/*		//Create LABEL & INVARIANTS
		ArrayList<String> strLabelLine = new ArrayList<String>();
		ArrayList<String> strInvariantLine = new ArrayList<String>();

		try{
			FileInputStream streamLabel = new FileInputStream(fl);
			DataInputStream inLabel = new DataInputStream(streamLabel);
			BufferedReader brLabel = new BufferedReader(new InputStreamReader(inLabel));
			String slabel;
			//Read File Line By Line label.txt
			while ((slabel = brLabel.readLine()) != null)   {
				strLabelLine.add(slabel);
			}
			inLabel.close();

			FileInputStream streamInvariant = new FileInputStream(fi);
			DataInputStream inInvariant = new DataInputStream(streamInvariant);
			BufferedReader brInvariant = new BufferedReader(new InputStreamReader(inInvariant));
			String sinvariant;
			//Read File Line By Line invariant.txt
			while ((sinvariant = brInvariant.readLine()) != null){
				strInvariantLine.add(sinvariant);
			}
			inInvariant.close();

		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}*/

		ArrayList<String> strInvariantLine = new ArrayList<String>();

		//SubSet of LABELS
		lPerc = (lPerc * labels.size())/100;
		ArrayList<String> tempLabel = new ArrayList<String>();
		for(int lp=0; lp<lPerc; lp++){
			int idLabel = randomGenerator.nextInt(labels.size());
			while(tempLabel.contains(labels.get(idLabel))){
				idLabel = randomGenerator.nextInt(labels.size());
			}
			tempLabel.add(labels.get(idLabel));
		}

		//Create TRANSITIONS
		for(int i=0; i<dimensione-1; i++){
			int idRand = randomGenerator.nextInt(tempLabel.size());

			//controllo che prima di inserire tutti i label possibili non ne peschi degli uguali
			Transition[] t_control = efsa.getTransitions();
			ArrayList<String> tmp_label_trans = new ArrayList<String>();
			for(int id_t=0; id_t < t_control.length; id_t++){
				tmp_label_trans.add(t_control[id_t].getDescription());
			}
			while(tmp_label_trans.contains(tempLabel.get(idRand)) && tmp_label_trans.size() < tempLabel.size()){
				idRand = randomGenerator.nextInt(tempLabel.size());
			}
			trans[i] = new FSATransition(states[i], states[i+1], tempLabel.get(idRand));
			efsa.addTransition(trans[i]);
		}

		//creazione del resto delle transizioni
		while(efsa.getTransitions().length < numTrans){
			int sFrom = randomGenerator.nextInt(dimensione);
			int sTo = randomGenerator.nextInt(dimensione);
			int idLabelRand = randomGenerator.nextInt(tempLabel.size());

			if((efsa.getTransitionsFromState(states[sFrom])).length <= complessita) {
				trans[efsa.getTransitions().length] = new FSATransition(states[sFrom], states[sTo], tempLabel.get(idLabelRand));
				efsa.addTransition(trans[efsa.getTransitions().length]);
			}
		}

		//SubSet of INVARIANTS
		iPerc = (iPerc * strInvariantLine.size())/100;
		ArrayList<String> tempInvariant = new ArrayList<String>();
		for(int ip=0; ip<iPerc; ip++){
			int inv = randomGenerator.nextInt(strInvariantLine.size());
			while(tempInvariant.contains(strInvariantLine.get(inv))){
				inv = randomGenerator.nextInt(strInvariantLine.size());
			}
			tempInvariant.add(strInvariantLine.get(inv));
		}

		tPerc = (tPerc * efsa.getTransitions().length)/100;
		Transition[] tempTrans = efsa.getTransitions();
		ArrayList<Integer> check = new ArrayList<Integer>(tempTrans.length);
		for(int id=0; id < tempTrans.length; id++){
			check.add(id);
		}
		FSATransition[] labeledTransition = new FSATransition[efsa.getTransitions().length];

		for(int i=0; i<tPerc; i++){
			int randId = randomGenerator.nextInt(check.size());
			int idTrans = check.get(randId);
			check.remove(check.indexOf(idTrans));
			/*	int idInv = randomGenerator.nextInt(tempInvariant.size());			
			String[] tmp = tempInvariant.get(idInv).split("\" ");

			if(tmp.length>1){
				for (int j=0; j<tmp.length-1;j++){
					tmp[j] = tmp[j].concat("\"");
				}
			}
			String newLabel = tempTrans[idTrans].getDescription().concat("\n \n"+tmp[0]+"\n");
			for(int n=1; n < tmp.length; n++){
				newLabel = newLabel.concat(tmp[n]+"\n");
			}
			 */
			String newLabel = tempTrans[idTrans].getDescription();
			System.out.println("label: " + newLabel);
			labeledTransition[idTrans] = (FSATransition) tempTrans[idTrans];
			labeledTransition[idTrans].setLabel(newLabel);
		}

		return efsa;
	}  

/*	public void saveAutoma(FiniteStateAutomaton efsa){

		File fsaFile = new File (outFile);

		try {
			FileOutputStream file = new FileOutputStream(fsaFile);
			ObjectOutputStream oos = new ObjectOutputStream(file);
			oos.writeObject(efsa);
			oos.close();
			System.out.println("Automa Created!");
		} 
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}