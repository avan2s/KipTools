package kip.tools.exception;

public class PeriodNotValidException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public PeriodNotValidException(){
		super("Period is not valid!");
	}
	
	public PeriodNotValidException(String message){
		super(message);
	}
}
