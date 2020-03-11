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

import il.ac.tau.cs.smlab.fsa.generator.automata.event.AutomataStateEvent;
import il.ac.tau.cs.smlab.fsa.generator.automata.event.AutomataStateListener;
import il.ac.tau.cs.smlab.fsa.generator.automata.event.AutomataTransitionEvent;
import il.ac.tau.cs.smlab.fsa.generator.automata.event.AutomataTransitionListener;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The automata object is the root class for the representation of all forms of
 * automata, including FSA, PDA, and Turing machines. This object does NOT
 * simulate the behavior of any of those machines; it simply maintains a
 * structure that holds and maintains the data necessary to represent such a
 * machine.
 * 
 * @see automata.State
 * @see automata.Transition
 * 
 * @author Thomas Finley
 */

public class Automaton implements Serializable, Cloneable {
	/**
	 * Creates an instance of <CODE>Automaton</CODE>. The created instance
	 * has no states and no transitions.
	 */
	public Automaton() {
		states = new HashSet();
		transitions = new HashSet();
		finalStates = new HashSet();
		initialState = null;
	}

	/**
	 * Creates a clone of this automaton.
	 * 
	 * @return a clone of this automaton, or <CODE>null</CODE> if the clone
	 *         failed
	 */
	public Object clone() {
		Automaton a;
		// Try to create a new object.
		try {
				a = (Automaton) getClass().newInstance();
		} catch (Throwable e) {
			// Well golly, we're sure screwed now!
			System.err.println("Warning: clone of automaton failed!");
			return null;
		}

		// Copy over the states.
		HashMap map = new HashMap(); // Old states to new states.
		Iterator it = states.iterator();
		while (it.hasNext()) {
			State state = (State) it.next();
			State nstate = new State(state.getID(),
					new Point(state.getPoint()), a);
			nstate.setLabel(state.getLabel());
			nstate.setName(state.getName());
			map.put(state, nstate);
			a.addState(nstate);
		}
		// Set special states.
		it = finalStates.iterator();
		while (it.hasNext()) {
			State state = (State) it.next();
			a.addFinalState((State) map.get(state));
		}
		a.setInitialState((State) map.get(getInitialState()));

		// Copy over the transitions.
		it = states.iterator();
		while (it.hasNext()) {
			State state = (State) it.next();
			Transition[] ts = getTransitionsFromState(state);
			State from = (State) map.get(state);
			for (int i = 0; i < ts.length; i++) {
				State to = (State) map.get(ts[i].getToState());
				a.addTransition(ts[i].copy(from, to));
			}
		}

		// Should be done now!
		return a;
	}

	/**
	 * Retrieves all transitions that eminate from a state.
	 * 
	 * @param from
	 *            the <CODE>State</CODE> from which returned transitions
	 *            should come from
	 * @return an array of the <CODE>Transition</CODE> objects emanating from
	 *         this state
	 */
	public Transition[] getTransitionsFromState(State from) {
		Transition[] toReturn = (Transition[]) transitionArrayFromStateMap
				.get(from);
		if (toReturn == null) {
			List list = (List) transitionFromStateMap.get(from);
			toReturn = (Transition[]) list.toArray(new Transition[0]);
			transitionArrayFromStateMap.put(from, toReturn);
		}
		return toReturn;
	}

	/**
	 * Retrieves all transitions that travel from a state.
	 * 
	 * @param to
	 *            the <CODE>State</CODE> to which all returned transitions
	 *            should go to
	 * @return an array of all <CODE>Transition</CODE> objects going to the
	 *         State
	 */
	public Transition[] getTransitionsToState(State to) {
		Transition[] toReturn = (Transition[]) transitionArrayToStateMap
				.get(to);
		if (toReturn == null) {
			List list = (List) transitionToStateMap.get(to);
			toReturn = (Transition[]) list.toArray(new Transition[0]);
			transitionArrayToStateMap.put(to, toReturn);
		}
		return toReturn;
	}

	/**
	 * Retrieves all transitions going from one given state to another given
	 * state.
	 * 
	 * @param from
	 *            the state all returned transitions should come from
	 * @param to
	 *            the state all returned transitions should go to
	 * @return an array of all transitions coming from <CODE>from</CODE> and
	 *         going to <CODE>to</CODE>
	 */
	public Transition[] getTransitionsFromStateToState(State from, State to) {
		Transition[] t = getTransitionsFromState(from);
		ArrayList list = new ArrayList();
		for (int i = 0; i < t.length; i++)
			if (t[i].getToState() == to)
				list.add(t[i]);
		return (Transition[]) list.toArray(new Transition[0]);
	}

