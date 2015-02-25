package exception;

public class IllegalActionException extends Exception {
	public IllegalActionException(String message){
		super();
		System.err.println(message);
	}
}
