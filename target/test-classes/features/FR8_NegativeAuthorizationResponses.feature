Feature: FR8 - Negative Authorization Responses from Bank
  In order to ensure security and proper user feedback
  As an ATM
  I want to eject the card and display an error message when receiving negative responses from the bank

  Scenario: Authorization failure due to bad bank code (FR8)
    Given the ATM is turned off (FR8)
    When the operator turns on the ATM (FR8)
    And the ATM is running with sufficient cash (FR8)
    And a cash card from an unsupported bank is entered (FR8)
    Then the ATM should display an error message about bad bank code (FR8)
    And the ATM should eject the card (FR8)

  Scenario: Authorization failure due to bad account (FR8)
    Given the ATM is turned off (FR8)
    When the operator turns on the ATM (FR8)
    And the ATM is running with sufficient cash (FR8)
    And a cash card with account issues is entered (FR8)
    Then the ATM should display an error message about bad account (FR8)
    And the ATM should eject the card (FR8)
