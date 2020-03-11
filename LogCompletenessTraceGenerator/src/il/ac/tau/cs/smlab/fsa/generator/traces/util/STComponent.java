package il.ac.tau.cs.smlab.fsa.generator.traces.util;

import il.ac.tau.cs.smlab.fsa.generator.automata.State;
import il.ac.tau.cs.smlab.fsa.generator.automata.Transition;

import java.io.Serializable;

/**
 * Questa Classe rappresenta un oggetto che contiene 
 * lo stato e la transazione uscente da quello stato
 * 
 * @author Anda Hoxhaj
 *
 */
public class STComponent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static String ENDSTCOMPONENT = "endstcomponent";
	public static String ENDPATH = "endpath";
	
	private State state = null;
	private Transition transition = null;
	private boolean endSTComponent = false;
	private boolean endPath = false;
	
	//public STComponent(){}
	
	public STComponent(State state, Transition transition){
		this.state = state;
		this.transition = transition;
	}
	
	public STComponent(State state, Transition transition, boolean endSTComponent, boolean endPath){
		this.state = state;
		this.transition = transition;
		this.endPath = endPath;
		this.endSTComponent = endSTComponent;
	}
	
	//Costruttore usato per aggiungere la componente di marcatura di fine path nella matrixPath
	public STComponent(String flag){

		if(flag.equals(ENDSTCOMPONENT)){
			endSTComponent = true;
		}else{
			if(flag.equals(ENDPATH)){
				endPath = true;
			}else{
				try {
					throw new Exception("Wrong Flag for STComponent");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public State getState(){
		return this.state;
	}
	
	public Transition getTransition(){
		return this.transition;
	}
	
	public void setState(State state){
		this.state = state;
	}
	
	public void setTransition(Transition transition){
		this.transition = transition;
	}
	
	/**
	 * Ritorna true se l'oggetto STComponent ? un oggetto di fine percorso
	 * @return
	 */
	public boolean isEndSTComponent(){
		return this.endSTComponent;
	}
	
	/**
	 * Ritrona true se l'oggetto STComponent ? un oggetto di fine cammino
	 * @return
	 */
	public boolean isEndPath(){
		return this.endPath;
	}
	
}
