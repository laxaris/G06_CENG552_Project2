Feature: ATM Initial Display Without Card
  In order to ensure proper user guidance
  As an ATM
  I want to display the initial screen when no cash card is present

  Scenario: No cash card present
    Given the ATM is turned off
    When the operator turns on the ATM
    And no cash card is inserted
    Then the ATM should display the initial screen message
