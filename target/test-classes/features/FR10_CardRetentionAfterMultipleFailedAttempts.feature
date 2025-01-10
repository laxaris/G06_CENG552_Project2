Feature: FR10 - Card Retention After Multiple Failed Attempts
  In order to protect customer security
  As an ATM
  I want to retain the card after three consecutive incorrect password attempts
  And notify the customer to call the bank

  Scenario: Card retained after three consecutive incorrect password attempts (FR10)
    Given the ATM is turned off (FR10)
    When the operator turns on the ATM (FR10)
    And the ATM is running with sufficient cash (FR10)
    And a valid cash card is entered (FR10)
    And the user enters the incorrect password three times consecutively (FR10)
	Then ATM should retain the card and should display a error message instructing the user to call the bank (FR10)
