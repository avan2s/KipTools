package kip.tools.exception;

public class ValueNotReadableException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public ValueNotReadableException(){
		super("Evidence could not be set");
	}
	
	public ValueNotReadableException(String message){
		super(message);
	}

}
