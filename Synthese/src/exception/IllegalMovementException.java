package exception;

public class IllegalMovementException extends Exception {
	public IllegalMovementException(String message){
		System.err.println(message);
	}
}
