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

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This class represents a single state in an automaton.  This class
 * is intended to act as nothing more than a simple placeholder.
 * 
 * @author Thomas Finley
 * @version 1.0
 */

public class State implements Serializable {
    /**
     * Instantiates a new state.
     * @param id the state id, used for generating
     * @param point the point that the center of the state will be at
     * in the canvas
     * @param automaton the automaton this belongs to
     */
    public State(int id, Point point, Automaton automaton) {
	this.point = point;
	this.id = id;
	this.automaton = automaton;
    }
    

    /**
     * Returns the point this state is centered on in the canvas.
     * @see #setPoint(Point)
     * @return the point this state is centered on in the canvas
     */
    public Point getPoint() {
	return point;
    }

    /**
     * Returns the automaton that this state belongs to.
     * @return the automaton that this state belongs to
     */
    public Automaton getAutomaton() {
	return automaton;
    }

    /**
     * Sets the point for this state.
     * @param point the point this state is moving to
     * @see #getPoint()
     */
    public void setPoint(Point point) {
	this.point = point;
	getAutomaton().distributeStateEvent
	    (new AutomataStateEvent(getAutomaton(), this, false, true, false));
    }

    /**
     * Returns the state ID for this state.
     * @return the ID of the state
     */
    public int getID() {
	return id;
    }

    /**
     * Sets the ID for this state.
     * @param id the new ID for this state.
     */
    protected void setID(int id) {
	if (("q"+this.id).equals(name)) name=null;
	this.id = id;
	getAutomaton().distributeStateEvent
	    (new AutomataStateEvent(getAutomaton(), this, false, false, true));
    }

    /**
     * Returns a string representation of this object.  The string
     * representation contains the ID and the point of the state.  If
     * the ID is <CODE>5</CODE> and the point object is at
     * <CODE>(50,80)</CODE>, then the string representation will be
     * </CODE>"q_5 at (50,80)"</CODE>
     */
    public String toString() {
	return "q_"+Integer.toString(getID())+
	    " at ("+Integer.toString(getPoint().x)+","+
	    Integer.toString(getPoint().y)+")" + " label: " + getLabel();
    }

    /**
     * Sets the name for this state.  A parameter of <CODE>null</CODE>
     * will reset this to the default.
     * @param name the new name for the state
     */
    public void setName(String name) {
	this.name = name;
	getAutomaton().distributeStateEvent
	    (new AutomataStateEvent(getAutomaton(), this, false, false, true));
    }

    /**
     * Returns the simple "name" for this state.  By default this will
     * simply be "qd", where d is the ID number.
     * @return the name for this state
     */
    public String getName() {
	if (name == null) {
	    name = "q"+Integer.toString(getID());
	    //digitizer(getID());
	}
	return name;
    }

    private String digitizer(int number) {
	if (number == 0)
	    return "\u2080";
	String s = digitizer(number/10, 1);
	return s + (SS+(char)(number%10));
    }
    private String digitizer(int number, int supp) {
	if (number == 0)
	    return "";
	String s = digitizer(number/10, 1);
	return s + (SS+(char)(number%10));
    }

    /**
     * Sets the "label" for a state, an optional description for the
     * state.
     * @param label the new descriptive label, or <CODE>null</CODE> if
     * the user wishes to specify that there is no label
     */
    public void setLabel(String label) {
	this.label = label;
	if (label == null) {
	    labels = new String[0];
	} else {
	    StringTokenizer st = new StringTokenizer(label, "\n");
	    ArrayList lines = new ArrayList();
	    while (st.hasMoreTokens())
		lines.add(st.nextToken());
	    labels = (String[]) lines.toArray(new String[0]);
	}
	getAutomaton().distributeStateEvent
	    (new AutomataStateEvent(getAutomaton(), this, false, false, true));
    }

    /**
     * Returns the label for the state.
     * @return the descriptive label of the state, or
     * <CODE>null</CODE> if this state has no label
     */
    public String getLabel() {
	return label;
    }

    /**
     * Returns the label for the state, broken across newlines if
     * there are newlines in it.
     * @return an array of all label elements, or an empty array if
     * this state has no labels
     */
    public String[] getLabels() {
	return labels;
    }

    /** The point where this state is to be drawn. */
    Point point;
    /** The state ID. */
    int id;
    /** The name of the state. */
    String name = null;
    /** The subscript unicode start point. */
    private static final char SS = '\u2080';
    /** The automaton this state belongs to. */
    private Automaton automaton = null;
    /** The label for the state. */
    private String label;
    /** If there are multiple labels, return those. */
    private String[] labels = new String[0];
}
