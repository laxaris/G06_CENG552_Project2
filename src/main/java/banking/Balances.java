package banking;

public class Balances {
    public Balances() {
    }

    public void setBalances(Money total, Money available) {
        this.total = total;
        this.available = available;
    }

    public Money getTotal() {
        return total;
    }

    public Money getAvailable() {
        return available;
    }

    private Money total;
    private Money available;
}
