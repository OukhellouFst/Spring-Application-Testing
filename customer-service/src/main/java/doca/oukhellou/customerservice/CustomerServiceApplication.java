package doca.oukhellou.customerservice;

import doca.oukhellou.customerservice.entities.Customer;
import doca.oukhellou.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Profile("main")
    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {

        return args -> {
            customerRepository.save(Customer.builder()
                    .firstName("Smail")
                    .lastName("Oukhellou")
                    .email("oukhellou.fst@gmail.com")
                    .build()
            );
            customerRepository.save(Customer.builder()
                    .firstName("Khalid")
                    .lastName("Najib")
                    .email("khalid.arm@gmail.com")
                    .build()
            );
            customerRepository.save(Customer.builder()
                    .firstName("Nourdine")
                    .lastName("Najib")
                    .email("Nourdine.khdr@gmail.com")
                    .build()
            );
        };
    }
}
