package com.restaurantapp.ndnhuy.bdd;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.restaurantapp.ndnhuy.TestcontainersConfiguration;
import com.restaurantapp.ndnhuy.customerservice.CreateCustomerRequest;
import com.restaurantapp.ndnhuy.orderservice.CreateOrderRequest;
import com.restaurantapp.ndnhuy.restaurantservice.CreateRestaurantRequest;

import com.restaurantapp.ndnhuy.restaurantservice.Restaurant;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@Import({TestcontainersConfiguration.class})
@ActiveProfiles({"default", "test", "testcontainers"})
public class RestaurantComponentTestStepDefs {

    private static final String CONTENT_TYPE = "application/json";

    private final TestData testData = new TestData();

    @Setter
    @Getter
    static class TestData {
        private long customerId;
        private long restaurantId;
        private long menuId;
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
                                .firstName("Huy")
                                .lastName("Nguyen")
                                .build()
                )
                .when()
                .post(baseUrl("/customers"))
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getLong("customerId");

        testData.setCustomerId(customerId);
    }

    @Given("^A restaurant has menu of Chicken with amount of 10 USD each$")
    public void givenARestaurantWithMenus() {
        // create restaurant
        var restId = given()
                .when()
                .contentType(CONTENT_TYPE)
                .body(
                        CreateRestaurantRequest
                                .builder()
                                .name("Chicken Restaurant")
                                .menuItems(List.of(
                                        Restaurant.MenuItem
                                                .builder()
                                                .name("chicken 1")
                                                .price(new BigDecimal(10))
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
                                .lineItems(List.of(
                                        CreateOrderRequest.LineItem.builder()
                                                .menuItemId(menuId)
                                                .quantity(1)
                                                .build()
                                ))
                                .build())
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
