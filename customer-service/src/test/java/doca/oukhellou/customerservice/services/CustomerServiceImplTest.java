package doca.oukhellou.customerservice.services;

import doca.oukhellou.customerservice.dtos.CustomerDTO;
import doca.oukhellou.customerservice.entities.Customer;
import doca.oukhellou.customerservice.exceptions.CustomerNotFoundException;
import doca.oukhellou.customerservice.exceptions.EmailAlreadyExistException;
import doca.oukhellou.customerservice.mapper.CustomerMapper;
import doca.oukhellou.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl underTest;



    @Test
    void saveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().firstName("Smail").lastName("Oukhellou").email("Oukhellou.fst@gmail.com").build();
        Customer customer = Customer.builder().firstName("Smail").lastName("Oukhellou").email("Oukhellou.fst@gmail.com").build();
        Customer savedCustomer = Customer.builder().id(1L).firstName("Smail").lastName("Oukhellou").email("Oukhellou.fst@gmail.com").build();
        CustomerDTO expected = CustomerDTO.builder().firstName("Smail").lastName("Oukhellou").email("Oukhellou.fst@gmail.com").build();

        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(savedCustomer);
        Mockito.when(customerMapper.fromCustomer(savedCustomer)).thenReturn(expected);

        CustomerDTO result = underTest.saveNewCustomer(customerDTO);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @Test
    void NotSaveNewCustomerWhenEmailExist() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .firstName("smail")
                .lastName("oukhellou")
                .email("oukhellou.fst@gmail.com")
                .build();
        Customer customer = Customer.builder()
                .id(5L)
                .firstName("smail")
                .lastName("oukhellou")
                .email("oukhellou.fst@gmail.com")
                .build();
        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.of(customer));
        assertThatThrownBy(()-> underTest.saveNewCustomer(customerDTO))
                .isInstanceOf(EmailAlreadyExistException.class);
    }

    @Test
    void getAllCustomers() {
        List<Customer> customers = List.of(
                Customer.builder()
                        .firstName("smail")
                        .lastName("oukhellou")
                        .email("oukhellou.fst@gmail.com")
                        .build(),
                Customer.builder()
                        .firstName("khild")
                        .lastName("najib")
                        .email("najib.ff@gmail.com")
                        .build()
        );
        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder()
                        .firstName("smail")
                        .lastName("oukhellou")
                        .email("oukhellou.fst@gmail.com")
                        .build(),
                CustomerDTO.builder()
                        .firstName("khild")
                        .lastName("najib")
                        .email("najib.ff@gmail.com")
                        .build()
        );

        Mockito.when(customerRepository.findAll()).thenReturn(customers);
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);
        List<CustomerDTO> result = underTest.getAllCustomers();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void findCustomerById() {
        Long customerId = 1L;
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("smail")
                .lastName("oukhellou")
                .email("oukhellou.fst@gmail.com")
                .build();
        CustomerDTO expected = CustomerDTO.builder()
                .id(1L)
                .firstName("smail")
                .lastName("oukhellou")
                .email("oukhellou.fst@gmail.com")
                .build();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomer(customer)).thenReturn(expected);
        CustomerDTO result = underTest.findCustomerById(customerId);
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void NotFindCustomerById() {
        Long customerId = 8L;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> {
            underTest.findCustomerById(customerId);
        }).isInstanceOf(CustomerNotFoundException.class).hasMessage(null);
    }

    @Test
    void searchCustomers() {
        String keyword = "m";
        List<Customer> customers = List.of(
                Customer.builder().firstName("smail").lastName("oukhellou").email("oukhellou.fst@gmail.com").build(),
                Customer.builder().firstName("khalid").lastName("najib").email("khalid.najib@gmail.com").build()
        );
        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder().firstName("smail").lastName("oukhellou").email("oukhellou.fst@gmail.com").build(),
                CustomerDTO.builder().firstName("khalid").lastName("najib").email("khalid.najib@gmail.com").build()
        );

        Mockito.when(customerRepository.findByFirstNameContainsIgnoreCase(keyword)).thenReturn(customers);
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);

        List<CustomerDTO> result = underTest.searchCustomers(keyword);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    void updateCustomer() {
        Long customerId = 6L;
        CustomerDTO customerDTO = CustomerDTO.builder().id(6L).firstName("smail").lastName("oukhellou").email("oukhellou.fst@gmail.com").build();
        Customer customer = Customer.builder().id(6L).firstName("smail").lastName("oukhellou").email("oukhellou.fst@gmail.com").build();
        Customer updatedCustomer = Customer.builder().id(6L).firstName("smail").lastName("oukhellou").email("oukhellou.fst@gmail.com").build();
        CustomerDTO expected = CustomerDTO.builder().id(6L).firstName("smail").lastName("oukhellou").email("oukhellou.fst@gmail.com").build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(updatedCustomer);
        Mockito.when(customerMapper.fromCustomer(updatedCustomer)).thenReturn(expected);

        CustomerDTO result = underTest.updateCustomer(customerId,customerDTO);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void deleteCustomer() {
        Long customerId = 1L;
        Customer customer = Customer.builder()
                .id(6L)
                .firstName("smail")
                .lastName("oukhellou")
                .email("oukhellou.fst@gmail.com")
                .build();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        underTest.deleteCustomer(customerId);
        Mockito.verify(customerRepository).deleteById(customerId);
    }

    void NotDeleteCustomerNotExist() {
        Long customerId = 9L;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.deleteCustomer(customerId)).isInstanceOf(CustomerNotFoundException.class);
    }

}