package il.ac.tau.cs.smlab.fsa.generator.traces.util;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * 
 * @author Anda Hoxhaj
 * 
 * Questa classe serve per assegnare ai parametri
 * dei valori scelti casualmente da un range. 
 *
 */

public class PickConstraintValue {

	static Hashtable<String, Integer> invariant;
	static String invTemp = "";

	public static ArrayList<String> solver(ArrayList<String> finalPaths){

		ArrayList<String> correctFinalPaths = new ArrayList<String>();

		//Percorre tutte le posizioni dell'ArrayList<String> finalPaths dove si trovano i path
		for(int i=0; i<finalPaths.size(); i++){

			String stmp = "";
			String[] result = finalPaths.get(i).split(" ");
			for(int j=0; j<result.length; j++){

				if(result[j].contains(">=") || result[j].contains("<=") || result[j].contains("==") || result[j].contains("<") || result[j].contains(">") || result[j].contains("=")){
					String[] subresult = result[j].split(":");
					String[] subsubresult = subresult[2].split(",");

					invTemp = "";
					invDeriveI(subsubresult);
					invDeriveII(subsubresult);

					subresult[2] = "";
					subresult[2] = invTemp;

					result[j] = "";
					for(int p=0; p<subresult.length-1; p++){
						result[j] = result[j].concat(subresult[p] + ":");
					}
					result[j] = result[j].concat(subresult[subresult.length-1]);

				}
			}

			for(int q=0; q<result.length-1; q++){
				stmp = stmp.concat(result[q] + " ");
			}
			stmp = stmp.concat(result[result.length-1]);
			correctFinalPaths.add(stmp);
		}

		return correctFinalPaths;
	}

