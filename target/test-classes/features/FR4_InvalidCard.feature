Feature: Validate Cash Card on ATM
  In order to ensure secure transactions
  As an ATM
  I want to verify the validity of the cash card before accepting it

  Scenario: Expired cash card entered
    Given the ATM is turned off4
    When the operator turns on the ATM4
    And the ATM is running with sufficient cash4
    And an expired cash card is entered4
    Then the ATM should display an error message about the expired card4
    And the ATM should eject the card4

  Scenario: Unreadable cash card entered
    Given the ATM is turned off4
    When the operator turns on the ATM4
    And the ATM is running with sufficient cash4
    And an unreadable cash card is entered4
    Then the ATM should display an error message about the unreadable card4
    And the ATM should eject the card4