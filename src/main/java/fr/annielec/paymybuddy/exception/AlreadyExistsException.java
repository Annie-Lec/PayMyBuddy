package fr.annielec.paymybuddy.exception;

/**
 * Used before adding data : we can add record only if it does'nt exist
 */
public class AlreadyExistsException extends Exception {

	private static final long serialVersionUID = -2459430512022097593L;

	public AlreadyExistsException(String message) {
		super(message);

	}

}
