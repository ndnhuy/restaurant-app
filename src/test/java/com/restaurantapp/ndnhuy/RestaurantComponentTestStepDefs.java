package com.restaurantapp.ndnhuy;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import com.restaurantapp.ndnhuy.orderservice.CreateOrderRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class RestaurantComponentTestStepDefs {

  private static String CONTENT_TYPE = "application/json";

  private static long TEST_CUSTOMER_ID = 1L;
  private static long TEST_RESTAURANT_ID = 1L;

  @LocalServerPort
  private int port;

  private String host = "localhost";

  private Response response;

  private String baseUrl(String path) {
    return String.format("http://%s:%s%s", host, port, path);
  }

  @Given("^A valid Customer$")
  public void givenAValidCustomer() {
    int statusCode = given()
      .when()
      .get(baseUrl("/customers/" + TEST_CUSTOMER_ID))
      .then()
      .extract()
      .statusCode();
    if (statusCode == HttpStatus.SC_NOT_FOUND) {
      // create customer
      given()
        .when()
        .contentType(CONTENT_TYPE)
        .body(
          CreateCustomerRequest
            .builder()
            .firstName("Huy")
            .lastName("Nguyen")
            .build()
        )
        .when()
        .post(baseUrl("/customers"))
        .then()
        .statusCode(200);
    }
  }

  @Given("^A restaurant has menu of Chicken with amount of 10 USD each$")
  public void restaurantAcceptOrder() {
    int statusCode = given()
      .when()
      .get(baseUrl("/restaurants/" + TEST_RESTAURANT_ID))
      .then()
      .extract()
      .statusCode();

    if (statusCode == HttpStatus.SC_NOT_FOUND) {
      // create restaurant 
      given()
        .when()
        .contentType(CONTENT_TYPE)
        .body(
          CreateRestaurantRequest
            .builder()
            .
            .build()
        )
        .when()
        .post(baseUrl("/customers"))
        .then()
        .statusCode(200);
    }
  }

  @When("^I place an order for a Chicken$")
  public void customerPlaceOrder() {
    this.response =
      given()
        .contentType(CONTENT_TYPE)
        .body(CreateOrderRequest.builder().customerId(1).build())
        .when()
        .post(baseUrl("/orders"));
  }

  @Then("^the order should be created and have status (.*)")
  public void orderShouldBeCreatedAndHaveStatus(String expectedOrderStatus) {
    Long orderId =
      this.response.then()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getLong("orderId");
    assertNotNull(orderId);
    assertNotEquals(0L, orderId);
    String orderStatus = given()
      .when()
      .get(baseUrl("/orders/" + orderId))
      .then()
      .statusCode(200)
      .extract()
      .path("status");
    assertEquals(expectedOrderStatus, orderStatus);
  }
}
