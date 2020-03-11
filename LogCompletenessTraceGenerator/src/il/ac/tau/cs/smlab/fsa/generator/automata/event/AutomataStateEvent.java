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
import il.ac.tau.cs.smlab.fsa.generator.automata.State;

import java.util.EventObject;

/**
 * This event is given to listeners of an automaton interested in
 * events when a state on an automaton is added or removed, or moved,
 * or it's label was changed, etc.
 * @see automata.Automaton
 * @see automata.State
 * @see automata.Automaton#addState
 * @see automata.Automaton#removeState
 * @see automata.event.AutomataStateListener
 * 
 * @author Thomas Finley
 */

public class AutomataStateEvent extends EventObject {
    /**
     * Instantiates a new <CODE>AutomataStateEvent</CODE>.
     * @param auto the <CODE>Automaton</CODE> that generated the event
     * @param state the <CODE>State</CODE> that was added or removed
     * @param add <CODE>true</CODE> if state added
     * @param move <CODE>true</CODE> if the state was merely moved
     * @param label <CODE>true</CODE> if the state was only changed in
     * such a fashion as
     */
    public AutomataStateEvent(Automaton auto, State state, boolean add,
			      boolean move, boolean label) {
	super(auto);
	myState = state;
	myAdd = add;
	myMove = move;
	myLabel = label;
    }

    /**
     * Returns the <CODE>Automaton</CODE> that generated this event.
     * @return the <CODE>Automaton</CODE> that generated this event
     */
    public Automaton getAutomaton() {
	return (Automaton) getSource();
    }

    /**
     * Returns the <CODE>State</CODE> that was added/removed.
     * @return the <CODE>State</CODE> that was added/removed
     */
    public State getState() {
	return myState;
    }

    /**
     * Returns if this was an add.
     * @return <CODE>true</CODE> if this event indicates the addition
     * of a state, <CODE>false</CODE> otherwise
     */
    public boolean isAdd() {
	return myAdd;
    }

    /**
     * Returns if this was a move.
     * @return <CODE>true</CODE> if this event indicates the mere
     * moving of a state, <CODE>false</CODE> otherwise
     */
    public boolean isMove() {
	return myMove;
    }

    /**
     * Returns if this was a label change.
     * @return <CODE>true</CODE> if this event indicates the mere
     * relabeling of a state, <CODE>false</CODE> otherwise
     */
    public boolean isLabel() {
	return myLabel;
    }

    /**
     * Returns if this was a delete of a state.
     * @return <CODE>true</CODE> if this event was the deletion of a
     * state, false otherwise
     */
    public boolean isDelete() {
	return !(myMove || myAdd || myLabel);
    }
    
    /** Was this an add? */
    private boolean myAdd;
    /** Was this a move? */
    private boolean myMove;
    /** Was the label for the state changed? */
    public boolean myLabel;
    /** Which state did we add/remove? */
    private State myState;
}
