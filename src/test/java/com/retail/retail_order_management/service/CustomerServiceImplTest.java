
package com.retail.retail_order_management.service;

import com.retail.retail_order_management.dto.request.CustomerRequest;
import com.retail.retail_order_management.dto.response.CustomerResponse;
import com.retail.retail_order_management.entity.Customer;
import com.retail.retail_order_management.exception.CustomerNotFoundException;
import com.retail.retail_order_management.exception.DuplicateEmailException;
import com.retail.retail_order_management.repository.CustomerRepository;
import com.retail.retail_order_management.service.impl.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequest request;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Alina");
        customer.setLastName("Khan");
        customer.setEmail("alina@example.com");
        customer.setPhone("9876543210");
        customer.setAddress("Indore");

        request = new CustomerRequest();
        request.setFirstName("Alina");
        request.setLastName("Khan");
        request.setEmail("alina@example.com");
        request.setPhone("9876543210");
        request.setAddress("Indore");
    }

    @Test
    void createCustomer_Success() {

        when(customerRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        CustomerResponse response =
                customerService.createCustomer(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Alina", response.getFirstName());
        assertEquals("alina@example.com", response.getEmail());

        verify(customerRepository, times(1))
                .save(any(Customer.class));
    }

    @Test
    void createCustomer_DuplicateEmail_ThrowsException() {

        when(customerRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(
                DuplicateEmailException.class,
                () -> customerService.createCustomer(request)
        );

        verify(customerRepository, never())
                .save(any(Customer.class));
    }

    @Test
    void getCustomerById_Success() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        CustomerResponse response =
                customerService.getCustomerById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Alina", response.getFirstName());

        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_NotFound_ThrowsException() {

        when(customerRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.getCustomerById(999L)
        );
    }

    @Test
void getAllCustomers_Success() {

    when(customerRepository.findAll())
            .thenReturn(java.util.List.of(customer));

    java.util.List<CustomerResponse> responses =
            customerService.getAllCustomers();

    assertEquals(1, responses.size());
    assertEquals("Alina", responses.get(0).getFirstName());

    verify(customerRepository).findAll();
}

@Test
void updateCustomer_Success() {

    when(customerRepository.findById(1L))
            .thenReturn(Optional.of(customer));

    when(customerRepository.existsByEmail(request.getEmail()))
            .thenReturn(true);

    when(customerRepository.save(any(Customer.class)))
            .thenReturn(customer);

    CustomerResponse response =
            customerService.updateCustomer(1L, request);

    assertNotNull(response);
    assertEquals("Alina", response.getFirstName());

    verify(customerRepository).save(customer);
}

@Test
void deleteCustomer_Success() {

    when(customerRepository.findById(1L))
            .thenReturn(Optional.of(customer));

    customerService.deleteCustomer(1L);

    verify(customerRepository).delete(customer);
}

}
