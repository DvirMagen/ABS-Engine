package engine.dto;

import engine.Loan;

import java.util.List;

public class LoansInfo {
    private List<Loan> loanInfoSet;

    public List<Loan> getLoansInfo(List<Loan> loansListReceived){
        this.loanInfoSet = loansListReceived;
        return loanInfoSet;
    }
}