	/**
	 * Retrieves all transitions.
	 * 
	 * @return an array containing all transitions for this automaton
	 */
	public Transition[] getTransitions() {
		if (cachedTransitions == null)
			cachedTransitions = (Transition[]) transitions
					.toArray(new Transition[0]);
		return cachedTransitions;
	}

	/**
	 * Adds a <CODE>Transition</CODE> to this automaton. This method may do
	 * nothing if the transition is already in the automaton.
	 * 
	 * @param trans
	 *            the transition object to add to the automaton
	 */
	public void addTransition(Transition trans) {
		if (!getTransitionClass().isInstance(trans)) {
			throw (new IncompatibleTransitionException());
		}
		if (transitions.contains(trans))
			return;
		transitions.add(trans);
		List list = (List) transitionFromStateMap.get(trans.getFromState());		
		list.add(trans);
		list = (List) transitionToStateMap.get(trans.getToState());
		list.add(trans);
		transitionArrayFromStateMap.remove(trans.getFromState());
		transitionArrayToStateMap.remove(trans.getToState());
		cachedTransitions = null;

		distributeTransitionEvent(new AutomataTransitionEvent(this, trans, true, false));
	}

	/**
	 * Removes a <CODE>Transition</CODE> from this automaton.
	 * 
	 * @param trans
	 *            the transition object to remove from this automaton.
	 */
	public void removeTransition(Transition trans) {
		transitions.remove(trans);
		List l = (List) transitionFromStateMap.get(trans.getFromState());
		l.remove(trans);
		l = (List) transitionToStateMap.get(trans.getToState());
		l.remove(trans);
		// Remove cached arrays.
		transitionArrayFromStateMap.remove(trans.getFromState());
		transitionArrayToStateMap.remove(trans.getToState());
		cachedTransitions = null;

		distributeTransitionEvent(new AutomataTransitionEvent(this, trans,
				false, false));
	}

	/**
	 * Creates a state, inserts it in this automaton, and returns that state.
	 * The ID for the state is set appropriately.
	 * 
	 * @param point
	 *            the point to put the state at
	 */
	public final State createState(Point point) {
		int i = 0;
		while (getStateWithID(i) != null)
			i++;
		State state = new State(i, point, this);
		addState(state);
		return state;
	}

	/**
	 * Adds a new state to this automata. Clients should use the
	 * <CODE>createState</CODE> method instead.
	 * 
	 * @param state
	 *            the state to add
	 */
	protected final void addState(State state) {
		states.add(state);
		transitionFromStateMap.put(state, new LinkedList());
		transitionToStateMap.put(state, new LinkedList());
		cachedStates = null;
		distributeStateEvent(new AutomataStateEvent(this, state, true, false,
				false));
	}

	/**
	 * Removes a state from the automaton. This will also remove all transitions
	 * associated with this state.
	 * 
	 * @param state
	 *            the state to remove
	 */
	public void removeState(State state) {
		Transition[] t = getTransitionsFromState(state);
		for (int i = 0; i < t.length; i++)
			removeTransition(t[i]);
		t = getTransitionsToState(state);
		for (int i = 0; i < t.length; i++)
			removeTransition(t[i]);
		distributeStateEvent(new AutomataStateEvent(this, state, false, false,
				false));
		states.remove(state);
		finalStates.remove(state);
		if (state == initialState)
			initialState = null;

		transitionFromStateMap.remove(state);
		transitionToStateMap.remove(state);

		transitionArrayFromStateMap.remove(state);
		transitionArrayToStateMap.remove(state);

		cachedStates = null;
	}

	/**
	 * Sets the new initial state to <CODE>initialState</CODE> and returns
	 * what used to be the initial state, or <CODE>null</CODE> if there was no
	 * initial state. The state specified should already exist in the automata.
	 * 
	 * @param initialState
	 *            the new initial state
	 * @return the old initial state, or <CODE>null</CODE> if there was no
	 *         initial state
	 */
	public State setInitialState(State initialState) {
		State oldInitialState = this.initialState;
		this.initialState = initialState;
		return oldInitialState;
	}

	/**
	 * Returns the start state for this automaton.
	 * 
	 * @return the start state for this automaton
	 */
	public State getInitialState() {
		return this.initialState;
	}

