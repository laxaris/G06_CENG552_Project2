Feature: Initialize ATM parameters
  In order to ensure correct operation
  As an Operator
  I want to set initial total cash, max withdraw per day, max withdraw per transaction, and min cash to allow transaction.

  Scenario: Set t, k, m, n parameters
    Given the ATM is turned offf
    When the operator turns on the  ATM
    And the operator sets the initial total cash to 10000
    And the operator sets the max withdraw per day to 2000
    And the operator sets the max withdraw per transaction to 1000
    And the operator sets the min cash to allow transaction to 500
    Then the ATM should have initial total cash of 10000
    And the ATM should have max withdraw per day of 2000
    And the ATM should have max withdraw per transaction of 1000
    And the ATM should have min cash to allow transaction of 500
