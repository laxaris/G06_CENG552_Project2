package atm.transaction;

import atm.physical.ATM;
import atm.physical.Session;
import banking.Card;
import banking.Message;
import banking.Money;
import banking.Status;
import banking.Receipt;
import banking.DatabaseProxy;

public class Transfer extends Transaction {

    private Money amount;
    private int toAccount;

    public Transfer(ATM atm, Session session, Card card, int pin) {
        super(atm, session, card, pin);
    }

    @Override
    protected Message getSpecificsFromCustomer() throws Exception {
        toAccount = atm.getDisplay().readPIN("Enter the account number to transfer to:");
        amount = atm.getDisplay().readAmount("Enter the transfer amount:");
        return new Message(Message.TRANSFER, card, pin, getSerialNumber(), card.getAccountNumber(), toAccount, amount);
    }

    @Override
    protected Receipt completeTransaction() throws Exception {

        return new Receipt(atm, card, this, balances) {
            {
                detailsPortion = new String[]{"TRANSFER", "To Account: " + toAccount, "Amount: " + amount.toString()};
            }
        };
    }
}
