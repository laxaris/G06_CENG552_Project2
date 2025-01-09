package atm;

import java.util.Enumeration;
import banking.Balances;
import banking.Receipt;

public class ReceiptPrinter {
    public ReceiptPrinter() {
    }

    public void printReceipt(Receipt receipt) {
        Enumeration receiptLines = receipt.getLines();
        while (receiptLines.hasMoreElements()) {
            System.out.println((String) receiptLines.nextElement());
        }
    }
}
