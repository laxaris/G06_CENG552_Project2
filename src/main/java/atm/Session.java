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
    private boolean isAuthorizationSuccesful;
    private static final int MAX_PIN_ATTEMPTS = 3;
    public boolean cardRetainedDueToMultiplePinAttempts;

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
        this.isAuthorizationSuccesful=false;
        this.cardRetainedDueToMultiplePinAttempts = false;
    }
    
    public boolean getIsAuthorizatinsuccesful() {
    	return isAuthorizationSuccesful;
    }
    
    public int getInvalidPinAttempts() {
    	return invalidPinAttempts;
    }
    
    public boolean getIsCardRetainedDueToMultiplePinAttempts() {
    	return cardRetainedDueToMultiplePinAttempts;
    }

    public void performSession() throws Display.Cancelled{
        Card card = null;
        Transaction currentTransaction = null;

        while (state != FINAL_STATE) {
            switch (state) {
                case READING_CARD_STATE:
                	
                    card = atm.getCardReader().readCard();
                    if(!atm.checkCashAvailability()) {
                    	atm.getDisplay().showMessage("[DISPLAY]: Insufficient Cash, Please contact this number 555-555-5555");
                        state = EJECTING_CARD_STATE;
                        break;
                    }
                    cardRetainedDueToMultiplePinAttempts = false;
                    if (card != null ) {
                    	
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
                        if (atm.getNetworkToBank().sendAuthorizationRequest(card, pin)) {
                        	isAuthorizationSuccesful = true;
                            state = CHOOSING_TRANSACTION_STATE;
                            invalidPinAttempts = 0;
                        } else {
                        	if(atm.getNetworkToBank().getCardAuthorizationCode()!=0) {
                        		atm.getCardReader().ejectCard();
                                state = FINAL_STATE;
                                break;
                        	}
                            invalidPinAttempts++;
                            atm.getDisplay().showMessage("Incorrect PIN. Attempt: " + invalidPinAttempts);
                            if (invalidPinAttempts >= MAX_PIN_ATTEMPTS) {
                                atm.getDisplay().showMessage("Card retained due to multiple incorrect attempts. Please call the bank");
                                cardRetainedDueToMultiplePinAttempts = true;
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
                	isAuthorizationSuccesful = false;
                    atm.getCardReader().ejectCard();
                    if(!atm.checkCashAvailability()) {
                    	state = FINAL_STATE;
                    	atm.state = 3;
                        break;
                    }
                    state = FINAL_STATE;
                    
                    break;
            }
        }
    }


}
