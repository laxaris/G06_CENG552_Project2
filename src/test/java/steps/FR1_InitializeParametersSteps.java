package steps;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import atm.ATM;
import atm.Display;
import banking.Money;
import io.cucumber.java.en.*;

public class FR1_InitializeParametersSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;

    @Given("the ATM is turned offf")
    public void the_ATM_is_turned_off() throws UnknownHostException {
    	  InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        atm = new ATM(1, "Mybank", null, bankAddress);
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
    }

    @When("the operator turns on the  ATM")
    public void the_operator_turns_on_the_atm() {
        atmThread = new Thread(atm);
        atmThread.start();
    }

    @When("the operator sets the initial total cash to {int}")
    public void the_operator_sets_the_initial_total_cash_to(Integer t) {
        atm.getCashDispenser().setInitialCash(new Money(t));
    }

    @When("the operator sets the max withdraw per day to {int}")
    public void the_operator_sets_the_max_withdraw_per_day_to(Integer k) {
        display.setFakeInput("", "1",  "2", k.toString() );
    }
    @When("the operator sets the max withdraw per transaction to {int}")
    public void the_operator_sets_the_max_withdraw_per_transaction_to(Integer m) {
     display.setFakeInput("3",m.toString());
    }

    @When("the operator sets the min cash to allow transaction to {int}")
    public void the_operator_sets_the_min_cash_to_allow_transaction_to(Integer n) {
    	display.setFakeInput("4",n.toString(),"5");
    }

    @Then("the ATM should have initial total cash of {int}")
    public void the_atm_should_have_initial_total_cash_of(Integer expected) {
        Money actual = atm.getCashDispenser().getCashOnHand();
        assertEquals(expected.intValue(), (int) actual.toDouble());
    }

    @Then("the ATM should have max withdraw per day of {int}")
    public void the_atm_should_have_max_withdraw_per_day_of(Integer expected) {
        Money actual = atm.getMaxWithdrawPerDay();
        assertEquals(expected.intValue(), (int) actual.toDouble());
    }
    
    @Then("the ATM should have max withdraw per transaction of {int}")
    public void the_atm_should_have_max_withdraw_per_transaction_of(Integer expected) {
        Money actual = atm.getMaxWithdrawPerTransaction();
        assertEquals(expected.intValue(), (int) actual.toDouble());
    }

    @Then("the ATM should have min cash to allow transaction of {int}")
    public void the_atm_should_have_min_cash_to_allow_transaction_of(Integer expected) {
        Money actual = atm.getMinCashToAllowTransaction();
        assertEquals(expected.intValue(), (int) actual.toDouble());
    }
}
