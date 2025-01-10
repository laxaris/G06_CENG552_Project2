Feature: FR7 & FR9 - Authorization and Transaction Dialog
  In order to ensure secure transactions
  As an ATM
  I want to verify the password and bank code with the bank computer
  And start the transaction dialog if authorization is successful

  Scenario: Successful authorization with correct password and bank code (FR7, FR9)
    Given the ATM is turned off (FR7, FR9)
    When the operator turns on the ATM (FR7, FR9)
    And the ATM is running with sufficient cash (FR7, FR9)
    And a valid cash card is entered (FR7, FR9)
    And the user enters the correct password (FR7, FR9)
    Then the ATM should verify the bank code and password with the bank computer (FR7, FR9)
    And the ATM should accept the authorization and start the transaction dialog (FR7, FR9)

  Scenario: Failed authorization with incorrect password (FR7, FR9)
    Given the ATM is turned off (FR7, FR9)
    When the operator turns on the ATM (FR7, FR9)
    And the ATM is running with sufficient cash (FR7, FR9)
    And a valid cash card is entered (FR7, FR9)
    And the user enters an incorrect password (FR7, FR9)
    Then the ATM should reject the authorization (FR7, FR9)
