Feature: Initialize ATM parameters
  In order to ensure correct operation
  As an Operator
  I want to set initial total cash, max withdraw per day, etc.

  Scenario: Set t, k, m, n parameters
    Given the ATM is turned off
    When the operator turns on the ATM
    And the operator sets the initial total cash to 10000
    And the operator sets the max withdraw per day to 2000
    Then the ATM should have initial total cash of 10000
    And the ATM should have max withdraw per day of 2000

