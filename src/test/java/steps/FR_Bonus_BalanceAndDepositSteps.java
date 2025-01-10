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

public class FR_Bonus_BalanceAndDepositSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;
    private NetworkToBank networkToBank;
    private double balanceOnTheDispenser;

    @Given("the ATM is turned off \\(FR-Bonus)")
    public void fr_bonus_the_ATM_is_turned_off() throws UnknownHostException {
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        atm = new ATM(1, Constants.branch, bankAddress);
        Account account1 = new Account(123456, "1234", 50000.0, 0);
        DatabaseProxy.addAccount(account1);
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
        networkToBank = atm.getNetworkToBank();
    }

    @When("the operator turns on the ATM \\(FR-Bonus)")
    public void fr_bonus_the_operator_turns_on_the_ATM() {
        atmThread = new Thread(atm);
        atmThread.start();
        display.setFakeInput("", "1", "6");
    }

    @When("the ATM is running with sufficient cash \\(FR-Bonus)")
    public void fr_bonus_the_ATM_is_running_with_sufficient_cash() {
        atm.getCashDispenser().setInitialCash(new Money(100000));
        balanceOnTheDispenser = atm.getCashDispenser().getCashOnHand().toDouble();
    }

    @When("a valid cash card is entered \\(FR-Bonus)")
    public void fr_bonus_a_valid_cash_card_is_entered() {
        display.setFakeInput("1234123456 12-25");
    }

    @When("the user enters the correct password and authorization succeeds \\(FR-Bonus)")
    public void fr_bonus_the_user_enters_the_correct_password() {
        display.setFakeInput("1234");
    }

    @When("the user performs a balance inquiry \\(FR-Bonus)")
    public void fr_bonus_user_performs_balance_inquiry() {
        display.setFakeInput("4");
    }

    @Then("the ATM should display the current balance \\(FR-Bonus)")
    public void fr_bonus_ATM_displays_current_balance() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(display.getAllTestMessages());
        
        String result = display.getDisplayedMessageAt(2);
        assertTrue(result.contains("Balance inquiry successful."));
    }

    @When("the user initiates a deposit transaction \\(FR-Bonus)")
    public void fr_bonus_user_initiates_deposit_transaction() {
        display.setFakeInput("1","2");
    }

    @When("the user deposits a valid amount \\(FR-Bonus)")
    public void fr_bonus_user_deposits_valid_amount() {
        display.setFakeInput("100");
    }

    @Then("the ATM should confirm the deposit and update the balance also cash on the dispenser \\(FR-Bonus)")
    public void fr_bonus_ATM_confirms_deposit_and_updates_balance() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(display.getAllTestMessages());
        String result = display.getDisplayedMessageAt(2);
        assertTrue(result.contains("Cash added"));
        boolean bool = (balanceOnTheDispenser+100)==atm.getCashDispenser().getCashOnHand().toDouble();
        assertTrue(bool);
        assertEquals(DatabaseProxy.getAccount(123456).getBalance(),50000+100,0.01);
    }

    @When("the user performs another balance inquiry \\(FR-Bonus)")
    public void fr_bonus_user_performs_another_balance_inquiry() {
        display.setFakeInput("1","4");
    }

    @Then("the ATM should display the updated balance including the deposit \\(FR-Bonus)")
    public void fr_bonus_ATM_displays_updated_balance() throws InterruptedException {
        Thread.sleep(100);
        String result = display.getDisplayedMessageAt(2);
        assertTrue(result.contains("Balance inquiry successful."));
        
    }
}
