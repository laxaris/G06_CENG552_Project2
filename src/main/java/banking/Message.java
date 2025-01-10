package banking;

public class Message {
    public Message(int messageCode, Card card, int pin,
            int serialNumber, int fromAccount, int toAccount, Money amount) {
        this.messageCode = messageCode;
        this.card = card;
        this.pin = pin;
        this.serialNumber = serialNumber;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String toString() {
        String result = "";
        switch (messageCode) {
            case WITHDRAWAL:
                result += "WITHDRAW";
                break;
            case COMPLETE_DEPOSIT:
                result += "COMP_DEP";
                break;
            case TRANSFER:
                result += "TRANSFER";
                break;
            case INQUIRY:
                result += "INQUIRY ";
                break;
        }
        result += " CARD# " + card.getNumber();
        result += " TRANS# " + serialNumber;
        if (fromAccount >= 0)
            result += " FROM " + fromAccount;
        else
            result += " NO FROM";
        if (toAccount >= 0)
            result += " TO " + toAccount;
        else
            result += " NO TO";
        if (!amount.lessEqual(new Money(0)))
            result += " " + amount;
        else
            result += " NO AMOUNT";
        return result;
    }

    public void setPIN(int pin) {
        this.pin = pin;
    }

    public int getMessageCode() {
        return messageCode;
    }

    public Card getCard() {
        return card;
    }

    public int getPIN() {
        return pin;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public Money getAmount() {
        return amount;
    }

    public static final int WITHDRAWAL = 0;
    public static final int INITIATE_DEPOSIT = 1;
    public static final int COMPLETE_DEPOSIT = 2;
    public static final int TRANSFER = 3;
    public static final int INQUIRY = 4;
    private int messageCode;
    private Card card;
    private int pin;
    private int serialNumber;
    private int fromAccount;
    private int toAccount;
    private Money amount;
}
