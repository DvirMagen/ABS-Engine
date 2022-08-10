package engine.dto;

import engine.Customer;

import java.util.List;

public class CustomerDto {
    double balance;
    String name;

    private final List<Customer.Movement> movementList;

    public CustomerDto(Customer customer){
        balance = customer.getBalance();
        name = customer.getName();
        movementList = customer.getUnmodifiableMovementList();
    }

    protected CustomerDto(CustomerDto customerDto) {
        balance = customerDto.getBalance();
        name = customerDto.getName();
        movementList = customerDto.getMovementList();
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String customerName){ this.name = customerName;}
    public List<Customer.Movement> getMovementList() {
        return movementList;
    }

    @Override
    public String toString() {
        return name;
    }
}
