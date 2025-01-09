package banking;

public class Account {
    private int accountNumber;
    private String password;
    private double balance;
    private int accountType;

    public Account(int accountNumber, String password, double balance, int accountType) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = balance;
        this.accountType = accountType;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
}
