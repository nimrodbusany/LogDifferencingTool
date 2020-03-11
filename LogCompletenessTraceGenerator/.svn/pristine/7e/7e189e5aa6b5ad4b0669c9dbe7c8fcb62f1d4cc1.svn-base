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
 
package il.ac.tau.cs.smlab.fsa.generator.automata.event;

import il.ac.tau.cs.smlab.fsa.generator.automata.Automaton;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;

import java.util.EventObject;

/**
 * This event is given to listeners of an automaton interested in
 * events when a transition on an automaton is added or removed.
 * @see automata.Automaton
 * @see automata.Transition
 * @see automata.Automaton#addTransition
 * @see automata.Automaton#removeTransition
 * @see automata.event.AutomataTransitionListener
 * 
 * @author Thomas Finley
 */

public class AutomataTransitionEvent extends EventObject {
    /**
     * Instantiates a new <CODE>AutomataStateEvent</CODE>.
     * @param auto the <CODE>Automaton</CODE> that generated the event
     * @param state the <CODE>State</CODE> that was added or removed
     * @param add <CODE>true</CODE> if state added, <CODE>false</CODE>
     * if removed
     */
    public AutomataTransitionEvent(Automaton auto, Transition transition,
				   boolean add, boolean change) {
	super(auto);
	myTransition = transition;
	myAdd = add;
	myChange = change;
    }

    /**
     * Returns the <CODE>Automaton</CODE> that generated this event.
     * @return the <CODE>Automaton</CODE> that generated this event
     */
    public Automaton getAutomaton() {
	return (Automaton) getSource();
    }

    /**
     * Returns the <CODE>Transition</CODE> that was added/removed.
     * @return the <CODE>Transition</CODE> that was added/removed
     */
    public Transition getTransition() {
	return myTransition;
    }

    /**
     * Returns if this was an add.
     * @return <CODE>true</CODE> if this event indicates the addition
     * of a transition, <CODE>false</CODE> otherwise
     */
    public boolean isAdd() {
	return myAdd;
    }

    /**
     * Returns if this was a delete.
     * @return <CODE>true</CODE> if this event indicates the removal
     * of a transition, <CODE>false</CODE> otherwise
     */
    public boolean isDelete() {
	return !(myAdd || myChange);
    }

    /**
     * Returns if this was a simple change in a property of the
     * transition.
     * @return <CODE>true</CODE> if the properties of this transition
     * were changed, <CODE>false</CODE> otherwise
     */
    public boolean isChange() {
	return myChange;
    }
    
    /** Was this an add? */
    private boolean myAdd;
    /** Which transition did we add/remove? */
    private Transition myTransition;
    /** Is this a change in property? */
    private boolean myChange;
}
