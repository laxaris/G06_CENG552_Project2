package banking;

public class Card {
    public Card(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    private int number;

    public int getBankNumber() {
        String num = Integer.toString(number);
        return Integer.parseInt(num.substring(0, 4));
    }

    public int getAccountNumber() {
        String num = Integer.toString(number);
        return Integer.parseInt(num.substring(4));
    }
}
