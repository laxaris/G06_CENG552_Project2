package steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import atm.ATM;

import atm.Display;
import atm.NetworkToBank;
import banking.Account;
import banking.Constants;
import banking.DatabaseProxy;
import banking.Money;
import io.cucumber.java.en.*;

public class FR7_FR9_AuthorizationAndTransactionSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;
    private NetworkToBank networkToBank;

    @Given("the ATM is turned off \\(FR7, FR9)")
    public void the_ATM_is_turned_off() throws UnknownHostException {
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        Account account1 = new Account(123456, "1234", 50000.0, 0);
        DatabaseProxy.addAccount(account1);
        atm = new ATM(1,  Constants.branch ,bankAddress);
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
        networkToBank = atm.getNetworkToBank();
    }

    @When("the operator turns on the ATM \\(FR7, FR9)")
    public void the_operator_turns_on_the_ATM() {
        atmThread = new Thread(atm);
        atmThread.start();
        display.setFakeInput("", "1", "6");
    }

    @When("the ATM is running with sufficient cash \\(FR7, FR9)")
    public void the_ATM_is_running_with_sufficient_cash() {
        atm.getCashDispenser().setInitialCash(new Money(10000));
    }

    @When("a valid cash card is entered \\(FR7, FR9)")
    public void a_valid_cash_card_is_entered() {
        display.setFakeInput("1234123456 12-25");
      
    }

    @When("the user enters the correct password \\(FR7, FR9)")
    public void the_user_enters_the_correct_password() {
        display.setFakeInput("1234"); 

    }

    @When("the user enters an incorrect password \\(FR7, FR9)")
    public void the_user_enters_an_incorrect_password() {
        display.setFakeInput("9999");  

    }

    @Then("the ATM should verify the bank code and password with the bank computer \\(FR7, FR9)")
    public void the_ATM_should_verify_the_bank_code_and_password_with_the_bank_computer() throws InterruptedException {
    	Thread.sleep(100);
    	assertTrue(atm.getSession().getIsAuthorizatinsuccesful());
    }

    @Then("the ATM should accept the authorization and start the transaction dialog \\(FR7, FR9)")
    public void the_ATM_should_accept_the_authorization_and_start_the_transaction_dialog() {
     String lastDisplayedMessage = atm.getDisplay().getLastDisplayedMessage();
     assertEquals("Please choose transaction type",lastDisplayedMessage);
    }

    @Then("the ATM should reject the authorization \\(FR7, FR9)")
    public void the_ATM_should_reject_the_authorization() throws InterruptedException {
    	Thread.sleep(100);
    	assertNotEquals(0,atm.getSession().getInvalidPinAttempts());
    }
}
