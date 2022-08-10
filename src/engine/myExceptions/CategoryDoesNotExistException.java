package engine.myExceptions;

public class CategoryDoesNotExistException extends Exception{
    private String loanId;
    private String loanCategory;

    private final String EXCEPTION_MESSAGE = "The loan '%s', has the category '%s' which is not included in the xml category list.";

    public CategoryDoesNotExistException(String loanId , String loanCategory){
        this.loanId = loanId;
        this.loanCategory = loanCategory;
    }

    @Override
    public String getMessage() {return String.format(EXCEPTION_MESSAGE,loanId,loanCategory);}
}
