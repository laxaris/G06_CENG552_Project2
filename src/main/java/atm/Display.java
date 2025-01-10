package atm;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import banking.Money;

public class Display {
    private Scanner scanner;
    private boolean testMode = false;
    private Queue<String> testInputs;
    private String lastShowedMessage;
    private List<String> testModeMessages; 

    public Display() {
        scanner = new Scanner(System.in);
        testInputs = new LinkedList<>();
        testModeMessages = new ArrayList<>();
    }

    public void setTestMode(boolean enabled) {
        this.testMode = enabled;
        if (enabled) {
            showMessage("[INFO] Test mode activated. Inputs will be taken from the queue.");
        } else {
            showMessage("[INFO] Test mode deactivated. Manual input required.");
        }
    }

    public void setFakeInput(String... inputs) {
        synchronized (testInputs) {
            for (String input : inputs) {
                testInputs.add(input);
            }
            showMessage("[TEST MODE] Fake inputs added: " + String.join(", ", inputs));
            testInputs.notifyAll();
        }
    }

    private String getNextInput() {
        if (testMode) {
            synchronized (testInputs) {
                while (testInputs.isEmpty()) {
                    try {
                        testInputs.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "";
                    }
                }
                String nextInput = testInputs.poll();
                showMessage("[TEST MODE] Input received: " + (nextInput.isEmpty() ? "[ENTER]" : nextInput));
                return nextInput;
            }
        } else {
            return scanner.nextLine().trim();
        }
    }

    public String readString() {
        return getNextInput();
    }

    public int readPIN(String prompt) throws Cancelled {
        showMessage(prompt);
        String input = getNextInput();
        if (input.isEmpty()) {
            throw new Cancelled();
        }
        return Integer.parseInt(input);
    }

    public boolean readEnter() {
        showMessage("[DISPLAY] Press ENTER to continue...");
        return getNextInput().isEmpty();
    }

    public int readMenuChoice(String prompt, String[] menu) throws Cancelled {
        showMessage(prompt);
        for (int i = 0; i < menu.length; i++) {
            System.out.println(" " + (i + 1) + ") " + menu[i]);
        }
        String input = getNextInput();
        if (input.isEmpty()) {
            throw new Cancelled();
        }
        return Integer.parseInt(input) - 1;
    }

    public Money readAmount(String prompt) throws Cancelled {
        showMessage(prompt);
        String input = getNextInput();
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
            super("Operation Cancelled by User");
        }
    }

    public void showMessage(String message) {
        lastShowedMessage = message;
        System.out.println(message);
        
        if (testMode) {
            testModeMessages.add(message);  
        }
    }

    public String getLastDisplayedMessage() {
        return lastShowedMessage;
    }

    public String getDisplayedMessageAt(int index) {
        if (testMode) {
            if (index >= 0 && index < testModeMessages.size()) {
                return testModeMessages.get(testModeMessages.size()-index-1);
            } else {
                return "[ERROR] Invalid message index!";
            }
        } else {
            return "[ERROR] Test mode is not active!";
        }
    }
    

    public List<String> getAllTestMessages() {
        if (testMode) {
            return new ArrayList<>(testModeMessages);
        } else {
            return new ArrayList<>();
        }
    }
}
