package com.restaurantapp.ndnhuy.customerservice.restaurantservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import com.restaurantapp.ndnhuy.customerservice.CustomerRepository;
import com.restaurantapp.ndnhuy.customerservice.CustomerService;
import com.restaurantapp.ndnhuy.restaurantservice.CreateRestaurantRequest;
import com.restaurantapp.ndnhuy.restaurantservice.Restaurant.MenuItem;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantRepository;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import( { TestcontainersConfiguration.class })
@ActiveProfiles("test")
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @SneakyThrows
    public void testCreateCustomer_shouldSuccess() {
        this.mockMvc.perform(
                        post("/customers")
                                .contentType("application/json")
                                .content(
                                        asJsonString(
                                                CreateCustomerRequest
                                                        .builder()
                                                        .firstName("John")
                                                        .lastName("Wick")
                                                        .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("customerId").value(1L))
                .andExpect(jsonPath("firstName").value("John"))
                .andExpect(jsonPath("lastName").value("Wick"));

        assertCustomerIsPersisted(1L, "John", "Wick");
    }

    @Test
    @SneakyThrows
    public void testCreateCustomer_shouldFail_whenMissingFirstName() {
        this.mockMvc.perform(
                        post("/customers")
                                .contentType("application/json")
                                .content(
                                        asJsonString(
                                                CreateCustomerRequest
                                                        .builder()
                                                        .lastName("Wick")
                                                        .build())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Transactional
    public void assertCustomerIsPersisted(long id, String firstName, String lastName) {
        var obj = customerRepository.findById(id);
        assertThat(obj.isPresent()).isTrue();
        var r = obj.get();
        assertThat(r.getId()).isEqualTo(1);
        assertThat(r.getFirstName()).isEqualTo(firstName);
        assertThat(r.getLastName()).isEqualTo(lastName);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
