package steps;

import static org.junit.Assert.assertEquals;


import java.net.InetAddress;
import java.net.UnknownHostException;

import atm.ATM;
import atm.Display;
import banking.Account;
import banking.Constants;
import banking.DatabaseProxy;
import banking.Money;
import io.cucumber.java.en.*;

public class FR3_IfAtmRunningOutOfCash {

	 private ATM atm;
	    private Display display;
	    private Thread atmThread;

	    @Given("the ATM is turned off3")
	    public void the_ATM_is_turned_off() throws UnknownHostException {
	    	Account account1 = new Account(123456, "1234", 50000.0, 0);
            Account account2 = new Account(111222, "9999", 1000.0, 0);
            
            DatabaseProxy.addAccount(account1);
            DatabaseProxy.addAccount(account2);
	        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
	        atm = new ATM(1,  Constants.branch ,bankAddress);

	        atm.getDisplay().setTestMode(true);
	        display = atm.getDisplay();
	    }

	    @When("the operator turns on the ATM3")
	    public void the_operator_turns_on_the_ATM() {
	        atmThread = new Thread(atm);
	        atmThread.start();
	        display.setFakeInput("","1","6");
	    }

	    @When("the ATM is running out of money")
	    public void the_ATM_is_running_out_of_money() {
	        atm.getCashDispenser().setInitialCash(new Money(5)); 
	    }

	    @When("a card is inserted")
	    public void a_card_is_inserted() {
	        display.setFakeInput("1234123456 12-28");

	    }

	    @Then("the ATM should display an error message about insufficient funds")
	    public void the_ATM_should_display_an_error_message_about_insufficient_funds() throws InterruptedException {
	    	Thread.sleep(500);
	        int state = atm.getState();
	        assertEquals(3,state);
	    }

	    @Then("the ATM should eject the card")
	    public void the_ATM_should_eject_the_card() {
	       String lastMessage = atm.getDisplay().getLastDisplayedMessage();
	       assertEquals("[CARD READER] Card ejected.",lastMessage);
	    }
}




