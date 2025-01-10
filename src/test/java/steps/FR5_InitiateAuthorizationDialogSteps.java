package steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.net.InetAddress;
import java.net.UnknownHostException;

import atm.ATM;

import atm.Display;import banking.Account;
import banking.Constants;
import banking.DatabaseProxy;
import banking.Money;
import io.cucumber.java.en.*;

public class FR5_InitiateAuthorizationDialogSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;


    @Given("the ATM is turned off \\(FR5)")
    public void fr5_the_ATM_is_turned_off() throws UnknownHostException {
    	Account account1 = new Account(123456, "1234", 50000.0, 0);
    	DatabaseProxy.addAccount(account1);
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        atm = new ATM(1,  Constants.branch, bankAddress);
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
    }

    @When("the operator turns on the ATM \\(FR5)")
    public void fr5_the_operator_turns_on_the_ATM() {
        atmThread = new Thread(atm);
        atmThread.start();
        display.setFakeInput("", "1", "6");
    }

    @When("the ATM is running with sufficient cash \\(FR5)")
    public void fr5_the_ATM_is_running_with_sufficient_cash() {
        atm.getCashDispenser().setInitialCash(new Money(10000));
    }

    @When("a valid cash card is entered \\(FR5)")
    public void fr5_a_valid_cash_card_is_entered() {
        display.setFakeInput("1234123456 12-25"); 
        
    }

    @Then("the ATM should read the serial number and bank code \\(FR5)")
    public void fr5_the_ATM_should_read_the_serial_number_and_bank_code() throws InterruptedException {
    	Thread.sleep(100);
        assertTrue(atm.getCardReader().getIsValidCard());
    }

    @Then("the ATM should initiate the authorization dialog \\(FR5)")
    public void fr5_the_ATM_should_initiate_the_authorization_dialog() {
        String lastDisplayedMessage = atm.getDisplay().getLastDisplayedMessage();
        assertEquals("Please enter your PIN\nThen press ENTER",lastDisplayedMessage);
    }
}
