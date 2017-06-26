package nts.uk.shr.infra.i18n.format;

public class IllegalDateTimeFormatException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public IllegalDateTimeFormatException() {
	}
	
	public IllegalDateTimeFormatException(Throwable ex) {
		super(ex);
	}

	public IllegalDateTimeFormatException(String message) {
		super(message);
	}
}
