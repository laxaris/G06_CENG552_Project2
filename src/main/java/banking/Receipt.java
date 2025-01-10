package banking;

import java.util.Date;
import java.util.Enumeration;

import atm.ATM;
import transaction.Transaction;

public abstract class Receipt {
    private String[] headingPortion;
    protected String[] detailsPortion;
    private String[] balancesPortion;

    protected Receipt(ATM atm, Card card, Transaction transaction, Balances balances) {
        headingPortion = new String[4];
        headingPortion[0] = new Date().toString();
        headingPortion[1] = atm.getBranch().getBank().getName();
        headingPortion[2] = "ATM #" + atm.getID() + " " + atm.getPlace();
        headingPortion[3] = "CARD " + card.getNumber() +
                " TRANS #" + transaction.getSerialNumber();
        balancesPortion = new String[2];
        balancesPortion[0] = "TOTAL BAL: " + balances.getTotal();
        balancesPortion[1] = "AVAILABLE TO WITHDRAW: " + balances.getAvailable();
    }

    public Enumeration getLines() {
        return new Enumeration() {
            private int portion = 0;
            private int index = 0;

            public boolean hasMoreElements() {
                return portion < 2 || index < balancesPortion.length;
            }

            public Object nextElement() {
                String line = null;
                switch (portion) {
                    case 0:
                        line = headingPortion[index++];
                        if (index >= headingPortion.length) {
                            portion++;
                            index = 0;
                        }
                        break;
                    case 1:
                        line = detailsPortion[index++];
                        if (index >= detailsPortion.length) {
                            portion++;
                            index = 0;
                        }
                        break;
                    case 2:
                        line = balancesPortion[index++];
                        break;
                }
                return line;
            }
        };
    }
}
