package banking;

import java.util.HashMap;
import java.util.Map;

public class DatabaseProxy {
    private static Map<Integer, Account> accounts = new HashMap<>();
    private static Map<Integer, Money> dailyLimits = new HashMap<>();

    // Hesap ekleme
    public static void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    // Hesap bilgilerini alma
    public static Account getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public static void setDailyLimits(Money dailyLimit) {
        for (Integer accountNumber : accounts.keySet()) {
            dailyLimits.put(accountNumber, new Money(dailyLimit));
        }
    }

    public static double getAvailableBalance(int accountNumber) {
        return dailyLimits.get(accountNumber).toDouble();

    }

    public static Money getDailyLimit(int accountNumber) {
        return dailyLimits.get(accountNumber);
    }

    // Şifre doğrulama
    public static boolean verifyPassword(int accountNumber, String password) {
        Account account = accounts.get(accountNumber);
        return account != null && account.getPassword().equals(password);
    }

    // Hesap bakiyesi kontrolü
    public static double getBalance(int accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            return account.getBalance();
        }
        throw new IllegalArgumentException("Account does not exist.");
    }

    // Para çekme işlemi
    public static boolean withdraw(int accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            dailyLimits.get(accountNumber).substract(new Money((int) amount));
            return true;
        }
        return false;
    }

    // Para yatırma işlemi
    public static void deposit(int accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
        }
    }


    private static boolean transferWithdraw(int accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            return true;
        }
        return false;
    }

    // Transfer işlemi
    public static boolean transfer(int fromAccount, int toAccount, double amount) {
        if (transferWithdraw(fromAccount, amount)) {
            deposit(toAccount, amount);
            return true;
        }
        return false;
    }

    // Maksimum günlük limit kontrolü
    public static boolean checkDailyLimit(int accountNumber, double amount, double dailyLimit) {
        return getBalance(accountNumber) >= amount && amount <= dailyLimit;
    }
}
