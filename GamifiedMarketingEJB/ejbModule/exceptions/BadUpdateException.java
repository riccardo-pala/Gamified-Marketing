package exceptions;

public class BadUpdateException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadUpdateException(String message) {
		super(message);
	}
}