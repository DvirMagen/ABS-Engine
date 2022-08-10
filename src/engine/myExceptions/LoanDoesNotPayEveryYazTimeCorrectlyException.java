package engine.myExceptions;

public class LoanDoesNotPayEveryYazTimeCorrectlyException extends Exception{
    private String loanId;
    private int loanPayEveryYaz;
    private int loanTotalYazTime;

    private final String EXCEPTION_MESSAGE = "The loan '%s' has total yaz time of %d and it cant be payed every %d as it offers.";

    public LoanDoesNotPayEveryYazTimeCorrectlyException(String loanId, int loanPayEveryYaz , int loanTotalYazTime){
        this.loanId = loanId;
        this.loanPayEveryYaz = loanPayEveryYaz;
        this.loanTotalYazTime= loanTotalYazTime;
    }

    @Override
    public String getMessage() {return String.format(EXCEPTION_MESSAGE, loanId , loanTotalYazTime, loanPayEveryYaz);}


}
