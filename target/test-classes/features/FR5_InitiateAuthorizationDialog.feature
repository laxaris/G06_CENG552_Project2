Feature: FR5 - Initiate Authorization Dialog
  In order to ensure secure transactions
  As an ATM
  I want to read the serial number and bank code from a valid cash card 
  And initiate the authorization dialog

  Scenario: Valid cash card entered (FR5)
    Given the ATM is turned off (FR5)
    When the operator turns on the ATM (FR5)
    And the ATM is running with sufficient cash (FR5)
    And a valid cash card is entered (FR5)
    Then the ATM should read the serial number and bank code (FR5)
    And the ATM should initiate the authorization dialog (FR5)
