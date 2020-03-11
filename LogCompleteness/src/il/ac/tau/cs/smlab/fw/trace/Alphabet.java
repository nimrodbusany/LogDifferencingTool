package il.ac.tau.cs.smlab.fw.trace;

import java.util.LinkedList;
import java.util.List;

public class Alphabet {

	private List<EventType> alphabet = new LinkedList<EventType>();
	
	public Alphabet() {}
	
	public Alphabet(List<EventType> alphabet) {
		this.alphabet = alphabet;
	}
	
	public void addEvent(EventType event) {
		alphabet.add(event);
	}
	
	public void addEvent(String event) {
		alphabet.add(new EventType(event));
	}
	
	public List<EventType> getEventTypes() {
		return alphabet;
	}
	
	public int size() {
		return alphabet.size();
	}
	
	public boolean contains(String event) {
		return alphabet.contains(new EventType(event));
	}
	
	public String toString() {
		return alphabet.toString();
	}
	
	public Alphabet remove(List<String> l) {
		Alphabet ab = new Alphabet();
		for (EventType e : this.alphabet) {
			if (!l.contains(e.getEvent())) {
				// e is not in l
				ab.addEvent(e);
			}
		}
		return ab;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alphabet == null) ? 0 : alphabet.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alphabet other = (Alphabet) obj;
		if (alphabet == null) {
			if (other.alphabet != null)
				return false;
		} else if (!alphabet.equals(other.alphabet))
			return false;
		return true;
	}
}
