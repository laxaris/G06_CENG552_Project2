package atm;

import java.util.Scanner;
import banking.Card;

public class CardReader {
    private ATM atm;
    private Scanner scanner;

    public CardReader(ATM atm) {
        this.atm = atm;
        this.scanner = new Scanner(System.in);
    }

    public Card readCard() throws Display.Cancelled {
        System.out.println("[CARD READER] Enter your 10-digit card number:");
        String input = scanner.nextLine().trim();
        
        if(input.compareTo("cancel") == 0){
            System.out.println("girdim");
            throw new Display.Cancelled();
        }

        if (input.length() != 10 || !input.matches("\\d+")) {
            System.out.println("[ERROR] Invalid card number format.");
            return null;
        }
    
        Card card = new Card(Integer.parseInt(input));
        if (!validateCard(card)) {
            System.out.println("[ERROR] Invalid or expired card.");
            return null;
        }
        return card;
    }
    
    private boolean validateCard(Card card) {
        return card.getAccountNumber() > 0; 
    }
    

    public void ejectCard() {
        System.out.println("[CARD READER] Card ejected.");
    }

    public void retainCard() {
        System.out.println("[CARD READER] Card retained.");
    }
}
