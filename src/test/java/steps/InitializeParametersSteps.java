package steps;

import static org.junit.Assert.assertEquals;

import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import atm.ATM;
import atm.Display;
import banking.Money;
import io.cucumber.java.en.*;

public class InitializeParametersSteps {

    private ATM atm;
    private Display display;

    @Given("the ATM is turned off")
    public void the_ATM_is_turned_off() {

        atm = new ATM(1, "Mybank", null, null);
        display = atm.getDisplay();
    }

    @When("the operator turns on the ATM")
    public void the_operator_turns_on_the_atm() {

    	Thread atmThread = new Thread(atm);
        atmThread.start();
    }

    @When("the operator sets the initial total cash to {int}")
    public void the_operator_sets_the_initial_total_cash_to(Integer t) {
        atm.getCashDispenser().setInitialCash(new Money(t));
    }

    @When("the operator sets the max withdraw per day to {int}")
    public void the_operator_sets_the_max_withdraw_per_day_to(Integer k) {
    	
    	String yalanGiris = "\n1\n2\n"+Integer.toString(k)+"\n";
    	display.setFakeInput(yalanGiris);
    	
    }



    @Then("the ATM should have initial total cash of {int}")
    public void the_atm_should_have_initial_total_cash_of(Integer expected) {
        Money actual = atm.getCashDispenser().getCashOnHand();
        assertEquals(expected.intValue(), (int) actual.toDouble());
    }

    @Then("the ATM should have max withdraw per day of {int}")
    public void the_atm_should_have_max_withdraw_per_day_of(Integer expected) {
        Money actual = atm.maxWithdrawPerDay;
        assertEquals(expected.intValue(), (int)actual.toDouble());
    }

}
