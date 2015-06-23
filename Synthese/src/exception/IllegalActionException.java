package exception;

public class IllegalActionException extends Exception {
	public IllegalActionException(String message) {
		super(message);
		System.err.println(message);
	}
}