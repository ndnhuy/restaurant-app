Feature: Place Order
  As a customer
  I should be able to place an order

  Scenario: Order is created
    Given A valid Customer
    Given A restaurant has menu of Chicken with amount of 10 USD each
    When I place an order for a Chicken
    Then the order should be created and have status INIT
