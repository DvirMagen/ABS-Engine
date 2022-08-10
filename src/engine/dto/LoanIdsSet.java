package engine.dto;

import engine.Loan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoanIdsSet {
    private Set<String> loanIds = new HashSet<>();

    public LoanIdsSet(List<Loan> loans){
        for (Loan loan : loans)
            loanIds.add(loan.getId());
    }

    public Set<String> getLoanIds() {
        return loanIds;
    }
}
