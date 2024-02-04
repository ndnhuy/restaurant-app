package com.restaurantapp.ndnhuy;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.restaurantapp.ndnhuy.orderservice.CreateOrderRequest;
import com.restaurantapp.ndnhuy.orderservice.OrderStatus;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class RestaurantComponentTestStepDefs {

  @LocalServerPort
  private int port;

  private String host = "localhost";

  private Response response;

  private String baseUrl(String path) {
    return String.format("http://%s:%s%s", host, port, path);
  }

  @When("^I place an order$")
  public void customerPlaceOrder() {
    this.response =
      given()
        .contentType("application/json")
        .body(CreateOrderRequest.builder().customerId(1).build())
        .when()
        .post(baseUrl("/orders"));
  }

  @Then("^the order should be created$")
  public void orderShouldBeCreated() {
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
    assertEquals("INIT", orderStatus);
  }

  @And("^the order should have status init")
  public void orderShouldHaveINITstatus() {}
}
