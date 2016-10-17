package beaform.dao;

public class InvalidFormulaException extends RuntimeException {

	private static final long serialVersionUID = 4798282216680922054L;

	public InvalidFormulaException() {
		super();
	}

	public InvalidFormulaException(String message) {
		super(message);
	}

}
