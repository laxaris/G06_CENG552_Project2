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

public class FR11_WithdrawalTransactionSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;
    private NetworkToBank networkToBank;

    @Given("the ATM is turned off \\(FR11)")
    public void fr11_the_ATM_is_turned_off() throws UnknownHostException {
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        atm = new ATM(1, Constants.branch, bankAddress);
        Account account1 = new Account(123456, "1234", 50000.0, 0);
        DatabaseProxy.addAccount(account1);
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
        networkToBank = atm.getNetworkToBank();
    }

    @When("the operator turns on the ATM \\(FR11)")
    public void fr11_the_operator_turns_on_the_ATM() {
        atmThread = new Thread(atm);
        atmThread.start();
        display.setFakeInput("", "1", "6");
    }

    @When("the ATM is running with sufficient cash \\(FR11)")
    public void fr11_the_ATM_is_running_with_sufficient_cash() {
        atm.getCashDispenser().setInitialCash(new Money(100000));
    }

    @When("a valid cash card is entered \\(FR11)")
    public void fr11_a_valid_cash_card_is_entered() {
        display.setFakeInput("1234123456 12-25");
    }

    @When("the user enters the correct password and authorization succeeds \\(FR11)")
    public void fr11_the_user_enters_the_correct_password() throws InterruptedException {
        Thread.sleep(100);  
        display.setFakeInput("1234");
    }

    @When("the user enters an amount within the transaction limit \\(FR11)")
    public void fr11_the_user_enters_an_amount_within_the_transaction_limit() {
        display.setFakeInput("1", "50");
    }

    @When("the user enters an amount exceeding the transaction limit \\(FR11)")
    public void fr11_the_user_enters_an_amount_exceeding_the_transaction_limit() {
        display.setFakeInput("1", "15000");
    }

    @Then("the withdrawal sequence should begin \\(FR11)")
    public void fr11_the_withdrawal_sequence_should_begin() throws InterruptedException {
    	Thread.sleep(100);
    	String result = display.getDisplayedMessageAt(2);
    	String expected = "[ERROR] Insufficient cash in the ATM.";
        assertEquals(expected,result);
    }

    @Then("the withdrawal should not proceed \\(FR11)")
    public void fr11_the_withdrawal_should_not_proceed() throws InterruptedException {
    	Thread.sleep(100);
    	System.out.println(display.getAllTestMessages());
    	boolean messageDisplayed = display.getDisplayedMessageAt(0).contains("Exceeds maximum withdrawal limit");
        assertTrue("The withdrawal should not proceed but was allowed.", messageDisplayed);
    }
}
