package il.ac.tau.cs.smlab.fsa.xml;

public class InvalidModelException extends Exception {

	private static final long serialVersionUID = 1L;


	public InvalidModelException() {
		super();
	}

	public InvalidModelException(String message) {
		super(message);
	}

	public InvalidModelException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidModelException(Throwable cause) {
		super(cause);
	}
}
