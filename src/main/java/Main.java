import java.net.InetAddress;

import atm.ATM;
import banking.Account;
import banking.DatabaseProxy;
import banking.Money;


public class Main {
    public static void main(String[] args) {
        try {
            Account account1 = new Account(123456, "1234", 50000.0, 0);
            Account account2 = new Account(111222, "9999", 1000.0, 0);
            
            DatabaseProxy.addAccount(account1);
            DatabaseProxy.addAccount(account2);

            InetAddress bankAddress = InetAddress.getByName("127.0.0.1");
            ATM atm = new ATM(1, "Main Branch", "MyBank", bankAddress);
            
            atm.getCashDispenser().setInitialCash(new Money(10000));
            Thread atmThread = new Thread(atm);
            atmThread.start();

            
            


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
