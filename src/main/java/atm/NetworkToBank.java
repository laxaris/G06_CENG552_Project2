package atm;

import java.net.InetAddress;
import banking.*;
import banking.Account;
import banking.DatabaseProxy;
import banking.Message;
import banking.Balances;
import banking.Card;
import banking.Status;
import banking.Money;

public class NetworkToBank {
    private Log log;
    private InetAddress bankAddress;
    private ATM atm;

    public NetworkToBank(Log log, InetAddress bankAddress,ATM atm) {
        this.log = log;
        this.bankAddress = bankAddress;
        this.atm = atm;
    }


    public void openConnection() {
        System.out.println("[NETWORK] Connection to bank opened at " + bankAddress);
    }


    public void closeConnection() {
        System.out.println("[NETWORK] Connection to bank closed.");
    }

    public Status sendMessage(Message message, Balances balances) {
        log.logSend(message);
        Status result;

        int accountNumber = message.getCard().getAccountNumber();
        double balance = DatabaseProxy.getBalance(accountNumber);
        double available = DatabaseProxy.getAvailableBalance(accountNumber);

        balances.setBalances(new Money((int) balance), new Money((int) available));

        switch (message.getMessageCode()) {
            case Message.WITHDRAWAL:
                result = handleWithdrawal(message,balances);
                break;
            case Message.INITIATE_DEPOSIT:
                result = Status.success();
                break;
            case Message.COMPLETE_DEPOSIT:
                result = handleDeposit(message);
                break;
            case Message.TRANSFER:
                result = handleTransfer(message,balances);
                break;
            case Message.INQUIRY:
                result = handleInquiry(message);
                break;
            default:
                result = Status.failure("Unknown transaction code.");
        }

        log.logResponse(result);
        return result;
    }


    public boolean sendAuthorizationRequest(Card card, int pin) {
        if (!DatabaseProxy.verifyPassword(card.getAccountNumber(), String.valueOf(pin))) {
            return false;
        }
        return true;
    }
    
    public void logValidCard(Card card) {
    	log.logCardInfo(card);
    }


    private Status handleWithdrawal(Message message,Balances balances) {

        int accountNumber = message.getCard().getAccountNumber();
        Money amount = message.getAmount();

        if(atm.getCashDispenser().checkCashOnHand(amount)){
            return Status.failure("Insufficient cash on hand.");
        }

        if (DatabaseProxy.withdraw(accountNumber, amount.toDouble())) {
            double balance = DatabaseProxy.getBalance(accountNumber);
            double available = DatabaseProxy.getAvailableBalance(accountNumber);
    
            balances.setBalances(new Money((int) balance), new Money((int) available));
            return Status.success();
        }
        return Status.failure("Insufficient balance.");
    }

 
    private Status handleDeposit(Message message) {
        int accountNumber = message.getCard().getAccountNumber();
        Money amount = message.getAmount();

        DatabaseProxy.deposit(accountNumber, amount.toDouble());
        return Status.success();
    }


    private Status handleTransfer(Message message,Balances balances) {
        int fromAccount = message.getFromAccount();
        int toAccount = message.getToAccount();
        Money amount = message.getAmount();

        if (DatabaseProxy.transfer(fromAccount, toAccount, amount.toDouble())) {
            double balance = DatabaseProxy.getBalance(fromAccount);
            double available = DatabaseProxy.getAvailableBalance(fromAccount);
    
            balances.setBalances(new Money((int) balance), new Money((int) available));
            return Status.success();
        }
        return Status.failure("Transfer failed. Check balance and account details.");
    }


    private Status handleInquiry(Message message) {

        return Status.success();
    }
}
