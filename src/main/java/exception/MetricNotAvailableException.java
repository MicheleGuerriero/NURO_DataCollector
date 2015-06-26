package exception;

public class MetricNotAvailableException extends Exception {
	private static final long serialVersionUID = 1L;

	public MetricNotAvailableException(String msg) {
		super(msg);	
	}
}
