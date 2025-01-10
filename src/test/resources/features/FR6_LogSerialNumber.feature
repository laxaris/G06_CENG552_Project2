Feature: FR6 - Log Serial Number from Cash Card
  In order to ensure accurate record keeping
  As an ATM
  I want to log the serial number from the cash card when inserted

  Scenario: Valid cash card serial number should be logged (FR6)
    Given the ATM is turned off (FR6)
    When the operator turns on the ATM (FR6)
    And the ATM is running with sufficient cash (FR6)
    And a valid cash card is entered (FR6)
    Then the ATM should log the serial number from the cash card (FR6)
