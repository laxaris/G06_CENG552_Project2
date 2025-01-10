Feature: FR11 FR12 FR13 FR14 FR15 FR16 - Withdrawal Transaction
  In order to provide cash withdrawal services
  As an ATM
  I want to allow users to withdraw cash after successful authorization
  And ensure the withdrawal amount is within the allowed transaction limit

  Scenario: Successful withdrawal within transaction limit (FR11)
    Given the ATM is turned off (FR11)
    When the operator turns on the ATM (FR11)
    And the ATM is running with sufficient cash (FR11)
    And a valid cash card is entered (FR11)
    And the user enters the correct password and authorization succeeds (FR11)
    And the user enters an amount within the transaction limit (FR11)
    Then the withdrawal sequence should begin (FR11)
    Then the cash dispenser should dispense the correct amount (FR11)
    Then the log should record the cash dispensing (FR11)
    Then the log should record the successful transaction also balance should be updated (FR11)
    Then the ATM should prompt for another transaction (FR11)

  Scenario: Withdrawal exceeding the transaction limit (FR11)
    Given the ATM is turned off (FR11)
    When the operator turns on the ATM (FR11)
    And the ATM is running with sufficient cash (FR11)
    And a valid cash card is entered (FR11)
    And the user enters the correct password and authorization succeeds (FR11)
    And the user enters an amount exceeding the transaction limit (FR11)
    Then the withdrawal should not proceed (FR11)

  Scenario: Withdrawal below minimum allowed amount (FR11)
    Given the ATM is turned off (FR11)
    When the operator turns on the ATM (FR11)
    And the ATM is running with sufficient cash (FR11)
    And a valid cash card is entered (FR11)
    And the user enters the correct password and authorization succeeds (FR11)
    And the user enters an amount below the minimum allowed amount (FR11)
    Then the ATM should display a message stating "Minimum withdrawal amount not met" (FR11)

  Scenario: Withdrawal exceeding account balance (FR11)
    Given the ATM is turned off (FR11)
    When the operator turns on the ATM (FR11)
    And the ATM is running with sufficient cash (FR11)
    And a valid cash card is entered (FR11)
    And the user enters the correct password and authorization succeeds (FR11)
    And the user enters an amount exceeding their account balance (FR11)
    Then the ATM should display a message stating "Insufficient account balance" (FR11)

  Scenario: Withdrawal exceeding ATM cash availability (FR11)
    Given the ATM is turned off (FR11)
    When the operator turns on the ATM (FR11)
    And the ATM is running with sufficient cash (FR11)
    And a valid cash card is entered (FR11)
    And the user enters the correct password and authorization succeeds (FR11)
    And the user enters an amount exceeding the ATM cash availability (FR11)
    Then the ATM should display a message stating "Insufficient cash in ATM" (FR11)