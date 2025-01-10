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

public class FR11_FR12_FR13_FR14_FR15_FR16_WithdrawalTransactionSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;
    private NetworkToBank networkToBank;

    @Given("the ATM is turned off \\(FR11)")
    public void fr11_the_ATM_is_turned_off() throws UnknownHostException {
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        atm = new ATM(1, Constants.branch, bankAddress);
        Account account1 = new Account(123456, "1234", 500.0, 0);
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
    @When("the user enters an amount below the minimum allowed amount \\(FR11)")
    public void fr11_the_user_enters_an_amount_below_minimum_allowed() {
        display.setFakeInput("1","9");  
    }

    
    @When("the user enters an amount exceeding their account balance \\(FR11)")
    public void fr11_the_user_enters_an_amount_exceeding_account_balance() {
        display.setFakeInput( "1","600");  
    }
    @When("the user enters an amount exceeding the ATM cash availability \\(FR11)")
    public void fr11_the_user_enters_an_amount_exceeding_ATM_cash_availability() {
        atm.getCashDispenser().setInitialCash(new Money(100));  
        display.setFakeInput( "1","200"); 
    }
    @Then("the ATM should display a message stating \"Minimum withdrawal amount not met\" \\(FR11)")
    public void fr11_the_ATM_should_display_minimum_amount_error() throws InterruptedException {
        Thread.sleep(100);
        String result = display.getLastDisplayedMessage();
        assertTrue(result.contains("[ERROR] Amount is less than the minimum cash allowed"));
    }
    @Then("the ATM should display a message stating \"Insufficient cash in ATM\" \\(FR11)")
    public void fr11_the_ATM_should_display_insufficient_cash_error() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(display.getAllTestMessages());
        String result = display.getLastDisplayedMessage();
        assertTrue(result.contains("Insufficient cash in the ATM"));
    }

    @Then("the ATM should display a message stating \"Insufficient account balance\" \\(FR11)")
    public void fr11_the_ATM_should_display_insufficient_balance_error() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(display.getAllTestMessages());
        String result = display.getLastDisplayedMessage();
        assertTrue(result.contains("Insufficient account balance"));
    }

    @Then("the withdrawal sequence should begin \\(FR11)")
    public void fr11_the_withdrawal_sequence_should_begin() throws InterruptedException {
    	Thread.sleep(100);
    	System.out.println(display.getAllTestMessages());
    	String result = display.getDisplayedMessageAt(4);
    	
        assertTrue(result.contains("[LOG] Sending: WITHDRAW CARD"));
    }
    
    @Then("the cash dispenser should dispense the correct amount \\(FR11)")
    public void fr11_the_cash_dispenser_should_dispense_the_correct_amount() throws InterruptedException {
        Thread.sleep(100);
        String result = display.getDisplayedMessageAt(3);  
        assertTrue(result.contains("[CASH DISPENSER] Dispensing"));
    }

    @Then("the log should record the cash dispensing \\(FR11)")
    public void fr11_the_log_should_record_the_cash_dispensing() throws InterruptedException {
        Thread.sleep(100);
        String result = display.getDisplayedMessageAt( 2);  
        assertTrue(result.contains("[LOG] Cash dispensed:"));
    }


	@Then("the log should record the successful transaction also balance should be updated \\(FR11)")
    public void fr11_the_log_should_record_the_successful_transaction() throws InterruptedException {
        Thread.sleep(100);
        String result = display.getDisplayedMessageAt( 1);  
        assertTrue(result.contains("[LOG] Response: SUCCESS: Operation successful."));
        assertEquals(500-50,DatabaseProxy.getAccount(123456).getBalance(),0.1);
    }

    @Then("the ATM should prompt for another transaction \\(FR11)")
    public void fr11_the_ATM_should_prompt_for_another_transaction() throws InterruptedException {
        Thread.sleep(100);
        String result = display.getLastDisplayedMessage();  
        assertTrue(result.contains("Would you like to do another transaction?"));
    }

    @Then("the withdrawal should not proceed \\(FR11)")
    public void fr11_the_withdrawal_should_not_proceed() throws InterruptedException {
    	Thread.sleep(100);
    	System.out.println(display.getAllTestMessages());
    	boolean messageDisplayed = display.getLastDisplayedMessage().contains("Exceeds maximum withdrawal per transaction");
        assertTrue("The withdrawal should not proceed but was allowed.", messageDisplayed);
    }
}
