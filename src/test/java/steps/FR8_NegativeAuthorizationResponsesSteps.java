package steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

public class FR8_NegativeAuthorizationResponsesSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;
    private NetworkToBank networkToBank;

    @Given("the ATM is turned off \\(FR8)")
    public void fr8_the_ATM_is_turned_off() throws UnknownHostException {
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        Account account1 = new Account(123456, "1234", 50000.0, 0);
        DatabaseProxy.addAccount(account1);
        atm = new ATM(1, Constants.branch, bankAddress);
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
        networkToBank = atm.getNetworkToBank();
    }

    @When("the operator turns on the ATM \\(FR8)")
    public void fr8_the_operator_turns_on_the_ATM() {
        atmThread = new Thread(atm);
        atmThread.start();
        display.setFakeInput("", "1", "6");
    }

    @When("the ATM is running with sufficient cash \\(FR8)")
    public void fr8_the_ATM_is_running_with_sufficient_cash() {
        atm.getCashDispenser().setInitialCash(new Money(10000));
    }

    @When("a cash card from an unsupported bank is entered \\(FR8)")
    public void fr8_a_cash_card_from_an_unsupported_bank_is_entered() {
       display.setFakeInput("1233123456 12-27","1234");
    }

    @When("a cash card with account issues is entered \\(FR8)")
    public void fr8_a_cash_card_with_account_issues_is_entered() {
    	display.setFakeInput("1234123455 12-27","1234");
    }

    @Then("the ATM should display an error message about bad bank code \\(FR8)")
    public void fr8_the_ATM_should_display_an_error_message_about_bad_bank_code() throws InterruptedException {
    	Thread.sleep(100);
      assertEquals(1,atm.getNetworkToBank().getCardAuthorizationCode());
    }

    @Then("the ATM should display an error message about bad account \\(FR8)")
    public void fr8_the_ATM_should_display_an_error_message_about_bad_account() throws InterruptedException {
    	Thread.sleep(100);
    	assertEquals(2,atm.getNetworkToBank().getCardAuthorizationCode());
    }

    @Then("the ATM should eject the card \\(FR8)")
    public void fr8_the_ATM_should_eject_the_card() {
        assertTrue("Card was not ejected!", atm.getCardReader().isCardEjected());
    }
}
