package atm;

import java.net.InetAddress;
import banking.*;

import java.util.Scanner;

public class ATM implements Runnable {
    private int id;
    private String place;
    private String bankName;
    private OperatorPanel operatorPanel;
    private InetAddress bankAddress;
    private CardReader cardReader;
    private CashDispenser cashDispenser;
    private Display display;
    private Log log;
    private NetworkToBank networkToBank;
    private ReceiptPrinter receiptPrinter;
    private int state;
    private boolean switchOn;
    protected Money maxWithdrawPerDay;
    protected Money maxWithdrawPerTransaction;
    protected Money minCashToAllowTransaction;

    private static final int OFF_STATE = 0;
    private static final int IDLE_STATE = 1;
    private static final int SERVING_CUSTOMER_STATE = 2;

    public ATM(int id, String place, String bankName, InetAddress bankAddress) {
        this.id = id;
        this.place = place;
        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.operatorPanel = new OperatorPanel(this);
        this.log = new Log();
        this.cardReader = new CardReader(this);
        this.cashDispenser = new CashDispenser(log);
        this.display = new Display();
        this.networkToBank = new NetworkToBank(log, bankAddress,this);
        this.receiptPrinter = new ReceiptPrinter();
        this.state = OFF_STATE;
        this.switchOn = false;
    }

    private boolean validateOperatorPanelSettings() {
        return operatorPanel.getInitialCash() != null &&
               maxWithdrawPerTransaction != null &&
               maxWithdrawPerDay != null &&
               minCashToAllowTransaction != null;
    }

    private boolean checkCashAvailability() {
        return cashDispenser.getCashOnHand().greaterEqual(minCashToAllowTransaction);
    }

    public void run() {
        while (true) {
            
            switch (state) {
                case OFF_STATE:
                    display.showMessage("ATM is OFF. Press ENTER to switch ON.");
                    Scanner scanner = new Scanner(System.in);
                    if (scanner.nextLine().isEmpty()) {
                        performStartup();
                    }
                    break;
                case IDLE_STATE:
             
                    display.showMessage("Waiting for card insertion...");
                    try{
                    new Session(this).performSession();}
                    catch(Display.Cancelled e){
                        display.showMessage("[ERROR] Operation Cancelled.");
                        state = OFF_STATE;
                        break;
                    }
                    state = SERVING_CUSTOMER_STATE;
                    break;
                case SERVING_CUSTOMER_STATE:
                    state = IDLE_STATE;
                    break;
            }
        }
    }

    private void performStartup() {
        try {
            int choice = display.readMenuChoice(
                    "[ATM] Press '1' for Operator Panel or '2' to continue without configuration.",
                    new String[]{"Enter Operator Panel", "Set ATM to IDLE mode"}
            );

            if (choice == 0) {
                operatorPanel.showMenu();
                if (!validateOperatorPanelSettings() || !checkCashAvailability()) {
                    display.showMessage("[ERROR] Invalid settings or insufficient cash.");
                    state = OFF_STATE;
                    return;
                }
            }
            if (!validateOperatorPanelSettings() || !checkCashAvailability()) {
                display.showMessage("[ERROR] Invalid settings or insufficient cash.");
                state = OFF_STATE;
                return;
            }

            cashDispenser.setInitialCash(operatorPanel.getInitialCash());
            networkToBank.openConnection();
            DatabaseProxy.setDailyLimits(maxWithdrawPerDay);
            state = IDLE_STATE;
        } catch (Display.Cancelled e) {
            display.showMessage("[ERROR] Operation Cancelled.");
            state = OFF_STATE;
        }
    }

    public int getID() { return id; }
    public String getPlace() { return place; }
    public String getBankName() { return bankName; }
    public CardReader getCardReader() { return cardReader; }
    public CashDispenser getCashDispenser() { return cashDispenser; }
    public Display getDisplay() { return display; }
    public Log getLog() { return log; }
    public NetworkToBank getNetworkToBank() { return networkToBank; }
    public ReceiptPrinter getReceiptPrinter() { return receiptPrinter; }
}
