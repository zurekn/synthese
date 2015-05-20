package exception;

@SuppressWarnings("serial")
public class PlayableCharacterException extends Exception {
	public PlayableCharacterException(String message){
		super();
		System.err.println(message);
	}
}
