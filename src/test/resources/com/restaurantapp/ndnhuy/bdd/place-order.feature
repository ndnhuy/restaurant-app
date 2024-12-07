Feature: Place Order
  As a customer
  I should be able to place an order

  Scenario: Customer places an order successfully
    Given A valid Customer
    Given A restaurant has menu of Chicken with amount of 10 USD each
    When I place an order for a Chicken
    Then the order should have status CREATED
    And the order should have amount 10 USD
    When Customer paid 10 USD for the Chicken
    Then the order should have status PAID
