package transaction;


import atm.ATM;
import atm.Session;
import banking.Card;
import banking.Message;
import banking.Money;
import banking.Receipt;
import banking.DatabaseProxy;

public class Inquiry extends Transaction {

    public Inquiry(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    @Override
    protected Message getSpecificsFromCustomer() {
        return new Message(Message.INQUIRY, card, pin, getSerialNumber(), card.getAccountNumber(), -1, new Money(0));
    }

    @Override
    protected Receipt completeTransaction() throws Exception {
        double balance = DatabaseProxy.getBalance(card.getAccountNumber());
        Money balanceAmount = new Money((int) balance, (int) ((balance - (int) balance) * 100));

        return new Receipt(atm, card, this, balances) {
            {
                detailsPortion = new String[]{"BALANCE INQUIRY", "Current Balance: " + balanceAmount.toString()};
            }
        };
    }
}

