package doca.oukhellou.customerservice.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import doca.oukhellou.customerservice.dtos.CustomerDTO;
import doca.oukhellou.customerservice.exceptions.CustomerNotFoundException;
import doca.oukhellou.customerservice.services.CustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(CustomerRestController.class)
class CustomerRestControllerTest {
    @MockBean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    List<CustomerDTO> customerDTOS;

    @BeforeEach
    void setUp() {
        this.customerDTOS = List.of(
                CustomerDTO.builder().id(1L).firstName("smail").lastName("oukhellou").email("oukhellou.fst@gmail.com").build(),
                CustomerDTO.builder().id(2L).firstName("khalid").lastName("najib").email("khalid.fst@gmail.com").build(),
                CustomerDTO.builder().id(3L).firstName("mehdi").lastName("kari").email("mehdi.fst@gmail.com").build()
        );
    }

    @Test
    void getAllCustomers() throws Exception {
        Mockito.when(customerService.getAllCustomers()).thenReturn(customerDTOS);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", org.hamcrest.Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTOS)));
        ;
    }

    @Test
    void getCustomerById() throws Exception {
        Long id = 1L;
        Mockito.when(customerService.findCustomerById(id)).thenReturn(customerDTOS.get(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTOS.get(0))));
    }

    @Test
    void NotGetCustomerByInvalidId() throws Exception {
        Long id = 10L;
        Mockito.when(customerService.findCustomerById(id)).thenThrow(CustomerNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void searchCustomers() throws Exception {
        String keyword = "i";
        Mockito.when(customerService.searchCustomers(keyword)).thenReturn(customerDTOS);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/search?keyword=",keyword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect( MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTOS)));
    }

    @Test
    void saveCustomer() throws  Exception{
        CustomerDTO customerDTO = customerDTOS.get(0);
        String expected = """
                { "id":1, "firstName":"smail","lastName":"oukhellou", "email":"oukhellou.fst@gmail.com"}
                """;
        Mockito.when(customerService.saveNewCustomer(Mockito.any())).thenReturn(customerDTOS.get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));

    }

    @Test
    void updateCustomer() throws Exception {
        Long customerId = 1L;
        CustomerDTO customerDTO = customerDTOS.get(0);
        Mockito.when(customerService.updateCustomer(Mockito.eq(customerId),Mockito.any())).thenReturn(customerDTOS.get(0));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/{id}",customerId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTO)));
    }

    @Test
    void deleteCustomer() throws Exception {
        Long customerId = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/{id}",customerId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}