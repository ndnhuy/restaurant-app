package com.restaurantapp.ndnhuy.customerservice;

import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.common.CustomerHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestcontainersConfiguration.class})
@ActiveProfiles("test")
public class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerHelper customerHelper;

  @Test
  @SneakyThrows
  public void testCreateCustomer_shouldSuccess() {
    var customerId = customerHelper
        .validCustomer()
        .andExpect(status().isOk())
        .andExpect(jsonPath("customerId").isNotEmpty())
        .andExpect(jsonPath("firstName").value("John"))
        .andExpect(jsonPath("lastName").value("Wick"))
        .thenGetCustomerId();

    assertCustomerIsPersisted(customerId, "John", "Wick");
  }

  @Test
  @SneakyThrows
  public void testCreateCustomer_shouldFail_whenMissingFirstName() {
    customerHelper.createCustomer(CreateCustomerRequest
            .builder()
            .lastName("Wick")
            .build())
        .andExpect(status().isBadRequest());
  }

  @Transactional
  public void assertCustomerIsPersisted(long id, String firstName, String lastName) {
    var obj = customerRepository.findById(id);
    assertThat(obj.isPresent()).isTrue();
    var r = obj.get();
    assertThat(r.getId()).isEqualTo(id);
    assertThat(r.getFirstName()).isEqualTo(firstName);
    assertThat(r.getLastName()).isEqualTo(lastName);
  }

}
