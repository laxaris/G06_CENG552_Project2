package atm;


import banking.Card;

public class CardReader {
    private ATM atm;

    public CardReader(ATM atm) {
        this.atm = atm;

    }

    public Card readCard() throws Display.Cancelled {
        atm.getDisplay().showMessage("[CARD READER] Enter your 10-digit card number followed by the expiry date (e.g., 1234567890 01-24):");
        String input = atm.getDisplay().readString().trim();
        
        // Kullanıcı iptali kontrolü
        if (input.equalsIgnoreCase("cancel")) {
            System.out.println("girdim");
            throw new Display.Cancelled();
        }


        String[] parts = input.split("\\s+"); 
        if (parts.length != 2) {
            System.out.println("[ERROR] Invalid input format. Please provide both card number and expiry date.");
            return null;
        }

        String cardNumber = parts[0];
        String expiryDate = parts[1];

    
        if (cardNumber.length() != 10 || !cardNumber.matches("\\d+")) {
            System.out.println("[ERROR] Invalid card number format.");
            return null;
        }

     
        Card card = new Card(Integer.parseInt(cardNumber), expiryDate);
        if (!validateCard(card) || card.isExpired()) {
            System.out.println("[ERROR] Invalid or expired card.");
            return null;
        }

        return card;
    }

    
    private boolean validateCard(Card card) {
        return card.getAccountNumber() > 0; 
    }
    

    public void ejectCard() {
    	atm.getDisplay().showMessage("[CARD READER] Card ejected.");
    }

    public void retainCard() {
    	atm.getDisplay().showMessage("[CARD READER] Card retained.");
    }
}
