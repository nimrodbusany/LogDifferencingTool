/* -- JFLAP 4.0 --
 *
 * Copyright information:
 *
 * Susan H. Rodger, Thomas Finley
 * Computer Science Department
 * Duke University
 * April 24, 2003
 * Supported by National Science Foundation DUE-9752583.
 *
 * Copyright (c) 2003
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the author.  The name of the author may not be used to
 * endorse or promote products derived from this software without
 * specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package il.ac.tau.cs.smlab.fsa.generator.automata.fsa;

import il.ac.tau.cs.smlab.fsa.generator.automata.Automaton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * This subclass of <CODE>Automaton</CODE> is specifically for a definition of
 * a regular Finite State Automaton.
 * 
 * @author Thomas Finley
 */

public class FiniteStateAutomaton extends Automaton {
	
	private final static long serialVersionUID = 5861270179276660246L;
	
	/**
	 * Creates a finite state automaton with no states and no transitions.
	 */
	public FiniteStateAutomaton() {
		super();
	}

	/**
	 * Returns the class of <CODE>Transition</CODE> this automaton must
	 * accept.
	 * 
	 * @return the <CODE>Class</CODE> object for <CODE>
	 *         automata.fsa.FSATransition</CODE>
	 */
	protected Class getTransitionClass() {
		return il.ac.tau.cs.smlab.fsa.generator.automata.fsa.FSATransition.class;
	}

	public static FiniteStateAutomaton readSerializedFSA(String fileName) throws ClassNotFoundException, FileNotFoundException, IOException {
		File toBeOpened = new File(fileName);
		if (!toBeOpened.exists()) {
			throw new FileNotFoundException();
		}

		ObjectInputStream in;
		FiniteStateAutomaton fsa = null;
		in = new ObjectInputStream(new FileInputStream(toBeOpened
				.getAbsolutePath()));
		fsa = (FiniteStateAutomaton) in.readObject();

		return fsa;
	}	

	public static FiniteStateAutomaton readSerializedFSA(InputStream inputStream) throws IOException, ClassNotFoundException {
		ObjectInputStream in;
		FiniteStateAutomaton fsa = null;
		in = new ObjectInputStream(inputStream);
		fsa = (FiniteStateAutomaton) in.readObject();

		return fsa;
	}
}