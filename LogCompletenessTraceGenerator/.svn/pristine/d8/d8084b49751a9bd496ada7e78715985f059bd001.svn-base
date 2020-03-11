package il.ac.tau.cs.smlab.fsa.generator.traces.util;

import java.util.ArrayList;
import java.util.Vector;

/**
 * 
 * @author Anda Hoxhaj
 * 
 * Questa classe serve per convertire la matrice 
 * Vector<Vector<STComponent>> che contiene le tracce in una
 * matrice ArrayList<String>.
 *
 */

public class Parsing {

	public static ArrayList<String> parse(Vector<Vector<STComponent>> matrix, int position){

		ArrayList<String> matrixS = new ArrayList<String>();
		for(int i=0; i < matrix.size(); i++){
			String temp = "";
			for(int j=0; j < matrix.get(i).size()-position; j++){
				String[] clear = matrix.get(i).get(j).getTransition().getDescription().toString().split("\n ");

				if(clear.length > 1){
					temp = temp.concat(clear[0] + ": ");
					String[] result = clear[1].split("\n");
					for(int c=0; c<result.length; c++){
						if(!result[c].contains("one of") 
								&& !result[c].contains("has only one") 
								&& !result[c].contains("returnValue") 
								&& !result[c].contains("(param") 
								&& !result[c].contains("!=")){
							temp = temp.concat(result[c] + " ");
						}
					}
					if(temp.endsWith(":  ")){
						temp = temp.substring(0, temp.length()-3).concat(" ");
					}

				}else {
					temp = temp.concat(clear[0] + " ");
				}
				temp = temp.concat(", ");

			}
			matrixS.add(temp);
		}
		return matrixS;
	}

}
