Feature: Balance Inquiry and Deposit Transactions (FR-Bonus)
  In order to provide complete banking services
  As an ATM
  I want to allow users to inquire their balance and make deposits successfully

  Scenario: Successful balance inquiry and deposit transaction (FR-Bonus)
    Given the ATM is turned off (FR-Bonus)
    When the operator turns on the ATM (FR-Bonus)
    And the ATM is running with sufficient cash (FR-Bonus)
    And a valid cash card is entered (FR-Bonus)
    And the user enters the correct password and authorization succeeds (FR-Bonus)
    And the user performs a balance inquiry (FR-Bonus)
    Then the ATM should display the current balance (FR-Bonus)
    When the user initiates a deposit transaction (FR-Bonus)
    And the user deposits a valid amount (FR-Bonus)
    Then the ATM should confirm the deposit and update the balance also cash on the dispenser (FR-Bonus)
    When the user performs another balance inquiry (FR-Bonus)
    Then the ATM should display the updated balance including the deposit (FR-Bonus)
