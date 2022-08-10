package engine;

//import engine.generated.AbsCustomer;
import engine.generated.AbsDescriptor;
import engine.generated.AbsLoan;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Xml {


   // private Set<Customer> customerSet = new HashSet<>();

  /*
    public Set<Customer> getCustomerSet(String xmlPath) throws JAXBException {
        this.file = new File(xmlPath);

        JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AbsDescriptor absDescriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);
        this.customerSet = new HashSet<>();
        for (AbsCustomer absCustomer : absDescriptor.getAbsCustomers().getAbsCustomer()){
            Customer customer = new Customer();
            customer.setBalance(absCustomer.getAbsBalance());
            customer.setName(absCustomer.getName());
            this.customerSet.add(customer);
        }
        return customerSet;
    }

        public Set<Customer> getCustomerSet(String xmlPath){
        try {
            this.file = new File(xmlPath);

            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AbsDescriptor absDescriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);
            this.customerSet = new HashSet<>();
            for (AbsCustomer absCustomer : absDescriptor.getAbsCustomers().getAbsCustomer()){
                Customer customer = new Customer();
                customer.setBalance(absCustomer.getAbsBalance());
                customer.setName(absCustomer.getName());
                this.customerSet.add(customer);
            }
        }catch (JAXBException e) {
            System.out.println("\nXML FILE WAS NOT FOUND\n");
        }
        return customerSet;
    }
     */

    public static Set<String> getCategoriesSet(String xmlPath) throws JAXBException {
        File file = new File(xmlPath);

        JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AbsDescriptor absDescriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);

        return new HashSet<>(absDescriptor.getAbsCategories().getAbsCategory());
    }

    /*
        public Set<String> getCategoriesSet(String xmlPath){
        try {
            this.file = new File(xmlPath);

            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AbsDescriptor absDescriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);
            this.categoriesSet = new HashSet<>();
            this.categoriesSet.addAll(absDescriptor.getAbsCategories().getAbsCategory());
        }catch (JAXBException e) {
            System.out.println("\nXML FILE WAS NOT FOUND\n");
        }

        return categoriesSet;
    }
     */

    public static Set<Loan> getLoansList(String xmlPath) throws JAXBException {
        File file = new File(xmlPath);

        JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AbsDescriptor absDescriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);
        Set<Loan> loansList = new HashSet<>();
        for(AbsLoan absLoan : absDescriptor.getAbsLoans().getAbsLoan()){
            Loan newLoan = new Loan(null,
                    absLoan.getAbsCategory(),
                    absLoan.getAbsCapital(),
                    absLoan.getAbsTotalYazTime(),
                    absLoan.getAbsPaysEveryYaz(),
                    absLoan.getAbsIntristPerPayment(),
                    absLoan.getId()
            );
            loansList.add(newLoan);
        }
        return loansList;
    }
/*
    public List<Loan> getLoansList(String xmlPath){
        try {
            this.file = new File(xmlPath);

            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AbsDescriptor absDescriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);
            this.loansList = new ArrayList<>();
            for(AbsLoan absLoan : absDescriptor.getAbsLoans().getAbsLoan()){
                Loan newLoan = new Loan();
                newLoan.setId(absLoan.getId());
                newLoan.setOwner(absLoan.getAbsOwner());
                newLoan.setCategory(absLoan.getAbsCategory());
                newLoan.setCapital(absLoan.getAbsCapital());
                newLoan.setTotalYazTime(absLoan.getAbsTotalYazTime());
                newLoan.setPaysEveryYaz(absLoan.getAbsPaysEveryYaz());
                newLoan.setIntristPerPayment(absLoan.getAbsIntristPerPayment());
                this.loansList.add(newLoan);
            }

        }catch (JAXBException e) {
            System.out.println("\nXML FILE WAS NOT FOUND\n");
        }
        return loansList;
    }

 */
}

