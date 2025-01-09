package atm;

import banking.Money;

public class OperatorPanel {
    private ATM atm;


    public OperatorPanel(ATM atm) {
        this.atm = atm;
    }

    public void showMenu() {
        while (true) {
            try {
                int choice = atm.getDisplay().readMenuChoice(
                    "[OPERATOR PANEL] Choose an option:",
                    new String[] {
                        "Set initial total cash (t)",
                        "Set max withdraw per day (k)",
                        "Set max withdraw per transaction (m)",
                        "Set min cash in ATM (n)",
                        "Exit Operator Panel"
                    }
                );
                if (choice == 0) {
                    Money t = atm.getDisplay().readAmount("[OPERATOR PANEL] Enter initial total cash (t): ");
                    atm.getCashDispenser().setInitialCash(t);
                    atm.getDisplay().showMessage("[OPERATOR PANEL] initialCash set to " + t);
                } else if (choice == 1) {
                    Money k = atm.getDisplay().readAmount("[OPERATOR PANEL] Enter max withdraw per day (k): ");
                    atm.setMaxWithdrawPerDay(k);
                    atm.getDisplay().showMessage("[OPERATOR PANEL] maxWithdrawPerDay set to " + k);
                } else if (choice == 2) {
                    Money m = atm.getDisplay().readAmount("[OPERATOR PANEL] Enter max withdraw per transaction (m): ");
                    atm.setMaxWithdrawPerTransaction(m);
                    atm.getDisplay().showMessage("[OPERATOR PANEL] maxWithdrawPerTransaction set to " + m);
                } else if (choice == 3) {
                    Money n = atm.getDisplay().readAmount("[OPERATOR PANEL] Enter min cash in ATM (n) to allow transactions: ");
                    atm.setMinCashToAllowTransaction(n);
                    atm.getDisplay().showMessage("[OPERATOR PANEL] minCashToAllowTransaction set to " + n);
                } else if (choice == 4) {
                    atm.getDisplay().showMessage("[OPERATOR PANEL] Exiting...");
                    break;
                }
                else{
                    Money money  = new Money(1000);
                    atm.getCashDispenser().setInitialCash(new Money(10000));
                    atm.setMaxWithdrawPerDay(money);
                    atm.setMaxWithdrawPerTransaction(money);
                    atm.setMinCashToAllowTransaction(new Money(1));
                    break;
                }
            } catch (Display.Cancelled e) {
                atm.getDisplay().showMessage("[OPERATOR PANEL] Cancelled or empty input. Returning to main menu...");
                break;
            }
            catch (Exception e ){
                atm.getDisplay().showMessage("[OPERATOR PANEL] Invalid input. Please try again.");
            }

        }
    }

    public Money getInitialCash() {
        return atm.getCashDispenser().getCashOnHand();
    }




}
