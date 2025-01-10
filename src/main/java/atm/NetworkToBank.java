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
    private int cardAuthorizationState = 0;
    private static final int NONE = 0;
    private static final int BAD_BANK_CODE = 1;
    private static final int BAD_ACCOUNT_NUMBER = 2;

    public NetworkToBank(Log log, InetAddress bankAddress, ATM atm) {
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
                result = handleWithdrawal(message, balances);
                break;
            case Message.INITIATE_DEPOSIT:
                result = Status.success();
                break;
            case Message.COMPLETE_DEPOSIT:
                result = handleDeposit(message);
                break;
            case Message.TRANSFER:
                result = handleTransfer(message, balances);
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
        if (card.getBankNumber() != atm.getBankCode()) {
            cardAuthorizationState = BAD_BANK_CODE;
            atm.getDisplay().showMessage("[ERROR] The cash card is not supported by this ATM. Please contact your bank.");
            return false;
        }

        if (!DatabaseProxy.hasAccount(card.getAccountNumber())) {
            cardAuthorizationState = BAD_ACCOUNT_NUMBER;
            atm.getDisplay().showMessage("[ERROR] Invalid account number detected. Please check your card or contact your bank.");
            return false;
        }

        if (!DatabaseProxy.verifyPassword(card.getAccountNumber(), String.valueOf(pin))) {
            atm.getDisplay().showMessage("[ERROR] Incorrect PIN. Please try again.");
            return false;
        }
        
        logValidCard(card);
        return true;
    }

    public void logValidCard(Card card) {
        log.logCardInfo(card);
    }

    private Status handleWithdrawal(Message message, Balances balances) {
        int accountNumber = message.getCard().getAccountNumber();
        Money amount = message.getAmount();

                if (amount.greaterEqual(atm.getMaxWithdrawPerTransaction())) {
            return Status.failure("[ERROR] Exceeds maximum withdrawal per transaction.");
        }
        if (amount.lessFrom(atm.getMinCashToAllowTransaction())) {
            return Status.failure("[ERROR] Amount is less than the minimum cash allowed for a transaction.");
        }


        if (atm.getCashDispenser().checkCashOnHand(amount)) {
            return Status.failure("[ERROR] Insufficient cash in the ATM.");
        }


        if (DatabaseProxy.withdraw(accountNumber, amount.toDouble())) {
            atm.getCashDispenser().dispenseCash(amount);
            double balance = DatabaseProxy.getBalance(accountNumber);
            double available = DatabaseProxy.getAvailableBalance(accountNumber);
            balances.setBalances(new Money((int) balance), new Money((int) available));
            return Status.success();
        }
        return Status.failure("[ERROR] Insufficient account balance.");
    }

    private Status handleDeposit(Message message) {
        int accountNumber = message.getCard().getAccountNumber();
        Money amount = message.getAmount();

        if (amount.lessFrom(new Money(atm.getMinCashToAllowTransaction()))) {
            return Status.failure("[ERROR] Deposit amount is below the minimum transaction limit.");
        }

        DatabaseProxy.deposit(accountNumber, amount.toDouble());
        atm.getCashDispenser().addCash(amount);
        return Status.success();
    }

    private Status handleTransfer(Message message, Balances balances) {
        int fromAccount = message.getFromAccount();
        int toAccount = message.getToAccount();
        Money amount = message.getAmount();

        if (amount.greaterEqual(atm.getMaxWithdrawPerTransaction())) {
            return Status.failure("[ERROR] Transfer exceeds maximum transaction limit.");
        }
        if (amount.lessFrom(atm.getMinCashToAllowTransaction())) {
            return Status.failure("[ERROR] Transfer amount below minimum transaction limit.");
        }

        if (DatabaseProxy.transfer(fromAccount, toAccount, amount.toDouble())) {
            double balance = DatabaseProxy.getBalance(fromAccount);
            double available = DatabaseProxy.getAvailableBalance(fromAccount);
            balances.setBalances(new Money((int) balance), new Money((int) available));
            return Status.success();
        }
        return Status.failure("[ERROR] Transfer failed. Insufficient balance or invalid account.");
    }

    private Status handleInquiry(Message message) {
        atm.getDisplay().showMessage("[INFO] Balance inquiry successful.");
        return Status.success();
    }

    public int getCardAuthorizationCode() {
        return cardAuthorizationState;
    }
}
