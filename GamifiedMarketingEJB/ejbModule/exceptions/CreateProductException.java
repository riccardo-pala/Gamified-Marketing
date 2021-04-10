package exceptions;

public class CreateProductException extends Exception {
	private static final long serialVersionUID = 1L;

	public CreateProductException(String message) {
		super(message);
	}
}
