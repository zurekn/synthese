package exception;

public class IllegalMovementException extends Exception {
	public IllegalMovementException(String message){
		super();
		System.err.println(message);
	}
}
