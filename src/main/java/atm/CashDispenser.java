package atm;

import banking.Money;

public class CashDispenser {
    private Log log;
    private Money cashOnHand;
    private ATM atm;

    public CashDispenser(Log log, ATM atm) {
        this.log = log;
        this.cashOnHand = new Money(0);
        this.atm = atm;
    }

    public void setInitialCash(Money initialCash) {
        cashOnHand = initialCash;
    }

    public boolean checkCashOnHand(Money amount) {
        return cashOnHand.lessFrom(amount);
    }
    
    public void addCash(Money amount) {
    	cashOnHand.add(amount);
        atm.getDisplay().showMessage("[CASH DISPENSER] Adding " + amount);
        log.logCashAdded(amount);
    }

    public void dispenseCash(Money amount) {
        cashOnHand.substract(amount);
        atm.getDisplay().showMessage("[CASH DISPENSER] Dispensing " + amount);
        log.logCashDispensed(amount);
    }

    public Money getCashOnHand() {
        return new Money(cashOnHand);
    }
}