	/**
	 * Returns an array that contains every state in this automaton. The array
	 * is not necessarily gauranteed to be in any particular order.
	 * 
	 * @return an array containing all the states in this automaton
	 */
	public State[] getStates() {
		if (cachedStates == null)
			cachedStates = (State[]) states.toArray(new State[0]);
		return cachedStates;
	}

	/**
	 * Adds a single final state to the set of final states. Note that the
	 * general automaton can have an unlimited number of final states, and
	 * should have at least one. The state that is added should already be one
	 * of the existing states.
	 * 
	 * @param finalState
	 *            a new final state to add to the collection of final states
	 */
	public void addFinalState(State finalState) {
		cachedFinalStates = null;
		finalStates.add(finalState);
	}

	/**
	 * Removes a state from the set of final states. This will not remove a
	 * state from the list of states; it shall merely make it nonfinal.
	 * 
	 * @param state
	 *            the state to make not a final state
	 */
	public void removeFinalState(State state) {
		cachedFinalStates = null;
		finalStates.remove(state);
	}

	/**
	 * Returns an array that contains every state in this automaton that is a
	 * final state. The array is not necessarily gauranteed to be in any
	 * particular order.
	 * 
	 * @return an array containing all final states of this automaton
	 */
	public State[] getFinalStates() {
		if (cachedFinalStates == null)
			cachedFinalStates = (State[]) finalStates.toArray(new State[0]);
		return cachedFinalStates;
	}

	/**
	 * Determines if the state passed in is in the set of final states.
	 * 
	 * @param state
	 *            the state to determine if is final
	 * @return <CODE>true</CODE> if the state is a final state in this
	 *         automaton, <CODE>false</CODE> if it is not
	 */
	public boolean isFinalState(State state) {
		return finalStates.contains(state);
	}

	/**
	 * Returns the <CODE>State</CODE> in this automaton with this ID.
	 * 
	 * @param id
	 *            the ID to look for
	 * @return the instance of <CODE>State</CODE> in this automaton with this
	 *         ID, or <CODE>null</CODE> if no such state exists
	 */
	public State getStateWithID(int id) {
		Iterator it = states.iterator();
		while (it.hasNext()) {
			State state = (State) it.next();
			if (state.getID() == id)
				return state;
		}
		return null;
	}

	/**
	 * Tells if the passed in object is indeed a state in this automaton.
	 * 
	 * @param state
	 *            the state to check for membership in the automaton
	 * @return <CODE>true</CODE> if this state is in the automaton,
	 *         <CODE>false</CODE>otherwise
	 */
	public boolean isState(State state) {
		return states.contains(state);
	}

	/**
	 * Returns the particular class that added transition objects should be a
	 * part of. Subclasses may wish to override in case they want to restrict
	 * the type of transitions their automaton will respect. By default this
	 * method simply returns the class object for the abstract class
	 * <CODE>automata.Transition</CODE>.
	 * 
	 * @see #addTransition
	 * @see automata.Transition
	 * @return the <CODE>Class</CODE> object that all added transitions should
	 *         derive from
	 */
	protected Class getTransitionClass() {
		return Transition.class;
	}

	/**
	 * Returns a string representation of this <CODE>Automaton</CODE>.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append('\n');
		State[] states = getStates();
		for (int s = 0; s < states.length; s++) {
			if (initialState == states[s])
				buffer.append("--> ");
			buffer.append(states[s]);
			if (isFinalState(states[s]))
				buffer.append(" **FINAL**");
			buffer.append('\n');
			Transition[] transitions = getTransitionsFromState(states[s]);
			for (int t = 0; t < transitions.length; t++) {
				buffer.append('\t');
				buffer.append(transitions[t]);
				buffer.append('\n');
			}
		}

		return buffer.toString();
	}

	/**
	 * Adds a <CODE>AutomataStateListener</CODE> to this automata.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addStateListener(AutomataStateListener listener) {
		stateListeners.add(listener);
	}

	/**
	 * Adds a <CODE>AutomataTransitionListener</CODE> to this automata.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addTransitionListener(AutomataTransitionListener listener) {
		transitionListeners.add(listener);
	}

	/**
	 * Gives an automata state change event to all state listeners.
	 * 
	 * @param event
	 *            the event to distribute
	 */
	void distributeStateEvent(AutomataStateEvent event) {
		Iterator it = stateListeners.iterator();
		while (it.hasNext()) {
			AutomataStateListener listener = (AutomataStateListener) it.next();
			listener.automataStateChange(event);
		}
	}

