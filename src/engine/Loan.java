package engine;

import engine.myInterfaces.HasLoanFuncs;
import engine.myInterfaces.HasName;

import java.util.*;

public class Loan implements HasLoanFuncs {


    public static class Lender implements HasName {
        final private String name;
        private double invest;

        public Lender(){this.name = ""; this.invest = 0;}

        public Lender(String name , double invest){this.name = name; this.invest = invest;}

//        @Override
//        public String toString() {
//            return name + " : " + invest + "; ";
//        }

        @Override
        public String toString() {
            return "{" +
                    "name=\"" + name + '"' +
                    ", invest=" + invest +
                    '}';
        }

        @Override
        public String getName() {return name;}

        public double getInvest() {return invest;}

        public void addToInvest(double invest){this.invest += invest;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Lender)) return false;
            Lender lender = (Lender) o;
            return getInvest() == lender.getInvest() && getName().equals(lender.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getInvest());
        }
    }

    public  class Payment{
        private int timeUnitPayed;
        private double fundPartPayed;
        private double intristPartPayed;
        private double paymentTotal;
        private boolean isPayed = false;
        private String lenderName;
        private double currentlyPayedMoney;
        private double currentlyLeftToPay;


        public Payment(int timeUnitPayed , double fundPartPayed, double intristPartPayed,
                       double paymentTotal,String lenderName,double currentlyPayedMoney,double currentlyLeftToPay){
            this.timeUnitPayed = timeUnitPayed;
            this.fundPartPayed = fundPartPayed;
            this.intristPartPayed = intristPartPayed;
            this.paymentTotal = paymentTotal;
            this.lenderName = lenderName;
            this.currentlyPayedMoney = currentlyPayedMoney;
            this.currentlyLeftToPay=currentlyLeftToPay;
        }

        public void setAsPayed(){this.isPayed = true;}

        public void setTimeUnitPayed(){timeUnitPayed++;}

        public double getPaymentTotal(){return paymentTotal;}

        public String getLenderName() {
            return lenderName;
        }

        public double getCapitalToPay() {
            return paymentTotal - capital;
        }

        public boolean isPayed() {
            return isPayed;
        }

        public int getTimeUnitPayed() {
            return timeUnitPayed;
        }

        public double getCurrentlyLeftToPay() {
            return currentlyLeftToPay;
        }

        public void setCurrentlyPayedMoney(double currentlyPayedMoney) {
            this.currentlyPayedMoney = currentlyPayedMoney;
        }

        public void setForPayTheRestOfTheLoan(double fundPartLeftToPay, double interestPartLetToPay){
            this.paymentTotal = fundPartLeftToPay+interestPartLetToPay;
            this.fundPartPayed = fundPartLeftToPay;
            this.intristPartPayed = interestPartLetToPay;
        }

        @Override
        public String toString() {
            String res = "time unit payed=" + timeUnitPayed +
                    "; fund part payed=" + String.format("%.1f" , fundPartPayed) +
                    "; intrist part payed=" + String.format("%.1f" , intristPartPayed) +
                    "; total payment=" + String.format("%.1f" , paymentTotal);

            if(!isPayed)
                res += " | (PAYMENT WAS NOT MADE!)";
            return res;
        }
    }

    private String owner;
    private String category;
    private int capital;
    private int totalYazTime;
    private int paysEveryYaz;
    private int intristPerPayment;
    private String id;
    private Map<Lender,Double> lenders = new HashMap<>(); // lender -> how much not payed
    private int yazTimeActive;
    private int yazTimeLeft;
    private double intristPayed;
    private double intristLeftToPay;
    private double loanPayed = 0;
    private double loanLeftToPay;
    private int nextYazTimePayment;
    private int lastYazTimePayment;
    private String status = "new";
    private List<Payment> payments = new ArrayList<>();
    private boolean wasMoneyTaken = false;

    private boolean isOnSale = false;

    public Loan(String owner, String category, int capital,
                int totalYazTime, int paysEveryYaz, int intristPerPayment,
                String id) {
        this.owner = owner;
        this.category = category;
        this.capital = capital;
        this.totalYazTime = totalYazTime;
        this.paysEveryYaz = paysEveryYaz;
        this.intristPerPayment = intristPerPayment;
        this.id = id;

        this.loanLeftToPay = capital;
        this.loanPayed = 0;
    }

    private Loan(String owner, String category, int capital, int totalYazTime, int paysEveryYaz, int intristPerPayment, String id, int yazTimeActive, int yazTimeLeft, double intristPayed, double intristLeftToPay, double loanPayed, double loanLeftToPay, int nextYazTimePayment, int lastYazTimePayment, String status, List<Payment> payments, boolean wasMoneyTaken, Map<Lender,Double> lenders) {
        this.owner = owner;
        this.category = category;
        this.capital = capital;
        this.totalYazTime = totalYazTime;
        this.paysEveryYaz = paysEveryYaz;
        this.intristPerPayment = intristPerPayment;
        this.id = id;
        this.yazTimeActive = yazTimeActive;
        this.yazTimeLeft = yazTimeLeft;
        this.intristPayed = intristPayed;
        this.intristLeftToPay = intristLeftToPay;
        this.loanPayed = loanPayed;
        this.loanLeftToPay = loanLeftToPay;
        this.nextYazTimePayment = nextYazTimePayment;
        this.lastYazTimePayment = lastYazTimePayment;
        this.status = status;
        this.payments = payments;
        this.wasMoneyTaken = wasMoneyTaken;
        this.lenders = lenders;
    }

    public Loan(Loan loan) {
        this(loan.owner, loan.category,loan.capital,loan.totalYazTime,loan.paysEveryYaz,loan.intristPerPayment,
                loan.id,loan.yazTimeActive,loan.yazTimeLeft
                ,loan.intristPayed,loan.intristLeftToPay,loan.loanPayed,loan.loanLeftToPay,
                loan.nextYazTimePayment,loan.lastYazTimePayment,
                loan.status,new ArrayList<>(loan.payments),loan.wasMoneyTaken, new HashMap<>(loan.lenders));
    }

    public void setOwner(String owner){this.owner = owner;}

    @Override
    public String getOwner(){return this.owner;}

    public void setCategory(String category){this.category = category;}

    @Override
    public String getCategory(){return this.category;}

    public void setCapital(int capital){this.capital = capital;}

    @Override
    public int getCapital(){return capital;}

    public void setTotalYazTime(int totalYazTime){this.totalYazTime = totalYazTime;}

    @Override
    public int getTotalYazTime(){return this.totalYazTime;}

    public void setPaysEveryYaz(int paysEveryYaz){this.paysEveryYaz = paysEveryYaz;}

    public String getStatus(){return status;}

    @Override
    public int getPaysEveryYaz(){return paysEveryYaz;}

    public void setIntristPerPayment(int intristPerPayment){this.intristPerPayment = intristPerPayment;}

    @Override
    public int getIntristPerPayment(){return intristPerPayment;}

    public double getLoanLeftToPay() {
        return loanLeftToPay;
    }

    public void setId(String id){this.id = id;}

    @Override
    public String getId(){return this.id;}

    public Set<Lender> getLenders() {
        return lenders.keySet();
    }

    public void setYazTimeLeft(){
        if(wasLastPaymentPayed()) {
            int counter = 1;
            for (Payment payment : payments){
                if(payment.isPayed)
                    counter = 1;
                else
                    counter++;
            }
            this.yazTimeLeft -= counter;
        }
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }

    public int getYazTimeLeft() {return yazTimeLeft;}

    public int getYazTimeActive(){return yazTimeActive;}

    public boolean wasTheMoneyTakenAlready(){return wasMoneyTaken;}

    @Override
    public String toString() {
        return "Loan information :\n" + " id = " + id + ".\n" +
                " owner = " + owner + ".\n" +
                " category = " + category + ".\n" +
                " capital = " + capital +
                "; total time for the loan = " + totalYazTime + ".\n" +
                " intrist for the loan = " + intristPerPayment +
                "%; payment is every " + paysEveryYaz + " yazTime.\n" +
                " status = " + status+ ".\n" +
                " payed = "+ loanPayed;
    }

    public void updateDebt(int yazTime){
        int counterNotPayed = 1;

        for(Payment payment : payments) {
            if (payment.isPayed && payment.timeUnitPayed != lastYazTimePayment)
                counterNotPayed = 1;
            if (!payment.isPayed && payment.timeUnitPayed != lastYazTimePayment)
                counterNotPayed++;
            if (payment.timeUnitPayed == yazTime && payment.isPayed) {
                this.intristPayed += (((double) intristPerPayment) / 100) * (capital / (totalYazTime / paysEveryYaz)) * counterNotPayed;
                this.intristLeftToPay -= (((double) intristPerPayment) / 100) * (capital / (totalYazTime / paysEveryYaz)) * counterNotPayed;
                this.intristPayed = Math.round(this.intristPayed*100)/100;
                this.intristLeftToPay = Math.round(this.intristLeftToPay*100)/100;
            }
        }
        this.loanLeftToPay = capital - this.loanPayed;
        this.loanLeftToPay  = Math.round( this.loanLeftToPay *100)/100;
    }

    public String additionPrint() {
        String res = "";
        String finishedAddition = "";
        if(status.equalsIgnoreCase("pending") || status.equalsIgnoreCase("finished")){
            int moneyInvested = 0, moneyLeftToFund = capital;
            String pendingAddition = "\n  - lenders:{ ";
            if(lenders.size() == 0)
                pendingAddition += "no lenders yet";
            else {
                for (Lender len : lenders.keySet()) {
                    moneyInvested += len.invest;
                    moneyLeftToFund -= len.invest;
                    pendingAddition += len.toString();
                }
            }
            pendingAddition += " }\n  - money invested=" + moneyInvested + ".\n  - money left to fund=" + moneyLeftToFund + ".\n";
            if(status.equalsIgnoreCase("pending"))
                res += pendingAddition;
            else if(status.equalsIgnoreCase("finished")){
                finishedAddition += pendingAddition + "  - the time the loan became 'active'=" + yazTimeActive + "; the time the loan was fully payed=" +
                        lastYazTimePayment + ".\n  - the payments{ " ;
                int i = 1;
                for (Payment payment : payments) {
                    finishedAddition += "\n payment #" + i + " : " + payment.toString() + " ;";
                    i++;
                }
                res += finishedAddition + " }\n";
            }
        }

        else if(status.equalsIgnoreCase("active") || status.equalsIgnoreCase("risk")) {
            String activeAddition = "\n  - lenders:{ ";
            for (Lender len : lenders.keySet())
                activeAddition += len.toString();
            activeAddition += " }\n  - the time the loan became 'active'=" + yazTimeActive + ".\n  - yazTime of the next expected payment = " + (nextYazTimePayment) +
                    ".\n  - payments payed already:{ ";
            int counterPaymentsDelayed = 0, i = 1;
            for (Payment payment : payments){
                if(!(payment.isPayed))
                    counterPaymentsDelayed++;
                activeAddition +=  "\n payment #" + i + " : "+ payment + " ;";
                i++;
            }
            activeAddition +=" }\n  - total fund money payed already=" + loanPayed
                    + ".\n  - total intrist payed already=" + intristPayed + ".\n  - total fund money left to pay=" + loanLeftToPay + ".\n  - total intrist left to pay="
                    + intristLeftToPay + ".\n";
            if(status.equalsIgnoreCase("risk"))
                activeAddition += "  - total amount delayed=" + counterPaymentsDelayed + " (total amount of " + String.format("%.1f" , (counterPaymentsDelayed*(((double)capital/(totalYazTime/paysEveryYaz))*(((double) 100+intristPerPayment)/100)))) + ").\n";
            res += activeAddition;
        }

        return res;
    }

    public int getLastYazTimePayment() {
        return lastYazTimePayment;
    }

    public String printDataForCustomerLoaner(){
        return "loan id : '" + id +"'; loan category : " + category + "; original loan amount : " + capital +
                "; payment is being done every " + paysEveryYaz + " yaz time; intrist for every payment : " + intristPerPayment +
                "; total amount to pay for the loan : " + String.format("%.1f" , (((double)(100+intristPerPayment))/100)*capital) + "; loan status: " + printStatusForCustomerLoaner();
    }

    public String printStatusForCustomerLoaner(){
        if(status.equalsIgnoreCase("pending")){
            double totalPayed = 0;
            for(Lender lender : lenders.keySet()){
                totalPayed += lender.invest;
            }
            return " -PENDING LOAN- left to fund for the loan to become active=" + (capital-totalPayed) + ".";
        }

        else if(status.equalsIgnoreCase("active")){
            return " -ACTIVE LOAN- next payment is at the " + nextYazTimePayment + "time unit; - the payment would be " +
                    String.format("%.1f" , ((((double)capital)/(totalYazTime/paysEveryYaz))*(((double) intristPerPayment+100)/100))) + "at total.";
        }

        else if(status.equalsIgnoreCase("risk")){
            int counter = 0;
            for (Payment payment : payments) {
                if (!payment.isPayed)
                    counter++;
                else
                    counter = 0;
            }
            return " -RISK LOAN- there were " + counter + " payments that had not happened, at total amount of " +
                    String.format("%.1f" , ((((double)capital)/(totalYazTime/paysEveryYaz))*(((double) intristPerPayment+100)/100))*counter) +".";
        }

        else if(status.equalsIgnoreCase("finished")){
            return " -FINISHED LOAN- became active at " + yazTimeActive + " yaz time, and was fully payed at " + lastYazTimePayment + "yaz time.";
        }
        else if(status.equalsIgnoreCase("new")){
            return "new";
        }

        return "";
    }

    public String printDataForCustomerLender(String name){
        String res = "";
        for (Lender lender : lenders.keySet()){
            if(lender.name.equalsIgnoreCase(name)){
                res += "loan id : " + id + "; loan category : " + category + "; original invest : " + lender.invest +
                        ";  payment is being done every " + paysEveryYaz + " yaz time; intrist for every payment : " + intristPerPayment
                        + "; total amount for the loan :" + String.format("%.1f" , (lender.invest + lender.invest*(((double)intristPerPayment)/100))) + "; loan status : " + printStatusForCustomerLender(lender.name);
            }
        }
        return res;
    }

    public String printStatusForCustomerLender(String name){
        if(status.equalsIgnoreCase("pending")){
            double totalPayed = 0;
            for(Lender lender : lenders.keySet()){
                totalPayed += lender.invest;
            }
            return " -PENDING LOAN- left to fund for the loan to become active=" + (capital-totalPayed) + ".";
        }

        else if(status.equalsIgnoreCase("active")){
            for (Lender lender : lenders.keySet()) {
                if(lender.name.equalsIgnoreCase(name)) {
                    return " -ACTIVE LOAN- next payment is at the " + nextYazTimePayment+ "time unit; - the payment would be " +
                            String.format("%.1f" , ((((double)lender.invest) / (totalYazTime/paysEveryYaz)) * (((double) intristPerPayment+100) / 100))) +
                            "at total.";
                }
            }
        }

        else if(status.equalsIgnoreCase("risk")){
            int counter = 0;
            for (Payment payment : payments) {
                if (!payment.isPayed)
                    counter++;
                else
                    counter = 0;
            }
            for(Lender lender : lenders.keySet()) {
                if (lender.name.equalsIgnoreCase(name)) {
                    return " -RISK LOAN- there were " + counter + " payments that had not happened, at total amount of " +
                            String.format("%.1f" , ((((double)lender.invest) / (totalYazTime/paysEveryYaz)) * (((double) intristPerPayment+100) / 100))*counter) + ".";
                }
            }
        }

        else if(status.equalsIgnoreCase("finished")){
            return " -FINISHED LOAN- became active at " + yazTimeActive + " yaz time, and was fully payed at " + lastYazTimePayment + "yaz time.";
        }

        return "";
    }

    public String showValidLoan(String name , int invest , int minimumIntrist, int minimumYazTime){
        return toString() + additionPrint();
    }

    public int leftToFund(){
        if(lenders.isEmpty())
            return capital;

        int leftToFund = capital;
        for(Lender lender : lenders.keySet()){
            leftToFund -= lender.getInvest();
        }
        return leftToFund;
    }

    public boolean isLoanFits(String lenderName , double invest , int minimumIntrist, int minimumYazTime){
        boolean res = false;
        double leftToFund = capital - lenders.keySet().stream().filter(lender -> !lender.name.equalsIgnoreCase(lenderName)).mapToDouble(Lender::getInvest).sum();

        for (Lender lender : lenders.keySet())
            if (lender.name.equals(lenderName))
                return false;

        if(!(owner.equals(lenderName)) && leftToFund >= invest && intristPerPayment >= minimumIntrist && totalYazTime >= minimumYazTime && ((status.equalsIgnoreCase("new") || status.equalsIgnoreCase("pending")))) {
            res = true;
        }

        return res;
    }

    public double getLenderInvest(String name){
        for(Lender lender : lenders.keySet())
            if (lender.name.equalsIgnoreCase(name))
                return lender.invest;
        return 0;
    }

    public double getLenderInvestLeft(String name){
        double invest = getLenderInvest(name);
        System.out.println("getLenderInvestLeft: ");
        System.out.println("Invest of Lender: " + invest);
        double investLeft = invest;
        //investLeft += this.leftToPayForTheLoan();
        int i = 0;
        for (Payment payment : payments) {
            if ((payment.isPayed() || payment.getCurrentlyLeftToPay() > 0)) {// && payment.timeUnitPayed < yazTime)
                //investLeft -= ((((double) invest) / capital) * payment.totalPaymentPayed) / (totalYazTime / paysEveryYaz);
                investLeft += ((((double) getLoanPayed()) / capital) * payment.fundPartPayed) / (totalYazTime / paysEveryYaz);
                System.out.println("investLeft #" + i +": " + investLeft);
                System.out.println("payment.fundPartPayed #" + i +": " + payment.fundPartPayed);
                i++;

            }
        }
        System.out.println("####return investLeft : " + investLeft);
        return investLeft;
    }

    //i dont have to see if the invest is ok because it will only show this loan to the user if it fits
    public void addNewLender(String name, double invest, int yazTime){
        System.out.println("addNewLender name="+name+", invest="+invest+", yazTime="+yazTime);
        boolean alreadyExist = false;
        for(Lender lender : lenders.keySet()){
            if(lender.name.equalsIgnoreCase(name)) {
                lender.addToInvest(invest);
                alreadyExist = true;
            }
        }

        if(!alreadyExist) {
            Lender lender = new Lender(name, invest);

            this.lenders.put(lender,0.0);
        }

        changeStatus(yazTime);
    }


    /*
    public void changeStatus(int yazTime){
        if(status.equalsIgnoreCase("new")){
            if(!(lenders.isEmpty())){
                int totalInvest = 0;

                for (Lender lender : lenders)
                    totalInvest += lender.invest;
                this.loanPayed = totalInvest;
                if (totalInvest < capital)
                    this.status = "pending";

                else if(totalInvest == capital) {
                    this.status = "active";
                    this.yazTimeActive = yazTime;
                    this.yazTimeLeft = totalYazTime;
                    this.nextYazTimePayment = yazTime + paysEveryYaz;
                    this.loanLeftToPay = capital;
                    this.loanPayed = totalInvest;
                    this.intristLeftToPay = (((double)intristPerPayment)/100)*capital;
                    this.intristPayed = 0;
                }
            }
        }
        else if(status.equalsIgnoreCase("pending")){
            int totalInvest = 0;

            for (Lender lender : lenders)
                totalInvest += lender.invest;
            this.loanPayed = totalInvest;
            if(totalInvest == capital){
                this.status = "active";
                this.yazTimeActive = yazTime;
                this.yazTimeLeft = totalYazTime;
                this.nextYazTimePayment = yazTime + paysEveryYaz;
                this.loanLeftToPay = capital;
                this.intristLeftToPay = (((double)intristPerPayment)/100)*capital;
                this.intristPayed = 0;
            }
        }

        else if(status.equalsIgnoreCase("active")){
            double totalPayed = 0;
            for(Payment payment : payments)
                if(payment.isPayed)
                    totalPayed += payment.paymentTotal;
            if(totalPayed == ((double)capital*(((double) intristPerPayment+100)/100))) {
                this.status = "finished";
                this.lastYazTimePayment = yazTime;
            }
            else {
                if(!wasLastPaymentPayed() && yazTime != yazTimeActive && yazTime == lastYazTimePayment)
                    this.status = "risk";
            }
        }
        else if(status.equalsIgnoreCase("risk")){

            double totalPayed = 0;
            for(Payment payment : payments)
                if(payment.isPayed)
                    totalPayed += payment.paymentTotal;
            if(totalPayed == ((double)capital*(((double) intristPerPayment+100)/100))) {
                this.status = "finished";
                this.lastYazTimePayment = yazTime;
            }

            else if(wasLastPaymentPayed())
                this.status = "active";
        }
    }
*/
    public void changeStatus(int yazTime){
        if(status.equalsIgnoreCase("new")){
            if(!(lenders.isEmpty())){
                int totalInvest = 0;

                for (Lender lender : lenders.keySet())
                    totalInvest += lender.invest;

                if (totalInvest < capital)
                    this.status = "pending";

                else if(totalInvest == capital) {
                    this.status = "active";
                    this.yazTimeActive = yazTime;
                    this.yazTimeLeft = totalYazTime;
                    this.nextYazTimePayment = yazTime + paysEveryYaz;
//                    this.loanPayed = 0;
                    this.loanLeftToPay = capital - this.loanPayed;
                    this.intristLeftToPay = (((double)intristPerPayment)/100)*capital;
                    this.intristPayed = 0;
                }
            }
        }
        else if(status.equalsIgnoreCase("pending")){
            int totalInvest = 0;

            for (Lender lender : lenders.keySet())
                totalInvest += lender.invest;

            if(totalInvest == capital){
                this.status = "active";
                this.yazTimeActive = yazTime;
                this.yazTimeLeft = totalYazTime;
                this.nextYazTimePayment = yazTime + paysEveryYaz;
//                this.loanPayed = 0;
                this.loanLeftToPay = capital - this.loanPayed;
                this.intristLeftToPay = (((double)intristPerPayment)/100)*capital;
                this.intristPayed = 0;
            }
        }

        else if(status.equalsIgnoreCase("active")){
            double totalPayed = 0;
            double my_totalPayed = 0;
            double your_totalPayed = 0;
            for(Payment payment : payments)
                if(payment.isPayed) {
                    totalPayed += payment.paymentTotal - this.lenders.get(this.getLenderByName(payment.lenderName));
                    my_totalPayed += payment.currentlyPayedMoney;
                    your_totalPayed+=payment.fundPartPayed;
                }




            System.out.println("Total Payed: " + totalPayed);
            System.out.println("My Total Payed: " + my_totalPayed);
            System.out.println("Your Total Payed: " + your_totalPayed);
            System.out.println("loanPayed " + loanPayed);;

            if(my_totalPayed >= ((double)capital*(((double) intristPerPayment+100)/100))) {
                System.out.println("I Got Finish #45542");
                this.status = "finished";
                this.lastYazTimePayment = yazTime;
            }
            else {
                if(!wasLastPaymentPayed() && yazTime > lastYazTimePayment-paysEveryYaz && yazTime > yazTimeActive + paysEveryYaz)
                    this.status = "risk";
            }
        }
        else if(status.equalsIgnoreCase("risk")){

            double totalPayed = 0;
            double my_totalPayed = 0;
            int totalNotPayed = 0;
            for(Payment payment : payments) {
                if (payment.isPayed) {
                    my_totalPayed+=payment.currentlyPayedMoney;
                    totalNotPayed += this.lenders.get(this.getLenderByName(payment.lenderName));
                    totalPayed += payment.paymentTotal;

                }else {
                    totalNotPayed = -1;
                    break;
                }

            }
            if (totalNotPayed == 0){
                this.status = "active";
            }
            if(my_totalPayed >= ((double)capital*(((double) intristPerPayment+100)/100))) {
                System.out.println("I Got Finish #5");
                System.out.println("TotalPayed: " + totalPayed );
                System.out.println("TotalNotPayed: " + totalNotPayed );
                System.out.println("My Total Payed: " + my_totalPayed);
                System.out.println("totalPayed-totalNotPayed: " + (totalPayed-totalNotPayed) );
                System.out.println("((double)capital*(((double) intristPerPayment+100)/100)): " +((double)capital*(((double) intristPerPayment+100)/100)) );
                this.status = "finished";
                this.lastYazTimePayment = yazTime;
            }
/*
            else if(wasLastPaymentPayed())
                this.status = "active";

 */
        }
    }


    public boolean isThereEnoughMoney(){
        int totalInvested = 0;
        for (Lender lender : lenders.keySet()){
            totalInvested += lender.invest;
        }

        return totalInvested == capital;
    }

    public void takeMoney(int yazTime){
        this.wasMoneyTaken = true;
        changeStatus(yazTime);
    }

    public void createUpcomingPayment(int yazTime){
        if (yazTime == nextYazTimePayment) {
            this.lastYazTimePayment = yazTime;
            if(payments.size() == totalYazTime/paysEveryYaz && wasLastPaymentPayed())
                this.nextYazTimePayment = 0;
            else
                this.nextYazTimePayment += paysEveryYaz;
            double fundPartPayed = calculateNextPaymentAmount() +((double) capital) / (totalYazTime / paysEveryYaz);
            double intristPerPay = fundPartPayed * ((double) intristPerPayment) / 100;
            for (Lender lender : this.getLenders()) {
                Payment payment = new Payment(yazTime, fundPartPayed, intristPerPay,
                        fundPartPayed + intristPerPay,lender.getName(),
                        0,this.lenders.get(lender));
                this.lenders.put(lender,this.lenders.get(lender)+payToLenderForOneDay(lender.getName()));
                payments.add(payment);
            }

        }

    }

    public double calculateNextPaymentAmount(){
        double nextPayment=0;
        for(Payment payment : payments){
            if (payment.isPayed)
                nextPayment =0;
            else
                nextPayment += payment.fundPartPayed;
        }
        return nextPayment;
    }

    public int theAmountOfPaymentsCreated(){
        return payments.size();
    }

    public void setFinalPaymentAtRiskTimePayed(){
        for (Payment payment : payments)
            if (payment.timeUnitPayed == lastYazTimePayment)
                payment.setTimeUnitPayed();
    }

    public double getTheUpcomingPayment(){
        for(Payment payment : payments){
            if (payment.timeUnitPayed == lastYazTimePayment) {
                return payment.getPaymentTotal();
            }
        }
        return 0;
    }


    public void setLastPaymentAsPayed(int yazTime, String lenderName, double moneyWasPayed){
        for(Payment payment : payments){
            if(payment.timeUnitPayed == yazTime && payment.getLenderName().equals(lenderName)) {
                Lender lender = getLenderByName(payment.getLenderName());
                assert lender != null;
                System.out.println("-----------------------------------------");
                System.out.println(this.lenders.get(lender));
                System.out.println(this.lenders.get(lender)-moneyWasPayed);
                System.out.println("-----------------------------------------");
                this.lenders.put(lender, this.lenders.get(lender)-moneyWasPayed);
                this.loanPayed += moneyWasPayed;
                payment.setAsPayed();
                payment.setCurrentlyPayedMoney(moneyWasPayed);
            }
        }
    }

    private Lender getLenderByName(String lenderName){
        for (Lender lender : this.lenders.keySet()) {
            if (lender.getName().equals(lenderName)) return lender;
        }
        return null;
    }

    public boolean wasLastPaymentPayed(){
//        boolean flag = true;
//        int max_time = -1;
//        for (Payment payment : payments ) {
//            if(payment.timeUnitPayed < max_time)
//                max_time = payment.timeUnitPayed;
//        }
//        for (Payment payment : payments ) {
//            if(payment.timeUnitPayed == max_time)
//                flag = flag && payment.isPayed() && this.lenders.get(this.getLenderByName(payment.lenderName))==0;
//        }
//        return flag;

        for (Payment payment : payments ) {
            if(payment.timeUnitPayed == lastYazTimePayment - paysEveryYaz && this.lenders.get(this.getLenderByName(payment.lenderName))==0)
                return true;
        }

        return false;
    }

    public double payToLender(String lenderName){
        for (Lender lender : lenders.keySet()){
            if (!lender.getName().equals(lenderName)) continue;
            int countPaymentsToPay = 1;
            for(Payment payment : payments){
                if (!payment.getLenderName().equals(lender.getName())) continue;
                if(payment.isPayed && payment.timeUnitPayed == lastYazTimePayment)
                    return 0;
                else if(payment.isPayed)
                    countPaymentsToPay = 1;
                else if(payment.timeUnitPayed != lastYazTimePayment)
                    countPaymentsToPay++;
            }
            return countPaymentsToPay*(((double)lender.invest)/(totalYazTime/paysEveryYaz))*(((double)(100+intristPerPayment))/100);
        }
        return 0;
    }

    public double payToLenderAsNotPayed(String lenderName){
        for (Lender lender : lenders.keySet()){
            if (!lender.getName().equals(lenderName)) continue;
            int countPaymentsToPay = 1;
            for(Payment payment : payments){
                if (!payment.getLenderName().equals(lender.getName())) continue;
                else if(payment.timeUnitPayed != lastYazTimePayment)
                    countPaymentsToPay++;
            }
            return countPaymentsToPay*(((double)lender.invest)/(totalYazTime/paysEveryYaz))*(((double)(100+intristPerPayment))/100);
        }
        return 0;
    }
    public double payToLenderForOneDay(String lenderName){
        Lender lender = getLenderByName(lenderName);

        return (((double)lender.invest)/(totalYazTime/paysEveryYaz))*(((double)(100+intristPerPayment))/100);

    }

    public void setNextYazTimePaymentInRisk(int yazTime){
        this.lastYazTimePayment = yazTime;
        this.nextYazTimePayment = yazTime+1;
    }

    public int getNextYazTimePayment() {
        return nextYazTimePayment;
    }

    public double getLoanPayed() {
        return loanPayed;
    }


    public boolean wasThisYazTimePaymentPayed(int yaz){
        long lastPayed = payments.stream().filter(payment -> payment.timeUnitPayed==yaz && payment.isPayed).count();
        return lastPayed != 0;
    }

    public List<Payment> getPayments() {
        return Collections.unmodifiableList(payments);
    }

    public void changeAllPaymentsFromLenderAsPayed(String lenderName) {
        for (Payment payment : payments) {
            if (payment.getLenderName().equals(lenderName)){
                payment.setAsPayed();
            }
        };
    }

    public double payToLenderTheRestOfTheLoan(String name){
        int paymentsNotPayed = 0;
        int paymentsPayedAlready = 0;
        if(!payments.isEmpty()) {
            for (Payment payment : payments) {
                if (payment.isPayed) {
                    paymentsPayedAlready += paymentsNotPayed + 1;
                    paymentsNotPayed = 0;
                }
                else
                    paymentsNotPayed++;
            }
        }

        for(Lender lender : lenders.keySet()){
            if(lender.name.equalsIgnoreCase(name)){
                // return ((double) lender.invest * ((((double) 100+intristPerPayment)/100)/(totalYazTime/paysEveryYaz)) * paymentsNotPayed);
                //return ((double) lender.invest * ((double) 100+intristPerPayment)/100)/((totalYazTime/paysEveryYaz) * paymentsNotPayed);
                //return  (((((double) lender.invest)/(totalYazTime/paysEveryYaz))* paymentsNotPayed) * ((double) 100+intristPerPayment)/100);
                double paybackForEveryPayment = ((((double) 100+intristPerPayment)/100) * lender.invest) / (totalYazTime/paysEveryYaz);
                int paymentsLeft = totalYazTime/paysEveryYaz - paymentsPayedAlready;
                return paybackForEveryPayment*paymentsLeft;
            }
        }
        return 0;
    }

    public double leftToPayForTheLoan(){
        double leftToPayForLoan = ((double) capital*(((double) 100+intristPerPayment)/100));
        for (Payment payment : payments)
            if(payment.isPayed)
                leftToPayForLoan -= payment.paymentTotal;
        return leftToPayForLoan;
    }

    public void setTheRestOfTheLoanPayed(int yazTime){
        for(Payment payment : payments){
            if(payment.timeUnitPayed == yazTime) {
                payment.setForPayTheRestOfTheLoan(loanLeftToPay,intristLeftToPay);
                payment.setAsPayed();
            }
        }
        System.out.println("I Got Finish #123");
        this.status = "finished";
        this.intristPayed = (((double) intristPerPayment) / 100)*capital;
        this.intristLeftToPay = 0;
        this.loanLeftToPay = 0;
        this.loanPayed += capital;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;
        Loan loan = (Loan) o;
        return getOwner().equals(loan.getOwner()) && getCategory().equals(loan.getCategory()) && getId().equals(loan.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getCategory(), getId());
    }
}