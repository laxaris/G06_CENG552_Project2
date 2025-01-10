package steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

public class FR10_CardRetentionAfterMultipleFailedAttemptsSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;
    private NetworkToBank networkToBank;
    private int failedAttempts;

    @Given("the ATM is turned off \\(FR10)")
    public void fr10_the_ATM_is_turned_off() throws UnknownHostException {
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        Account account1 = new Account(123456, "1234", 50000.0, 0);
        DatabaseProxy.addAccount(account1);
        atm = new ATM(1, Constants.branch, bankAddress);
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
        networkToBank = atm.getNetworkToBank();
    }

    @When("the operator turns on the ATM \\(FR10)")
    public void fr10_the_operator_turns_on_the_ATM() {
        atmThread = new Thread(atm);
        atmThread.start();
        display.setFakeInput("", "1", "6");
    }

    @When("the ATM is running with sufficient cash \\(FR10)")
    public void fr10_the_ATM_is_running_with_sufficient_cash() {
        atm.getCashDispenser().setInitialCash(new Money(10000));
    }

    @When("a valid cash card is entered \\(FR10)")
    public void fr10_a_valid_cash_card_is_entered() {

        display.setFakeInput("1234123456 12-25");
    }

    @When("the user enters the incorrect password three times consecutively \\(FR10)")
    public void fr10_the_user_enters_the_incorrect_password_three_times_consecutively() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            display.setFakeInput("1233");
           
        }
    }

    @Then("ATM should retain the card and should display a error message instructing the user to call the bank \\(FR10)")
    public void fr10_the_ATM_should_retain_the_card() throws InterruptedException {
        Thread.sleep(100);
        assertTrue("The card was not retained after three incorrect attempts.", atm.getCardReader().isCardRetained());
    }

    
}
