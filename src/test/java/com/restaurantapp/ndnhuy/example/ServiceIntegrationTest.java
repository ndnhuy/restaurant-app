package com.restaurantapp.ndnhuy.example;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.restaurantapp.ndnhuy.restaurantservice.RestaurantController;
import com.restaurantapp.ndnhuy.restaurantservice.RestaurantService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ServiceIntegrationTest {
  @Autowired
  private ServiceLayer serviceLayer;

  @Autowired
  private RestaurantService restaurantService;

  @Autowired
  private RestaurantController restaurantController;

  private static final long EXPECTED_DOCS_COLLECTION_SIZE = 6;

  @Test
  public void whenCallNonTransactionalMethodWithPropertyOff_thenThrowException() {
    serviceLayer.countAllDocsNonTransactional();
  }

  @Test
  public void foo2() {
    // var dto = restaurantService.getRestaurantById(1L);
    // System.out.println(dto);
    restaurantController.getRestaurant(1L);
  }

  @Test
  public void whenCallTransactionalMethodWithPropertyOff_thenTestPass() {
    // SQLStatementCountValidator.reset();

    long docsCount = serviceLayer.countAllDocsTransactional();

    assertEquals(EXPECTED_DOCS_COLLECTION_SIZE, docsCount);

    // SQLStatementCountValidator.assertSelectCount(2);
  }

}
