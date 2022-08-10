package engine.dto;

import engine.Customer;

import java.util.HashSet;
import java.util.Set;

public class CustomersNames {
    private Set<String> customersNamesSet;

    public Set<String> getCustomersNamesSet(Set<Customer> customerSetReceived){
        Set<String> customerNames = new HashSet<>();
        for (Customer customer : customerSetReceived)
            customerNames.add(customer.getName());
        this.customersNamesSet = customerNames;
        return this.customersNamesSet;
    }
}
