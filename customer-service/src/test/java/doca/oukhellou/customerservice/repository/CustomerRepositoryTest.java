package doca.oukhellou.customerservice.repository;

import doca.oukhellou.customerservice.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        System.out.println("\n\n\n-----------------------------------");
        customerRepository.save(Customer.builder()
                .firstName("Smail")
                .lastName("Oukhellou")
                .email("oukhellou.smail@gmail.com")
                .build()
        );
        customerRepository.save(Customer.builder()
                .firstName("Khalid")
                .lastName("Najib")
                .email("Khalid.najib@gmail.com")
                .build()
        );
        customerRepository.save(Customer.builder()
                .firstName("Nourdine")
                .lastName("Najib")
                .email("Nourdine.najib@gmail.com")
                .build()
        );
        System.out.println("---------------------------------------\n\n\n");
    }

    @Test
    public void shouldFindCustomerByEmail() {
        String givenEmail = "oukhellou.smail@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        assertThat(result).isPresent();
    }

    @Test
    public void shouldNotFindCustomerByEmail() {
        String givenEmail = "xxxx.xxx@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindCustomersByFirstName() {
        String keyword = "l";
        List<Customer> expected = List.of(
                Customer.builder()
                        .firstName("Smail")
                        .lastName("Oukhellou")
                        .email("oukhellou.smail@gmail.com")
                        .build()
                ,
                Customer.builder()
                        .firstName("Khalid")
                        .lastName("Najib")
                        .email("Khalid.najib@gmail.com").build()
        );
        List<Customer> result = customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }
}
