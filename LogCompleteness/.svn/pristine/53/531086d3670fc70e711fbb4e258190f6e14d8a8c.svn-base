package il.ac.tau.cs.smlab.fw.trace;

public class EventType {

	private String event;
	
	public EventType(String event) {
		this.event = event;
	}
	
	public String getEvent() {
		return event;
	}
	
	public String toString() {
		return event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() == String.class) {
			return event.equals((String) obj);
		}
		if (getClass() != obj.getClass())
			return false;
		EventType other = (EventType) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		return true;
	}
	
}
