package steps;

import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;

import atm.ATM;
import atm.Display;
import atm.Log;
import banking.Account;
import banking.Constants;
import banking.DatabaseProxy;
import banking.Money;
import io.cucumber.java.en.*;

public class FR6_LogSerialNumberSteps {

    private ATM atm;
    private Display display;
    private Thread atmThread;

    private Log log;

    @Given("the ATM is turned off \\(FR6)")
    public void fr6_the_ATM_is_turned_off() throws UnknownHostException {
        InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
        Account account1 = new Account(123456, "1234", 50000.0, 0);
        DatabaseProxy.addAccount(account1);
        atm = new ATM(1,  Constants.branch, bankAddress);
        
        atm.getDisplay().setTestMode(true);
        display = atm.getDisplay();
        log = new Log();
    }

    @When("the operator turns on the ATM \\(FR6)")
    public void fr6_the_operator_turns_on_the_ATM() {
        atmThread = new Thread(atm);
        atmThread.start();
        display.setFakeInput("", "1", "6");
    }

    @When("the ATM is running with sufficient cash \\(FR6)")
    public void fr6_the_ATM_is_running_with_sufficient_cash() {
        atm.getCashDispenser().setInitialCash(new Money(10000));
    }

    @When("a valid cash card is entered \\(FR6)")
    public void fr6_a_valid_cash_card_is_entered() {
        display.setFakeInput("1234123456 12-25"); 
       
    }

    @Then("the ATM should log the serial number from the cash card \\(FR6)")
    public void fr6_the_ATM_should_log_the_serial_number_from_the_cash_card() {
        String lastLog = log.getLastLog(1); 
        boolean containsSerialNumber = lastLog.contains("Serial Number: 1234123456");
        assertTrue("Serial number was not logged correctly!", containsSerialNumber);
    }
}
