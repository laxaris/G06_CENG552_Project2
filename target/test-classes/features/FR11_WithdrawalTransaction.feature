Feature: FR11 - Withdrawal Transaction
  In order to provide cash withdrawal services
  As an ATM
  I want to allow users to withdraw cash after successful authorization
  And ensure the withdrawal amount is within the allowed transaction limit

  Scenario: Successful withdrawal within transaction limit (FR11)
    Given the ATM is turned off (FR11)
    When the operator turns on the ATM (FR11)
    And the ATM is running with sufficient cash (FR11)
    And a valid cash card is entered (FR11)
    And the user enters the correct password (FR11)
    And the user enters an amount within the transaction limit (FR11)
    Then the withdrawal sequence should begin (FR11)

  Scenario: Withdrawal exceeding the transaction limit (FR11)
    Given the ATM is turned off (FR11)
    When the operator turns on the ATM (FR11)
    And the ATM is running with sufficient cash (FR11)
    And a valid cash card is entered (FR11)
    And the user enters the correct password (FR11)
    And the user enters an amount exceeding the transaction limit (FR11)
    Then the withdrawal should not proceed (FR11)
