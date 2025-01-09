package atm.transaction;

import atm.physical.*;
import banking.Balances;
import banking.Card;
import banking.Message;
import banking.Status;
import banking.Receipt;

public abstract class Transaction {
    protected ATM atm;
    protected Session session;
    protected Card card;
    protected int pin;
    protected int serialNumber;
    protected Message message;
    protected Balances balances;
    private static int nextSerialNumber = 1;

    private static final String[] TRANSACTION_TYPES_MENU = 
        { "Withdrawal", "Deposit", "Transfer", "Balance Inquiry" };

    private int state;
    private static final int GETTING_SPECIFICS_STATE = 1;
    private static final int SENDING_TO_BANK_STATE = 2;
    private static final int INVALID_PIN_STATE = 3;
    private static final int COMPLETING_TRANSACTION_STATE = 4;
    private static final int PRINTING_RECEIPT_STATE = 5;
    private static final int ASKING_DO_ANOTHER_STATE = 6;

    protected Transaction(ATM atm, Session session, Card card, int pin) {
        this.atm = atm;
        this.session = session;
        this.card = card;
        this.pin = pin;
        this.serialNumber = nextSerialNumber++;
        this.balances = new Balances();
        this.state = GETTING_SPECIFICS_STATE;
    }

    public static Transaction makeTransaction(ATM atm, Session session, Card card, int pin) throws Exception {
        int choice = atm.getDisplay().readMenuChoice("Please choose transaction type", TRANSACTION_TYPES_MENU);
        
        switch (choice) {
            case 0: return new Withdrawal(atm, session, card, pin);
            case 1: return new Deposit(atm, session, card, pin);
            case 2: return new Transfer(atm, session, card, pin);
            case 3: return new Inquiry(atm, session, card, pin);
            default: throw new Exception("Invalid transaction type selected.");
        }
    }

    public boolean performTransaction() throws CardRetained {
        String doAnotherMessage = "";
        Status status = null;
        Receipt receipt = null;

        while (true) {
            switch (state) {
                case GETTING_SPECIFICS_STATE:
                    try {
                        message = getSpecificsFromCustomer();
                        state = SENDING_TO_BANK_STATE;
                    } catch (Exception e) {
                        doAnotherMessage = "Transaction cancelled.";
                        state = ASKING_DO_ANOTHER_STATE;
                    }
                    break;

                case SENDING_TO_BANK_STATE:
                    status = atm.getNetworkToBank().sendMessage(message, balances);
                    if (status.isInvalidPIN()) {
                        state = INVALID_PIN_STATE;
                    } else if (status.isSuccess()) {
                        state = COMPLETING_TRANSACTION_STATE;
                    } else {
                        doAnotherMessage = status.getMessage();
                        state = ASKING_DO_ANOTHER_STATE;
                    }
                    break;

                case INVALID_PIN_STATE:
                    try {
                        pin = atm.getDisplay().readPIN("PIN was incorrect. Please re-enter your PIN:");
                        message.setPIN(pin);
                        state = SENDING_TO_BANK_STATE;
                    } catch (Exception e) {
                        doAnotherMessage = "Transaction cancelled.";
                        state = ASKING_DO_ANOTHER_STATE;
                    }
                    break;

                case COMPLETING_TRANSACTION_STATE:
                    try {
                        receipt = completeTransaction();
                        state = PRINTING_RECEIPT_STATE;
                    } catch (Exception e) {
                        doAnotherMessage = "Transaction cancelled.";
                        state = ASKING_DO_ANOTHER_STATE;
                    }
                    break;

                case PRINTING_RECEIPT_STATE:
                    atm.getReceiptPrinter().printReceipt(receipt);
                    state = ASKING_DO_ANOTHER_STATE;
                    break;

                case ASKING_DO_ANOTHER_STATE:
                    try {
                        boolean doAgain = atm.getDisplay().readMenuChoice(
                                doAnotherMessage + "Would you like to do another transaction?",
                                new String[]{"Yes", "No"}) == 0;
                        return doAgain;
                    } catch (Exception e) {
                        return false;
                    }
            }
        }
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    protected abstract Message getSpecificsFromCustomer() throws Exception;
    protected abstract Receipt completeTransaction() throws Exception;

    public static class CardRetained extends Exception {
        public CardRetained() {
            super("Card retained due to too many invalid PIN attempts.");
        }
    }
}
