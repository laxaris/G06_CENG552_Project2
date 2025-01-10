package steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;

import atm.ATM;
import atm.Display;
import banking.Money;
import io.cucumber.java.en.*;

public class FR2_IfNoCardInsertedShouldDisplayInitialScreen {

	private ATM atm;
    private Display display;
    private Thread atmThread;

    @Given("the ATM is turned off")
    public void the_ATM_is_turned_off() throws UnknownHostException {
    	  InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        atm = new ATM(1, "Mybank", "mybranch", bankAddress);
        atm.getCashDispenser().setInitialCash(new Money(100000));
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
    }

    @When("the operator turns on the ATM")
    public void the_operator_turns_on_the_atm() {
        atmThread = new Thread(atm);
        atmThread.start();
        
        display.setFakeInput("","1","6");
    }

    @When("no cash card is inserted")
    public void no_cash_card_is_inserted() {
 
        
    }

    @Then("the ATM should display the initial screen message")
    public void the_ATM_should_display_the_initial_screen_message() throws InterruptedException {
    	Thread.sleep(500);
    	boolean messageDisplayed = display.getLastDisplayedMessage().contains("[CARD READER] Enter your 10-digit card number followed by the expiry date (e.g., 1234567890 01-24):");
        assertTrue("Initial screen message not displayed!", messageDisplayed);
    }
}



