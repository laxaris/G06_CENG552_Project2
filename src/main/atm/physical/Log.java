package atm.physical;

import banking.Message;
import banking.Money;
import banking.Status;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
    private static final String LOG_FILE_PATH = "logfile.txt";

    public Log() {
    }

    public void logSend(Message message) {
        String logMessage = "[LOG] Sending: " + message;
        System.out.println(logMessage);
        writeToFile(logMessage);
    }

    public void logResponse(Status response) {
        String logMessage = "[LOG] Response: " + response;
        System.out.println(logMessage);
        writeToFile(logMessage);
    }

    public void logCashDispensed(Money amount) {
        String logMessage = "[LOG] Cash dispensed: " + amount;
        System.out.println(logMessage);
        writeToFile(logMessage);
    }



    private void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
