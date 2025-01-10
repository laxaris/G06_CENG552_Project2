package atm;

import java.net.InetAddress;
import banking.*;


public class ATM implements Runnable {
    private int id;
    private Branch branch;
    
    private OperatorPanel operatorPanel;
    private InetAddress bankAddress;
    private CardReader cardReader;
    private CashDispenser cashDispenser;
    private Display display;
    private Log log;
    private NetworkToBank networkToBank;
    private ReceiptPrinter receiptPrinter;
    protected int state;
    private boolean switchOn;
    private Money maxWithdrawPerDay;
    private Money maxWithdrawPerTransaction;
    private Money minCashToAllowTransaction;
    private Session session;

    private static final int OFF_STATE = 0;
    private static final int IDLE_STATE = 1;
    private static final int SERVING_CUSTOMER_STATE = 2;
	protected static final int ERROR_STATE = 3;

    public ATM(int id, Branch branch, InetAddress bankAddress) {
    	this.display = new Display();
        this.id = id;
        this.branch = branch;
        		
        this.bankAddress = bankAddress;
        this.operatorPanel = new OperatorPanel(this);
        this.log = new Log();
        this.cardReader = new CardReader(this);
        this.cashDispenser = new CashDispenser(log);
        
        this.networkToBank = new NetworkToBank(log, bankAddress,this);
        this.receiptPrinter = new ReceiptPrinter();
        this.state = OFF_STATE;
        this.switchOn = false;
    }

    private boolean validateOperatorPanelSettings() {
        return operatorPanel.getInitialCash() != null &&
               getMaxWithdrawPerTransaction() != null &&
               getMaxWithdrawPerDay() != null &&
               getMinCashToAllowTransaction() != null;
    }

    public boolean checkCashAvailability() {
        return cashDispenser.getCashOnHand().greaterEqual(getMinCashToAllowTransaction());
    }

    public void run() {

            while(true) {
            switch (state) {
            	case ERROR_STATE:
            		break;

            	
                case OFF_STATE:
                	
                    display.showMessage("ATM is OFF. Press ENTER to switch ON.");
                    
      
                    if (display.readEnter()) {
                        performStartup();
                    }
                    break;
                    
                case IDLE_STATE:
                	
                    display.showMessage("Waiting for card insertion...");
                    try{
                    session = new Session(this);
                    		session.performSession();}
                    catch(Display.Cancelled e){
                        display.showMessage("[ERROR] Operation Cancelled.");
                        state = OFF_STATE;
                        break;
                    }
                    if(this.state == ERROR_STATE) {
                        System.out.println("Error");
                        break;
                    }
                    else{

                    state = SERVING_CUSTOMER_STATE;
                    break;}
                case SERVING_CUSTOMER_STATE:
                    state = IDLE_STATE;
                    break;
            }}
        
    }
    
    public Session getSession() {
    	return session;
    }

    public void performStartup() {
        try {
            int choice = display.readMenuChoice(
                    "[ATM] Press '1' for Operator Panel or '2' to continue without configuration.",
                    new String[]{"Enter Operator Panel", "Set ATM to IDLE mode"}
            );

            if (choice == 0) {
                operatorPanel.showMenu();
                if (!validateOperatorPanelSettings() ) {
                    display.showMessage("[ERROR] Invalid settings or insufficient cash.");
                    state = OFF_STATE;
                    return;
                }
            }
            if (!validateOperatorPanelSettings() ) {
                display.showMessage("[ERROR] Invalid settings or insufficient cash.");
                state = OFF_STATE;
                return;
            }

            cashDispenser.setInitialCash(operatorPanel.getInitialCash());
            networkToBank.openConnection();
            DatabaseProxy.setDailyLimits(getMaxWithdrawPerDay());
            state = IDLE_STATE;
        } catch (Display.Cancelled e) {
            display.showMessage("[ERROR] Operation Cancelled.");
            state = OFF_STATE;
        }
    }
    
    
    public int getState() {
    	return state;
    }
    public int getBankCode() { return branch.getBank().getBankCode(); }
    public int getID() { return id; }
    public Bank getPlace() { return branch.getBank(); }
    public Branch getBranch() { return branch; }
    public CardReader getCardReader() { return cardReader; }
    public CashDispenser getCashDispenser() { return cashDispenser; }
    public Display getDisplay() { return display; }
    public Log getLog() { return log; }
    public NetworkToBank getNetworkToBank() { return networkToBank; }
    public ReceiptPrinter getReceiptPrinter() { return receiptPrinter; }

	public Money getMaxWithdrawPerDay() {
		return maxWithdrawPerDay;
	}

	public void setMaxWithdrawPerDay(Money maxWithdrawPerDay) {
		this.maxWithdrawPerDay = maxWithdrawPerDay;
	}

	public Money getMaxWithdrawPerTransaction() {
		return maxWithdrawPerTransaction;
	}

	public void setMaxWithdrawPerTransaction(Money maxWithdrawPerTransaction) {
		this.maxWithdrawPerTransaction = maxWithdrawPerTransaction;
	}

	public Money getMinCashToAllowTransaction() {
		return minCashToAllowTransaction;
	}

	public void setMinCashToAllowTransaction(Money minCashToAllowTransaction) {
		this.minCashToAllowTransaction = minCashToAllowTransaction;
	}
}
