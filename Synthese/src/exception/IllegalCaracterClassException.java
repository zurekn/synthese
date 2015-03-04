package exception;

public class IllegalCaracterClassException extends Exception {
	public IllegalCaracterClassException(String message) {
		super();
		System.err.println(message);
	}
}