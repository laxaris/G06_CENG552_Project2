package steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.net.InetAddress;
import java.net.UnknownHostException;

import atm.ATM;

import atm.Display;
import banking.Constants;
import banking.Money;
import io.cucumber.java.en.*;

public class FR4_InvalidCard {

	private ATM atm;
    private Display display;
    private Thread atmThread;


    @Given("the ATM is turned off4")
    public void the_ATM_is_turned_off() throws UnknownHostException {
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        atm = new ATM(1,  Constants.branch ,bankAddress);
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
    }

    @When("the operator turns on the ATM4")
    public void the_operator_turns_on_the_ATM() {
        atmThread = new Thread(atm);
        atmThread.start();
        display.setFakeInput("", "1", "6");
    }

    @When("the ATM is running with sufficient cash4")
    public void the_ATM_is_running_with_sufficient_cash() {
        atm.getCashDispenser().setInitialCash(new Money(10000));
    }

    @When("an expired cash card is entered4")
    public void an_expired_cash_card_is_entered() {
        display.setFakeInput("1234567890 01-20"); // Expired card
        
    }

    @When("an unreadable cash card is entered4")
    public void an_unreadable_cash_card_is_entered() {
        display.setFakeInput("12ABCD567X 12-25"); // Unreadable card
        
    }

 
    @Then("the ATM should display an error message about the expired card4")
    public void the_ATM_should_display_an_error_message_about_the_expired_card() throws InterruptedException {
    	Thread.sleep(500);
        assertTrue("Error message for expired card not displayed!", atm.getCardReader().getIsExpiredCard());
    }

    @Then("the ATM should display an error message about the unreadable card4")
    public void the_ATM_should_display_an_error_message_about_the_unreadable_card() throws InterruptedException {
    	Thread.sleep(500);
        
        assertTrue("Error message for unreadable card not displayed!", atm.getCardReader().getIsInvalidCard());
    }

    @Then("the ATM should eject the card4")
    public void the_ATM_should_eject_the_card() {
        String message = display.getLastDisplayedMessage();
        assertEquals("[CARD READER] Enter your 10-digit card number followed by the expiry date (e.g., 1234567890 01-24):",message);
    }

}




