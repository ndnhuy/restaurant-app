package com.restaurantapp.ndnhuy.bdd;

import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import com.restaurantapp.ndnhuy.orderservice.CreateOrderRequest;
import com.restaurantapp.ndnhuy.common.RequestLineItem;
import com.restaurantapp.ndnhuy.paymentservice.CreatePaymentOrderRequest;
import com.restaurantapp.ndnhuy.restaurantservice.AcceptOrderRequest;
import com.restaurantapp.ndnhuy.restaurantservice.CreateRestaurantRequest;
import com.restaurantapp.ndnhuy.restaurantservice.MenuItem;
import com.restaurantapp.ndnhuy.restaurantservice.TicketStatus;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@Import({TestcontainersConfiguration.class})
@ActiveProfiles({"default", "test", "testcontainers"})
public class RestaurantComponentTestStepDefs {

  private static final String CONTENT_TYPE = "application/json";

  private final String SCENARIO_ID = UUID.randomUUID().toString();

  private final TestData testData = new TestData();

  @Setter
  @Getter
  static class TestData {
    private long customerId;
    private long restaurantId;
    private long menuId;
    private Long orderId;
  }

  @LocalServerPort
  private int port;

  private final String host = "localhost";

  private Response response;

  private String baseUrl(String path) {
    return String.format("http://%s:%s%s", host, port, path);
  }

  @Given("^A valid Customer$")
  public void givenAValidCustomer() {
    // create customer
    var customerId = given()
        .when()
        .contentType(CONTENT_TYPE)
        .body(
            CreateCustomerRequest
                .builder()
                .firstName("Huy " + SCENARIO_ID)
                .lastName("Nguyen " + SCENARIO_ID)
                .build()
        )
        .when()
        .post(baseUrl("/customers"))
        .then()
        .statusCode(200)
        .extract().body().jsonPath().getLong("customerId");

    testData.setCustomerId(customerId);
  }

  @Given("A restaurant has menu of Chicken with amount of {double} USD each")
  public void givenARestaurantWithMenus(double itemPrice) {
    // create restaurant
    var restId = given()
        .when()
        .contentType(CONTENT_TYPE)
        .body(
            CreateRestaurantRequest
                .builder()
                .name("Chicken Restaurant " + SCENARIO_ID)
                .menuItems(List.of(
                    MenuItem
                        .builder()
                        .name("chicken 1 " + SCENARIO_ID)
                        .price(new BigDecimal(itemPrice))
                        .build()
                ))
                .build()
        )
        .when()
        .post(baseUrl("/restaurants"))
        .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath()
        .getLong("id");
    testData.setRestaurantId(restId);
  }

  @When("^I place an order for a Chicken$")
  public void customerPlaceOrder() {
    var menuId = given()
        .when()
        .get(baseUrl("/restaurants/" + testData.getRestaurantId()))
        .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath()
        .getLong("menuItems[0].id");

    this.response =
        given()
            .contentType(CONTENT_TYPE)
            .body(CreateOrderRequest.builder()
                .customerId(testData.getCustomerId())
                .restaurantId(testData.getRestaurantId())
                .lineItems(List.of(
                    RequestLineItem.builder()
                        .menuItemId(menuId)
                        .quantity(1)
                        .build()
                ))
                .build())
            .when()
            .post(baseUrl("/orders"));
  }

  @Then("^the order should have status (.*)")
  public void orderShouldBeCreatedAndHaveStatus(String expectedOrderStatus) {
    Long orderId = this.response
        .prettyPeek()
        .then()
        .statusCode(200)
        .extract().jsonPath().getLong("id");
    assertNotNull(orderId);
    assertNotEquals(0L, orderId);
    testData.orderId = orderId;

    String orderStatus = given()
        .when()
        .get(baseUrl("/orders/" + orderId))
        .then()
        .statusCode(200)
        .extract()
        .path("status");
    assertEquals(expectedOrderStatus, orderStatus);
  }

  @And("the order should have amount {double} USD")
  public void orderShouldHaveStatus(double expectedAmount) {
    var jsonConfig = JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE);
    RestAssured.config = RestAssured.config().jsonConfig(jsonConfig);
    given()
        .when()
        .get(baseUrl("/orders/" + testData.orderId))
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("amount", equalTo(expectedAmount));
  }

  @And("a ticket is created")
  public void aTicketIsCreated() {
    given()
        .when()
        .get(baseUrl(String.format("/restaurants/ticket/order/%d", testData.orderId)))
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("status", equalTo(TicketStatus.CREATED.toString()));
  }


  @When("Customer paid for the order")
  public void customerPaidUSDForTheChicken() {
    given()
        .when()
        .contentType(CONTENT_TYPE)
        .body(
            CreatePaymentOrderRequest.builder()
                .orderId(testData.getOrderId())
                .build()
        )
        .post(baseUrl("/payments/createAndPay"))
        .then()
        .statusCode(200);
  }

  @When("Restaurant accepts the order")
  public void restaurantAcceptsTheOrder() {
    given()
        .when()
        .contentType(CONTENT_TYPE)
        .body(
            AcceptOrderRequest.builder()
                .orderId(testData.getOrderId())
                .build()
        )
        .post(baseUrl("/restaurants/accept"))
        .then()
        .statusCode(200);
  }

  @And("Order is assigned to a shipper")
  public void orderIsAssignedToShipper() {

  }

}
