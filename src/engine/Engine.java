package engine;

import com.sun.istack.internal.Nullable;
import engine.dto.*;
import engine.myExceptions.*;

import javax.xml.bind.JAXBException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Engine {

    private String xmlPath = "";

    private boolean isFileLoaded = false;

    private Set<Customer> customers = Collections.synchronizedSet(new HashSet<>());

    private Set<String> categories = Collections.synchronizedSet(new HashSet<>(Collections.singleton("None")));

    private Set<Loan> loans = Collections.synchronizedSet(new HashSet<>());;

    private int timeUnit = 1;

    private String loanIdNotValid = "";

    private String loanCategoryNotValid ="";

    private String loanOwnerNotValid = "";

    private int loanPayEveryYazNotValid = 0;

    private int loanTotalYazTimeNotValid = 0;


    private boolean xmlPathValid = false;

    private boolean isRewindMode = false;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private Map<Integer, List<Customer>> customersDataForRewindMode = new HashMap<>();

    private Map<Integer, List<Loan>> loansDataForRewindMode = new HashMap<>();

    private int realTimeUnit = 1;


    public int getTimeUnit() {return timeUnit;}

    public int getRealTimeUnit() {return realTimeUnit;}

    public boolean isXmlPathValid(){
        return xmlPathValid;
    }

    public void loadXmlPath(String xmlPath) throws XmlPathIsDoesNotEndAsNeededException{
        if (!xmlPath.endsWith("xml")) {
            throw new XmlPathIsDoesNotEndAsNeededException();
        }
        this.xmlPath = xmlPath;
        this.xmlPathValid = true;
    }

//    public void loadDataFromXmlFile() throws CustomerDoesNotExistException, CategoryDoesNotExistException, LoanDoesNotPayEveryYazTimeCorrectlyException, JAXBException {
//
//        this.isFileLoaded = true;
//       // this.customers = xmlFile.getCustomerSet(xmlPath);
//        this.categories = Xml.getCategoriesSet(xmlPath);
//        this.loans = Xml.getLoansList(xmlPath);
//        if(!loansHasAllOwners()) {
//            this.xmlPathValid = false;
//            throw new CustomerDoesNotExistException(loanIdNotValid, loanOwnerNotValid);
//        }
//        if(!loansHasAllCategories()){
//            this.xmlPathValid = false;
//            throw new CategoryDoesNotExistException(loanIdNotValid, loanCategoryNotValid);
//        }
//        if(!loansPaysEveryYazCorrectly()){
//            this.xmlPathValid = false;
//            throw new LoanDoesNotPayEveryYazTimeCorrectlyException(loanIdNotValid, loanPayEveryYazNotValid, loanTotalYazTimeNotValid);
//        }
//        this.timeUnit = 1;
//        addAllLoansToLoaners();
//    }

    public int loadDataFromClientXmlFile(String xmlPath_client,String clientName) throws CategoryDoesNotExistException, LoanDoesNotPayEveryYazTimeCorrectlyException, JAXBException {

//       boolean is_file_loaded = true;
        // this.customers = xmlFile.getCustomerSet(xmlPath);
        Set<String> categories =new HashSet<>(this.categories);
        Set<Loan> loans = new HashSet<>(this.loans);

        int status= 200;
        Set<String> categoriesSet = Xml.getCategoriesSet(xmlPath_client);
        categories.addAll(categoriesSet);
        Set<Loan> loansList = Xml.getLoansList(xmlPath_client);
        for (Loan loan : loansList) {
            loan.setOwner(clientName);
            if (this.loans.contains(loan)){
                status = 400;
            }
        }

        loans.addAll(loansList);
        if(!loansHasAllCategories(categories)){
            throw new CategoryDoesNotExistException(loanIdNotValid, loanCategoryNotValid);
        }
        if(!loansPaysEveryYazCorrectly(loans)){
            throw new LoanDoesNotPayEveryYazTimeCorrectlyException(loanIdNotValid, loanPayEveryYazNotValid, loanTotalYazTimeNotValid);
        }

        this.categories.addAll(categoriesSet);
        this.loans.addAll(loansList);

        this.timeUnit = 1;
        addAllLoansToLoaners();
        System.out.println("categories");
        for (String category : this.categories) {
            System.out.println("category:"+category);
        }
        System.out.println("loans");
        for (Loan loan : this.loans) {
            System.out.println("loan: "+loan);
        }

        return status;
    }


    protected void addAllLoansToLoaners(){
        for (Customer customer: customers)
            for (Loan loan : loans)
                if(loan.getOwner().equals(customer.getName()))
                    customer.addLoanToLoaners(loan);

    }

    public String getAllLoansInformation(){
        LoansInfosString loansInfosString = new LoansInfosString();
        for (Loan loan : loans)
            loansInfosString.addLoansInfo(loan.toString() + loan.additionPrint() + "\n");
        return loansInfosString.getLoansInformation();
    }

    public Set<String> getAvailableInvestLoansIds(String customerName , int invest, int minimumIntrist,int minimumYazTime) throws NoLoansMatchedMinimumIntristException, NoLoansMatchedMinimumYazTimeException{
        LoanIdsSet loanIdsSet = new LoanIdsSet(getAvailableInvestLoans(customerName, invest, minimumIntrist,minimumYazTime));
        return loanIdsSet.getLoanIds();
    }

    public boolean loansHasAllCategories(){
        return loansHasAllCategories(this.categories);
    }
    public boolean loansHasAllCategories(Set<String> categories){
        boolean valid;
        for(Loan loan : loans) {
            valid = false;
            for (String category : categories)
                if (category.equals(loan.getCategory())) {
                    valid = true;
                }
            if(!valid) {
                this.loanIdNotValid = loan.getId();
                this.loanCategoryNotValid = loan.getCategory();
                return false;
            }
        }
        return true;
    }


    public boolean loansHasAllOwners(){
        boolean valid = false;
        for(Loan loan : loans) {
            valid = false;
            for (Customer customer : customers)
                if (customer.getName().equalsIgnoreCase(loan.getOwner()))
                    valid = true;

            if (!valid) {
                this.loanIdNotValid = loan.getId();
                this.loanOwnerNotValid = loan.getOwner();
                return valid;
            }
        }
        return valid;
    }

    public boolean loansPaysEveryYazCorrectly(){
        return loansPaysEveryYazCorrectly(this.loans);
    }
    public boolean loansPaysEveryYazCorrectly(Set<Loan> loans){
        boolean valid = false;
        for (Loan loan : loans){
            valid = false;
            if(loan.getTotalYazTime()%loan.getPaysEveryYaz() == 0 && loan.getPaysEveryYaz() != 0 && loan.getTotalYazTime() != 0)
                valid = true;

            if(!valid){
                this.loanIdNotValid = loan.getId();
                this.loanPayEveryYazNotValid = loan.getPaysEveryYaz();
                this.loanTotalYazTimeNotValid = loan.getTotalYazTime();
                return valid;
            }
        }
        return valid;
    }


    public Set<CustomerDto> getAllCustomers(){
        Set<CustomerDto> allCustomersInfo = new HashSet<>();
        for(Customer customer : customers){
            CustomerDto customerDto = new CustomerDto(customer);
            allCustomersInfo.add(customerDto);
        }
        return allCustomersInfo;
    }

    public List<Customer> getAllCustomersDataInRewindMode(){
        if(isRewindMode) {
            List<Customer> customerList = customersDataForRewindMode.get(this.timeUnit);
            return customerList;
        }
        return null;
    }

    public List<Loan> getAllLoansDataInRewindMode(){
        if(isRewindMode) {
            List<Loan> loansList = loansDataForRewindMode.get(this.timeUnit);
            return loansList;
        }
        return null;
    }


    public Set<Loan> getAllLoans(){
        return Collections.unmodifiableSet(loans);
    }

    public boolean isFileLoaded(){
        return isFileLoaded;
    }

    public Set<String> getAllCustomersNames(){
        CustomersNames customersNames = new CustomersNames();
        return customersNames.getCustomersNamesSet(customers);
    }

    public int loadMoneyToCustomerAccount(String name, double amount){
        for(Customer customer: customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                customer.loadMoneyToAccount(amount, realTimeUnit);
                return 200;
            }
        }
        return 404;
    }

    public int withdrawMoneyFromCustomerAccount(String name , double amount){
        int status = 200;
        for(Customer customer: customers)
            if(customer.getName().equalsIgnoreCase(name)) {
                customer.withdrawMoneyFromAccount(amount, realTimeUnit);
                return status;
            }
        return 404;
    }

    public String showInformationForThisLoanId(String loanId, String name , int invest , int minimumIntrist, int minimumYazTime){
        for(Loan loan : loans) {
            if (loan.getId().equalsIgnoreCase(loanId))
                return loan.showValidLoan(name, invest, minimumIntrist, minimumYazTime);
        }
        return "";
    }

    public String showInformationForThisCustomerName(String name){
        for (Customer customer : customers)
            if(customer.getName().equalsIgnoreCase(name))
                return customer.toString() + "\n";
        return "";
    }

    public boolean areThereAnyLoansLeftToInvest(){
        for (Loan loan : loans)
            if(loan.leftToFund() > 0)
                return true;
        return false;
    }

    //I need this one in case I can invest only some of the money.
    public List<Loan> getAvailableInvestLoans(String investorName ,int invest, int minimumIntrist, int minimumYazTime) throws NoLoansMatchedMinimumIntristException, NoLoansMatchedMinimumYazTimeException {
        List<Loan> availableInvestLoans = new ArrayList<>();
        for(Loan loan : loans){
            if (loan.getIntristPerPayment()>=minimumIntrist && !loan.getOwner().equalsIgnoreCase(investorName) && (loan.getStatus().equalsIgnoreCase("new") || loan.getStatus().equalsIgnoreCase("pending")))
                availableInvestLoans.add(loan);
        }

        if(availableInvestLoans.isEmpty()){
            throw new NoLoansMatchedMinimumIntristException(minimumIntrist);
        }

        Predicate<Loan> loanYazTimeDontFit = loan -> (loan.getTotalYazTime() < minimumYazTime);

        availableInvestLoans.removeIf(loanYazTimeDontFit);

        if(availableInvestLoans.isEmpty()){
            throw new NoLoansMatchedMinimumYazTimeException(minimumYazTime);
        }

        LoansInfo loansAvailableInfo = new LoansInfo();
        return loansAvailableInfo.getLoansInfo(availableInvestLoans);
    }

    public int totalInvest(List<Loan> loansToInvest){
        return loansToInvest.stream().mapToInt(Loan::leftToFund).sum();
    }

    public List<Loan> getLoansToInvest(Set<String> loansNamesToInvest){
        List<Loan> loansToInvest = new ArrayList<>();
        for (String loanId : loansNamesToInvest)
            for (Loan loan : loans)
                if(loan.getId().equalsIgnoreCase(loanId))
                    loansToInvest.add(loan);

        return loansToInvest;
    }



    public int investMoney(String loanNameToInvest , double investMoney, String investorName) {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(loanNameToInvest);
        return this.investMoney(hashSet,investMoney,investorName);
    }
    /*
        public String investMoney(Set<String> loansNamesToInvest , int investMoney, String investorName){

        List<Loan> loansToInvest;
        loansToInvest = getLoansToInvest(loansNamesToInvest);
        String howMuchMoneyWasInvested = "";

        int totalNeededFund = 0;
        totalNeededFund = totalInvest(loansToInvest);
        if(totalNeededFund<investMoney) {
            howMuchMoneyWasInvested = "\nonly " + totalNeededFund + " was needed for all the loans you asked for.\n";
        }

        int minimumInvestInLoan =0;
        for(Loan loan : loansToInvest){
            if (minimumInvestInLoan == 0 || loan.leftToFund() < minimumInvestInLoan) {
                minimumInvestInLoan = loan.leftToFund();
            }
        }


        while (minimumInvestInLoan * loansToInvest.size() < investMoney && !loansToInvest.isEmpty()){
            int newMinimumInvestInLoan = 0;
            List<Loan> removedLoans = new ArrayList<>();
            for(Loan loan : loansToInvest){
                if(loan.leftToFund() == minimumInvestInLoan){
                    loan.addNewLender(investorName, minimumInvestInLoan , timeUnit);

                    loan.takeMoney(timeUnit);
                    for (Customer customer : customers){
                        if (customer.getName().equalsIgnoreCase(loan.getOwner()))
                            customer.loadMoneyToAccount(loan.getCapital(), timeUnit);

                        if(customer.getName().equalsIgnoreCase(investorName))
                            customer.addLoanToLenders(loan, minimumInvestInLoan , timeUnit);

                    }

                    removedLoans.add(loan);
                    investMoney -= minimumInvestInLoan;
                    totalNeededFund -= minimumInvestInLoan;
                }
                else if(newMinimumInvestInLoan == 0 || loan.leftToFund() < newMinimumInvestInLoan)
                    newMinimumInvestInLoan = loan.leftToFund();

            }
            loansToInvest.removeAll(removedLoans);
            minimumInvestInLoan = newMinimumInvestInLoan;
        }
        /////
        if(!loansToInvest.isEmpty()) {
            if (investMoney % loansToInvest.size() == 0) {
                int eachInvest = investMoney / loansToInvest.size();
                for (Loan loan : loansToInvest) {
                    loan.addNewLender(investorName, eachInvest, timeUnit);

                    for (Customer customer : customers) {
                        if (customer.getName().equalsIgnoreCase(investorName)) {
                            customer.addLoanToLenders(loan, eachInvest, timeUnit);
                        }
                    }

                    if (loan.isThereEnoughMoney() && (!(loan.wasTheMoneyTakenAlready()))) {
                        loan.takeMoney(timeUnit);
                        for (Customer customer : customers) {
                            if (customer.getName().equalsIgnoreCase(loan.getOwner()))
                                customer.loadMoneyToAccount(loan.getCapital(), timeUnit);
                        }
                    }
                }
            }
            /////
            /////
            else {
                int howMuchLeft = investMoney % loansToInvest.size();
                int i = 0;

                for (Loan loan : loansToInvest) {
                    int eachInvest = investMoney / loansToInvest.size();
                    if (i <= howMuchLeft) {
                        eachInvest += 1;
                        i++;
                    }
                    loan.addNewLender(investorName, eachInvest, timeUnit);

                    for (Customer customer : customers) {
                        if (customer.getName().equalsIgnoreCase(investorName)) {
                            customer.addLoanToLenders(loan, eachInvest, timeUnit);
                        }
                    }

                    if (loan.isThereEnoughMoney() && (!(loan.wasTheMoneyTakenAlready()))) {
                        loan.takeMoney(timeUnit);
                        for (Customer customer : customers) {
                            if (customer.getName().equalsIgnoreCase(loan.getOwner()))
                                customer.loadMoneyToAccount(loan.getCapital(), timeUnit);
                        }
                    }
                }


                return howMuchMoneyWasInvested;
            }
        }

        return howMuchMoneyWasInvested;
    }
     */
    public int investMoney(Set<String> loansNamesToInvest , double investMoney, String investorName){

        List<Loan> loansToInvest;
        loansToInvest = getLoansToInvest(loansNamesToInvest);
        String howMuchMoneyWasInvested = "";

        int totalNeededFund = 0;
        totalNeededFund = totalInvest(loansToInvest);
        if(totalNeededFund<investMoney) {
            howMuchMoneyWasInvested = "\nonly " + totalNeededFund + " out of the " + investMoney + " you wanted to invest, was needed for all the loans you asked for.\n";
            investMoney = totalNeededFund;
        }

        int minimumInvestInLoan =0;
        for(Loan loan : loansToInvest){
            if (minimumInvestInLoan == 0 || loan.leftToFund() < minimumInvestInLoan) {
                minimumInvestInLoan = loan.leftToFund();
            }
        }


        while (minimumInvestInLoan * loansToInvest.size() < investMoney && !loansToInvest.isEmpty()){
            int newMinimumInvestInLoan = 0;
            List<Loan> removedLoans = new ArrayList<>();
            for(Loan loan : loansToInvest){
                if(loan.leftToFund() == minimumInvestInLoan){
                    loan.addNewLender(investorName, minimumInvestInLoan , realTimeUnit);

                    loan.takeMoney(realTimeUnit);
                    for (Customer customer : customers){
                        if (customer.getName().equalsIgnoreCase(loan.getOwner()))
                            customer.loadMoneyToAccount(loan.getCapital(), realTimeUnit);

                        if(customer.getName().equalsIgnoreCase(investorName))
                            customer.addLoanToLenders(loan, minimumInvestInLoan , realTimeUnit);

                    }

                    removedLoans.add(loan);
                    investMoney -= minimumInvestInLoan;
                    totalNeededFund -= minimumInvestInLoan;
                }
                else if(newMinimumInvestInLoan == 0 || loan.leftToFund() < newMinimumInvestInLoan)
                    newMinimumInvestInLoan = loan.leftToFund();

            }
            loansToInvest.removeAll(removedLoans);
            minimumInvestInLoan = newMinimumInvestInLoan;
        }
        /////
        if(!loansToInvest.isEmpty()) {
            if (investMoney % loansToInvest.size() == 0) {
                double eachInvest = investMoney / loansToInvest.size();
                for (Loan loan : loansToInvest) {
                    loan.addNewLender(investorName, eachInvest, realTimeUnit);

                    for (Customer customer : customers) {
                        if (customer.getName().equalsIgnoreCase(investorName)) {
                            customer.addLoanToLenders(loan, eachInvest, realTimeUnit);
                        }
                    }

                    if (loan.isThereEnoughMoney() && (!(loan.wasTheMoneyTakenAlready()))) {
                        loan.takeMoney(realTimeUnit);
                        for (Customer customer : customers) {
                            if (customer.getName().equalsIgnoreCase(loan.getOwner()))
                                customer.loadMoneyToAccount(loan.getCapital(), realTimeUnit);
                        }
                    }
                }
            }
            /////
            /////
            else {
                double howMuchLeft = investMoney % loansToInvest.size();
                int i = 0;

                for (Loan loan : loansToInvest) {
                    double eachInvest = investMoney / loansToInvest.size();
                    if (i < howMuchLeft) {
                        eachInvest += 1;
                        i++;
                    }
                    loan.addNewLender(investorName, eachInvest, realTimeUnit);

                    for (Customer customer : customers) {
                        if (customer.getName().equalsIgnoreCase(investorName)) {
                            customer.addLoanToLenders(loan, eachInvest, realTimeUnit);
                        }
                    }

                    if (loan.isThereEnoughMoney() && (!(loan.wasTheMoneyTakenAlready()))) {
                        loan.takeMoney(realTimeUnit);
                        for (Customer customer : customers) {
                            if (customer.getName().equalsIgnoreCase(loan.getOwner()))
                                customer.loadMoneyToAccount(loan.getCapital(), realTimeUnit);
                        }
                    }
                }
                return 200;
            }
        }

        return 200;
    }

    public int investMoneyToLoan(String lenderName, String loanId, double investment_amount)
    {
        System.out.println("investMoneyToLoan lenderName="+lenderName+", loanId="+loanId+", investment_amount="+investment_amount);
        Set<Loan> allLoans = getAllLoans();
        Loan current_loan = null;
        for(Loan loan : allLoans)
        {
            if (loan.getId().equals(loanId))
                current_loan = loan;
        }
        if (current_loan != null)
        {
            System.out.println("current_loan="+current_loan);
            if (current_loan.getStatus().equalsIgnoreCase("new") || current_loan.getStatus().equalsIgnoreCase("pending"))
            {
                int leftToFund = current_loan.leftToFund();
                if (leftToFund < investment_amount)
                {
                    investment_amount = leftToFund;
                }
               current_loan.addNewLender(lenderName,investment_amount,realTimeUnit);
                return 200;
            }
        }
        return 404;
    }

    public String updateYazTime(){
        return updateYazTime(true);
    }

    public void updateDataForRewindMode(){
        updateCustomersDataForRewindMode(this.realTimeUnit);
        updateLoansDataForRewindMode(this.realTimeUnit);
    }
    public String updateYazTime(boolean isPayPayments){
        String res = "the time unit we were in before this act:" + timeUnit + ".\n";
        if(!isRewindMode)
        {
            updateCustomersDataForRewindMode(this.realTimeUnit);
            updateLoansDataForRewindMode(this.realTimeUnit);
            this.realTimeUnit++;
        }

        if(this.realTimeUnit > this.timeUnit)
            this.timeUnit++;
        res += "the time unit we are in after this act:" + timeUnit + ".\n";


        /**updatin the yaz time that was left for the loan*/
        for (Loan loan : loans) {
            loan.setYazTimeLeft();
            if(!loan.getStatus().equals("finished") && loan.theAmountOfPaymentsCreated() < loan.getTotalYazTime()/loan.getPaysEveryYaz())
                loan.createUpcomingPayment(realTimeUnit);
            if(loan.getStatus().equals("risk") && loan.theAmountOfPaymentsCreated() == loan.getTotalYazTime()/loan.getPaysEveryYaz()) {
                loan.setFinalPaymentAtRiskTimePayed();
                loan.setNextYazTimePaymentInRisk(realTimeUnit);
            }
        }
        if (isPayPayments) {
            for (Customer customer : customers) {
                customer.payPayments(realTimeUnit);
            }
            for (Customer customer : customers) {
                customer.getPayment(realTimeUnit);
            }
        }

        for (Loan loan : loans) {
            loan.changeStatus(realTimeUnit);
            loan.updateDebt(realTimeUnit);
        }

        AddNewNotifications();
        return res;
    }

    public int decreaseTimeUnit()
    {
        int status = 200;
        if(isRewindMode)
        {
            if(this.timeUnit > 1)
                this.timeUnit--;
            else
                status = 401;
        }
        else
            status = 404;
        return status;
    }

    public List<Customer> getCustomersDataInRewindMode()
    {
        List<Customer> customerList = new ArrayList<>();
        if (isRewindMode) {
            customerList = customersDataForRewindMode.get(this.timeUnit);
            return customerList;
        }
        return null;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public boolean getRewindMode(){
        return isRewindMode;
    }

    public void setRewindMode(boolean isRewindMode){
        if(isRewindMode == false)
            this.timeUnit = this.realTimeUnit;
        this.isRewindMode = isRewindMode;
    }

    @Nullable
    public CustomerDto getCustomerDtoByName(String name){
        Optional<Customer> oCustomer = customers.stream().filter(customer -> customer.getName().equalsIgnoreCase(name)).findFirst();

        if (oCustomer.isPresent()){
            Customer customer = oCustomer.get();
            return new CustomerDto(customer);
        }
        return null;
    }

    public Set<String> getAllCategories()
    {
        return this.categories;
    }


    public Map<String,Integer> getOwnerSeen(){
        Map<String,Integer> map = new HashMap<>();
        for (Loan loan : getAllLoans()) {
            map.putIfAbsent(loan.getOwner(),0);
            map.computeIfPresent(loan.getOwner(),(s, integer) -> integer+1);
        }
        return Collections.unmodifiableMap(map);
    }

    public void updateCustomersDataForRewindMode(int time_unit){
        if(!isRewindMode){
            List<Customer> customerListForRewindMode = new ArrayList<>();
            for (Customer customer : customers) {
                customerListForRewindMode.add(new Customer(customer));
            }
            customersDataForRewindMode.put(time_unit, customerListForRewindMode);
        }
        for (Integer timeUnit : customersDataForRewindMode.keySet())
        {
            int i = 1;
            System.out.println("Time Unit: " + time_unit);
            for (Customer customer : customersDataForRewindMode.get(timeUnit)){
                System.out.println("Customer Number " + i + "#: " +customer);
                i++;
            }
        }
    }

    public void updateLoansDataForRewindMode(int time_unit){
        if(!isRewindMode){
            List<Loan> loansListForRewindMode = new ArrayList<>();
            for (Loan loan : loans) {
                loansListForRewindMode.add(new Loan(loan));
            }
            loansDataForRewindMode.put(time_unit, loansListForRewindMode);
        }
        for (Integer timeUnit : loansDataForRewindMode.keySet())
        {
            int i = 1;
            System.out.println("Time Unit: " + time_unit);
            for (Loan loan : loansDataForRewindMode.get(timeUnit)){
                System.out.println("Loan Number " + i + "#: " +loan);
                i++;
            }
        }
    }


    public void updateStatusOfLoan(String loanId)
    {
        for (Loan loan : this.loans) {
            if(loan.getId().equalsIgnoreCase(loanId))
            {
                System.out.println("Loan Status Before Change Status" + loan.getStatus());
                loan.changeStatus(realTimeUnit);

                System.out.println("Loan Status After Change Status" + loan.getStatus());
            }
        }
    }
    public void paySelectedLoan(String loanId,String loanerName,String lenderName,double moneyToPay) {
        System.out.println("paySelectedLoan "+loanId+", "+loanerName+", "+lenderName);
        Map<String, Customer> customerMap = customers.stream().collect(Collectors.toMap(Customer::getName, customer -> customer));
        Customer loaner = customerMap.get(loanerName);
        if (loaner != null) {
            Map<String, Loan> loanMap = loaner.getLoanerLoans().stream().collect(Collectors.toMap(Loan::getId, loan -> loan));
            Loan loan = loanMap.get(loanId);
            if (loan != null) {
                loaner.withdrawMoneyFromAccount(moneyToPay, realTimeUnit);
                loan.setLastPaymentAsPayed(realTimeUnit,lenderName,moneyToPay);
                Set<String> lendersNames = loan.getLenders().stream().map(Loan.Lender::getName).collect(Collectors.toSet());
                if (lendersNames.contains(lenderName)) {
                    customerMap.get(lenderName).loadMoneyToAccount(moneyToPay, realTimeUnit);
                }
                //loan.changeStatus(timeUnit);
                loan.updateDebt(realTimeUnit);
                loan.changeAllPaymentsFromLenderAsPayed(lenderName);
                loan.changeStatus(realTimeUnit);
//                if (!loan.getStatus().equals("finished") &&
//                        loan.theAmountOfPaymentsCreated() < loan.getTotalYazTime() / loan.getPaysEveryYaz())
//                    loan.createUpcomingPayment(timeUnit);
            }
        }
    }

    public List<Loan> getThisYazLoansPaymentForCustomer(String name){
        List<Loan> paymentLoans = new ArrayList<>();
        for (Customer customer : customers)
            if (customer.getName().equalsIgnoreCase(name))
                for (Loan loan : customer.getLoanerLoans())
                    if(loan.getLastYazTimePayment() <= this.realTimeUnit)// &&
//                            !loan.wasThisYazTimePaymentPayed(this.timeUnit))//!loan.wasLastPaymentPayed())
                        paymentLoans.add(loan);

        return paymentLoans;
    }

//    public void increaseYaz(){
//        for(Loan loan : loans){
//            loan.setYazTimeLeft();
//            if(!loan.getStatus().equals("finished") && loan.theAmountOfPaymentsCreated() < loan.getTotalYazTime()/loan.getPaysEveryYaz()) //&& /**/loan.wasThisYazTimePaymentPayed(yazTime.getValue()))
//                loan.createUpcomingPayment(this.timeUnit);
//            if(loan.getStatus().equals("risk") && loan.theAmountOfPaymentsCreated() == loan.getTotalYazTime()/loan.getPaysEveryYaz()) {
//                loan.setFinalPaymentAtRiskTimePayed();
//                loan.setNextYazTimePaymentInRisk(this.timeUnit);
//            }
//            loan.changeStatus(this.timeUnit);
//            //loan.checkIfLoanInRisk(this.timeUnit);
//            loan.updateDebt(this.timeUnit);
//        }
//    }


    public List<String> getNotifications(String customerName) {
        for (Customer customer : customers) {
            if(customer.getName().equals(customerName)){
                return customer.getNotifications();
            }
        }
        return null;
    }
    public String getNotificationsAsString(String customerName) {
        if(customerName != null && !customerName.equals("null")) {
            List<String> notifications = getNotifications(customerName);
            return String.join("\n", notifications);
        }
        return null;
    }

    public int addNewNotification(String customerName,String notification){
        for (Customer customer : customers) {
            if(customer.getName().equals(customerName)){
                customer.addNewNotification(notification);
                return 200;
            }
        }
        return 404;
    }

    public int payTheRestOfTheSelectedLoan(String loanId, String loanerName){
        int flag = 0;
        for(Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(loanerName)) {
                for (Loan loan : customer.getLoanerLoans()) {
                    if (loan.getId().equalsIgnoreCase(loanId)) {
                        if(customer.getBalance() < loan.getLoanLeftToPay())
                            return 1;
                        Set<String> lendersNames = loan.getLenders().stream().map(Loan.Lender::getName).collect(Collectors.toSet());
                        for (Customer lender : customers) {
                            if (lendersNames.contains(lender.getName()))
                                lender.loadMoneyToAccount(loan.payToLenderTheRestOfTheLoan(lender.getName()), realTimeUnit);
                        }
                        customer.withdrawMoneyFromAccount(loan.leftToPayForTheLoan(), realTimeUnit);
                        loan.setTheRestOfTheLoanPayed(realTimeUnit);
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public void addNewCustomer(String customerName)
    {
        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setBalance(0);
        this.customers.add(customer);
    }

    public int createNewLoanForCustomer(String customerName, String loanId, String loanCategory,
                                        String loanCapital, String loanTotalYazTime,
                                        String loanPaymentEveryYazTime, String loanInstintPerPayment)
    {
        for(Loan loan : loans)
        {
            if(loan.getId().equalsIgnoreCase(loanId))
                return 400;
        }
        for(Customer customer : customers)
        {
            if (customer.getName().equalsIgnoreCase(customerName))
            {
                Loan loan = customer.addNewLoan(loanId, loanCategory, loanCapital, loanTotalYazTime, loanPaymentEveryYazTime, loanInstintPerPayment);
                loans.add(loan);
                return 200;
            }
        }
        return 404;
    }

    private void AddNewNotifications() {

        Set<Loan> myLoans_update = getAllLoans();
        for (CustomerDto customer : getAllCustomers()) {

            for (Loan loan : myLoans_update) {
                if (!loan.getOwner().equals(customer.getName())) continue;
                if ((loan.getStatus().equals("risk") || loan.getStatus().equals("active")) &&
                        loan.getLastYazTimePayment() == getRealTimeUnit()
                ) {
                    for (Loan.Lender lender : loan.getLenders()) {
                        for (Loan.Payment payment : loan.getPayments()) {
                            if (!payment.getLenderName().equals(lender.getName())) continue;
                            if (!payment.isPayed() && payment.getTimeUnitPayed() == getRealTimeUnit()) {
                                String notifictionMessage = "";
                                notifictionMessage += "Current Time Unit: ";
                                notifictionMessage += getRealTimeUnit();
                                notifictionMessage += "\n";
                                notifictionMessage += "Loan Id: ";
                                notifictionMessage += loan.getId();
                                notifictionMessage += "\n";
                                notifictionMessage += "Lender Name: ";
                                notifictionMessage += lender.getName();
                                notifictionMessage += "\n";
                                notifictionMessage += "Loan Status: ";
                                notifictionMessage += loan.getStatus();
                                notifictionMessage += "\n";
                                notifictionMessage += "Total Invest: ";
                                notifictionMessage += lender.getInvest();
                                notifictionMessage += "\n";
                                notifictionMessage += "Upcoming Payment: ";
                                notifictionMessage += df.format(loan.payToLenderForOneDay(lender.getName()) + payment.getCurrentlyLeftToPay());
                                notifictionMessage += "\n";
                                notifictionMessage += "------------------------------------------\n";
                                addNewNotification(customer.getName(), notifictionMessage);
                            }
                        }
                    }
                }
            }
        }
    }


    public List<Customer.Movement> getMovementListByCustomerName(String customerName) {
        for(Customer customer : customers)
        {
            if (customer.getName().equalsIgnoreCase(customerName))
            {
                return customer.getUnmodifiableMovementList();
            }
        }
        return Collections.emptyList();
    }

    public void putLoanOnSale(String loanId){
        for (Loan loan : this.loans) {
            if (loan.getId().equals(loanId)) {
                loan.setOnSale(true);
                break;
            }
        }
        //if loan at customer didnt update change on client
    }

    public int transferLoanOwnership(String loanId,String newOwnerName, String oldOwnerName){
        double amount = 0;
        //List<Loan.Payment> payments = null;
        Customer customerOldOwner = null;
        Customer customerNewOwner = null;
        Loan current_loan = null;
        for (Customer customer : this.customers)
        {
            if (customer.getName().equalsIgnoreCase(oldOwnerName))
                customerOldOwner = customer;
            if (customer.getName().equalsIgnoreCase(newOwnerName))
                customerNewOwner = customer;
        }
        for (Loan loan : this.loans) {
            if (loan.getId().equals(loanId))
                current_loan = loan;
        }
       //payments.addAll(current_loan.getPayments());

        for (Loan loan : this.loans) {
            if (loan.getId().equals(loanId)) {
                //TODO todo money?

                for (Customer customer : this.customers) {
                    if (customer.getName().equalsIgnoreCase(loan.getOwner())){
                        amount = loan.getLenderInvestLeft(loan.getOwner());
                        System.out.println("loan.getOwner(): " + loan.getOwner());
                        System.out.println("customer.getName(): " + customer.getName());
                        System.out.println("oldOwnerName: " + oldOwnerName);
                        System.out.println("Amount: " + amount);
                        if(customerNewOwner.getBalance() < amount)
                            return 4;
                        customer.getLoanerLoans().removeIf(loan1 -> loan1.equals(loan));
                        System.out.println("transferLoanOwnership");
                        System.out.println("Amount: " + amount);
                        break;
                    }
                }

                int i = 0;
                for (Customer customer : customers)
                {
                    if (customer.getName().equalsIgnoreCase(oldOwnerName) && i < 2) {
                        customer.loadMoneyToAccount(amount, getRealTimeUnit());
                    } else if (customer.getName().equalsIgnoreCase(newOwnerName) && i < 2) {
                        customer.withdrawMoneyFromAccount(amount, getRealTimeUnit());
                    }
                }
                loan.setOnSale(false);
                loan.setOwner(newOwnerName);

                for (Customer customer : this.customers) {
                    if (customer.getName().equalsIgnoreCase(loan.getOwner())){
                        customer.getLoanerLoans().add(loan);
                        break;
                    }
                }
                break;
            }

        }
        return 0;
    }
}


