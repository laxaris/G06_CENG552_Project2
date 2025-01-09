package atm.transaction;

import atm.physical.ATM;
import atm.physical.Session;
import banking.Card;
import banking.Message;
import banking.Money;
import banking.Status;
import banking.Receipt;
import banking.DatabaseProxy;

public class Withdrawal extends Transaction {

    private Money amount;

    public Withdrawal(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    @Override
    protected Message getSpecificsFromCustomer() throws Exception {
        amount = atm.getDisplay().readAmount("Enter the amount to withdraw:");
        return new Message(Message.WITHDRAWAL, card, pin, getSerialNumber(), -1, -1, amount);
    }

    @Override
    protected Receipt completeTransaction() throws Exception {
        
        return new Receipt(atm, card, this, balances) {
            {
                detailsPortion = new String[]{"WITHDRAWAL", "Amount: " + amount.toString()};
            }
        };
    }
}