	/**
	 * Crea la Hashtable per memmorizzare le coppie <parametro, valore> 
	 * degli invarianti che nella parte destra non hanno un altro parametro
	 * 
	 * @param subsubresult
	 */
	private static void invDeriveI(String[] subsubresult) {

		invariant = new Hashtable<String, Integer>();

		for(int n=0; n<subsubresult.length; n++){
			String[] temp = {};

			//tratta l'invarianti con operatore ">"
			if(subsubresult[n].contains(">") && !subsubresult[n].contains("=")){
				temp = subsubresult[n].split(">");

				//controlla che nella parte destra dell'operatore ci sia un numero
				if(temp[1].substring(0, 1).matches("[0-9]|-")){
					Integer MaxValue = SettingsManager.getMax(Integer.parseInt(temp[1]+1));
					int num = Integer.parseInt(temp[1])+1 + (int)(Math.random() * (MaxValue - Integer.parseInt(temp[1]) + 2));
					invariant.put(temp[0], num);

					//si controlla se si sta inserendo l'ultimo invariante del ciclo o meno
					if(invTemp.equals("")){
						invTemp = invTemp.concat(temp[0] + "=" + num);
					}else{
						invTemp = invTemp.concat("," + temp[0] + "=" + num);
					}
				}

				//tratta l'invarianti con operatore ">="
			}else if(subsubresult[n].contains(">=")){
				temp = subsubresult[n].split(">=");

				//controlla che nella parte destra dell'operatore ci sia un numero
				if(temp[1].substring(0, 1).matches("[0-9]|-")){
					Integer MaxValue = SettingsManager.getMax(Integer.parseInt(temp[1]));
					int num = Integer.parseInt(temp[1]) + (int)(Math.random() * (MaxValue - Integer.parseInt(temp[1]) + 1));
					invariant.put(temp[0], num);

					//si controlla se si sta inserendo l'ultimo invariante del ciclo o meno
					if(invTemp.equals("")){
						invTemp = invTemp.concat(temp[0] + "=" + num);
					}else{
						invTemp = invTemp.concat("," + temp[0] + "=" + num);
					}
				}	

				//tratta l'invarianti con operatore "<"
			}else if(subsubresult[n].contains("<") && !subsubresult[n].contains("=")){
				temp = subsubresult[n].split("<");

				//controlla che nella parte destra dell'operatore ci sia un numero
				if(temp[1].substring(0, 1).matches("[0-9]|-")){
					Integer MinValue = SettingsManager.getMin(Integer.parseInt(temp[1])-1);
					int num = MinValue + (int)(Math.random() * (Integer.parseInt(temp[1])-1 - MinValue + 1));
					invariant.put(temp[0], num);

					//si controlla se si sta inserendo l'ultimo invariante del ciclo o meno
					if(invTemp.equals("")){
						invTemp = invTemp.concat(temp[0] + "=" + num);
					}else{
						invTemp = invTemp.concat("," + temp[0] + "=" + num);
					}
				}

				//tratta l'invarianti con operatore "<="
			}else if(subsubresult[n].contains("<=")){
				temp = subsubresult[n].split("<=");

				//controlla che nella parte destra dell'operatore ci sia un numero
				if(temp[1].substring(0, 1).matches("[0-9]|-")){
					Integer MinValue = SettingsManager.getMin(Integer.parseInt(temp[1]));
					int num = MinValue + (int)(Math.random() * (Integer.parseInt(temp[1]) - MinValue + 1));
					invariant.put(temp[0], num);

					//si controlla se si sta inserendo l'ultimo invariante del ciclo o meno
					if(invTemp.equals("")){
						invTemp = invTemp.concat(temp[0] + "=" + num);
					}else{
						invTemp = invTemp.concat("," + temp[0] + "=" + num);
					}
				}

				//tratta l'invarianti con operatore "=="
			}else if(subsubresult[n].contains("==")){
				temp = subsubresult[n].split("==");

				//controlla che nella parte destra dell'operatore ci sia un numero
				if(temp[1].substring(0, 1).matches("[0-9]|-")){
					invariant.put(temp[0], Integer.parseInt(temp[1]));

					//si controlla se si sta inserendo l'ultimo invariante del ciclo o meno
					if(invTemp.equals("")){
						invTemp = invTemp.concat(temp[0] + "=" + Integer.parseInt(temp[1]));
					}else{
						invTemp = invTemp.concat("," + temp[0] + "=" + Integer.parseInt(temp[1]));
					}
				}

				//tratta l'invarianti con operatore "="
			}else if(subsubresult[n].contains("=")){
				temp = subsubresult[n].split("=");

				//controlla che nella parte destra dell'operatore ci sia un numero
				if(temp[1].substring(0, 1).matches("[0-9]|-")){
					invariant.put(temp[0], Integer.parseInt(temp[1]));

					//si controlla se si sta inserendo l'ultimo invariante del ciclo o meno
					if(invTemp.equals("")){
						invTemp = invTemp.concat(temp[0] + "=" + Integer.parseInt(temp[1]));
					}else{
						invTemp = invTemp.concat("," + temp[0] + "=" + Integer.parseInt(temp[1]));
					}
				}	
			}
		}
	}

	/**
	 * Risolve i casi quando nell'invariante compaiono dei parametri 
	 * contemporanamente sia nella parte sinistra che nella parte destra.
	 * @param subsubresult
	 */
	private static void invDeriveII(String[] subsubresult) {

		for(int n=0; n<subsubresult.length; n++){
			String[] temp = {};

			//tratta l'invarianti con operatore ">"
			if(subsubresult[n].contains(">") && !subsubresult[n].contains("=")){
				temp = subsubresult[n].split(">");

				//controlla che nella parte destra dell'operatore ci sia un parametro
				if(temp[1].substring(0, 1).matches("[a-zA-Z]")){

					//parametro a sinistra definito e a destra non definito
					if(invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){
						Integer MinValue = SettingsManager.getMin(invariant.get(temp[0])-1);
						int num = MinValue + (int)(Math.random() * (invariant.get(temp[0])-1 - MinValue + 1));
						invariant.put(temp[1], num);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[1] + "=" + num);
						}else{
							invTemp = invTemp.concat("," + temp[1] + "=" + num);
						}

						//parametro a sinistra non definito e a destra definito
					}else if(!invariant.containsKey(temp[0]) && invariant.containsKey(temp[1])){
						Integer MaxValue = SettingsManager.getMax(invariant.get(temp[1])+1);
						int num = invariant.get(temp[1])+1 + (int)(Math.random() * (MaxValue - invariant.get(temp[1]) + 2));
						invariant.put(temp[0], num);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + num);
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + num);
						}

