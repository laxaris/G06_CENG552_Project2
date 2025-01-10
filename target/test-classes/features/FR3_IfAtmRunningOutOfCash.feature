Feature: ATM Error Handling When Running Out of Money
  In order to prevent failed transactions
  As an ATM
  I want to reject cards when running out of money and display an error message

  Scenario: ATM running out of money
    Given the ATM is turned off3
    When the operator turns on the ATM3
    And the ATM is running out of money
    And a card is inserted
    Then the ATM should display an error message about insufficient funds
    And the ATM should eject the card
