package banking;

public class Money {
    public Money(int dollars) {
        this(dollars, 0);
    }

    public Money(int dollars, int cents) {
        this.cents = 100L * dollars + cents;
    }

    public Money(Money toCopy) {
        this.cents = toCopy.cents;
    }

    public double toDouble() {
        return cents / 100.0;
    }

    public String toString() {
        return "$" + cents / 100 +
                (cents % 100 >= 10 ? "." + cents % 100 : ".0" + cents % 100);
    }

    public void add(Money amountToAdd) {
        this.cents += amountToAdd.cents;
    }

    

    public boolean greaterEqual(Money compareTo) {
        return this.cents >= compareTo.cents;
    }

    public boolean lessEqual(Money compareTo) {
        return this.cents <= compareTo.cents;
    }

    public void substract(Money amountToSubtract) {
        this.cents -= amountToSubtract.cents;
    }

    public boolean lessFrom(Money compareTo) {
        return this.cents < compareTo.cents;
    }

    private long cents;
}
