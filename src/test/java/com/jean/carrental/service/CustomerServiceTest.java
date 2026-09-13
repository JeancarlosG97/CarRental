package com.jean.carrental.service;

import com.jean.carrental.dto.CustomerDTO;
import com.jean.carrental.exception.CustomerNotFoundException;
import com.jean.carrental.model.Customer;
import com.jean.carrental.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.jean.carrental.model.Role.ADMIN;
import static com.jean.carrental.model.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldReturnAllCustomers() {

        Customer c1 = new Customer();
        c1.setId(1);
        c1.setEmail("Jean@Jean.com");
        c1.setRole(ADMIN);

        Customer c2 = new Customer();
        c2.setId(2);
        c2.setEmail("Ange@Ange.com");
        c2.setRole(USER);

        when(customerRepository.findAll())
                .thenReturn(List.of(c1, c2));

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getId());
        assertEquals("Jean@Jean.com", result.get(0).getEmail());
        assertEquals("ADMIN", result.get(0).getRole());

        assertEquals(2, result.get(1).getId());
        assertEquals("Ange@Ange.com", result.get(1).getEmail());
        assertEquals("USER", result.get(1).getRole());

        verify(customerRepository).findAll();
    }

    @Test
    void shouldReturnCustomerWhenCustomerExists() {

        Customer c1 = new Customer();
        c1.setId(1);
        c1.setRole(ADMIN);
        c1.setEmail("Jean@Jean.com");

        when(customerRepository.findById(1))
                .thenReturn(Optional.of(c1));

        CustomerDTO result = customerService.getCustomerById(1);

        assertEquals(1, result.getId());
        assertEquals("ADMIN", result.getRole());
        assertEquals("Jean@Jean.com", result.getEmail());

        verify(customerRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {

        when(customerRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.getCustomerById(1)
        );

        verify(customerRepository).findById(1);
    }

    @Test
    void shouldAddCustomer() {

        Customer customer = new Customer();
        customer.setRole(ADMIN);

        when(customerRepository.save(customer))
                .thenReturn(customer);

        CustomerDTO result = customerService.addCustomer(customer);

        assertEquals("USER", result.getRole());

        verify(customerRepository).save(customer);
    }

    @Test
    void shouldUpdateCustomerWhenCustomerExists() {

        Customer existingCustomer = new Customer();
        existingCustomer.setId(1);
        existingCustomer.setName("Jean");
        existingCustomer.setEmail("Jean@jean.com");
        existingCustomer.setPhoneNumber("8573891606");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Jeancarlos");
        updatedCustomer.setEmail("JeanG@jean.com");
        updatedCustomer.setPhoneNumber("8573891607");

        when(customerRepository.findById(1))
                .thenReturn(Optional.of(existingCustomer));

        when(customerRepository.save(existingCustomer))
                .thenReturn(existingCustomer);

        CustomerDTO result = customerService.updateCustomer(1, updatedCustomer);

        assertEquals(1, result.getId());
        assertEquals("Jeancarlos", result.getName());
        assertEquals("JeanG@jean.com", result.getEmail());
        assertEquals("8573891607", result.getPhoneNumber());

        verify(customerRepository).findById(1);
        verify(customerRepository).save(existingCustomer);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingCustomer() {

        when(customerRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.updateCustomer(1, new Customer())
        );

        verify(customerRepository).findById(1);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldDeleteCustomerWhenCustomerExists() {

        Customer customer = new Customer();
        customer.setId(1);

        when(customerRepository.findById(1))
                .thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1);

        verify(customerRepository).findById(1);
        verify(customerRepository).delete(customer);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingCustomer() {

        when(customerRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.deleteCustomer(1)
        );

        verify(customerRepository).findById(1);
        verify(customerRepository, never()).delete(any());
    }

    @Test
    void shouldReturnCustomerWhenCustomerEmailExists() {

        Customer customer = new Customer();
        customer.setId(1);
        customer.setEmail("jean@jean.com");
        customer.setRole(ADMIN);

        when(customerRepository.findByEmail("jean@jean.com"))
                .thenReturn(Optional.of(customer));

        CustomerDTO result =
                customerService.getCustomerByEmail("jean@jean.com");

        assertEquals(1, result.getId());
        assertEquals("jean@jean.com", result.getEmail());
        assertEquals("ADMIN", result.getRole());

        verify(customerRepository)
                .findByEmail("jean@jean.com");
    }

    @Test
    void shouldThrowExceptionWhenCustomerEmailDoesNotExist() {

        when(customerRepository.findByEmail("missing@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.getCustomerByEmail("missing@email.com")
        );

        verify(customerRepository)
                .findByEmail("missing@email.com");
    }

    @Test
    void shouldUpdateCustomerByEmail() {

        Customer existingCustomer = new Customer();
        existingCustomer.setName("Jean");
        existingCustomer.setPhoneNumber("111");
        existingCustomer.setPassword("oldPassword");
        existingCustomer.setEmail("jean@jean.com");
        existingCustomer.setRole(USER);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Jeancarlos");
        updatedCustomer.setPhoneNumber("222");
        updatedCustomer.setPassword("newPassword");

        when(customerRepository.findByEmail("jean@jean.com"))
                .thenReturn(Optional.of(existingCustomer));

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("encodedPassword");

        when(customerRepository.save(existingCustomer))
                .thenReturn(existingCustomer);

        CustomerDTO result = customerService.updateCustomerByEmail("jean@jean.com", updatedCustomer);

        assertEquals("encodedPassword", existingCustomer.getPassword());
        assertEquals("Jeancarlos", result.getName());
        assertEquals("222", result.getPhoneNumber());
        assertEquals("encodedPassword", existingCustomer.getPassword());

        verify(customerRepository).findByEmail("jean@jean.com");
        verify(passwordEncoder).encode("newPassword");
        verify(customerRepository).save(existingCustomer);
    }
}
