package il.ac.tau.cs.smlab.fsa.generator.traces.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;


/**
 * 
 * @author Anda Hoxhaj
 * Classe utilizzata per formattare e memorizzare le tracce
 * nel file finale .txt
 *
 */

public class CreateInputTraces {

	static String paramTemp = "";
	
	//Crea il file di input per Quark
	public static void create(ArrayList<String> matrixS, String fileName){

		ArrayList<String> finalPaths = new ArrayList<String>();
		for(int i=0;i<matrixS.size();i++){
			String temp = "";

			//divide il cammino nelle transizioni che la compongono
			String[] result = matrixS.get(i).split(" , ");
			for (int x=0; x<result.length; x++){

				if(result[x].length() == 1){
					temp = temp.concat(result[x].concat(":: "));

				}else if(result[x].length() == 2){
					temp = temp.concat(result[x].concat(": "));

				}else if(result[x].length() > 2){

					//divide la transizione nel metodo e nelle invarianti che la compongono
					String[] subResult = result[x].split(": |" + "\" ");
					for(int r=0; r<subResult.length; r++){
						//pulisce l'invariante dagli spazi e dalle virgolette
						subResult[r] = subResult[r].replaceAll(" |" + "\"", "");
					}
					temp = temp.concat(subResult[0] + ":");
					paramTemp = "";

					for(int j=1; j < subResult.length; j++){
						
						String[] subSubResultStrings = {};

						//divide l'invariante sull'operatore
						if(subResult[j].contains(">")){
							subSubResultStrings = subResult[j].split(">");
							
						}else if(subResult[j].contains("<")){
							subSubResultStrings = subResult[j].split("<");
							
						}else if(subResult[j].contains(">=")){
							subSubResultStrings = subResult[j].split(">=");
							
						}else if(subResult[j].contains("<=")){
							subSubResultStrings = subResult[j].split("<=");
							
						}else if(subResult[j].contains("==")){
							subSubResultStrings = subResult[j].split("==");
							
						}else if(subResult[j].contains("=")){
							subSubResultStrings = subResult[j].split("=");
						}

						if(j < subResult.length-1){

							if(!paramTemp.contains(subSubResultStrings[0]+",")){
								paramTemp = paramTemp.concat(subSubResultStrings[0].concat(","));
							}

							//controlla se nella parte destra dell'invariante c'? un'altro parametro
							if(subSubResultStrings[1].substring(0, 1).matches("[a-zA-Z]") && !paramTemp.contains(subSubResultStrings[1]+",")){
								paramTemp = paramTemp.concat(subSubResultStrings[1].concat(","));
							}
						}else{
							//controlla se nella parte destra del ultimo invariante c'? un'altro parametro
							if(subSubResultStrings[1].substring(0, 1).matches("[a-zA-Z]") && !paramTemp.contains(subSubResultStrings[1]+",")){
								paramTemp = paramTemp.concat(subSubResultStrings[1]);//.concat("," + subSubResultStrings[1] + ":"));

								if(!paramTemp.contains(subSubResultStrings[0]+",")){
									paramTemp = paramTemp.concat("," + subSubResultStrings[0] + ":");
								}else{
									paramTemp = paramTemp.concat(":");
								}
							}else if(!paramTemp.contains(subSubResultStrings[0]+",")){
								paramTemp = paramTemp.concat(subSubResultStrings[0].concat(":"));
							}

						}

					}
					if(!paramTemp.endsWith(":")){
						paramTemp = paramTemp.concat(":");
					}
					temp = temp.concat(paramTemp);
					for(int j=1; j < subResult.length; j++){
						if(j < subResult.length-1){
							temp = temp.concat(subResult[j].concat(","));
						}else if(x == result.length-1){
							temp = temp.concat(subResult[j]);
						}else{
							temp = temp.concat(subResult[j].concat(" "));
						}
					}
				}
			}
			finalPaths.add(temp);
		}

		//writeFile(fileName, finalPaths);
		
		ArrayList<String> finalPathsInvCorrect = new ArrayList<String>();
		finalPathsInvCorrect = PickConstraintValue.solver(finalPaths);

		//write to file
		writeFile(fileName, finalPathsInvCorrect);

	}

	/**
	 * Scrive le tracce sul file .txt
	 * @param fileName
	 * @param finalPathsInvCorrect
	 */
	private static void writeFile(String fileName, ArrayList<String> finalPathsInvCorrect){

		//System.out.println("Inizio scrittura file input per Quark...");

		try {
			FileOutputStream file = new FileOutputStream(fileName);
			PrintStream Output = new PrintStream(file);

			for(int i=0; i<finalPathsInvCorrect.size(); i++){
				Output.print(finalPathsInvCorrect.get(i)+"\r\n");
			}
			
			file.close();

		} catch (IOException e) {
			System.out.println("Errore Scrittura file di Quark: " + e);
			System.exit(1);
		}
	//	System.out.println("Fine scrittura file!");	
	//	System.out.println("Ci sono: " +finalPathsInvCorrect.size()+ " tracce nel file: " +fileName);

	}

}
