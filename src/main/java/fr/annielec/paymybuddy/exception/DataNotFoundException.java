package fr.annielec.paymybuddy.exception;

/**
 * Used in the search methods : when the data is not found, not present in
 * 'database'
 * 
 * @author aNewL
 *
 */
public class DataNotFoundException extends Exception {

	private static final long serialVersionUID = -6958488039909991305L;

	public DataNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
