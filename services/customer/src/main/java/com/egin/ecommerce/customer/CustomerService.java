package com.egin.ecommerce.customer;

import com.egin.ecommerce.exception.CustomerNotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(@Valid CustomerRequest request) {
        var customer = customerRepository.save(customerMapper.toCustomer(request));

        return customer.getId();
    }


    public void updateCustomer(@Valid CustomerRequest request) {

        var customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                    format("Customer with id %s not found", request.id())
                ));
        mergerCustomer(customer, request);
        customerRepository.save(customer);
    }

    private void mergerCustomer(Customer customer, @Valid CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName())) {
            customer.setFirstName(request.firstName());
        }
        if(StringUtils.isNotBlank(request.lastName())) {
            customer.setLastName(request.lastName());
        }
        if(StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if(request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existsCustomerById(String customerId) {
        return customerRepository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return customerRepository
                .findById(customerId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException("No customer found with provided id"));
    }

    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }
}
