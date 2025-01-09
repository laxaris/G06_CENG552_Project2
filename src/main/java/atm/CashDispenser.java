package atm;

import banking.Money;

public class CashDispenser {
    private Log log;
    private Money cashOnHand;

    public CashDispenser(Log log) {
        this.log = log;
        this.cashOnHand = new Money(0);
    }

    public void setInitialCash(Money initialCash) {
        cashOnHand = initialCash;
    }

    public boolean checkCashOnHand(Money amount) {
        return cashOnHand.lessFrom(amount);
    }

    public void dispenseCash(Money amount) {
        cashOnHand.substract(amount);
        System.out.println("[CASH DISPENSER] Dispensing " + amount);
        log.logCashDispensed(amount);
    }

    public Money getCashOnHand() {
        return new Money(cashOnHand);
    }
}
