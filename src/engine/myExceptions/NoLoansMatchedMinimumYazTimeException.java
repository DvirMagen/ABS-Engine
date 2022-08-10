package engine.myExceptions;

public class NoLoansMatchedMinimumYazTimeException extends Exception{

    private int minimumYazTime;

    private final String EXCEPTION_MESSAGE = "The minimum yaz time that you asked for is %s, and there were no loans that matched your request.";

    public NoLoansMatchedMinimumYazTimeException(int minimumYazTime){
        this.minimumYazTime = minimumYazTime;
    }

    @Override
    public String getMessage() {return String.format(EXCEPTION_MESSAGE,minimumYazTime);}
}