						//nessuno dei due parametri definito
					}else if(!invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						int num1 = Integer.MIN_VALUE + (int)(Math.random() * (Integer.MAX_VALUE - Integer.MIN_VALUE + 1));
						invariant.put(temp[1], num1);

						Integer MaxValue = SettingsManager.getMax(invariant.get(temp[1])+1);
						int num2 = invariant.get(temp[1])+1 + (int)(Math.random() * (MaxValue - invariant.get(temp[1]) + 2));
						invariant.put(temp[0], num2);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}

					}
				}

				//tratta l'invarianti con operatore ">="
			}else if(subsubresult[n].contains(">=")){
				temp = subsubresult[n].split(">=");

				//controlla che nella parte destra dell'operatore ci sia un parametro
				if(temp[1].substring(0, 1).matches("[a-zA-Z]")){

					//parametro a sinistra definito e a destra non definito
					if(invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){
						Integer MinValue = SettingsManager.getMin(invariant.get(temp[0]));
						int num = MinValue + (int)(Math.random() * (invariant.get(temp[0]) - MinValue + 1));
						invariant.put(temp[1], num);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[1] + "=" + num);
						}else{
							invTemp = invTemp.concat("," + temp[1] + "=" + num);
						}

						//parametro a sinistra non definito e a destra definito
					}else if(!invariant.containsKey(temp[0]) && invariant.containsKey(temp[1])){
						Integer MaxValue = SettingsManager.getMax(invariant.get(temp[1]));
						int num = invariant.get(temp[1]) + (int)(Math.random() * (MaxValue - invariant.get(temp[1]) + 1));
						invariant.put(temp[0], num);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + num);
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + num);
						}

						//nessuno dei due parametri definito
					}else if(!invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						int num1 = Integer.MIN_VALUE + (int)(Math.random() * (Integer.MAX_VALUE - Integer.MIN_VALUE + 1));
						invariant.put(temp[1], num1);

						Integer MaxValue = SettingsManager.getMax(invariant.get(temp[1]));
						int num2 = invariant.get(temp[1]) + (int)(Math.random() * (MaxValue - invariant.get(temp[1]) + 1));
						invariant.put(temp[0], num2);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}
					}	

				}	

				/**tratta l'invarianti con operatore "<"**/
			}else if(subsubresult[n].contains("<") && !subsubresult[n].contains("=")){
				temp = subsubresult[n].split("<");

				//controlla che nella parte destra dell'operatore ci sia un parametro
				if(temp[1].substring(0, 1).matches("[a-zA-Z]")){

					//parametro a sinistra definito e a destra non definito
					if(invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						Integer MaxValue = SettingsManager.getMax(invariant.get(temp[0])+1);
						int num = invariant.get(temp[0])+1 + (int)(Math.random() * (MaxValue - invariant.get(temp[0]) + 2));
						invariant.put(temp[1], num);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[1] + "=" + num);
						}else{
							invTemp = invTemp.concat("," + temp[1] + "=" + num);
						}

						//parametro a sinistra non definito e a destra definito
					}else if(!invariant.containsKey(temp[0]) && invariant.containsKey(temp[1])){

						Integer MinValue = SettingsManager.getMin(invariant.get(temp[1])-1);
						int num = MinValue + (int)(Math.random() * (invariant.get(temp[1])-1 - MinValue + 1));
						invariant.put(temp[0], num);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + num);
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + num);
						}

						//nessuno dei due parametri definito
					}else if(!invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						int num1 = Integer.MIN_VALUE + (int)(Math.random() * (Integer.MAX_VALUE - Integer.MIN_VALUE + 1));
						invariant.put(temp[1], num1);

						Integer MinValue = SettingsManager.getMin(invariant.get(temp[1])-1);
						int num2 = MinValue + (int)(Math.random() * (invariant.get(temp[1])-1 - MinValue + 1));
						invariant.put(temp[0], num2);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}
					}

				}

				//tratta l'invarianti con operatore "<="
			}else if(subsubresult[n].contains("<=")){
				temp = subsubresult[n].split("<=");

				//controlla che nella parte destra dell'operatore ci sia un parametro
				if(temp[1].substring(0, 1).matches("[a-zA-Z]")){

					//parametro a sinistra definito e a destra non definito
					if(invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						Integer MaxValue = SettingsManager.getMax(invariant.get(temp[0]));
						int num = invariant.get(temp[0]) + (int)(Math.random() * (MaxValue - invariant.get(temp[0]) + 1));
						invariant.put(temp[1], num);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[1] + "=" + num);
						}else{
							invTemp = invTemp.concat("," + temp[1] + "=" + num);
						}

						//parametro a sinistra non definito e a destra definito
					}else if(!invariant.containsKey(temp[0]) && invariant.containsKey(temp[1])){

						Integer MinValue = SettingsManager.getMin(invariant.get(temp[1]));
						int num = MinValue + (int)(Math.random() * (invariant.get(temp[1])-1 - MinValue + 1));
						invariant.put(temp[0], num);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + num);
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + num);
						}

						//nessuno dei due parametri definito
					}else if(!invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						int num1 = Integer.MIN_VALUE + (int)(Math.random() * (Integer.MAX_VALUE - Integer.MIN_VALUE + 1));
						invariant.put(temp[1], num1);

						Integer MinValue = SettingsManager.getMin(invariant.get(temp[1]));
						int num2 = MinValue + (int)(Math.random() * (invariant.get(temp[1]) - MinValue + 1));
						invariant.put(temp[0], num2);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}
					}

				}

				//tratta l'invarianti con operatore "=="
			}else if(subsubresult[n].contains("==")){
				temp = subsubresult[n].split("==");

				//controlla che nella parte destra dell'operatore ci sia un parametro
				if(temp[1].substring(0, 1).matches("[a-zA-Z]")){

					//parametro a sinistra definito e a destra non definito
					if(invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						invariant.put(temp[1], invariant.get(temp[0]));

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[1] + "=" + invariant.get(temp[1]));
						}else{
							invTemp = invTemp.concat("," + temp[1] + "=" + invariant.get(temp[1]));
						}

						//parametro a sinistra non definito e a destra definito
					}else if(!invariant.containsKey(temp[0]) && invariant.containsKey(temp[1])){

						invariant.put(temp[0], invariant.get(temp[1]));

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + invariant.get(temp[0]));
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + invariant.get(temp[0]));
						}

						//nessuno dei due parametri definito	
					}else if(!invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						int num1 = Integer.MIN_VALUE + (int)(Math.random() * (Integer.MAX_VALUE - Integer.MIN_VALUE + 1));
						invariant.put(temp[1], num1);
						invariant.put(temp[0], num1);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}
					}

				}

				//tratta l'invarianti con operatore "="
			}else if(subsubresult[n].contains("=")){
				temp = subsubresult[n].split("=");

				//controlla che nella parte destra dell'operatore ci sia un parametro
				if(temp[1].substring(0, 1).matches("[a-zA-Z]")){

					//parametro a sinistra definito e a destra non definito
					if(invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						invariant.put(temp[1], invariant.get(temp[0]));

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[1] + "=" + invariant.get(temp[1]));
						}else{
							invTemp = invTemp.concat("," + temp[1] + "=" + invariant.get(temp[1]));
						}

						//parametro a sinistra non definito e a destra definito
					}else if(!invariant.containsKey(temp[0]) && invariant.containsKey(temp[1])){

						invariant.put(temp[0], invariant.get(temp[1]));

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + invariant.get(temp[0]));
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + invariant.get(temp[0]));
						}

						//nessuno dei due parametri definito	
					}else if(!invariant.containsKey(temp[0]) && !invariant.containsKey(temp[1])){

						int num1 = Integer.MIN_VALUE + (int)(Math.random() * (Integer.MAX_VALUE - Integer.MIN_VALUE + 1));
						invariant.put(temp[1], num1);
						invariant.put(temp[0], num1);

						//si controlla se si sta inserendo il primo invariante del ciclo o meno
						if(invTemp.equals("")){
							invTemp = invTemp.concat(temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}else{
							invTemp = invTemp.concat("," + temp[0] + "=" + invariant.get(temp[0]) + "," + temp[1] + "=" + invariant.get(temp[1]));
						}
					}

				}	
			}
		}	
	}

}
