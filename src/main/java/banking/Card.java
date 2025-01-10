package banking;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Card {
    private int number;
    private LocalDate expiryDate;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-yy");

    public Card(int number, String expiryDate) {
        this.number = number;
        setExpiryDate(expiryDate);
    }

    public Card(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getBankNumber() {
        String num = Integer.toString(number);
        return Integer.parseInt(num.substring(0, 4));
    }

    public int getAccountNumber() {
        String num = Integer.toString(number);
        return Integer.parseInt(num.substring(4));
    }

    public void setExpiryDate(String expiryDate) {
        try {
 
            YearMonth ym = YearMonth.parse(expiryDate, DATE_FORMATTER);
            
     
            int year = 2000 + ym.getYear() % 100;
            
         
            this.expiryDate = LocalDate.of(year, ym.getMonth(), ym.lengthOfMonth());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid expiry date format. Use MM-yy format (e.g., 01-28)", e);
        }
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
}