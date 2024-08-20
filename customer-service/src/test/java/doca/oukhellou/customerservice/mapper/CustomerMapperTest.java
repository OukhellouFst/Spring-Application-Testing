package doca.oukhellou.customerservice.mapper;

import doca.oukhellou.customerservice.dtos.CustomerDTO;
import doca.oukhellou.customerservice.entities.Customer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class CustomerMapperTest {
    CustomerMapper underTest = new CustomerMapper();

    @Test
    public void shouldMapCustomerToCustomerDTO(){
        Customer givenCustomer = Customer.builder()
                .id(1L)
                .firstName("Smail")
                .lastName("Oukhellou")
                .email("oukhellou.fst@gmail.com").build();
        CustomerDTO expected = CustomerDTO.builder()
                .id(1L)
                .firstName("Smail")
                .lastName("Oukhellou")
                .email("oukhellou.fst@gmail.com").build();

        CustomerDTO result = underTest.fromCustomer(givenCustomer);

        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldNotMapNullToCustomerDTO(){
        Customer givenCustomer = null;
        CustomerDTO expected = CustomerDTO.builder()
                .id(1L)
                .firstName("Smail")
                .lastName("Oukhellou")
                .email("oukhellou.fst@gmail.com").build();

        // we are waiting for an exception : IllegalArgumentException
        assertThatThrownBy(()-> underTest.fromCustomer(givenCustomer)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldMapCustomerDTOToCustomer(){
        CustomerDTO givenCustomerDTO = CustomerDTO.builder()
                .id(1L)
                .firstName("Smail")
                .lastName("Oukhellou")
                .email("oukhellou.fst@gmail.com").build();
        Customer expected = Customer.builder()
                .id(1L)
                .firstName("Smail")
                .lastName("Oukhellou")
                .email("oukhellou.fst@gmail.com").build();


        Customer result = underTest.fromCustomerDTO(givenCustomerDTO);

        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldMapListOfCustomersToListOfCustomerDTO(){
        List<Customer> givenCustomers = List.of(
                Customer.builder()
                        .id(1L)
                        .firstName("Smail")
                        .lastName("Oukhellou")
                        .email("oukhellou.fst@gmail.com").build(),
                Customer.builder()
                        .id(2L)
                        .firstName("Khalid")
                        .lastName("Najib")
                        .email("Khalid.najib@gmail.com").build(),
                Customer.builder()
                        .id(3L)
                        .firstName("Hasnae")
                        .lastName("Oukhellou")
                        .email("Hasnae.fst@gmail.com").build()
        );

        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder()
                        .id(1L)
                        .firstName("Smail")
                        .lastName("Oukhellou")
                        .email("oukhellou.fst@gmail.com").build(),
                CustomerDTO.builder()
                        .id(2L)
                        .firstName("Khalid")
                        .lastName("Najib")
                        .email("Khalid.najib@gmail.com").build(),
                CustomerDTO.builder()
                        .id(3L)
                        .firstName("Hasnae")
                        .lastName("Oukhellou")
                        .email("Hasnae.fst@gmail.com").build()
        );

        List<CustomerDTO> result = underTest.fromListCustomers(givenCustomers);

        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }


}