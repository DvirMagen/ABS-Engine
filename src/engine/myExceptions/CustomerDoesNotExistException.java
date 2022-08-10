package engine.myExceptions;

public class CustomerDoesNotExistException extends Exception{
    private String loanId;
    private String loanOwner;

    private final String EXCEPTION_MESSAGE = "The loan '%s', has the owner '%s' which is not included in the xml customer list.";

    public CustomerDoesNotExistException(String loanId, String loanOwner){
        this.loanId = loanId;
        this.loanOwner = loanOwner;
    }

    @Override
    public String getMessage() {return String.format(EXCEPTION_MESSAGE, loanId , loanOwner);}

}
