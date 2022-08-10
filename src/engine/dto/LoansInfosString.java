package engine.dto;

public class LoansInfosString {
    String loansInformation;

    public LoansInfosString(){
        loansInformation = "";
    }

    public void addLoansInfo(String loanInfo){
        loansInformation += loanInfo + "\n";
    }

    public String getLoansInformation() {
        return loansInformation;
    }
}
