package il.ac.tau.cs.smlab.fsa.generator.traces.util;

import il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FiniteStateAutomaton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import xmlConverter.tools.efsa2xml.codec.api.EFSACodec;

public class EFSASer{

	public final static EFSASer INSTANCE = new EFSASer();

	private EFSASer(){

	}

	/**
	 * Loads a FSA object from a Serialized object
	 * @param filename
	 * @return the FSA
	 */
	public FiniteStateAutomaton loadEFSA(String filename) throws IOException, ClassNotFoundException{
		FiniteStateAutomaton res = null;  
		ObjectInputStream in = null;
		
		try {
			in = new ObjectInputStream(new FileInputStream(filename));
			res = (FiniteStateAutomaton)in.readObject();
			
		}finally{
			if ( in != null ){
				in.close();
			}
		}
		return res;
	}

	public FiniteStateAutomaton loadEFSA(File file) throws IOException, ClassNotFoundException{    
		return loadEFSA(file.getAbsolutePath());
	}

	/**
	 * Saves a FSA object in a Serialized object format
	 * @param fsa
	 * @param filename
	 * @return
	 */
	public void saveEFSA(FiniteStateAutomaton efsa, File file) throws FileNotFoundException, IOException{
		saveEFSA(efsa, file.getAbsolutePath());
	}

	public void saveEFSA(FiniteStateAutomaton efsa, String filename) throws FileNotFoundException, IOException{
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(filename));
			out.writeObject(efsa);
		} finally {
			if ( out != null ){
				out.close();
			}
		}
	}
}
