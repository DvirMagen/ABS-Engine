package engine.myExceptions;

public class NoLoansMatchedMinimumIntristException extends Exception{

    private int minimumIntrist;

    private final String EXCEPTION_MESSAGE = "The minimum intrist that you asked for is %s, and there were no loans that matched your request.";

    public NoLoansMatchedMinimumIntristException(int minimumIntrist){
        this.minimumIntrist = minimumIntrist;
    }

    @Override
    public String getMessage() {return String.format(EXCEPTION_MESSAGE,minimumIntrist);}
}