	/**
	 * Removes a <CODE>AutomataStateListener</CODE> from this automata.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeStateListener(AutomataStateListener listener) {
		stateListeners.remove(listener);
	}

	/**
	 * Removes a <CODE>AutomataTransitionListener</CODE> from this automata.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeTransitionListener(AutomataTransitionListener listener) {
		transitionListeners.remove(listener);
	}

	/**
	 * Gives an automata transition change event to all transition listeners.
	 * 
	 * @param event
	 *            the event to distribute
	 */
	void distributeTransitionEvent(AutomataTransitionEvent event) {
		Iterator it = transitionListeners.iterator();
		while (it.hasNext()) {
			AutomataTransitionListener listener = (AutomataTransitionListener) it
					.next();
			listener.automataTransitionChange(event);
		}
	}

	/**
	 * Indicates that a transition has changed. Transitions should call this
	 * method on the automaton to register changes in their parameters.
	 * 
	 * @param transition
	 *            the transition that changed
	 */
	public void transitionChanged(Transition transition) {
		distributeTransitionEvent(new AutomataTransitionEvent(this, transition,
				false, true));
	}

	/**
	 * This handles deserialization so that the listener sets are reset to avoid
	 * null pointer exceptions when one tries to add listeners to the object.
	 * 
	 * @param in
	 *            the input stream for the object
	 */
	private void readObject(java.io.ObjectInputStream in)
			throws java.io.IOException, ClassNotFoundException {
		// Reset all nonread objects.
		transitionListeners = new HashSet();
		stateListeners = new HashSet();
		transitionFromStateMap = new HashMap();
		transitionToStateMap = new HashMap();
		transitionArrayFromStateMap = new HashMap();
		transitionArrayToStateMap = new HashMap();
		transitions = new HashSet();
		states = new HashSet();

		// Do the reading in of objects.
		int version = in.readInt();
		if (version >= 0) { // Adjust by version.
			// The reading for version 0 of this object.
			Set s = (Set) in.readObject();
			Iterator it = s.iterator();
			while (it.hasNext())
				addState((State) it.next());
			initialState = (State) in.readObject();
			finalStates = (Set) in.readObject();
			// Let the class take care of the transition stuff.
			Set trans = (Set) in.readObject();
			it = trans.iterator();
			while (it.hasNext())
				addTransition((Transition) it.next());

		}
		if (version >= 1) {

		}
		while (!in.readObject().equals("SENT"))
			; // Read until sentinel.
	}

	/**
	 * This handles serialization.
	 */
	private void writeObject(java.io.ObjectOutputStream out)
			throws java.io.IOException {
		out.writeInt(0); // Version of the stream.
		// Version 0 outstuff...
		out.writeObject(states);
		out.writeObject(initialState);
		out.writeObject(finalStates);
		out.writeObject(transitions);
		out.writeObject("SENT"); // The sentinel object.
	}

	// AUTOMATA SPECIFIC CRAP
	// This includes lots of stuff not strictly necessary for the
	// defintion of automata, but stuff that makes it at least
	// somewhat efficient in the process.

	/** The collection of states in this automaton. */
	private Set states;
	/** The cached array of states. */
	private State[] cachedStates = null;
	/** The cached array of transitions. */
	private Transition[] cachedTransitions = null;
	/** The cached array of final states. */
	private State[] cachedFinalStates = null;

	/**
	 * The collection of final states in this automaton. This is a subset of the
	 * "states" collection.
	 */
	private Set finalStates;
	/** The initial state. */
	private State initialState = null;

	/** The list of transitions in this automaton. */
	private Set transitions;
	/**
	 * A mapping from states to a list holding transitions from those states.
	 */
	private HashMap transitionFromStateMap = new HashMap();
	/**
	 * A mapping from state to a list holding transitions to those states.
	 */
	private HashMap transitionToStateMap = new HashMap();
	/**
	 * A mapping from states to an array holding transitions from a state. This
	 * is a sort of cashing.
	 */
	private HashMap transitionArrayFromStateMap = new HashMap();
	/**
	 * A mapping from states to an array holding transitions from a state. This
	 * is a sort of cashing.
	 */
	private HashMap transitionArrayToStateMap = new HashMap();

	// LISTENER STUFF
	// Structures related to this object as something that generates
	// events, in particular as it pertains to the removal and
	// addition of states and transtions.
	private transient HashSet transitionListeners = new HashSet();
	private transient HashSet stateListeners = new HashSet();
}
