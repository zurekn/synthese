package exception;

import java.util.IllformedLocaleException;

public class InvalidMovementException extends Exception {
	public InvalidMovementException(String message){
		System.err.println(message);
	}
}
