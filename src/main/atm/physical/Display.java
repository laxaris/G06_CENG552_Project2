package atm.physical;

import java.util.Scanner;
import banking.Money;

public class Display {
    private Scanner scanner;

    public Display() {
        scanner = new Scanner(System.in);
    }

    public void showMessage(String message) {
        System.out.println("[DISPLAY] " + message);
    }

    public int readPIN(String prompt) throws Cancelled {
        System.out.println("[DISPLAY] " + prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new Cancelled();
        }
        return Integer.parseInt(input);
    }

    public int readMenuChoice(String prompt, String[] menu) throws Cancelled {
        System.out.println("[DISPLAY] " + prompt);
        for (int i = 0; i < menu.length; i++) {
            System.out.println(" " + (i + 1) + ") " + menu[i]);
        }
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new Cancelled();
        }
        return Integer.parseInt(input) - 1;
    }

    public Money readAmount(String prompt) throws Cancelled {
        System.out.println("[DISPLAY] " + prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new Cancelled();
        }
        double val = Double.parseDouble(input);
        int dollars = (int) val;
        int cents = (int) Math.round((val - dollars) * 100);
        return new Money(dollars, cents);
    }

    public static class Cancelled extends Exception {
        public Cancelled() {
            super("Cancelled by user");
        }
    }
}
