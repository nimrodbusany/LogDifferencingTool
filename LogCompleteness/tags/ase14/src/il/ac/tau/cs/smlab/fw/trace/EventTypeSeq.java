package il.ac.tau.cs.smlab.fw.trace;

import java.util.Arrays;
import java.util.List;

public class EventTypeSeq {

	private List<EventType> events;

	public List<EventType> getEvents() {
		return events;
	}

	public void setEvents(List<EventType> events) {
		this.events = events;
	}
	
	public EventTypeSeq(EventType... events) {
		this.events = Arrays.asList(events);
	}
	
	public EventTypeSeq(List<EventType> events) {
		this.events = events;
	}
	
	public String toString() {
		return events.toString();
	}
	
	public EventType getEvent(int index) {
		return events.get(index);
	}
	
	public int size() {
		return events.size();
	}

	// true iff this starts with seq
	public boolean prefix(EventTypeSeq seq) {
		if (events.size() < seq.size()) return false;
		return events.subList(0, seq.size()).equals(seq.getEvents());
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((events == null) ? 0 : events.hashCode());
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
		EventTypeSeq other = (EventTypeSeq) obj;
		if (events == null) {
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		return true;
	}
}
