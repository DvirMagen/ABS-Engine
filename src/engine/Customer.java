package engine;

import engine.myInterfaces.HasCustomerFuncs;

import java.util.*;

public class Customer implements HasCustomerFuncs {

    public class Movement {

        private int yazTimeAccured;
        private double amount;
        private char outOrIn;
        private double balanceBeforeAction;
        private double balanceAfterAction;

        @Override
        public String toString() {
            return "yaz: " + yazTimeAccured +
                    "\namount: " + String.format("%.1f" , amount) + outOrIn +
                    "\ncurrent balance:" + String.format("%.1f" , balanceAfterAction);
        }

        public void setYazTimeAccured(int time){this.yazTimeAccured = time;}

        public void setAmount(double amount){this.amount = amount;}

        public void setOutOrIn(char sign){this.outOrIn = sign;}

        public void setBalanceBeforeAction(double balanceBeforeAction){this.balanceBeforeAction = balanceBeforeAction;}

        public void setBalanceAfterAction(double balanceAfterAction){this.balanceAfterAction = balanceAfterAction;}
    }

    private double balance = 0;
    private String name;

    private List<Movement> movementList = new ArrayList<>();

    private List<Loan> loanerLoans = new ArrayList<>();

    private List<Loan> lenderLoans = new ArrayList<>();

    private List<String> notifications = new ArrayList<>();

    public Customer() {
    }

    public Customer(Customer customer) {
        this.name = customer.name;
        this.balance = customer.balance;
        this.notifications = new ArrayList<>(customer.notifications);
        this.movementList = new ArrayList<>(customer.movementList);
        this.lenderLoans = new ArrayList<>(customer.lenderLoans);
        this.loanerLoans = new ArrayList<>(customer.loanerLoans);
    }

    public void setBalance(double balance){this.balance = balance;}

    @Override
    public double getBalance() {return balance;}

    public void setName(String name){this.name = name;}

    @Override
    public String getName(){return name;}

    public List<Movement> getUnmodifiableMovementList() {
        return Collections.unmodifiableList(movementList);
    }

    @Override
    public String toString() {
        String res = "Customer information :\n" +
                " name : " + name +
                ";\n balance : " + String.format("%.1f" , balance) +";\n movements in the account :{\n";
        for (Movement movement : movementList){
            res += movement.toString() + "; \n";
        }
        res += "};\n loans as a loner :{";
        int i=1;
        for(Loan loan : loanerLoans){
            res += "loan #" + i + loan.printDataForCustomerLoaner() + ";\n";
            i++;
        }
        res += "};\n loans as a lender :{";
        i=1;
        for (Loan loan : lenderLoans){
            res += "loan #" + i + loan.printDataForCustomerLender(name) + ";\n";
            i++;
        }
        res += "};\n";

        return res;
    }

    public void loadMoneyToAccount(double money , int yazTime){
        if(money>0) {
            Movement movement = new Movement();
            movement.outOrIn = '+';
            movement.amount = money;
            movement.yazTimeAccured = yazTime;
            movement.balanceBeforeAction = balance;
            this.balance += money;
            movement.balanceAfterAction = balance;
            this.movementList.add(movement);
        }
    }

    public void withdrawMoneyFromAccount(double money , int yazTime){
        if(money>0) {
            Movement movement = new Movement();
            movement.outOrIn = '-';
            movement.amount = money;
            movement.yazTimeAccured = yazTime;
            movement.balanceBeforeAction = balance;
            this.balance -= money;
            movement.balanceAfterAction = balance;
            this.movementList.add(movement);
        }
    }

    public void addLoanToLenders(Loan loanToInvest , double invest , int yazTime){
        boolean alreadyExist = false;

        for(Loan loan : lenderLoans)
            if(loan.getId().equalsIgnoreCase(loanToInvest.getId()))
                alreadyExist = true;

        if(!alreadyExist)
            this.lenderLoans.add(loanToInvest);

        withdrawMoneyFromAccount(invest , yazTime);
    }

    public void addLoanToLoaners(Loan newLoan){
        this.loanerLoans.add(newLoan);
    }

    public void payPayments(int yazTime){
        System.out.println("Customer payPayments "+yazTime);
        Comparator<Loan> compareByStartingYazTime = new Comparator<Loan>() {
            @Override
            public int compare(Loan loan1, Loan loan2) {
                if(loan1.getYazTimeActive() == loan2.getYazTimeActive()){
                    return (int) (loan1.getTheUpcomingPayment() - loan2.getTheUpcomingPayment());
                }
                return (loan1.getYazTimeActive() - loan2.getYazTimeActive());
            }
        };

        Collections.sort(loanerLoans,compareByStartingYazTime);

        for (Loan loan : loanerLoans){
            if(loan.getTheUpcomingPayment() <= balance && (loan.getLastYazTimePayment() == yazTime || (loan.getStatus().equalsIgnoreCase("risk")
                    && loan.getTotalYazTime() + loan.getYazTimeActive() <= yazTime) )&& !loan.getStatus().equals("finished")){

                for (Loan.Lender lender : loan.getLenders()) {
                    double payToLender = loan.payToLender(lender.getName());
                    withdrawMoneyFromAccount(payToLender, yazTime);
                    loan.setLastPaymentAsPayed(yazTime, lender.getName(), payToLender);
                }
            }
        }

    }
    public void getPayment(int yazTime){
        for (Loan loan : lenderLoans){
            if(!loan.getStatus().equalsIgnoreCase("finished") && loan.wasLastPaymentPayed() && loan.getLastYazTimePayment() == yazTime)
                loadMoneyToAccount(loan.payToLender(name), yazTime);
        }

    }

    public List<Loan> getLoanerLoans(){
        return loanerLoans;
    }


    public List<Loan> getLenderLoans() {
        return lenderLoans;
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }
    public void addNewNotification(String notification){
        this.notifications.add(notification);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getName().equals(customer.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public Loan addNewLoan(String loanId, String loanCategory,
                          String loanCapital, String loanTotalYazTime,
                          String loanPaymentEveryYazTime, String loanInstintPerPayment)
    {
        Loan loan = new Loan(this.name,
                loanCategory,
                Integer.parseInt(loanCapital),
                Integer.parseInt(loanTotalYazTime),
                Integer.parseInt(loanPaymentEveryYazTime),
                (int) Math.round(Double.parseDouble(loanInstintPerPayment)),
                loanId
        );
        addLoanToLoaners(loan);
        return loan;
    }


}
