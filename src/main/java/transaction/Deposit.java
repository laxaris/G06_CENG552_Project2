package transaction;

import atm.ATM;
import atm.Session;
import banking.Card;
import banking.Message;
import banking.Money;
import banking.Receipt;

public class Deposit extends Transaction {

    private Money amount;

    public Deposit(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    @Override
    protected Message getSpecificsFromCustomer() throws Exception {
        amount = atm.getDisplay().readAmount("Enter the amount to deposit:");
        return new Message(Message.COMPLETE_DEPOSIT, card, pin, getSerialNumber(), -1, -1, amount);
    }

    @Override
    protected Receipt completeTransaction() throws Exception {

        return new Receipt(atm, card, this, balances) {
            {
                detailsPortion = new String[]{"DEPOSIT", "Amount: " + amount.toString()};
            }
        };
    }
}
