package steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

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

public class FR17_TransferTransactionSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;
    private NetworkToBank networkToBank;

    @Given("the ATM is turned off \\(FR17)")
    public void fr17_the_ATM_is_turned_off() throws UnknownHostException {
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        atm = new ATM(1, Constants.branch, bankAddress);
        Account account1 = new Account(123456, "1234", 500.0, 0);
        Account account2 = new Account(654321, "5678", 1000.0, 0);
        DatabaseProxy.addAccount(account1);
        DatabaseProxy.addAccount(account2);
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
        networkToBank = atm.getNetworkToBank();
    }

    @When("the operator turns on the ATM \\(FR17)")
    public void fr17_the_operator_turns_on_the_ATM() {
        atmThread = new Thread(atm);
        atmThread.start();
        display.setFakeInput("", "1", "6");
    }

    @When("the ATM is running with sufficient cash \\(FR17)")
    public void fr17_the_ATM_is_running_with_sufficient_cash() {
        atm.getCashDispenser().setInitialCash(new Money(100000));
    }

    @When("a valid cash card is entered \\(FR17)")
    public void fr17_a_valid_cash_card_is_entered() {
        display.setFakeInput("1234123456 12-25");
    }

    @When("the user enters the correct password and authorization succeeds \\(FR17)")
    public void fr17_the_user_enters_the_correct_password() throws InterruptedException {
        Thread.sleep(100);
        display.setFakeInput("1234");
    }

    @When("the user selects the transfer option \\(FR17)")
    public void fr17_the_user_selects_transfer_option() {
        display.setFakeInput("3");
    }

    @When("the user enters a valid recipient account number \\(FR17)")
    public void fr17_the_user_enters_valid_recipient() {
        display.setFakeInput("654321");
    }

    @When("the user enters a transfer amount within the transaction limit \\(FR17)")
    public void fr17_the_user_enters_amount_within_limit() {
        display.setFakeInput("200");
    }

    @When("the user enters a transfer amount exceeding the transaction limit \\(FR17)")
    public void fr17_the_user_enters_amount_exceeding_limit() {
        display.setFakeInput("20000");
    }

    @When("the user enters a transfer amount exceeding their account balance \\(FR17)")
    public void fr17_the_user_enters_amount_exceeding_balance() {
        display.setFakeInput("600");
    }

    @Then("the transfer sequence should begin \\(FR17)")
    public void fr17_transfer_sequence_should_begin() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(display.getAllTestMessages());
        String result = display.getDisplayedMessageAt(2);
        assertTrue(result.contains("Sending: TRANSFER"));
    }

    @Then("the log should record the successful transfer \\(FR17)")
    public void fr17_log_should_record_transfer() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(display.getAllTestMessages());
        String result = display.getDisplayedMessageAt(1);
        assertTrue(result.contains("LOG] Response: SUCCESS:"));
    }

    @Then("the transfer should not proceed \\(FR17)")
    public void fr17_transfer_should_not_proceed() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(display.getAllTestMessages());
        String result = display.getLastDisplayedMessage();
        assertTrue(result.contains("[ERROR] Transfer exceeds maximum transaction"));
    }
    @When("the user attempts to transfer money to an undefined account \\(FR17)")
    public void fr17_the_user_attempts_to_transfer_money_to_an_undefined_account() {
        display.setFakeInput("3", "100","99");
    }

    @Then("the ATM should display a message stating \"Invalid destination account\" \\(FR17)")
    public void fr17_the_ATM_should_display_invalid_destination_account() throws InterruptedException {
        Thread.sleep(100);
        String result = display.getLastDisplayedMessage();
        assertTrue(result.contains("invalid account"));
    }

    @Then("the ATM should display a message stating \"Insufficient account balance for transfer\" \\(FR17)")
    public void fr17_transfer_insufficient_balance_message() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(display.getAllTestMessages());
        String result = display.getLastDisplayedMessage();
        assertTrue(result.contains("[ERROR] Transfer failed. Insufficient balance or"));
    }

    @Then("the ATM should prompt for another transaction \\(FR17)")
    public void fr17_the_ATM_should_prompt_for_another_transaction() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(display.getAllTestMessages());
        String result = display.getLastDisplayedMessage();
        assertTrue(result.contains("Would you like to do another transaction?"));
    }
    
    

    

}
