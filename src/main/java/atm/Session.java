package atm;

import banking.Card;

import banking.Money;
import transaction.Transaction;
import banking.DatabaseProxy;

public class Session {
    private ATM atm;
    private int pin;
    private int state;
    private int invalidPinAttempts;
    private Money dailyWithdrawalLimit;
    private static final int MAX_PIN_ATTEMPTS = 3;

    private static final int READING_CARD_STATE = 1;
    private static final int READING_PIN_STATE = 2;
    private static final int CHOOSING_TRANSACTION_STATE = 3;
    private static final int PERFORMING_TRANSACTION_STATE = 4;
    private static final int EJECTING_CARD_STATE = 5;
    private static final int FINAL_STATE = 6;

    public Session(ATM atm) {
        this.atm = atm;
        this.state = READING_CARD_STATE;
        this.invalidPinAttempts = 0;
    }

    

    public void performSession() throws Display.Cancelled{
        Card card = null;
        Transaction currentTransaction = null;

        while (state != FINAL_STATE) {
            switch (state) {
                case READING_CARD_STATE:
                    card = atm.getCardReader().readCard();
                    if (card != null && DatabaseProxy.getAccount(card.getAccountNumber()) != null) {
                        state = READING_PIN_STATE;
                        dailyWithdrawalLimit = DatabaseProxy.getDailyLimit(card.getAccountNumber());
                    } else {
                        atm.getDisplay().showMessage("Unable to read card or invalid card.");
                        state = EJECTING_CARD_STATE;
                    }
                    break;

                case READING_PIN_STATE:
                    try {
                        pin = atm.getDisplay().readPIN("Please enter your PIN\nThen press ENTER");
                        if (DatabaseProxy.verifyPassword(card.getAccountNumber(), String.valueOf(pin))) {
                            state = CHOOSING_TRANSACTION_STATE;
                            invalidPinAttempts = 0;
                        } else {
                            invalidPinAttempts++;
                            atm.getDisplay().showMessage("Incorrect PIN. Attempt: " + invalidPinAttempts);
                            if (invalidPinAttempts >= MAX_PIN_ATTEMPTS) {
                                atm.getDisplay().showMessage("Card retained due to multiple incorrect attempts.");
                                atm.getCardReader().retainCard();
                                state = FINAL_STATE;
                            }
                        }
                    } catch (Exception e) {
                        state = EJECTING_CARD_STATE;
                    }
                    break;

                case CHOOSING_TRANSACTION_STATE:
                    try {
                        currentTransaction = Transaction.makeTransaction(atm, this, card, pin);
                        state = PERFORMING_TRANSACTION_STATE;
                    } catch (Exception e) {
                        state = EJECTING_CARD_STATE;
                    }
                    break;

                case PERFORMING_TRANSACTION_STATE:
                    try {
                        boolean doAgain = currentTransaction.performTransaction();
                        if (doAgain) {
                            state = CHOOSING_TRANSACTION_STATE;
                        } else {
                            state = EJECTING_CARD_STATE;
                        }
                    } catch (Exception e) {
                        state = EJECTING_CARD_STATE;
                    }
                    break;

                case EJECTING_CARD_STATE:
                    atm.getCardReader().ejectCard();
                    state = FINAL_STATE;
                    break;
            }
        }
    }


}
