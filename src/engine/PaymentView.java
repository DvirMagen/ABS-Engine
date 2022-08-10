package engine;

import java.io.Serializable;
import java.util.Objects;

public class PaymentView implements Serializable {
    private String lenderName;
    private String loanId;
    private double interestRate;
    private double totalInvest;
    private double upcomingPayment;
    private String loanStatus;

    public PaymentView() {
    }

    public PaymentView(String lenderName, String loanId, double interestRate, double totalInvest, double upcomingPayment, String loanStatus) {
        this.lenderName = lenderName;
        this.loanId = loanId;
        this.interestRate = interestRate;
        this.totalInvest = totalInvest;
        this.upcomingPayment = upcomingPayment;
        this.loanStatus = loanStatus;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getTotalInvest() {
        return totalInvest;
    }

    public void setTotalInvest(double totalInvest) {
        this.totalInvest = totalInvest;
    }

    public double getUpcomingPayment() {
        return upcomingPayment;
    }

    public void setUpcomingPayment(double upcomingPayment) {
        this.upcomingPayment = upcomingPayment;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }



    @Override
    public String toString() {
        return "{" +
                "lenderName:\"" + lenderName + '\"' +
                ", loanId:\"" + loanId + '\"' +
                ", interestRate:" + interestRate +
                ", totalInvest:" + totalInvest +
                ", upcomingPayment:" + upcomingPayment +
                ", loanStatus:\"" + loanStatus + '\"' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentView)) return false;
        PaymentView that = (PaymentView) o;
        return Double.compare(that.getInterestRate(), getInterestRate()) == 0 && Double.compare(that.getTotalInvest(), getTotalInvest()) == 0 && Double.compare(that.getUpcomingPayment(), getUpcomingPayment()) == 0 && getLenderName().equals(that.getLenderName()) && getLoanId().equals(that.getLoanId()) && getLoanStatus().equals(that.getLoanStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLenderName(), getLoanId(), getInterestRate(), getTotalInvest(), getUpcomingPayment(), getLoanStatus());
    }
}
