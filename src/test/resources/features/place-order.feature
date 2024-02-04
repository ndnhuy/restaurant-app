Feature: Place Order
  As a customer
  I should be able to place an order

  Scenario: Order is created
    When I place an order
    Then the order should be created
    And the order should have status init
