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
 
package il.ac.tau.cs.smlab.fsa.generator.automata;

/**
 * This class is an exception that is thrown in the event an
 * incompatible <CODE>Transition</CODE> object is assigned to an
 * automaton.
 * @see automata.Automaton
 * @see automata.Transition
 * @see automata.Automaton#getTransitionClass
 * @see automata.Automaton#addTransition
 * @author Thomas Finley
 */

public class IncompatibleTransitionException extends RuntimeException {
    
}
