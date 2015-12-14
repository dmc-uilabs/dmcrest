package vehicle_forge;

public class ErrorMessage {
	private final String message;

	private final String logTag = ErrorMessage.class.getName();
	
	public ErrorMessage(String txt){
		
		this.message = txt;
	}
	
	public ErrorMessage(ErrorMessageBuilder build){
		this.message = build.message;
	}
	
	public String getMessage(){
		return message;
	}
	
	//Service Builder
    public static class ErrorMessageBuilder {
    	
    	private String message;
   
    	public ErrorMessageBuilder(String txt) {
    		this.message = txt;
    	}  	
    	    	
    	public ErrorMessage build() {
    		return new ErrorMessage(this);
    	}
    }
}
